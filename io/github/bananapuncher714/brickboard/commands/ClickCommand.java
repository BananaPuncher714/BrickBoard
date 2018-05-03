package io.github.bananapuncher714.brickboard.commands;

import org.bukkit.entity.Player;

/**
 * Simple command-to-action for chat buttons with runnable commands; The id MUST be unique! Not cAsE sEnSiTiVe
 * 
 * @author BananaPuncher714
 */
public interface ClickCommand {
	public void onClick( Player player, String... args );
	public String getId();
}
