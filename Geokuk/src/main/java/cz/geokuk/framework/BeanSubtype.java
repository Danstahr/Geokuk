/**
 * 
 */
package cz.geokuk.framework;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author tatinek
 *
 */
@Retention(RetentionPolicy.RUNTIME)

public @interface BeanSubtype {
  String value() default "_use_interface_BeanSubtypable_"; 
}
