package io.github.bananapuncher714.brickboard.util;

import org.bukkit.Bukkit;

import io.github.bananapuncher714.brickboard.api.PacketHandler;

public class ReflectionUtils {
	public static final String VERSION;
	
	static {
		VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	}
	
	public static PacketHandler getNewPacketHandlerInstance() {
		try {
			Class< ? > clazz = Class.forName( "io.github.bananapuncher714.brickboard.implementation." + VERSION + ".BBPacketHandler" );
			return ( PacketHandler ) clazz.newInstance();
		} catch ( ClassNotFoundException | InstantiationException | IllegalAccessException e ) {
			e.printStackTrace();
			return null;
		}
	}
}
