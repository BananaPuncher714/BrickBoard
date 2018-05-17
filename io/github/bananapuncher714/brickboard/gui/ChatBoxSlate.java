package io.github.bananapuncher714.brickboard.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.DependencyManager;
import io.github.bananapuncher714.brickboard.api.ChatBox;
import io.github.bananapuncher714.brickboard.api.chat.ChatComponent;
import io.github.bananapuncher714.brickboard.api.chat.ChatMessage;
import io.github.bananapuncher714.ngui.objects.BoxCoord;

public class ChatBoxSlate extends ChatBox {
	ChatMessage message;
	
	public ChatBoxSlate( ChatMessage message ) {
		this.message = message;
	}

	@Override
	public List< ChatMessage > getMessages( Player player, BoxCoord coord ) {
		List< ChatMessage > messages = new ArrayList< ChatMessage >();
		ChatMessage message = this.message.clone();
		for ( ChatComponent component : message.getComponents()  ) {
			component.setText( DependencyManager.parse( player, component.getText() ) );
		}
		messages.add( message );
		return messages;
	}

	@Override
	public ChatBox clone() {
		return new ChatBoxSlate( message.clone() );
	}

	@Override
	public ConfigurationSection serialize() {
		return null;
	}
	
	public static ChatBox deserialize( ConfigurationSection map ) {
		return null;
	}
}
