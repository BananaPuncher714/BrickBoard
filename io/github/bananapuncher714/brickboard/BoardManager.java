package io.github.bananapuncher714.brickboard;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;

import io.github.bananapuncher714.brickboard.gui.ChatBox;
import io.github.bananapuncher714.brickboard.gui.ChatBoxBoardSelector;
import io.github.bananapuncher714.brickboard.gui.ChatBoxChannel;
import io.github.bananapuncher714.brickboard.gui.ChatBoxRainbow;
import io.github.bananapuncher714.brickboard.objects.Board;
import io.github.bananapuncher714.brickboard.objects.BoardTemplate;
import io.github.bananapuncher714.ngui.objects.BoxCoord;

public class BoardManager {
	private static BoardManager instance;
	
	private Map< UUID, Board > boards = new HashMap< UUID, Board >();
	private Map< UUID, BoardTemplate > templates = new HashMap< UUID, BoardTemplate >();
	private Map< String, ChatBox > chatBoxes = new HashMap< String, ChatBox >();
	
	private Board defaultBoard;
	
	public BoardManager() {
		defaultBoard = new Board( "default_board", FontManager.getInstance() );
		defaultBoard.setContainer( new ChatBoxRainbow( ChatColor.AQUA + "----[", " BrickBoard by BananaPuncher714 ", ChatColor.AQUA + "]----" ), new BoxCoord( 0, 0, 0, 0 ) );
		defaultBoard.setContainer( new ChatBoxBoardSelector( this ), new BoxCoord( 0, 1 ) );
		defaultBoard.setContainer( new ChatBoxChannel( FontManager.getInstance() ), new BoxCoord( 0, 2 ) );
		defaultBoard.sort( true );
	}
	
	public void addTemplate( BoardTemplate template ) {
		templates.put( template.getUUID(), template );
	}
	
	public BoardTemplate getTemplate( UUID uuid ) {
		return templates.get( uuid );
	}
	
	public BoardTemplate getTemplate( String id ) {
		for ( BoardTemplate board : templates.values() ) {
			if ( board.getId().equalsIgnoreCase( id ) ) {
				return board;
			}
		}
		return null;
	}
	
	public Collection< BoardTemplate > getTemplates() {
		return templates.values();
	}
	
	public void addBoard( Board board ) {
		boards.put( board.getUUID(), board );
	}
	
	public Board getBoard( String id ) {
		for ( Board board : boards.values() ) {
			if ( board.getId().equalsIgnoreCase( id ) ) {
				return board;
			}
		}
		return null;
	}
	
	public Board getBoard( UUID id ) {
		return boards.get( id );
	}
	
	public void setDefaultBoard( Board board ) {
		defaultBoard = board;
	}
	
	public Board getDefaultBoard() {
		return defaultBoard;
	}
	
	public Collection< Board > getBoards() {
		return boards.values();
	}
	
	public void registerChatBox( ChatBox chatbox ) {
		
	}
	
	public static final BoardManager getInstance() {
		if ( instance == null ) {
			instance = new BoardManager();
		}
		return instance;
	}
}
