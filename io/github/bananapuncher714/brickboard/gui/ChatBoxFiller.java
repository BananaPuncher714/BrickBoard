package io.github.bananapuncher714.brickboard.gui;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.chat.ChatMessage;
import io.github.bananapuncher714.brickboard.objects.BoxCoord;

public class ChatBoxFiller extends ChatBox {
	protected ChatColor color;
	private String stuffing = StringUtils.repeat( '|', 2000 );
	
	public ChatBoxFiller( ChatColor color ) {
		this.color = color;
	}
	
	@Override
	public List< ChatMessage > getMessages( Player player, BoxCoord coord ) {
		List< ChatMessage > messages = new ArrayList< ChatMessage >();
		messages.add( ChatMessage.getMessageFromString( color + stuffing ) );
		return messages;
	}

}
