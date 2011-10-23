package com.danstoncube.plugin.drzoid.Boutique;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.danstoncube.plugin.drzoid.Boutique.SignTypes.BoutiqueSign;
import com.danstoncube.plugin.drzoid.Boutique.SignTypes.BoutiqueSignChest;
import com.danstoncube.plugin.drzoid.Boutique.SignTypes.BoutiqueSignDummy;
import com.danstoncube.plugin.drzoid.Boutique.SignTypes.BoutiqueSignServer;
import com.danstoncube.plugin.drzoid.Boutique.SignTypes.BoutiqueSignWebAuction;

@SuppressWarnings("unused")
public class BoutiqueSignManager
{
/*
 * 
 * add
 * remove
 * update
 * 
 * isboutiquesign(sign)
 * getboutiquesign(sign)
 * getboutiquesign(key)
 * 
 * 
 * LoadSigns();
 * SaveSigns();	
*/
	
/*	
 * isbuysign
 * isfreesign
 * isdonationsign
 * issellsign
 * istradesign
 * 
 * 
 * 
 * 
 * 
 */
	
	
	private HashMap<Location,BoutiqueSign> _signs = new HashMap<Location,BoutiqueSign>();
	
	//TODO: changer ca
	private String plugName = "TOTO";
	
	private Boutique plugin;
	
	
	public BoutiqueSignManager(Boutique boutique)
	{
		// TODO Auto-generated constructor stub
		this.plugin = boutique;
	}


	public Boolean isBoutiqueSign(Sign s)
	{
		Location signLoc = s.getBlock().getLocation();		
		//todo: mettre une fonction keygen ?
		//String signKey = signLoc.getBlockX() +  ":" + signLoc.getBlockY() + ":" + signLoc.getBlockZ() + ":" + signLoc.getWorld();		
		return _signs.containsKey(signLoc);			
	}
	
	public Boolean haveLocation(Location l)
	{
		return _signs.containsKey(l);			
	}
	
	
	public Boolean isEnabled(Sign s)
	{
		BoutiqueSign bs = _signs.get(s.getBlock().getLocation());
		
		if(bs == null) 
			return false;
		
		return bs.isEnabled();		
	}
	
	public BoutiqueSign getBoutiqueSign(Sign s)
	{
		BoutiqueSign bs = _signs.get(s.getBlock().getLocation());
		return bs;		
	}
	

	public void saveSignData()	
	{
		
	}
	

	public void useSign(Sign s, Player p) 
	{
		BoutiqueSign bs = getBoutiqueSign(s);
		
		if (!bs.isEnabled())
		{
			//TODO: formatter le chat ailleurs ? 
			p.sendMessage(ChatColor.YELLOW + "Le panneau n'est pas actif.");
			p.sendMessage(ChatColor.YELLOW + "Activez le en reposant le panneau.");
			return;
		}		
		
		// See if player has permission to use signs.
		if(!PermissionsHandler.canUseSign(p))
		{
			p.sendMessage(plugName + PermissionsHandler.permissionErr);
			return;
		}
		
		// Vérifie les items / lignes
		if(!bs.checkLines(p))
			return;
		
		// Lance les actions (echange / achat / vente / etc)
		if(!bs.doAction(p))
			return;
		
		// Actualise les détails du panneau
		bs.Render();
	}
	
	
	
	


