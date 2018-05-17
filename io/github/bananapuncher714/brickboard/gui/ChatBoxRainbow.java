package io.github.bananapuncher714.brickboard.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.api.ChatBox;
import io.github.bananapuncher714.brickboard.api.chat.ChatComponent;
import io.github.bananapuncher714.brickboard.api.chat.ChatMessage;
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
		
		this.prefix = new ChatMessage().addComponent( new ChatComponent( prefix ) );
		this.suffix = new ChatMessage().addComponent( new ChatComponent( suffix ) );
		this.message = message;
	}

	@Override
	public List< ChatMessage > getMessages( Player player, BoxCoord coord ) {
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
		return null;
	}
	
	public static ChatBox deserialize( ConfigurationSection map ) {
		return null;
	}
}
