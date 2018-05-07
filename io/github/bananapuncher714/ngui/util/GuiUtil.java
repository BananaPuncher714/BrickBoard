package io.github.bananapuncher714.ngui.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.inventory.ItemStack;

import io.github.bananapuncher714.ngui.objects.BoxCoord;

public class GuiUtil {

	/**
	 * Organize a set of BoxCoords to fit in a given width/height
	 * 
	 * @param containers
	 */
	public static void organize( Collection< BoxCoord > containers ) {
		organize( containers, 9, 6 );
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
	public static void organize( Collection< BoxCoord > containers, int width, int height ) {
		// Here starts the 4 loops to stretch in the 4 directions
		for ( int stretch = 1; stretch < 5; stretch ++ ) {
			Set< BoxCoord > remList = new TreeSet< BoxCoord >( containers );
			while ( !remList.isEmpty() ) {
				for ( Iterator< BoxCoord > it = remList.iterator(); it.hasNext(); ) {
					int w = 0, h = 0, a = 0, b = 0;
					switch( stretch ) {
					case 1: w = 1; break;
					case 2: h = 1; break;
					case 3: w = 1; a = -1; break;
					case 4: h = 1; b = -1; break;
					default: break;
					}
					BoxCoord coord = it.next();
					boolean overlap = false;
					// Check if any of them intersect
					for ( BoxCoord compCoord : containers ) {
						if ( compCoord.compareTo( coord ) == 0 ) {
							continue;
						}
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
	
	/**
	 * Convert a slot to Cartesian coordinates, with the origin being the top left and the highest values the bottom right
	 * 
	 * @param slot
	 * @param width
	 * @return
	 */
	public static int[] slotToCoord( int slot, int width ) {
		int x = slot % width;
		int y = ( slot - x ) / width;
		return new int[] { x, y };
	}
	
	/**
	 * Convert Cartesian coordinates into a slot number, inverse of {@link #slotToCoord( int, int ) slotToCoord} method 
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	public static int coordToSlot( int x, int y, int width, int height ) {
		if ( x > width || x < 0 || y > height || y < 0 ) return -1;
		return y * width + x;
	}
	
	/**
	 * Combine like ItemStacks into a new list; Excludes null items
	 * 
	 * @param items
	 * @return
	 */
	public static ArrayList< ItemStack > combine( List< ItemStack > items ) {
		ArrayList< ItemStack > sorted = new ArrayList< ItemStack >();
		for ( ItemStack item : items ) {
			if ( item == null ) {
				continue;
			}
			boolean putInPlace = false;
			for ( ItemStack sitem : sorted ) {
				if ( item.isSimilar( sitem ) ) {
					if ( item.getAmount() + sitem.getAmount() < sitem.getMaxStackSize() ) {
						sitem.setAmount( sitem.getAmount() + item.getAmount() );
						putInPlace = true;
					} else {
						item.setAmount( item.getAmount() - ( sitem.getMaxStackSize() - sitem.getAmount() ) );
						sitem.setAmount( sitem.getMaxStackSize() );
					}
					break;
				}
			}
			if ( !putInPlace ) {
				sorted.add( item );
			}
		}
		return sorted;
	}
	
	// TODO sort into method for getting values
//	InventoryPanel bigComponent = ( InventoryPanel ) clickedComponent;
//	int componentSlot = ( int ) ( e.getRawSlot() - clickedComponent.getSlot() - Math.floor( ( e.getRawSlot() - clickedComponent.getSlot() ) / 9.0 ) * ( 9 - clickedComponent.getWidth() ) );
//	ContentPane clickedPane = bigComponent.findPane( componentSlot );
//	int slot = ( int ) ( componentSlot - clickedPane.getSlot() - Math.floor( ( componentSlot - clickedPane.getSlot() ) / 9.0 ) * ( clickedComponent.getWidth() - clickedPane.getWidth() ) );

}
