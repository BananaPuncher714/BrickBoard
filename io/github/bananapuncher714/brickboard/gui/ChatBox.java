package io.github.bananapuncher714.brickboard.gui;

import java.util.List;

import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.chat.ChatMessage;
import io.github.bananapuncher714.ngui.objects.BoxCoord;

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
	
	@Override
	public abstract ChatBox clone();
}