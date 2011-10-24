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
	
	private HashMap<String,BoutiqueSign> _signs = new HashMap<String,BoutiqueSign>();
	
	//TODO: changer ca
	private String plugName = "Boutique";
	
	private Boutique plugin = null;
	
	private static BoutiqueSignManager _instance = null;
	public static BoutiqueSignManager getInstance()
	{
		return _instance;
	}
	
	public BoutiqueSignManager(Boutique boutique)
	{
		this.plugin = boutique;
	}


	public Boolean isBoutiqueSign(Sign s)
	{
		Location signLoc = s.getBlock().getLocation();		
		//todo: mettre une fonction keygen ?
		//String signKey = signLoc.getBlockX() +  ":" + signLoc.getBlockY() + ":" + signLoc.getBlockZ() + ":" + signLoc.getWorld();		
		return _signs.containsKey(signLoc);			
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
		return l.getWorld().getName() + ":" + l.getBlockX() + ":" +  l.getBlockY() + ":" + l.getBlockZ(); 
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
	
	
	
	


	//private void signSetter(String[] lines, Player p, Block s) 
	
	private void signSetter(Block b, Player p, String[] lines) 
	{		
		p.sendMessage("dbg1");
		
		
		if(b==null) 
			return;
		
		BoutiqueSign bs = new BoutiqueSign();
		
		bs.setOwner(p);
		bs.setLocation(b.getLocation());
		bs.setLines(lines);

		p.sendMessage("dbg1 : line1 = " + bs.getLine1());
		p.sendMessage("dbg1 : line2 = " + bs.getLine2());
		p.sendMessage("dbg1 : line3 = " + bs.getLine3());
		p.sendMessage("dbg1 : line4 = " + bs.getLine4());
		
		p.sendMessage("dbg2 : type = " + bs.getType());
		
		// Vérifie les items / lignes
		
		
		if(bs.getType().compareToIgnoreCase(BoutiqueSignServer.getTypeStr()) == 0)
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
			
			p.sendMessage(plugName + "Panneau serveur ajouté à la liste :)");
			
			this.put(bs);
			
			//TODO : signmanager.addsign
			//TODO : prerender
			
			bs.setLine4(ChatColor.GREEN + "[Actif]");
			
			plugin.signmanager.saveGlobalSigns();
		}
		
		else if(bs.getType().compareToIgnoreCase(BoutiqueSignChest.getTypeStr()) == 0)
		{
			if (!PermissionsHandler.canSetPersonalSign(p))
			{
				p.sendMessage(PermissionsHandler.permissionErr);
				return;
			}
			
			if(!bs.checkLines(p))
			{
				return;
			}
			
			p.sendMessage(plugName + "Panneau joueur ajouté à la liste :)");
			
			this.put(bs);
			
			bs.setLine4(ChatColor.GREEN + "[Actif]");
			
			plugin.signmanager.saveGlobalSigns();
		}	
		else if(bs.getType().compareToIgnoreCase(BoutiqueSignWebAuction.getTypeStr()) == 0)
		{
			if (!PermissionsHandler.canSetWebAuctionSign(p))
			{
				p.sendMessage(PermissionsHandler.permissionErr);
				return;
			}
			
			if(!bs.checkLines(p))
				return;
			
			p.sendMessage(plugName + "Panneau web joueur ajouté à la liste :)");
			
			//TODO : signmanager.addsign
			//TODO : prerender
			this.put(bs);
			
			bs.setLine4(ChatColor.GREEN + "[Actif]");
			
			plugin.signmanager.saveGlobalSigns();
		}
		
		
		
	}
	
	
	public void displaySignInfo(Block b, Player p) 
	{
		
		p.sendMessage(plugName + "displaySignInfo");
		
		BoutiqueSign bs = getBoutiqueSign(b);
		
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
		
		p.sendMessage("dbg1: Type=" + bs.getType());
		
		
		//Type BoutiqueSignServer
		if(bs.getType().compareToIgnoreCase(BoutiqueSignServer.getTypeStr()) == 0)
		{
			p.sendMessage("Ce panneau fait partie du magasin du serveur.");
		}
		
		//Type BoutiqueSignChest
		else if(bs.getType().compareToIgnoreCase(BoutiqueSignChest.getTypeStr()) == 0)
		{
			p.sendMessage("Ce panneau fait partie du magasin de " + ChatColor.RED + signOwnerString + ChatColor.WHITE +  ".");			
			
			String signchest = bs.getChestString();			
			
			if(signchest.isEmpty()) 
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
			else if (!WebItemsOperator.containEnough(signOwner, id, damage, qty))
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
			
			if(WebItemsOperator.removeFromWebStock(signOwner, qty, id, damage))
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

	public static  boolean buyItem(BoutiqueSign bs, Player p)
	{
		Double costAmount = bs.getMoneyTo();
		String signOwner = bs.getOwnerString();
		Integer qty = bs.getQtyFrom();
		Integer id = bs.getItemFrom().itemId;
		Integer damage = bs.getItemFrom().itemDamage;
		
		//int econ = EconomyHandler.hasEnough(p.getName(), costAmount);
		
		//TODO changer plugname par chatprefix
		String plugName = "";
		
		if (!PlayerOperator.playerHasEnough( qty, id, damage, p))
		{
			p.sendMessage(plugName + PlayerOperator.playerStockErr);
			return false;
		}
		
		// Panneau "Serveur"
		if (bs.getSignTypeString().compareToIgnoreCase(BoutiqueSignServer.getTypeStr()) == 0)
		{
			//Enleve les objets dans l'inventaire du joueur
			PlayerOperator.removeFromPlayer(qty, id ,damage, p);
			
			//Ajoute les thunes au compte du joueur
			EconomyHandler.modifyMoney(p.getName(), (double) costAmount);
			
			//Enleve le propriétaire du panneau pour les logs (car panneau serveur) !
			signOwner = "";
		}
		
		// Panneau "webauctions"
		// Achete des objet, et les met dans le stock "webauctions"
		else if (bs.getSignTypeString().compareToIgnoreCase(BoutiqueSignWebAuction.getTypeStr()) == 0)
		{
			
			// Vérifie que l'acheteur à assez d'argent
			int econ = EconomyHandler.hasEnough(signOwner, costAmount);
			if (econ != 1)
			{
				p.sendMessage(plugName + EconomyHandler.getEconError(econ));
				return false;
			}
			
			// Ajoute les objets au stock WebAuctions
			if(!WebItemsOperator.addToWebStock(signOwner, id, damage, qty))
			{
				p.sendMessage(plugName + "Erreur lors de l'ajout dans les stocks web de "  + signOwner + " :(");
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
		else if (bs.getSignTypeString().compareToIgnoreCase(BoutiqueSignChest.getTypeStr()) == 0)
		{
			
			// Cherche le coffre relié au panneau
			Chest chest = bs.getChest();
			if(chest==null)	
			{
				//TODO:  symbole debug ici
				return false;
			}
				
			
			
			// Vérifie qu'il reste de la place dans le coffre pour stocker les objets à acheter
			if (!ChestOperator.hasEnoughSpace(qty,id,damage, chest))
			{
				p.sendMessage(plugName + ChestOperator.notEnoughSpaceErr);
				return false;
			}			
			
			// Vérifie que l'acheteur possède assez d'argent
			int econ = EconomyHandler.hasEnough(signOwner, costAmount);
			if (econ != 1)
			{
				p.sendMessage(plugName + EconomyHandler.getEconError(econ));
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
		}
		
		
		p.sendMessage(plugName + "Tu as maintenant " + EconomyHandler.playerHave(p.getName()) + ".");
		
		try 
		{
			Boutique.getInstance().db.logTransaction(p.getLocation(), p.getName(), signOwner, id, damage, qty, costAmount,"toto","titi");
		}
		catch (SQLException e) 
		{
			// TODO
			e.printStackTrace();
		} 
		catch (Exception e) 
		{
			// TODO
			e.printStackTrace();
		} 
		
		return true;
	}


	public static boolean tradeItems(BoutiqueSign bs, Player p)
	{
		return false;
		// TODO Auto-generated method stub
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
		this._signs.remove(location);
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


	/* Enregistre le coffre pour un BoutiqueSignChest */
	public void setChest(Sign sign, Chest chest, Player p)
	{
		BoutiqueSign bs = this.getBoutiqueSign(sign.getBlock());
		
		
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


	public void setOwner(Sign s, Player p, String newowner)
	{
		
		
		if (!isSign(s))
		{
			p.sendMessage(plugName + "Ce panneau est mal configuré. Reposes le correctement avant.");
			return;
		}
		
		
		
		Location location = s.getBlock().getLocation();
		
		
		BoutiqueSign bs = getBoutiqueSign(s.getBlock());
		if(bs==null)
			return;
		
		bs.setOwnerString(newowner);
		
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


	public void loadGlobalSignData()
	{
		plugin.fileio.loadGlobalSignData();
	}

	public void put(BoutiqueSign bs)
	{
		put(bs.getLocationString(), bs);		
	}

	public boolean isBoutiqueSign(Block clickedBlock)
	{
		// TODO Auto-generated method stub
		return haveLocation(clickedBlock.getLocation());
	}


	

	

}
