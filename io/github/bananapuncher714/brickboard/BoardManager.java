package io.github.bananapuncher714.brickboard;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;

import io.github.bananapuncher714.brickboard.gui.ChatBoxBoardSelector;
import io.github.bananapuncher714.brickboard.gui.ChatBoxChannel;
import io.github.bananapuncher714.brickboard.gui.ChatBoxRainbow;
import io.github.bananapuncher714.ngui.objects.BoxCoord;

public class BoardManager {
	private Map< String, Board > boards = new HashMap< String, Board >();
	private Board defaultBoard;
	private BrickBoard main;
	
	public BoardManager( BrickBoard plugin ) {
		main = plugin;
		defaultBoard = new Board( "default_board", main.getFontManager() );
		defaultBoard.setContainer( new ChatBoxRainbow( ChatColor.AQUA + "----[", " BrickBoard by BananaPuncher714 ", ChatColor.AQUA + "]----" ), new BoxCoord( 0, 0, 0, 0 ) );
		defaultBoard.setContainer( new ChatBoxBoardSelector( this ), new BoxCoord( 0, 1 ) );
		defaultBoard.setContainer( new ChatBoxChannel( main.getFontManager() ), new BoxCoord( 0, 2 ) );
		defaultBoard.sort( true );
	}
	
	public void addBoard( Board board ) {
		boards.put( board.getId(), board );
	}
	
	public Board getBoard( String id ) {
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
}
