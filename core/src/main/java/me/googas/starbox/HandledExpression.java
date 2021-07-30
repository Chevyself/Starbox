package me.googas.starbox;

import java.util.Optional;
import java.util.function.Consumer;
import lombok.NonNull;

public class HandledExpression<O> {

  @NonNull private final Expression<O> expression;
  @NonNull private Consumer<Throwable> handle;
  private RunnableExpression next;

  private HandledExpression(
      @NonNull Expression<O> expression,
      @NonNull Consumer<Throwable> handle,
      RunnableExpression next) {
    this.expression = expression;
    this.handle = handle;
    this.next = next;
  }

  @NonNull
  public static <O> HandledExpression<O> using(@NonNull Expression<O> expression) {
    return new HandledExpression<>(expression, (throwable) -> {}, null);
  }

  @NonNull
  public Optional<O> get() {
    O other = null;
    try {
      other = expression.run();
    } catch (Throwable e) {
      handle.accept(e);
    } finally {
      if (next != null) {
        try {
          next.run();
        } catch (Throwable e) {
          handle.accept(e);
        }
      }
    }
    return Optional.ofNullable(other);
  }

  public void run() {
    this.get();
  }

  @NonNull
  public HandledExpression<O> handle(@NonNull Consumer<Throwable> handle) {
    this.handle = handle;
    return this;
  }

  @NonNull
  public HandledExpression<O> next(@NonNull RunnableExpression next) {
    this.next = next;
    return this;
  }

  public interface Expression<O> {
    @NonNull
    O run() throws Throwable;
  }

  public interface RunnableExpression {
    void run() throws Throwable;
  }
}
