package me.googas.io.mocks.events;

import lombok.NonNull;
import me.googas.io.mocks.Person;
import me.googas.starbox.events.Listener;

/** Some examples of wrong listeners. */
@SuppressWarnings("javadoc")
public class WrongListeners {

  // Wrong: missing parameter
  @Listener
  public void onNewPerson() {}

  // Wrong: first parameter does not implement Event
  @Listener
  public void onNewPerson(@NonNull Person person) {}

  // Wrong: It has more than one parameter
  @Listener
  public void onNewPerson(@NonNull NewPersonEvent event, @NonNull String message) {}
}
