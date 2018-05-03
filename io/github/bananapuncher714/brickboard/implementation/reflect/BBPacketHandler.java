package io.github.bananapuncher714.brickboard.implementation.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
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
import net.minecraft.server.v1_11_R1.ChatClickable;
import net.minecraft.server.v1_11_R1.ChatClickable.EnumClickAction;
import net.minecraft.server.v1_11_R1.ChatComponentText;
import net.minecraft.server.v1_11_R1.ChatHoverable;
import net.minecraft.server.v1_11_R1.ChatHoverable.EnumHoverAction;
import net.minecraft.server.v1_11_R1.ChatModifier;
import net.minecraft.server.v1_11_R1.EnumChatFormat;
import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import net.minecraft.server.v1_11_R1.PacketPlayOutChat;
import net.minecraft.server.v1_11_R1.PacketPlayOutTabComplete;

/**
 * Last resort class for handling the unknown version
 * 
 * @author BananaPuncher714
 * Created on 2018-05-2
 * 
 * TODO Finish this to work with 1.8-1.12, maybe?
 */
public class BBPacketHandler implements PacketHandler {
	private static final Map< String, Field > fieldCache;
	private static final Map< String, Method > methodCache;
	private static final Map< String, Class< ? > > classCache;
	private static final Map< String, Constructor< ? > > constructorCache;

	private static final String VERSION;

	static {
		VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

		fieldCache = new HashMap< String, Field >();
		methodCache = new HashMap< String, Method >();
		classCache = new HashMap< String, Class< ? > >();
		constructorCache = new HashMap< String, Constructor< ? > >();

		try {
			classCache.put( "ChatClickable", Class.forName( "net.minecraft.server." + VERSION + "." + "ChatClickable" ) );
			classCache.put( "EnumClickAction", classCache.get( "ChatClickable" ).getDeclaredClasses()[ 0 ] );
			classCache.put( "ChatComponentText", Class.forName( "net.minecraft.server." + VERSION + "." + "ChatComponentText" ) );
			classCache.put( "ChatHoverable", Class.forName( "net.minecraft.server." + VERSION + "." + "ChatHoverable" ) );
			classCache.put( "EnumHoverAction", classCache.get( "ChatHoverable" ).getDeclaredClasses()[ 0 ] );
			classCache.put( "ChatModifier", Class.forName( "net.minecraft.server." + VERSION + "." + "ChatModifier" ) );
			classCache.put( "EnumChatFormat", Class.forName( "net.minecraft.server." + VERSION + "." + "EnumChatFormat" ) );
			classCache.put( "IChatBaseComponent", Class.forName( "net.minecraft.server." + VERSION + "." + "IChatBaseComponent" ) );
			classCache.put( "Packet", Class.forName( "net.minecraft.server." + VERSION + "." + "Packet" ) );
			classCache.put( "PacketPlayOutChat", Class.forName( "net.minecraft.server." + VERSION + "." + "PacketPlayOutChat" ) );
			classCache.put( "PacketPlayOutTabComplete", Class.forName( "net.minecraft.server." + VERSION + "." + "PacketPlayOutTabComplete" ) );

			classCache.put( "PlayerConnection", Class.forName("net.minecraft.server." + VERSION + "." + "PlayerConnection" ) );
			classCache.put( "CraftPlayer", Class.forName( "org.bukkit.craftbukkit." + VERSION + ".entity.CraftPlayer" ) );
		} catch ( Exception exception ) {
			exception.printStackTrace();
		}

		try {
			methodCache.put( "CraftPlayer.getHandle", getClass( "CraftPlayer" ).getMethod( "getHandle" ) );
			methodCache.put( "PlayerConnection.sendPacket", getClass( "PlayerConnection" ).getMethod( "sendPacket", getClass( "Packet" ) ) );
			methodCache.put( "IChatBaseComponent.getChatModifier", getClass( "IChatBaseComponent" ).getMethod( "getChatModifier" ) );
		} catch ( Exception exception ) {
			exception.printStackTrace();
		}

		try {
			fieldCache.put( "PacketPlayOutChat_message", getClass( "PacketPlayOutChat" ).getDeclaredField( "a" ) );
			fieldCache.get( "PacketPlayOutChat_message" ).setAccessible( true );
			fieldCache.put( "PacketPlayOutChat_type", getClass( "PacketPlayOutChat" ).getDeclaredField( "b" ) );
			fieldCache.get( "PacketPlayOutChat_type" ).setAccessible( true );
			fieldCache.put( "PacketPlayOutTabComplete_message", getClass( "PacketPlayOutTabComplete" ).getDeclaredField( "a" ) );
			fieldCache.get( "PacketPlayOutTabComplete_message" ).setAccessible( true );
		} catch ( Exception exception ) {
			exception.printStackTrace();
		}


		try {
			constructorCache.put( "PacketPlayOutChat", getClass( "PacketPlayOutChat" ).getConstructor( getClass( "IChatBaseComponent" ), byte.class ) );
			constructorCache.put( "ChatComponentText", getClass( "ChatComponentText" ).getConstructor( String.class ) );
		} catch ( Exception exception ) {
			exception.printStackTrace();
		}
	}