	private void signSetter(String[] lines, Player p, Block s) 
	{		
		//TODO: verifier instanceof sign sur le block
		
		
		BoutiqueSign bs = plugin.signmanager.getBoutiqueSign((Sign)s);
		
		// Vérifie les items / lignes
		if(!bs.checkLines(p))
			return;
		
		if(bs.getSignTypeString().compareToIgnoreCase(BoutiqueSignServer.getTypeStr()) == 0)
		{
			if (!PermissionsHandler.canSetGlobalSign(p))
			{
				p.sendMessage(PermissionsHandler.permissionErr);
				return;
			}
			p.sendMessage(plugName + "Panneau serveur ajouté à la liste :)");
			
			//TODO : signmanager.addsign
			//TODO : prerender
			
			bs.setLine4(ChatColor.GREEN + "[Actif]");
			
			plugin.signmanager.saveGlobalSigns();
		}
		else if(bs.getSignTypeString().compareToIgnoreCase(BoutiqueSignChest.getTypeStr()) == 0)
		{
			if (!PermissionsHandler.canSetPersonalSign(p))
			{
				p.sendMessage(PermissionsHandler.permissionErr);
				return;
			}
			
			p.sendMessage(plugName + "Panneau joueur ajouté à la liste :)");
			
			//TODO : signmanager.addsign
			//TODO : prerender
			
			bs.setLine4(ChatColor.GREEN + "[Actif]");
			
			plugin.signmanager.saveGlobalSigns();
		}	
		else if(bs.getSignTypeString().compareToIgnoreCase(BoutiqueSignWebAuction.getTypeStr()) == 0)
		{
			if (!PermissionsHandler.canSetWebAuctionSign(p))
			{
				p.sendMessage(PermissionsHandler.permissionErr);
				return;
			}
			
			p.sendMessage(plugName + "Panneau web joueur ajouté à la liste :)");
			
			//TODO : signmanager.addsign
			//TODO : prerender
			
			bs.setLine4(ChatColor.GREEN + "[Actif]");
			
			plugin.signmanager.saveGlobalSigns();
		}
		
		
		
	}
	
	
	public void displaySignInfo(Sign s, Player p) 
	{
		BoutiqueSign bs = getBoutiqueSign(s);
		
		String signOwnerString = bs.getOwnerString();
		String signTypeStr = bs.getSignTypeString();
		String separator = "-----------------------------------";
		

		//debut texte
		p.sendMessage(separator);
		
		if (!bs.isEnabled())
		{
			//TODO: formatter le chat ailleurs ? 
			p.sendMessage(ChatColor.YELLOW + "Le panneau n'est pas actif.");
			p.sendMessage(ChatColor.YELLOW + "Activez le en reposant le panneau.");
			return;
		}		
					
		/* Affiche le type de panneau et le propriétaire */
		
		//TODO: extern. messages
		
		//Type BoutiqueSignServer
		if(signTypeStr.compareToIgnoreCase(BoutiqueSignServer.getTypeStr()) == 0)
		{
			p.sendMessage("Ce panneau fait partie du magasin du serveur.");
		}
		
		//Type BoutiqueSignChest
		else if(signTypeStr.compareToIgnoreCase(BoutiqueSignChest.getTypeStr()) == 0)
		{
			p.sendMessage("Ce panneau fait partie du magasin de " + ChatColor.RED + signOwnerString + ChatColor.WHITE +  ".");			
			Chest signChest = ((BoutiqueSignChest) bs).getChest();			
			if(signChest == null) 
			{
				p.sendMessage(ChatColor.AQUA + "Aucun coffre n'est relié au panneau pour le moment !");				
			}
		}
		
		//Type BoutiqueSignWebAuction
		else if(signTypeStr.compareToIgnoreCase(BoutiqueSignWebAuction.getTypeStr()) == 0)
		{
			p.sendMessage("Ce panneau fait partie du magasin web de " + ChatColor.RED + signOwnerString + ChatColor.WHITE +  ".");
		}
		
		//Type Dummy ou autres
		else
		{
			//TODO: message : panneau de type inconnu
			//TODO: virer le panneau de la hashmap et de la bdd ?
			return;
		}
			
		
		Integer qtyFrom = bs.getQtyFrom();
		Integer qtyTo = bs.getQtyTo();
		BoutiqueItem itemTo = bs.getItemTo();
		BoutiqueItem itemFrom = bs.getItemFrom();
		Double moneyFrom = bs.getMoneyFrom();
		Double moneyTo = bs.getMoneyTo();
		
		
		/* Test iconomy */
		if(!EconomyHandler.currencyEnabled)
		{
			p.sendMessage("Il n'y a pas iConomy sur le serveur !");
			p.sendMessage(separator);
			return;
		}
		
		
		if(!bs.checkLines(p))
			return;
		
		
		/* Détermine le message en fonction du type de panneau */
		
		//Freebies
		if(bs.isFreebiesSign())
		{
			if(itemTo == null)
			{
				//TODO: message && currencystring
				p.sendMessage
				(
					"Un clic-droit sur ce panneau te donnera " + 
					ChatColor.RED + moneyTo + "Eus" + ChatColor.WHITE
				);			
			}
			else
			{
				//TODO: message
				p.sendMessage
				(
					"Un clic-droit sur ce panneau te donnera  " + 
					ChatColor.RED + qtyTo + ChatColor.WHITE + 	" "	+ 
					ChatColor.RED + itemTo.itemName + ChatColor.WHITE + 
					" gratuitement."
				);				
			}
		}
		//Donation (item ou $ => rien)
		else if(bs.isDonationSign())
		{
			if(itemFrom == null)
			{
				//TODO: message && currencystring
				p.sendMessage
				(
					"Ce panneau recoit des dons de " + 
					ChatColor.RED + moneyFrom + "Eus" + ChatColor.WHITE
				);			
			}
			else
			{
				//TODO: message
				p.sendMessage
				(
					"Ce panneau recoit des dons de " +
					ChatColor.RED + qtyFrom + ChatColor.WHITE + " "	+ 
					ChatColor.RED + itemFrom.itemName + ChatColor.WHITE
				);				
			}
		}
		else if(bs.isSellSign())
		{
			p.sendMessage(
				"Un clic-droit sur ce panneau te donnera " + 
				ChatColor.RED + qtyTo + ChatColor.WHITE + " " + 
				ChatColor.RED + itemTo.itemName + ChatColor.WHITE + 
				" pour la somme de " + 
				ChatColor.RED + moneyFrom + " Eus" + ChatColor.WHITE + 
				"."
			);
		}
		else if(bs.isBuySign())
		{
			p.sendMessage
			(
				"Ce panneau te donne " + ChatColor.RED + moneyTo + "Eus " + 
				ChatColor.WHITE + " pour chaque lot de " + 
				ChatColor.RED + qtyFrom + "x " + itemFrom.itemName + 
				ChatColor.WHITE + " que tu lui donneras."
			);
		}
		else if(bs.isTradeSign())
		{
			p.sendMessage
			(
				"Ce panneau échange " + 
				ChatColor.RED + qtyFrom + "x" + ChatColor.WHITE + " " + 
				ChatColor.RED + itemFrom.itemName + ChatColor.WHITE + ", contre " +
				ChatColor.RED + qtyTo + "x"  + ChatColor.WHITE + " " +
				ChatColor.RED + itemTo.itemName + ChatColor.WHITE + "."
			);
		}
		else
		{
			p.sendMessage
			(
				"VROUM VROUM, c'est quoi ce panneau omg, il a fumé ou quoi ???"
			);
			
			return;
		}
		
		bs.Render();
		
		p.sendMessage(separator);
	}


	
	public static boolean giveFree(BoutiqueSign bs, Player p)
	{
		return false;
		// TODO Auto-generated method stub
		
	}
	
