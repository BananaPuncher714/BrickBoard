package io.github.bananapuncher714.brickboard.objects;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.BrickBoard;
import io.github.bananapuncher714.brickboard.BrickPlayer;
import io.github.bananapuncher714.brickboard.BrickPlayerManager;
import io.github.bananapuncher714.brickboard.FontManager;
import io.github.bananapuncher714.brickboard.chat.ChatComponent;
import io.github.bananapuncher714.brickboard.chat.ChatMessage;
import io.github.bananapuncher714.brickboard.gui.ChatBox;
import io.github.bananapuncher714.brickboard.util.MessageUtil;
import io.github.bananapuncher714.ngui.objects.BoxCoord;

public class Board extends BoardTemplate {
	protected Map< ChatBox, BoxCoord > containers = new HashMap< ChatBox, BoxCoord >();
	protected boolean forceExtend;
	protected FontManager manager;
	
	public Board( String id, FontManager manager ) {
		this( id, manager, false, BrickBoard.CHAT_LEN, 20 );
	}
	
	public Board( String id, FontManager manager, boolean forceExtend ) {
		this( id, manager, forceExtend, BrickBoard.CHAT_LEN, 20 );
	}
	
	public Board( String id, FontManager manager, boolean forceExtend, int width, int height ) {
		super( id, width, height );
		this.manager = manager;
		this.forceExtend = forceExtend;
	}
	
	public ChatMessage getMessage( Player player ) {
		Map< BoxCoord, ChatMessage[] > output = new TreeMap< BoxCoord, ChatMessage[] >();
		BrickPlayer bPlayer = BrickPlayerManager.getInstance().getPlayer( player.getUniqueId() );
		
		for ( ChatBox container : containers.keySet() ) {
			if ( forceExtend ) {
				output.put( containers.get( container ), MessageUtil.truncateAndExtend( player, container, containers.get( container ), 0, manager.getContainer( bPlayer.getFont() ) ) );
			} else {
				output.put( containers.get( container ), MessageUtil.truncateAndExtend( player, container, containers.get( container ), width, manager.getContainer( bPlayer.getFont() ) ) );
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

	public FontManager getManager() {
		return manager;
	}
	
	public Board setFontManager( FontManager manager ) {
		this.manager = manager;
		return this;
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
}
