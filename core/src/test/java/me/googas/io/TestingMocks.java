package me.googas.io;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import lombok.Getter;
import lombok.NonNull;
import me.googas.io.mocks.Person;

public class TestingMocks {

  @NonNull @Getter private final List<Person> persons;

  public TestingMocks(@NonNull List<Person> persons) {
    this.persons = persons;
  }

  public TestingMocks() {
    this(new ArrayList<>());
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", TestingMocks.class.getSimpleName() + "[", "]")
        .add("persons=" + persons)
        .toString();
  }
}
