package io.github.bananapuncher714.brickboard.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bananapuncher714.brickboard.BrickPlayer;
import io.github.bananapuncher714.brickboard.BrickPlayerManager;
import io.github.bananapuncher714.brickboard.FontManager;
import io.github.bananapuncher714.brickboard.chat.ChatMessage;
import io.github.bananapuncher714.brickboard.objects.MinecraftFontContainer;
import io.github.bananapuncher714.brickboard.util.MessageUtil;
import io.github.bananapuncher714.ngui.items.ItemBuilder;
import io.github.bananapuncher714.ngui.objects.BoxCoord;
import io.github.bananapuncher714.ngui.util.NBTEditor;

public class ChatBoxAeNet extends ChatBox {
	private static final List< ChatMessage > messages;
	
	private FontManager manager;
	
	public ChatBoxAeNet( FontManager manager ) {
		this.manager = manager;
	}
	
	static {
		messages = new ArrayList< ChatMessage >();
		
		messages.add( ChatMessage.getMessageFromString( " ".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&9&lA".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&1&lA&9&le".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&3&lA&1&le&9&lt".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lA&3&le&1&lt&9&le".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lAe&3&lt&1&le&9&lr".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lAet&3&le&1&lr&9&ln".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lAete&3&lr&1&ln&9&lu".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lAeter&3&ln&1&lu&9&lm".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lAetern&3&lu&1&lm&9&l".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lAeternu&3&lm&1&l &9&lN".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lAeternum&3&l &1&lN&9&le".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lAeternum &3&lN&1&le&9&lt".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lAeternum N&3&le&1&lt&9&lw".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lAeternum Ne&3&lt&1&lw&9&lo".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lAeternum Net&3&lw&1&lo&9&lr".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lAeternum Netw&3&lo&1&lr&9&lk".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lAeternum Netwo&3&lr&1&lk ".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lAeternum Networ&3&lk".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lAeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&a&lAeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lAeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&a&lAeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lAeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&a&lAeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lAeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lAeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&9&le &b&lAeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&9&lh&1&le &b&lAeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&9&lT&1&lh&3&le &b&lAeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&1&lT&3&lh&b&le Aeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&3&lT&b&lhe Aeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lThe Aeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lThe Aeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lThe Aeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lThe Aeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lThe Aeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lThe Aeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lThe Aeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lThe Aeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lThe Aeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lThe Aeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lThe Aeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lhe Aeternum Networ".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&le Aeternum Netwo".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&l Aeternum Netw".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lAeternum Net".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&leternum Ne".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lernum N".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lrnum ".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lnum".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b&lu".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( " ".replace( '&', ChatColor.COLOR_CHAR ) ) );
	}
	
	private double index = 0;

	@Override
	public List< ChatMessage > getMessages( Player player, BoxCoord coord ) {
		List< ChatMessage > msgs = new ArrayList< ChatMessage >();
		int width = coord.getWidth();
		index = ( index + .7 ) % messages.size();
		ChatMessage message = messages.get( ( int ) index );
		BrickPlayer bPlayer = BrickPlayerManager.getInstance().getPlayer( player.getUniqueId() );
		MinecraftFontContainer container = manager.getContainer( bPlayer.getFont() );
		msgs.add( MessageUtil.center( message, width, ' ', container ) );
		return msgs;
	}

	@Override
	public ChatBox clone() {
		return new ChatBoxAeNet( manager );
	}
}