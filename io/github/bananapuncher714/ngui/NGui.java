package io.github.bananapuncher714.ngui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bananapuncher714.ngui.inventory.BananaHolder;

public class NGui extends JavaPlugin {
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents( new ClickListener(), this );
	}
	
	@Override
	public void onDisable() {
		disable();
	}
	
	public static void disable() {
		for ( Player player : Bukkit.getOnlinePlayers() ) {
			Inventory inventory = player.getOpenInventory().getTopInventory();
			if ( inventory != null && inventory.getHolder() instanceof BananaHolder ) {
				player.closeInventory();
			}
		}
	}
}
