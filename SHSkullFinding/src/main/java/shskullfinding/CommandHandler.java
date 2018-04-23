package shskullfinding;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class CommandHandler implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		Player p = (Player) sender;
		
		if (args.length < 2) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&lSH&7] &6&lUsage: &r&6/shfinding <playerHeadName> <amount>"));
			return false;
		}
		
		int amount = 1;
		
		try {
			amount = Integer.parseInt(args[1]);
		} catch (Exception e) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&lSH&7] &cThe specified amount is invalid! we've changed it to 1"));
		}
		
		ItemStack skull = createSkull(args[0], amount);
		
		p.getInventory().setItem(0, skull);
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6&lSH&7] &6You can now start placing the skulls around the map!"));
		return true;
	}

	private ItemStack createSkull(String playerName, int amount) {
		@SuppressWarnings("deprecation")
		ItemStack item = new ItemStack(Material.SKULL_ITEM,amount, (short) 0, (byte) 3);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cSkullFinding &6skulls"));
		meta.setOwner(playerName);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&cSkull Owner: &6"+playerName));
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

}
