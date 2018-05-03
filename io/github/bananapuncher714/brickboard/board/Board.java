package io.github.bananapuncher714.brickboard.board;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.BrickBoard;
import io.github.bananapuncher714.brickboard.chat.ChatComponent;
import io.github.bananapuncher714.brickboard.chat.ChatMessage;
import io.github.bananapuncher714.brickboard.gui.ChatBox;
import io.github.bananapuncher714.brickboard.objects.BoxCoord;
import io.github.bananapuncher714.brickboard.util.GuiUtil;
import io.github.bananapuncher714.brickboard.util.MessageUtil;

public class Board {
	protected Map< ChatBox, BoxCoord > containers = new HashMap< ChatBox, BoxCoord >();
	protected String id;

	public Board( String id ) {
		this.id = id;
	}

	public ChatMessage getMessage( Player player ) {
		Map< BoxCoord, ChatMessage[] > output = new TreeMap< BoxCoord, ChatMessage[] >();
		for ( ChatBox container : containers.keySet() ) {
			output.put( containers.get( container ), MessageUtil.truncateAndExtend( player, container, containers.get( container ), BrickBoard.getInstance().getDefaultFont() ) );
		}
		ChatMessage message = new ChatMessage();
		for ( int i = 0; i < 20; i++ ) {
			for ( BoxCoord coord : output.keySet() ) {
				if ( !doesLineContain( i, coord ) ) {
					continue;
				}
				ChatMessage[] messages = output.get( coord );
				int level = i - coord.getY();
				message.merge( messages[ level ], ChatMessage.getMessageFromString( ChatColor.RESET + "" ) );
			}
			
			if ( i < 19 ) {
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
		GuiUtil.organize( containers );
		for ( BoxCoord coord : containers.values() ) {
			System.out.println( "Coordinate of container: " + coord.getX() + ", " + coord.getY() + " : " + coord.getWidth() + ", " + coord.getHeight() );
		}
	}

	private boolean doesLineContain( int line, BoxCoord coord ) {
		return line >= coord.getY() && line < coord.getY() + coord.getHeight();
	}
}
