package io.github.bananapuncher714.brickboard.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.api.ChatBox;
import io.github.bananapuncher714.brickboard.api.chat.ChatComponent;
import io.github.bananapuncher714.brickboard.api.chat.ChatMessage;
import io.github.bananapuncher714.brickboard.objects.Board;
import io.github.bananapuncher714.ngui.objects.BoxCoord;

public class ChatBoxRainbow extends ChatBox {
	protected List< ChatColor > colors = new ArrayList< ChatColor >();
	protected String message;
	protected int colorIndex = 0;
	protected ChatMessage prefix, suffix;
	
	public ChatBoxRainbow( String prefix, String message, String suffix ) {
		colors.add( ChatColor.DARK_RED );
		colors.add( ChatColor.RED );
		colors.add( ChatColor.GOLD );
		colors.add( ChatColor.YELLOW );
		colors.add( ChatColor.GREEN );
		colors.add( ChatColor.DARK_GREEN );
		colors.add( ChatColor.DARK_BLUE );
		colors.add( ChatColor.BLUE );
		colors.add( ChatColor.AQUA );
		colors.add( ChatColor.DARK_AQUA );
		colors.add( ChatColor.LIGHT_PURPLE );
		colors.add( ChatColor.DARK_PURPLE );
		
		this.prefix = ChatMessage.getMessageFromString( prefix, true );
		this.suffix = ChatMessage.getMessageFromString( suffix, true );
		this.message = message;
	}

	@Override
	public List< ChatMessage > getMessages( Board board, Player player, BoxCoord coord ) {
		List< ChatMessage > messages = new ArrayList< ChatMessage >();
		colorIndex = ( colorIndex + 1 ) % colors.size();
		ChatMessage rainbow = prefix.clone();
		rainbow.addComponent( new ChatComponent( message ).setBold( true ).setColor( colors.get( colorIndex ) ) );
		rainbow.merge( suffix );
		messages.add( rainbow );
		return messages;
		
	}

	@Override
	public ChatBox clone() {
		return new ChatBoxRainbow( prefix.getMessage(), message, suffix.getMessage() );
	}

	@Override
	public ConfigurationSection serialize() {
		ConfigurationSection config = new YamlConfiguration();
		config.set( "prefix", prefix.toString() );
		config.set( "message", message );
		config.set( "suffix", suffix.toString() );
		return config;
	}
	
	public static ChatBox deserialize( ConfigurationSection map ) {
		if ( map == null ) {
			return null;
		}
		String suffix =  map.getString( "suffix" );
		String message =  map.getString( "message" );
		String prefix = map.getString( "prefix" );
		return new ChatBoxRainbow( prefix, message, suffix );
	}
}
