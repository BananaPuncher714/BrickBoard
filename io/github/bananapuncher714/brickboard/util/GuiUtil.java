package io.github.bananapuncher714.brickboard.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import io.github.bananapuncher714.brickboard.BrickBoard;
import io.github.bananapuncher714.brickboard.gui.ChatBox;
import io.github.bananapuncher714.brickboard.objects.BoxCoord;

public class GuiUtil {

	/**
	 * Organize a set of BoxCoords so that they fit within the Minecraft chat window
	 * 
	 * @param containers
	 */
	public static void organize( Map< ChatBox, BoxCoord > containers ) {
		organize( containers, BrickBoard.CHAT_LEN, 20 );
	}
	
	/**
	 * Extend a set of BoxCoords so that they take up the most space possible; Stretch right first, then down, left, and up
	 * 
	 * @param containers
	 * @param width
	 * Width of the rows in pixels
	 * @param height
	 * Amount of rows
	 */
	public static void organize( Map< ChatBox, BoxCoord > containers, int width, int height ) {
		// Here starts the 4 loops to stretch in the 4 directions
		for ( int stretch = 1; stretch < 5; stretch ++ ) {
			Set< ChatBox > remList = new HashSet< ChatBox >( containers.keySet() );
			while ( !remList.isEmpty() ) {
				for ( Iterator< ChatBox > it = remList.iterator(); it.hasNext(); ) {
					ChatBox rc = it.next();
					int w = 0, h = 0, a = 0, b = 0;
					switch( stretch ) {
					case 1: w = 1; break;
					case 2: h = 1; break;
					case 3: w = 1; a = -1; break;
					case 4: h = 1; b = -1; break;
					default: break;
					}
					BoxCoord coord = containers.get( rc );
					boolean overlap = false;
					// Check if any of them intersect
					for ( ChatBox ic : containers.keySet() ) {
						if ( ic == rc ) {
							continue;
						}
						BoxCoord compCoord = containers.get( ic );
						// See if the container is the only object
						if ( overlap( Math.max( 0, coord.getX() + a ), Math.max( 0, coord.getY() + b ), coord.getWidth() + w, coord.getHeight() + h, compCoord.getX(), compCoord.getY(), compCoord.getWidth(), compCoord.getHeight() ) ) {
							overlap = true;
							break;
						}
					}
					// If it doesn't overlap and it isn't the only object
					if ( overlap ) {
						it.remove();
					} else {
						coord.setX( coord.getX() + a );
						coord.setY( coord.getY() + b );
						coord.setWidth( coord.getWidth() + w );
						coord.setHeight( coord.getHeight() + h );
						
						boolean outOfBounds = false;
						if ( coord.getX() < 0 || coord.getX() > width ) {
							coord.setX( Math.min( width - 1, Math.max( 0, coord.getX() ) ) );
							outOfBounds = true;
						}
						if ( coord.getY() < 0 || coord.getY() > height - 1 ) {
							coord.setY( Math.min( height, Math.max( 0, coord.getY() ) ) );
							outOfBounds = true;
						}
						if ( coord.getWidth() < 0 || coord.getWidth() > width - coord.getX() ) {
							coord.setWidth( Math.min( width - coord.getX(), Math.max( 0, coord.getWidth() ) ) );
							outOfBounds = true;
						}
						if ( coord.getHeight() < 0 || coord.getHeight() > height - coord.getY() ) {
							coord.setHeight( Math.min( height - coord.getY(), Math.max( 0, coord.getHeight() ) ) );
							outOfBounds = true;
						}
						if ( outOfBounds ) {
							it.remove();
						}
					}
				}
			}
		}
	}
	
	private static boolean overlap( int x, int y, int w, int h, int a, int b, int i, int e  ) {
		return x < a + i && x + w > a && y < b + e && y + h > b;
	}
}
