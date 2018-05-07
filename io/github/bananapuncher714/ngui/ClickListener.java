package io.github.bananapuncher714.ngui;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import io.github.bananapuncher714.ngui.inventory.BananaHolder;
import io.github.bananapuncher714.ngui.inventory.Clickable;

public class ClickListener implements Listener {
	private static Set< Clickable > clickables = new HashSet< Clickable >();
	
	@EventHandler
	public void onInventoryClickEvent( InventoryClickEvent event ) {
		for ( Clickable clickable : clickables ) {
			if ( !clickable.onClick( event ) ) {
				return;
			}
		}
		if ( event.getRawSlot() < 0 ) {
			return;
		}
		Inventory inventory = event.getClickedInventory();
		InventoryHolder holder = inventory.getHolder();
		if ( holder instanceof BananaHolder ) {
			( ( BananaHolder ) holder ).onInventoryClick( event );
		}
		
	}
	
	@EventHandler
	public void onInventoryDragEvent( InventoryDragEvent event ) {
		Inventory inventory = event.getInventory();
		InventoryHolder holder = inventory.getHolder();
		if ( holder instanceof BananaHolder ) {
			for ( int slot : event.getRawSlots() ) {
				if ( event.getInventorySlots().contains( slot ) ) {
					event.setCancelled( true );
				}
			}
			( ( BananaHolder ) holder ).onInventoryDrag( event );
		}
	}
	
	@EventHandler
	public void onInventoryOpenEvent( InventoryOpenEvent event ) {
		Inventory inventory = event.getInventory();
		InventoryHolder holder = inventory.getHolder();
		if ( holder instanceof BananaHolder ) {
			( ( BananaHolder ) holder ).onInventoryOpen( event );
		}
	}
	
	@EventHandler
	public void onInventoryCloseEvent( InventoryCloseEvent event ) {
		Inventory inventory = event.getInventory();
		InventoryHolder holder = inventory.getHolder();
		if ( holder instanceof BananaHolder ) {
			( ( BananaHolder ) holder ).onInventoryClose( event );
		}
	}
	
	public static void registerClickable( Clickable clickable ) {
		clickables.add( clickable );
	}
	
	public static void unregister( Clickable clickable ) {
		clickables.remove( clickable );
	}

}