	public static boolean getDonation(BoutiqueSign bs, Player p)
	{
		return false;
		// TODO Auto-generated method stub
	}


	public static boolean sellItem(BoutiqueSign bs, Player p)
	{
		Double costAmount = bs.getMoneyFrom();
		String signOwner = bs.getOwnerString();
		Integer qty = bs.getQtyTo();
		Integer id = bs.getItemTo().itemId;
		Integer damage = bs.getItemTo().itemDamage;
		
		int econ = EconomyHandler.hasEnough(p.getName(), costAmount);
		
		//TODO changer plugname par chatprefix
		String plugName = "";
		
		//Signe Serveur
		if (bs.getSignTypeString().compareToIgnoreCase(BoutiqueSignServer.getTypeStr()) == 0)
		{
			if (econ != 1)
			{
				p.sendMessage(plugName + EconomyHandler.getEconError(econ));
				return false;
			}

			EconomyHandler.modifyMoney(p.getName(), -costAmount);
			PlayerOperator.givePlayerItem(qty, id, damage, p);
			
			//todo: serverplayername
			signOwner = "[Serveur]";
		}
		
		
		//Signe coffre
		else if (bs.getSignTypeString().compareToIgnoreCase(BoutiqueSignChest.getTypeStr()) == 0)
		{
			
			Chest chest = ((BoutiqueSignChest) bs).getChest();
			if(chest == null)
			{
				//TODO
			}
			
			if (econ != 1)
			{
				p.sendMessage(plugName + EconomyHandler.getEconError(econ));
				return false;
			}
			else if (!ChestOperator.containsEnough(qty, id, damage, chest))
			{
				p.sendMessage(plugName + ChestOperator.notEnoughErr);
				return false;
			}
			
			
			int econOwner = EconomyHandler.modifyMoney(signOwner, costAmount);
			if (econOwner != 1)
			{
				p.sendMessage(plugName + EconomyHandler.getEconError(econ));
				return false;
			}
			
			
			
			EconomyHandler.modifyMoney(p.getName(), -costAmount);			
			ChestOperator.removeFromChestStock(qty, id, damage, chest);
			PlayerOperator.givePlayerItem(qty, id, damage , p);
		}
		
		//Signe webauction
		else if (bs.getSignTypeString().compareToIgnoreCase(BoutiqueSignWebAuction.getTypeStr()) == 0)
		{
			if (econ != 1)
			{
				p.sendMessage(plugName + EconomyHandler.getEconError(econ));
				return false;
			}
			else if (!Boutique.getInstance().webitems.containEnough(signOwner, id, damage, qty))
			{
				p.sendMessage(plugName + ChestOperator.notEnoughErr);
				return false;
			}
			
			int econOwner = EconomyHandler.modifyMoney(signOwner, costAmount);
			if (econOwner != 1)
			{
				p.sendMessage(plugName + EconomyHandler.getEconError(econ));
				return false;
			}
			
			if(Boutique.getInstance().webitems.removeFromWebStock(signOwner, qty, id, damage))
			{
				EconomyHandler.modifyMoney(p.getName(), -costAmount);
				PlayerOperator.givePlayerItem(qty, id, damage, p);	
			}
			//ChestOperator.removeFromChestStock(lTwoData[0], lTwoData[1], lTwoData[2], chest);
		}
		
		
		
		p.sendMessage(plugName + "Il te reste " + EconomyHandler.playerHave(p.getName()));
			
		try 
		{
			Boutique.getInstance().db.logTransaction(p.getLocation(), signOwner, p.getName(),  id , damage , qty, costAmount,"toto","titi");
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		
		
		
		
		
		
		
		return true;
		
	}


	public static boolean buyItem(BoutiqueSign bs, Player p)
	{
		return false;
		// TODO Auto-generated method stub
	}


	public static boolean tradeItems(BoutiqueSign bs, Player p)
	{
		return false;
		// TODO Auto-generated method stub
	}


	
	
	public void remove(Location location)
	{
		// TODO Auto-generated method stub
		this._signs.remove(location);
	}


	public void saveGlobalSigns()
	{
		// TODO Auto-generated method stub
	}


	public Set<Entry<Location, BoutiqueSign>> entrySet()
	{
		return this._signs.entrySet();
	}


	/* Enregistre le coffre pour un BoutiqueSignChest */
	public void setChest(Sign sign, Chest chest, Player p)
	{
		BoutiqueSign bs = this.getBoutiqueSign(sign);
		
		
		if(bs == null)
		{
			// TODO: message "Impossible de trouver le panneau en question"
			return;
		}
		else
		{
			Chest bsc = bs.getChest();
			
			/*
			avant: verifiait que le panneau n'etait pas deja relié au coffre			
			if (Boutique.SignChest.get(bsc) == cLoc)
			{
				p.sendMessage(plugName + "Ce panneau est déjà relié à ce coffre :/");
				return;
			}
			*/
				
			int distX = sign.getBlock().getX() - chest.getBlock().getX();
			int distZ = sign.getBlock().getZ() - chest.getBlock().getZ();
			
			int maxDist = 15;
			if (distX > maxDist || distZ > maxDist ) 
			{
				p.sendMessage(plugName + "Le coffre est trop loin du panneau ! (" + maxDist + " blocs max.)");
				return;
			}
			
						
			bs.setChest(chest);			
			
			p.sendMessage(plugName + "Panneau et coffre reliés !");
		}		
	}


	public void setOwner(Sign s, Player p)
	{
		
		
		if (!isSign(s))
		{
			p.sendMessage(plugName + "Ce panneau est mal configuré. Reposes le correctement avant.");
			return;
		}
		
		
		
		Location location = s.getBlock().getLocation();
		
		//TODO
		this.add(location, s);
		
		//TODO: a changer
		plugin.fileio.saveGlobalSigns();
			
		
	}


	
	///
	/// Le joueur peut-il casser le panneau ?
	///
	public boolean canBreakSign(Sign s, Player p) 
	{
		if (!this.isSign(s))
			return false;
		
		boolean bool = false;
		bool = isSignOwner(s,p);
		if (!bool)
			bool = p.isOp();
		
		return bool;
	}
	
	

	private void add(Location location, Sign s)
	{
		// TODO Auto-generated method stub
		
	}


	public void setSign(Sign s, Player p) 
	{
		this.signSetter(s.getLines(),p,s.getBlock());
	}

	public void setSign(String[] lines, Player p, Block s) 
	{
		this.signSetter(lines, p, s);
	}
	


	public boolean isSign(Block b) 
	{
		if (!(b.getState() instanceof Sign))
			return false;
		
		return isSign((Sign) b);
	}
	
	public boolean isSign(Sign s) 
	{
		return haveLocation(s.getBlock().getLocation());
	}
	

	/* Renvoi true si le player est le proprio du panneau */
	public boolean isSignOwner(Sign sign, Player p)
	{	
		return this.getBoutiqueSign(sign).getOwner().getName().compareToIgnoreCase(p.getName()) == 0;
	}


	public void loadGlobalSignData()
	{
		// TODO Auto-generated method stub
		plugin.fileio.loadGlobalSignData();
	}


	

	

}
