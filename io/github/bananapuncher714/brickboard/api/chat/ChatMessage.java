package io.github.bananapuncher714.brickboard.api.chat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.ChatColor;

import io.github.bananapuncher714.brickboard.util.MessageUtil;

/**
 * Parallel implementation of vanilla style JSON formatting, except simpler
 * 
 * @author BananaPuncher714
 */
public class ChatMessage implements Cloneable, Serializable {
	private static String NEW_LINE_TOKEN = "-n";
	private final static char PLACEHOLDER = '\u02A7';
	private final List< ChatComponent > components = new ArrayList< ChatComponent >();

	public List< ChatComponent > getComponents() {
		return components;
	}

	public ChatMessage addComponent( ChatComponent component ) {
		if ( component != null ) {
			components.add( component );
		}
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
		StringBuilder builder = new StringBuilder();
		for ( ChatComponent component : components ) {
			builder.append( component.toString() );
		}
		return builder.toString();
	}
	
	public static void setNewLineToken(String newToken) {
		NEW_LINE_TOKEN = newToken;	
	}

	public static ChatMessage getMessageFromString( String message ) {
		return getMessageFromString( message, false );
	}
	
	public static ChatMessage getMessageFromString( String message, boolean readExtra ) {
		ChatMessage chatMessage = new ChatMessage();

		if ( message == null ) {
			return chatMessage;
		}
		
		ChatComponent last = new ChatComponent( "" );

		List< ChatMessage > actions = new ArrayList< ChatMessage >();
		if ( readExtra ) {
			// Thanks to StarShadow#3546 for negative look behind
			List< String > caught = MessageUtil.getMatches( message, "<(.+?)(?<!\\\\)>" );
			for ( String action : caught ) {
				String hover = MessageUtil.getMatch( action, "\\{(.+?)(?<!\\\\)\\}" );
				String click = MessageUtil.getMatch( action, "\\((.+?\\:.+?)(?<!\\\\)\\)" );
				if ( hover == null && click == null ) {
					// Requires a click or hover
					continue;
				}
				String desc = action;
				HoverAction hAction = null;
				ClickAction cAction = null;
				if ( click != null ) {
					String[] clickSplit = click.split( "\\:", 2 );
					ClickAction.Action cActionAction;
					try {
						cActionAction = ClickAction.Action.valueOf( clickSplit[ 0 ].toUpperCase() );
					} catch ( IllegalArgumentException exception ) {
						cActionAction = null;
					}
					if ( cActionAction == null ) {
						if ( hover == null ) {
							continue;
						}
					} else {
						cAction = new ClickAction( cActionAction, clickSplit[ 1 ] );
						desc = desc.replace( "(" + click + ")", "" );
					}
				}
				if ( hover != null ) {
					hAction = new HoverAction( HoverAction.Action.SHOW_TEXT, hover.replace(NEW_LINE_TOKEN, "\n") );
					desc = desc.replace( "{" + hover + "}", "" );
				}
				ChatMessage description = ChatMessage.getMessageFromString( desc, false );
				for ( ChatComponent component : description.components ) {
					component.setHoverAction( hAction );
					component.setClickAction( cAction );
				}
				message = message.replace( "<" + action + ">", "" + ChatColor.COLOR_CHAR + PLACEHOLDER );
				actions.add( description );
			}
		}
		
		// More efficient conversion
		String[] parts = ( " " + message ).split( "" + ChatColor.COLOR_CHAR );
		for ( String part : parts ) {
			if ( part.isEmpty() ) {
				continue;
			}
			char colorCharacter = part.charAt( 0 );
			if ( colorCharacter == PLACEHOLDER ) {
				chatMessage.merge( actions.remove( 0 ) );
			} else {
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
