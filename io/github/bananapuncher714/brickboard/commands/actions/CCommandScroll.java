package io.github.bananapuncher714.brickboard.commands.actions;

import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.BrickPlayer;
import io.github.bananapuncher714.brickboard.BrickPlayerManager;
import io.github.bananapuncher714.brickboard.commands.ClickCommand;

public class CCommandScroll implements ClickCommand {

	@Override
	public void onClick( Player player, String... args ) {
		if ( args.length != 1 ) {
			return;
		}
		BrickPlayer bPlayer = BrickPlayerManager.getInstance().getPlayer( player.getUniqueId() );
		if ( args[ 0 ].equalsIgnoreCase( "up" ) ) {
			bPlayer.setPage( bPlayer.getPage() + 3 );
		} else{
			bPlayer.setPage( Math.max( 0, bPlayer.getPage() - 3 ) );
		}
	}

	@Override
	public String getId() {
		return "scroll";
	}
}