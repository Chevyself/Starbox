package me.googas.starbox.builders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import lombok.Getter;
import lombok.NonNull;
import me.googas.io.StarboxFile;

/**
 * Helps in the creation of {@link Logger}. At the moment it is mainly to allow the use of {@link
 * FileHandler}
 */
public class LoggerBuilder implements Builder<Logger> {

  @NonNull private final String name;
  @NonNull @Getter private final List<Handler> handlers;
  @NonNull private Formatter formatter;
  private boolean useParents;
  private StarboxFile file;
  private Consumer<IOException> ioHandle;

  /**
   * Create the logger builder.
   *
   * @param name the name of the builder
   */
  private LoggerBuilder(@NonNull String name) {
    this.name = name;
    this.handlers = new ArrayList<>();
    this.formatter = new SimpleFormatter();
    this.useParents = true;
    this.file = null;
    this.ioHandle = null;
  }

  /**
   * Start a new builder.
   *
   * @param name the name of the logger
   * @return the new builder instance
   */
  @NonNull
  public static LoggerBuilder of(@NonNull String name) {
    return new LoggerBuilder(name);
  }

  /**
   * Add a handler to the logger.
   *
   * @param handler the handler to be used
   * @return this same instance
   */
  @NonNull
  public LoggerBuilder add(@NonNull Handler handler) {
    this.handlers.add(handler);
    return this;
  }

  /**
   * Set the formatter for the handlers.
   *
   * @param formatter the new formatter
   * @return this same instance
   */
  @NonNull
  public LoggerBuilder format(@NonNull Formatter formatter) {
    this.formatter = formatter;
    return this;
  }

  /**
   * Set the file in which the logger will be saved.
   *
   * @param file the file to be saved.
   * @param ioHandle the handler for the {@link IOException} in case it is thrown
   * @return this same instance
   */
  @NonNull
  public LoggerBuilder saveAt(@NonNull StarboxFile file, @NonNull Consumer<IOException> ioHandle) {
    this.file = file;
    this.ioHandle = ioHandle;
    return this;
  }

  /**
   * Set whether to use the parent handlers. This will set the {@link
   * Logger#setUseParentHandlers(boolean)}
   *
   * @param useParents whether to use parent handlers
   * @return this same instance
   */
  public LoggerBuilder setUseParents(boolean useParents) {
    this.useParents = useParents;
    return this;
  }

  @Override
  public @NonNull Logger build() {
    Logger logger = Logger.getLogger(name);
    this.handlers.forEach(logger::addHandler);
    if (file != null) {
      try {
        logger.addHandler(new FileHandler(file.getPath()));
      } catch (IOException e) {
        if (ioHandle != null) ioHandle.accept(e);
      }
    }
    logger.setUseParentHandlers(useParents);
    for (Handler handler : logger.getHandlers()) {
      handler.setFormatter(formatter);
    }
    return logger;
  }
}