	public static final Class< ? > getClass( String name ) {
		return classCache.containsKey( name ) ? classCache.get( name ) : null;
	}

	public static final Method getMethod( String name ) {
		return methodCache.containsKey( name ) ? methodCache.get( name ) : null;
	}

	public static final Field getField( String name ) {
		return fieldCache.containsKey( name ) ? fieldCache.get( name ) : null;
	}

	public static final Constructor< ? > getConstructor( String name ) {
		return constructorCache.containsKey( name ) ? constructorCache.get( name ) : null;
	}

	@Override
	public boolean handlePacket( Player player, Object packet ) {
		if ( packet == null ) {
			return true;
		} else if ( getClass( "PacketPlayOutChat" ).isInstance( packet ) ) {
			return handleChatPacket( player, packet );
		} else if ( getClass( "PacketPlayOutTabComplete" ).isInstance( packet ) ) {
			return handleTabCompletePacket( player, packet );
		} else {
			return true;
		}
	}

	private boolean handleChatPacket( Player player, Object packet ) {
		IChatBaseComponent component;
		try {
			byte position = ( byte ) FIELD_CHAT_POSITION.get( packet );
			if ( position == 2 ) {
				return true;
			}
			component = ( IChatBaseComponent ) FIELD_COMPONENT.get( packet );
		} catch ( SecurityException | IllegalArgumentException | IllegalAccessException e ) {
			e.printStackTrace();
			return false;
		}
		if ( ChatComponentUtils.removeSnowman( component ) ) {
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

	private boolean handleTabCompletePacket( Player player, Object packet ) {
		String[] arguments;
		try {
			arguments = ( String[] ) getField( "PacketPlayOutTabComplete_message" ).get( packet );
		} catch ( IllegalArgumentException | IllegalAccessException e ) {
			return true;
		}
		BrickPlayer bPlayer = BrickPlayerManager.getInstance().getPlayer( player.getUniqueId() );
		bPlayer.setTabCompletes( arguments );

		return true;
	}

	@Override
	public void sendMessage( Player player, String message ) {
		try {
			Object packet = getConstructor( "PacketPlayOutChat" ).newInstance( getConstructor( "ChatComponentText" ).newInstance( BrickBoard.SEND_CHAR + message ) );
			getMethod( "PlayerConnection.sendPacket" ).invoke( getMethod( "CraftPlayer.getHandle" ).invoke( player ), packet );
		} catch ( IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e ) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendMessage( Player player, ChatMessage component ) {
		IChatBaseComponent icbc = convert( component, true );
		if ( icbc == null ) {
			return;
		}
		PacketPlayOutChat packet = new PacketPlayOutChat( icbc );
		( ( CraftPlayer ) player ).getHandle().playerConnection.sendPacket( packet );
	}

	private Object convert( ChatMessage message, boolean compress ) {
		if ( message.getComponents().isEmpty() ) {
			return null;
		}
		try {
			Object icbc = getConstructor( "ChatComponentText" ).newInstance( BrickBoard.SEND_CHAR + "" );
			if ( !compress ) {
				for ( ChatComponent component : message.getComponents() ) {
					Object textComponent = getConstructor( "ChatComponentText" ).newInstance( component.getText() );
					Object modifier = getMethod( "IChatBaseComponent.getChatModifier" ).invoke( textComponent );
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
		} catch ( Exception exception ) {
			exception.printStackTrace();
		}
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
}