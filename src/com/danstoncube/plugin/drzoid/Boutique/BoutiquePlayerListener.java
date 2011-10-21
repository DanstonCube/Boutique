package com.danstoncube.plugin.drzoid.Boutique;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

public class BoutiquePlayerListener extends PlayerListener {

	private Boutique plugin;
	private String plugName;
	
	//Sign Setting
	public HashMap<Player, Boolean> playerSetSign = new HashMap<Player,Boolean>();
	
	//Chest setting
	public HashMap<Player, Boolean> playerSetChest = new HashMap<Player, Boolean>();
	public HashMap<Player, Chest> playerChest = new HashMap<Player, Chest>();
	public HashMap<Player, Sign> playerSign = new HashMap<Player, Sign>();
	
	//SignOwner Setting
	public HashMap<Player, String> setOwner = new HashMap<Player,String>();
	

	public BoutiquePlayerListener(Boutique signTrader) {
		this.plugin = signTrader;
		signTrader.getClass();
		plugName = "" + ChatColor.BLUE + "[" + signTrader.displayname + "] " + ChatColor.WHITE;
	}	

	public void onPlayerInteract(PlayerInteractEvent e) 
	{
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getState() instanceof Sign) 
		{
			if (plugin.signoperator.isSign(e.getClickedBlock()))
			{
				e.setCancelled(true);

				this.rightClickSign(e.getPlayer(), (Sign)e.getClickedBlock().getState());
			}
		}	
		if (e.getAction() == Action.LEFT_CLICK_BLOCK)
		{
			if (e.getClickedBlock().getState() instanceof Sign){
				leftClickSign(e.getPlayer(), e.getClickedBlock());
			}
			
			if (e.getClickedBlock().getState() instanceof Chest)
				leftClickChest(e.getPlayer(), e.getClickedBlock());
		}
	}

	private void leftClickChest(Player p, Block b) 
	{
		if (playerSetChest.containsKey(p)){
			if (playerChest.get(p) == (Chest)b.getState())
				p.sendMessage(plugName + "Tu as déjà choisi ce coffre.");
			else 
			{				
				playerChest.put(p, (Chest)b.getState());
				p.sendMessage(plugName + "Coffre mémorisé.");
				if (playerChest.containsKey(p) && playerSign.containsKey(p)){
					plugin.sm.setChest(playerSign.get(p), playerChest.get(p), p);
					if (playerSetChest.get(p) == false)
						playerSetChest.remove(p);
					playerChest.remove(p);
					playerSign.remove(p);
					plugin.fileIO.saveGlobalSigns();
				}
			}
		}
		
	}

	
	private void rightClickSign(Player p, Sign s)
	{	
		plugin.signmanager.useSign(s, p);
	}
	
	
	private void leftClickSign(Player p, Block b) 
	{
		if (playerSetSign.containsKey(p))
		{
			plugin.sm.setSign((Sign) b.getState(), p);
			if (!playerSetSign.get(p))
				playerSetSign.remove(p);
		}
		else if (setOwner.containsKey(p)) 
		{
			plugin.sm.setOwner(p, setOwner.get(p), (Sign) b.getState());
			setOwner.remove(p);
		}
		else if (playerSetChest.containsKey(p))
		{
			if (playerSign.get(p) == (Sign)b.getState())
				p.sendMessage(plugName + "Tu as déjà choisi ce panneau.");
			else 
			{
				if (!SignOperator.isSignOwner((Sign)b.getState(), p.getName()))
				{
					p.sendMessage(plugName + "Ce n'est pas ton panneau.");
					return;
				}
				playerSign.put(p, (Sign)b.getState());
				p.sendMessage(plugName + "Panneau mémorisé.");
				if (playerChest.containsKey(p) && playerSign.containsKey(p)) 
				{
					plugin.sm.setChest(playerSign.get(p), playerChest.get(p), p);
					if (playerSetChest.get(p) == false)
						playerSetChest.remove(p);
					playerChest.remove(p);
					playerSign.remove(p);
					plugin.fileIO.saveGlobalSigns();
				}
			}
		}
		else
		{
			Sign s = (Sign) b;
			
			if(plugin.signmanager.isBoutiqueSign(s))			
				plugin.signmanager.displaySignInfo(s, p);
			
		}
		
	}

}
