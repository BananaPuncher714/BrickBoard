package io.github.bananapuncher714.brickboard.implementation.v1_12_R1;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.BrickBoard;
import io.github.bananapuncher714.brickboard.BrickPlayer;
import io.github.bananapuncher714.brickboard.BrickPlayerManager;
import io.github.bananapuncher714.brickboard.chat.ChatComponent;
import io.github.bananapuncher714.brickboard.chat.ChatMessage;
import io.github.bananapuncher714.brickboard.chat.ClickAction;
import io.github.bananapuncher714.brickboard.chat.HoverAction;
import io.github.bananapuncher714.brickboard.events.PlayerRecieveMessageEvent;
import io.github.bananapuncher714.brickboard.implementation.API.PacketHandler;
import net.minecraft.server.v1_12_R1.ChatClickable;
import net.minecraft.server.v1_12_R1.ChatClickable.EnumClickAction;
import net.minecraft.server.v1_12_R1.ChatComponentText;
import net.minecraft.server.v1_12_R1.ChatHoverable;
import net.minecraft.server.v1_12_R1.ChatHoverable.EnumHoverAction;
import net.minecraft.server.v1_12_R1.ChatMessageType;
import net.minecraft.server.v1_12_R1.ChatModifier;
import net.minecraft.server.v1_12_R1.EnumChatFormat;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import net.minecraft.server.v1_12_R1.PacketPlayOutTabComplete;

public class BBPacketHandler implements PacketHandler {
	private static final Field FIELD_COMPONENT;
	private static final Field FIELD_TAB_COMPLETES;

	static {
		Field field1 = null;
		Field field3 = null;
		try {
			field1 = PacketPlayOutChat.class.getDeclaredField( "a" );
			field3 = PacketPlayOutTabComplete.class.getDeclaredField( "a" );
		} catch ( NoSuchFieldException | SecurityException e ) {
			e.printStackTrace();
		}
		FIELD_COMPONENT = field1;
		FIELD_COMPONENT.setAccessible( true );
		
		FIELD_TAB_COMPLETES = field3;
		FIELD_TAB_COMPLETES.setAccessible( true );
	}

	@Override
	public boolean handlePacket( Player player, Object packet ) {
		if ( packet == null ) {
			return true;
		} else if ( packet instanceof PacketPlayOutChat ) {
			return ( handleChatPacket( player, ( PacketPlayOutChat ) packet ) );
		} else if ( packet instanceof PacketPlayOutTabComplete ) {
			return ( handleTabCompletePacket( player, ( PacketPlayOutTabComplete ) packet ) );
		} else {
			return true;
		}
	}

	private boolean handleChatPacket( Player player, PacketPlayOutChat packet ) {
		IChatBaseComponent component;
		if ( packet.c() == ChatMessageType.GAME_INFO ) {
			return true;
		}
			
		try {
			component = ( IChatBaseComponent ) FIELD_COMPONENT.get( packet );
		} catch ( SecurityException | IllegalArgumentException | IllegalAccessException e ) {
			e.printStackTrace();
			return false;
		}
		
		if ( removeSnowman( component ) ) {
			return true;
		}
		ChatMessage message = convert( component );
		PlayerRecieveMessageEvent event = new PlayerRecieveMessageEvent( player, message );
		Bukkit.getScheduler().scheduleSyncDelayedTask( BrickBoard.getInstance(), new Runnable() {
			@Override
			public void run() {
				Bukkit.getPluginManager().callEvent( event );
			}
		} );
		if ( event.isCancelled() ) {
			return false;
		}
		BrickPlayer bPlayer = BrickPlayerManager.getInstance().getPlayer( player.getUniqueId() );
		if ( event.getMessage() != null ) {
			bPlayer.addToLog( event.getMessage() );
		}

		return false;
	}

