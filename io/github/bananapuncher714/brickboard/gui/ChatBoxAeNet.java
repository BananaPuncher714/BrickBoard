package io.github.bananapuncher714.brickboard.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.BrickPlayer;
import io.github.bananapuncher714.brickboard.BrickPlayerManager;
import io.github.bananapuncher714.brickboard.chat.ChatMessage;
import io.github.bananapuncher714.brickboard.objects.BoxCoord;
import io.github.bananapuncher714.brickboard.objects.MinecraftFontContainer;
import io.github.bananapuncher714.brickboard.util.MessageUtil;

public class ChatBoxAeNet extends ChatBox {
	private static final List< ChatMessage > messages;
	
	static {
		messages = new ArrayList< ChatMessage >();
		
		messages.add( ChatMessage.getMessageFromString( " ".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&9A".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&1A&9e".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&3A&1e&9t".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bA&3e&1t&9e".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bAe&3t&1e&9r".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bAet&3e&1r&9n".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bAete&3r&1n&9u".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bAeter&3n&1u&9m".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bAetern&3u&1m&9".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bAeternu&3m&1 &9N".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bAeternum&3 &1N&9e".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bAeternum &3N&1e&9t".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bAeternum N&3e&1t&9w".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bAeternum Ne&3t&1w&9o".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bAeternum Net&3w&1o&9r".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bAeternum Netw&3o&1r&9k".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bAeternum Netwo&3r&1k ".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bAeternum Networ&3k".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bAeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&aAeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bAeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&aAeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bAeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&aAeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bAeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( " &bAeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&9e &bAeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&9h&1e &bAeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&9T&1h&3e &bAeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&1T&3h&be Aeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&3T&bhe Aeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bThe Aeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bThe Aeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bThe Aeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bThe Aeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bThe Aeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bThe Aeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bThe Aeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bThe Aeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bThe Aeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bThe Aeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bThe Aeternum Network".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bhe Aeternum Networ".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&be Aeternum Netwo".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&b Aeternum Netw".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bAeternum Net".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&beternum Ne".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bernum N".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&brnum ".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bnum".replace( '&', ChatColor.COLOR_CHAR ) ) );
		messages.add( ChatMessage.getMessageFromString( "&bu".replace( '&', ChatColor.COLOR_CHAR ) ) );
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
		MinecraftFontContainer container = bPlayer.getFont();
		msgs.add( MessageUtil.center( message, width, ' ', container ) );
		return msgs;
	}

}
