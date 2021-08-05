package me.googas.io.mocks.events;

import lombok.Getter;
import lombok.NonNull;
import me.googas.io.mocks.Person;
import me.googas.starbox.events.Event;

public class PersonEvent implements Event {


    @NonNull @Getter
    private final Person person;

    public PersonEvent(@NonNull Person person) {
        this.person = person;
    }
}
