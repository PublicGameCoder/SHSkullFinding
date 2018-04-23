package shskullfinding;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import spacehorizonscore.SpaceHorizonsCore;

public class SHSkullFinding extends JavaPlugin {
	
	private static SHSkullFinding instance;
	public static SpaceHorizonsCore core;
	private static File playerFilesFolder;
	private static File skullLocationsFile;

	@Override
	public void onEnable() {
		instance = this;
		core = (SpaceHorizonsCore) getServer().getPluginManager().getPlugin("SpaceHorizonsCore");
		if (core == null || !core.isEnabled()) {
			System.out.println("SpaceHorizonsCore needed!");
			getPluginLoader().disablePlugin(this);
			return;
		}
		createConfig();
		RegisterEvents();
		getCommand("shfinding").setExecutor(new CommandHandler());
	}
	
	private void createConfig() {
		playerFilesFolder = new File(instance.getDataFolder(), "playerFiles");
		if (!playerFilesFolder.exists()) {
			playerFilesFolder.mkdir();
        }
		
		skullLocationsFile = new File(instance.getDataFolder(), "skullLocations.yml");
		if (!skullLocationsFile.exists()) {
			try {
				skullLocationsFile.createNewFile();
				createDefaults(skullLocationsFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}

	private void RegisterEvents() {
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(new PlacingListener(), instance);
		pm.registerEvents(new FindingListener(), instance);
		
	}
	
	public void createDefaults(File file) {
		List<String> text = new ArrayList<String>();
		int counter = 0;
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(getInstance().getResource("defaultSkullLocationsFile.yml"));
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
	}

	public static SHSkullFinding getInstance() {
		return instance;
	}
	
	public static File getPlayerFilesFolder() {
		return playerFilesFolder;
	}
	
	public static File getskullLocationsFile() {
		return skullLocationsFile;
	}

	public static void setPlayerFilesFolder(File file) {
		playerFilesFolder = file;
	}
}
