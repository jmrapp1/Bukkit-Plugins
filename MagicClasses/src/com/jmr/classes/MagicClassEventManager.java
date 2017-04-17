package com.jmr.classes;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.jmr.menu.MenuManager;

public class MagicClassEventManager implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.hasBlock()) {
			if (event.getClickedBlock().getType() == Material.WALL_SIGN || event.getClickedBlock().getType() == Material.SIGN || event.getClickedBlock().getType() == Material.SIGN_POST) {
				if (event.getPlayer().hasPermission("classes.removeSigns")) {
					if (event.getPlayer().getItemInHand().getData().getItemType() == Material.STICK) {
						return;
					}
				}
				Sign sign = (Sign) event.getClickedBlock().getState();
				if (sign.getLine(0).equalsIgnoreCase("[Magic Classes]")) {
					if (event.getPlayer().hasPermission("classes.magicclassesmenu")) {
						MenuManager.getInstance().getMenu("magicClasses").open(event.getPlayer());
					}
					if (event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType() == Material.STICK)
						event.setCancelled(false);
					else
						event.setCancelled(true);
				} else if (sign.getLine(1).equalsIgnoreCase("[Spell Shop]")) {
					if (event.getPlayer().hasPermission("classes.magicshopmenu")) {
						String magicClassName = sign.getLine(0).replace("[", "").replace("]", "");
						MagicClass mc = MagicClassManager.getInstance().getMagicClass(magicClassName);
						if (mc != null) {
							MenuManager.getInstance().getMenu("spellShop").open(event.getPlayer(), mc);
						}
						if (event.getPlayer().getItemInHand() != null && event.getPlayer().hasPermission("classes.removesign") && event.getPlayer().getItemInHand().getType() == Material.STICK)
							event.setCancelled(false);
						else
							event.setCancelled(true);
					}
				}
			}
		}
	}
}
