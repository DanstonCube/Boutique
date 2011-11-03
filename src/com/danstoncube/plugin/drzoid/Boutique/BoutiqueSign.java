package com.danstoncube.plugin.drzoid.Boutique;



import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.danstoncube.plugin.drzoid.Boutique.SignTypes.BoutiqueSignChest;
import com.danstoncube.plugin.drzoid.Boutique.SignTypes.BoutiqueSignServer;
import com.danstoncube.plugin.drzoid.Boutique.SignTypes.BoutiqueSignWebAuction;


public class BoutiqueSign
{
	private String[] _lines = new String[4];
	
	private String _type = "err";
	private String _location = "";
	private String _owner = "";
	private String _chest = "";
	private Boolean _enabled = true;
	
	
	// calculé
	private BoutiqueItem _itemFrom;
	private BoutiqueItem _itemTo;
	private Integer _qtyFrom;
	private Integer _qtyTo;
	private Double _moneyFrom;
	private Double _moneyTo;
	
	
	public BoutiqueSign()
	{
		this._itemFrom = null;
		this._itemTo = null;
		this._qtyFrom = null;
		this._qtyTo = null;
		this._moneyFrom = null;
		this._moneyTo = null;
	}
	
	
	BoutiqueSign(Sign sign, Player owner, String type)
	{
		super();
		
		this.setLocation(sign.getBlock().getLocation());
		this.setOwner(owner);
		this.setType(type);
		this.setLines(sign.getLines());			
	}

	
	
	
	

