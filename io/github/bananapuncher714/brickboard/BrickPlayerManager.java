package io.github.bananapuncher714.brickboard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BrickPlayerManager {
	private static BrickPlayerManager instance;
	
	private Map< UUID, BrickPlayer > players = new HashMap< UUID, BrickPlayer >();
	
	private BrickPlayerManager() {
	}
	
	public BrickPlayer getPlayer( UUID uuid ) {
		if ( players.containsKey( uuid ) ) {
			return players.get( uuid );
		} else {
			BrickPlayer player = new BrickPlayer( uuid, FontManager.getInstance().getDefaultContainer().getId() );
			players.put( uuid, player );
			return player;
		}
	}
	
	public static BrickPlayerManager getInstance() {
		if ( instance == null ) {
			instance = new BrickPlayerManager();
		}
		return instance;
	}

}
