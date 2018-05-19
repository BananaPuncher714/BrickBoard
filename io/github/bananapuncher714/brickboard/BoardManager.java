package io.github.bananapuncher714.brickboard;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import io.github.bananapuncher714.brickboard.api.ChatBox;
import io.github.bananapuncher714.brickboard.gui.ChatBoxBoardSelector;
import io.github.bananapuncher714.brickboard.gui.ChatBoxChannel;
import io.github.bananapuncher714.brickboard.gui.ChatBoxRainbow;
import io.github.bananapuncher714.brickboard.objects.Board;
import io.github.bananapuncher714.ngui.objects.BoxCoord;

public class BoardManager {
	private static BoardManager instance;
	
	private Map< UUID, Board > boards = new HashMap< UUID, Board >();
	
	public BoardManager() {
		setDefBoard();
	}
	
	private void setDefBoard() {
		Board defaultBoard = new Board( "default" );
		defaultBoard.setContainer( new ChatBoxRainbow( ChatColor.AQUA + "----[", " BrickBoard by BananaPuncher714 ", ChatColor.AQUA + "]----" ), new BoxCoord( 0, 0, 0, 0 ) );
		defaultBoard.setContainer( new ChatBoxBoardSelector(), new BoxCoord( 0, 1 ) );
		defaultBoard.setContainer( new ChatBoxChannel(), new BoxCoord( 0, 2 ) );
		defaultBoard.sort( true );
		addBoard( defaultBoard );
	}
	
	protected void loadBoards( File baseDir ) {
		if ( !baseDir.exists() ) {
			return;
		}
		ChatBoxManager boxManager = ChatBoxManager.getInstance();
		for ( File file : baseDir.listFiles() ) {
			FileConfiguration config = YamlConfiguration.loadConfiguration( file );
			String id = config.getString( "board-id" );
			if ( id == null ) {
				continue;
			}
			id = id.replace( " ", "" );
			Board board = new Board( id );
			for ( String key : config.getConfigurationSection( "boxes" ).getKeys( false ) ) {
				String[] coords = key.split( "/" );
				BoxCoord coord = new BoxCoord( Integer.parseInt( coords[ 0 ] ), Integer.parseInt( coords[ 1 ] ) );
				ChatBox preset = boxManager.getPreset( config.getString( "boxes" + "." + key ) );
				if ( preset == null ) {
					System.out.println( "Invalid preset found!" );
					continue;
				}
				board.setContainer( preset, coord );
			}
			board.sort( true );
			boards.put( board.getUUID(), board );
		}
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
	
	public Board getDefaultBoard() {
		Board board = getBoard( "default" );
		if ( board == null ) {
			setDefBoard();
		}
		return getBoard( "default" );
	}
	
	public Collection< Board > getBoards() {
		return boards.values();
	}
	
	public static final BoardManager getInstance() {
		if ( instance == null ) {
			instance = new BoardManager();
		}
		return instance;
	}
}
