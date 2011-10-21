package com.danstoncube.plugin.drzoid.Boutique;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

import com.danstoncube.plugin.drzoid.Boutique.SignTypes.BoutiqueSign;
import com.danstoncube.plugin.drzoid.Boutique.SignTypes.BoutiqueSignChest;
import com.danstoncube.plugin.drzoid.Boutique.SignTypes.BoutiqueSignServer;
import com.danstoncube.plugin.drzoid.Boutique.SignTypes.BoutiqueSignWebAuction;

public class BoutiqueBlockListener extends BlockListener
{
	private Boutique plugin;
	
	public BoutiqueBlockListener(Boutique signTrader)
	{
		this.plugin = signTrader;
		signTrader.getClass();
	}

	public void onSignChange(SignChangeEvent e) 
	{
		Location location = e.getBlock().getLocation();
		
		if ((!e.isCancelled()) && (plugin.signmanager.haveLocation(location)))
			plugin.signmanager.remove(location);
		
		
		//this.plugin.sm.setSign(e.getLines(), e.getPlayer(), e.getBlock());
		
		
		String lines[] = e.getLines();
		
		BoutiqueSign bs = null;
		
		if(lines[0].compareToIgnoreCase(BoutiqueSignServer.getTypeStr())==0)
		{
			bs = new BoutiqueSignServer((Sign)e.getBlock(), e.getPlayer(), bs);
		}
		else if(lines[0].compareToIgnoreCase(BoutiqueSignChest.getTypeStr())==0)
		{
			bs = new BoutiqueSignChest((Sign)e.getBlock(), e.getPlayer(), bs);
		}
		else if(lines[0].compareToIgnoreCase(BoutiqueSignWebAuction.getTypeStr())==0)
		{
			bs = new BoutiqueSignWebAuction((Sign)e.getBlock(), e.getPlayer(), bs);
		}
		else
		{
			return;
		}
			
			
		 //BoutiqueSign.Create((Sign) e.getBlock(), e.getPlayer(), e.getLines());
		
		
		
		
		
		
	}

	public void onBlockPlace(BlockPlaceEvent e) 
	{
		Location location = e.getBlock().getLocation();
		
		//on enleve de la hashmap, on l'y remettra au moment ou un panneau sera edité
		if ((!e.isCancelled()) && (plugin.signmanager.haveLocation(location)))
			plugin.signmanager.remove(location);
	}

	public void onBlockBreak(BlockBreakEvent e) 
	{
		Location location = e.getBlock().getLocation();
		
		//on enleve de la hashmap s'il y est
		if ((!e.isCancelled()) && (plugin.signmanager.haveLocation(location))) 
			plugin.signmanager.remove(location);
	}
}