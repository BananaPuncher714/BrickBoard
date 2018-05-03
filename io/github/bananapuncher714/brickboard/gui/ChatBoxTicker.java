package io.github.bananapuncher714.brickboard.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.chat.ChatComponent;
import io.github.bananapuncher714.brickboard.chat.ChatMessage;
import io.github.bananapuncher714.brickboard.objects.BoxCoord;

public class ChatBoxTicker extends ChatBox {
	protected String tickerTape;
	protected int tickerIndex = 0;
	
	public ChatBoxTicker( InputStream messages ) {
		BufferedReader reader = new BufferedReader( new InputStreamReader( messages ) );
		String line;
		StringBuilder builder = new StringBuilder();
		try {
			while ( ( line = reader.readLine() ) != null ) {
				if ( line.isEmpty() ) {
					builder.append( "  \u2603  " );
				} else {
					builder.append( line );
				}
			}
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		tickerTape = builder.toString();
	}

	@Override
	public List< ChatMessage > getMessages( Player player, BoxCoord coord ) {
		List< ChatMessage > messages = new ArrayList< ChatMessage >();
		
		String ticker = tickerTape.substring( tickerIndex / 2, tickerIndex / 2 + 300 );
		tickerIndex = ( tickerIndex + 1 ) % ( tickerTape.length() * 2 );
		
		ChatMessage message = new ChatMessage().addComponent( new ChatComponent( ticker ).setColor( ChatColor.GRAY ) );
		
		messages.add( message );
		
		return messages;
	}

}
