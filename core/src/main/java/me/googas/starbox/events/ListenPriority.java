package me.googas.starbox.events;

/**
 * A class with static instances for easier understanding of priorities. It will call the lowest
 * priority first, this means that 0 will be called first then 1, for example with cancellable, the
 * highest priority can decide whether to cancel the event or not
 */
public class ListenPriority {

  /** The lowest priority to listen the event */
  public static final int LOWEST = 0;

  /** The low priority to listen the event */
  public static final int LOW = 1;

  /** The medium priority to listen the event */
  public static final int MEDIUM = 2;

  /** The high priority to listen the event */
  public static final int HIGH = 3;

  /** The highest priority to listen the event */
  public static final int HIGHEST = 4;
}