	private boolean handleTabCompletePacket( Player player, PacketPlayOutTabComplete packet ) {
		String[] arguments;
		try {
			arguments = ( String[] ) FIELD_TAB_COMPLETES.get( packet );
		} catch ( IllegalArgumentException | IllegalAccessException e ) {
			return true;
		}
		BrickPlayer bPlayer = BrickPlayerManager.getInstance().getPlayer( player.getUniqueId() );
		bPlayer.setTabCompletes( arguments );
		
		return true;
	}

	@Override
	public void sendMessage( Player player, String message ) {
		PacketPlayOutChat packet = new PacketPlayOutChat( new ChatComponentText( BrickBoard.SEND_CHAR + message ) );
		( ( CraftPlayer ) player ).getHandle().playerConnection.sendPacket( packet );
	}

	@Override
	public void sendMessage( Player player, ChatMessage component ) {
		IChatBaseComponent icbc = convert( component, true );
		if ( icbc == null ) {
			return;
		}
		PacketPlayOutChat packet = new PacketPlayOutChat( icbc, ChatMessageType.CHAT );
		( ( CraftPlayer ) player ).getHandle().playerConnection.sendPacket( packet );
	}

	private IChatBaseComponent convert( ChatMessage message, boolean compress ) {
		if ( message.getComponents().isEmpty() ) {
			return null;
		}
		IChatBaseComponent icbc = new ChatComponentText( BrickBoard.SEND_CHAR + "" );
		if ( !compress ) {
			for ( ChatComponent component : message.getComponents() ) {
				ChatComponentText textComponent = new ChatComponentText( component.getText() );
				ChatModifier modifier = textComponent.getChatModifier();
				modifier.setColor( EnumChatFormat.valueOf( component.getColor().name() ) );
				modifier.setBold( component.isBold() );
				modifier.setItalic( component.isItalic() );
				modifier.setRandom( component.isRandom() );
				modifier.setStrikethrough( component.isStrikethrough() );
				modifier.setUnderline( component.isUnderline() );

				ClickAction cAction = component.getClickAction();
				if ( cAction != null ) {
					ChatClickable clickable = new ChatClickable( EnumClickAction.valueOf( cAction.getAction().name() ), cAction.getMessage() );
					modifier.setChatClickable( clickable );
				}

				HoverAction hAction = component.getHoverAction();
				if ( hAction != null ) {
					ChatHoverable hoverable = new ChatHoverable( EnumHoverAction.valueOf( hAction.getAction().name() ), new ChatComponentText( hAction.getMessage() ) );
					modifier.setChatHoverable( hoverable );
				}

				icbc.a().add( textComponent );
			}
		} else {
			StringBuilder builder = new StringBuilder();
			ChatComponent last = null;
			HoverAction lastHover = null;
			ClickAction lastClick = null;
			boolean set = false, wasBold = false;
			for ( ChatComponent component : message.getComponents() ) {
				last = component;
				ClickAction cAction = component.getClickAction();
				HoverAction hAction = component.getHoverAction();
				
				if ( !set ) {
					set = true;
					wasBold = component.isBold();
					lastClick = cAction;
					lastHover = hAction;
				}
				
				boolean reqNew = false;
				reqNew = wasBold != component.isBold();
				if ( !reqNew && cAction != null ) {
					reqNew = !cAction.equals( lastClick );
				} else if ( !reqNew && lastClick != null ) {
					reqNew = !lastClick.equals( cAction );
				} else if ( !reqNew && hAction != null ) {
					reqNew = !hAction.equals( lastHover );
				} else if ( !reqNew && lastHover != null ) {
					reqNew = !lastHover.equals( hAction );
				}
				
				if ( reqNew ) {
					ChatComponentText text = new ChatComponentText( builder.toString() );
					ChatModifier modifier = text.getChatModifier();
					
					if ( lastClick != null ) {
						ChatClickable clickable = new ChatClickable( EnumClickAction.valueOf( lastClick.getAction().name() ), lastClick.getMessage() );
						modifier.setChatClickable( clickable );
					}

					if ( lastHover != null ) {
						ChatHoverable hoverable = new ChatHoverable( EnumHoverAction.valueOf( lastHover.getAction().name() ), new ChatComponentText( lastHover.getMessage() ) );
						modifier.setChatHoverable( hoverable );
					}
					
					icbc.a().add( text );
					
					builder = new StringBuilder();
				}
				builder.append( component.getLegacyText() );
				lastClick = cAction;
				lastHover = hAction;
				wasBold = component.isBold();
			}
			if ( builder.length() > 0 && last != null ) {
				ChatComponentText text = new ChatComponentText( builder.toString() );
				ChatModifier modifier = text.getChatModifier();
				
				ClickAction cAction = last.getClickAction();
				if ( cAction != null ) {
					ChatClickable clickable = new ChatClickable( EnumClickAction.valueOf( cAction.getAction().name() ), cAction.getMessage() );
					modifier.setChatClickable( clickable );
				}

				HoverAction hAction = last.getHoverAction();
				if ( hAction != null ) {
					ChatHoverable hoverable = new ChatHoverable( EnumHoverAction.valueOf( hAction.getAction().name() ), new ChatComponentText( hAction.getMessage() ) );
					modifier.setChatHoverable( hoverable );
				}
				icbc.a().add( text );
			}
		}
		return icbc;
	}

