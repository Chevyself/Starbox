package me.googas.io;

import lombok.NonNull;
import me.googas.io.mocks.Person;
import me.googas.io.mocks.events.Listeners;
import me.googas.io.mocks.events.NewPersonEvent;
import me.googas.io.mocks.events.PersonMessageEvent;
import me.googas.starbox.events.ListenerManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class EventsTest {

    @NonNull
    private static final Person mock = new Person(0, "Foo", "Bar", "a@a.com", 18);
    @NonNull
    private static final ListenerManager manager = new ListenerManager();

    @BeforeAll
    static void beforeAll() {
        EventsTest.manager.parseAndRegister(new Listeners());
        EventsTest.manager.register(event -> {
          if (event instanceof PersonMessageEvent) {
              // Lets not allow goodbyes
              if (((PersonMessageEvent) event).getMessage().contains("bye")) {
                  ((PersonMessageEvent) event).setCancelled(true);
              }
          }
        });
    }

    @Test
    void call() {
        EventsTest.manager.call(new NewPersonEvent(EventsTest.mock));
        EventsTest.manager.call(new PersonMessageEvent(EventsTest.mock, "Hello world!"));
        EventsTest.manager.call(new PersonMessageEvent(EventsTest.mock, "Goodbye world!"));
    }
}
