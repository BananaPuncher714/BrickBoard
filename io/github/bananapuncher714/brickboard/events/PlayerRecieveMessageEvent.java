package io.github.bananapuncher714.brickboard.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import io.github.bananapuncher714.brickboard.chat.ChatMessage;

/**
 * Only receives messages that would not have been sent to the player 
 * 
 * @author BananaPuncher714
 */
public class PlayerRecieveMessageEvent extends PlayerEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	
	private ChatMessage message;
	private boolean cancelled;
	
	public PlayerRecieveMessageEvent( Player player, ChatMessage message ) {
		super( player );
		this.message = message;
	}
	
	public ChatMessage getMessage() {
		return message;
	}

	public void setMessage( ChatMessage message ) {
		this.message = message;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled( boolean cancelled ) {
		this.cancelled = cancelled;
	}	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
}
