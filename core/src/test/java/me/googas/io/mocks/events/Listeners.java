package me.googas.io.mocks.events;

import lombok.NonNull;
import me.googas.starbox.events.ListenPriority;
import me.googas.starbox.events.Listener;

/** Mock listeners for testing */
public class Listeners {

  /**
   * When a new person is created send a message to console.
   *
   * @param event the event of a new person being created.
   */
  @Listener
  public void onNewPerson(@NonNull NewPersonEvent event) {
    System.out.println("A new person has been born! " + event.getPerson());
  }

  /**
   * When a person sends a message send a message to console telling what it said.
   *
   * @param event the event of person sending a message
   */
  @Listener(priority = ListenPriority.MEDIUM)
  public void onPersonMessage(@NonNull PersonMessageEvent event) {
    String username = event.getPerson().getUsername();
    if (!event.isCancelled()) {
      System.out.println(username + " has said " + event.getMessage());
    } else {
      System.out.println(username + " will not say anything");
    }
  }
}
