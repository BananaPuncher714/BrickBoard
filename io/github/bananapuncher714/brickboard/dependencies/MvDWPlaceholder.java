package io.github.bananapuncher714.brickboard.dependencies;

import org.bukkit.OfflinePlayer;

import be.maximvdw.placeholderapi.PlaceholderAPI;

public class MvDWPlaceholder {
	public static String parse( OfflinePlayer player, String input ) {
		String result = PlaceholderAPI.replacePlaceholders( player, input );
		return result;
	}
}
