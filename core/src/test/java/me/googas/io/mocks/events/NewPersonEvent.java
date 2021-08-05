package me.googas.io.mocks.events;

import lombok.Getter;
import lombok.NonNull;
import me.googas.io.mocks.Person;

public class NewPersonEvent extends PersonEvent {

    public NewPersonEvent(@NonNull Person person) {
        super(person);
    }
}
