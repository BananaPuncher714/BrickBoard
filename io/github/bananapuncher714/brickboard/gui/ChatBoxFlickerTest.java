package io.github.bananapuncher714.brickboard.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.BrickBoard;
import io.github.bananapuncher714.brickboard.chat.ChatComponent;
import io.github.bananapuncher714.brickboard.chat.ChatMessage;
import io.github.bananapuncher714.brickboard.objects.BoxCoord;

public class ChatBoxFlickerTest extends ChatBox {
	public static final String CHARS = "\u2588\u2588";
	protected static final List< ChatColor > COLORS = new ArrayList< ChatColor >();
	
	int index = 0;
	
	static {
		for ( ChatColor color : ChatColor.values() ) {
			if ( color.isColor() ) {
				COLORS.add( color );
			}
		}
	}
	
	@Override
	public List< ChatMessage > getMessages( Player player, BoxCoord coord ) {
		List< ChatMessage > flickers = new ArrayList< ChatMessage >();
		int length = BrickBoard.getInstance().getDefaultFont().getStringWidth( CHARS, false );
		int amount = coord.getWidth() / length;
		
		for ( int i = 0; i < coord.getHeight(); i++ ) {
			ChatMessage row = new ChatMessage();
			for ( int j = 0; j < amount; j++ ) {
				ChatColor color = COLORS.get( ( index + i + j ) % COLORS.size() );
				row.addComponent( new ChatComponent( CHARS ).setColor( color ) );
			}
			flickers.add( row );
		}
		
		index = ( index + 1 ) % ( Integer.MAX_VALUE - 1 );
		return flickers;
	}

}
