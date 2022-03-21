# ICS / iCal

Add support for schedules from ICS / iCal files. Files can be either stored in a  local folder or fetched from a static URL. The waste type will be taken from the `summary` attribute.

This source has been successfully tested with the following service providers:

### Belgium

- [Limburg.net](https://www.limburg.net/afvalkalender) ([Example](#limburg-net))

### Germany

- [Müllabfuhr-Deutschland](https://www.muellabfuhr-deutschland.de/) ([Notes](#müllabfuhr-deutschland))

#### Baden-Württemberg

- [Abfallwirtschaftsamt Bodenseekreis](https://www.bodenseekreis.de/umwelt-landnutzung/abfallentsorgung-privat/termine/abfuhrkalender/) ([Notes](#abfallwirtschaftsamt-bodenseekreis))
- [Abfallwirtschaft Kreis Böblingen](https://www.lrabb.de/start/Service+_+Verwaltung/Abfuhrtermine.html)
- [Abfall Landkreis Stade](https://abfall.landkreis-stade.de/)
- [AVL Ludwigsburg](https://www.avl-ludwigsburg.de/) ([Example](#avl-ludwigsburg))
- [AWB Esslingen](https://www.awb-es.de/)

#### Bayern

- [AWM München](https://www.awm-muenchen.de) ([Notes](#awm-münchen))
- [Awista Starnberg](https://www.awista-starnberg.de/)

#### Hessen

- [Erlensee](https://sperrmuell.erlensee.de/?type=reminder) ([Example](#erlensee))
- [EAW Rheingau Taunus](https://www.eaw-rheingau-taunus.de/service/abfallkalender.html) ([Example](#eaw-rheingau-taunus))

#### Niedersachsen

- [Abfallkalender Zollernalbkreis](https://www.zollernalbkreis.de/landratsamt/aemter++und+organisation/Elektronischer+Abfallkalender) ([Example](#abfallkalender-zollernalbkreis))

#### Nordrhein-Westfalen

- [EDG Entsorgung Dortmund](https://www.edg.de/)

#### Sachsen

- [ASR Chemnitz](https://www.asr-chemnitz.de/kundenportal/entsorgungskalender/)
- [Stadtreinigung Leipzig](https://www.stadtreinigung-leipzig.de/)
- [Entsorgungsgesellschaft Görlitz-Löbau-Zittau](https://www.abfall-eglz.de/abfallkalender.0.html) ([Notes](#entsorgungsgesellschaft-görlitz-löbau-zittau))

### Sweden

- [NSR Nordvästra Skåne](https://nsr.se/privat/allt-om-din-sophamtning/nar-toms-mitt-karl/tomningskalender/)

### United States of America

- [ReCollect.net](https://recollect.net) ([Notes](#recollect))
- [Western Disposal Residential (Colorado)](https://www.westerndisposal.com/residential/) (Unofficial, [Notes](#western-disposal-colorado))

## Configuration via configuration.yaml

```yaml
waste_collection_schedule:
  sources:
    - name: ics
      args:
        url: URL
        file: FILE
        offset: OFFSET
        method: METHOD
        params: PARAMS
        year_field: YEAR_FIELD
        split_at: SPLIT_AT
        version: 2
```

### Configuration Variables

**url**<br>
*(string) (optional)*

URL to ICS / iCal file. File will be downloaded using a HTTP GET request.

If the original url contains the current year (4 digits including century), this can be replaced by the wildcard `{%Y}` (see example below).

You have to specify either `url` or `file`!

**file**<br>
*(string) (optional)*

Local ICS / iCal file name. Can be used instead of `url` for local files.

You have to specify either `url` or `file`!

**offset**<br>
*(int) (optional, default: `0`)*

Offset in days which will be added to every start time. Can be used if the start time of the events in the ICS file are ahead of the actual date.

**method**<br>
*(string) (optional, default: `GET`)*

Method to send the URL `params`.

Need to be `GET` or `POST`.

**params**<br>
*(dict) (optional, default: None)*

Dictionary, list of tuples or bytes to send in the query string for the HTTP request.

This gets

- urlencoded and either attached to the raw URL when GET method is used.
- send with `Content-Type: application/x-www-form-urlencoded` and an automatically generated `Content-Length` header as POST method HTTP body.

Only used if `url` is specified, not used for `file`.

**year_field**<br>
*(string) (optional, default: None)*

Field in params dictionary to be replaced with current year (4 digits including century).

**split_at**<br>
*(string) (optional, default: None)*

Delimiter to split event summary into individual collection types. If your service puts multiple collections types which occur at the same day into a single event, this option can be used to separate the collection types again.

**version**<br>
*(integer) (optional, default: 2)*

Selects the underlying ICS file parser:

- version: 1 uses `recurring_ical_events`
- version: 2 uses `icalevents`

## Examples and Notes

***

### Local file

```yaml
waste_collection_schedule:
  sources:
    - name: ics
      args:
        file: "test.ics"
```

***

### AVL Ludwigsburg

```yaml
waste_collection_schedule:
  sources:
    - name: ics
      args:
        url: "https://www.avl-ludwigsburg.de/fileadmin/Files/Abfallkalender/ICS/Privat/Privat_{%Y}_Ossweil.ics"
        offset: 1
```

***

### Abfallwirtschaftsamt Bodenseekreis

Go to the [service provider website](https://www.bodenseekreis.de/umwelt-landnutzung/abfallentsorgung-privat/termine/abfuhrkalender/) and select location and desired waste types. Afterwards an iCal calender export is provided. Simply take this URL and replace the year with "{%Y}" and use this URL within the configuration.

```yaml
waste_collection_schedule:
  sources:
    - name: ics
      args:
        url: https://www.bodenseekreis.de/umwelt-landnutzung/abfallentsorgung-privat/termine/abfuhrkalender/export/{%Y}/salem/salem-i/1,4,5,16,7,8,10,6/ics/
      customize:
        - type: Restmüll 2-wöchentlich
          alias: Restmüll
          icon: mdi:trash-can
        - type: Bioabfall 2-wöchentlich
          alias: Bioabfall
          icon: mdi:flower-outline
        - type: Papier 4-wöchentlich
          alias: Papierabfall
          icon: mdi:trash-can-outline
        - type: Gelber Sack
          icon: mdi:recycle
```

### Abfallkalender Zollernalbkreis

```yaml
waste_collection_schedule:
  sources:
    - name: ics
      args:
        url: "https://www.abfallkalender-zak.de",
        params:
            city: 2,3,4
            street: 3
            types[]:
              - restmuell
                gelbersack
                papiertonne
                biomuell
                gruenabfall
                schadstoffsammlung
                altpapiersammlung
                schrottsammlung
                weihnachtsbaeume
                elektrosammlung
            go_ics: Download
        },
        year_field: year
```

***

### EAW Rheingau Taunus

```yaml
waste_collection_schedule:
  sources:
    - name: ics
      args:
        url: "https://www.eaw-rheingau-taunus.de/abfallkalender/calendar.ics?streetid=1429"
        split_at: ","
```

***

### ReCollect

To get the URL, search your address in the recollect form of your home town, click "Get a calendar", then "Add to iCal". Finally, the URL under "Subscribe to calendar" is your ICS calendar link:

```url
webcal://recollect.a.ssl.fastly.net/api/places/BCCDF30E-578B-11E4-AD38-5839C200407A/services/208/events.en.ics?client_id=6FBD18FE-167B-11EC-992A-C843A7F05606
```

Strip the client ID and change the protocol to https, and you have a valid ICS URL.

```yaml
waste_collection_schedule:
  sources:
    - name: ics
      args:
        url: "https://recollect.a.ssl.fastly.net/api/places/BCCDF30E-578B-11E4-AD38-5839C200407A/services/208/events.en.ics",
        split_at: "\\, [and ]*",
```

***

### Entsorgungsgesellschaft Görlitz-Löbau-Zittau

Remove the year from the generated URL to always get the current year.

***

### Müllabfuhr-Deutschland

You need to find the direct ics export link for your region, e.g. [Weimarer Land, Bad Berka](https://www.muellabfuhr-deutschland.de/weimarer-land/location/0c595d1c-2cbc-4d19-ae81-df5318fceb7c/pickups).

Known districts:

- [Burgenlandkreis](https://www.muellabfuhr-deutschland.de/burgenlandkreis)
- [Saalkreis](https://www.muellabfuhr-deutschland.de/saalekreis)
- [Sömmerda](https://www.muellabfuhr-deutschland.de/soemmerda)
- [Weimarer Land](https://www.muellabfuhr-deutschland.de/weimarer-land)

```yaml
waste_collection_schedule:
  sources:
    - name: ics
      args:
        url: https://www.muellabfuhr-deutschland.de/weimarer-land/location/0c595d1c-2cbc-4d19-ae81-df5318fceb7c/pickups/ical.ics
```

***

### AWM München

1. Find your ICS export link via the AWM web page
2. Remove the cHash attribute
3. Replace current year with {%Y}

```yaml
waste_collection_schedule:
  sources:
    - name: ics
      args:
        url: "https://www.awm-muenchen.de/entsorgen/abfuhrkalender?tx_awmabfuhrkalender_abfuhrkalender%5Bhausnummer%5D=2&tx_awmabfuhrkalender_abfuhrkalender%5Bleerungszyklus%5D%5BB%5D=1%2F2%3BG&tx_awmabfuhrkalender_abfuhrkalender%5Bleerungszyklus%5D%5BP%5D=1%2F2%3BU&tx_awmabfuhrkalender_abfuhrkalender%5Bleerungszyklus%5D%5BR%5D=001%3BG&tx_awmabfuhrkalender_abfuhrkalender%5Bsection%5D=ics&tx_awmabfuhrkalender_abfuhrkalender%5Bsinglestandplatz%5D=false&tx_awmabfuhrkalender_abfuhrkalender%5Bstandplatzwahl%5D=true&tx_awmabfuhrkalender_abfuhrkalender%5Bstellplatz%5D%5Bbio%5D=70114566&tx_awmabfuhrkalender_abfuhrkalender%5Bstellplatz%5D%5Bpapier%5D=70114566&tx_awmabfuhrkalender_abfuhrkalender%5Bstellplatz%5D%5Brestmuell%5D=70114566&tx_awmabfuhrkalender_abfuhrkalender%5Bstrasse%5D=Freimanner%20Bahnhofstr.&tx_awmabfuhrkalender_abfuhrkalender%5Byear%5D={%Y}}
```

***

### Erlensee

Just replace the street number (8 in the example below) with the number of your street. You can find the right number if you inspect the street drop-down list [here](https://sperrmuell.erlensee.de/?type=reminder).

```yaml
waste_collection_schedule:
  sources:
    - name: ics
      args:
        url: "https://sperrmuell.erlensee.de/?type=reminder"
        method: POST
        params:
          street: 8
          eventType[]:
            - 27
            - 23
            - 19
            - 20
            - 21
            - 24
            - 22
            - 25
            - 26
          timeframe: 23
          download: ical
```

***

### Limburg.net

This tool works for all municipalities of the province of Limburg and the municipality of Diest.

Find your ICS export link via the calendar page - enter your address so that the system can look up the necessary data for your city and street to construct the URL.

Generating the URL on the site of Limburg.net is the simplest.

```yaml
waste_collection_schedule:
  sources:
    - name: ics
      args:
        url: https://limburg.net/api-proxy/public/kalender-download/ical/72030?straatNummer=66536&huisNummer=1&toevoeging=&includeAllEventTypes=1&eventTypes[]=14&eventTypes[]=22&eventTypes[]=23&eventTypes[]=26&eventTypes[]=27&eventTypes[]=29
```

You can also compose the URL yourself. You need the following elements for this:
1. the nis-code of your municipality: query the api with the name of your municipality;<br>example: https://limburg.net/api-proxy/public/afval-kalender/gemeenten/search?query=Peer
```json
[{"nisCode":"72030","naam":"Peer"}]
```
2. the number of your street: query the api with the nis-code of your municipality and the name of your street;<br>example: https://limburg.net/api-proxy/public/afval-kalender/gemeente/72030/straten/search?query=Zuidervest
```json
[{"nummer":"66536","naam":"Zuidervest"}]
```
3. your housenumber

```yaml
waste_collection_schedule:
  sources:
    - name: ics
      args:
        url: "https://limburg.net/api-proxy/public/kalender-download/ical/72030"
        method: GET
        params:
          straatNummer: 66536
          huisNummer: 1
          includeAllEventTypes: 1
          eventTypes[]:
            - 14
            - 22
            - 23
            - 26
            - 27
            - 29
```

***

### Western Disposal Colorado

*Unofficial calendar* maintained by burkemw3@gmail.com

[online calendar view](https://calendar.google.com/calendar/embed?src=gn2i5lqgobo5deb6p7j69l9aq8%40group.calendar.google.com&ctz=America%2FDenver)

```yaml
sensor:
  - platform: waste_collection_schedule
    name: Trash Recycling # whatever you want the UI to show. Consider adding a similar prefix for both sensors so they get sorted together, "Trash" in this case
    types:
      - Recycling # matches alias in waste_collection_schedule below
  - platform: waste_collection_schedule
    name: Trash Compost
    types:
      - Compost

waste_collection_schedule:
  sources:
    - name: ics
      args:
        url: https://calendar.google.com/calendar/ical/gn2i5lqgobo5deb6p7j69l9aq8%40group.calendar.google.com/public/basic.ics
      customize:
        - type: Wednesday E Compost # from calendar event name
          alias: Compost # matches type in sensor configuration above
        - type: Wednesday E Recycling
          alias: Recycling
```
