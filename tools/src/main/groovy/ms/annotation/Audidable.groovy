package ms.annotation

import asteroid.Local

/**
 * A given method annotated with this annotation will log any call
 * that exceeds the given threshold (ms). It will log the name of the
 * method called and the expected threshold.
 *
 * @since 0.1.0
 */
@Local(value = AuditableTransformation, applyTo = Local.TO.METHOD)
@interface Auditable {
    /**
     * The maximum threshold expected
     *
     * @since 0.1.0
     */
    int threshold()
}
