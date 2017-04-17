package com.jmr.menu.menus;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.jmr.classes.MagicClass;
import com.jmr.classes.MagicClassManager;
import com.jmr.menu.IGameMenu;
import com.jmr.menu.MenuCloseBehaviour;
import com.jmr.menu.MenuItem;
import com.jmr.menu.Plugin;
import com.jmr.menu.PopupMenu;
import com.jmr.pmanager.PlayerInformation;
import com.jmr.pmanager.PlayerManager;
import com.nisovin.magicspells.MagicSpells;
import com.nisovin.magicspells.Spell;
import com.nisovin.magicspells.Spellbook;

public class SpellShopMenu implements IGameMenu {

	private PopupMenu menu;
	private MagicClass magicClass;
	
	public SpellShopMenu() {
		menu = Plugin.createMenu("Spell Shop", 4);		
		menu.setMenuCloseBehaviour(new MenuCloseBehaviour() {

			@Override
			public void onClose(Player player) {
			}
			
		});
	}

	private void addItems(Player player, PopupMenu menu) {
		for (int i = 0, j = 0; i < magicClass.getSpells().size(); i++) {
			final Spell spell = magicClass.getSpells().get(i);
			ItemStack item = MagicSpells.getSpellItemWithLore(spell, false, true);
			if (item != null) {
				final int price = spell.getPrice();
				final PlayerInformation info = PlayerManager.getInstance().getInfo(player);
				List<String> lore = item.getItemMeta().getLore();
				if (lore == null)
					lore = new ArrayList<String>();
				final boolean owns = info.ownsSpell(spell);
				if (owns) {
					lore.add("Owned.");
				} else {
					lore.add("Price: $" + price);
				}
				item.getItemMeta().setLore(lore);
				MenuItem menuItem = new MenuItem(spell.getName(), item.getData()) {
	
					@Override
					public void onClick(Player player) {
						if (!owns) {
							int money = info.getCoins();
							if (money >= price) {
								info.setCoins(money - price);
								Spellbook spellbook = MagicSpells.getSpellbook(player);
								spellbook.addSpell(spell);
								spellbook.save();
								player.sendMessage(ChatColor.GOLD + "You have purchase the \"" + spell.getName() + "\" spell for $" + price + "!");
							} else {
								player.sendMessage(ChatColor.RED + "You don't have enough money to purchase the \"" + spell.getName() + "\" spell!");
							}
						} else {
							player.sendMessage(ChatColor.RED + "You already own the \"" + spell.getName() + "\" spell!");
						}
						getMenu().closeMenu(player);
					}
					
				};
				menuItem.setDescriptions(lore);
				menu.addMenuItem(menuItem, j);
				j++;
			}
		}
	}
	
	@Override
	public void open(Player player, Object...obj) {
		if (obj.length == 1) {
			magicClass = (MagicClass) obj[0];
			PopupMenu menuClone = menu.clone();
			menuClone.setExitOnClickOutside(true);
			addItems(player, menuClone);
			menuClone.openMenu(player);
		}
	}
	
}
