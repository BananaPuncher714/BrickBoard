package io.github.bananapuncher714.brickboard.dependencies;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;

public class ClipsPlaceholder {
	public static void init() {
		new BrickBoardExpansion().register();
	}
	
	public static String parse( Player player, String input ) {
		String result = PlaceholderAPI.setPlaceholders( player, input );
		return result;
	}
}
