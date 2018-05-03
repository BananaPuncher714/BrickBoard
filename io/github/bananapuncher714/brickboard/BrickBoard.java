package io.github.bananapuncher714.brickboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.tinyprotocol.TinyProtocol;

import io.github.bananapuncher714.brickboard.board.Board;
import io.github.bananapuncher714.brickboard.chat.ChatComponent;
import io.github.bananapuncher714.brickboard.chat.ChatMessage;
import io.github.bananapuncher714.brickboard.chat.ClickAction;
import io.github.bananapuncher714.brickboard.chat.HoverAction;
import io.github.bananapuncher714.brickboard.commands.BrickExecutor;
import io.github.bananapuncher714.brickboard.commands.actions.CCommandChangeChannel;
import io.github.bananapuncher714.brickboard.commands.actions.CCommandScroll;
import io.github.bananapuncher714.brickboard.gui.ChatBoxChannel;
import io.github.bananapuncher714.brickboard.gui.ChatBoxFiller;
import io.github.bananapuncher714.brickboard.gui.ChatBoxFlickerTest;
import io.github.bananapuncher714.brickboard.gui.ChatBoxRainbow;
import io.github.bananapuncher714.brickboard.gui.ChatBoxSlate;
import io.github.bananapuncher714.brickboard.gui.ChatBoxTabCompletes;
import io.github.bananapuncher714.brickboard.gui.ChatBoxTicker;
import io.github.bananapuncher714.brickboard.implementation.API.PacketHandler;
import io.github.bananapuncher714.brickboard.listeners.PlayerListener;
import io.github.bananapuncher714.brickboard.objects.BoxCoord;
import io.github.bananapuncher714.brickboard.objects.MinecraftFontContainer;
import io.github.bananapuncher714.brickboard.util.MessageUtil;
import io.github.bananapuncher714.brickboard.util.ReflectionUtils;
import io.netty.channel.Channel;

public class BrickBoard extends JavaPlugin {
	/**
	 * The character prefixed by all messages which get sent to the player
	 */
	public static final char SEND_CHAR = '\u23e3';
	/**
	 * The length of the Minecraft chat; May change depending on client settings; Not recommended to set above 316
	 */
	public static final int CHAT_LEN = 316;
	
	private static BrickBoard instance;
	
	MinecraftFontContainer defaultFont = new MinecraftFontContainer( getResource( "data/ascii.png" ), getResource( "data/default_font.bin" ) );
	BrickExecutor command;
	
	TinyProtocol tProtocol;
	PacketHandler handler;
	
	Board breadBoard;
	
	@Override
	public void onEnable() {
		instance = this;
		
		tProtocol = new TinyProtocol( this ) {
			@Override
			public Object onPacketOutAsync( Player player, Channel channel, Object packet ) {
				return handler.handlePacket( player, packet ) ? packet : null;
			}
		};
		
		handler = ReflectionUtils.getNewPacketHandlerInstance();
		command = new BrickExecutor();
		
		// TODO refractor this
		breadBoard = new Board( "test" );
		ChatMessage control = new ChatMessage();
		control.addComponent( new ChatComponent( "[Log]" ).setColor( ChatColor.GRAY ).setBold( true ).setClickAction( new ClickAction( ClickAction.Action.RUN_COMMAND, "/brickboard execute changechannel log" ) ).setHoverAction( new HoverAction( HoverAction.Action.SHOW_TEXT, "View all raw messages" ) ) );
		control.addComponent( new ChatComponent( " " ) );
		control.addComponent( new ChatComponent( "[Global]" ).setColor( ChatColor.GREEN ).setBold( true ).setClickAction( new ClickAction( ClickAction.Action.RUN_COMMAND, "/brickboard execute changechannel global" ) ).setHoverAction( new HoverAction( HoverAction.Action.SHOW_TEXT, "View the global chat" ) ) );
		control.addComponent( new ChatComponent( " " ) );
		control.addComponent( new ChatComponent( "[Offtopic]" ).setColor( ChatColor.GOLD ).setBold( true ).setClickAction( new ClickAction( ClickAction.Action.RUN_COMMAND, "/brickboard execute changechannel offtopic" ) ).setHoverAction( new HoverAction( HoverAction.Action.SHOW_TEXT, "View the offtopic chat" ) ) );
		control.addComponent( new ChatComponent( " " ) );
		control.addComponent( new ChatComponent( "[Announcements]" ).setColor( ChatColor.LIGHT_PURPLE ).setBold( true ).setClickAction( new ClickAction( ClickAction.Action.RUN_COMMAND, "/brickboard execute changechannel announcements" ) ).setHoverAction( new HoverAction( HoverAction.Action.SHOW_TEXT, "View the announcements" ) ) );
		breadBoard.setContainer( new ChatBoxSlate( control ), new BoxCoord( 0, 2 ) );
		breadBoard.setContainer( new ChatBoxTicker( getResource( "data/ticker.txt" ) ), new BoxCoord( 0, 1, 0, 0 ) );
		breadBoard.setContainer( new ChatBoxFiller( ChatColor.AQUA ), new BoxCoord( 0, 3 ) );
		breadBoard.setContainer( new ChatBoxTabCompletes(), new BoxCoord( 0, 4 ) );
		breadBoard.setContainer( new ChatBoxFiller( ChatColor.AQUA ), new BoxCoord( 0, 7 ) );
		breadBoard.setContainer( new ChatBoxFiller( ChatColor.AQUA ), new BoxCoord( 0, 8 ) );
		breadBoard.setContainer( new ChatBoxChannel(), new BoxCoord( 0, 10 ) );
		breadBoard.setContainer( new ChatBoxFlickerTest(), new BoxCoord( 150, 8 ) );
		breadBoard.setContainer( new ChatBoxRainbow( ChatColor.AQUA + "----[", " BrickBoard by BananaPuncher714 ", ChatColor.AQUA + "]----" ), new BoxCoord( 0, 0, 0, 0 ) );

		breadBoard.sort( true );
		
		getCommand( "brickboard" ).setExecutor( command );
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask( this, new Runnable() {
			@Override
			public void run() {
				for ( Player player : Bukkit.getOnlinePlayers() ) {
					handler.sendMessage( player, breadBoard.getMessage( player ) );
				}
			}
		}, 1, 1 );
		
		Bukkit.getPluginManager().registerEvents( new PlayerListener( this ), this );

		registerClickCommands();
	}
	
	private void registerClickCommands() {
		command.registerClickCommand( new CCommandChangeChannel() );
		command.registerClickCommand( new CCommandScroll() );
	}

	@Override
	public boolean onCommand( CommandSender sender, Command command, String label, String[] args ) {
		if ( !command.getName().equalsIgnoreCase( "len" ) ) {
			return false;
		}
		if ( args.length < 1 ) {
			sender.sendMessage( "Arguments required!" );
			return false;
		}
		
		System.out.println( args[ 0 ] );
		
		sender.sendMessage( "Length: " + MessageUtil.lengthOf( ChatMessage.getMessageFromString( args[ 0 ].replace( '&', ChatColor.COLOR_CHAR ) ), defaultFont ) );
		return false;
	}
	
	public TinyProtocol getProtocol() {
		return tProtocol;
	}
	
	public MinecraftFontContainer getDefaultFont() {
		return defaultFont;
	}
	
	public static BrickBoard getInstance() {
		return instance;
	}
}