package io.github.bananapuncher714.brickboard.api;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.api.chat.ChatMessage;
import io.github.bananapuncher714.ngui.objects.BoxCoord;

/**
 * Any subclasses are required to implement a static deserialize method
 * which must accept a ConfigurationSection object as its argument.
 * 
 * @author BananaPuncher714
 */
public abstract class ChatBox implements Cloneable {
	/**
	 * Return a list of ChatMessages, use '\n' for newlines
	 * @param player
	 * Player who is recieving the messages
	 * @param coord
	 * The BoxCoord in terms of chat positioning, may vary among players
	 * @return
	 */
	public abstract List< ChatMessage > getMessages( Player player, BoxCoord coord );
	
	/**
	 * Load a ChatBox from a FileConfiguration object, used for getting user created ones
	 * 
	 * @param config
	 */
	
	public abstract ConfigurationSection serialize();
	
	@Override
	public abstract ChatBox clone();
}