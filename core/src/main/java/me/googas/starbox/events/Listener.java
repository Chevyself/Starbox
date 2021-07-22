package me.googas.starbox.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation that is used in a method to know that it purpose is listening to an event. Check
 * how it is parsed in {@link ListenerManager#registerListeners(Object)}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Listener {
  /**
   * Get the priority for the event to be called in the listener
   *
   * @return the priority for the event to be called in the listener
   */
  int priority() default 1;
}
