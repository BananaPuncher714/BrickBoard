package io.github.bananapuncher714.brickboard.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.BrickPlayer;
import io.github.bananapuncher714.brickboard.BrickPlayerManager;
import io.github.bananapuncher714.brickboard.FontManager;
import io.github.bananapuncher714.brickboard.api.ChatBox;
import io.github.bananapuncher714.brickboard.api.chat.ChatComponent;
import io.github.bananapuncher714.brickboard.api.chat.ChatMessage;
import io.github.bananapuncher714.brickboard.objects.Board;
import io.github.bananapuncher714.brickboard.util.MessageUtil;
import io.github.bananapuncher714.brickboard.util.Util;
import io.github.bananapuncher714.ngui.objects.BoxCoord;

/**
 * Designed to mimic default Minecraft chat, with the newest messages being pushed up from the bottom
 * 
 * @author BananaPuncher714
 */
public class ChatBoxChannel extends ChatBox {
	String channel;

	public ChatBoxChannel() {
		this( null );
	}
	
	/**
	 * Creates a channel window which shows a fixed channel
	 * @param channel
	 */
	public ChatBoxChannel( String channel ) {
		this.channel = channel;
	}

	@Override
	public List< ChatMessage > getMessages( Board board, Player player, BoxCoord coord ) {
		List< ChatMessage > messages = new ArrayList< ChatMessage >();
		int rows = coord.getHeight();
		BrickPlayer bPlayer = BrickPlayerManager.getInstance().getPlayer( player.getUniqueId() );
		List< ChatMessage > channel = bPlayer.getChannel( this.channel != null ? this.channel : bPlayer.getActiveChannelName() );

		int page = bPlayer.getPage();
		
		List< ChatMessage > chat = new ArrayList< ChatMessage >();
		int index = channel.size() - 1 - page;
		while ( chat.size() < rows ) {
			if ( index < 0 ) {
				break;
			}
			ChatMessage message = channel.get( index-- );
			ChatMessage[] split = MessageUtil.split( coord.getWidth(), message.clone(), FontManager.getInstance().getContainer( bPlayer.getFont() ) );
			for ( int i = split.length; i > 0; i--  ) {
				chat.add( split[ i - 1 ] );
				if ( chat.size() > rows - 1 ) {
					break;
				}
			}
		}
		int len = rows - chat.size();
		for ( int i = 0; i < len; i++ ) {
			chat.add( ChatMessage.getMessageFromString( ChatColor.RESET + " " ) );
		}
		
		Util.reverseList( chat );
		messages.addAll( chat );
		
		return messages;
	}

	@Override
	public ChatBox clone() {
		return new ChatBoxChannel( channel );
	}

	@Override
	public ConfigurationSection serialize() {
		ConfigurationSection config = new YamlConfiguration();
		config.set( "channel", channel );
		return config;
	}
	
	public static ChatBox deserialize( ConfigurationSection map ) {
		if ( map == null ) {
			return new ChatBoxChannel();
		}
		return new ChatBoxChannel( map.getString( "channel" ) );
	}
}