	private void setType(String type)
	{
		this._type = type;
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
	
	public String getLine2()
	{
		return this._lines[1];
	}
	
	public String getLine3()
	{
		return this._lines[2];
	}
	
	public String getLine4()
	{
		return this._lines[3];
	}
	
	
	
	
	
	private void setLine1(String line1) 
	{
		this._lines[0] = line1;
		this.setType(line1);
	}
	
	private void setLine2(String line2) 
	{
		this._lines[1] = line2;		
		
		String[] splited = line2.split(":"); //$NON-NLS-1$
		
		//Teste validitée découpage
		if(splited.length < 2 || splited.length > 3) 
			return;
		
		
		Double moneyFrom = null;
		Integer itemId = null;
		Integer itemDamage = null;
		Integer itemQty = null;
		
		
		//FROM = MONEY
		//TODO: extern key $
		if(splited[1].compareTo("$")==0) //$NON-NLS-1$
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
	
	
	private void setLine3(String line3) 
	{
		this._lines[2] = line3;	
		
		String[] splited = line3.split(":"); //$NON-NLS-1$
		
		//Teste validitée découpage
		if(splited.length < 2 || splited.length > 3)
		{
			//TODO virer debug
			//Bukkit.getServer().getLogger().info("DBG: taille incorrecte pour : " + line3);
			return;
		}
		
		Double moneyTo = null;
		Integer itemId = null;
		Integer itemDamage = null;
		Integer itemQty = null;
		
		//TO = MONEY
		//TODO: extern key $
		if(splited[1].compareTo("$")==0) //$NON-NLS-1$
		{
			moneyTo = Double.parseDouble(splited[0]);
			this._moneyTo = moneyTo;
			this._itemTo = null;
			this._qtyTo = null;
			return;
		}
		//TO = ITEM
		else
		{
			//item = new BoutiqueItem();
			
			itemQty = Integer.parseInt(splited[0]);
			itemId =  Integer.parseInt(splited[1]);
			itemDamage = (splited.length == 3) ? Integer.parseInt(splited[2]) : null;
			
			this._itemTo = new BoutiqueItem(itemId,itemDamage);
			this._qtyTo = itemQty;
			this._moneyTo = null;			
		}
					
		
		
	}
	
	public void setLine4(String line4) 
	{
		this._lines[3] = line4;
	}
	
	
	
	
	/* Location */
	
	
	public void setLocation(Location loc)
	{
		//this._loc = loc;
		this._location = getLocationString(loc); 
	}
	
	public void setLocation(String loc)
	{
		this._location = loc;
	}
	
	
	
	
	
	public static String getLocationString(Location loc)
	{
		return loc.getWorld().getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" +  loc.getBlockZ();  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	
	
	
	
	/* Owner */
	
	
	
	public String getOwnerString()
	{
		return this._owner;
	}
	
	public void setOwner(Player player)
	{
		this._owner = player.getName();
	}
	
	public void setOwnerString(String playerName)
	{
		this._owner = playerName;
	}
	
	
	

	
	
	
	public Boolean checkLine1()
	{
		//TODO;
		return true;
	}
	
	
	public Boolean checkLine2()
	{
		String[] splited = this.getLine2().split(":"); //$NON-NLS-1$
		
		if(splited.length < 2 || splited.length > 3)
			return false;
				
		return true;
	}
		
	public Boolean checkLine3()
	{
		String[] splited = this.getLine2().split(":"); //$NON-NLS-1$
		
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
		//FREEBIES
		if(this.isFreebiesSign())
		{
			RenderFreeSign();
		}
		//DONATION
		else if(this.isDonationSign())
		{
			RenderDonationSign();
		}
		//VENTE
		else if(this.isSellSign())
		{
			RenderSellSign();
		}
		//ACHAT
		else if(this.isBuySign())
		{
			RenderBuySign();
		}
		//ECHANGE
		else if (this.isTradeSign())
		{
			RenderTradeSign();
		}
		else
		{
			//TODO: chatprefix + VroumvroumERR
			//p.sendMessage(Boutique.getInstance().name + "VROUM VROUM, c'est quoi ce panneau de merde ???" );
			RenderDummySign();
			return;
		}
	}




	private void RenderDonationSign()
	{
		if(this.isSignServer())
		{
			BoutiqueSign.RenderDonationSignServer(this);
		}
		else if(this.isSignChest() ||  this.isSignWebAuction())
		{
			BoutiqueSign.RenderDonationSignPersonal(this);
		}
		else
		{
			BoutiqueSign.RenderDonationSignDummy(this);
		}
	}


	private static void RenderDonationSignPersonal(BoutiqueSign bs)
	{
		// TODO Auto-generated method stub
		Sign s = bs.getSign();
		if(s==null) return;
		
		String getAmount = ""; //$NON-NLS-1$
		String getTypeText = ""; //$NON-NLS-1$
		
		s.setLine(0, ChatColor.LIGHT_PURPLE + Messages.getString("BoutiqueSign.DONATION")); //$NON-NLS-1$
		// Argent ou item ?	
		if(bs.getItemFrom() != null)
		{
			getAmount = bs.getQtyFrom().toString();
			getTypeText = bs.getItemFrom().itemShortname;
			
			s.setLine(1, ChatColor.WHITE + Messages.getString("BoutiqueSign.BATCHSOFF") + ChatColor.AQUA + Messages.getString("BoutiqueSign.QTY") + getAmount ); //ChatColor.BLACK + giveTypeText);	 //$NON-NLS-1$ //$NON-NLS-2$
			s.setLine(2, ChatColor.AQUA + getTypeText);
		}
		else if(bs.getMoneyFrom() != null)
		{
			getAmount = formatMoney(bs.getMoneyFrom());
			getTypeText = formatCurrency(bs.getMoneyFrom());
			
			//s.setLine(1, ChatColor.WHITE + "Don de "); //ChatColor.BLACK + giveTypeText);	
			s.setLine(1, ChatColor.YELLOW + getAmount + " " + getTypeText); //$NON-NLS-1$
			s.setLine(2, ChatColor.WHITE + Messages.getString("BoutiqueSign.FOR")); //$NON-NLS-1$
			s.setLine(3, ChatColor.AQUA + bs.getOwnerString());
		}
				
		s.update();
	}


	private static void RenderDonationSignServer(BoutiqueSign bs)
	{
		Sign s = bs.getSign();
		if(s==null) return;
		
		Integer getAmount = bs.getQtyFrom();
		String getTypeText = bs.getItemFrom().itemShortname;
	
		s.setLine(0, ChatColor.GOLD + "[" + ChatColor.LIGHT_PURPLE + Messages.getString("BoutiqueSign.DONATION") + ChatColor.GOLD + "]"); // + ChatColor.WHITE + " de lots de"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		s.setLine(1, ChatColor.WHITE + Messages.getString("BoutiqueSign.BATCHSOFFCAP") + ChatColor.AQUA + Messages.getString("BoutiqueSign.QTY") + getAmount ); //ChatColor.BLACK + giveTypeText);			 //$NON-NLS-1$ //$NON-NLS-2$
		s.setLine(2, ChatColor.AQUA + getTypeText);
		s.setLine(3, ChatColor.WHITE + ""); //$NON-NLS-1$
		s.update();
	}
	
	private static void RenderDonationSignDummy(BoutiqueSign bs)
	{
		Sign s = bs.getSign();
		if(s==null) return;
		
		s.setLine(3, ChatColor.RED + "[Err. type]"); //$NON-NLS-1$
		s.update();
	}

	private void RenderTradeSign()
	{
		if(this.isSignServer())
		{
			BoutiqueSign.RenderTradeSignServer(this);
		}
		else if(this.isSignChest())
		{
			BoutiqueSign.RenderTradeSignChest(this);
		}
		else if(this.isSignWebAuction())
		{
			BoutiqueSign.RenderTradeSignWebAuction(this);
		}
	}
	

	private static void RenderTradeSignWebAuction(BoutiqueSign bs)
	{
		Sign s = bs.getSign();
	
		
		if(s==null) 
			return;
		
		String giveTypeText = bs.getItemFrom().itemShortname;
		String getTypeText = bs.getItemTo().itemShortname;
				
		int giveAmount = bs.getQtyFrom();
		int getAmount = bs.getQtyTo();
		int getType = bs.getItemTo().itemId;
		int getDamage = bs.getItemTo().itemDamage;
		

		//compte le nombre de stacks dispos au total dans le chest						
		int lots = (int)(WebItemsOperator.contains(bs.getOwnerString(), getType, getDamage) / getAmount);		
		
		//couleur suivant la quantité
		String strLot = "";						 //$NON-NLS-1$
		if(lots < 0)		strLot = ChatColor.DARK_PURPLE + "?" + ChatColor.WHITE; //$NON-NLS-1$
		else if(lots == 0)	strLot = ChatColor.RED + String.valueOf(lots) + ChatColor.WHITE;
		else if(lots < 5)	strLot = ChatColor.YELLOW + String.valueOf(lots) + ChatColor.WHITE;
		else 				strLot = ChatColor.DARK_GREEN + String.valueOf(lots) + ChatColor.WHITE;
		
		if(lots > 1) 		strLot += Messages.getString("BoutiqueSign.BATCHS"); //$NON-NLS-1$
		else				strLot += Messages.getString("BoutiqueSign.BATCH"); //$NON-NLS-1$
			
		s.setLine(0, ChatColor.YELLOW + Messages.getString("BoutiqueSign.TRADE")); //$NON-NLS-1$
		s.setLine(1, ChatColor.AQUA + giveTypeText + Messages.getString("BoutiqueSign.QTY") +" "+ giveAmount); //$NON-NLS-1$
		s.setLine(2, ChatColor.WHITE + Messages.getString("BoutiqueSign.VERSUS") +" "+ strLot + Messages.getString("BoutiqueSign.OF")); //$NON-NLS-1$ //$NON-NLS-2$
		s.setLine(3, ChatColor.AQUA + getTypeText + Messages.getString("BoutiqueSign.QTY") + " " + getAmount); //$NON-NLS-1$
		s.update();
		
	}


	private static void RenderTradeSignServer(BoutiqueSign bs)
	{
		Sign s = bs.getSign();
		if(s==null) return;

		String giveTypeText = bs.getItemFrom().itemShortname;
		String getTypeText = bs.getItemTo().itemShortname;
				
		int giveAmount = bs.getQtyFrom();
		int getAmount = bs.getQtyTo();
		
		s.setLine(0, ChatColor.GOLD + "[" + ChatColor.YELLOW + Messages.getString("BoutiqueSign.TRADE") + ChatColor.GOLD + "]");
		s.setLine(1, ChatColor.AQUA + giveTypeText + Messages.getString("BoutiqueSign.QTY") + " " + giveAmount);
		s.setLine(2, ChatColor.WHITE + Messages.getString("BoutiqueSign.VERSUS")); 
		s.setLine(3, ChatColor.AQUA + getTypeText + Messages.getString("BoutiqueSign.QTY") + " " + getAmount);
		s.update();
	}
	
	private static void RenderTradeSignChest(BoutiqueSign bs)
	{
		Sign s = bs.getSign();
		Chest c = bs.getChest();
		
		if(s==null) 
			return;
		
		String giveTypeText = bs.getItemFrom().itemShortname;
		String getTypeText = bs.getItemTo().itemShortname;
				
		int giveAmount = bs.getQtyFrom();
		int getAmount = bs.getQtyTo();
		int getType = bs.getItemTo().itemId;
		int getDamage = bs.getItemTo().itemDamage;
		

		//compte le nombre de stacks dispos au total dans le chest						
		int lots = (c!=null) ? (int)(ChestOperator.contains(getType, getDamage, c) / getAmount) : - 1;		
		
		//couleur suivant la quantité
		String strLot = "";						 //$NON-NLS-1$
		if(lots < 0)		strLot = ChatColor.DARK_PURPLE + "?" + ChatColor.WHITE; //$NON-NLS-1$
		else if(lots == 0)	strLot = ChatColor.RED + String.valueOf(lots) + ChatColor.WHITE;
		else if(lots < 5)	strLot = ChatColor.YELLOW + String.valueOf(lots) + ChatColor.WHITE;
		else 				strLot = ChatColor.DARK_GREEN + String.valueOf(lots) + ChatColor.WHITE;
		
		if(lots > 1) 		strLot += Messages.getString("BoutiqueSign.BATCHS"); //$NON-NLS-1$
		else				strLot += Messages.getString("BoutiqueSign.BATCH"); //$NON-NLS-1$
			
		s.setLine(0, ChatColor.YELLOW + Messages.getString("BoutiqueSign.TRADE")); //$NON-NLS-1$
		s.setLine(1, ChatColor.AQUA + giveTypeText + Messages.getString("BoutiqueSign.QTY") + " " + giveAmount); //$NON-NLS-1$
		s.setLine(2, ChatColor.WHITE + Messages.getString("BoutiqueSign.VERSUS") + " " + strLot + Messages.getString("BoutiqueSign.OF")); //$NON-NLS-1$ //$NON-NLS-2$
		s.setLine(3, ChatColor.AQUA + getTypeText + Messages.getString("BoutiqueSign.QTY") + " " + getAmount); //$NON-NLS-1$
		s.update();
	}
	
	private void RenderDummySign()
	{
	}
	
	
	
	
	public void RenderBuySign()
	{
		
		
		if(this.isSignServer())
		{
			BoutiqueSign.RenderBuySignServer(this);
		}
		else if(this.isSignChest() ||  this.isSignWebAuction())
		{
			BoutiqueSign.RenderBuySignPersonal(this);
		}
		else
		{
			BoutiqueSign.RenderBuySignDummy(this);
		}
		
	}

	private static void RenderBuySignServer(BoutiqueSign bs)
	{
		Sign s = bs.getSign();
		if(s==null) return;
		
		Integer giveAmount = bs.getQtyFrom();
		String giveTypeText = bs.getItemFrom().itemShortname;
		Double getAmount = bs.getMoneyTo();

		s.setLine(0, ChatColor.GOLD + "[" + ChatColor.LIGHT_PURPLE + Messages.getString("BoutiqueSign.BUY") + ChatColor.GOLD + "]"); // + ChatColor.WHITE + " de lots de"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		s.setLine(1, ChatColor.WHITE + Messages.getString("BoutiqueSign.BATCHSOFFCAP") + ChatColor.AQUA + Messages.getString("BoutiqueSign.QTY") + giveAmount ); //ChatColor.BLACK + giveTypeText);			 //$NON-NLS-1$ //$NON-NLS-2$
		s.setLine(2, ChatColor.AQUA + giveTypeText);
		s.setLine(3, ChatColor.WHITE + Messages.getString("BoutiqueSign.FOR") + " " + ChatColor.YELLOW + formatMoney(getAmount) + " " + ChatColor.GOLD + formatCurrency(getAmount) + ChatColor.WHITE + Messages.getString("BoutiqueSign.FOREACHBATCH")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		s.update();
	}


	

	


	private static void RenderBuySignPersonal(BoutiqueSign bs)
	{
		Sign s = bs.getSign();
		if(s==null) return;
		
		
		Integer giveAmount = bs.getQtyFrom();
		String giveTypeText = bs.getItemFrom().itemShortname;
		Double getAmount = bs.getMoneyTo();
		
		s.setLine(0, ChatColor.LIGHT_PURPLE + Messages.getString("BoutiqueSign.BUY")); 
		s.setLine(1, ChatColor.WHITE + Messages.getString("BoutiqueSign.BATCHSOFFCAP") + ChatColor.AQUA + Messages.getString("BoutiqueSign.QTY") + giveAmount );
		s.setLine(2, ChatColor.AQUA + giveTypeText);
		s.setLine(3, ChatColor.WHITE + Messages.getString("BoutiqueSign.FOR") + " " + ChatColor.YELLOW + formatMoney(getAmount) + formatCurrency(getAmount) + " " + ChatColor.WHITE + Messages.getString("BoutiqueSign.FOREACHBATCH"));
		
		if(bs.isSignChest() && bs.getChest() == null)
		{
			s.setLine(3,ChatColor.RED + Messages.getString("BoutiqueSign.NOCHEST"));
		}
		
		
		
		s.update();
	}

	private static void RenderBuySignDummy(BoutiqueSign bs)
	{
		Sign s = bs.getSign();
		if(s==null) return;
		
		s.setLine(3, ChatColor.RED + "[Err. type]");
		s.update();
	}

	public static String formatMoney(Double money)
	{
		float y = (float) (money * 10.0);
		int i =(int) y;
		int mod = i % 10;
		 
		if(mod == 0)
		   return String.valueOf(i/10);

		return String.valueOf(i/10) + "." + mod;
	}
	

	
	
	
	
	public void RenderFreeSign()
	{			
		if(this.isSignServer())
		{
			BoutiqueSign.RenderFreeSignServer(this);
		}
		else if(this.isSignChest())
		{
			BoutiqueSign.RenderFreeSignChest(this);
		}
		else if(this.isSignWebAuction())
		{
			BoutiqueSign.RenderFreeSignWebAuction(this);
		}
		else
		{
			BoutiqueSign.RenderFreeSignDummy(this);
		}
	}
	
	
	
	
	
	

	private static void RenderFreeSignDummy(BoutiqueSign boutiqueSign)
	{
	}


	private static void RenderFreeSignWebAuction(BoutiqueSign bs)
	{
		Sign s = bs.getSign();
		
		if(s==null) 
			return;
		
		Integer giveType = bs.getItemTo().itemId;
		Integer giveDamage = bs.getItemTo().itemDamage;
		Integer giveAmount = bs.getQtyTo();
		String giveTypeText = bs.getItemTo().itemShortname;
		
		
		//compte le nombre de stacks dispos au total dans le chest						
		int lots = (int)(WebItemsOperator.contains(bs.getOwnerString(), giveType, giveDamage) / giveAmount);		
		
		//couleur suivant la quantité
		String strLot = "";	 //$NON-NLS-1$
		
		if(lots < 0)			strLot = ChatColor.DARK_PURPLE + "?" + ChatColor.BLACK; //$NON-NLS-1$
		else if(lots == 0)		strLot = ChatColor.RED + String.valueOf(lots) + ChatColor.BLACK;
		else if(lots < 5)		strLot = ChatColor.YELLOW + String.valueOf(lots) + ChatColor.BLACK;
		else 					strLot = ChatColor.DARK_GREEN + String.valueOf(lots) + ChatColor.BLACK;
		
		String strNbLot = ""; //$NON-NLS-1$
		if(lots > 1)	strNbLot = Messages.getString("BoutiqueSign.BATCHSOFFCAP"); //$NON-NLS-1$
		else			strNbLot = Messages.getString("BoutiqueSign.BATCHOFFCAP"); //$NON-NLS-1$
		
		s.setLine(0, ChatColor.GREEN + Messages.getString("BoutiqueSign.FREE") + " " + strLot); // + ChatColor.WHITE + " de lots de"); //$NON-NLS-1$
		s.setLine(1, ChatColor.WHITE + strNbLot + ChatColor.AQUA + Messages.getString("BoutiqueSign.QTY") + giveAmount ); //ChatColor.BLACK + giveTypeText); //$NON-NLS-1$
		s.setLine(2, ChatColor.AQUA + giveTypeText);
		s.setLine(3, ""); //$NON-NLS-1$
		s.update();
	}


	private static void RenderFreeSignChest(BoutiqueSign bs)
	{
		Sign s = bs.getSign();
		Chest c = bs.getChest();
		
		if(s==null) 
			return;
		
		Integer giveType = bs.getItemTo().itemId;
		Integer giveDamage = bs.getItemTo().itemDamage;
		Integer giveAmount = bs.getQtyTo();
		String giveTypeText = bs.getItemTo().itemShortname;
		
		
		//compte le nombre de stacks dispos au total dans le chest						
		int lots = (c!=null) ? (int)(ChestOperator.contains(giveType, giveDamage, c) / giveAmount) : -1;		
		
		//couleur suivant la quantité
		String strLot = "";	 //$NON-NLS-1$
		
		if(lots < 0)			strLot = ChatColor.DARK_PURPLE + "?" + ChatColor.BLACK; //$NON-NLS-1$
		else if(lots == 0)		strLot = ChatColor.RED + String.valueOf(lots) + ChatColor.BLACK;
		else if(lots < 5)		strLot = ChatColor.YELLOW + String.valueOf(lots) + ChatColor.BLACK;
		else 					strLot = ChatColor.DARK_GREEN + String.valueOf(lots) + ChatColor.BLACK;
		
		String strNbLot = ""; //$NON-NLS-1$
		if(lots > 1)	strNbLot = Messages.getString("BoutiqueSign.BATCHSOFFCAP"); //$NON-NLS-1$
		else			strNbLot = Messages.getString("BoutiqueSign.BATCHOFFCAP"); //$NON-NLS-1$
		
		s.setLine(0, ChatColor.GREEN + Messages.getString("BoutiqueSign.FREE") + " " + strLot); // + ChatColor.WHITE + " de lots de"); //$NON-NLS-1$
		s.setLine(1, ChatColor.WHITE + strNbLot + ChatColor.AQUA + Messages.getString("BoutiqueSign.QTY") + giveAmount ); //ChatColor.BLACK + giveTypeText); //$NON-NLS-1$
		s.setLine(2, ChatColor.AQUA + giveTypeText);
		s.setLine(3, ""); //$NON-NLS-1$
		
		if(c==null)
		{
			s.setLine(3,ChatColor.RED + Messages.getString("BoutiqueSign.NOCHEST")); //$NON-NLS-1$
		}
		
		
		s.update();
		
	}


	private static void RenderFreeSignServer(BoutiqueSign bs)
	{
		Sign s = bs.getSign();
		if(s==null) return;
		
		Integer giveAmount = bs.getQtyTo();
		String giveTypeText = bs.getItemTo().itemShortname;
		
		s.setLine(0, ChatColor.GOLD + "[" + ChatColor.GREEN + Messages.getString("BoutiqueSign.FREE") + ChatColor.GOLD + "]"); // + ChatColor.WHITE + " de lots de"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		s.setLine(1, ChatColor.WHITE + Messages.getString("BoutiqueSign.BATCHSOFFCAP") + ChatColor.AQUA + Messages.getString("BoutiqueSign.QTY") + giveAmount ); //ChatColor.BLACK + giveTypeText);			 //$NON-NLS-1$ //$NON-NLS-2$
		s.setLine(2, ChatColor.AQUA + giveTypeText);
		s.setLine(3, "");
		s.update();
	}
	
	

	
	public void RenderSellSign()
	{			
		if(this.isSignServer())
		{
			BoutiqueSign.RenderSellSignServer(this);
		}
		else if(this.isSignChest())
		{
			BoutiqueSign.RenderSellSignChest(this);
		}
		else if(this.isSignWebAuction())
		{
			BoutiqueSign.RenderSellSignWebAuction(this);
		}
		else
		{
			BoutiqueSign.RenderSellSignDummy(this);
		}
	}
	
	
	private static void RenderSellSignDummy(BoutiqueSign bs)
	{
		Sign s = bs.getSign();
		if(s==null) return;
		
		s.setLine(3, ChatColor.RED + "[Err. type]"); //$NON-NLS-1$
		s.update();
	}


	private static void RenderSellSignWebAuction(BoutiqueSign bs)
	{
		Sign s = bs.getSign();
		if(s==null)
			return;
		
		Integer giveType = bs.getItemTo().itemId;
		Integer giveDamage = bs.getItemTo().itemDamage;
		Integer giveAmount = bs.getQtyTo();
		String player  = bs.getOwnerString();
		String giveTypeText = bs.getItemTo().itemShortname;
		
		Double getAmount = bs.getMoneyFrom();
		
		
		
		//compte le nombre de stacks dispos au total dans le chest						
		int lots = (int)(WebItemsOperator.contains(player, giveType, giveDamage) / giveAmount);		
		
		//couleur suivant la quantité
		String strLot = "";	 //$NON-NLS-1$
		
		if(lots < 0)			strLot = ChatColor.DARK_PURPLE + "?" + ChatColor.BLACK; //$NON-NLS-1$
		else if(lots == 0)		strLot = ChatColor.RED + String.valueOf(lots) + ChatColor.BLACK;
		else if(lots < 5)		strLot = ChatColor.YELLOW + String.valueOf(lots) + ChatColor.BLACK;
		else 					strLot = ChatColor.DARK_GREEN + String.valueOf(lots) + ChatColor.BLACK;
		
		String strNbLot = ""; //$NON-NLS-1$
		if(lots > 1)	strNbLot = Messages.getString("BoutiqueSign.BATCHSOFFCAP"); //$NON-NLS-1$
		else			strNbLot = Messages.getString("BoutiqueSign.BATCHOFFCAP"); //$NON-NLS-1$
		
		String strAmount = ChatColor.YELLOW + formatMoney(getAmount) + formatCurrency(getAmount);
		
		//mise a jour du panneau
		
		s.setLine(0, ChatColor.GREEN + Messages.getString("BoutiqueSign.SELL") + " "+ strLot); // + ChatColor.WHITE + " de lots de"); //$NON-NLS-1$
		s.setLine(1, ChatColor.WHITE + strNbLot + ChatColor.AQUA + Messages.getString("BoutiqueSign.QTY") + giveAmount ); //ChatColor.BLACK + giveTypeText);			 //$NON-NLS-1$
		s.setLine(2, ChatColor.AQUA + giveTypeText);
		s.setLine(3, strAmount + ChatColor.WHITE + Messages.getString("BoutiqueSign.FOREACHBATCH")); //$NON-NLS-1$
		s.update();
	}


	private static void RenderSellSignServer(BoutiqueSign bs)
	{	
		Sign s = bs.getSign();
		if(s==null)
			return;
		
		Integer giveAmount = bs.getQtyTo();
		String giveTypeText = bs.getItemTo().itemShortname;
		Double getAmount = bs.getMoneyFrom();
		
		s.setLine(0, ChatColor.GOLD + "[" + ChatColor.GREEN + Messages.getString("BoutiqueSign.SELL") + ChatColor.GOLD + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		s.setLine(1, ChatColor.WHITE + Messages.getString("BoutiqueSign.BATCHSOFFCAP") + ChatColor.AQUA + Messages.getString("BoutiqueSign.QTY") + giveAmount );	 //$NON-NLS-1$ //$NON-NLS-2$
		s.setLine(2, ChatColor.AQUA + giveTypeText);
		s.setLine(3, ChatColor.YELLOW +  formatMoney(getAmount) + formatCurrency(getAmount) + ChatColor.WHITE + Messages.getString("BoutiqueSign.110")); //$NON-NLS-1$
		s.update();
	}

	public static void RenderSellSignChest(BoutiqueSign bs)
	{
		Sign s = bs.getSign();
		Chest c = bs.getChest();
		
		if(s==null)
			return;
		
		
		Integer giveType = bs.getItemTo().itemId;
		Integer giveDamage = bs.getItemTo().itemDamage;
		Integer giveAmount = bs.getQtyTo();

		String giveTypeText = bs.getItemTo().itemShortname;
		
		Double getAmount = bs.getMoneyFrom();
		
		//compte le nombre de stacks dispos au total dans le chest						
		int lots = (c!=null) ? (int)(ChestOperator.contains(giveType, giveDamage, c) / giveAmount) : -1;		
		
		//couleur suivant la quantité
		String strLot = "";
		
		if(lots < 0)			strLot = ChatColor.DARK_PURPLE + "?" + ChatColor.BLACK;
		else if(lots == 0)		strLot = ChatColor.RED + String.valueOf(lots) + ChatColor.BLACK;
		else if(lots < 5)		strLot = ChatColor.YELLOW + String.valueOf(lots) + ChatColor.BLACK;
		else 					strLot = ChatColor.DARK_GREEN + String.valueOf(lots) + ChatColor.BLACK;
		
		String strNbLot = "";
		if(lots > 1)	strNbLot = Messages.getString("BoutiqueSign.BATCHSOFFCAP");
		else			strNbLot = Messages.getString("BoutiqueSign.BATCHOFFCAP");
		
		String strAmount = ChatColor.YELLOW + formatMoney(getAmount) + formatCurrency(getAmount);
		
		//mise a jour du panneau
		
		s.setLine(0, ChatColor.GREEN + Messages.getString("BoutiqueSign.SELL") + " " + strLot);
		s.setLine(1, ChatColor.WHITE + strNbLot + ChatColor.AQUA + Messages.getString("BoutiqueSign.QTY") + giveAmount );
		s.setLine(2, ChatColor.AQUA + giveTypeText);
		s.setLine(3, strAmount + ChatColor.WHITE + Messages.getString("BoutiqueSign.FOREACHBATCH"));
		
		if(c==null)
		{
			s.setLine(3,ChatColor.RED + Messages.getString("BoutiqueSign.NOCHESTFOUND"));
		}
		
		s.update();
	}
	
	public boolean isSignServer()
	{
		return this.getType().compareToIgnoreCase(BoutiqueSignServer.getTypeStr())==0;		
	}
	
	public boolean isSignChest()
	{
		return this.getType().compareToIgnoreCase(BoutiqueSignChest.getTypeStr())==0;		
		
	}
	
	public boolean isSignWebAuction()
	{
		return this.getType().compareToIgnoreCase(BoutiqueSignWebAuction.getTypeStr())==0;		
	}
	
	
	
	public boolean isFreebiesSign()
	{
		if(this._moneyFrom == null || this._itemFrom != null )
			return false;
		
		return this._moneyFrom == 0.0;
	}

	public boolean isDonationSign()
	{
		if(this._moneyTo == null || this._itemTo != null )
			return false;
		
		return this._moneyTo == 0.0;
	}

	public boolean isSellSign()
	{
		return (this._moneyFrom != null &&  this._itemFrom == null && this._itemTo != null);
	}
	
	public boolean isBuySign()
	{
		return (this._moneyTo != null &&  this._itemTo == null && this._itemFrom != null);
	}

	public boolean isTradeSign()
	{
		return (this._itemTo != null && this._itemFrom != null);
	}
	

	public static String formatCurrency(Double getAmount)
	{
		//TODO: demander le nom de la currency a iconomy
		return "Eu" + (getAmount > 1 ? "s":"");
	}
	
	
	
	public boolean doAction(Player p)
	{
		//FREEBIES
		if(this.isFreebiesSign())
		{
			return BoutiqueSignManager.getInstance().giveFree(this, p);
		}
		//DONATION
		else if(this.isDonationSign())
		{
			return BoutiqueSignManager.getInstance().getDonation(this, p);			
		}
		//VENTE
		else if(this.isSellSign())
		{
			return BoutiqueSignManager.getInstance().sellItem(this, p);
		}
		//ACHAT
		else if(this.isBuySign())
		{
			return BoutiqueSignManager.getInstance().buyItem(this, p);
		}
		//ECHANGE
		else if (this.isTradeSign())
		{
			return BoutiqueSignManager.getInstance().tradeItems(this, p);
		}
		
		
		p.sendMessage(Boutique.getInstance().chatPrefix + ChatColor.RED + Messages.getString("BoutiqueSign.VROUMVROUMERR") );
		
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
			p.sendMessage(Messages.getString("BoutiqueSign.SECONDLINEERR"));
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
					p.sendMessage(Messages.getString("BoutiqueSign.SECONDLINEINCORECTITEM"));
					return false;
				}
				
				qtyFrom = this.getQtyFrom(); 
				if(qtyFrom == null || qtyFrom < 0 || qtyFrom > 64)
				{
					p.sendMessage(Messages.getString("BoutiqueSign.SECONDLINEINCORRECTQTY"));
					return false;
				}
			}
			else if(moneyFrom == null)
			{
				p.sendMessage(Messages.getString("BoutiqueSign.SECONDLINEINCORRECTMONEY"));
				return false;
			}
			
			
		}
		
		//teste ligne 3
		if (!this.checkLine3()) 
		{
			p.sendMessage(Messages.getString("BoutiqueSign.THIRDLINEERR"));
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
					p.sendMessage(Messages.getString("BoutiqueSign.FIRDLINEINCORRECTITEM"));
					return false;
				}
				
				qtyTo = this.getQtyTo(); 
				if(qtyTo == null || qtyTo < 0 || qtyTo > 64)
				{
					p.sendMessage(Messages.getString("BoutiqueSign.FIRDLINEINCORRECTQTY"));
					return false;
				}
			}
			else if(moneyTo == null)
			{
				p.sendMessage(Messages.getString("BoutiqueSign.FIRDLINEINCORRECTMONEY"));
				return false;
			}
		}
		
		return true;
	}




	
	
