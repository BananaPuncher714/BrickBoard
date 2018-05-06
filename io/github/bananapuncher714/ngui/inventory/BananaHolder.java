package io.github.bananapuncher714.ngui.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;

public abstract class BananaHolder implements InventoryHolder {
	public abstract void onInventoryClick( InventoryClickEvent event );
	
	public void onInventoryDrag( InventoryDragEvent event ) {
	}
	
	public void onInventoryOpen( InventoryOpenEvent event ) {
	}
	
	public void onInventoryClose( InventoryCloseEvent event ) {
	}
}
