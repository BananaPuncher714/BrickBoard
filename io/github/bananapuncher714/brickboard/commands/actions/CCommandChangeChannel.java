package io.github.bananapuncher714.brickboard.commands.actions;

import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.BrickPlayer;
import io.github.bananapuncher714.brickboard.BrickPlayerManager;
import io.github.bananapuncher714.brickboard.commands.ClickCommand;

public class CCommandChangeChannel implements ClickCommand {
	@Override
	public void onClick( Player player, String... args ) {
		if ( args.length != 1 ) {
			return;
		}
		BrickPlayer bPlayer = BrickPlayerManager.getInstance().getPlayer( player.getUniqueId() );
		bPlayer.setActiveChannel( args[ 0 ] );
		bPlayer.setPage( 0 );
	}
	
	@Override
	public String getId() {
		return "changechannel";
	}
}
