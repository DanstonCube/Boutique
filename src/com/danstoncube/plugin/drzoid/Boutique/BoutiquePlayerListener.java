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

public class BoutiquePlayerListener extends PlayerListener 
{

	private Boutique plugin;
	private String plugName;
	
	//Sign Setting
	public HashMap<Player, Boolean> playerSetSign = new HashMap<Player,Boolean>();
	public HashMap<Player, Sign> playerSign = new HashMap<Player, Sign>();
	
	//Chest setting
	public HashMap<Player, Boolean> playerSetChest = new HashMap<Player, Boolean>();
	public HashMap<Player, Chest> playerChest = new HashMap<Player, Chest>();
	
	
	//SignOwner Setting
	public HashMap<Player, String> setOwner = new HashMap<Player,String>();
	

	public BoutiquePlayerListener(Boutique boutique) 
	{
		this.plugin = boutique;
		plugName = "" + ChatColor.BLUE + "[" + boutique.displayname + "] " + ChatColor.WHITE;
	}
	

	public void onPlayerInteract(PlayerInteractEvent e) 
	{
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getState() instanceof Sign) 
		{
			if (e.getClickedBlock().getState() instanceof Sign)
			{
				e.getPlayer().sendMessage(plugName + "Right click sign");
				
				if (plugin.signmanager.isBoutiqueSign(e.getClickedBlock()))
				{
					e.getPlayer().sendMessage(plugName + "Ok pour ce panneau");
					
					e.setCancelled(true);
					this.rightClickSign(e.getPlayer(), e.getClickedBlock());
				}
				else
				{
					e.getPlayer().sendMessage(plugName + "je connais pas ce panneau !");
				}
			}
			
		}	
		
		if (e.getAction() == Action.LEFT_CLICK_BLOCK)
		{
			
			if (e.getClickedBlock().getState() instanceof Sign)
			{
				leftClickSign(e.getPlayer(), e.getClickedBlock());
			}
			
			if (e.getClickedBlock().getState() instanceof Chest)
			{
				leftClickChest(e.getPlayer(), e.getClickedBlock());
			}
				
		}
	}

	private void leftClickChest(Player p, Block b) 
	{
		if (playerSetChest.containsKey(p))
		{
			if (playerChest.get(p) == (Chest)b.getState())
			{
				p.sendMessage(plugName + "Tu as déjà choisi ce coffre.");
			}
			else 
			{				
				playerChest.put(p, (Chest)b.getState());
				
				p.sendMessage(plugName + "Coffre mémorisé.");
				
				if (playerChest.containsKey(p) && playerSign.containsKey(p))
				{
					plugin.signmanager.setChest(playerSign.get(p), playerChest.get(p), p);
			
					if (playerSetChest.get(p) == false)
						playerSetChest.remove(p);
					
					playerChest.remove(p);
					playerSign.remove(p);
					
					plugin.signmanager.saveSignData();
				}
			}
		}
		
	}

	
	
	private void rightClickSign(Player p, Block b)
	{	
		plugin.signmanager.useSign(b, p);
	}
	
	
	private void leftClickSign(Player p, Block b) 
	{
		if (playerSetSign.containsKey(p))
		{
			//plugin.signmanager.setSign(b, p);
			
			if (!playerSetSign.get(p))
				playerSetSign.remove(p);
		}
		else if (setOwner.containsKey(p)) 
		{
			plugin.signmanager.setOwner((Sign) b.getState(), p, setOwner.get(p));
			setOwner.remove(p);
		}
		else if (playerSetChest.containsKey(p))
		{
			if (playerSign.get(p) == (Sign)b.getState())
			{
				p.sendMessage(plugName + "Tu as déjà choisi ce panneau.");
			}
			else 
			{
				if (!plugin.signmanager.isSignOwner((Sign)b.getState(), p))
				{
					p.sendMessage(plugName + "Ce n'est pas ton panneau.");
					return;
				}
				
				playerSign.put(p, (Sign)b.getState());
				
				p.sendMessage(plugName + "Panneau mémorisé.");
				
				if (playerChest.containsKey(p) && playerSign.containsKey(p)) 
				{
					plugin.signmanager.setChest(playerSign.get(p), playerChest.get(p), p);
					
					if (playerSetChest.get(p) == false)
					{
						playerSetChest.remove(p);
					}
					
					playerChest.remove(p);
					playerSign.remove(p);
					
					plugin.signmanager.saveGlobalSigns();
				}
			}
		}
		else
		{
			//Sign s = (Sign) b.getState();
			
			p.sendMessage(plugName + "Click panneau");
			
			if(plugin.signmanager.haveLocation(b.getLocation()))
			{
				plugin.signmanager.displaySignInfo(b, p);
			}
			else
			{
				p.sendMessage(plugName + "Connais pas ce panneau");
			}
				
			
		}
		
	}

}
