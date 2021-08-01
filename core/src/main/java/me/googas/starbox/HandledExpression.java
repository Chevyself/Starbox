package me.googas.starbox;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * This class represents an expression which throws an exception that must be handled. This was
 * created to provide methods ready for developers to use but still allow a way to handle exceptions
 * as they please.
 *
 * <p>TODO EJ
 *
 * @param <O> the type to return when the expression is completed
 */
public class HandledExpression<O> {

  /** The expression to execute in the try block */
  @NonNull private final Expression<O> expression;
  /** The expressions to execute in the finally block */
  @NonNull private final List<RunnableExpression> nexts;
  /** The consumer which will handle the thrown exceptions */
  @NonNull private Consumer<Throwable> handle;

  private HandledExpression(
      @NonNull Expression<O> expression,
      @NonNull List<RunnableExpression> nexts,
      @NonNull Consumer<Throwable> handle) {
    this.expression = expression;
    this.nexts = nexts;
    this.handle = handle;
  }

  /**
   * Start handling an expression
   *
   * @param expression the expression to handle
   * @param <O> the type to return after the expression is handled
   * @return a new instance of this class to handle the expresion
   */
  @NonNull
  public static <O> HandledExpression<O> using(@NonNull Expression<O> expression) {
    return new HandledExpression<>(expression, new ArrayList<>(), (throwable) -> {});
  }

  /**
   * Run the expression and provide the returned object from {@link Expression#run()}
   *
   * @return an {@link Optional} instance with the object from the expression or empty if an
   *     exception was thrown
   */
  @NonNull
  public Optional<O> provide() {
    O other = null;
    try {
      other = expression.run();
    } catch (Throwable e) {
      handle.accept(e);
    } finally {
      if (nexts != null) {
        for (RunnableExpression next : this.nexts) {
          try {
            next.run();
          } catch (Throwable e) {
            handle.accept(e);
          }
        }
      }
    }
    return Optional.ofNullable(other);
  }

  /** Run the expression */
  public void run() {
    this.provide();
  }

  /**
   * Assign the handle for the expression
   *
   * @param handle the consumer which will handle exceptions thrown by the expression
   * @return this same object
   */
  @NonNull
  public HandledExpression<O> handle(@NonNull Consumer<Throwable> handle) {
    this.handle = handle;
    return this;
  }

  /**
   * Assign an expression to be run in the finally block
   *
   * @param next the expression to be run in the finally block
   * @return this same object
   */
  @NonNull
  public HandledExpression<O> next(@NonNull RunnableExpression next) {
    this.nexts.add(next);
    return this;
  }

  /**
   * This interface represents an expression which when run may thrown an exception
   *
   * @param <O> the type of object that the expression returns when run correctly
   */
  public interface Expression<O> {
    /**
     * Run the expression
     *
     * @return any object
     * @throws Throwable any exception
     */
    @NonNull
    O run() throws Throwable;
  }

  /** This interface represents an expression that does not return an object when ran */
  public interface RunnableExpression {
    /**
     * Run the expression
     *
     * @throws Throwable any exception
     */
    void run() throws Throwable;
  }
}
