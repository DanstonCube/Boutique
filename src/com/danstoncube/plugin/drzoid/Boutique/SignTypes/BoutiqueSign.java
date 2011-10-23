package com.danstoncube.plugin.drzoid.Boutique.SignTypes;

import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.danstoncube.plugin.drzoid.Boutique.Boutique;
import com.danstoncube.plugin.drzoid.Boutique.BoutiqueItem;
import com.danstoncube.plugin.drzoid.Boutique.BoutiqueItems;
import com.danstoncube.plugin.drzoid.Boutique.BoutiqueSignManager;

public abstract class BoutiqueSign
{
	private String[] _lines = new String[3];
	
	private String _stype = "err";
	private Location _loc = null;
	private Sign _sign = null;
	private Player _owner = null;
	private Boolean _enabled = true;
	
	
	
	//
	private BoutiqueItem _itemFrom;
	private BoutiqueItem _itemTo;
	private Integer _qtyFrom;
	private Integer _qtyTo;
	private Double _moneyFrom;
	private Double _moneyTo;
	
	
	BoutiqueSign(Sign sign, Player owner, BoutiqueSign type)
	{
		this._sign = sign;
		this._loc = sign.getBlock().getLocation();
		this._owner = owner;
		
		this._stype = type.getSignTypeString();
		
		this._itemFrom = null;
		this._itemTo = null;
		this._qtyFrom = null;
		this._qtyTo = null;
		this._moneyFrom = null;
		this._moneyTo = null;
		
		String[] lines = new String[3];
		lines = this._sign.getLines();
		
		this.setLine1(lines[0]);
		this.setLine2(lines[1]);
		this.setLine3(lines[2]);
		this.setLine4(lines[3]);
	}

	
	
	
	

	public Boolean isEnabled()
	{
		return this._enabled;
	}
	
	
	/* ligne 1 */
	
	public String getLine1()
	{
		return this._lines[0];
	}
	
	private void setLine1(String line1) 
	{
		this._lines[0] = line1;
		
		
		
		
	}
	
