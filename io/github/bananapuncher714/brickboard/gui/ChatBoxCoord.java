package io.github.bananapuncher714.brickboard.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.chat.ChatComponent;
import io.github.bananapuncher714.brickboard.chat.ChatMessage;
import io.github.bananapuncher714.brickboard.objects.BoxCoord;

public class ChatBoxCoord extends ChatBox {

	@Override
	public List< ChatMessage > getMessages( Player player, BoxCoord coord ) {
		List< ChatMessage > messages = new ArrayList< ChatMessage >();
		Location location = player.getLocation();
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
		messages.add( new ChatMessage().addComponent( new ChatComponent( "X: " + x + " Y: " + y + " Z: " + z ) ) );
		return messages;
	}

}
