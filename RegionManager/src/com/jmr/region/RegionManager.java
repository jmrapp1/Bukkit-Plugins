package com.jmr.region;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.jmr.region.regions.DefaultRegion;
import com.jmr.region.regions.MagicKitRegion;
import com.jmr.region.regions.WelcomeRegion;

public class RegionManager {

	private static final RegionManager instance = new RegionManager();
	private ArrayList<AbstractRegion> regions = new ArrayList<AbstractRegion>();
	
	private RegionManager() {
	}
	
	public void loadRegions() {
		regions.clear();
		try {
			regions.addAll(new RegionLoader().loadRegions("plugins/regions/regions.yml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*if (!f.exists()) {
			f.mkdirs();
			try {
				new File("plugins/regions/regions.txt").createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
		    try {
				BufferedReader br = new BufferedReader(new FileReader("plugins/regions/regions.txt"));
		        String line = br.readLine();

		        while (line != null) {
		        	String[] split = line.split(" ");
		        	String type = split[1];
		        	if (type.equalsIgnoreCase("default")) {
		        		regions.add(new DefaultRegion(split[0], Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]), Integer.parseInt(split[5]), Integer.parseInt(split[6]), Integer.parseInt(split[7]), Boolean.parseBoolean(split[8]), Boolean.parseBoolean(split[9]), Boolean.parseBoolean(split[10]), Boolean.parseBoolean(split[11]), Boolean.parseBoolean(split[12]), Boolean.parseBoolean(split[13])));
		        	} else if (type.equalsIgnoreCase("welcome")) {
		        		regions.add(new WelcomeRegion(split[0], Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]), Integer.parseInt(split[5]), Integer.parseInt(split[6]), Integer.parseInt(split[7]), Boolean.parseBoolean(split[8]), Boolean.parseBoolean(split[9]), Boolean.parseBoolean(split[10]), Boolean.parseBoolean(split[11]), Boolean.parseBoolean(split[12]), Boolean.parseBoolean(split[13])));
		        	} else if (type.equalsIgnoreCase("kitpvp")) {
		        		regions.add(new MagicKitRegion(split[0], Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]), Integer.parseInt(split[5]), Integer.parseInt(split[6]), Integer.parseInt(split[7])));
		        	}
		        	System.out.println("Loaded \"" + split[0] + "\" region.");
		        	line = br.readLine();
		        }
		        br.close();
		    } catch (Exception e) {
		    	e.printStackTrace();
		    } 
		}*/		
	}
	
	/*public void createRegion(Player player) {
		if (player.hasMetadata("regionName") && player.hasMetadata("regionLoc1") && player.hasMetadata("regionLoc2")) {
			String name = player.getMetadata("regionName").get(0).asString();
			if (!regionExists(name)) {
				String type = "default";
				if (player.hasMetadata("regionType")) {
					type = player.getMetadata("regionType").get(0).asString();
					player.removeMetadata("regionType", Plugin.pluginInstance);
				}
				String loc1 = player.getMetadata("regionLoc1").get(0).asString();
				String loc2 = player.getMetadata("regionLoc2").get(0).asString();
				player.removeMetadata("regionLoc1", Plugin.pluginInstance);
				player.removeMetadata("regionLoc2", Plugin.pluginInstance);
				String[] split1 = loc1.split(" ");
				String[] split2 = loc2.split(" ");
				if (type.equalsIgnoreCase("default")) {
					regions.add(new DefaultRegion(name, Integer.parseInt(split1[0]), Integer.parseInt(split1[1]), Integer.parseInt(split1[2]), Integer.parseInt(split2[0]), Integer.parseInt(split2[1]), Integer.parseInt(split2[2]), true, true, true, true, false, false));
				} else if (type.equalsIgnoreCase("welcome")) {
					regions.add(new WelcomeRegion(name, Integer.parseInt(split1[0]), Integer.parseInt(split1[1]), Integer.parseInt(split1[2]), Integer.parseInt(split2[0]), Integer.parseInt(split2[1]), Integer.parseInt(split2[2]), true, true, true, true, false, false));
				} else if (type.equalsIgnoreCase("kitpvp")) {
					regions.add(new MagicKitRegion(name, Integer.parseInt(split1[0]), Integer.parseInt(split1[1]), Integer.parseInt(split1[2]), Integer.parseInt(split2[0]), Integer.parseInt(split2[1]), Integer.parseInt(split2[2])));
				}
				player.sendMessage(ChatColor.AQUA + "The region \"" + name + "\" was created.");
	        	saveRegions();
			} else {
				player.sendMessage(ChatColor.RED + "Region with the name \"" + name + "\" already exists!");
			}
		} else {
			player.sendMessage(ChatColor.RED + "Please make sure the name and location of the region is set.");
		}
	}*/
	
	public boolean deleteRegion(String name) {
		for (AbstractRegion r : regions) {
			if (r.getName().equalsIgnoreCase(name)) {
				regions.remove(r);
				saveRegions();
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<AbstractRegion> getRegions() {
		return regions;
	}
	
	public void saveRegions() {
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("plugins/regions/regions.txt"))));
			for (AbstractRegion r : regions) {
				bw.write(r.getName() + " " + r.getType() + " " + r.getStartX() + " " + r.getStartY() + " " + r.getStartZ() + " " + r.getEndX() + " " + r.getEndY() + " " + r.getEndZ() + " " + r.allowsPvp() + " " + r.allowsBlockBreaking() + " " + r.allowsBlockPlacing() + " " + r.allowsLeaving() + " " + r.savesBlocks() + " " + r.ignoresY());
				bw.newLine();
			}
			bw.close();
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public AbstractRegion getRegion(String name) {
		for (AbstractRegion r : regions) {
			if (r.getName().equalsIgnoreCase(name)) {
				return r;
			}
		}
		return null;
	}
	
	public boolean regionExists(String name) {
		for (AbstractRegion r : regions)
			if (r.getName().equalsIgnoreCase(name))
				return true;
		return false;
	}
	
	public ArrayList<AbstractRegion> getRegionsInside(Player player) {
		ArrayList<AbstractRegion> ret = new ArrayList<AbstractRegion>();
		for (AbstractRegion r : regions)
			if (r.isPlayerInside(player))
				ret.add(r);
		return ret;
	}
	
	public static RegionManager getInstance() {
		return instance;
	}
	
}
