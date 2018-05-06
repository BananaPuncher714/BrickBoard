package io.github.bananapuncher714.ngui;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class NGui extends JavaPlugin {
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents( new ClickListener(), this );
	}
}
