# ICS / iCal

Add support for schedules from ICS / iCal files. Files can be either stored in a  local folder or fetched from a static URL. The waste type will be taken from the `summary` attribute.

This source has been successfully tested with the following service providers:

<!--Begin of service section-->
### Belgium

- [Limburg.net](/doc/ics/limburg_net.md) / limburg.net

### Germany

- [Abfallwirtschaft Freiburg](/doc/ics/abfallwirtschaft_freiburg_de.md) / abfall-eglz.de
- [Abfallwirtschaftsbetrieb Ilm-Kreis](/doc/ics/ilm_kreis_de.md) / ilm-kreis.de
- [Abfallwirtschaftsbetrieb Landkreis Karlsruhe](/doc/ics/awb_landkreis_karlsruhe_de.md) / awb-landkreis-karlsruhe.de
- [Abfallwirtschaftsbetrieb München](/doc/ics/awm_muenchen_de.md) / awm-muenchen.de
- [Abfallwirtschaftsgesellschaft Landkreis Schaumburg](/doc/ics/aws_shg_de.md) / aws-shg.de
- [AVL - Abfallverwertungsgesellschaft des Landkreises Ludwigsburg mbH](/doc/ics/avl_ludwigsburg_de.md) / avl-ludwigsburg.de
- [Awista Starnberg](/doc/ics/awista_starnberg_de.md) / awista-starnberg.de
- [EDG Entsorgung Dortmund](/doc/ics/edg_de.md) / edg.de
- [Entsorgungsbetrieb Märkisch-Oderland](/doc/ics/entsorgungsbetrieb_mol_de.md) / entsorgungsbetrieb-mol.de
- [Entsorgungsgesellschaft Görlitz-Löbau-Zittau](/doc/ics/abfall_eglz_de.md) / abfall-eglz.de
- [FES Frankfurter Entsorgungs- und Service GmbH](/doc/ics/fes_frankfurt_de.md) / fes-frankfurt.de
- [Gelsendienste Gelsenkirchen](/doc/ics/gelsendienste_de.md) / gelsendienste.de
- [Landkreis Hameln-Pyrmont](/doc/ics/hameln_pyrmont_de.md) / hameln-pyrmont.de
- [Landkreis Northeim (unofficial)](/doc/ics/nerdbridge_de.md) / abfall.nerdbridge.de
- [Landkreis Stade](/doc/ics/landkreis_stade_de.md) / landkreis-stade.de
- [Landratsamt Bodenseekreis](/doc/ics/bodenseekreis_de.md) / bodenseekreis.de
- [Lübeck Entsorgungsbetriebe](/doc/ics/luebeck_de.md) / luebeck.de
- [Stadt Detmold](/doc/ics/detmold_de.md) / detmold.de
- [Stadt Koblenz](/doc/ics/koblenz_de.md) / koblenz.de
- [Stadt Osnabrück](/doc/ics/osnabrueck_de.md) / osnabrueck.de
- [Stadtreinigung Leipzig](/doc/ics/stadtreinigung_leipzig_de.md) / stadtreinigung-leipzig.de
- [ZAH Hildesheim](/doc/ics/zah_hildesheim_de.md) / zah-hildesheim.de
- [Zweckverband Abfallwirtschaft Region Trier (A.R.T.)](/doc/ics/art_trier_de.md) / art-trier.de

<!--End of service section-->

In addition, users reported that the following service providers are working:

### Sweden

- [NSR Nordvästra Skåne](https://nsr.se/privat/allt-om-din-sophamtning/nar-toms-mitt-karl/tomningskalender/)

### United States of America

- [ReCollect.net](https://recollect.net) ([Notes](#recollect))
- [Western Disposal Residential (Colorado)](https://www.westerndisposal.com/residential/) (Unofficial, [Notes](#western-disposal-colorado))

***

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
        regex: REGEX
        split_at: SPLIT_AT
        version: 2
        verify_ssl: VERIFY_SSL
        headers: HEADERS
```

### Configuration Variables

**url**  
*(string) (optional)*

URL to ICS / iCal file. File will be downloaded using a HTTP GET request.

If the original url contains the current year (4 digits including century), this can be replaced by the wildcard `{%Y}` (see example below).

You have to specify either `url` or `file`!

**file**  
*(string) (optional)*

Local ICS / iCal file name. Can be used instead of `url` for local files.

You have to specify either `url` or `file`!

Notes:

- Some users have reported that on their installation, only local files below the folder `config/www` are accessible by the system. Therefore place the ics file there.
- If you are using relative paths (like in the example below), the path depends on which working directory your Home Assistant instance is running on. And this might depend on the installation method (core vs supervisor vs OS vs ...). Therefore check the log output, it tells you the current working directory.

  This example should work for HAOS based installations:

  ```yaml
  # file location: config/www/calendar.ics
  waste_collection_schedule:
    sources:
      - name: ics
        args:
          file: "www/calendar.ics"
  ```

**offset**  
*(int) (optional, default: `0`)*

Offset in days which will be added to every start time. Can be used if the start time of the events in the ICS file are ahead of the actual date.

**method**  
*(string) (optional, default: `GET`)*

Method to send the URL `params`.

Need to be `GET` or `POST`.

**params**  
*(dict) (optional, default: None)*

Dictionary, list of tuples or bytes to send in the query string for the HTTP request.

This gets

- urlencoded and either attached to the raw URL when GET method is used.
- send with `Content-Type: application/x-www-form-urlencoded` and an automatically generated `Content-Length` header as POST method HTTP body.

Only used if `url` is specified, not used for `file`.

**year_field**  
*(string) (optional, default: None)*

Field in params dictionary to be replaced with current year (4 digits including century).

**regex**  
*(string) (optional, default: None)*

Regular expression used to remove needless text from collection types.

See also example below.

**split_at**  
*(string) (optional, default: None)*

Delimiter to split event summary into individual collection types. If your service puts multiple collections types which occur at the same day into a single event, this option can be used to separate the collection types again.

**version**  
*(integer) (optional, default: 2)*

Selects the underlying ICS file parser:

- version: 1 uses `recurring_ical_events`
- version: 2 uses `icalevents`

**verify_ssl**  
*(boolean) (optional, default: True)*

Allows do disable SSL certificate checks in case the HTTPS server of your service provider is misconfigured and therefore doesn't send intermediate certificates. Unlike browsers, python doesn't support automatic fetching of missing intermediates.

Set this option to `False` if you see the following warning in the logs:

`[SSL: CERTIFICATE_VERIFY_FAILED] certificate verify failed: unable to get local issuer certificate`.

**headers**  
*(dict) (optional, default: empty dict)*

Add custom headers to HTTP request, e.g. `referer`. By default, the `user-agent` is already set to `Mozilla/5.0 (Windows NT 10.0; Win64; x64)`.

See also [example](#custom-headers) below.

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

### Custom Headers

```yaml
waste_collection_schedule:
  sources:
    - name: ics
      args:
        url: "https://abc.com"
        headers:
          referer: special-referer
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
        url: "https://recollect.a.ssl.fastly.net/api/places/BCCDF30E-578B-11E4-AD38-5839C200407A/services/208/events.en.ics"
        split_at: "\\, [and ]*"
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

***

### Regular Expression

```yaml
waste_collection_schedule:
  sources:
    - name: ics
      args:
        file: waste.ics
        regex: "Abfuhr: (.*)"
```

Removes the needless prefix "Abfuhr: " from the waste collection type.
