package de.lukasbreuer.bot.event.play;

import de.lukasbreuer.bot.block.Block;
import de.lukasbreuer.bot.event.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "create")
public final class BlockUpdateEvent extends Event {
  private final Block block;
}
