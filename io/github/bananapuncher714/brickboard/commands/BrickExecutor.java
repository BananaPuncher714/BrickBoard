package io.github.bananapuncher714.brickboard.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.BrickBoard;
import io.github.bananapuncher714.brickboard.inventory.BoardCreatorHolder;
import io.github.bananapuncher714.brickboard.objects.BoardTemplate;

public class BrickExecutor implements CommandExecutor {
	private Map< String, ClickCommand > commands = new HashMap< String, ClickCommand >();

	@Override
	public boolean onCommand( CommandSender arg0, Command arg1, String arg2, String[] arg3 ) {
		if ( arg3.length > 0 ) {
			if ( arg3[ 0 ].equalsIgnoreCase( "execute" ) || arg3[ 0 ].equalsIgnoreCase( "exec" ) ) {
				executeCommand( arg0, arg3 );
			} else if ( arg3[ 0 ].equalsIgnoreCase( "edit" ) ) {
				newBoard( arg0, arg3 );
			}
		}
		return false;
	}

	private void executeCommand( CommandSender sender, String... args ) {
		if ( sender instanceof Player ) {
			Player player = ( Player ) sender;
			if ( args.length > 1 ) {
				ClickCommand command = commands.get( args[ 1 ].toLowerCase() );

				if ( command == null ) {
					return;
				}

				String[] params = new String[ args.length - 2 ];
				for ( int i = 0; i < params.length; i++ ) {
					params[ i ] = args[ i + 2 ];
				}

				command.onClick( player, params );
			}
		}
	}

	public void newBoard( CommandSender sender, String... args ) {
		if ( !sender.hasPermission( BrickBoard.Permission.ADMIN.getPermission() ) ) {
			sender.sendMessage( "No permission!" );
			return;
		} else if ( !( sender instanceof Player ) ) {
			sender.sendMessage( "Must be player!" );
			return;
		}
		Player player = ( Player ) sender;
		String name = args.length > 1 ? args[ 1 ] : "default";

		( ( Player ) sender ).openInventory( new BoardCreatorHolder( player, new BoardTemplate( name ) ).getInventory() );
	}

	public void registerClickCommand( ClickCommand command ) {
		commands.put( command.getId().toLowerCase(), command );
	}

}
