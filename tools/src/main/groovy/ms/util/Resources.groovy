package ms.util

import org.yaml.snakeyaml.Yaml

/**
 * Utility class used to get resources both from the classpath and the
 * local filesystem
 *
 * @since 0.1.0
 */
class Resources {

    /**
     * Loads a given Yaml file located in the classpath and exposes
     * its properties as a plain {@link Map}
     *
     * @param classpath location within the classpath
     * @return a map containing the yaml file properties
     * @since 0.1.0
     */
    static Map loadYamlFromClasspath(String classpath) {
        return new Yaml().load(loadInputStreamfromClasspath('/application.yml'))
    }

    /**
     * Loads a given resource's input stream. That resource should be
     * located in the classpath
     *
     * @param classpath location within the classpath
     * @return an {@link InputStream} with the content of the file
     * @since 0.1.0
     */
    static InputStream loadInputStreamfromClasspath(String classpath) {
        return Resources.class.getResourceAsStream(classpath)
    }
}
