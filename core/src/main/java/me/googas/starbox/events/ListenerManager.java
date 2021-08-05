package me.googas.starbox.events;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;

/**
 * Manages calling to events and selecting the respective listeners for the event. Listener are
 * registered using reflection that is why the annotation {@link Listener} is required for the
 * methods that are going to be listening for an event.
 */
public class ListenerManager {

  /** The listeners registered in the manager */
  @NonNull @Getter private final Collection<EventListener> listeners = new HashSet<>();

  @NonNull
  public ListenerManager register(@NonNull EventListener listener) {
    this.listeners.add(listener);
    return this;
  }

  @NonNull
  public ListenerManager unregister(@NonNull EventListener listener) {
    listeners.remove(listener);
    return this;
  }

  /**
   * Register the listeners from the object.
   *
   * <p>Using reflection this method will get the class of the object doing a loop for each method.
   * The methods that have the annotation {@link Listener} will be attempted to create a listener
   *
   * @param object to get the class and methods to create the listeners
   * @throws ListenerRegistrationException if the method does not have parameters, if the method has
   *     more than one parameters and if the parameter does not extend {@link Event}
   * @return this same instance of listener manager
   */
  // Suppressed the warning for the cast of the event class but it is check by using the Event class
  // to see if the class of the parameter can be assigned
  @SuppressWarnings("unchecked")
  @NonNull
  public ListenerManager parseAndRegister(@NonNull Object object) {
    Class<?> aClass = object.getClass();
    for (Method method : aClass.getMethods()) {
      Listener annotation = method.getAnnotation(Listener.class);
      if (annotation != null) {
        Parameter[] parameters = method.getParameters();
        if (parameters.length == 0) {
          throw new ListenerRegistrationException(
              "Method "
                  + method
                  + "  in "
                  + aClass
                  + " does not have parameters therefore no event was found");
        } else if (parameters.length > 1) {
          throw new ListenerRegistrationException(
              "Method "
                  + method
                  + " in "
                  + aClass
                  + " has more than one parameter which will cause an exception when trying to prepare the method");
        } else if (Event.class.isAssignableFrom(parameters[0].getType())) {
          ReflectEventListener eventListener =
              new ReflectEventListener(
                  object,
                  method,
                  // This is the cast that is "unchecked"
                  (Class<? extends Event>) parameters[0].getType(),
                  annotation.priority());
          this.register(eventListener);
        } else {
          throw new ListenerRegistrationException(
              "Method " + method + " in " + aClass + " parameter does not extend " + Event.class);
        }
      }
    }
    return this;
  }

  /**
   * Get all the listeners for certain event.
   *
   * @param clazz the clazz of the event to get all the listeners
   * @return a sorted list of listeners for the event
   */
  @NonNull
  public List<EventListener> getListeners(@NonNull Class<? extends Event> clazz) {
    return this.listeners.stream()
        .filter(
            listener -> {
              if (listener instanceof ReflectEventListener) {
                return ((ReflectEventListener) listener).getEvent().isAssignableFrom(clazz);
              }
              return true;
            })
        .sorted(Comparator.comparingInt(EventListener::getPriority))
        .collect(Collectors.toList());
  }

  /**
   * Unregisters a listener
   *
   * @param object the listener to unregister, this is the object used to prepare the methods of the
   *     listeners
   */
  public void unregister(@NonNull Object object) {
    this.listeners.removeIf(
        listener ->
            listener instanceof ReflectEventListener
                && ((ReflectEventListener) listener).getListener() == object);
  }

  /**
   * Calls an event. This will get all the listeners for the event and call it for each of them
   *
   * @param event the event to be called
   */
  public void call(@NonNull Event event) {
    for (EventListener listener : this.getListeners(event.getClass())) {
      listener.call(event);
    }
  }

  /**
   * Calls an event. As in {@link #call(Event)} but returns whether it was cancelled
   *
   * @param cancellable the event to be called
   * @return true if the event was cancelled
   */
  public boolean callAndGet(@NonNull Cancellable cancellable) {
    this.call(cancellable);
    return cancellable.isCancelled();
  }
}
