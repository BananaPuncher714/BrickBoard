package io.github.bananapuncher714.brickboard.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.BoardManager;
import io.github.bananapuncher714.brickboard.api.ChatBox;
import io.github.bananapuncher714.brickboard.api.chat.ChatComponent;
import io.github.bananapuncher714.brickboard.api.chat.ChatMessage;
import io.github.bananapuncher714.brickboard.api.chat.ClickAction;
import io.github.bananapuncher714.brickboard.api.chat.HoverAction;
import io.github.bananapuncher714.brickboard.objects.Board;
import io.github.bananapuncher714.ngui.objects.BoxCoord;

public class ChatBoxBoardSelector extends ChatBox {
	BoardManager manager;
	
	public ChatBoxBoardSelector( BoardManager boardManager ) {
		manager = boardManager;
	}

	@Override
	public List< ChatMessage > getMessages( Player player, BoxCoord coord ) {
		List< ChatMessage > messages = new ArrayList< ChatMessage >();
		ChatMessage message = new ChatMessage();
		for ( Board board : manager.getBoards() ) {
			ChatComponent component = new ChatComponent( "[" + board.getId() + "]" );
			component.setClickAction( new ClickAction( ClickAction.Action.RUN_COMMAND, "/brickboard execute changeboard " + board.getId() ) );
			component.setHoverAction( new HoverAction( HoverAction.Action.SHOW_TEXT, "Board Preview " + board.getId() + "\n" + board.getMessage( player ).getMessage() ) );
			message.addComponent( component );
			message.addComponent( new ChatComponent( " " ) );
		}
		messages.add( message );
		
		return messages;
	}
	
	@Override
	public ChatBox clone() {
		return new ChatBoxBoardSelector( manager );
	}

	@Override
	public ConfigurationSection serialize() {
		return null;
	}
	
	public static ChatBox deserialize( ConfigurationSection map ) {
		return null;
	}
}
