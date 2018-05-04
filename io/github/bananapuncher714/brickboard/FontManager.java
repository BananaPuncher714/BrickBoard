package io.github.bananapuncher714.brickboard;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.Plugin;

import io.github.bananapuncher714.brickboard.objects.MinecraftFontContainer;

public class FontManager {
	Plugin plugin;
	Map< String, MinecraftFontContainer > fonts = new HashMap< String, MinecraftFontContainer >();
	MinecraftFontContainer defaultFont;
	
	public FontManager( MinecraftFontContainer container ) {
		defaultFont = container;
	}

	public MinecraftFontContainer getDefaultContainer() {
		return defaultFont;
	}
	
	public void addFont( String id, MinecraftFontContainer container ) {
		fonts.put( id, container );
	}
	
	public MinecraftFontContainer getContainer( String id ) {
		return fonts.get( id );
	}
	
	protected Map< String, MinecraftFontContainer > getContainers() {
		return fonts;
	}
}