	private ChatMessage convert( IChatBaseComponent chatBase ) {
		ChatMessage message = new ChatMessage();
		for ( IChatBaseComponent base : chatBase ) {
			ChatComponent component = new ChatComponent( base.getText() );
			ChatModifier modifier = base.getChatModifier();
			component.setBold( modifier.isBold() );
			component.setItalic( modifier.isItalic() );
			component.setUnderline( modifier.isUnderlined() );
			component.setStrikethrough( modifier.isStrikethrough() );
			component.setRandom( modifier.isRandom() );
			if ( modifier.getColor() != null ) { 
				component.setColor( ChatColor.valueOf( modifier.getColor().name() ) );
			} else {
				component.setColor( ChatColor.WHITE );
			}

			ChatClickable clickable = modifier.h();
			if ( clickable != null ) {
				ClickAction clickAction = new ClickAction( ClickAction.Action.valueOf( clickable.a().name() ), clickable.b() );
				component.setClickAction( clickAction );
			}

			ChatHoverable hoverable = modifier.i();
			if ( hoverable != null ) {
				HoverAction hoverAction = new HoverAction( HoverAction.Action.valueOf( hoverable.a().name() ), hoverable.b().toPlainText() );
				component.setHoverAction( hoverAction );
			}
			message.addComponent( component );
		}

		return message;
	}
	
	private boolean removeSnowman( IChatBaseComponent component ) {
		if ( component.getText().startsWith( BrickBoard.SEND_CHAR + "" ) ) {
			try {
				Field text = component.getClass().getDeclaredField( "b" );
				text.setAccessible( true );
				Field modifiersField = Field.class.getDeclaredField( "modifiers" );
				modifiersField.setAccessible( true );
				modifiersField.setInt( text, text.getModifiers() & ~Modifier.FINAL);
				
				text.set( component, component.getText().substring( 1 ) );
			} catch ( IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e ) {
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			for ( IChatBaseComponent icbc : component.a() ) {
				if ( removeSnowman( icbc ) ) {
					return true;
				}
			}
		}
		return false;
	}
	
	private IChatBaseComponent getFirstNonEmpty( IChatBaseComponent component ) {
		if ( component.getText().isEmpty() ) {
			for ( IChatBaseComponent extra : component.a() ) {
				IChatBaseComponent first = getFirstNonEmpty( extra );
				if ( first != null ) {
					return first;
				}
			}
			return null;
		} else {
			return component;
		}
	}
}