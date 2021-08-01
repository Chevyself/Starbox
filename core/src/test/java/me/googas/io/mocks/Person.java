package me.googas.io.mocks;

import java.util.Objects;
import java.util.StringJoiner;
import lombok.NonNull;

public class Person {

  private final int id;
  @NonNull private final String username;
  @NonNull private final String name;
  @NonNull private final String email;
  private final int age;

  public Person(
      int id, @NonNull String username, @NonNull String name, @NonNull String email, int age) {
    this.id = id;
    this.username = username;
    this.name = name;
    this.email = email;
    this.age = age;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    Person person = (Person) o;
    return id == person.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Person.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("username='" + username + "'")
        .add("name='" + name + "'")
        .add("email='" + email + "'")
        .add("age=" + age)
        .toString();
  }
}
