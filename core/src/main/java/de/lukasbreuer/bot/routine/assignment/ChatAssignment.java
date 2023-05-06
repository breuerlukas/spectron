package de.lukasbreuer.bot.routine.assignment;

import de.lukasbreuer.bot.chat.ChatMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "create")
public final class ChatAssignment implements Assignment {
  private final ChatMessage message;

  @Override
  public void call() throws Exception {
    message.send();
  }
}
