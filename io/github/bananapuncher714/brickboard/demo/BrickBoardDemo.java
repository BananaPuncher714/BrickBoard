package io.github.bananapuncher714.brickboard.demo;

import org.bukkit.ChatColor;

import io.github.bananapuncher714.brickboard.BoardManager;
import io.github.bananapuncher714.brickboard.BrickBoard;
import io.github.bananapuncher714.brickboard.FontManager;
import io.github.bananapuncher714.brickboard.chat.ChatComponent;
import io.github.bananapuncher714.brickboard.chat.ChatMessage;
import io.github.bananapuncher714.brickboard.chat.ClickAction;
import io.github.bananapuncher714.brickboard.chat.HoverAction;
import io.github.bananapuncher714.brickboard.gui.ChatBoxAeNet;
import io.github.bananapuncher714.brickboard.gui.ChatBoxChannel;
import io.github.bananapuncher714.brickboard.gui.ChatBoxFiller;
import io.github.bananapuncher714.brickboard.gui.ChatBoxFlickerTest;
import io.github.bananapuncher714.brickboard.gui.ChatBoxSlate;
import io.github.bananapuncher714.brickboard.gui.ChatBoxTabCompletes;
import io.github.bananapuncher714.brickboard.gui.ChatBoxTicker;
import io.github.bananapuncher714.brickboard.objects.Board;
import io.github.bananapuncher714.ngui.objects.BoxCoord;

public class BrickBoardDemo {
	public static void addDefaultBoard( BrickBoard plugin ) {
		FontManager fManager = FontManager.getInstance();
		
		Board breadBoard = new Board( "test", fManager );
		ChatMessage control = new ChatMessage();
		control.addComponent( new ChatComponent( "[Log]" ).setColor( ChatColor.GRAY ).setBold( true ).setClickAction( new ClickAction( ClickAction.Action.RUN_COMMAND, "/brickboard execute changechannel log" ) ).setHoverAction( new HoverAction( HoverAction.Action.SHOW_TEXT, "View all raw messages" ) ) );
		control.addComponent( new ChatComponent( " " ) );
		control.addComponent( new ChatComponent( "[Global]" ).setColor( ChatColor.GREEN ).setBold( true ).setClickAction( new ClickAction( ClickAction.Action.RUN_COMMAND, "/brickboard execute changechannel global" ) ).setHoverAction( new HoverAction( HoverAction.Action.SHOW_TEXT, "View the global chat" ) ) );
		control.addComponent( new ChatComponent( " " ) );
		control.addComponent( new ChatComponent( "[Offtopic]" ).setColor( ChatColor.GOLD ).setBold( true ).setClickAction( new ClickAction( ClickAction.Action.RUN_COMMAND, "/brickboard execute changechannel offtopic" ) ).setHoverAction( new HoverAction( HoverAction.Action.SHOW_TEXT, "View the offtopic chat" ) ) );
		control.addComponent( new ChatComponent( " " ) );
		control.addComponent( new ChatComponent( "[Announcements]" ).setColor( ChatColor.LIGHT_PURPLE ).setBold( true ).setClickAction( new ClickAction( ClickAction.Action.RUN_COMMAND, "/brickboard execute changechannel announcements" ) ).setHoverAction( new HoverAction( HoverAction.Action.SHOW_TEXT, "View the announcements" ) ) );
		control.addComponent( new ChatComponent( " " ) );
		control.addComponent( new ChatComponent( "[\u25B2]" ).setColor( ChatColor.GREEN ).setClickAction( new ClickAction( ClickAction.Action.RUN_COMMAND, "/brickboard execute scroll up" ) ).setHoverAction( new HoverAction( HoverAction.Action.SHOW_TEXT, "Scroll up" ) ) );
		control.addComponent( new ChatComponent( "[\u25BC]" ).setColor( ChatColor.GREEN ).setClickAction( new ClickAction( ClickAction.Action.RUN_COMMAND, "/brickboard execute scroll down" ) ).setHoverAction( new HoverAction( HoverAction.Action.SHOW_TEXT, "Scroll down" ) ) );
		breadBoard.setContainer( new ChatBoxSlate( control ), new BoxCoord( 0, 2 ) );
		breadBoard.setContainer( new ChatBoxTicker( plugin.getResource( "data/ticker.txt" ) ), new BoxCoord( 0, 1, 0, 0 ) );
		breadBoard.setContainer( new ChatBoxFiller( ChatColor.AQUA ), new BoxCoord( 0, 3 ) );
		breadBoard.setContainer( new ChatBoxTabCompletes(), new BoxCoord( 0, 4 ) );
		breadBoard.setContainer( new ChatBoxFiller( ChatColor.AQUA ), new BoxCoord( 0, 7 ) );
		breadBoard.setContainer( new ChatBoxFiller( ChatColor.AQUA ), new BoxCoord( 0, 9 ) );
		breadBoard.setContainer( new ChatBoxSlate( ChatMessage.getMessageFromString( ChatColor.DARK_GREEN + "Server TPS: " + ChatColor.GREEN + "%server_tps_1%  / 20" ) ), new BoxCoord( 0, 8 ) );
		breadBoard.setContainer( new ChatBoxChannel( fManager ), new BoxCoord( 0, 10 ) );
		breadBoard.setContainer( new ChatBoxFlickerTest(), new BoxCoord( 150, 8 ) );
//		breadBoard.setContainer( new ChatBoxRainbow( ChatColor.AQUA + "----[", " BrickBoard by BananaPuncher714 ", ChatColor.AQUA + "]----" ), new BoxCoord( 0, 0, 0, 0 ) );
		breadBoard.setContainer( new ChatBoxAeNet( fManager ), new BoxCoord( 0, 0 ) );
		
		breadBoard.sort( true );
		
		BoardManager.getInstance().addBoard( breadBoard );
	}

}
