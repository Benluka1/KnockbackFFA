package eu.mrkavatch.knockbackffa;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
	public static Location spawn;
	public static boolean damage = false;
	public static boolean effekt = false;
	public static boolean countkills = false;
	public static int lowpos = 0;
	public static ItemStack startItem = Config.getStick();
	public static HashMap<Player, Player> lastDamage = new HashMap<Player, Player>();

	@Override
	public void onEnable()
	{
		this.getServer().getPluginManager().registerEvents(this, this);
		Config.saveConfig();
		Config.setDefaults();
		Config.saveConfig();
		try
		{
			spawn = Config.getSpawn();
		} catch (Exception e)
		{
			Bukkit.broadcastMessage("Es wurde kein Spawn gefunden bitte melde dich bei einem Admin oder erstelle einen mit /setspawn");
		}

		damage = Config.getDamage();
		effekt = Config.getEffect();
		lowpos = Config.getlowestPos();
		countkills = Config.getCountPoints();
		if (startItem == null)
		{
			startItem = Methoden.standartItem();
		}
	}

	@Override
	public void onDisable()
	{
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (sender instanceof Player)
		{

			Player p = (Player) sender;

			if (command.getName().equalsIgnoreCase("setspawn"))
			{
				if (!p.hasPermission("ffa.commands"))
				{
					return false;
				}
				Config.setSpawn(p.getLocation());
				p.sendMessage("Du hast den Spawn umgesetzt!");
			}
			if (command.getName().equalsIgnoreCase("setstick"))
			{
				if (!p.hasPermission("ffa.commands"))
				{
					return false;
				}
				if (p.getItemInHand() != null || p.getItemInHand() != new ItemStack(Material.AIR))
				{
					Config.setStick(p.getItemInHand());
					p.sendMessage("Du hast das Item in deiner Hand als neuen Knockbackstick festgesetzt!");
				} else
				{
					p.sendMessage("Bitte nehme ein Item in die Hand");
				}
			}
			if (command.getName().equalsIgnoreCase("seteffect"))
			{
				if (!p.hasPermission("ffa.commands"))
				{
					return false;
				}
				if (effekt)
				{
					effekt = false;
					Config.setEffect(false);
					p.sendMessage("Der Effekt wurde Deaktiviert");
				} else
				{
					effekt = true;
					Config.setEffect(true);
					p.sendMessage("Der Effekt wurde Aktiviert");
				}
			}

			if (command.getName().equalsIgnoreCase("setdamage"))
			{
				if (!p.hasPermission("ffa.commands"))
				{
					return false;
				}
				if (damage)
				{
					damage = false;
					Config.setDamage(false);
					p.sendMessage("Der Schaden wurde Deaktiviert");
				} else
				{
					damage = true;
					Config.setDamage(true);
					p.sendMessage("Der Schaden wurde Aktiviert");
				}
			}

			if (command.getName().equalsIgnoreCase("lowpos"))
			{
				if (!p.hasPermission("ffa.commands"))
				{
					return false;
				}
				if (args.length == 1)
				{
					Config.setlowestPos(Integer.parseInt(args[0]));
					p.sendMessage("Die Niedrigste Position wurde auf " + args[0] + " gesetzt");
				}
			}
			if (command.getName().equalsIgnoreCase("stats"))
			{
				if (!p.hasPermission("ffa.commands"))
				{
					return false;
				}
				if (args.length == 1)
				{
					p.sendMessage("Der Spieler " + args[0] + " hat " + Config.getKills(Bukkit.getPlayer(args[0])) + " Kills");
				} else
				{
					p.sendMessage("Bitte gebe einen Namen an.");
				}
			}
			if (command.getName().equalsIgnoreCase("countkills"))
			{
				if (!p.hasPermission("ffa.commands"))
				{
					return false;
				}
				if (countkills)
				{
					countkills = false;
					Config.setCountPoints(false);
					p.sendMessage("Kills werden nicht mehr mitgezählt");
				} else
				{
					countkills = true;
					Config.setCountPoints(true);
					p.sendMessage("Kills werden nun mitgezählt");
				}
			}

		}
		return false;
	}

	/********************************************************** EVENTS ***********************************************************************/

	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		event.getPlayer().getInventory().clear();
		try
		{
			event.getPlayer().teleport(Main.spawn);
		} catch (Exception e)
		{
			// Spawn könnt null sein.
		}
		Methoden.giveItems(event.getPlayer());
		Config.config.set("PlayerKills." + event.getPlayer().getName(), Config.getKills(event.getPlayer()));
		Config.saveConfig();
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event)
	{
		if (!event.isCancelled())
		{
			if (event.getEntity() instanceof Player && event.getDamager() instanceof Player)
			{

				Player player1 = (Player) event.getEntity();
				Player player2 = (Player) event.getDamager();
				if (Main.lastDamage.containsKey(player1))
				{
					Main.lastDamage.remove(player1);
				}
				Main.lastDamage.put(player1, player2);
			}
		} else {
		}
		
		try
		{
			if (!Main.damage)
			{
				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

					@Override
					public void run()
					{
						Player p = (Player) event.getEntity();
						p.setHealth(20);
					}
				}, 1);

			}
		} catch (Exception e)
		{
		}

	}

	@EventHandler
	public void onDamage(EntityDamageEvent event)
	{
		if (event.getCause() == DamageCause.FALL)
		{
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event)
	{
		if (event.getPlayer().getLocation().getBlockY() <= Main.lowpos)
		{
			Methoden.playEffect(event.getPlayer().getLocation());
			if (countkills)
			{
				try
				{
					Config.setAddKills(Methoden.getLatestDamager(event.getPlayer()), 1);
				} catch (Exception e)
				{

				}
			}
			event.getPlayer().teleport(Main.spawn);
		}
	}

	@EventHandler
	public void onFoodLevel(FoodLevelChangeEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event)
	{
		if (!event.getPlayer().hasPermission("ffa.break"))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event)
	{
		if (!event.getPlayer().hasPermission("ffa.place"))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onInt(PlayerInteractEvent event)
	{
		if (!event.getPlayer().hasPermission("ffa.interact"))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onRespanw(PlayerRespawnEvent event)
	{
		if (Main.spawn.getWorld() != null)
		{
			event.getPlayer().getInventory().clear();
			Methoden.giveItems(event.getPlayer());
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

				@Override
				public void run()
				{
					event.getPlayer().teleport(Main.spawn);

				}
			}, 2);
		}
	}

}
