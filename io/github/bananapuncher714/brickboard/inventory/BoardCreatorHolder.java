package io.github.bananapuncher714.brickboard.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import io.github.bananapuncher714.ngui.inventory.BananaHolder;
import io.github.bananapuncher714.ngui.items.ItemBuilder;
import io.github.bananapuncher714.ngui.items.SkullBuilder;
import io.github.bananapuncher714.ngui.util.NBTEditor;

public class BoardCreatorHolder extends BananaHolder {
	public static final String ARROW_DOWN = "http://textures.minecraft.net/texture/ff7416ce9e826e4899b284bb0ab94843a8f7586e52b71fc3125e0286f926a";
	public static final String ARROW_UP = "http://textures.minecraft.net/texture/5da027477197c6fd7ad33014546de392b4a51c634ea68c8b7bcc0131c83e3f";
	public static final int MAX_ROWS = 20;
	
	String name;
	Player player;
	Inventory inventory;
	int page = 0;
	
	public BoardCreatorHolder( Player player, String name ) {
		this.player = player;
		this.name = name;
		inventory = Bukkit.createInventory( this, 54, "Creating a new board: " + name );
	}

	@Override
	public Inventory getInventory() {
		inventory.clear();
		// Create the side panels
		for ( int i = 0; i < 6; i++ ) {
			ItemStack item;
			if ( i == 0 && page > 0 ) {
				item = new SkullBuilder( "Go up", ARROW_UP, true ).addFlags( ItemFlag.values() ).getItem();
				item = NBTEditor.setItemTag( item, "page-up", "brickboard", "inventory", "meta-1" );
			} else if ( i == 5 && page < 14 ) {
				item = new SkullBuilder( "Go down", ARROW_DOWN, true ).addFlags( ItemFlag.values() ).getItem();
				item = NBTEditor.setItemTag( item, "page-down", "brickboard", "inventory", "meta-1" );
			} else {
				item  = new ItemBuilder( Material.STAINED_GLASS_PANE, 1, ( byte ) 15, " ", false ).addFlags( ItemFlag.values() ).getItem();
			}
			int slot = ( ( i + 1 ) * 9 ) - 1;
			item = NBTEditor.setItemTag( item, ( byte ) 1, "brickboard", "inventory", "custom-item" );
			inventory.setItem( slot, item );
		}
		
		
		return inventory;
	}

	@Override
	public void onInventoryClick( InventoryClickEvent event ) {
		ClickType click = event.getClick();
		if ( event.getSlot() != event.getRawSlot() ) {
			if ( click.isKeyboardClick() || click.isShiftClick() ) {
				event.setCancelled( true );
			}
			return;
		}
		
		event.setCancelled( true );
		ItemStack item = event.getCurrentItem();
		ItemStack cursor = event.getCursor();
		
		String meta = ( String ) NBTEditor.getItemTag( item, "brickboard", "inventory", "meta-1" );
		if ( meta == null ) {
			return;
		}
		if ( meta.equalsIgnoreCase( "page-down" ) ) {
			page++;
		} else if ( meta.equalsIgnoreCase( "page-up" ) ) {
			page = Math.max( 0, page - 1 );
		} else {
			
		}
		
		getInventory();
	}

}
