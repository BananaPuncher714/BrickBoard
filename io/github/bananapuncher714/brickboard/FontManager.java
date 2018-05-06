package io.github.bananapuncher714.brickboard;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.Plugin;

import io.github.bananapuncher714.brickboard.objects.MinecraftFontContainer;

public class FontManager {
	Plugin plugin;
	Map< String, MinecraftFontContainer > fonts = new HashMap< String, MinecraftFontContainer >();
	MinecraftFontContainer defaultFont;
	
	public FontManager( Plugin plugin, MinecraftFontContainer container ) {
		defaultFont = container;
		this.plugin = plugin;
	}

	public MinecraftFontContainer getDefaultContainer() {
		return defaultFont;
	}
	
	public void addFont( MinecraftFontContainer container ) {
		fonts.put( container.getId(), container );
	}
	
	public MinecraftFontContainer getContainer( String id ) {
		return fonts.containsKey( id ) ? fonts.get( id ) : defaultFont;
	}
	
	protected Map< String, MinecraftFontContainer > getContainers() {
		return fonts;
	}
}