	/* ligne 2 */
	public String getLine2()
	{
		return this._lines[1];
	}
	
	
	
	
	private void setLine2(String line2) 
	{
		this._lines[1] = line2;		
		
		String[] splited = line2.split(":");
		
		//Teste validitée découpage
		if(splited.length < 2 || splited.length > 3) 
			return;
		
		
		Double moneyFrom = null;
		Integer itemId = null;
		Integer itemDamage = null;
		Integer itemQty = null;
		
		
		//FROM = MONEY
		//TODO: extern key $
		if(splited[1]=="$")
		{
			moneyFrom = Double.parseDouble(splited[0]);
			this._moneyFrom = moneyFrom;
			this._itemFrom = null;
			this._qtyFrom = null;
			return;
		}
		//FROM = ITEM
		else
		{
			//item = new BoutiqueItem();
			
			itemQty = Integer.parseInt(splited[0]);
			itemId =  Integer.parseInt(splited[1]);
			itemDamage = (splited.length == 3) ? Integer.parseInt(splited[2]) : null;
			
			this._itemFrom = new BoutiqueItem(itemId,itemDamage);
			this._qtyFrom = itemQty;
			this._moneyFrom = null;			
		}
			
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/* ligne 3 */
	public String getLine3()
	{
		return this._lines[2];
	}
	private void setLine3(String line3) 
	{
		this._lines[2] = line3;		
	}

	/* ligne 4 */
	public String getLine4()
	{
		return this._lines[3];
	}
	public void setLine4(String line4) 
	{
		this._lines[3] = line4;		
	}
	
	
	/* Location */
	
	public Location getLocation()
	{
		return this._loc;
	}
	
	public String getLocationString()
	{
		return this._loc.getBlockX() + ":" + this._loc.getBlockY() + ":" +  this._loc.getBlockZ() + ":" + this._loc.getWorld();
	}
	
	/* Owner */

	public Player getOwner()
	{
		return this._owner;
	}
	
	public String getOwnerString()
	{
		return this._owner.getName();
	}
	
	
	/* Sign Type */
	
	//a redeclarer
	public String getSignTypeString()
	{
		return this._stype;
	}
	
	
	
	public Boolean checkLine1()
	{
		//TODO;
		return true;
	}
	
	
	public Boolean checkLine2()
	{
		String[] splited = this.getLine2().split(":");
		
		if(splited.length < 2 || splited.length > 3)
			return false;
				
		return true;
	}
		
	public Boolean checkLine3()
	{
		String[] splited = this.getLine2().split(":");
		
		if(splited.length < 2 || splited.length > 3)
			return false;
				
		return true;
	}

	
	/* itemFrom / itemTo */
	
	public BoutiqueItem getItemFrom()
	{
		return this._itemFrom;
	}
	
	public BoutiqueItem getItemTo()
	{
		return this._itemTo;
	}
	
	
	/* moneyFrom / moneyTo */
	
	public Double getMoneyFrom() {
		return this._moneyFrom;
	}
	
	public Double getMoneyTo() {
		return this._moneyTo;
	}

	
	
	/* qtyFrom / qtyTo */
	
	public Integer getQtyFrom() {
		return this._qtyFrom;
	}
	
	public Integer getQtyTo() {
		return this._qtyTo;
	}

	
	public void Render()
	{
		// TODO Auto-generated method stub
	}

	public boolean isFreebiesSign()
	{
		return this._moneyFrom == 0 && this._itemFrom == null;
	}

	public boolean isDonationSign()
	{
		return (this._moneyFrom > 0 || this._itemFrom != null) && (this._moneyTo == 0 && this._itemTo == null);
	}

	public boolean isSellSign()
	{
		return (this._moneyFrom > 0 &&  this._itemFrom == null && this._itemTo != null);
	}
	
	public boolean isBuySign()
	{
		return (this._moneyTo > 0 &&  this._itemTo == null && this._itemFrom != null);
	}

	public boolean isTradeSign()
	{
		return (this._itemTo != null && this._itemFrom != null);
	}


	public boolean doAction(Player p)
	{
		//FREEBIES
		if(this.isFreebiesSign())
		{
			return BoutiqueSignManager.giveFree(this, p);
		}
		//DONATION
		else if(this.isDonationSign())
		{
			return BoutiqueSignManager.getDonation(this, p);			
		}
		//VENTE
		else if(this.isSellSign())
		{
			return BoutiqueSignManager.sellItem(this, p);
		}
		//ACHAT
		else if(this.isBuySign())
		{
			return BoutiqueSignManager.buyItem(this, p);
		}
		//ECHANGE
		else if (this.isTradeSign())
		{
			return BoutiqueSignManager.tradeItems(this, p);
		}
		
		//TODO: chatprefix + VroumvroumERR
		p.sendMessage(Boutique.getInstance().name + "VROUM VROUM, c'est quoi ce panneau de merde ???" );
		
		return false;
	}


	public boolean checkLines(Player p)
	{
		Integer qtyFrom = null;
		Integer qtyTo = null;
		BoutiqueItem itemTo = null;
		BoutiqueItem itemFrom = null;
		Double moneyFrom = null;
		Double moneyTo = null;
		
		//teste ligne 2
		if (!this.checkLine2()) 
		{	
			p.sendMessage("Erreur sur la deuxième ligne du panneau");
			return false;
		}
		else
		{
			itemFrom = this.getItemFrom();
			moneyFrom = this.getMoneyFrom();
			
			if(itemFrom != null)
			{
				if(!BoutiqueItems.isValidItem(itemFrom.itemId,itemFrom.itemDamage))
				{
					p.sendMessage("Item incorrect ou interdit sur la deuxième ligne");
					return false;
				}
				
				qtyFrom = this.getQtyFrom(); 
				if(qtyFrom == null || qtyFrom < 0 || qtyFrom > 64)
				{
					p.sendMessage("Quantité incorrecte sur la deuxième ligne");
					return false;
				}
			}
			
			if(moneyFrom == null)
			{
				p.sendMessage("Somme d'argent incorrecte sur la deuxième ligne");
				return false;
			}
			
			
		}
		
		//teste ligne 3
		if (!this.checkLine3()) 
		{
			p.sendMessage("Erreur sur la troisième ligne du panneau");
			return false;
		}		
		else
		{
			itemTo = this.getItemTo();
			moneyTo = this.getMoneyTo();
					
			if(itemTo != null)
			{
				if(!BoutiqueItems.isValidItem(itemTo.itemId,itemTo.itemDamage))
				{
					p.sendMessage("Item incorrect ou interdit sur la troisième ligne");
					return false;
				}
				
				qtyTo = this.getQtyTo(); 
				if(qtyTo == null || qtyTo < 0 || qtyTo > 64)
				{
					p.sendMessage("Quantité incorrecte sur la troisième ligne");
					return false;
				}
			}
			
			if(moneyTo == null)
			{
				p.sendMessage("Somme d'argent incorrecte sur la troisième ligne");
				return false;
			}
		}
		
		return true;
	}



	private Chest _chest = null;

	public Chest getChest()
	{
		// TODO Auto-generated method stub
		return this._chest;
	} 
	
	public void setChest(Chest chest)
	{
		this._chest = chest;
	}


	
	
}
