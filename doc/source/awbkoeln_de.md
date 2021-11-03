# Abfallwirtschaftsbetriebe Köln

Support for schedules provided by [awbkoeln.de](https://www.awbkoeln.de/).

## Configuration via configuration.yaml

```yaml
waste_collection_schedule:
  sources:
    - name: awbkoeln_de
      args:
        street_code: STREET_CODE
        building_number: BUILDING_NUMBER
```

### Configuration Variables

**street_code**<br>
*(string) (required)*

**building_number**<br>
*(string) (required)*

## Example

```yaml
waste_collection_schedule:
  sources:
    - name: awbkoeln_de
      args:
        street_code: 4272
        building_number: 10
```

## How to get the source arguments

There is a script with an interactive command line interface which generates the required source configuration:

[https://github.com/mampfes/hacs_waste_collection_schedule/blob/master/custom_components/waste_collection_schedule/waste_collection_schedule/wizard/awbkoeln_de.py](https://github.com/mampfes/hacs_waste_collection_schedule/blob/master/custom_components/waste_collection_schedule/waste_collection_schedule/wizard/awbkoeln_de.py).

First, install the Python module `inquirer`. Then run this script from a shell and answer the questions.
