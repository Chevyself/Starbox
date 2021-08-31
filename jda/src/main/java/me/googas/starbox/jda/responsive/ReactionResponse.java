package me.googas.starbox.jda.responsive;

import java.util.Optional;
import lombok.NonNull;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

/** This class is the one that executes an action when the unicode matches. */
public interface ReactionResponse {

  /**
   * If unicode matches this is the action to run.
   *
   * @param event the reaction event
   * @return whether to remove the reaction. true to remove it false otherwise
   */
  default boolean onReaction(@NonNull MessageReactionAddEvent event) {
    return true;
  }

  /**
   * If unicode matches this is the action to run.
   *
   * @param event the reaction event
   */
  default void onReactionRemoved(@NonNull MessageReactionRemoveEvent event) {}

  /**
   * Get the unicode to add in a message. This unicode does not have to match {@link
   * #hasUnicode(String)} it is just used to add this reaction in a message, that is why it is
   * optional.
   *
   * @return a {@link Optional} instance holding the nullable string
   */
  @NonNull
  default Optional<String> getUnicode() {
    return Optional.empty();
  }

  /**
   * Check if the unicode matches to run the action.
   *
   * @param unicode to unicode to match
   * @return true if the unicode matches to run
   */
  boolean hasUnicode(@NonNull String unicode);
}