	public String getChestString()
	{
		return this._chest;
	} 
	
	public static String getChestString(Chest chest)
	{
		return (chest != null) ? chest.getBlock().getWorld().getName() + ":" + chest.getBlock().getX() + ":" + chest.getBlock().getY() + ":" + chest.getBlock().getZ() : "";		 //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	} 
	
	

	public  Location getLocationFromString()
	{
		return BoutiqueSign.getLocationFromString(this._location);
	}
	
	
	public  Location getChestLocationFromString()
	{
		return BoutiqueSign.getLocationFromString(this._chest);
	}
	
	
	
	public static Location getLocationFromString(String stringloc)
	{
		Location l = null;
	
		try
		{
			String[] sLocs = stringloc.split(":");
			
			if(sLocs.length != 4) 
				return null;
			
			World w = Bukkit.getWorld(sLocs[0]);
			if(w == null) 
				return null;
			
			Double x = Double.parseDouble(sLocs[1]);
			Double y = Double.parseDouble(sLocs[2]);
			Double z = Double.parseDouble(sLocs[3]);
			
			l = new Location(w,x,y,z);
		}
		catch(Exception e)
		{
			
		}
		
		return l;
	}
	
	
	public void setChest(Chest chest)
	{
		//this._chest = chest;
		this._chest = BoutiqueSign.getChestString(chest);
	}

	
	public void setChest(Block block)
	{
		//this._chest = chest;
		this._chest = BoutiqueSign.getLocationString(block.getLocation());
	}

	public void setChest(String chest)
	{
		this._chest = chest;
	}





	public String getLinesString()
	{
		return this.getLine1() + ";" + this.getLine2()+ ";" + this.getLine3() + ";" + this.getLine4();
	}


	public boolean parseString(String objet)
	{
		String[] brokeText = objet.split(";");
    	
    	if(brokeText.length < 6) 
    	{    		
    		return false;
    	}
    	
    	String strLocPanneau = brokeText[0];
    	
    	String strProprio = brokeText[1];
    	
    	String strLigne1Panneau = brokeText[2];
    	String strLigne2Panneau = brokeText[3];
    	String strLigne3Panneau = brokeText[4];
    	String strLigne4Panneau = brokeText[5];
    	
    	String strLocCoffre = (brokeText.length == 7) ? brokeText[6] : "";
  
    	setLocation(strLocPanneau);
    	setOwnerString(strProprio);
    	
    	setLine1(strLigne1Panneau);
    	setLine2(strLigne2Panneau);
    	setLine3(strLigne3Panneau);
    	setLine4(strLigne4Panneau);
    	
    	setChest(strLocCoffre);
		
    	return true;
	}


	public String serializeString()
	{
		return
	
			//Coordonnees du panneau
			this.getLocationString() + ";" + //$NON-NLS-1$
			//Poseur/propriétaire du panneau
			this.getOwnerString() + ";" + //$NON-NLS-1$
			//Lignes textes du panneau
			this.getLinesString() + ";" + //$NON-NLS-1$
			//Coffre relié eventuellement au panneau
			this.getChestString()
		;
	}



	public String getLocationString()
	{
		return this._location;
	}

	public void setLines(String[] split)
	{
		if(split.length<4)
			return;

		
		this.setLine1(split[0]);
		this.setLine2(split[1]);
		this.setLine3(split[2]);
		this.setLine4(split[3]);
	}

	

	public Sign getSign()
	{	
		Location signloc = BoutiqueSign.getLocationFromString(this.getLocationString());
		
		if(signloc == null)
			return null;

		BlockState blockTest = Bukkit.getServer().getWorld(signloc.getWorld().getName()).getBlockAt(signloc.getBlockX(), signloc.getBlockY(), signloc.getBlockZ()).getState();

		if(blockTest instanceof Sign)
			return (Sign) blockTest;

		return null;
	}
	
	
	public Chest getChest()
	{
		
		Location chestloc = BoutiqueSign.getLocationFromString(this.getChestString());
		
		if(chestloc == null)
			return null;
		
		
		BlockState blockTest = Bukkit.getServer().getWorld(chestloc.getWorld().getName()).getBlockAt(chestloc.getBlockX(), chestloc.getBlockY(), chestloc.getBlockZ()).getState();
		
		if(blockTest instanceof Chest)
			return (Chest) blockTest;

		
		return null;
	}

	public Location getChestLocation()
	{
		String[] strings = this.getChestString().split(":");
		
		if(strings.length<4) 
			return null;
		
		org.bukkit.World W = Bukkit.getWorld(strings[0]);
		Double X = Double.parseDouble(strings[1]);
		Double Y = Double.parseDouble(strings[2]);
		Double Z = Double.parseDouble(strings[3]);

		return new Location(W,X,Y,Z);
	}


	public String getType()
	{
		return _type;
	}


	
	
}
