package io.github.bananapuncher714.brickboard.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.BrickBoard;
import io.github.bananapuncher714.brickboard.api.ChatBox;
import io.github.bananapuncher714.brickboard.api.chat.ChatComponent;
import io.github.bananapuncher714.brickboard.api.chat.ChatMessage;
import io.github.bananapuncher714.brickboard.objects.MinecraftFontContainer;
import io.github.bananapuncher714.ngui.objects.BoxCoord;

/**
 * Utility class for handling ChatMessages and manipulating them
 * 
 * @author BananaPuncher714
 */
public class MessageUtil {

	/**
	 * Get the length of a given ChatMessage
	 * 
	 * @param message
	 * @param container
	 * @return
	 */
	public static int lengthOf( ChatMessage message, MinecraftFontContainer container ) {
		if ( message == null ) {
			return -1;
		}
		int len = 0;
		for ( ChatComponent component : message.getComponents() ) {
			if ( component.getText().contains( "" + ChatColor.COLOR_CHAR ) ) {
				ChatMessage noLegacy = ChatMessage.getMessageFromString( component.getLegacyText() );
				len = len + lengthOf( noLegacy, container );
			} else {
				len = len + container.getStringWidth( component.getLegacyText(), component.isBold() );
			}
		}
		
		return len;
	}
	
	/**
	 * Split a given ChatMessage into messages of the width provided; Has a buffer of 2 pixels
	 * 
	 * @param width
	 * @param message
	 * @param container
	 * @return
	 */
	public static ChatMessage[] split( final int width, ChatMessage message, MinecraftFontContainer container ) {
		// This is so the extend method can work properly, because there is no character with a width of 1
		final int PADDING = 2;

		int len = lengthOf( message, container );
		if ( len < 1 ) {
			return new ChatMessage[ 0 ];
		}
		if ( len + 2 <= width && !message.getMessage().contains( "\n" ) ) {
			return new ChatMessage[] { message };
		}
		
		List< ChatComponent > splitComponents = new ArrayList< ChatComponent >( message.clone().getComponents() );

		ChatMessage[] array = new ChatMessage[ ( int ) Math.ceil( len / ( double ) ( width + PADDING  ) ) + StringUtils.countMatches( message.getMessage(), "\n" ) ];
		ChatMessage cur = new ChatMessage();
		int i = 0;

		Util.reverseList( splitComponents );
		while ( !splitComponents.isEmpty() ) {
			ChatComponent component = splitComponents.remove( splitComponents.size() - 1 );

			if ( component == null || component.getText().isEmpty() ) {
				continue;
			}
			
			// Width of component
			int curLen = container.getStringWidth( component.getText(), component.isBold() );
			
			// Width of the message
			int messageLen = lengthOf( cur, container );

			if ( messageLen + curLen + PADDING <= width ) {
				int newlineIndex = component.getText().indexOf( "\n" );
				if ( newlineIndex > -1 ) {
					splitComponents.add( component.clone().setText( component.getText().substring( newlineIndex + 1 ) ) );
					cur.addComponent( component.clone().setText( component.getText().substring( 0, newlineIndex ) ) );
					array[ i++ ] = cur;
					cur = new ChatMessage();
				} else {
					cur.addComponent( component.clone() );
				}
				continue;
			}

			// Amount of characters of component
			int characters = component.getText().length();
			// Width difference between the message and the max
			int diff = width - ( messageLen + PADDING );
			// Average width of characters in component, is larger than 1
			double avgWidth = curLen / ( double ) characters;
			// Sub is how many characters it would take to fill up the rest of the message
			int sub = ( int ) ( diff / avgWidth );
			
			String fore = component.getText().substring( 0, sub );
			String aft = component.getText().substring( sub );
			curLen = container.getStringWidth( fore, component.isBold() );

			// We can add to aft and subtract from fore
			while ( curLen + messageLen + PADDING > width ) {
				aft = fore.substring( fore.length() - 1 ) + aft;
				fore = fore.substring( 0, fore.length() - 1 );
				if ( fore.isEmpty() ) {
					break;
				}
				curLen = container.getStringWidth( fore, component.isBold() );
			}

			// We know there has to be extra, so we put it back into the list
			if ( !fore.isEmpty() ) {
				int index = fore.indexOf( "\n" );
				if ( index > -1 ) {
					splitComponents.add( component.clone().setText( fore.substring( index + 1 ) ) );
					cur.addComponent( component.clone().setText( fore.substring( index ) ) );
				} else {
					cur.addComponent( component.clone().setText( fore ) );
				}
			}

			splitComponents.add( component.clone().setText( aft ) );
			
			array[ i++ ] = cur;
			
			// This should never be thrown
			if ( i >= array.length ) {
				return array;
			}
			cur = new ChatMessage();
		}
		if ( i < array.length ) {
			array[ i ] = cur;
		}
		return array;
	}

