import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


public class PlayerSex extends JavaPlugin implements Listener {
	public static double LOWER_BOUND_SQUARED = 0.2*0.2;
	public static double UPPER_BOUND_SQUARED = 0.7*0.7;
	public static double DEGREES_RANGE = 45.0;
	public static double Y_RANGE = 0.5;
	HashMap<Player, Double> pleasure;
	public double getDistanceSquared(Location a, Location b) {
		return ((a.getX()-b.getX())*(a.getX()-b.getX())+(a.getZ()-b.getZ())*(a.getZ()-b.getZ()));
	}
	public void onPlayerSneak(PlayerToggleSneakEvent event) {
		if (event.isSneaking()) {
			Player a = event.getPlayer();
			double cDist = 100.0;
			Player cP = null;
			for (Player p: event.getPlayer().getWorld().getPlayers()) {
				double dist = getDistanceSquared(a.getLocation(), p.getLocation());
				if (dist > LOWER_BOUND_SQUARED && dist < UPPER_BOUND_SQUARED &&
						Math.abs(a.getLocation().getYaw()-p.getLocation().getYaw())<DEGREES_RANGE &&
						(a.getLocation().getY()-p.getLocation().getY()) <= Y_RANGE) {
					if (dist < cDist) {
						cDist = dist;
						cP = p;
					}
				}	
			}
			if (cP != null) {
				pleasure.put(a, pleasure.get(a)+1.5);
				pleasure.put(cP, pleasure.get(cP)+1.5);
			}
		}/* else {
			Player a = event.getPlayer();
			double cDist = 100.0;
			Player cP = null;
			for (Player p: event.getPlayer().getWorld().getPlayers()) {
				double dist = getDistanceSquared(a.getLocation(), p.getLocation());
				if (dist > LOWER_BOUND_SQUARED && dist < UPPER_BOUND_SQUARED &&
						Math.abs(a.getLocation().getYaw()-p.getLocation().getYaw())<DEGREES_RANGE &&
						(a.getLocation().getY()-p.getLocation().getY()) <= Y_RANGE) {
					if (dist < cDist) {
						cDist = dist;
						cP = p;
					}
				}	
			}
			if (cP != null) {
				pleasure.put(a, pleasure.get(a)+1.5);
				pleasure.put(cP, pleasure.get(cP)+1.5);
			}
		}*/
	}
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		
	}
}

class PlayerSexRunnable extends BukkitRunnable {
	PlayerSex plugin;
	public PlayerSexRunnable(PlayerSex instance) {
		plugin = instance;
	}
	@Override
	public void run() {
		for (Player p: plugin.pleasure.keySet()) {
			double pl = plugin.pleasure.get(p);
			pl = Math.max(pl-0.05, 0);
			plugin.pleasure.put(p, pl);
		}
	}
	
}
