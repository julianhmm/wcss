import requests
import datetime
import icalendar
from collections import OrderedDict
from pathlib import Path
import recurring_ical_events


from ..helpers import CollectionAppointment


DESCRIPTION = "Source for ICS based services"
URL = ""
TEST_CASES = OrderedDict(
    [
        (
            "Dortmund, Dudenstr. 5",
            {
                "url": "https://www.edg.de/ical/kalender.ics?Strasse=Dudenstr.&Hausnummer=5&Erinnerung=-1&Abfallart=1,2,3,4"
            },
        ),
        (
            "Leipzig, Sandgrubenweg 27",
            {
                "url": "https://www.stadtreinigung-leipzig.de/leistungen/abfallentsorgung/abfallkalender-entsorgungstermine.html&ical=true&loc=Sandgrubenweg%20%2027&lid=x38296"
            },
        ),
        (
            "Ludwigsburg",
            {
                "url": "https://www.avl-ludwigsburg.de/fileadmin/Files/Abfallkalender/ICS/Privat/Privat_{%Y}_Ossweil.ics"
            },
        ),
        (
            "Esslingen, Bahnhof",
            {
                "url": "https://api.abfall.io/?kh=DaA02103019b46345f1998698563DaAd&t=ics&s=1a862df26f6943997cef90233877a4fe"
            },
        ),
        (
            "Test File",
            {
                # Path is used here to allow to call the Source from any location.
                # This is not required in a yaml configuration!
                "file": Path(__file__)
                .resolve()
                .parents[1]
                .joinpath("test/test.ics")
            },
        ),
        (
            "Test File (recurring)",
            {
                # Path is used here to allow to call the Source from any location.
                # This is not required in a yaml configuration!
                "file": Path(__file__)
                .resolve()
                .parents[1]
                .joinpath("test/recurring.ics")
            },
        ),
        (
            "München, Bahnstr. 11",
            {
                "url": "https://www.awm-muenchen.de/index/abfuhrkalender.html?tx_awmabfuhrkalender_pi1%5Bsection%5D=ics&tx_awmabfuhrkalender_pi1%5Bstandplatzwahl%5D=true&tx_awmabfuhrkalender_pi1%5Bsinglestandplatz%5D=false&tx_awmabfuhrkalender_pi1%5Bstrasse%5D=Bahnstr.&tx_awmabfuhrkalender_pi1%5Bhausnummer%5D=11&tx_awmabfuhrkalender_pi1%5Bstellplatz%5D%5Brestmuell%5D=70024507&tx_awmabfuhrkalender_pi1%5Bstellplatz%5D%5Bpapier%5D=70024507&tx_awmabfuhrkalender_pi1%5Bstellplatz%5D%5Bbio%5D=70024507&tx_awmabfuhrkalender_pi1%5Bleerungszyklus%5D%5BR%5D=001%3BU&tx_awmabfuhrkalender_pi1%5Bleerungszyklus%5D%5BP%5D=1%2F2%3BG&tx_awmabfuhrkalender_pi1%5Bleerungszyklus%5D%5BB%5D=1%2F2%3BU&tx_awmabfuhrkalender_pi1%5Byear%5D={%Y}"
            },
        ),
        (
            "Buxtehude, Am Berg",
            {
                "url": "https://abfall.landkreis-stade.de/api_v2/collection_dates/1/ort/10/strasse/90/hausnummern/1/abfallarten/R02-R04-B02-D04-D12-P04-R12-R14-W0-R22-R24-R31/kalender.ics"
            },
        ),
    ]
)


HEADERS = {"user-agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"}


class Source:
    def __init__(self, url=None, file=None, offset=None):
        self._url = url
        self._file = file
        self._offset = offset
        if bool(self._url is not None) == bool(self._file is not None):
            raise RuntimeError("Specify either url or file")

    def fetch(self):
        if self._url is not None:
            if "{%Y}" in self._url:
                # url contains wildcard
                now = datetime.datetime.now()
                url = self._url.replace("{%Y}", str(now.year))
                entries = self.fetch_url(url)
                if now.month == 12:
                    # also get data for next year if we are already in december
                    url = self._url.replace("{%Y}", str(now.year + 1))
                    try:
                        entries.extend(self.fetch_url(url))
                    except Exception:
                        # ignore if fetch for next year fails
                        pass
                return entries
            else:
                return self.fetch_url(self._url)
        elif self._file is not None:
            return self.fetch_file(self._file)

    def fetch_url(self, url):
        # get ics file
        r = requests.get(url, headers=HEADERS)
        r.encoding = "utf-8"  # requests doesn't guess the encoding correctly

        return self._convert(r.text)

    def fetch_file(self, file):
        f = open(file, "r")
        return self._convert(f.read())

    def _convert(self, data):
        entries = []

        # parse ics file
        calendar = icalendar.Calendar.from_ical(data)

        start_date = datetime.datetime.now().replace(
            hour=0, minute=0, second=0, microsecond=0
        )
        end_date = start_date.replace(year=start_date.year + 1)

        events = recurring_ical_events.of(calendar).between(start_date, end_date)

        entries = []

        for e in events:
            if e.name == "VEVENT":
                dtstart = None
                if type(e.get("dtstart").dt) == datetime.date:
                    dtstart = e.get("dtstart").dt
                elif type(e.get("dtstart").dt) == datetime.datetime:
                    dtstart = e.get("dtstart").dt.date()
                if self._offset is not None:
                    dtstart += datetime.timedelta(days=self._offset)
                summary = str(e.get("summary"))
                entries.append(CollectionAppointment(dtstart, summary))

        return entries
