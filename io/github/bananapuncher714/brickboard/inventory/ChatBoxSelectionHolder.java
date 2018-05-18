package io.github.bananapuncher714.brickboard.inventory;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import io.github.bananapuncher714.brickboard.ChatBoxManager;
import io.github.bananapuncher714.brickboard.api.ChatBox;
import io.github.bananapuncher714.ngui.inventory.BananaHolder;
import io.github.bananapuncher714.ngui.items.ItemBuilder;
import io.github.bananapuncher714.ngui.items.SkullBuilder;
import io.github.bananapuncher714.ngui.util.NBTEditor;

public class ChatBoxSelectionHolder extends BananaHolder {
	public static final String ARROW_DOWN = "http://textures.minecraft.net/texture/ff7416ce9e826e4899b284bb0ab94843a8f7586e52b71fc3125e0286f926a";
	public static final String ARROW_UP = "http://textures.minecraft.net/texture/5da027477197c6fd7ad33014546de392b4a51c634ea68c8b7bcc0131c83e3f";
	
	Inventory inventory;
	Player player;
	int page = 0;
	
	public ChatBoxSelectionHolder( Player player ) {
		this.player = player;
		inventory = Bukkit.createInventory( this, 54, "ChatBoxSelectionHolder" );
	}

	@Override
	public Inventory getInventory() {
		inventory.clear();
		for ( int i = 0; i < 8; i++ ) {
			ItemStack item;
			if ( i == 0 && page > 0 ) {
				item = new SkullBuilder( "Go up", ARROW_UP, true ).addFlags( ItemFlag.values() ).getItem();
				item = NBTEditor.setItemTag( item, "page-up", "brickboard", "inventory", "meta-1" );
			} else if ( i == 5 && page < 14 ) {
				item = new SkullBuilder( "Go down", ARROW_DOWN, true ).addFlags( ItemFlag.values() ).getItem();
				item = NBTEditor.setItemTag( item, "page-down", "brickboard", "inventory", "meta-1" );
			} else {
				item = new ItemBuilder( Material.STAINED_GLASS_PANE, 1, ( byte ) 15, " ", false ).addFlags( ItemFlag.values() ).getItem();
			}
			item = NBTEditor.setItemTag( item, ( byte ) 1, "brickboard", "inventory", "custom-item" );
			inventory.setItem( inventory.getSize() - ( 9 - i ), item );
		}
		
		int i = 0;
		Map< String, ChatBox > presets = ChatBoxManager.getInstance().getPresets();
		for ( String box : presets.keySet() ) {
			ItemStack item = new ItemBuilder( Material.PAPER, 1, ( byte ) 0, box, false ).addFlags( ItemFlag.values() ).getItem();
			item = NBTEditor.setItemTag( item, ( byte ) 1, "brickboard", "inventory", "custom-item" );
			item = NBTEditor.setItemTag( item, box, "brickboard", "inventory", "meta-1" );
			inventory.setItem( i++, item );
		}
		
		return inventory;
	}

	@Override
	public void onInventoryClick(InventoryClickEvent event) {

	}

}
