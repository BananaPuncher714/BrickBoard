package io.github.bananapuncher714.brickboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.bananapuncher714.brickboard.commands.BrickExecutor;
import io.github.bananapuncher714.brickboard.commands.BrickTabCompleter;
import io.github.bananapuncher714.brickboard.commands.actions.CCommandChangeBoard;
import io.github.bananapuncher714.brickboard.commands.actions.CCommandChangeChannel;
import io.github.bananapuncher714.brickboard.commands.actions.CCommandScroll;
import io.github.bananapuncher714.brickboard.demo.BrickBoardDemo;
import io.github.bananapuncher714.brickboard.implementation.API.PacketHandler;
import io.github.bananapuncher714.brickboard.listeners.PlayerListener;
import io.github.bananapuncher714.brickboard.objects.Board;
import io.github.bananapuncher714.brickboard.objects.MinecraftFontContainer;
import io.github.bananapuncher714.brickboard.util.ReflectionUtils;
import io.github.bananapuncher714.ngui.ClickListener;
import io.github.bananapuncher714.ngui.NGui;
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
					board = boardManager.getBoard( boardId );
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
	
	@Override
	public void onDisable() {
		NGui.disable();
	}
	
	private void saveResources() {
		saveResource( "README.md", true );
	}
	
	private void registerListeners() {
		Bukkit.getPluginManager().registerEvents( new PlayerListener( this ), this );
		
		Bukkit.getPluginManager().registerEvents( new ClickListener(), this );
	}
	
	private void registerCommands() {
		command = new BrickExecutor();
		getCommand( "brickboard" ).setExecutor( command );
		getCommand( "brickboard" ).setTabCompleter( new BrickTabCompleter() );
		
		registerClickCommands();
	}
	
	private void registerClickCommands() {
		command.registerClickCommand( new CCommandChangeChannel() );
		command.registerClickCommand( new CCommandScroll() );
		command.registerClickCommand( new CCommandChangeBoard( boardManager ) );
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
	
	public enum Permission {
		ADMIN( "brickboard.admin" );
		
		private String permission;
		
		private Permission( String permission ) {
			this.permission = permission;
		}
		
		public String getPermission() {
			return permission;
		}
	}
}