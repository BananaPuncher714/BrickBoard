package io.github.bananapuncher714.brickboard.listeners;

import io.github.bananapuncher714.brickboard.BrickBoard;
import io.github.bananapuncher714.brickboard.BrickPlayer;
import io.github.bananapuncher714.brickboard.BrickPlayerManager;
import io.github.bananapuncher714.brickboard.chat.ChatMessage;
import io.github.bananapuncher714.tinyprotocol.TinyProtocol;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
	BrickBoard main;
	TinyProtocol protocol;
	
	public PlayerListener( BrickBoard board ) {
		main = board;
		protocol = main.getProtocol();
	}
	
	@EventHandler
	public void onPlayerJoinEvent( PlayerJoinEvent event ) {
		Player player = event.getPlayer();
		if ( !protocol.hasInjected( player ) ) {
			protocol.injectPlayer( player );
		}
	}
	
	@EventHandler
	public void onPlayerQuitEvent( PlayerQuitEvent event ) {
		Player player = event.getPlayer();
		if ( protocol.hasInjected( player ) ) {
			protocol.uninjectPlayer( player );
		}
	}
	
	@EventHandler( priority = EventPriority.MONITOR )
	public void onPlayerChat( AsyncPlayerChatEvent event ) {
		Player player = event.getPlayer();
		BrickPlayer bPlayer = BrickPlayerManager.getInstance().getPlayer( event.getPlayer().getUniqueId() );
		String name = bPlayer.getActiveChannelName();
		if ( name.equalsIgnoreCase( "log" ) ) {
			return;
		}
		for ( Player onlinePlayer : Bukkit.getOnlinePlayers() ) {
			BrickPlayer brickPlayer = BrickPlayerManager.getInstance().getPlayer( onlinePlayer.getUniqueId() );
			brickPlayer.getChannel( name ).add( ChatMessage.getMessageFromString( String.format( event.getFormat(), player.getName(), event.getMessage() ) ) );
		}
	}
}
