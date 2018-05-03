package io.github.bananapuncher714.brickboard.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BrickExecutor implements CommandExecutor {
	private Map< String, ClickCommand > commands = new HashMap< String, ClickCommand >();

	@Override
	public boolean onCommand( CommandSender arg0, Command arg1, String arg2, String[] arg3 ) {
		if ( arg0 instanceof Player ) {
			Player player = ( Player ) arg0;
			if ( arg3.length > 1 ) {
				if ( arg3[ 0 ].equalsIgnoreCase( "execute" ) ) {
					ClickCommand command = commands.get( arg3[ 1 ].toLowerCase() );
					
					if ( command == null ) {
						return false;
					}
					
					String[] args = new String[ arg3.length - 2 ];
					for ( int i = 0; i < args.length; i++ ) {
						args[ i ] = arg3[ i + 2 ];
					}
					
					command.onClick( player, args );
				}
			}
		}
		return false;
	}
	
	public void registerClickCommand( ClickCommand command ) {
		commands.put( command.getId().toLowerCase(), command );
	}

}
