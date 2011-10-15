package com.danstoncube.plugin.drzoid.Boutique;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

public class BoutiqueBlockListener extends BlockListener
{
  private Boutique plugin;
  @SuppressWarnings("unused")
  private String plugName;

  public BoutiqueBlockListener(Boutique signTrader)
  {
    this.plugin = signTrader;
    signTrader.getClass(); this.plugName = (ChatColor.BLUE + "[" + "Boutique" + "] " + ChatColor.WHITE);
  }

  public void onSignChange(SignChangeEvent e) {
    Location location = e.getBlock().getLocation();
    String loc = location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ() + ":" + location.getWorld().toString();
    if ((!e.isCancelled()) && (Boutique.signLocs.containsKey(loc)))
    {
    	Boutique.signLocs.remove(loc);
    	Boutique.signLine1.remove(loc);
		Boutique.signLine2.remove(loc);
		Boutique.signLine3.remove(loc);
    }
    this.plugin.sm.setSign(e.getLines(), e.getPlayer(), e.getBlock());
  }

  public void onBlockPlace(BlockPlaceEvent e) {
    Location location = e.getBlock().getLocation();
    String loc = location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ() + ":" + location.getWorld().toString();
    if ((!e.isCancelled()) && (Boutique.signLocs.containsKey(loc)))
    {
    	Boutique.signLocs.remove(loc);
    	Boutique.signLine1.remove(loc);
		Boutique.signLine2.remove(loc);
		Boutique.signLine3.remove(loc);
    }
  }

  public void onBlockBreak(BlockBreakEvent e) 
  {
    Location location = e.getBlock().getLocation();
    String loc = location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ() + ":" + location.getWorld().toString();
    
    if ((!e.isCancelled()) && (Boutique.signLocs.containsKey(loc))) 
    {
    	Boutique.signLocs.remove(loc);
    	Boutique.signLine1.remove(loc);
		Boutique.signLine2.remove(loc);
		Boutique.signLine3.remove(loc);
    }
    
    Sign[] signs = SignOperator.getAttachedSigns(e.getBlock());
    for (Sign sign : signs)
      if (sign != null) {
        location = sign.getBlock().getLocation();
        loc = location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ() + ":" + location.getWorld().toString();
        if ((!e.isCancelled()) && (Boutique.signLocs.containsKey(loc)))
        {
        	Boutique.signLocs.remove(loc);
        	Boutique.signLine1.remove(loc);
    		Boutique.signLine2.remove(loc);
    		Boutique.signLine3.remove(loc);
        }
      }
  }
}