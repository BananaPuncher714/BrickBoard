package io.github.bananapuncher714.brickboard.implementation.API;

import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.chat.ChatMessage;

public interface PacketHandler {
	/**
	 * Not called from the main thread.
	 * 
	 * @param player
	 * Player receiving the packet
	 * @param packet
	 * NMS packet being sent
	 * 
	 * @return
	 * Whether or not to return the packet
	 */
	public boolean handlePacket( Player player, Object packet );
	
	/**
	 * Send a message to a player directly; See {@link PacketHandler#sendMessage(Player, ChatMessage)}
	 * 
	 * @param player
	 * The player to send
	 * @param message
	 * The message to send
	 */
	@Deprecated
	public void sendMessage( Player player, String message );
	
	/**
	 * Send a message to a player directly using Banana's ChatMessage system
	 * 
	 * @param player
	 * The player to send
	 * @param component
	 * The ChatMessage they will recieve; does not undergo any formatting
	 */
	public void sendMessage( Player player, ChatMessage message );
}
