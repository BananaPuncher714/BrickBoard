package io.github.bananapuncher714.brickboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.dependencies.ClipsPlaceholder;
import io.github.bananapuncher714.brickboard.dependencies.MvDWPlaceholder;

public class DependencyManager {
	public static final boolean placeholderAPI = Bukkit.getPluginManager().isPluginEnabled( "PlaceholderAPI" );
	public static final boolean mvdwplaceholderAPI = Bukkit.getPluginManager().isPluginEnabled( "MVdWPlaceholderAPI" );

	public static String parse( Player player, String message ) {
		if ( placeholderAPI ) {
			message = ClipsPlaceholder.parse( player, message );
		}
		if ( mvdwplaceholderAPI ) {
			message = MvDWPlaceholder.parse( player, message );
		}
		return message;
	}
}
