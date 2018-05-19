package io.github.bananapuncher714.brickboard.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.api.ChatBox;
import io.github.bananapuncher714.brickboard.api.chat.ChatComponent;
import io.github.bananapuncher714.brickboard.api.chat.ChatMessage;
import io.github.bananapuncher714.brickboard.objects.Board;
import io.github.bananapuncher714.ngui.objects.BoxCoord;

public class ChatBoxTicker extends ChatBox {
	private String separator = "  \u2588  ";
	protected String tickerTape;
	protected int tickerIndex = 0;
	
	public ChatBoxTicker( String tickerTape ) {
		this.tickerTape = tickerTape;
	}
	
	public ChatBoxTicker( InputStream messages ) {
		BufferedReader reader = new BufferedReader( new InputStreamReader( messages ) );
		String line;
		StringBuilder builder = new StringBuilder();
		try {
			while ( ( line = reader.readLine() ) != null ) {
				if ( line.isEmpty() ) {
					continue;
				} else {
					builder.append( line );
					builder.append( separator );
				}
			}
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		tickerTape = builder.toString();
	}

	@Override
	public List< ChatMessage > getMessages( Board board, Player player, BoxCoord coord ) {
		List< ChatMessage > messages = new ArrayList< ChatMessage >();
		
		String ticker = tickerTape.substring( tickerIndex / 2, tickerIndex / 2 + 300 );
		tickerIndex = ( tickerIndex + 1 ) % ( tickerTape.length() * 2 - 600 );
		
		ChatMessage message = new ChatMessage().addComponent( new ChatComponent( ticker ).setColor( ChatColor.GRAY ) );
		
		messages.add( message );
		
		return messages;
	}
	
	@Override
	public ChatBox clone() {
		return new ChatBoxTicker( tickerTape );
	}

	@Override
	public ConfigurationSection serialize() {
		return null;
	}
	
	public static ChatBox deserialize( ConfigurationSection map ) {
		return null;
	}
}
