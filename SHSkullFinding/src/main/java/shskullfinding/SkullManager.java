package shskullfinding;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class SkullManager {

	private static SkullManager sm;
	private List<Location> skullLocations;
	
	public SkullManager() {
		skullLocations = new ArrayList<Location>();
		LoadSkullLocations();
	}
	
	public static SkullManager getManager() {
		if (sm == null) sm = new SkullManager();
		return sm;
	}

	public void addSkull(Location loc,Player p) {
		if (skullLocations.contains(loc))return;
		
		skullLocations.add(loc);
		File skullLocationsFile = SHSkullFinding.getskullLocationsFile();
		FileConfiguration skullLocationsConfigFile = YamlConfiguration.loadConfiguration(skullLocationsFile);
		List<String> skullLocationsStrings = skullLocationsConfigFile.getStringList("locations");
		String sprippedLocation = GenerateStrippedLoc(loc);
		if (!skullLocationsStrings.contains(sprippedLocation)) {
			skullLocationsStrings.add(sprippedLocation);
			skullLocationsConfigFile.set("locations", skullLocationsStrings);
			try {
				skullLocationsConfigFile.save(skullLocationsFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&lSH&7] &6Succesfully &aadded &6a skull to find!"));
		}
		
		try {
			skullLocationsConfigFile.save(skullLocationsFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean checkSkull(Location loc) {
		if (!skullLocations.contains(loc))return false;
		else return true;
	}

	public void removeSkull(Location loc, Player p) {
		if (checkSkull(loc)) {
			skullLocations.remove(loc);
			
			File skullLocationsFile = SHSkullFinding.getskullLocationsFile();
			FileConfiguration skullLocationsConfigFile = YamlConfiguration.loadConfiguration(skullLocationsFile);
			List<String> skullLocationsStrings = skullLocationsConfigFile.getStringList("locations");
			String sprippedLocation = GenerateStrippedLoc(loc);
			if (skullLocationsStrings.contains(sprippedLocation)) {
				skullLocationsStrings.remove(sprippedLocation);
				skullLocationsConfigFile.set("locations", skullLocationsStrings);
				try {
					skullLocationsConfigFile.save(skullLocationsFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&lSH&7] &6Succesfully &cremoved &6a skull to find!"));
			}
		}
	}
	
	public void addFoundSkull(Player p, Block b) {
		File playerFile = findFileFromPlayer(p);
		FileConfiguration playerConfigFile = YamlConfiguration.loadConfiguration(playerFile);
		List<String> Foundlocations = playerConfigFile.getStringList("locations");
		String sprippedLocation = GenerateStrippedLoc(b.getLocation());
		if (!Foundlocations.contains(sprippedLocation)) {
			Foundlocations.add(GenerateStrippedLoc(b.getLocation()));
			playerConfigFile.set("locations", Foundlocations);
			try {
				playerConfigFile.save(playerFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (Foundlocations.size() >= skullLocations.size()) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&lSH&7] &6&lYou have found all skulls!"));
			}else {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&lSH&7] &aFound a skull! &8[ &b"+Foundlocations.size()+"&6/&9"+skullLocations.size()+" &8]"));
			}
		}else {
			if (Foundlocations.size() >= skullLocations.size()) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&lSH&7] &6&lYou have found all skulls!"));
			}else {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&lSH&7] &cSorry but you already found this one!"));
			}
		}
	}

	private String GenerateStrippedLoc(Location loc) {
		String world = loc.getWorld().getName();
		String x = loc.getBlockX()+"";
		String y = loc.getBlockY()+"";
		String z = loc.getBlockZ()+"";
		String sprippedLocString = world+":"+x+":"+y+":"+z;
		return sprippedLocString;
	}
	
	private Location GenerateLocFromSpripped(String strippedLoc) {
		String[] args = strippedLoc.split(":");
		World world = Bukkit.getWorld(args[0]);
		if (world == null) world = Bukkit.getWorld("world");
		int x = Integer.parseInt(args[1]);
		int y = Integer.parseInt(args[2]);
		int z = Integer.parseInt(args[3]);
		Location loc = new Location(world,x,y,z);
		return loc;
	}

	private File findFileFromPlayer(Player p) {
		UUID uuid = p.getUniqueId();
		File playerFilesFolder = new File(SHSkullFinding.getInstance().getDataFolder(), "playerFiles");
		if (!playerFilesFolder.exists()) {
			playerFilesFolder.mkdir();
			SHSkullFinding.setPlayerFilesFolder(playerFilesFolder);
			if (!playerFilesFolder.exists()) {
				System.out.println("Failed to create playerFilesFolder!");
			}
        }
		File playerFile = new File(SHSkullFinding.getPlayerFilesFolder().getPath(), uuid+".yml");
		if (!playerFile.exists()) {
			try {
				playerFile.createNewFile();
				createDefaults(playerFile, p);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
		return playerFile;
	}

	public void createDefaults(File file, Player p) {
		List<String> text = new ArrayList<String>();
		int counter = 0;
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(SHSkullFinding.getInstance().getResource("defaultPlayerFile.yml"));
		while (scanner.hasNextLine()) {
			counter++;
			text.add( scanner.nextLine() );
		}
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (int i = 0; i < counter; i++) {
			//System.out.println(text.get(i)); //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Prints the line of text currently passing through
			try {
				bw.write(text.get(i));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {

				try {

					if (bw != null)
						bw.close();

					if (fw != null)
						fw.close();

				} catch (IOException ex) {

					ex.printStackTrace();

				}

			}
		}
		FileConfiguration playerConfigFile = YamlConfiguration.loadConfiguration(file);
		playerConfigFile.set("playerName", p.getName());
	}
	
	public void LoadSkullLocations() {
		File skullLocationsFile = SHSkullFinding.getskullLocationsFile();
		FileConfiguration skullLocationsConfigFile = YamlConfiguration.loadConfiguration(skullLocationsFile);
		List<String> skullLocationsStrings = skullLocationsConfigFile.getStringList("locations");
		for (String StrippedLoc : skullLocationsStrings) {
			Location loc = GenerateLocFromSpripped(StrippedLoc);
			skullLocations.add(loc);
		}
	}
	
	

}
