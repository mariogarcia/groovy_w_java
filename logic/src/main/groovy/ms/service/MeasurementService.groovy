package ms.service

import ms.model.Country

/**
 * Abstraction to get temperature measurements from a given location
 *
 * @since 0.1.0
 */
interface MeasurementService {

    /**
     * Finds all measurements of a given country
     *
     * @param country the name of the country we want to load the
     * measurements from. It should be a valid value from @{link
     * Country}
     * @return a list of measurements
     * @since 0.1.0
     */
    List<Map> findAllMeasurementsFrom(Country country)
}
