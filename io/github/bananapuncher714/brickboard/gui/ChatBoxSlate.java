package io.github.bananapuncher714.brickboard.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.DependencyManager;
import io.github.bananapuncher714.brickboard.api.ChatBox;
import io.github.bananapuncher714.brickboard.api.chat.ChatComponent;
import io.github.bananapuncher714.brickboard.api.chat.ChatMessage;
import io.github.bananapuncher714.brickboard.objects.Board;
import io.github.bananapuncher714.ngui.objects.BoxCoord;

public class ChatBoxSlate extends ChatBox {
	ChatMessage message;
	
	public ChatBoxSlate( ChatMessage message ) {
		this.message = message;
	}

	@Override
	public List< ChatMessage > getMessages( Board board, Player player, BoxCoord coord ) {
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
		ConfigurationSection section = new YamlConfiguration();
		section.set( "message", message.toString() );
		return section;
	}
	
	public static ChatBox deserialize( ConfigurationSection map ) {
		if ( map == null ) {
			return null;
		}
		return new ChatBoxSlate( ChatMessage.getMessageFromString( map.getString( "message" ), true ) );
	}
}
