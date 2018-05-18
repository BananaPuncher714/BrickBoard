package io.github.bananapuncher714.brickboard.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.api.ChatBox;
import io.github.bananapuncher714.brickboard.api.chat.ChatMessage;
import io.github.bananapuncher714.ngui.objects.BoxCoord;

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

	@Override
	public ChatBox clone() {
		return new ChatBoxFiller( color );
	}

	@Override
	public ConfigurationSection serialize() {
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChatBoxFiller other = (ChatBoxFiller) obj;
		if (color != other.color)
			return false;
		return true;
	}

	public static ChatBox deserialize( ConfigurationSection map ) {
		ChatColor color = ChatColor.valueOf( map.getString( "color" ).toUpperCase() );
		return new ChatBoxFiller( color );
	}
}
