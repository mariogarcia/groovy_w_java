package ms.util

import groovy.json.JsonOutput

/**
 * Wraps all JSON serialization using plain Groovy
 *
 * @since 0.1.0
 */
class Json {

    /**
     * Serializes any type of object using plain Groovy JSON
     * serialization
     *
     * @param o object to serialize to JSON
     * @return a {@link String} JSON representation of the object
     * @since 0.1.0
     */
    static String toJson(Object o) {
        new JsonOutput().with {
            return prettyPrint(toJson(o))
        }
    }
}