	/**
	 * @deprecated
	 */
	public static ChatComponent[] split( int width, ChatComponent component, MinecraftFontContainer container ) {
		if ( component == null || component.getText().isEmpty() ) {
			return new ChatComponent[ 0 ];
		}
		int len = container.getStringWidth( component.getText(), component.isBold() );
		ChatComponent[] components = new ChatComponent[ ( int ) Math.ceil( len / ( double ) width ) ];
		ChatComponent builder = component.clone().setText( "" );
		StringBuilder sB = new StringBuilder();
		int i = 0;
		for ( char ch : component.getText().toCharArray() ) {
			if ( container.getStringWidth( sB.toString() + ch, builder.isBold() ) > width || ch == '\n' ) {
				builder.setText( sB.toString() );
				components[ i++ ] = builder;
				sB = new StringBuilder();
				builder = component.clone().setText( "" );
			}

			if ( ch != '\n' ) {
				sB.append( ch );
			}
		}
		if ( i < components.length ) {
			components[ i ] = builder.setText( sB.toString() );
		}

		return components;
	}

	/**
	 * Extend a ChatMessage to the given width exactly, uses randomly placed periods
	 * 
	 * @param width
	 * @param message
	 * @param container
	 * @return
	 */
	public static boolean extend( int width, ChatMessage message, MinecraftFontContainer container ) {
		int len = lengthOf( message, container );
		int spaceWidth = container.getCharWidth( ' ' );
		int diff = width - len;
		if ( len >= width ) {
			return false;
		}
		int spaces = diff / spaceWidth;
		int spaceLeft = diff - ( spaces * spaceWidth );
		if ( spaceLeft == 1 ) {
			spaces = spaces - 1;
		}
		String repeated = "";
		if ( spaces > 0 ) {
			repeated = StringUtils.repeat( ' ', spaces );
		}
		if ( spaceLeft == 2 ) {
			repeated = insertRandomChar( repeated, 'A' );
		} else if ( spaceLeft == 3 ) {
			repeated = insertRandomChar( repeated, 'B' );
		} else if ( spaceLeft == 1 ) {
			repeated = insertRandomChar( repeated, 'A' );
			repeated = insertRandomChar( repeated, 'B' );
		}
		repeated = repeated.replace( "A", ChatColor.DARK_GRAY + "." + ChatColor.RESET );
		repeated = repeated.replace( "B", ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + "." + ChatColor.RESET );
		message.merge( ChatMessage.getMessageFromString( repeated ) );
		return true;
	}

	/**
	 * Insert a random character into a string, excluding the first and last position; Does not replace
	 * 
	 * @param message
	 * @param characters
	 * @return
	 */
	public static String insertRandomChar( String message, char character ) {
		int max = message.length() - 1;
		int place = ( int ) ( Math.random() * ( max - 1 ) + 1 );
		return message.substring( 0, place ) + character + message.substring( place );
	}
	
	/**
	 * Center a given message to the width provided, and pad the edges with the character given
	 * 
	 * @param message
	 * @param width
	 * @param space
	 * @param container
	 * @return
	 * The original message if the length exceeds the width
	 */
	public static ChatMessage center( ChatMessage message, int width, char space, MinecraftFontContainer container ) {
		int len = lengthOf( message, container );
		if ( len < width ) {
			int diff = ( width - len ) / ( 2 * container.getCharWidth( space ) );
			String repeated = StringUtils.repeat( space, diff );
			message = new ChatMessage().addComponent( new ChatComponent( repeated ) ).merge( message ).addComponent( new ChatComponent( repeated ) );
		}
		return message;
	}
	
	// TODO do this, eventually?
//	public static boolean translateTabs( ChatMessage message, BoxCoord coord, int tabWidth, MinecraftFontContainer container ) {
//		if ( message == null ) {
//			return false;
//		}
//		
//		
//		return true;
//	}

	/**
	 * Split the output from a ChatBox and extend the parts up to the width described in the width of the BoxCoord
	 * 
	 * @param player
	 * @param container
	 * @param coord
	 * @param fontContainer
	 * @return
	 */
	public static ChatMessage[] truncateAndExtend( Player player, ChatBox container, BoxCoord coord, int overallWidth, MinecraftFontContainer fontContainer ) {
		ChatMessage[] lines = new ChatMessage[ coord.getHeight() ];
		int width = coord.getWidth();
		if ( width == 0 ) {
			throw new IllegalArgumentException( "Invalid dimensions for a BoxCoord! Width must be at least 1!" );
		}
		List< ChatMessage > buffer = container.getMessages( player, coord );
		List< ChatMessage > splitted = new ArrayList< ChatMessage >();
		boolean isEOL = coord.getX() + coord.getWidth() >= overallWidth;
		for ( ChatMessage raw : buffer ) {
			for ( ChatMessage split : split( width, raw, fontContainer ) ) {
				if ( split == null ) {
					continue;
				}
				if ( !isEOL ) {
					extend( width, split, fontContainer );
				}
				splitted.add( split );
			}
		}
		for ( int i = 0; i < lines.length; i++ ) {
			if ( splitted.size() <= i ) {
				ChatMessage whitespace = new ChatMessage();
				lines[ i ] = whitespace; 
				if ( !isEOL ) {
					extend( width, whitespace, fontContainer );
				}
			} else {
				lines[ i ] = splitted.get( i );
			}
		}
		return lines;
	}

}
