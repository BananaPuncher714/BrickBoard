package io.github.bananapuncher714.brickboard.objects;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.BrickBoard;
import io.github.bananapuncher714.brickboard.BrickPlayer;
import io.github.bananapuncher714.brickboard.BrickPlayerManager;
import io.github.bananapuncher714.brickboard.FontManager;
import io.github.bananapuncher714.brickboard.api.ChatBox;
import io.github.bananapuncher714.brickboard.api.chat.ChatComponent;
import io.github.bananapuncher714.brickboard.api.chat.ChatMessage;
import io.github.bananapuncher714.brickboard.util.MessageUtil;
import io.github.bananapuncher714.ngui.objects.BoxCoord;
import io.github.bananapuncher714.ngui.util.GuiUtil;

public class Board implements Cloneable, Serializable {
	protected boolean forceExtend;
	protected Map< ChatBox, BoxCoord > containers = new HashMap< ChatBox, BoxCoord >();
	protected String id;
	protected final int width, height;
	protected final UUID uuid;
	
	public Board( String id ) {
		this( id, false, BrickBoard.CHAT_LEN, 20 );
	}
	
	public Board( String id, boolean forceExtend ) {
		this( id, forceExtend, BrickBoard.CHAT_LEN, 20 );
	}
	
	public Board( String id, boolean forceExtend, int width, int height ) {
		this.id = id;
		this.width = width;
		this.height = height;
		uuid = UUID.randomUUID();
		this.forceExtend = forceExtend;
	}
	
	public ChatMessage getMessage( Player player ) {
		Map< BoxCoord, ChatMessage[] > output = new TreeMap< BoxCoord, ChatMessage[] >();
		BrickPlayer bPlayer = BrickPlayerManager.getInstance().getPlayer( player.getUniqueId() );
		
		for ( ChatBox container : containers.keySet() ) {
			if ( forceExtend ) {
				output.put( containers.get( container ), MessageUtil.truncateAndExtend( player, container, containers.get( container ), 0, FontManager.getInstance().getContainer( bPlayer.getFont() ) ) );
			} else {
				output.put( containers.get( container ), MessageUtil.truncateAndExtend( player, container, containers.get( container ), width, FontManager.getInstance().getContainer( bPlayer.getFont() ) ) );
			}
		}
		ChatMessage message = new ChatMessage();
		for ( int i = 0; i < height; i++ ) {
			for ( BoxCoord coord : output.keySet() ) {
				if ( !doesLineContain( i, coord ) ) {
					continue;
				}
				ChatMessage[] messages = output.get( coord );
				int level = i - coord.getY();
				message.merge( messages[ level ], ChatMessage.getMessageFromString( ChatColor.RESET + "" ) );
			}
			
			if ( i < height - 1 ) {
				message.addComponent( new ChatComponent( "\n" ).clearFormatting().setColor( ChatColor.WHITE ) );
			}
		}
		return message;
	}

	public boolean isForceExtend() {
		return forceExtend;
	}
	
	public Board setForceExtend( boolean forceExtend ) {
		this.forceExtend = forceExtend;
		return this;
	}

	private boolean doesLineContain( int line, BoxCoord coord ) {
		return line >= coord.getY() && line < coord.getY() + coord.getHeight();
	}
	
	public String getId() {
		return id;
	}
	
	public void setId( String newId ) {
		id = newId;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setContainer( ChatBox container, BoxCoord coord ) {
		for ( BoxCoord value : containers.values() ) {
			if ( coord.compareTo( value ) == 0 ) {
				throw new IllegalArgumentException( "BoxCoord must not already exist in Board! At (" + coord.getX() + ", " + coord.getY() + ")" );
			}
		}
		containers.put( container, coord );
	}
	
	public Map< ChatBox, BoxCoord > getContainers() {
		return containers;
	}
	
	/**
	 * Note that this MUST be called before attempting to use!! If not, it will cause errors
	 * 
	 * @param reset
	 */
	public void sort( boolean reset ) {
		if ( reset ) {
			for ( BoxCoord coord : containers.values() ) {
				coord.setHeight( 1 );
				coord.setWidth( 1 );
			}
		}
		GuiUtil.organize( containers.values(), width, height );
	}
	
	@Override
	public Board clone() {
		Board template = new Board( id, forceExtend, width, height );
		for ( ChatBox box : containers.keySet() ) {
			template.setContainer( box.clone(), containers.get( box ).clone() );
		}
		return template;
	}
}
