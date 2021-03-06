package io.github.bananapuncher714.brickboard.commands.actions;

import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.BPerms;
import io.github.bananapuncher714.brickboard.BoardManager;
import io.github.bananapuncher714.brickboard.BrickPlayer;
import io.github.bananapuncher714.brickboard.BrickPlayerManager;
import io.github.bananapuncher714.brickboard.commands.ClickCommand;
import io.github.bananapuncher714.brickboard.objects.Board;

public class CCommandChangeBoard implements ClickCommand {
	
	@Override
	public void onClick( Player player, String... args ) {
		if ( args.length != 1 ) {
			return;
		}
		String id = args[ 0 ];
		Board board = BoardManager.getInstance().getBoard( id );
		if ( board == null ) {
			return;
		}
		if ( !BPerms.hasBoardAccess( id, player ) ) {
			return;
		}
		BrickPlayer bPlayer = BrickPlayerManager.getInstance().getPlayer( player.getUniqueId() );
		bPlayer.setActiveBoard( board );
	}

	@Override
	public String getId() {
		return "changeboard";
	}

}
