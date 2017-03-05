package ms.service

import groovy.util.logging.Slf4j

import ms.model.Country
import ms.util.ConfigurationAware
import ms.annotation.Auditable

/**
 * Loads given temperature measurements from a given CSV file found in
 * the filesystem. The service loads the file in the configured folder
 * with the name of the country + the .csv suffix.
 *
 * @since 0.1.0
 */
@Slf4j
class CsvMeasurementService implements MeasurementService, ConfigurationAware {

    static final List<String> COLUMNS = ['city', 'date', 'max', 'min']

    @Override
    @Auditable(threshold = 1500)
    List<Map> findAllMeasurementsFrom(Country country) {
        Thread.sleep(1494)
        File countryFile = new File(
            "${loadConfig().external_path}",
            "${country}.csv")

        log.debug "loading data from file: $countryFile"

        return countryFile.exists() ? countryFile.collect(this.&lineToMap) : []
    }

    private Map lineToMap(String line) {
        log.debug "mapping csv line to map"

        Map params =  [COLUMNS, line.split(',')]
          .transpose() // transpose is like the zip function
          .collectEntries { List tuple ->
              String key = tuple.first()
              String val = tuple.last()

              [(key): val] // creates an entry map
          }
    }
}
