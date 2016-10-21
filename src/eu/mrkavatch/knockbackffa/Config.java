package eu.mrkavatch.knockbackffa;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Config
{

	public static File configFile = new File("plugins/Knockbackffa", "config.yml");
	public static FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

	public static void setSpawn(Location loc)
	{
		config.set("Spawn.X", loc.getX());
		config.set("Spawn.Y", loc.getY());
		config.set("Spawn.Z", loc.getZ());
		config.set("Spawn.YAW", loc.getYaw());
		config.set("Spawn.PITCH", loc.getPitch());
		config.set("Spawn.WORLD", loc.getWorld().getName());
		saveConfig();

	}

	public static void setSetKills(Player p, int Kills)
	{
		config.set("PlayerKills." + p.getName(), Kills);
		saveConfig();
	}

	public static void setAddKills(Player p, int Kills)
	{
		config.set("PlayerKills." + p.getName(), getKills(p) + 1);
		saveConfig();
	}

	public static Integer getKills(Player p)
	{
		return config.getInt("PlayerKills." + p.getName());
	}

	public static Location getSpawn()
	{
		try
		{
			double y = config.getDouble("Spawn.Y");
			double x = config.getDouble("Spawn.X");
			double z = config.getDouble("Spawn.Z");
			double yaw = config.getDouble("Spawn.YAW");
			double pitch = config.getDouble("Spawn.PITCH");
			String world = config.getString("Spawn.WORLD");
			if (world == null)
			{
				System.out.println("BITTE LEGE EINEN SPAWN FEST");
				return new Location(null, 1, 1, 1, (float) 1, (float) 1);
			}
			return new Location(Bukkit.getWorld(world), x, y, z, (float) yaw, (float) pitch);
		} catch (NullPointerException ex)
		{
			System.out.println("BITTE LEGE EINEN SPAWN FEST");
			return new Location(null, 1, 1, 1, (float) 1, (float) 1);
		}
	}

	public static void setDamage(boolean damageEnable)
	{
		config.set("Settings.damage", damageEnable);
		saveConfig();
	}

	public static boolean getDamage()
	{
		return config.getBoolean("Settings.damage");
	}

	public static void setStick(ItemStack item)
	{
		config.set("Settings.stick", item);
		saveConfig();
	}

	public static ItemStack getStick()
	{
		return config.getItemStack("Settings.stick");
	}

	public static void setCountPoints(boolean pointsEnable)
	{
		config.set("Settings.kills", pointsEnable);
		saveConfig();
	}

	public static boolean getCountPoints()
	{
		return config.getBoolean("Settings.kills");
	}

	public static void setEffect(boolean pointsEnable)
	{
		config.set("Settings.effect", pointsEnable);
		saveConfig();
	}

	public static boolean getEffect()
	{
		return config.getBoolean("Settings.effect");
	}

	public static void setlowestPos(int pos)
	{
		config.set("Settings.lowestpos", pos);
		saveConfig();
	}

	public static Integer getlowestPos()
	{
		return config.getInt("Settings.lowestpos");
	}

	public static void setDefaults()
	{
		config.set("Settings.damage", getDamage());
		config.set("Settings.stick", getStick());
		config.set("Settings.points", getCountPoints());
		config.set("Settings.effect", getEffect());
		config.set("Settings.lowestpos", getlowestPos());
		saveConfig();
	}

	public static void saveConfig()
	{
		try
		{
			config.save(configFile);
		} catch (IOException e)
		{
		}
	}
}
