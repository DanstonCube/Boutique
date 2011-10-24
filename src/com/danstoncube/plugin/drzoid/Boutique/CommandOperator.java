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
			System.out.println("[" + plugin.displayname + "] Seuls les joueurs ont accès aux commandes.");
			return true;
		}
		
		if ((commandLabel.compareToIgnoreCase("boutique") == 0)||(commandLabel.compareToIgnoreCase("signtrader") == 0)) 
		{
			if (args.length < 1)
			{
				p.sendMessage(ChatColor.DARK_BLUE + "[" + plugin.displayname + "]" + ChatColor.WHITE + " Pour utiliser cette commande utilisez la syntaxe suivante:");
				p.sendMessage("'/boutique argument p' le 'p' est facultatif.");
				p.sendMessage("Les arguments disponibles sont: ");
				//p.sendMessage(ChatColor.AQUA + "-s" + ChatColor.WHITE + " pour modifier les panneaux.");
				p.sendMessage(ChatColor.AQUA + "-sc" + ChatColor.WHITE + " pour relier les coffres aux panneaux.");
				p.sendMessage(ChatColor.AQUA + "-so" + ChatColor.WHITE + " pour redéfinir le proprio d'un panneau. Attention aux majuscules");
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
					p.sendMessage(ChatColor.DARK_BLUE + "[" + plugin.displayname + "]" + ChatColor.WHITE + " Tu ne relies plus de panneau à des coffres.");
					if (args[0].compareToIgnoreCase("-sc") == 0)
						return bool;
				}
				
				if (plugin.playerListener.setOwner.containsKey(p))
				{
					plugin.playerListener.playerSetSign.remove(p);
					p.sendMessage(ChatColor.DARK_BLUE + "[" + plugin.displayname + "]" + ChatColor.WHITE + " Tu ne redéfinis plus les propriétaires des panneaux.");
					if (args[0].compareToIgnoreCase("-so") == 0)
						return bool;
				}
				
				/*
				if (args[0].compareToIgnoreCase("-s") == 0) 
				{
					bool = sCommand(p, args);
				}
				*/
				
				if (args[0].compareToIgnoreCase("-sc") == 0) 
				{
					bool = scCommand(p,args);
				}
				else if (args[0].compareToIgnoreCase("-so") == 0)
				{
					bool = soCommand(p,args);
				}
				else
				{
					bool = false;
				}
				
				
				return bool;
			}
		}
		else if (commandLabel.compareToIgnoreCase("getdata") == 0 || commandLabel.compareToIgnoreCase("infoitem") == 0) 
		{
			return getData(p);
		}
		
		return false;
	}

	private boolean soCommand(Player p, String[] args) 
	{
		if (!PermissionsHandler.canSetOwner(p)) 
		{
			p.sendMessage(ChatColor.DARK_BLUE + "[" + plugin.displayname + "]" + PermissionsHandler.permissionErr);
			return true;	
		}
		else if (args.length != 2)
		{
			p.sendMessage(ChatColor.DARK_BLUE + "[" + plugin.displayname + "]" + ChatColor.WHITE + " Tu ne peux mettre que le nom que d'un seul joueur.");
		}
		else 
		{
			plugin.playerListener.setOwner.put(p, args[1]);
			p.sendMessage(ChatColor.DARK_BLUE + "[" + plugin.displayname + "]" + ChatColor.WHITE + " Le prochain panneau que tu poses sera le panneau de: " + args[1] + ".");
		}
		return true;
		
	}

	private boolean scCommand(Player p, String[] args) 
	{
		if (!PermissionsHandler.canSetPersonalSign(p)) 
		{
			p.sendMessage(ChatColor.DARK_BLUE + "[" + plugin.displayname + "]" + PermissionsHandler.permissionErr);
			return true;
		}
		else if (args.length == 2) 
		{
			if (args[1].compareToIgnoreCase("-p") == 0)
			{
				p.sendMessage(ChatColor.DARK_BLUE + "[" + plugin.displayname + "]" + ChatColor.WHITE + " Clic-gauche sur un panneau puis sur le coffre à relier. Tu peux faire çà tant que tu ne retappes pas cette commande.");
				plugin.playerListener.playerSetChest.put(p, true);
			}
			else
			{
				p.sendMessage(ChatColor.DARK_BLUE + "[" + plugin.displayname + "]" + ChatColor.WHITE + " Argument incorrect.");
			}
		}
		else if (args.length == 1)
		{
			p.sendMessage(ChatColor.DARK_BLUE + "[" + plugin.displayname + "]" + ChatColor.WHITE + " Clic-gauche sur un panneau puis sur le coffre à relier.");
			plugin.playerListener.playerSetChest.put(p, false);
		}
		else
		{
			p.sendMessage(ChatColor.DARK_BLUE + "[" + plugin.displayname + "]" + ChatColor.WHITE + " Arguments incorrects.");
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
		p.sendMessage("Tu as " + p.getItemInHand().getAmount() + "x " + p.getItemInHand().getType().toString().toLowerCase(Locale.FRENCH) + " avec " + p.getItemInHand().getDurability() + " de durabilité, ayant pour ID: " + p.getItemInHand().getTypeId() + ".");
		return true;
	}
	
	
}
