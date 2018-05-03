package io.github.bananapuncher714.brickboard.chat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.ChatColor;

public class ChatMessage implements Cloneable {
	private final List< ChatComponent > components = new ArrayList< ChatComponent >();

	public List< ChatComponent > getComponents() {
		return components;
	}

	public ChatMessage addComponent( ChatComponent component ) {
		components.add( component );
		return this;
	}

	public ChatMessage addComponents( String message ) {
		ChatMessage cm = getMessageFromString( message );
		components.addAll( cm.getComponents() );
		return this;
	}

	public ChatMessage merge( ChatMessage... toMerge ) {
		for ( ChatMessage message : toMerge ) {
			if ( message == null ) {
				continue;
			}
			components.addAll( message.clone().components );
		}
		return this;
	}

	public ChatMessage merge( Collection< ChatMessage > toMerge ) {
		for ( ChatMessage message : toMerge ) {
			if ( message == null ) {
				continue;
			}
			components.addAll( message.clone().components );
		}
		return this;
	}

	public String getMessage() {
		StringBuilder builder = new StringBuilder();
		for ( ChatComponent component : components ) {
			builder.append( component.getLegacyText() );
		}
		return builder.toString();
	}

	@Override
	public ChatMessage clone() {
		ChatMessage message = new ChatMessage();
		for ( ChatComponent component : components ) {
			message.addComponent( component.clone() );
		}
		return message;
	}

	@Override
	public String toString() {
		return getMessage();
	}

	public static ChatMessage getMessageFromString( String message ) {
		ChatMessage chatMessage = new ChatMessage();

		ChatComponent last = new ChatComponent( "" );

		// More efficient conversion
		String[] parts = ( " " + message ).split( "" + ChatColor.COLOR_CHAR );
		for ( String part : parts ) {
			if ( part.isEmpty() ) {
				continue;
			}
			char colorCharacter = part.charAt( 0 );
			ChatColor color = ChatColor.getByChar( colorCharacter );

			if ( color != null ) {
				if ( color.isColor() ) {
					last.clearFormatting();
					last.setColor( color );
				} else {
					if ( color == ChatColor.BOLD ) {
						last.setBold( true );
					}
					if ( color == ChatColor.ITALIC ) {
						last.setItalic( true );
					}
					if ( color == ChatColor.UNDERLINE ) {
						last.setUnderline( true );
					}
					if ( color == ChatColor.MAGIC ) {
						last.setRandom( true );
					}
					if ( color == ChatColor.RESET ) {
						last.clearFormatting();
					}
					if ( color == ChatColor.STRIKETHROUGH ) {
						last.setStrikethrough( true );
					}
				}
			}
			if ( part.length() > 1 ) {
				last.setText( part.substring( 1 ) );
				chatMessage.addComponent( last );
				last = last.clone();
			}
		}

		return chatMessage;
	}
}