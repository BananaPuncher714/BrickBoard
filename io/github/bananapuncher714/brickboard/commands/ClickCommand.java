package io.github.bananapuncher714.brickboard.commands;

import java.util.UUID;

import org.bukkit.entity.Player;

public interface ClickCommand {
	public void onClick( Player player, String... args );
	public String getId();
}
