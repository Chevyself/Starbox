package me.googas.net.sockets.json.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import me.googas.net.sockets.json.JsonReceptor;
import me.googas.net.sockets.json.ParamName;
import me.googas.net.sockets.json.Receptor;
import me.googas.net.sockets.json.exception.JsonExternalCommunicationException;
import me.googas.net.sockets.json.exception.JsonInternalCommunicationException;
import me.googas.reflect.utility.ReflectUtil;

/**
 * This object represents the {@link me.googas.net.sockets.json.JsonReceptor} registered inside a
 * {@link me.googas.net.sockets.json.JsonMessenger} this means that this is the object after
 * reflection was made
 */
public class ReflectJsonReceptor implements JsonReceptor {

  /** The method which request must use to prepare this receptor. */
  @NonNull private final String requestMethod;

  /** The object required to prepare the method. */
  @NonNull private final Object object;

  /** The method to prepare. This is the annotated method with {@link JsonReceptor} */
  @NonNull private final Method method;

  /** The parameters that the receptor requires to be executed. */
  @NonNull private final List<JsonReceptorParameter<?>> parameters;

  /**
   * Create the receptor.
   *
   * @param requestMethod the method which request use to prepare this receptor
   * @param object the object required to prepare the method
   * @param method the method to prepare
   * @param parameters the parameters that the receptor requires to be executed
   */
  public ReflectJsonReceptor(
      @NonNull String requestMethod,
      @NonNull Object object,
      @NonNull Method method,
      @NonNull List<JsonReceptorParameter<?>> parameters) {
    this.requestMethod = requestMethod;
    this.object = object;
    this.method = method;
    this.parameters = parameters;
  }

  /**
   * Get all the receptors given from certain object.
   *
   * @param object the object to get the receptors from
   * @return the list of receptors
   */
  @NonNull
  public static List<JsonReceptor> getReceptors(@NonNull Object object) {
    List<JsonReceptor> receptors = new ArrayList<>();
    for (Method method : object.getClass().getMethods()) {
      if (ReflectUtil.hasAnnotation(method, Receptor.class)) {
        Receptor annotation = method.getAnnotation(Receptor.class);
        List<JsonReceptorParameter<?>> parameters = new ArrayList<>();
        Parameter[] params = method.getParameters();
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < params.length; i++) {
          String name = params[i].getName();
          if (ReflectUtil.hasAnnotation(paramAnnotations[i], ParamName.class)) {
            name = ReflectUtil.getAnnotation(paramAnnotations[i], ParamName.class).value();
          }
          parameters.add(new JsonReceptorParameter<>(name, params[i].getType()));
        }
        receptors.add(new ReflectJsonReceptor(annotation.value(), object, method, parameters));
      }
    }
    return receptors;
  }

  /**
   * Invokes the receptor.
   *
   * @param objects the parameters which the receptor needs
   * @return the object can be either any object or nothing if it is void
   * @throws JsonInternalCommunicationException if the method cannot be accessed
   * @throws JsonExternalCommunicationException if the method was not executed correctly
   */
  public Object invoke(@NonNull Object... objects)
      throws JsonInternalCommunicationException, JsonExternalCommunicationException {
    try {
      return this.method.invoke(this.object, objects);
    } catch (IllegalAccessException e) {
      throw new JsonInternalCommunicationException(e);
    } catch (InvocationTargetException e) {
      throw new JsonExternalCommunicationException(e);
    }
  }

  /**
   * Get the method which request must use to prepare this receptor.
   *
   * @return the method as a string
   */
  @NonNull
  public String getRequestMethod() {
    return this.requestMethod;
  }

  /**
   * Get the parameters that the receptor needs to be executed.
   *
   * @return the list of parameters that the receptor needs to be executed
   */
  @NonNull
  public List<JsonReceptorParameter<?>> getParameters() {
    return this.parameters;
  }
}
