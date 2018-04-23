package shskullfinding;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class FindingListener implements Listener {

	@EventHandler
	public void onInteractWithSkull(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (!e.getHand().equals(EquipmentSlot.HAND))return;
		Block b = e.getClickedBlock();
		if (b.getType() != Material.SKULL) return;
		if (!SkullManager.getManager().checkSkull(b.getLocation()))return;
		SkullManager.getManager().addFoundSkull(e.getPlayer(), b);
	}
}
