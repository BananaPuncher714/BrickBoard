package io.github.bananapuncher714.brickboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.bananapuncher714.brickboard.board.Board;
import io.github.bananapuncher714.brickboard.chat.ChatComponent;
import io.github.bananapuncher714.brickboard.chat.ChatMessage;
import io.github.bananapuncher714.brickboard.chat.ClickAction;
import io.github.bananapuncher714.brickboard.chat.HoverAction;
import io.github.bananapuncher714.brickboard.commands.BrickExecutor;
import io.github.bananapuncher714.brickboard.commands.actions.CCommandChangeChannel;
import io.github.bananapuncher714.brickboard.commands.actions.CCommandScroll;
import io.github.bananapuncher714.brickboard.demo.BrickBoardDemo;
import io.github.bananapuncher714.brickboard.gui.ChatBoxAeNet;
import io.github.bananapuncher714.brickboard.gui.ChatBoxChannel;
import io.github.bananapuncher714.brickboard.gui.ChatBoxFiller;
import io.github.bananapuncher714.brickboard.gui.ChatBoxFlickerTest;
import io.github.bananapuncher714.brickboard.gui.ChatBoxSlate;
import io.github.bananapuncher714.brickboard.gui.ChatBoxTabCompletes;
import io.github.bananapuncher714.brickboard.gui.ChatBoxTicker;
import io.github.bananapuncher714.brickboard.implementation.API.PacketHandler;
import io.github.bananapuncher714.brickboard.listeners.PlayerListener;
import io.github.bananapuncher714.brickboard.objects.BoxCoord;
import io.github.bananapuncher714.brickboard.objects.MinecraftFontContainer;
import io.github.bananapuncher714.brickboard.util.ReflectionUtils;
import io.github.bananapuncher714.tinyprotocol.TinyProtocol;
import io.netty.channel.Channel;

/**
 * "Crushing the competition since 2018"
 * 
 * @author BananaPuncher714
 */
public class BrickBoard extends JavaPlugin {
	/**
	 * The character prefixed by all messages which get sent to the player
	 */
	public static final char SEND_CHAR = '\u23e3';
	/**
	 * The length of the Minecraft chat; May change depending on client settings; Not recommended to set above 316
	 */
	public static final int CHAT_LEN = 316;
	/**
	 * Singleton class management
	 */
	private static BrickBoard instance;
	
	// TODO re-implement FontManager for serialization purposes
	FontManager fontManager;
	BoardManager boardManager;
	BrickExecutor command;
	
	private TinyProtocol tProtocol;
	private PacketHandler handler;
	private BukkitRunnable runnable = new BukkitRunnable() {
		BrickPlayerManager manager = BrickPlayerManager.getInstance();
		@Override
		public void run() {
			for ( Player player : Bukkit.getOnlinePlayers() ) {
				BrickPlayer bPlayer = manager.getPlayer( player.getUniqueId() );
				
				String boardId = bPlayer.getActiveBoard();
				Board board = boardManager.getDefaultBoard();
				if ( boardId != null ) {
					boardManager.getBoard( boardId );
				}
				
				handler.sendMessage( player, board.getMessage( player ) );
			}
		}
	};
	
	@Override
	public void onEnable() {
		instance = this;
		
		fontManager = new FontManager( this, new MinecraftFontContainer( getResource( "data/ascii.png" ), getResource( "data/default_font.bin" ) ) );
		boardManager = new BoardManager( this );
		
		handler = ReflectionUtils.getNewPacketHandlerInstance();
		tProtocol = new TinyProtocol( this ) {
			@Override
			public Object onPacketOutAsync( Player player, Channel channel, Object packet ) {
				return handler.handlePacket( player, packet ) ? packet : null;
			}
		};
		
		BrickBoardDemo.addDefaultBoard( this );
		
		runnable.runTaskTimer( this, 1, 1 );
		
		saveResources();
		registerListeners();
		registerCommands();
	}
	
	private void saveResources() {
		saveResource( "README.md", true );
	}
	
	private void registerListeners() {
		Bukkit.getPluginManager().registerEvents( new PlayerListener( this ), this );
	}
	
	private void registerCommands() {
		command = new BrickExecutor();
		getCommand( "brickboard" ).setExecutor( command );
		
		registerClickCommands();
	}
	
	private void registerClickCommands() {
		command.registerClickCommand( new CCommandChangeChannel() );
		command.registerClickCommand( new CCommandScroll() );
	}

	// Getters
	
	public TinyProtocol getProtocol() {
		return tProtocol;
	}
	
	public MinecraftFontContainer getDefaultFont() {
		return fontManager.getDefaultContainer();
	}
	
	public FontManager getFontManager() {
		return fontManager;
	}
	
	public BoardManager getBoardManager() {
		return boardManager;
	}
	
	public static BrickBoard getInstance() {
		return instance;
	}
}