package me.googas.io.mocks.events;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.googas.io.mocks.Person;
import me.googas.starbox.events.Cancellable;

public class PersonMessageEvent extends PersonEvent implements Cancellable {

    @NonNull @Getter
    private final String message;
    @Getter @Setter
    private boolean cancelled;

    public PersonMessageEvent(@NonNull Person person, @NonNull String message) {
        super(person);
        this.message = message;
    }

}
