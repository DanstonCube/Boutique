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

import com.danstoncube.plugin.drzoid.Boutique.SignTypes.BoutiqueSignChest;
import com.danstoncube.plugin.drzoid.Boutique.SignTypes.BoutiqueSignDummy;
import com.danstoncube.plugin.drzoid.Boutique.SignTypes.BoutiqueSignServer;
import com.danstoncube.plugin.drzoid.Boutique.SignTypes.BoutiqueSignWebAuction;

@SuppressWarnings("unused")
public class BoutiqueSignManager
{
	
	private HashMap<String,BoutiqueSign> _signs = new HashMap<String,BoutiqueSign>();
	
	
	
	
	private Boutique plugin = null;
	
	private static BoutiqueSignManager _instance = null;
	public static BoutiqueSignManager getInstance()
	{
		return _instance;
	}
	
	public BoutiqueSignManager(Boutique boutique)
	{
		this.plugin = boutique;
		BoutiqueSignManager._instance = this;
	}


	public Boolean haveLocation(String l)
	{
		return _signs.containsKey(l);			
	}
	
	public Boolean haveLocation(Location l)
	{		
		return _signs.containsKey(getLocationString(l));			
	}
	
	private String getLocationString(Location l)
	{
		return l.getWorld().getName() + ":" + l.getBlockX() + ":" +  l.getBlockY() + ":" + l.getBlockZ();  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	public Boolean isEnabled(Sign s)
	{
		BoutiqueSign bs = _signs.get(s.getBlock().getLocation());
		
		if(bs == null) 
			return false;
		
		return bs.isEnabled();		
	}
	
	public BoutiqueSign getBoutiqueSign(Block b)
	{
		return _signs.get(getLocationString(b.getLocation()));	
	}
	

	public void saveSignData()	
	{
		
	}
	

	public void useSign(Block b, Player p) 
	{
		BoutiqueSign bs = getBoutiqueSign(b);
		
		if (!bs.isEnabled())
		{
			//TODO: formatter le chat ailleurs ? 
			p.sendMessage(ChatColor.YELLOW + Messages.getString("Sign.INACTIVESIGN")); //$NON-NLS-1$
			p.sendMessage(ChatColor.YELLOW + Messages.getString("Sign.MAKEANOTHERONE")); //$NON-NLS-1$
			return;
		}		
		
		// See if player has permission to use signs.
		if(!PermissionsHandler.canUseSign(p))
		{
			p.sendMessage(plugin.chatPrefix + PermissionsHandler.permissionErr);
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
	
	
	
	


	//private void signSetter(String[] lines, Player p, Block s) 
	
	private void signSetter(Block b, Player p, String[] lines) 
	{		
		//TODO: virer debug
		//p.sendMessage("dbg1");
		
		
		if(b==null) 
			return;
		
		BoutiqueSign bs = new BoutiqueSign();
		
		bs.setOwner(p);
		bs.setLocation(b.getLocation());
		bs.setLines(lines);

		//TODO: virer debug
		/*
		p.sendMessage("dbg1 : line1 = " + bs.getLine1());
		p.sendMessage("dbg1 : line2 = " + bs.getLine2());
		p.sendMessage("dbg1 : line3 = " + bs.getLine3());
		p.sendMessage("dbg1 : line4 = " + bs.getLine4());		
		p.sendMessage("dbg2 : type = " + bs.getType());
		*/
		
		if(bs.isSignServer())
		{
			
			if (!PermissionsHandler.canSetGlobalSign(p))
			{
				p.sendMessage(PermissionsHandler.permissionErr);
				return;
			}
			
			if(!bs.checkLines(p))
			{
				return;
			}
			
			p.sendMessage(plugin.chatPrefix + Messages.getString("Sign.SERVERSIGNADDED")); //$NON-NLS-1$
		}
		
		else if(bs.isSignChest())
		{
			if (!PermissionsHandler.canSetPersonalSign(p))
			{
				p.sendMessage(plugin.chatPrefix + PermissionsHandler.permissionErr);
				return;
			}
			
			if(!bs.checkLines(p))
			{
				//TODO: virer debug
				//p.sendMessage("erreur checkline");
				return;
			}
			
			p.sendMessage(plugin.chatPrefix + Messages.getString("Sign.CHESTSIGNADDED")); //$NON-NLS-1$
		}	
		else if(bs.isSignWebAuction())
		{
			if (!PermissionsHandler.canSetWebAuctionSign(p))
			{
				p.sendMessage(PermissionsHandler.permissionErr);
				return;
			}
			
			if(!bs.checkLines(p))
			{
				//TODO: virer debug
				//p.sendMessage("erreur checkline");
				return;
			}
			
			p.sendMessage(plugin.chatPrefix + Messages.getString("Sign.WEBAUCTIONSIGNADDED")); //$NON-NLS-1$
		}
		else
		{
			return;
		}
		
		
		bs.setLine4("ok");			 //$NON-NLS-1$
		bs.Render();
		
		this.put(bs);
		plugin.signmanager.saveGlobalSigns();
	}
	
	
	public void displaySignInfo(Block b, Player p) 
	{
		//TODO virer debug
		//p.sendMessage(plugin.chatPrefix + "displaySignInfo");
		
		BoutiqueSign bs = getBoutiqueSign(b);
		
		String signOwnerString = bs.getOwnerString();
		String signTypeStr = bs.getType();
		String separator = Messages.getString("Sign.SIGNINFOSEPARATOR"); //$NON-NLS-1$
		

		//debut texte
		p.sendMessage(separator);
		
		if (!bs.isEnabled())
		{
			//TODO: formatter le chat ailleurs ? 
			p.sendMessage(ChatColor.YELLOW + Messages.getString("Sign.INACTIVESIGN")); //$NON-NLS-1$
			p.sendMessage(ChatColor.YELLOW + Messages.getString("Sign.MAKEANOTHERONE")); //$NON-NLS-1$
			return;
		}		
					
		
		
		/* Affiche le type de panneau et le propriétaire */
		
		//TODO virer debug
		//p.sendMessage("dbg1: Type=" + bs.getType());
		
		if(bs.isSignServer())
		{
			//Type BoutiqueSignServer
			p.sendMessage(Messages.getString("Sign.SIGNISSERVER")); //$NON-NLS-1$
		}
		else if(bs.isSignChest())
		{	
			//Type BoutiqueSignChest
			
			p.sendMessage(Messages.getString("Sign.SIGNISPLAYER") + ChatColor.RED + signOwnerString + ChatColor.WHITE +  ".");			 //$NON-NLS-1$ //$NON-NLS-2$
			
			String signchest = bs.getChestString();			
			if(signchest.isEmpty()) 
			{
				p.sendMessage(ChatColor.RED + Messages.getString("Sign.SIGNCHESTNOTBIND")); //$NON-NLS-1$
			}
			
		}
		else if(bs.isSignWebAuction())
		{
			//Type BoutiqueSignWebAuction
			p.sendMessage(Messages.getString("Sign.SIGNISWEB") + ChatColor.RED + signOwnerString + ChatColor.WHITE +  "."); //$NON-NLS-1$ //$NON-NLS-2$
		}
		else
		{
			//Type inconnu
			p.sendMessage(ChatColor.RED + Messages.getString("Sign.SIGNISUNKNOWTYPE") + ChatColor.WHITE +  signOwnerString + ChatColor.RED + Messages.getString("Sign.ISTHEOWNER") ); //$NON-NLS-1$ //$NON-NLS-2$
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
			p.sendMessage(Messages.getString("Sign.NOECONERR")); //$NON-NLS-1$
			p.sendMessage(separator);
			return;
		}
		
		
		if(!bs.checkLines(p))
		{
			//TODO virer debug
			return;
		}
		
		/* Détermine le message en fonction du type de panneau */
		
		//Freebies
		if(bs.isFreebiesSign())
		{
			if(itemTo == null)
			{
				//TODO: message && currencystring
				p.sendMessage
				(
					Messages.getString("Sign.RIGHTCLICKWILLGETYOU") +  //$NON-NLS-1$
					ChatColor.RED + moneyTo + Messages.getString("Sign.22") + ChatColor.WHITE //$NON-NLS-1$
				);			
			}
			else
			{
				//TODO: message
				p.sendMessage
				(
					Messages.getString("Sign.RIGHTCLICKWILLGETYOU") +  //$NON-NLS-1$
					ChatColor.RED + qtyTo + ChatColor.WHITE + 	" "	+  //$NON-NLS-1$
					ChatColor.RED + itemTo.itemName + ChatColor.WHITE + 
					Messages.getString("Sign.FORFREE") //$NON-NLS-1$
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
					Messages.getString("Sign.SIGNGETDONATIONS") +  //$NON-NLS-1$
					ChatColor.RED + moneyFrom + Messages.getString("Sign.27") + ChatColor.WHITE //$NON-NLS-1$
				);			
			}
			else
			{
				//TODO: message
				p.sendMessage
				(
					Messages.getString("Sign.SIGNGETDONATIONS") + //$NON-NLS-1$
					ChatColor.RED + qtyFrom + ChatColor.WHITE + " "	+  //$NON-NLS-1$
					ChatColor.RED + itemFrom.itemName + ChatColor.WHITE
				);				
			}
		}
		else if(bs.isSellSign())
		{
			p.sendMessage(
				Messages.getString("Sign.RIGHTCLICKWILLGETYOU") +  //$NON-NLS-1$
				ChatColor.RED + qtyTo + ChatColor.WHITE + " " +  //$NON-NLS-1$
				ChatColor.RED + itemTo.itemName + ChatColor.WHITE + 
				Messages.getString("Sign.FORMONEY") +  //$NON-NLS-1$
				ChatColor.RED + moneyFrom + " Eus" + ChatColor.WHITE +  //$NON-NLS-1$
				"." //$NON-NLS-1$
			);
		}
		else if(bs.isBuySign())
		{
			p.sendMessage
			(
				Messages.getString("Sign.SIGNGIVEYOUFORMONEY") + ChatColor.RED + moneyTo + "Eus " +  //$NON-NLS-1$ //$NON-NLS-2$
				ChatColor.WHITE + Messages.getString("Sign.FOREACHSTACKOF") +  //$NON-NLS-1$
				ChatColor.RED + qtyFrom + Messages.getString("Sign.QTY1") + itemFrom.itemName +  //$NON-NLS-1$
				ChatColor.WHITE + Messages.getString("Sign.YOUWILLGIVEHIM") //$NON-NLS-1$
			);
		}
		else if(bs.isTradeSign())
		{
			p.sendMessage
			(
				Messages.getString("Sign.SIGNISTRADING") +  //$NON-NLS-1$
				ChatColor.RED + qtyFrom + Messages.getString("Sign.QTY") + ChatColor.WHITE + " " +  //$NON-NLS-1$ //$NON-NLS-2$
				ChatColor.RED + itemFrom.itemName + ChatColor.WHITE + Messages.getString("Sign.VERSUS1") + //$NON-NLS-1$
				ChatColor.RED + qtyTo + Messages.getString("Sign.QTY")  + ChatColor.WHITE + " " + //$NON-NLS-1$ //$NON-NLS-2$
				ChatColor.RED + itemTo.itemName + ChatColor.WHITE + "." //$NON-NLS-1$
			);
		}
		else
		{
			p.sendMessage
			(
				Messages.getString("Sign.VROUMVROUMERR") //$NON-NLS-1$
			);
			
			return;
		}
		
		bs.Render();
		
		p.sendMessage(separator);
	}


	
	
	
	


	public boolean sellItem(BoutiqueSign bs, Player p)
	{
		Double costAmount = bs.getMoneyFrom();
		String signOwner = bs.getOwnerString();
		Integer qty = bs.getQtyTo();
		Integer id = bs.getItemTo().itemId;
		Integer damage = bs.getItemTo().itemDamage;
		
		int econ = EconomyHandler.hasEnough(p.getName(), costAmount);
		

		//Panneau Serveur
		if (bs.isSignServer())
		{
			if (econ != 1)
			{
				p.sendMessage(plugin.chatPrefix + EconomyHandler.getEconError(econ));
				return false;
			}

			EconomyHandler.modifyMoney(p.getName(), -costAmount);
			PlayerOperator.givePlayerItem(qty, id, damage, p);
			
			//todo: serverplayername
			signOwner = "[Serveur]"; //$NON-NLS-1$
		}
		//Panneau coffre
		else if (bs.isSignChest())
		{
			
			Chest chest = bs.getChest();
			
			if(chest == null)
			{			
				p.sendMessage(plugin.chatPrefix + Messages.getString("Sign.NOBINDEDCHESTFOUND")); //$NON-NLS-1$
				return false;
			}
			
			if (econ != 1)
			{
				p.sendMessage(plugin.chatPrefix + EconomyHandler.getEconError(econ));
				return false;
			}
			else if (!ChestOperator.containsEnough(qty, id, damage, chest))
			{
				p.sendMessage(plugin.chatPrefix + ChestOperator.notEnoughErr);
				return false;
			}
			
			
			int econOwner = EconomyHandler.modifyMoney(signOwner, costAmount);
			if (econOwner != 1)
			{
				p.sendMessage(plugin.chatPrefix + EconomyHandler.getEconError(econ));
				return false;
			}
			
			
			
			EconomyHandler.modifyMoney(p.getName(), -costAmount);			
			ChestOperator.removeFromChestStock(qty, id, damage, chest);
			PlayerOperator.givePlayerItem(qty, id, damage , p);
		}
		
		//Panneau webauction
		else if (bs.isSignWebAuction())
		{

			if (econ != 1)
			{
				p.sendMessage(plugin.chatPrefix + EconomyHandler.getEconError(econ));
				return false;
			}
			
			//TO CHANGE
			if(!plugin.db.wa_HasEnoughItem(signOwner, id, damage, qty))
			{
				p.sendMessage(plugin.chatPrefix + WebItemsOperator.notEnoughErr);
				return false;
			}
			
			int econOwner = EconomyHandler.modifyMoney(signOwner, costAmount);
			if (econOwner != 1)
			{
				p.sendMessage(plugin.chatPrefix + EconomyHandler.getEconError(econ));
				return false;
			}
			
			
			if(!plugin.db.wa_RemoveFromStock(signOwner, id, damage, qty))
			{
				p.sendMessage(Messages.getString("Sign.UNABLETOREMOVEFROMWEBSTOCKS")); //$NON-NLS-1$
				
				return false;
			}
			
				
				
			int retmoney = EconomyHandler.modifyMoney(p.getName(), -costAmount);
			
			//TODO virer debug
			//p.sendMessage("dbg4: retmoney = " + retmoney);
			
			PlayerOperator.givePlayerItem(qty, id, damage, p);	
			
			//TODO virer debug
			//p.sendMessage("dbg4: wa_RemoveFromStock");
			
		}
		
		
		p.sendMessage(plugin.chatPrefix + Messages.getString("Sign.YOUGET") + qty + " " + bs.getItemTo().itemName); //$NON-NLS-1$ //$NON-NLS-2$
		p.sendMessage(plugin.chatPrefix + Messages.getString("Sign.MONEYREMAINING") + EconomyHandler.playerBalance(p.getName())); //$NON-NLS-1$
			
		try 
		{
			plugin.db.logTransaction(p.getLocation(), signOwner, p.getName(),  id , damage , qty, costAmount,"toto","titi"); //$NON-NLS-1$ //$NON-NLS-2$
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
	
	
	public boolean giveFree(BoutiqueSign bs, Player p)
	{
		Double costAmount = bs.getMoneyFrom();
		String signOwner = bs.getOwnerString();
		Integer qty = bs.getQtyTo();
		Integer id = bs.getItemTo().itemId;
		Integer damage = bs.getItemTo().itemDamage;
		
		int econ = EconomyHandler.hasEnough(p.getName(), costAmount);
			
		//Signe Serveur
		if (bs.isSignServer())
		{
			PlayerOperator.givePlayerItem(qty, id, damage, p);
			signOwner = "[Serveur]"; //$NON-NLS-1$
		}
		//Signe coffre
		else if (bs.isSignChest())
		{
			
			Chest chest = bs.getChest();
			
			if(chest == null)
			{			
				p.sendMessage(plugin.chatPrefix + Messages.getString("Sign.NOBINDEDCHESTFOUND")); //$NON-NLS-1$
				return false;
			}
			
			if (!ChestOperator.containsEnough(qty, id, damage, chest))
			{
				p.sendMessage(plugin.chatPrefix + ChestOperator.notEnoughErr);
				return false;
			}
			
			ChestOperator.removeFromChestStock(qty, id, damage, chest);
			PlayerOperator.givePlayerItem(qty, id, damage , p);
		}
		
		//Signe webauction
		else if (bs.isSignWebAuction())
		{
			//TODO virer debug
			//p.sendMessage("dbg1: isSignWebAuction");
			
			//TO CHANGE
			if(!plugin.db.wa_HasEnoughItem(signOwner, id, damage, qty))
			{
				p.sendMessage(plugin.chatPrefix + ChatColor.RED + WebItemsOperator.notEnoughErr);
				return false;
			}
			
			if(!plugin.db.wa_RemoveFromStock(signOwner, id, damage, qty))
			{
				p.sendMessage(plugin.chatPrefix + ChatColor.RED + Messages.getString("Sign.UNABLETOREMOVEFROMWEBSTOCKS")); //$NON-NLS-1$
				return false;
			}
			
			PlayerOperator.givePlayerItem(qty, id, damage, p);	
		}

		p.sendMessage(plugin.chatPrefix + Messages.getString("Sign.YOUGET") + qty + " " + bs.getItemTo().itemName); //$NON-NLS-1$ //$NON-NLS-2$
		
		//p.sendMessage(plugin.chatPrefix + "Il te reste " + EconomyHandler.playerHave(p.getName()));
			
		try 
		{
			plugin.db.logTransaction(p.getLocation(), signOwner, p.getName(),  id , damage , qty, 0.0,"toto","titi"); //$NON-NLS-1$ //$NON-NLS-2$
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
	
	
	
	
	
	public boolean buyItem(BoutiqueSign bs, Player p)
	{
		Double costAmount = bs.getMoneyTo();
		String signOwner = bs.getOwnerString();
		Integer qty = bs.getQtyFrom();
		Integer id = bs.getItemFrom().itemId;
		Integer damage = bs.getItemFrom().itemDamage;
		

		
		if (!PlayerOperator.playerHasEnough( qty, id, damage, p))
		{
			p.sendMessage(plugin.chatPrefix + PlayerOperator.playerStockErr);
			return false;
		}
		
		// Panneau "Serveur"
		if (bs.isSignServer())
		{
			//Enleve les objets dans l'inventaire du joueur
			PlayerOperator.removeFromPlayer(qty, id ,damage, p);
			
			//Ajoute les thunes au compte du joueur
			EconomyHandler.modifyMoney(p.getName(), costAmount);
			
			//Enleve le propriétaire du panneau pour les logs (car panneau serveur) !
			signOwner = ""; //$NON-NLS-1$
		}
		
		// Panneau "webauctions"
		// Achete des objet, et les met dans le stock "webauctions"
		else if (bs.isSignWebAuction())
		{
			
			// Vérifie que l'acheteur à assez d'argent
			int econ = EconomyHandler.hasEnough(signOwner, costAmount);
			if (econ != 1)
			{
				p.sendMessage(plugin.chatPrefix + EconomyHandler.getEconError(econ));
				return false;
			}
			
			// Ajoute les objets au stock WebAuctions
			if(!plugin.db.wa_AddToStock(p.getName(), id, damage, qty))
			{
				p.sendMessage(plugin.chatPrefix + Messages.getString("Sign.UNABLETOADDINWEBSTOCKS")  + signOwner + Messages.getString("Sign.SMILEYSAD")); //$NON-NLS-1$ //$NON-NLS-2$
				return false;
			}
			
			
			// Enleve les objets de l'inventaire du vendeur
			PlayerOperator.removeFromPlayer(qty, id, damage, p);
			
			// Enleve la somme du compte de l'acheteur
			EconomyHandler.modifyMoney(signOwner, -costAmount);
			
			// Credite le vendeur
			EconomyHandler.modifyMoney(p.getName(), costAmount);
			
		}
		
		// Panneau "coffre"
		// Achete des objet, et les met dans le coffre relié
		else if (bs.isSignChest())
		{
			//TODO virer debug
			//p.sendMessage("dbg0: debut signchest");
			
			// Cherche le coffre relié au panneau
			Chest chest = bs.getChest();
			if(chest==null)	
			{
				//TODO virer debug
				//p.sendMessage("dbg1: coffre introuvable");
				return false;
			}
			
			// Vérifie qu'il reste de la place dans le coffre pour stocker les objets à acheter
			if (!ChestOperator.hasEnoughSpace(qty,id,damage, chest))
			{
				p.sendMessage(plugin.chatPrefix + ChestOperator.notEnoughSpaceErr);
				return false;
			}			
			
			// Vérifie que l'acheteur possède assez d'argent
			int econ = EconomyHandler.hasEnough(signOwner, costAmount);
			if (econ != 1)
			{
				p.sendMessage(plugin.chatPrefix + EconomyHandler.getEconError(econ));
				return false;
			}
			
			// Debitte la somme au a l'acheteur
			EconomyHandler.modifyMoney(signOwner, -costAmount);
			
			// Credite le coffre de l'acheteur
			ChestOperator.addToChestStock(qty, id, damage, chest);
			
			// Retire les objets au vendeur
			PlayerOperator.removeFromPlayer(qty, id, damage, p);
			
			// Credite le compte du vendeur
			EconomyHandler.modifyMoney(p.getName(), costAmount);
			
			//TODO virer debug
			//p.sendMessage("dbg2: fin signchest");
		}
		
		p.sendMessage(plugin.chatPrefix + Messages.getString("Sign.YOUSELL") + qty + " " + bs.getItemFrom().itemName); //$NON-NLS-1$ //$NON-NLS-2$
		p.sendMessage(plugin.chatPrefix + Messages.getString("Sign.YOUHAVENOW") + EconomyHandler.playerBalance(p.getName()) + "."); //$NON-NLS-1$ //$NON-NLS-2$
		
		try 
		{
			Boutique.getInstance().db.logTransaction(p.getLocation(), p.getName(), signOwner, id, damage, qty, costAmount,"toto","titi"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		catch (Exception e) 
		{
			// TODO
			e.printStackTrace();
		} 
		
		return true;
	}
	
	

	public boolean getDonation(BoutiqueSign bs, Player p)
	{
		//Double costAmount = bs.getMoneyTo();
		
		String signOwner = bs.getOwnerString();
		
		//Teste donation item ou thunes ?
		
		//Don d'item
		if(bs.getItemFrom() != null)
		{
			Integer qty = bs.getQtyFrom();
			Integer id = bs.getItemFrom().itemId;
			Integer damage = bs.getItemFrom().itemDamage;
			
			if (!PlayerOperator.playerHasEnough( qty, id, damage, p))
			{
				p.sendMessage(plugin.chatPrefix + PlayerOperator.playerStockErr);
				return false;
			}

			if (bs.isSignServer())
			{
				//Enleve le propriétaire du panneau pour les logs (car panneau serveur) !
				signOwner = ""; //$NON-NLS-1$
			}
			else if (bs.isSignWebAuction())
			{
				// Panneau "webauctions"
				// Ajoute les objets au stock WebAuctions
				if(!plugin.db.wa_AddToStock(p.getName(), id, damage, qty))
				{
					p.sendMessage(plugin.chatPrefix + Messages.getString("Sign.UNABLETOADDINWEBSTOCKS")  + signOwner + Messages.getString("Sign.SMILEYSAD")); //$NON-NLS-1$ //$NON-NLS-2$
					return false;
				}
			}			
			else if (bs.isSignChest())
			{
				//p.sendMessage("dbg0: debut signchest");
				
				// Cherche le coffre relié au panneau
				Chest chest = bs.getChest();
				if(chest==null)	
				{
					//p.sendMessage("dbg1: coffre introuvable");
					return false;
				}
								
				// Vérifie qu'il reste de la place dans le coffre pour stocker les objets à acheter
				if (!ChestOperator.hasEnoughSpace(qty,id,damage, chest))
				{
					p.sendMessage(plugin.chatPrefix + ChestOperator.notEnoughSpaceErr);
					return false;
				}			

				// Credite le coffre de l'acheteur
				ChestOperator.addToChestStock(qty, id, damage, chest);
			}
			
			// Retire les objets au donneur
			PlayerOperator.removeFromPlayer(qty, id, damage, p);
			
			p.sendMessage(plugin.chatPrefix + Messages.getString("Sign.YOUGIVE") + qty + Messages.getString("Sign.QTY1") + bs.getItemFrom().itemName + " " + (bs.isSignServer() ? Messages.getString("Sign.TOSERVER"): Messages.getString("Sign.TO") + signOwner + ".")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			
			
			try 
			{
				Boutique.getInstance().db.logTransaction(p.getLocation(), p.getName(), signOwner, id , damage, qty , 0.0 ,"toto","titi"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
		}
		else if(bs.getMoneyFrom() != null)
		{
			
			//Don d'argent
			
			
			//Verif money doneur
			if (EconomyHandler.hasEnough(p.getName(), bs.getMoneyFrom())!=1)
			{
				p.sendMessage(plugin.chatPrefix + EconomyHandler.noFundsErr);
				return false;
			}
			
			//Crédite le receveur (sauf si serveur)
			if(!bs.isSignServer())
			{
				EconomyHandler.modifyMoney(bs.getOwnerString(), bs.getMoneyFrom());
			}
			
			//Débite le doneur
			EconomyHandler.modifyMoney(p.getName(), -bs.getMoneyFrom());
			

			p.sendMessage(plugin.chatPrefix + Messages.getString("Sign.YOUGIVE") + BoutiqueSign.formatMoney(bs.getMoneyFrom()) + " " + BoutiqueSign.formatCurrency(bs.getMoneyFrom()) + " " + (bs.isSignServer() ? Messages.getString("Sign.TOSERVER"): Messages.getString("Sign.TO") + signOwner));
			p.sendMessage(plugin.chatPrefix + Messages.getString("Sign.MONEYREMAINING") + EconomyHandler.playerHave(p.getName())); //$NON-NLS-1$
			
			
			try 
			{
				Boutique.getInstance().db.logTransaction(p.getLocation(), p.getName(), signOwner, 0, 0, 0, bs.getMoneyFrom() ,"toto","titi"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
		}
		else
		{
			p.sendMessage(plugin.chatPrefix + Messages.getString("Sign.VROUMVROUMERR"));
			return false;
		}

		return true;
	}
	

	public boolean tradeItems(BoutiqueSign bs, Player p)
	{
				
		int qtyFrom = bs.getQtyFrom();
		int qtyTo = bs.getQtyTo();
		int damageFrom = bs.getItemFrom().itemDamage;
		int damageTo = bs.getItemTo().itemDamage;
		int idFrom = bs.getItemFrom().itemId;
		int idTo = bs.getItemTo().itemId;
		
		if (bs.isSignServer())
		{
			if (!PlayerOperator.playerHasEnough(qtyFrom, idFrom, damageFrom, p))
			{
				p.sendMessage(plugin.chatPrefix + PlayerOperator.playerStockErr);
				return false;
			}
			
			PlayerOperator.removeFromPlayer(qtyFrom, idFrom, damageFrom, p);
			PlayerOperator.givePlayerItem(qtyTo, idTo, damageTo, p);
		
		}
		else if (bs.isSignChest())
		{
			
			Chest chest = bs.getChest();
			if(chest==null)
				return false;
			
			if (!PlayerOperator.playerHasEnough(qtyFrom, idFrom, damageFrom, p))
			{
				p.sendMessage(plugin.chatPrefix + PlayerOperator.playerStockErr);
				return false;
			}
			else if (!ChestOperator.containsEnough(qtyTo, idTo, damageTo, chest))
			{
				p.sendMessage(plugin.chatPrefix + ChestOperator.notEnoughErr);
				return false;
			}
			else if (!ChestOperator.hasEnoughSpace(qtyFrom, idFrom, damageFrom, chest))
			{
				p.sendMessage(plugin.chatPrefix + ChestOperator.notEnoughSpaceErr);
				return false;
			}
			
			
			ChestOperator.removeFromChestStock(qtyTo, idTo, damageTo, chest);
			ChestOperator.addToChestStock(qtyFrom, idFrom, damageFrom, chest);
			
			PlayerOperator.removeFromPlayer(qtyFrom, idFrom, damageFrom, p);
			PlayerOperator.givePlayerItem(qtyTo, idTo, damageTo, p);
						
		}
		else if (bs.isSignWebAuction())
		{

			if (!PlayerOperator.playerHasEnough(qtyFrom, idFrom, damageFrom, p))
			{
				p.sendMessage(plugin.chatPrefix + PlayerOperator.playerStockErr);
				return false;
			}
			else if (!WebItemsOperator.containEnough(bs.getOwnerString(), idTo, damageTo, qtyTo))
			{
				//TODO: message "La personne avec qui tu échanges n'a plus assez d'items à échanger !"
				p.sendMessage(plugin.chatPrefix + Messages.getString("Sign.OTHERHAVENOSTOCKS")); //$NON-NLS-1$
				return false;
			}
			
			
			//Prend l'objet source
			PlayerOperator.removeFromPlayer(qtyFrom, idFrom, damageFrom, p);
			WebItemsOperator.addToWebStock(bs.getOwnerString(), idFrom, damageFrom, qtyFrom);
			
			//Donne l'objet de destination
			WebItemsOperator.removeFromWebStock(bs.getOwnerString(), idTo, damageTo, qtyTo);
			PlayerOperator.givePlayerItem(qtyTo, idTo, damageTo, p);						
		}
		
		
		
		//TODO: messages configurables
		p.sendMessage(plugin.chatPrefix + Messages.getString("Sign.YOUGIVE") + bs.getQtyFrom() + " " + bs.getItemFrom().itemName);			 //$NON-NLS-1$ //$NON-NLS-2$
		p.sendMessage(plugin.chatPrefix + Messages.getString("Sign.VERSUS2") + bs.getQtyTo() + " " + bs.getItemTo().itemName); //$NON-NLS-1$ //$NON-NLS-2$
	
		
		//log transaction
		
		try 
		{
			Boutique.getInstance().db.logTransaction(p.getLocation(), p.getName(), bs.getOwnerString(), idFrom, damageFrom, qtyFrom, 0.0, "toto", "TRADE"); //$NON-NLS-1$ //$NON-NLS-2$
			Boutique.getInstance().db.logTransaction(p.getLocation(), bs.getOwnerString(), p.getName(), idTo, damageTo, qtyTo, 0.0, "toto", "TRADE");		 //$NON-NLS-1$ //$NON-NLS-2$
		}
		catch (Exception e) 
		{
			// TODO
			e.printStackTrace();
		} 
		
		return true;

	}

	public void put(String locationstr, BoutiqueSign bs)
	{
		this._signs.put(locationstr, bs);
	}
	
	public void put(Location location, BoutiqueSign bs)
	{
		this._signs.put(getLocationString(location), bs);
	}
	
	public void remove(Location location)
	{
		this._signs.remove(BoutiqueSign.getLocationString(location));
	}


	public void saveGlobalSigns()
	{
		// TODO Auto-generated method stub
		plugin.fileio.saveGlobalSigns();
	}


	public Set<Entry<String, BoutiqueSign>> entrySet()
	{
		return this._signs.entrySet();
	}


	public void setChest(Sign sign, Chest chest, Player p)
	{
		setChest(sign.getBlock(), chest,  p);
		return;
	}
	
	/* Enregistre le coffre pour un BoutiqueSignChest */
	public void setChest(Block sign, Chest chest, Player p)
	{
		BoutiqueSign bs = this.getBoutiqueSign(sign);
		
		
		if(bs == null)
		{
			// TODO: message "Impossible de trouver le panneau en question"
			p.sendMessage(plugin.chatPrefix + Messages.getString("Sign.CHOOSESIGNBEFORE")); //$NON-NLS-1$
			return;
		}
		else
		{
			String newChestLoc = ""; //$NON-NLS-1$
			String oldChestLoc = ""; //$NON-NLS-1$
			
			Chest bsc = bs.getChest();
			
			
			oldChestLoc = bs.getChestString();
			newChestLoc = BoutiqueSign.getLocationString(chest.getBlock().getLocation());
			
			
			
			//verifie que le panneau n'etait pas deja relié au coffre			
			if (oldChestLoc == newChestLoc)
			{
				p.sendMessage(plugin.chatPrefix + Messages.getString("Sign.SIGNALREADYBINDED")); //$NON-NLS-1$
				return;
			}
			
			int distX = sign.getX() - chest.getBlock().getX();
			int distZ = sign.getZ() - chest.getBlock().getZ();
		
			
			int maxDist = 15;
			if (distX > maxDist || distZ > maxDist ) 
			{
				p.sendMessage(plugin.chatPrefix + Messages.getString("Sign.CHESTTOOFARAWAY") + maxDist + Messages.getString("Sign.MAXBLOCKS")); //$NON-NLS-1$ //$NON-NLS-2$
				return;
			}
			
			//DEBUG
			//p.sendMessage(plugin.chatPrefix + "Enregistrement coffre");
						
			bs.setChest(chest);			
			
			updateSignDb(bs);
			
			p.sendMessage(plugin.chatPrefix + Messages.getString("Sign.CHESTBINDED")); //$NON-NLS-1$
					
			//DEBUG
			//p.sendMessage(plugin.chatPrefix + "panneau: " + bs.getChestString());
			
		}		
	}



	
	
	public void setOwner(Sign s, Player p, String newowner)
	{
		
		
		if (!isSign(s))
		{
			p.sendMessage(plugin.chatPrefix + Messages.getString("Sign.SIGNERRORPLEASEREDO")); //$NON-NLS-1$
			return;
		}
		
		
		
		Location location = s.getBlock().getLocation();
		
		
		BoutiqueSign bs = getBoutiqueSign(s.getBlock());
		if(bs==null)
			return;
		
		bs.setOwnerString(newowner);
		
		updateSignDb(bs);
		
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
	
	


	public void setSign(Block b, Player p, String[] lines) 
	{
		this.signSetter(b,p,lines);
		
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
		return getBoutiqueSign(sign.getBlock()).getOwnerString().compareToIgnoreCase(p.getName()) == 0;
	}

	
	public boolean isBoutiqueSign(Sign clickedSign)
	{
		return haveLocation(clickedSign.getBlock().getLocation());
	}

	public boolean isBoutiqueSign(Block clickedBlock)
	{
		return haveLocation(clickedBlock.getLocation());
	}


	
	

	private void insertSignDb(BoutiqueSign bs)
	{
		//TODO: if datafile
		put(bs);
		saveGlobalSigns();
		
		//TODO: else if datasql
		
	}

	private void updateSignDb(BoutiqueSign bs)
	{
		//TODO: if datafile
		put(bs);
		saveGlobalSigns();
		
		//TODO: else if datasql
	}
	
	public void loadGlobalSignData()
	{
		plugin.fileio.loadGlobalSignData();
	}

	public void put(BoutiqueSign bs)
	{
		if(this._signs.containsKey(bs.getLocationString()))
		{
			this._signs.remove(bs.getLocationString());
		}
		
		put(bs.getLocationString(), bs);		
	}

	public boolean isChestOwner(Chest chest, Player p)
	{
		//TODO: Check worldguard
		//TODO: Cehck LWC
		return WorldGuardOperator.canBuild(p, chest.getBlock());
	}

	


	

	

}
