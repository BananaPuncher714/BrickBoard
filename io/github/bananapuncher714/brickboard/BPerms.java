package io.github.bananapuncher714.brickboard;

import org.bukkit.permissions.Permissible;

import io.github.bananapuncher714.brickboard.objects.Board;

public class BPerms {
	public static boolean isAdmin( Permissible who ) {
		return who.hasPermission( "brickboard.admin" );
	}

	public static boolean hasBoardAccess( String board, Permissible who ) {
		return board.equalsIgnoreCase( "default" ) || who.hasPermission( "brickboard.boards." + board + ".access" ) || isAdmin( who );
	}
	
	public static boolean canEdit( String board, Permissible who ) {
		return who.hasPermission( "brickboard.boards." + board + ".edit" ) || isAdmin( who );
	}
	
	public static boolean canGetPresets( Permissible who ) {
		return who.hasPermission( "brickboard.inventory.presets" );
	}
	
	public static boolean canAccessChannel( String channel, Permissible who ) {
		return channel.equalsIgnoreCase( "log" ) || who.hasPermission( "brickboard.channels." + channel + ".receive" ) || canWriteChannel( channel, who );
	}
	
	public static boolean canWriteChannel( String channel, Permissible who ) {
		return channel.equalsIgnoreCase( "log" ) || who.hasPermission( "brickboard.channels." + channel + ".send" );
	}
}
