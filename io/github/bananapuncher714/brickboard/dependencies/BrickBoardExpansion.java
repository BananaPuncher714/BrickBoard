package io.github.bananapuncher714.brickboard.dependencies;

import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.BrickBoard;
import io.github.bananapuncher714.brickboard.BrickPlayer;
import io.github.bananapuncher714.brickboard.BrickPlayerManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class BrickBoardExpansion extends PlaceholderExpansion {

	@Override
	public String getAuthor() {
		return "BananaPuncher714";
	}

	@Override
	public String getIdentifier() {
		return "BrickBoard";
	}

	@Override
	public String getPlugin() {
		return BrickBoard.getInstance().getName();
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String onPlaceholderRequest( Player player, String identifier ) {
		if ( player == null ) {
			return null;
		}
		BrickPlayer bPlayer = BrickPlayerManager.getInstance().getPlayer( player.getUniqueId() );
		if ( identifier.equalsIgnoreCase( "active_channel" ) ) {
			return bPlayer.getActiveChannelName();
		} else {
			return null;
		}
	}

}
