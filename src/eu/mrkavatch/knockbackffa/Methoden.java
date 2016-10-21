package eu.mrkavatch.knockbackffa;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Methoden
{

	public static void giveItems(Player p)
	{
		p.getInventory().addItem(Main.startItem);
		p.setHealth(20);
		p.setFoodLevel(20);
		p.setSaturation(20);
	}

	public static Player getLatestDamager(Player player)
	{	
		return Main.lastDamage.get(player);
	}

	public static void playEffect(Location location)
	{
		FireworkEffect.Builder builder = FireworkEffect.builder();

		FireworkEffect effect = builder.flicker(true).trail(true).with(FireworkEffect.Type.BURST)
				.withColor(Color.RED).withFade(Color.ORANGE).build();

		for (Player p : Bukkit.getOnlinePlayers())
		{
			Fireworks.spawn(location.add(0,10,0), effect, p);
		}

	}

	public static ItemStack standartItem()
	{
		ItemStack item = new ItemStack(Material.STICK);
		ItemMeta itemM = item.getItemMeta();
		item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
		itemM.setDisplayName("§cKnockback FFA");
		item.setItemMeta(itemM);
		return item;
	}
}
