package shskullfinding;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlacingListener implements Listener {
	
	@EventHandler
	public void onSkullPlace(BlockPlaceEvent e) {
		Block placedBlock = e.getBlockPlaced();
		Location placedLocation = placedBlock.getLocation();
		if (placedBlock.getType() != Material.SKULL) return;
		SkullManager.getManager().addSkull(placedLocation, e.getPlayer());
	}
	
	@EventHandler
	public void onSkullBreak(BlockBreakEvent e) {
		Block brokenBlock = e.getBlock();
		Location placedLocation = brokenBlock.getLocation();
		if (brokenBlock.getType() != Material.SKULL) return;
		SkullManager.getManager().removeSkull(placedLocation,e.getPlayer());
	}
	
}