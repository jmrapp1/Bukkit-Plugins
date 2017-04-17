package com.jmr.menu.menus;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import com.jmr.classes.MagicClass;
import com.jmr.classes.MagicClassManager;
import com.jmr.menu.IGameMenu;
import com.jmr.menu.MenuCloseBehaviour;
import com.jmr.menu.MenuItem;
import com.jmr.menu.Plugin;
import com.jmr.menu.PopupMenu;

public class MagicClassesMenu implements IGameMenu {

	private PopupMenu menu;
	
	public MagicClassesMenu() {
		double size = MagicClassManager.getInstance().getMagicClasses().size();
		menu = Plugin.createMenu("Magic Classes", (int)Math.ceil(size / 9));		
		menu.setMenuCloseBehaviour(new MenuCloseBehaviour() {

			@Override
			public void onClose(Player player) {
			}
			
		});
	}

	private void addItems(Player player, PopupMenu menu) {
		for (int i = 0; i < MagicClassManager.getInstance().getMagicClasses().size(); i++) {
			final MagicClass mc = MagicClassManager.getInstance().getMagicClasses().get(i);
			MenuItem item = new MenuItem(mc.getName(), mc.getIcon().getData()) {

				@Override
				public void onClick(Player player) {
					player.setMetadata("magicClass", new FixedMetadataValue(Plugin.instance, mc.getName()));
					player.sendMessage(ChatColor.DARK_PURPLE + "You chose the \"" + mc.getName() + "\" kit and it will be given to you when entering the Magic zones.");
					getMenu().closeMenu(player);
				}
				
			};
			String desc = ChatColor.DARK_PURPLE + mc.getDesc() + "/n";
			System.out.println(mc.getName() + " - " + mc.getDesc());
			item.setDescriptions(Arrays.asList(desc.split("/n")));
			menu.addMenuItem(item, i);
		}
	}
	
	@Override
	public void open(Player player, Object...obj) {
		PopupMenu menuClone = menu.clone();
		addItems(player, menuClone);
		menuClone.openMenu(player);
	}
	
}
