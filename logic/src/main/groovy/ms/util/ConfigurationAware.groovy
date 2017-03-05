package ms.util

import ms.util.Resources

/**
 * Trait that can be used by any class to be able to get configuration
 * properties
 *
 * @since 0.1.0
 */
trait ConfigurationAware {

    /**
     * Loads configuration from classpath resource 'application.yml'
     *
     * @return a map containing configuration properties
     * @since 0.1.0
     */
    Map<String,?> loadConfig() {
        return Resources.loadYamlFromClasspath('/application.yml')
    }
}
