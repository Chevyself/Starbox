package me.googas.io;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import lombok.Getter;
import lombok.NonNull;
import me.googas.io.mocks.Person;

/** Class that contains mocks for testing. */
public class TestingMocks {

  @NonNull @Getter private final List<Person> persons;

  /**
   * Create the mocks.
   *
   * @param persons the list of persons for testing
   */
  public TestingMocks(@NonNull List<Person> persons) {
    this.persons = persons;
  }

  /** Create the mocks. */
  public TestingMocks() {
    this(new ArrayList<>());
  }

  /**
   * Get a person by its id.
   *
   * @param id the id of the person to get
   * @return a {@link Optional} instance holding the person
   */
  @NonNull
  public Optional<Person> getPerson(int id) {
    return this.persons.stream().filter(person -> person.getId() == id).findFirst();
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", TestingMocks.class.getSimpleName() + "[", "]")
        .add("persons=" + persons)
        .toString();
  }
}
