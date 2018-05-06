package io.github.bananapuncher714.brickboard.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.DependencyManager;
import io.github.bananapuncher714.brickboard.chat.ChatComponent;
import io.github.bananapuncher714.brickboard.chat.ChatMessage;
import io.github.bananapuncher714.brickboard.objects.BoxCoord;

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
}
