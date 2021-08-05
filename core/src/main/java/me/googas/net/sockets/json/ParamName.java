package me.googas.net.sockets.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Parameter;
import lombok.NonNull;
import me.googas.net.sockets.json.reflect.JsonReceptorParameter;

/**
 * Use a custom name for a parameter. The normal behaviour while parsing a {@link
 * JsonReceptorParameter} is using {@link Parameter#getName()} but in case that something else wants
 * to be used this has to be used
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ParamName {

  /**
   * The name to use in the parameter.
   *
   * @return the name
   */
  @NonNull
  String value();
}
