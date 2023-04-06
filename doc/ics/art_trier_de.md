# Zweckverband Abfallwirtschaft Region Trier (A.R.T.)

Zweckverband Abfallwirtschaft Region Trier (A.R.T.) is supported by the generic [ICS](/doc/source/ics.md) source. For all available configuration options, please refer to the source description.


## How to get the configuration arguments

- Goto <https://www.art-trier.de/> and select your municipality.  
- Scroll down to `JAHRESKALENDER FÜR IHR OUTLOOK, ETC.`  
- Set `Wann möchten Sie erinnert werden?` to `Am Abfuhrtag`.
- Click on `> Kalender (ICS) importieren` to get a webcal link.
- Replace the `url` in the example configuration with this link.

## Examples

### Basberg

```yaml
waste_collection_schedule:
  sources:
    - name: ics
      args:
        regex: 'A.R.T. Abfuhrtermin: (.*)'
        split_at: ' & '
        url: webcal://abfallkalender.art-trier.de/ics-feed/54578_basberg_1-1800.ics
```
