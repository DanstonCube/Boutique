package com.danstoncube.plugin.drzoid.Boutique;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;

import com.danstoncube.plugin.drzoid.Boutique.SignTypes.BoutiqueSign;
import com.miykeal.showCaseStandalone.Messaging;
import com.miykeal.showCaseStandalone.Properties;
import com.miykeal.showCaseStandalone.Shop;
import com.miykeal.showCaseStandalone.ShowCaseStandalone;
import com.miykeal.showCaseStandalone.Shop.Activities;

public class ShowCaseHandler
{

	public static ShowCaseStandalone showcase;

	public static void setupShowcase()
	{
		Boutique plugin = Boutique.getInstance();
		
		Plugin test = Bukkit.getServer().getPluginManager().getPlugin("ShowCaseStandalone");
		if (test != null) 
		{
			((ShowCaseStandalone)test).getClass();
			
			showcase = (ShowCaseStandalone)test;
			plugin.log.info("["+plugin.displayname+"] utilise ShowcaseStandalone");
			return;
		}
		
	}
	
	public static boolean isShowCaseOwner(Block b, Player player)
	{
		if(showcase==null)
			return false;
	
		return WorldGuardOperator.canBuild(player, b);
	}


	public static void remShowcase(Sign sign, String ShowcaseLoc, Player p)
	{
		if(showcase==null)
			return;
		
	}
	
	
	
	public static void setShowcase(Sign sign, String ShowcaseLoc, Player p)
	{
		if(showcase==null)
			return;
		
		BoutiqueSign bs = Boutique.getInstance().signmanager.getBoutiqueSign(sign.getBlock());
    	if(bs==null)
    		return;
    	
		Block b = p.getWorld().getBlockAt(BoutiqueSign.getLocationFromString(ShowcaseLoc));
		if(b==null)
			return;
		
		if(bs.getItemTo() == null && bs.getItemFrom() == null)
		{
			//pas d'objet a mettre en vitrine !
			return;
		}
		
        //Shop shop = new Shop(scs, a, 0, new MaterialData(15) ,  0.0, "serveur");
        
        //Shop.
        //Block b = null;
        //Player p = null;
        
        //Si c'est un shop existant, et que l'on est le proprio, on l'efface et on en refait un
        if(Shop.isShopBlock(b))
        {
        	removeshop(p,b);
        }
        else
        {
        	
        	String matData = "0:0";
        	Activities signActivity = Activities.SELL;
        	
        	
        	if(bs.getItemTo()!=null)
        	{
        		matData = bs.getItemTo().itemId + ":" + bs.getItemTo().itemDamage;
        	}
        	else if(bs.getItemFrom()!=null)
        	{
        		matData = bs.getItemFrom().itemId + ":" + bs.getItemFrom().itemDamage;
        	}
        	
        	if(bs.isBuySign())
        	{
        		signActivity = Activities.BUY;
        	}
        	
        	
        	try
			{
				MaterialData m = ShowCaseStandalone.getMaterialData(matData);
				Shop newShop = new Shop(showcase, signActivity, 0, m,  0.0, p.getName());
				createshop(newShop,p,b);
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
	
	/*
	* Create a shop
	*/
	private static void createshop (Shop p, Player player, Block b) 
	{
		if(showcase==null) return;
		
		//Shop p 					= this.removeTodo(player).shop;
		MaterialData material	= p.getMaterialData();
		Inventory inv 			= player.getInventory();
		ItemStack stack			= new ItemStack(material.getItemType(), p.getAmount(), material.getItemType().getMaxDurability(), material.getData());
		
		if (Shop.isShopBlock(b)) {
			Messaging.send(player, "`rError: Selected block is already a shop.");
			return;
		}
                
                if(Properties.forbiddenBlocks.contains(b.getType())){
                    Messaging.send(player, "`rYou cannot use that block as a shop.");
                    return;
                }
		
		if (p.getAtivitie().equals(Activities.SELL) && p.getAmount() > 0) 
		{
            if(inv.contains(material.getItemType(), p.getAmount()))
                if(material.getItemType().getMaxStackSize() > 1)
                    inv.removeItem(stack);
                else if(p.countSaleableItems(player) == p.getAmount())
                    inv.removeItem(stack);
                else 
                {
                    Messaging.send(player, "`rError: You do not have enough saleable items to create the");
                    Messaging.send(player, "`rshop.");
                    return;
                }
            else
            {
                Messaging.send(player, "`rError: You do not have enough items to create the shop.");
                return;
            }
		}
		
		p.setBlock		(b);
		p.show		 	();
		p.register		();
        
		Messaging.send(player,"`gVitrine mise à jour.");

	}
	
	
	private static void removeshop (Player player, Block b) 
	{
		if(showcase==null) return;
		
		Shop shop = Shop.getShopForBlock(b);
		
		if (shop == null) {
			Messaging.send(player, "`rThe block you have selected is not a shop.");
			return;
		}
		
		MaterialData material = shop.getMaterialData();
		
		if (player.getName().equals(shop.getOwner()) || showcase.hasPermission(player, "scs.admin")) 
		{
			HashMap<Integer, ItemStack> rest = new HashMap<Integer, ItemStack>();
			
			if (shop.getAmount() > 0) 
				rest = player.getInventory().addItem( new ItemStack(material.getItemType(), shop.getAmount(), material.getItemType().getMaxDurability(), material.getData()) );
			
			if (rest.size() > 0) 
			{
				shop.setAmount(shop.getAmount() - rest.get(0).getAmount());
				player.sendMessage("`rYou do not have enough space in your inventory for ALL of your shop's items. Drop some items and try again.");
				player.sendMessage("Left: "+shop.getAmount());
			} else 
			{
				shop.hidd();
				shop.delete();
				
				Messaging.send(player, "`gSuccessfully removed shop.");
			}
		} 
		else
			Messaging.send(player, "`rOnly the owner, or an admin, can remove a shop.");
			
	}

	public static boolean isNull()
	{
		return showcase == null;
	}
}
