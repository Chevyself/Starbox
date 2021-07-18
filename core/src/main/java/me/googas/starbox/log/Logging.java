package me.googas.starbox.log;

import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.NonNull;

/** Static util for logging */
public class Logging {

  public static void info(Logger logger, @NonNull String s) {
    if (logger == null) {
      System.out.println(s);
    } else {
      logger.info(s);
    }
  }

  public static void info(Logger logger, @NonNull String s, @NonNull Object... format) {
    Logging.info(logger, String.format(s, format));
  }

  public static void severe(Logger logger, @NonNull String s) {
    if (logger == null) {
      System.out.println(s);
    } else {
      logger.severe(s);
    }
  }

  public static void severe(Logger logger, @NonNull String s, @NonNull Object... format) {
    Logging.severe(logger, String.format(s, format));
  }

  public static void fine(Logger logger, @NonNull String s) {
    if (logger == null) {
      System.out.println(s);
    } else {
      logger.fine(s);
    }
  }

  public static void fine(Logger logger, @NonNull String s, @NonNull Object... format) {
    Logging.fine(logger, String.format(s, format));
  }

  public static void process(Logger logger, @NonNull Throwable e) {
    if (logger != null) {
      logger.log(Level.SEVERE, e, () -> "");
    } else {
      e.printStackTrace();
    }
  }
}
