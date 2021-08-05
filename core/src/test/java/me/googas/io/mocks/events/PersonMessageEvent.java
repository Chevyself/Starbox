package me.googas.io.mocks.events;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.io.mocks.Person;
import me.googas.starbox.events.Cancellable;

/** Mock event. */
public class PersonMessageEvent extends PersonEvent implements Cancellable {

  @NonNull @Getter private final String message;
  @Getter @Setter private boolean cancelled;

  /**
   * Create the event.
   *
   * @param person the person involved in the event
   * @param message the message "sent" by the person
   */
  public PersonMessageEvent(@NonNull Person person, @NonNull String message) {
    super(person);
    this.message = message;
  }
}
