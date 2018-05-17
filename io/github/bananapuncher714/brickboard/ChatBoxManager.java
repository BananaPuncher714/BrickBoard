package io.github.bananapuncher714.brickboard;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import io.github.bananapuncher714.brickboard.api.ChatBox;

public class ChatBoxManager {
	private static ChatBoxManager instance;
	
	private Map< String, ChatBox > presets = new HashMap< String, ChatBox >();
	private Map< String, Class< ? extends ChatBox > > boxes = new HashMap< String, Class< ? extends ChatBox > >();
	
	public void registerBox( String id, Class< ? extends ChatBox > clazz ) {
		boxes.put( id.toLowerCase(), clazz );
	}
	
	public void addPreset( String id, ChatBox preset ) {
		presets.put( id.toLowerCase(), preset );
	}
	
	public ChatBox getPreset( String id ) {
		return presets.get( id.toLowerCase() ).clone();
	}
	
	protected void loadPresets( File baseDir ) {
		if ( !baseDir.exists() ) {
			return;
		}
		
		for ( File file : baseDir.listFiles() ) {
			FileConfiguration config = YamlConfiguration.loadConfiguration( file );
			String id = config.getString( "required.preset-id" );
			String boxId = config.getString( "required.chatbox-id" );
			if ( id == null || boxId == null ) {
				continue;
			}
			Class< ? extends ChatBox > box = boxes.get( boxId );
			if ( box == null ) {
				continue;
			}
			
			ChatBox boxInstance = null;
			try {
				Method method = box.getMethod( "deserialize", ConfigurationSection.class );
				boxInstance = ( ChatBox ) method.invoke( null, config.getConfigurationSection( "options" ) );
			} catch ( Exception exception ) {
				exception.printStackTrace();
				continue;
			}
			
			presets.put( id.toLowerCase(), boxInstance );
		}
	}
	
	private ChatBoxManager() {
		
	}
	
	public static ChatBoxManager getInstance() {
		if ( instance == null ) {
			instance = new ChatBoxManager();
		}
		return instance;
	}

}
