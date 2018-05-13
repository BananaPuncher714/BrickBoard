package io.github.bananapuncher714.brickboard;

import java.util.HashMap;
import java.util.Map;

import io.github.bananapuncher714.brickboard.objects.MinecraftFontContainer;

public class FontManager {
	private static FontManager instance;
	
	Map< String, MinecraftFontContainer > fonts = new HashMap< String, MinecraftFontContainer >();
	MinecraftFontContainer defaultFont;
	
	private FontManager() {
	}

	public MinecraftFontContainer getDefaultContainer() {
		return defaultFont;
	}
	
	public void setDefaultContainer( MinecraftFontContainer container ) {
		this.defaultFont = container;
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
	
	public static final FontManager getInstance() {
		if ( instance == null ) {
			instance = new FontManager();
		}
		return instance;
	}
}
