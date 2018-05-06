package io.github.bananapuncher714.brickboard.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.BrickPlayer;
import io.github.bananapuncher714.brickboard.BrickPlayerManager;
import io.github.bananapuncher714.brickboard.chat.ChatComponent;
import io.github.bananapuncher714.brickboard.chat.ChatMessage;
import io.github.bananapuncher714.brickboard.objects.BoxCoord;

public class ChatBoxTabCompletes extends ChatBox {

	@Override
	public List< ChatMessage > getMessages( Player player, BoxCoord coord ) {
		List< ChatMessage > messages = new ArrayList< ChatMessage >();
		BrickPlayer bPlayer = BrickPlayerManager.getInstance().getPlayer( player.getUniqueId() );
		
		String[] completes = bPlayer.getTabCompletes();
		if ( completes == null ) {
			return messages;
		}
		ChatMessage message = new ChatMessage();
		
		for ( int i = 0; i < completes.length; i++ ) {
			message.addComponent( new ChatComponent( completes[ i ] ) );
			if ( i < completes.length - 1 ) {
				message.addComponent( new ChatComponent( ", " ) );
			}
		}
		messages.add( message );
		
		return messages;
	}

	@Override
	public ChatBox clone() {
		return new ChatBoxTabCompletes();
	}
}
