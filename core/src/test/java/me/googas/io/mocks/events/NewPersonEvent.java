package me.googas.io.mocks.events;

import lombok.NonNull;
import me.googas.io.mocks.Person;

/** Mock person event. */
public class NewPersonEvent extends PersonEvent {

  /**
   * Create the event.
   *
   * @param person the person that participates in this event
   */
  public NewPersonEvent(@NonNull Person person) {
    super(person);
  }
}
