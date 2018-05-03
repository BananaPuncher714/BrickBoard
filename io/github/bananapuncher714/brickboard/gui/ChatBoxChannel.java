package io.github.bananapuncher714.brickboard.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.BrickBoard;
import io.github.bananapuncher714.brickboard.BrickPlayer;
import io.github.bananapuncher714.brickboard.BrickPlayerManager;
import io.github.bananapuncher714.brickboard.chat.ChatComponent;
import io.github.bananapuncher714.brickboard.chat.ChatMessage;
import io.github.bananapuncher714.brickboard.objects.BoxCoord;
import io.github.bananapuncher714.brickboard.util.MessageUtil;
import io.github.bananapuncher714.brickboard.util.Util;

/**
 * Designed to mimic default Minecraft chat, with the newest messages being pushed up from the bottom
 * 
 * @author BananaPuncher714
 */
public class ChatBoxChannel extends ChatBox {
	String channel;
	
	/**
	 * Creates a channel window which shows the player's active channel
	 */
	public ChatBoxChannel() {
		channel = null;
	}

	/**
	 * Creates a channel window which shows a fixed channel
	 * @param channel
	 */
	public ChatBoxChannel( String channel ) {
		this.channel = channel;
	}

	@Override
	public List< ChatMessage > getMessages( Player player, BoxCoord coord ) {
		List< ChatMessage > messages = new ArrayList< ChatMessage >();
		int rows = coord.getHeight();
		BrickPlayer bPlayer = BrickPlayerManager.getInstance().getPlayer( player.getUniqueId() );
		List< ChatMessage > channel = bPlayer.getChannel( this.channel != null ? this.channel : bPlayer.getActiveChannelName() );

		List< ChatMessage > chat = new ArrayList< ChatMessage >();
		int index = channel.size() - 1;
		while ( chat.size() < rows ) {
			if ( index < 0 ) {
				break;
			}
			ChatMessage message = channel.get( index-- );
			ChatMessage[] split = MessageUtil.split( coord.getWidth(), message.clone(), BrickBoard.getInstance().getDefaultFont() );
			for ( int i = split.length; i > 0; i--  ) {
				chat.add( split[ i - 1 ] );
				if ( chat.size() > rows - 1 ) {
					break;
				}
			}
		}
		int len = rows - chat.size();
		for ( int i = 0; i < len; i++ ) {
			chat.add( ChatMessage.getMessageFromString( " " ) );
		}
		
		Util.reverseList( chat );
		messages.addAll( chat );
		
		return messages;
	}

}
