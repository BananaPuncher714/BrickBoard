package io.github.bananapuncher714.brickboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.github.bananapuncher714.brickboard.chat.ChatMessage;
import io.github.bananapuncher714.brickboard.objects.MinecraftFontContainer;

public class BrickPlayer {
	// TODO Make this more modular
	public static final int MAX_LOG_SIZE = 100;
	
	private Map< String, List< ChatMessage > > channels = new HashMap< String, List< ChatMessage > >();
	private String[] lastTabCompletes = null;
	private String activeChannel = "log";
	private MinecraftFontContainer container = null;
	
	private int page = 0;
	
	private UUID uuid;
	
	public BrickPlayer( UUID uuid, MinecraftFontContainer container ) {
		this.uuid = uuid;
		this.container = container;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public List< ChatMessage > getActiveChannel() {
		return getChannel( activeChannel );
	}
	
	public String getActiveChannelName() {
		return activeChannel;
	}
	
	public void setActiveChannel( String channel ) {
		activeChannel = channel;
	}
	
	public List< ChatMessage > getChannel( String channel ) {
		if ( !channels.containsKey( channel ) ) {
			channels.put( channel, new ArrayList< ChatMessage >() ); 
		}
		return channels.get( channel );
	}
	
	public int getPage() {
		return page;
	}
	
	public void setPage( int page ) {
		this.page = page;
	}
	
	public void addToLog( ChatMessage message ) {
		List< ChatMessage > log = getChannel( "log" );
		log.add( message );
		if ( log.size() > MAX_LOG_SIZE ) {
			log.remove( 0 );
		}
	}
	
	public void setTabCompletes( String... completes ) {
		lastTabCompletes = completes;
	}
	
	public String[] getTabCompletes() {
		return lastTabCompletes;
	}
	
	public MinecraftFontContainer getFont() {
		return container;
	}
	
	public boolean setFont( MinecraftFontContainer container ) {
		if ( container == null ) {
			return false;
		}
		this.container = container;
		return true;
	}
}
