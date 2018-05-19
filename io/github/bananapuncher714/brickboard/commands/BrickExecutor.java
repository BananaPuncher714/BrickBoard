package io.github.bananapuncher714.brickboard.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.bananapuncher714.brickboard.BoardManager;
import io.github.bananapuncher714.brickboard.BrickBoard;
import io.github.bananapuncher714.brickboard.BrickPlayer;
import io.github.bananapuncher714.brickboard.BrickPlayerManager;
import io.github.bananapuncher714.brickboard.api.chat.ChatMessage;
import io.github.bananapuncher714.brickboard.inventory.BoardCreatorHolder;
import io.github.bananapuncher714.brickboard.inventory.ChatBoxSelectionHolder;
import io.github.bananapuncher714.brickboard.objects.Board;

public class BrickExecutor implements CommandExecutor {
	private Map< String, ClickCommand > commands = new HashMap< String, ClickCommand >();

	@Override
	public boolean onCommand( CommandSender arg0, Command arg1, String arg2, String[] arg3 ) {
		if ( arg3.length > 0 ) {
			if ( arg3[ 0 ].equalsIgnoreCase( "execute" ) || arg3[ 0 ].equalsIgnoreCase( "exec" ) ) {
				executeCommand( arg0, arg3 );
			} else if ( arg3[ 0 ].equalsIgnoreCase( "edit" ) ) {
				newBoard( arg0, arg3 );
			} else if ( arg3[ 0 ].equalsIgnoreCase( "presets" ) ) {
				getPresets( arg0, arg3 );
			} else if ( arg3[ 0 ].equalsIgnoreCase( "parse" ) ) {
				parse( arg0, arg3 );
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

		Board board = BoardManager.getInstance().getBoard( name );
		if ( board == null ) {
			board = new Board( name );
			BoardManager.getInstance().addBoard( board );
		}
		
		player.openInventory( new BoardCreatorHolder( player, board ).getInventory() );
	}
	
	public void getPresets( CommandSender sender, String... args ) {
		if ( !sender.hasPermission( BrickBoard.Permission.ADMIN.getPermission() ) ) {
			sender.sendMessage( "No permission!" );
			return;
		} else if ( !( sender instanceof Player ) ) {
			sender.sendMessage( "Must be player!" );
			return;
		}
		Player player = ( Player ) sender;
		player.openInventory( new ChatBoxSelectionHolder( player ).getInventory() );
	}

	public void registerClickCommand( ClickCommand command ) {
		commands.put( command.getId().toLowerCase(), command );
	}
	
	private void parse( CommandSender sender, String... args )  {
		if ( !sender.hasPermission( BrickBoard.Permission.ADMIN.getPermission() ) ) {
			sender.sendMessage( "No permission!" );
			return;
		} else if ( !( sender instanceof Player ) ) {
			sender.sendMessage( "Must be player!" );
			return;
		}
		Player player = ( Player ) sender;
		StringBuilder builder = new StringBuilder();
		for ( String arg : args ) {
			builder.append( arg );
			builder.append( " " );
		}
		String message = builder.toString().replace( '&', ChatColor.COLOR_CHAR );
		BrickPlayer bPlayer = BrickPlayerManager.getInstance().getPlayer( player.getUniqueId() );
		bPlayer.addToLog( ChatMessage.getMessageFromString( message, true ) );
	}
}
