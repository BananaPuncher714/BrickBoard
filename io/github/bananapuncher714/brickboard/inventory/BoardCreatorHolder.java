package io.github.bananapuncher714.brickboard.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.bananapuncher714.brickboard.ChatBoxManager;
import io.github.bananapuncher714.brickboard.api.ChatBox;
import io.github.bananapuncher714.brickboard.objects.Board;
import io.github.bananapuncher714.ngui.inventory.BananaHolder;
import io.github.bananapuncher714.ngui.items.ItemBuilder;
import io.github.bananapuncher714.ngui.items.SkullBuilder;
import io.github.bananapuncher714.ngui.objects.BoxCoord;
import io.github.bananapuncher714.ngui.util.GuiUtil;
import io.github.bananapuncher714.ngui.util.NBTEditor;

public class BoardCreatorHolder extends BananaHolder {
	public static final String ARROW_DOWN = "http://textures.minecraft.net/texture/ff7416ce9e826e4899b284bb0ab94843a8f7586e52b71fc3125e0286f926a";
	public static final String ARROW_UP = "http://textures.minecraft.net/texture/5da027477197c6fd7ad33014546de392b4a51c634ea68c8b7bcc0131c83e3f";
	public static final int MAX_ROWS = 20;
	
	Board board;
	Player player;
	Inventory inventory;
	int page = 0;
	Map< UUID, ChatBox > parts = new HashMap< UUID, ChatBox >();
	
	public BoardCreatorHolder( Player player, Board template ) {
		this.player = player;
		this.board = template;
		inventory = Bukkit.createInventory( this, 54, "Editing board: " + template.getId() );
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
				item = NBTEditor.setItemTag( item, ( byte ) 1, "brickboard", "inventory", "custom-item" );
			} else if ( i == 5 && page < 14 ) {
				item = new SkullBuilder( "Go down", ARROW_DOWN, true ).addFlags( ItemFlag.values() ).getItem();
				item = NBTEditor.setItemTag( item, "page-down", "brickboard", "inventory", "meta-1" );
				item = NBTEditor.setItemTag( item, ( byte ) 1, "brickboard", "inventory", "custom-item" );
			} else {
//				item = new ItemBuilder( Material.STAINED_GLASS_PANE, 1, ( byte ) 15, " ", false ).addFlags( ItemFlag.values() ).getItem();
				item = new ItemStack( Material.AIR );
			}
			int slot = ( ( i + 1 ) * 9 ) - 1;
//			item = NBTEditor.setItemTag( item, ( byte ) 1, "brickboard", "inventory", "custom-item" );
			inventory.setItem( slot, item );
		}
		
		double slotWidth = board.getWidth() / 8.0;
		Map< BoxCoord, ItemStack > organized = new TreeMap< BoxCoord, ItemStack >();
		parts.clear();
		for ( ChatBox box : board.getContainers().keySet() ) {
			ItemStack item = new ItemStack( Material.PAPER );
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName( box.getClass().getSimpleName() );
			item.setItemMeta( meta );
			item = NBTEditor.setItemTag( item, ( byte ) 1, "brickboard", "inventory", "custom-item" );
			item = NBTEditor.setItemTag( item, "edit-box", "brickboard", "inventory", "meta-1" );
			UUID randUUID = UUID.randomUUID();
			parts.put( randUUID, box );
			item = NBTEditor.setItemTag( item, randUUID.toString(), "brickboard", "inventory", "meta-2" );
			
			BoxCoord coord = board.getContainers().get( box );
			int col = ( int ) ( coord.getX() / slotWidth );
			int row = coord.getY();
			
			organized.put( new BoxCoord( col, row, 1, 1 ), item );
		}
		GuiUtil.organize( organized.keySet(), 8, 20 );
		
		for ( BoxCoord coord : organized.keySet() ) {
			byte color = ( byte ) ( coord.hashCode() & 15 );
			
			int coordY = coord.getY();
			
			int slot = GuiUtil.coordToSlot( coord.getX(), coordY, 9, 20 );
			slot = slot - 9 * page;
			if ( coordY >= page && coordY < page + 6 ) {
				inventory.setItem( slot, organized.get( coord ) );
			}
			
			for ( int x = 0; x < coord.getWidth(); x++ ) {
				for ( int y = 0; y < coord.getHeight(); y++ ) {
					if ( x + y == 0 ) {
						continue;
					}
					if ( y + coordY >= page && y + coordY < page + 6 ) {
						int pane = GuiUtil.coordToSlot( coord.getX() + x, coordY + y, 9, 20 );
						pane = pane - 9 * page;
						ItemStack item = new ItemBuilder( Material.STAINED_GLASS_PANE, 1, color, " " ).getItem();
						
						inventory.setItem( pane, item );
					}
				}
			}
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
		
		if ( item != null && NBTEditor.getItemTag( item, "brickboard", "inventory", "custom-item" ) != null ) {
			String meta = ( String ) NBTEditor.getItemTag( item, "brickboard", "inventory", "meta-1" );
			if ( meta == null ) {
				return;
			}
			if ( meta.equalsIgnoreCase( "page-down" ) ) {
				page++;
			} else if ( meta.equalsIgnoreCase( "page-up" ) ) {
				page = Math.max( 0, page - 1 );
			} else if ( meta.equalsIgnoreCase( "edit-box" ) ) {
				UUID uuid = UUID.fromString( ( String ) NBTEditor.getItemTag( item, "brickboard", "inventory", "meta-2" ) );
				board.getContainers().remove( parts.get( uuid ) );
				board.sort( true );
			}
		} else if ( cursor != null && cursor.getType() != Material.AIR ) {
			if ( NBTEditor.getItemTag( cursor, "brickboard", "inventory", "custom-item" ) != null ) {
				String preset = ( String ) NBTEditor.getItemTag( cursor, "brickboard", "inventory", "meta-1" );
				ChatBox box = ChatBoxManager.getInstance().getPreset( preset );
				if ( box == null ) {
					System.out.println( "Preset '" + box + "' not found!" );
					return;
				}
				int[] coords = GuiUtil.slotToCoord( event.getSlot(), 9 );
				int x = ( int ) Math.ceil( coords[ 0 ] * ( board.getWidth() / 8.0 ) );
				int y = coords[ 1 ] + page;
				
				board.setContainer( box, new BoxCoord( x, y ) );
				board.sort( true );
			}
		}
		getInventory();
	}

}
