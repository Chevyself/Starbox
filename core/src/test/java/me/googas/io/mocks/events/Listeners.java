package me.googas.io.mocks.events;

import lombok.NonNull;
import me.googas.io.mocks.Person;
import me.googas.starbox.events.ListenPriority;
import me.googas.starbox.events.Listener;

public class Listeners {

    // Correct
    @Listener
    public void onNewPerson(@NonNull NewPersonEvent event) {
        System.out.println("A new person has been born! " + event.getPerson());
    }

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
