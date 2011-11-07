package com.danstoncube.plugin.drzoid.Boutique;

import java.util.Locale;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CommandOperator 
{

	Boutique plugin;
	
	public CommandOperator (Boutique p) {
		plugin = p;
	}
	
	public boolean command(CommandSender sender, Command command, String commandLabel, String[] args) 
	{
		Player p = null;
		
		if (sender instanceof Player)
			p = (Player)sender;
		else
		{
			System.out.println(plugin.logPrefix + Messages.getString("Cmd.ONLYPLAYERS")); //$NON-NLS-1$
			return true;
		}
		
		if ((commandLabel.compareToIgnoreCase("boutique") == 0)||(commandLabel.compareToIgnoreCase("signtrader") == 0))  //$NON-NLS-1$ //$NON-NLS-2$
		{
			if (args.length < 1)
			{
				p.sendMessage(plugin.chatPrefix +Messages.getString("Cmd.HOWTO1")); //$NON-NLS-1$
				p.sendMessage(Messages.getString("Cmd.4")); //$NON-NLS-1$
				p.sendMessage(Messages.getString("Cmd.AVAILABLEARGS")); //$NON-NLS-1$
				//p.sendMessage(ChatColor.AQUA + "-s" + ChatColor.WHITE + " pour modifier les panneaux.");
				p.sendMessage(ChatColor.AQUA + Messages.getString("Cmd.CHESTCMD") + ChatColor.WHITE + Messages.getString("Cmd.BINDCHEST")); //$NON-NLS-1$ //$NON-NLS-2$
				p.sendMessage(ChatColor.AQUA + Messages.getString("Cmd.SHOWCASECMD") + ChatColor.WHITE + Messages.getString("Cmd.BINDSHOWCASE")); //$NON-NLS-1$ //$NON-NLS-2$
				p.sendMessage(ChatColor.AQUA + Messages.getString("Cmd.OWNERCMD") + ChatColor.WHITE + Messages.getString("Cmd.SETOWNER")); //$NON-NLS-1$ //$NON-NLS-2$
				return true;
			}
			else 
			{
				boolean bool = true;
				/*
				if (plugin.playerListener.playerSetSign.containsKey(p))
				{
					plugin.playerListener.playerSetSign.remove(p);
					p.sendMessage(ChatColor.DARK_BLUE + "[" + plugin.displayname + "]" + ChatColor.WHITE + " Tu ne configures plus de panneau.");
					if (args[0].compareToIgnoreCase("-s") == 0)
						return bool;
				}
				*/
				
				if(plugin.playerListener.playerSetChest.containsKey(p))
				{
					plugin.playerListener.playerSetChest.remove(p);
					plugin.playerListener.playerSign.remove(p);
					plugin.playerListener.playerChest.remove(p);
					p.sendMessage(plugin.chatPrefix + Messages.getString("Cmd.STOPBINDCHEST")); //$NON-NLS-1$
					if (args[0].compareToIgnoreCase("-sc") == 0 || args[0].compareToIgnoreCase("-coffre") == 0 || args[0].compareToIgnoreCase("-chest") == 0)
						return bool;
				}
				
				if(plugin.playerListener.playerSetShowcase.containsKey(p))
				{
					plugin.playerListener.playerSetShowcase.remove(p);
					plugin.playerListener.playerSign.remove(p);
					plugin.playerListener.playerShowcase.remove(p);
					p.sendMessage(plugin.chatPrefix + Messages.getString("Cmd.STOPBINDSHOWCASE")); //$NON-NLS-1$
					if (args[0].compareToIgnoreCase("-ssc") == 0 || args[0].compareToIgnoreCase("-vitrine") == 0 || args[0].compareToIgnoreCase("-showcase") == 0)
						return bool;
				}
				
				if(plugin.playerListener.playerDelShowcase.containsKey(p))
				{
					plugin.playerListener.playerDelShowcase.remove(p);
					p.sendMessage(plugin.chatPrefix + Messages.getString("Cmd.STOPDELSHOWCASE")); //$NON-NLS-1$
					if (args[0].compareToIgnoreCase("-scd") == 0 || args[0].compareToIgnoreCase("-vitrinesuppr") == 0 || args[0].compareToIgnoreCase("-delshowcase") == 0)
						return bool;
				}
				 
				
				
				if (plugin.playerListener.setOwner.containsKey(p))
				{
					plugin.playerListener.playerSetSign.remove(p);
					p.sendMessage(plugin.chatPrefix + Messages.getString("Cmd.STOPSETOWNER")); //$NON-NLS-1$
					if (args[0].compareToIgnoreCase("-so") == 0 || args[0].compareToIgnoreCase("-proprio") == 0 || args[0].compareToIgnoreCase("-owner") == 0)
						return bool;
				}
				
				/*
				if (args[0].compareToIgnoreCase("-s") == 0) 
				{
					bool = sCommand(p, args);
				}
				*/
				
				if (args[0].compareToIgnoreCase("-so") == 0 || args[0].compareToIgnoreCase("-proprio") == 0 || args[0].compareToIgnoreCase("-owner") == 0)  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				{
					bool = soCommand(p,args);
				}
				else if (args[0].compareToIgnoreCase("-sc") == 0 || args[0].compareToIgnoreCase("-coffre") == 0 || args[0].compareToIgnoreCase("-chest") == 0)  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				{
					bool = scCommand(p,args);
				}
				else if (args[0].compareToIgnoreCase("-ssc") == 0 || args[0].compareToIgnoreCase("-vitrine") == 0 || args[0].compareToIgnoreCase("-showcase") == 0)  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				{
					bool = sscCommand(p,args);
				}
				else if (args[0].compareToIgnoreCase("-scd") == 0 || args[0].compareToIgnoreCase("-vitrinesuppr") == 0 || args[0].compareToIgnoreCase("-delshowcase") == 0)  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				{
					bool = scdCommand(p,args);
				}
				else
				{
					bool = false;
				}
				
				
				return bool;
			}
		}
		else if (commandLabel.compareToIgnoreCase("getdata") == 0 || commandLabel.compareToIgnoreCase("infoitem") == 0  || commandLabel.compareToIgnoreCase("iteminfo") == 0)  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		{
			return getData(p);
		}
		
		return false;
	}

	
	/* Commande showcasedelete (-scd) */
	private boolean scdCommand(Player p, String[] args)
	{
		if (!PermissionsHandler.canSetPersonalSign(p)) 
		{
			p.sendMessage(plugin.chatPrefix + PermissionsHandler.permissionErr);
			return true;
		}
		else if (args.length == 2) 
		{
			if (args[1].compareToIgnoreCase("-p") == 0) //$NON-NLS-1$
			{
				p.sendMessage(plugin.chatPrefix + Messages.getString("Cmd.LEFTCLICKDELSHOWCASEP")); //$NON-NLS-1$
				plugin.playerListener.playerDelShowcase.put(p, true);
			}
			else
			{
				p.sendMessage(plugin.chatPrefix + Messages.getString("Cmd.INCORRECTARG")); //$NON-NLS-1$
			}
		}
		else if (args.length == 1)
		{
			p.sendMessage(plugin.chatPrefix + Messages.getString("Cmd.LEFTCLICKDELSHOWCASE")); //$NON-NLS-1$
			plugin.playerListener.playerDelShowcase.put(p, false);
		}
		else
		{
			p.sendMessage(plugin.chatPrefix + ChatColor.WHITE + Messages.getString("Cmd.INCORRECTARGS")); //$NON-NLS-1$
		}
		
		return true;
	}
	
	
	
	private boolean sscCommand(Player p, String[] args) 
	{
		if (!PermissionsHandler.canSetPersonalSign(p)) 
		{
			p.sendMessage(plugin.chatPrefix + PermissionsHandler.permissionErr);
			return true;
		}
		else if (args.length == 2) 
		{
			if (args[1].compareToIgnoreCase("-p") == 0) //$NON-NLS-1$
			{
				p.sendMessage(plugin.chatPrefix + Messages.getString("Cmd.LEFTCLICKSIGNSHOWCASEP")); //$NON-NLS-1$
				plugin.playerListener.playerSetShowcase.put(p, true);
			}
			else
			{
				p.sendMessage(plugin.chatPrefix + Messages.getString("Cmd.INCORRECTARG")); //$NON-NLS-1$
			}
		}
		else if (args.length == 1)
		{
			p.sendMessage(plugin.chatPrefix + Messages.getString("Cmd.LEFTCLICKSIGNSHOWCASE")); //$NON-NLS-1$
			plugin.playerListener.playerSetShowcase.put(p, false);
		}
		else
		{
			p.sendMessage(plugin.chatPrefix + ChatColor.WHITE + Messages.getString("Cmd.INCORRECTARGS")); //$NON-NLS-1$
		}
		
		return true;
	}
	private boolean soCommand(Player p, String[] args) 
	{
		if (!PermissionsHandler.canSetOwner(p)) 
		{
			p.sendMessage(plugin.chatPrefix + PermissionsHandler.permissionErr);
			return true;	
		}
		else if (args.length != 2)
		{
			p.sendMessage(plugin.chatPrefix + Messages.getString("Cmd.ONLYONEPLAYER")); //$NON-NLS-1$
		}
		else 
		{
			plugin.playerListener.setOwner.put(p, args[1]);
			p.sendMessage(plugin.chatPrefix + Messages.getString("Cmd.NEXTWILLBE") + args[1] + "."); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return true;
		
	}

	private boolean scCommand(Player p, String[] args) 
	{
		if (!PermissionsHandler.canSetPersonalSign(p)) 
		{
			p.sendMessage(plugin.chatPrefix + PermissionsHandler.permissionErr);
			return true;
		}
		else if (args.length == 2) 
		{
			if (args[1].compareToIgnoreCase("-p") == 0) //$NON-NLS-1$
			{
				p.sendMessage(plugin.chatPrefix + Messages.getString("Cmd.LEFTCLICKSIGNCHESTP")); //$NON-NLS-1$
				plugin.playerListener.playerSetChest.put(p, true);
			}
			else
			{
				p.sendMessage(plugin.chatPrefix + Messages.getString("Cmd.INCORRECTARG")); //$NON-NLS-1$
			}
		}
		else if (args.length == 1)
		{
			p.sendMessage(plugin.chatPrefix + Messages.getString("Cmd.LEFTCLICKSIGNCHEST")); //$NON-NLS-1$
			plugin.playerListener.playerSetChest.put(p, false);
		}
		else
		{
			p.sendMessage(plugin.chatPrefix + Messages.getString("Cmd.INCORRECTARGS")); //$NON-NLS-1$
		}
		
		return true;
	}
	
	/*
	private boolean sCommand(Player p, String[] args) 
	{
		if (!PermissionsHandler.canSetPersonalSign(p)) 
		{
			p.sendMessage(ChatColor.DARK_BLUE + "[" + plugin.displayname + "]" + PermissionsHandler.permissionErr);
			return true;
		}
		else if (args.length == 2) 
		{
			if (args[1].compareToIgnoreCase("p") == 0)
			{
				plugin.playerListener.playerSetSign.put(p, true);
				p.sendMessage(ChatColor.DARK_BLUE + "[" + plugin.displayname + "]" + ChatColor.WHITE + " Tu peux configurer des panneaux. Tu peux le refaire jusqu'a ce que tu retappes cette commande.");
			}
			else
			{
				p.sendMessage(ChatColor.DARK_BLUE + "[" + plugin.displayname + "]" + ChatColor.WHITE + " Arguments incorrects.");
			}
		}
		else 
		{
			plugin.playerListener.playerSetSign.put(p, false);
			p.sendMessage(ChatColor.DARK_BLUE + "[" + plugin.displayname + "]" + ChatColor.WHITE + " Tu peux configurer des panneaux.");
		}
		
		return true;
	}
	*/
	
	private boolean getData(Player p) 
	{
		p.sendMessage(plugin.chatPrefix + Messages.getString("Cmd.YOUHAVE") + p.getItemInHand().getAmount() + Messages.getString("Cmd.QTY") + p.getItemInHand().getType().toString().toLowerCase(Locale.FRENCH) + Messages.getString("Cmd.WITH") + p.getItemInHand().getDurability() + Messages.getString("Cmd.DAMAGEANDID") + p.getItemInHand().getTypeId() + "."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		return true;
	}
	
	
}
