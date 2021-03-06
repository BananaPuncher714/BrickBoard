package io.github.bananapuncher714.brickboard.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.BrickPlayer;
import io.github.bananapuncher714.brickboard.BrickPlayerManager;
import io.github.bananapuncher714.brickboard.api.ChatBox;
import io.github.bananapuncher714.brickboard.api.chat.ChatComponent;
import io.github.bananapuncher714.brickboard.api.chat.ChatMessage;
import io.github.bananapuncher714.brickboard.objects.Board;
import io.github.bananapuncher714.ngui.objects.BoxCoord;

public class ChatBoxTabCompletes extends ChatBox {

	@Override
	public List< ChatMessage > getMessages( Board board, Player player, BoxCoord coord ) {
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

	@Override
	public ConfigurationSection serialize() {
		return null;
	}
	
	public static ChatBox deserialize( ConfigurationSection map ) {
		return new ChatBoxTabCompletes();
	}
}
