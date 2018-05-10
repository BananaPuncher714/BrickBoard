package io.github.bananapuncher714.brickboard.objects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.github.bananapuncher714.brickboard.BrickBoard;
import io.github.bananapuncher714.brickboard.FontManager;
import io.github.bananapuncher714.brickboard.gui.ChatBox;
import io.github.bananapuncher714.ngui.objects.BoxCoord;
import io.github.bananapuncher714.ngui.util.GuiUtil;

public class BoardTemplate implements Cloneable {
	protected Map< ChatBox, BoxCoord > containers = new HashMap< ChatBox, BoxCoord >();
	protected String id;
	protected final int width, height;
	protected final UUID uuid;
	
	public BoardTemplate( String id ) {
		this( id, BrickBoard.CHAT_LEN, 20 );
	}
	
	public BoardTemplate( String id, int width, int height ) {
		this.id = id;
		this.width = width;
		this.height = height;
		uuid = UUID.randomUUID();
	}
	
	public String getId() {
		return id;
	}
	
	public void setId( String newId ) {
		id = newId;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setContainer( ChatBox container, BoxCoord coord ) {
		for ( BoxCoord value : containers.values() ) {
			if ( coord.compareTo( value ) == 0 ) {
				throw new IllegalArgumentException( "BoxCoord must not already exist in Board! At (" + coord.getX() + ", " + coord.getY() + ")" );
			}
		}
		containers.put( container, coord );
	}
	
	public Map< ChatBox, BoxCoord > getContainers() {
		return containers;
	}
	
	public void sort( boolean reset ) {
		if ( reset ) {
			for ( BoxCoord coord : containers.values() ) {
				coord.setHeight( 1 );
				coord.setWidth( 1 );
			}
		}
		GuiUtil.organize( containers.values(), width, height );
	}
	
	public Board build( FontManager manager ) {
		Board board = new Board( id, manager, false, width, height );
		for ( ChatBox box : containers.keySet() ) {
			board.setContainer( box.clone(), containers.get( box ).clone() );
		}
		return board;
	}
	
	@Override
	public BoardTemplate clone() {
		BoardTemplate template = new BoardTemplate( id, width, height );
		for ( ChatBox box : containers.keySet() ) {
			template.setContainer( box.clone(), containers.get( box ).clone() );
		}
		return template;
	}
}
