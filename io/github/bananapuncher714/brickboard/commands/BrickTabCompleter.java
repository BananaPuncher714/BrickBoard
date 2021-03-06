package io.github.bananapuncher714.brickboard.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import io.github.bananapuncher714.brickboard.BrickBoard;

public class BrickTabCompleter implements TabCompleter {
	@Override
	public List< String > onTabComplete( CommandSender sender, Command command, String label, String[] args ) {
		List< String > completions = new ArrayList< String >();
		List< String > aos = new ArrayList< String >();
		if ( args.length == 0 || args.length == 1 ) {
			aos.add( "edit" );
			aos.add( "presets" );
		}
		
		StringUtil.copyPartialMatches( args[ args.length - 1 ], aos, completions );
		Collections.sort( completions );
		return completions;
	}

}
