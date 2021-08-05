package me.googas.io.mocks.events;

import lombok.Getter;
import lombok.NonNull;
import me.googas.io.mocks.Person;
import me.googas.starbox.events.Event;

/** Mock events. */
public class PersonEvent implements Event {

  @NonNull @Getter private final Person person;

  /**
   * Create the event.
   *
   * @param person the person involved in the event.
   */
  public PersonEvent(@NonNull Person person) {
    this.person = person;
  }
}
