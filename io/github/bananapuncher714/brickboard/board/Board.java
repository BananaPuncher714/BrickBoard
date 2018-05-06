package io.github.bananapuncher714.brickboard.board;

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
import io.github.bananapuncher714.ngui.util.GuiUtil;

public class Board {
	protected Map< ChatBox, BoxCoord > containers = new HashMap< ChatBox, BoxCoord >();
	protected final String id;
	protected final int width, height;
	protected boolean forceExtend;
	FontManager manager;
	
	public Board( String id, FontManager manager ) {
		this( id, manager, false, BrickBoard.CHAT_LEN, 20 );
	}
	
	public Board( String id, FontManager manager, boolean forceExtend ) {
		this( id, manager, forceExtend, BrickBoard.CHAT_LEN, 20 );
	}
	
	public Board( String id, FontManager manager, boolean forceExtend, int width, int height ) {
		this.id = id;
		this.width = width;
		this.height = height;
		this.manager = manager;
		this.forceExtend = forceExtend;
	}
	
	public String getId() {
		return id;
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

	public void setContainer( ChatBox container, BoxCoord coord ) {
		for ( BoxCoord value : containers.values() ) {
			if ( coord.compareTo( value ) == 0 ) {
				throw new IllegalArgumentException( "BoxCoord must not already exist in Board! At (" + coord.getX() + ", " + coord.getY() + ")" );
			}
		}
		containers.put( container, coord );
	}

	public void sort( boolean reset ) {
		if ( reset ) {
			for ( BoxCoord coord : containers.values() ) {
				coord.setHeight( 1 );
				coord.setWidth( 1 );
			}
		}
		GuiUtil.organize( containers.values(), width, height );
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public FontManager getManager() {
		return manager;
	}

	private boolean doesLineContain( int line, BoxCoord coord ) {
		return line >= coord.getY() && line < coord.getY() + coord.getHeight();
	}
}
