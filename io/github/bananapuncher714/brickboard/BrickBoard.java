package io.github.bananapuncher714.brickboard;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.bananapuncher714.brickboard.api.PacketHandler;
import io.github.bananapuncher714.brickboard.commands.BrickExecutor;
import io.github.bananapuncher714.brickboard.commands.BrickTabCompleter;
import io.github.bananapuncher714.brickboard.commands.actions.CCommandChangeBoard;
import io.github.bananapuncher714.brickboard.commands.actions.CCommandChangeChannel;
import io.github.bananapuncher714.brickboard.commands.actions.CCommandScroll;
import io.github.bananapuncher714.brickboard.demo.BrickBoardDemo;
import io.github.bananapuncher714.brickboard.gui.ChatBoxAeNet;
import io.github.bananapuncher714.brickboard.gui.ChatBoxBoardSelector;
import io.github.bananapuncher714.brickboard.gui.ChatBoxChannel;
import io.github.bananapuncher714.brickboard.gui.ChatBoxCoord;
import io.github.bananapuncher714.brickboard.gui.ChatBoxFiller;
import io.github.bananapuncher714.brickboard.gui.ChatBoxFlickerTest;
import io.github.bananapuncher714.brickboard.gui.ChatBoxRainbow;
import io.github.bananapuncher714.brickboard.gui.ChatBoxSlate;
import io.github.bananapuncher714.brickboard.gui.ChatBoxTabCompletes;
import io.github.bananapuncher714.brickboard.gui.ChatBoxTicker;
import io.github.bananapuncher714.brickboard.listeners.PlayerListener;
import io.github.bananapuncher714.brickboard.objects.Board;
import io.github.bananapuncher714.brickboard.objects.MinecraftFontContainer;
import io.github.bananapuncher714.brickboard.util.FileUtil;
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
	
	
	BrickExecutor command;
	
	private TinyProtocol tProtocol;
	private PacketHandler handler;
	private BukkitRunnable runnable = new BukkitRunnable() {
		BrickPlayerManager manager = BrickPlayerManager.getInstance();
		@Override
		public void run() {
			for ( Player player : Bukkit.getOnlinePlayers() ) {
				BrickPlayer bPlayer = manager.getPlayer( player.getUniqueId() );
				
				Board board = bPlayer.getActiveBoard();
				if ( board == null ) {
					board = BoardManager.getInstance().getDefaultBoard();
				}
				
				handler.sendMessage( player, board.getMessage( player ) );
			}
		}
	};
	
	@Override
	public void onEnable() {
		instance = this;
		
		FontManager.getInstance().setDefaultContainer( new MinecraftFontContainer( getResource( "data/ascii.png" ), getResource( "data/default_font.bin" ) ) );
		
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
		registerChatBoxes();
		
		DependencyManager.init();
		
		Bukkit.getScheduler().runTaskLater( this, new Runnable() {
			@Override
			public void run() {
				ChatBoxManager.getInstance().loadPresets( new File( getDataFolder() + "/" + "presets" ) );
				BoardManager.getInstance().loadBoards( new File( getDataFolder() + "/" + "boards" ) );
			}
		}, 20 );
	}
	
	@Override
	public void onDisable() {
		NGui.disable();
	}
	
	private void saveResources() {
		saveResource( "README.md", true );
		
		// Debug info here
		File presetDir = new File( getDataFolder() + "/" + "presets" );
		FileUtil.saveToFile( getResource( "data/presets/demo-filler-aqua.yml" ), new File( presetDir + "/" + "demo-filler-aqua.yml" ), false );
		FileUtil.saveToFile( getResource( "data/presets/demo-channel.yml" ), new File( presetDir + "/" + "demo-channel.yml" ), false );
		FileUtil.saveToFile( getResource( "data/presets/demo-header.yml" ), new File( presetDir + "/" + "demo-header.yml" ), false );
		FileUtil.saveToFile( getResource( "data/presets/demo-flicker-test.yml" ), new File( presetDir + "/" + "demo-flicker-test.yml" ), false );
		FileUtil.saveToFile( getResource( "data/presets/demo-slate.yml" ), new File( presetDir + "/" + "demo-slate.yml" ), false );
		FileUtil.saveToFile( getResource( "data/presets/demo-tab-completes.yml" ), new File( presetDir + "/" + "demo-tab-completes.yml" ), false );
		FileUtil.saveToFile( getResource( "data/presets/demo-active-channel.yml" ), new File( presetDir + "/" + "demo-active-channel.yml" ), false );
		
		File boardDir = new File( getDataFolder() + "/" + "boards" );
		FileUtil.saveToFile( getResource( "data/boards/demo-board.yml" ), new File( boardDir + "/" + "demo-board.yml" ), false );
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
		command.registerClickCommand( new CCommandChangeBoard() );
	}
	
	private void registerChatBoxes() {
		ChatBoxManager manager = ChatBoxManager.getInstance();
		manager.registerBox( "ChatBoxAeNet", ChatBoxAeNet.class );
		manager.registerBox( "ChatBoxBoardSelector", ChatBoxBoardSelector.class );
		manager.registerBox( "ChatBoxChannel", ChatBoxChannel.class );
		manager.registerBox( "ChatBoxCoord", ChatBoxCoord.class );
		manager.registerBox( "chatboxfiller", ChatBoxFiller.class );
		manager.registerBox( "ChatBoxFlickerTest", ChatBoxFlickerTest.class );
		manager.registerBox( "ChatBoxRainbow", ChatBoxRainbow.class );
		manager.registerBox( "ChatBoxSlate", ChatBoxSlate.class );
		manager.registerBox( "ChatBoxTabCompletes", ChatBoxTabCompletes.class );
		manager.registerBox( "ChatBoxTicker", ChatBoxTicker.class );
	}

	// Getters
	
	public TinyProtocol getProtocol() {
		return tProtocol;
	}

	public static BrickBoard getInstance() {
		return instance;
	}
}