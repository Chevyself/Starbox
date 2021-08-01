package me.googas.io.mocks;

import java.util.Objects;
import java.util.StringJoiner;
import lombok.Getter;
import lombok.NonNull;
import me.googas.net.cache.Catchable;
import me.googas.starbox.time.Time;
import me.googas.starbox.time.unit.Unit;

public class Person implements Catchable {

  @Getter private final int id;
  @NonNull @Getter private final String username;
  @NonNull @Getter private final String name;
  @NonNull @Getter private final String email;
  @Getter private final int age;

  public Person(
      int id, @NonNull String username, @NonNull String name, @NonNull String email, int age) {
    this.id = id;
    this.username = username;
    this.name = name;
    this.email = email;
    this.age = age;
  }

  @Override
  public @NonNull Time getToRemove() {
    return Time.of(5, Unit.SECONDS);
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

  @Override
  public void onRemove() throws Throwable {
    System.out.println(username + " has been unloaded from cache");
  }
}
