package io.github.bananapuncher714.brickboard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.github.bananapuncher714.brickboard.board.Board;
import io.github.bananapuncher714.brickboard.chat.ChatMessage;
import io.github.bananapuncher714.brickboard.objects.MinecraftFontContainer;

public class BrickPlayer implements Serializable {
	// TODO Make this more modular
	public static final String LOG_CHANNEL_NAME = "log";
	public static final int MAX_LOG_SIZE = 100;
	
	private Map< String, List< ChatMessage > > channels = new HashMap< String, List< ChatMessage > >();
	private String[] lastTabCompletes = null;
	private String activeChannel = LOG_CHANNEL_NAME;
	private String container = null;
	private String activeBoard = null;
	
	private int page = 0;
	
	private UUID uuid;
	
	public BrickPlayer( UUID uuid, String container ) {
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
		List< ChatMessage > log = getChannel( LOG_CHANNEL_NAME );
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
	
	public String getFont() {
		return container;
	}
	
	public String getActiveBoard() {
		return activeBoard;
	}
	
	public void setActiveBoard( Board board ) {
		activeBoard = board.getId();
	}
	
	public boolean setFont( MinecraftFontContainer container ) {
		if ( container == null ) {
			return false;
		}
		this.container = container.getId();
		return true;
	}
}
