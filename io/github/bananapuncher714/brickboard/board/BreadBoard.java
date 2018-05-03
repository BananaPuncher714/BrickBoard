package io.github.bananapuncher714.brickboard.board;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.BrickBoard;
import io.github.bananapuncher714.brickboard.BrickPlayer;
import io.github.bananapuncher714.brickboard.BrickPlayerManager;
import io.github.bananapuncher714.brickboard.chat.ChatComponent;
import io.github.bananapuncher714.brickboard.chat.ChatMessage;
import io.github.bananapuncher714.brickboard.chat.ClickAction;
import io.github.bananapuncher714.brickboard.chat.HoverAction;
import io.github.bananapuncher714.brickboard.objects.MinecraftFontContainer;
import io.github.bananapuncher714.brickboard.util.MessageUtil;

/**
 * This is what it looks like; A breadboard
 * 
 * @author BananaPuncher714
 * @deprecated
 */
public class BreadBoard {
	private List< ChatColor > colors = new ArrayList< ChatColor >();
	private int index = 0, tickerIndex;
	private String tickerTape;
	
	public BreadBoard() {
		colors.add( ChatColor.DARK_RED );
		colors.add( ChatColor.RED );
		colors.add( ChatColor.GOLD );
		colors.add( ChatColor.YELLOW );
		colors.add( ChatColor.GREEN );
		colors.add( ChatColor.DARK_GREEN );
		colors.add( ChatColor.DARK_BLUE );
		colors.add( ChatColor.BLUE );
		colors.add( ChatColor.AQUA );
		colors.add( ChatColor.DARK_AQUA );
		colors.add( ChatColor.LIGHT_PURPLE );
		colors.add( ChatColor.DARK_PURPLE );
		
		BufferedReader reader = new BufferedReader( new InputStreamReader( BrickBoard.getInstance().getResource( "data/ticker.txt" ) ) );
		String line;
		StringBuilder builder = new StringBuilder();
		try {
			while ( ( line = reader.readLine() ) != null ) {
				if ( line.isEmpty() ) {
					builder.append( "  \u2603  " );
				} else {
					builder.append( line );
				}
			}
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		tickerTape = builder.toString();
	}
	
	public ChatMessage getMessage( Player player ) {
		MinecraftFontContainer container = BrickBoard.getInstance().getDefaultFont();
		ChatMessage message = new ChatMessage();
		BrickPlayer bPlayer = BrickPlayerManager.getInstance().getPlayer( player.getUniqueId() );
		for ( int i = 0; i < 20; i++ ) {
			if ( i == 0 ) {
				index = ( index + 1 ) % colors.size();
				message.addComponents( ChatColor.AQUA + "-----[ " + colors.get( index ) + ChatColor.BOLD + "BrickBoard by BananaPuncher714 " + ChatColor.AQUA + "]-----" );
			} else if ( i == 1 ) {
				String ticker = tickerTape.substring( tickerIndex / 2 );
				tickerIndex = ( tickerIndex + 1 ) % ( tickerTape.length() * 2 );
				String fin = "";
				for ( char ch : ticker.toCharArray() ) {
					int len = MessageUtil.lengthOf( new ChatMessage().addComponent( new ChatComponent( fin + ch ) ), BrickBoard.getInstance().getDefaultFont() );
					if ( len > 310 ) {
						break;
					} else {
						fin = fin + ch;
					}
				}
				message.addComponent( new ChatComponent( fin ).setColor( ChatColor.GRAY ) );
			} else if ( i == 3 ) {
				ChatMessage control = new ChatMessage();
				control.addComponent( new ChatComponent( "[Log]" ).setColor( ChatColor.GRAY ).setBold( true ).setClickAction( new ClickAction( ClickAction.Action.RUN_COMMAND, "/brickboard execute changechannel log" ) ).setHoverAction( new HoverAction( HoverAction.Action.SHOW_TEXT, "View all raw messages" ) ) );
				control.addComponent( new ChatComponent( " " ) );
				control.addComponent( new ChatComponent( "[Global]" ).setColor( ChatColor.GREEN ).setBold( true ).setClickAction( new ClickAction( ClickAction.Action.RUN_COMMAND, "/brickboard execute changechannel global" ) ).setHoverAction( new HoverAction( HoverAction.Action.SHOW_TEXT, "View the global chat" ) ) );
				control.addComponent( new ChatComponent( " " ) );
				control.addComponent( new ChatComponent( "[Offtopic]" ).setColor( ChatColor.GOLD ).setBold( true ).setClickAction( new ClickAction( ClickAction.Action.RUN_COMMAND, "/brickboard execute changechannel offtopic" ) ).setHoverAction( new HoverAction( HoverAction.Action.SHOW_TEXT, "View the offtopic chat" ) ) );
				control.addComponent( new ChatComponent( " " ) );
				control.addComponent( new ChatComponent( "[Announcements]" ).setColor( ChatColor.LIGHT_PURPLE ).setBold( true ).setClickAction( new ClickAction( ClickAction.Action.RUN_COMMAND, "/brickboard execute changechannel announcements" ) ).setHoverAction( new HoverAction( HoverAction.Action.SHOW_TEXT, "View the announcements" ) ) );
				
				message.merge( control );
			} else if ( i == 11 ) {
				String title = bPlayer.getActiveChannelName().toUpperCase();
				int len = BrickBoard.getInstance().getDefaultFont().getStringWidth( "  " + title, true );
				int arrowLen = MessageUtil.lengthOf( new ChatMessage().addComponent( new ChatComponent( " [\u25B2][\u25BC] " ) ), BrickBoard.getInstance().getDefaultFont() );
				
				int chars = ( ( 320 - arrowLen ) - len ) / 2;
				message.addComponent( new ChatComponent( StringUtils.repeat( '|', chars / 2 ) ).setRandom( true ).setColor( ChatColor.GOLD ) );
				message.addComponent( new ChatComponent( " " + title + " " ).setUnderline( true ).setBold( true ).setColor( ChatColor.YELLOW ) );
				message.addComponent( new ChatComponent( StringUtils.repeat( '|', chars / 2 ) ).setRandom( true ).setColor( ChatColor.GOLD ) );
				message.addComponent( new ChatComponent( "[\u25B2]" ).setColor( ChatColor.GREEN ).setClickAction( new ClickAction( ClickAction.Action.RUN_COMMAND, "/brickboard execute scroll up" ) ).setHoverAction( new HoverAction( HoverAction.Action.SHOW_TEXT, "Scroll up" ) ) );
				message.addComponent( new ChatComponent( "[\u25BC]" ).setColor( ChatColor.GREEN ).setClickAction( new ClickAction( ClickAction.Action.RUN_COMMAND, "/brickboard execute scroll down" ) ).setHoverAction( new HoverAction( HoverAction.Action.SHOW_TEXT, "Scroll down" ) ) );
			} else if ( i == 10 ) {
				ChatMessage xCoord = ChatMessage.getMessageFromString( ChatColor.AQUA + "X: " + player.getLocation().getBlockX() );
				MessageUtil.extend( 70, xCoord, container );
				ChatMessage yCoord = ChatMessage.getMessageFromString( ChatColor.AQUA + "Y: " + player.getLocation().getBlockY() );
				MessageUtil.extend( 70, yCoord, container );
				ChatMessage zCoord = ChatMessage.getMessageFromString( ChatColor.AQUA + "Z: " + player.getLocation().getBlockZ() );
				MessageUtil.extend( 70, zCoord, container );
				message.merge( xCoord, yCoord, zCoord );
			} else if ( i > 11 ) {
				int pos = ( 8 + bPlayer.getPage() ) - ( i - 11 );
				if ( bPlayer.getActiveChannel().size() > pos ) {
					message.merge( bPlayer.getActiveChannel().get( pos ) );
				}
			}
			
			if ( i < 19 ) {
				message.addComponent( new ChatComponent( "\n" ) );
			}
		}
		return message;
	}
	
}
