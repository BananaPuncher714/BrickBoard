package io.github.bananapuncher714.brickboard.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.Board;
import io.github.bananapuncher714.brickboard.BoardManager;
import io.github.bananapuncher714.brickboard.chat.ChatComponent;
import io.github.bananapuncher714.brickboard.chat.ChatMessage;
import io.github.bananapuncher714.brickboard.chat.ClickAction;
import io.github.bananapuncher714.brickboard.chat.HoverAction;
import io.github.bananapuncher714.ngui.objects.BoxCoord;

public class ChatBoxBoardSelector extends ChatBox {
	BoardManager manager;
	
	public ChatBoxBoardSelector( BoardManager boardManager ) {
		manager = boardManager;
	}

	@Override
	public List< ChatMessage > getMessages( Player player, BoxCoord coord ) {
		List< ChatMessage > messages = new ArrayList< ChatMessage >();
		int i = 0;
		for ( Board board : manager.getBoards() ) {
			ChatMessage message = new ChatMessage();
			ChatComponent component = new ChatComponent( "[" + i + "]" );
			component.setClickAction( new ClickAction( ClickAction.Action.RUN_COMMAND, "/brickboard execute changeboard " + board.getId() ) ).setHoverAction( new HoverAction( HoverAction.Action.SHOW_TEXT, i + "\n" + board.getMessage( player ).getMessage() ) );
			message.addComponent( component );
			message.addComponent( new ChatComponent( " " ) );
			messages.add( message );
		}
		return messages;
	}
	
	@Override
	public ChatBox clone() {
		return new ChatBoxBoardSelector( manager );
	}
}
