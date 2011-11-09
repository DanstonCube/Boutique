package com.danstoncube.Boutique;

import java.util.HashMap;

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

	
	//Sign Setting
	public HashMap<Player, Boolean> playerSetSign = new HashMap<Player,Boolean>();
	public HashMap<Player, Sign> playerSign = new HashMap<Player, Sign>();
	
	//Chest setting
	public HashMap<Player, Boolean> playerSetChest = new HashMap<Player, Boolean>();
	public HashMap<Player, Chest> playerChest = new HashMap<Player, Chest>();
	
	//Showcase
	public HashMap<Player, Boolean> playerSetShowcase = new HashMap<Player, Boolean>();
	public HashMap<Player, String> playerShowcase = new HashMap<Player, String>();
	
	
	//SignOwner Setting
	public HashMap<Player, String> setOwner = new HashMap<Player,String>();


	public HashMap<Player, Boolean> playerDelShowcase = new HashMap<Player, Boolean>();

	

	public BoutiquePlayerListener(Boutique boutique) 
	{
		this.plugin = boutique;
	}
	

	public void onPlayerInteract(PlayerInteractEvent e) 
	{
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getState() instanceof Sign) 
		{
			if (e.getClickedBlock().getState() instanceof Sign)
			{
				//e.getPlayer().sendMessage(plugName + "Right click sign");
				
				if (plugin.signmanager.isBoutiqueSign(e.getClickedBlock()))
				{
					//e.getPlayer().sendMessage(plugName + "Ok pour ce panneau");
					
					e.setCancelled(true);
					this.rightClickSign(e.getPlayer(), e.getClickedBlock());
				}
				else
				{
					//e.getPlayer().sendMessage(plugName + "je connais pas ce panneau !");
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
			
			if (e.getClickedBlock().getTypeId() == 44)
			{
				leftClickShowcase(e.getPlayer(), e.getClickedBlock());
			}
				
		}
	}

	private void leftClickShowcase(Player player, Block clickedBlock)
	{
		String strLocShowcase = BoutiqueSign.getLocationString(clickedBlock.getLocation());
		
		
		if (playerDelShowcase.containsKey(player))
		{
			
			if(!ShowCaseHandler.isShowCaseOwner(clickedBlock, player))
			{
				player.sendMessage(plugin.chatPrefix + Messages.getString("BoutiquePlayerListener.NOTYOURSHOWCASE"));
				return;
			}
			
			
			ShowCaseHandler.remShowcase(strLocShowcase, player);
			
			player.sendMessage(plugin.chatPrefix + Messages.getString("BoutiquePlayerListener.SHOWCASEDELETED"));
			
			if (playerDelShowcase.get(player) == false)
				playerDelShowcase.remove(player);
						
			return;
		}	
		
		
		if (playerSetShowcase.containsKey(player))
		{
			if (playerShowcase.get(player) == strLocShowcase)
			{
				player.sendMessage(plugin.chatPrefix + Messages.getString("BoutiquePlayerListener.SHOWCASEALREADYCHOOSEN")); //$NON-NLS-1$
				return;
			}
			else if(!ShowCaseHandler.isShowCaseOwner(clickedBlock, player))
			{
				player.sendMessage(plugin.chatPrefix + Messages.getString("BoutiquePlayerListener.NOTYOURSHOWCASE")); //$NON-NLS-1$
				return;
			}
						
			playerShowcase.put(player, strLocShowcase);
			
			player.sendMessage(plugin.chatPrefix + Messages.getString("BoutiquePlayerListener.SHOWCASESAVED")); //$NON-NLS-1$
			
			if (playerShowcase.containsKey(player) && playerSign.containsKey(player))
			{
				ShowCaseHandler.setShowcase(playerSign.get(player), playerShowcase.get(player), player);
		
				if (playerSetShowcase.get(player) == false)
					playerSetShowcase.remove(player);
				
				playerShowcase.remove(player);
				playerSign.remove(player);
				
				//plugin.signmanager.saveSignData();
			}
			
		}
		
		
	}
	


	private void leftClickChest(Player p, Block b) 
	{
		if (playerSetChest.containsKey(p))
		{
			if (playerChest.get(p) == (Chest)b.getState())
			{
				p.sendMessage(plugin.chatPrefix + Messages.getString("BoutiquePlayerListener.CHESTALREADYCHOOSEN")); //$NON-NLS-1$
				return;
			}
			else if(!plugin.signmanager.isChestOwner((Chest)b.getState(), p))
			{
					p.sendMessage(plugin.chatPrefix + Messages.getString("BoutiquePlayerListener.NOTYOURCHEST")); //$NON-NLS-1$
					return;
			}
			
			
							
			playerChest.put(p, (Chest)b.getState());
			
			p.sendMessage(plugin.chatPrefix + Messages.getString("BoutiquePlayerListener.CHESTSAVED")); //$NON-NLS-1$
			
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
				p.sendMessage(plugin.chatPrefix + Messages.getString("BoutiquePlayerListener.SIGNALREADYCHOOSEN")); //$NON-NLS-1$
			}
			else 
			{
				if (!plugin.signmanager.isSignOwner((Sign)b.getState(), p))
				{
					p.sendMessage(plugin.chatPrefix + Messages.getString("BoutiquePlayerListener.NOTYOURSIGN")); //$NON-NLS-1$
					return;
				}
				
				playerSign.put(p, (Sign)b.getState());
				
				p.sendMessage(plugin.chatPrefix + Messages.getString("BoutiquePlayerListener.SIGNSAVED")); //$NON-NLS-1$
				
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
		else if(playerSetShowcase.containsKey(p))
		{
			
			
			if (!plugin.signmanager.isSignOwner((Sign)b.getState(), p))
			{
				p.sendMessage(plugin.chatPrefix + Messages.getString("BoutiquePlayerListener.NOTYOUSIGN")); //$NON-NLS-1$
				return;
			}
			
			playerSign.put(p, (Sign)b.getState());
			
			p.sendMessage(plugin.chatPrefix + Messages.getString("BoutiquePlayerListener.SIGNSAVED")); //$NON-NLS-1$
			

			if (playerShowcase.containsKey(p) && playerSign.containsKey(p)) 
			{
				//setShowCase();
				
				if (playerSetShowcase.get(p) == false)
				{
					playerSetShowcase.remove(p);
				}
				
				playerShowcase.remove(p);
				playerSign.remove(p);
			}
		}
		else
		{
			//Sign s = (Sign) b.getState();
			
			//TODO virer debug
			//p.sendMessage(plugName + "Click panneau");
			
			if(plugin.signmanager.haveLocation(b.getLocation()))
			{
				plugin.signmanager.displaySignInfo(b, p);
			}
			else
			{
				//TODO virer debug
				//p.sendMessage(plugName + "Connais pas ce panneau");
			}
				
			
		}
		
	}

}
