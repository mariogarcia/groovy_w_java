package ms.rest;

import static spark.Spark.get;

import spark.Route;
import java.util.List;

import ms.util.Json;
import ms.model.Country;
import ms.service.MeasurementService;
import ms.service.CsvMeasurementService;

/**
 * This class holds rest endpoints serving temperature measurements
 * from different countries around the world.
 *
 * @since 0.1.0
 */
public final class Measurements {

    static final MeasurementService SERVICE = new CsvMeasurementService();

    /**
     * Application entry point
     *
     * @param args runtime arguments
     * @since 0.1.0
     */
    public static void main(String[] args) {
        get("/measurements/uk", from(SERVICE, Country.uk));
        get("/measurements/spain", from(SERVICE, Country.spain));
    }

    /**
     * Returns the function retrieving measurements the {@link
     * Country} passed as parameter. It is built over a specific
     * implementation.  This way you could use a mock when testing the
     * behavior of the endpoint. That keeps logic and rest
     * presentation loosely coupled.
     *
     * @param service concrete implementation of the {@link MeasurementService}
     * @return an instance of {@link Route} which will take care of
     * how data should be rendered
     * @since 0.1.0
     */
    public static Route from(final MeasurementService service, final Country country) {
        return (req, res) -> {
            res.type("application/json");

            return Json.toJson(service.findAllMeasurementsFrom(country));
        };
    }
}
