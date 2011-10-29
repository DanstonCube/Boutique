package com.danstoncube.plugin.drzoid.Boutique;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map.Entry;

import com.danstoncube.plugin.drzoid.Boutique.SignTypes.BoutiqueSign;


public class FileOperations 
{
	private final Boutique plugin;
	private final String unexpectedFormat = "Error: Unexpected format.";
	private final int itemListVersion = 3;
	
	FileOperations(Boutique boutique)
	{
		this.plugin = boutique;
		//plugin.makeFolder = plugind.getDataFolder();
	}
	
	public void checkDataFolder() 
	{
        if(!plugin.makeFolder.exists())
        {
        	plugin.log.info(plugin.logPrefix + "Création du dossier Boutique");
        	plugin.makeFolder.mkdir();
        }
        
	}
	
	public void checkPropertiesFile()
	{
		if (!plugin.makeFolder.exists())
	    {
			checkDataFolder();
			
			File fWhitelist = new File(plugin.makeFolder.getAbsolutePath() + File.separator + "config.txt");
			if (!fWhitelist.exists())
			{
				plugin.log.info(plugin.logPrefix + "Config file is missing, creating.");
				try
				{
					fWhitelist.createNewFile();
					System.out.println("done.");
				} 
				catch (IOException ex) 
				{
					plugin.log.severe(plugin.logPrefix + "Config file creation FAILED.");
					ex.printStackTrace();
				}
			}
	    }
	}
	
	
	
	
	
	public void saveGlobalSigns() 
	{
		ArrayList<String> strings = new ArrayList<String>();
		for (Entry<String, BoutiqueSign> entry : plugin.signmanager.entrySet()) 
		{
			BoutiqueSign bs = entry.getValue();
			strings.add(bs.serializeString());
		}
		
		String[] lines = new String[strings.size()];
		
		for(int i = 0; i < strings.size(); i++)
		{
			lines[i] = strings.get(i);
		}
		
		writeGlobalSignFile(lines);
		
	}
	
	
	
	public void loadItemsData()
	{
		try
		{
			File itemsFiles = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "boutiqueitems.txt");
			
			if (!itemsFiles.exists())
			{ 
				System.out.print("[" + plugin.name + "] Creation du fichier items...");
				writeItemsFile(null);
			}
			
			FileInputStream fstream = new FileInputStream(itemsFiles);
			DataInputStream in = new DataInputStream(fstream);
	        BufferedReader br = new BufferedReader(new InputStreamReader(in));
	        
	        String strLine;
	       
	        
	        int compteur = 0;
	        while ((strLine = br.readLine()) != null)
	        {
	        	strLine = strLine.trim();
	        	
	        	//Boutique.getInstance().log.info("Line: " + strLine);
	        	
	        	if(strLine.isEmpty() || strLine.startsWith("#"))
	        		continue;
	        
	        
	        	BoutiqueItem bi = new BoutiqueItem();
	        	
	        	
	        	if(bi.parseString(strLine))
	        	{
	        		//TODO: virer debug
	        		//Boutique.getInstance().log.info("Load item OK: " + strLine);
	        		BoutiqueItems.put(bi);
	        		compteur++;
	        	}
	        	else
	        	{
	        		plugin.log.warning(plugin.logPrefix + " boutiqueitems.txt: ligne incorrecte: " + strLine);
	        	}
        		
	        	
	        }
	        
	        plugin.log.info(plugin.logPrefix + compteur + " items chargés depuis boutiqueitems.txt");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
		


	public void loadGlobalSignData()
	{
		try
		{
			File globalFile = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "boutiquedb.txt");
			
			if (!globalFile.exists())
			{ 
				System.out.print("[" + plugin.name + "] Creation du fichier db...");
				writeGlobalSignFile(null);
			}
			
			FileInputStream fstream = new FileInputStream(globalFile);
			DataInputStream in = new DataInputStream(fstream);
	        BufferedReader br = new BufferedReader(new InputStreamReader(in));
	        
	        String strLine;
	        
	        //String pName = "";
	        //String coords = "";
	        
	        int compteur = 0;
	        
	        while ((strLine = br.readLine()) != null)
	        {
	        	strLine = strLine.trim();
	        	
	        	if(strLine.isEmpty() || strLine.startsWith("#"))
	        		continue;
	        	
	        	BoutiqueSign bs = new BoutiqueSign();
	        	
	        	
	        	if(bs.parseString(strLine))
	        	{
	        		//TODO: virer debug
	        		//plugin.log.info("Ajout sign : " + strLine);
	        		plugin.signmanager.put(bs);
	        		compteur++;
	        	}
	        	else
	        	{
	        		//TODO: virer debug
	        		plugin.log.warning(plugin.logPrefix + " boutiquedb.txt: ligne incorrecte: " + strLine);
	        	}

	        }
	        
	        plugin.log.info(plugin.logPrefix + compteur + " panneaux chargés depuis boutiquedb.txt");
	        
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	
	private void writeItemsFile(String[] sLines)
	{
		String[] s = new String[1];
		s[0] = "#";
		
		try 
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter((plugin.makeFolder.getAbsolutePath() + File.separator + "boutiqueitems.txt")));
			for(int i=0;i<s.length;i++)
			{
				writer.write(s[i]);
				writer.newLine();
			}
			if(sLines != null)
			{
				for(String line : sLines)
				{
					writer.write(line);
					writer.newLine();
				}
			}
			writer.close();
		} 
		catch (Exception ex) 
		{
			plugin.log.severe(plugin.logPrefix + "Impossible de sauvegarder le fichier items !!!");
			ex.printStackTrace();
		}
		
	}
	
	private void writeGlobalSignFile(String[] sLines) 
	{
		String[] s = new String[1];
		s[0] = "#locsign;owner;line1;line2;line3;line4;[chest]";
		try 
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter((plugin.makeFolder.getAbsolutePath() + File.separator + "boutiquedb.txt")));
			for(int i=0;i<s.length;i++)
			{
				writer.write(s[i]);
				writer.newLine();
			}
			if(sLines != null)
			{
				for(String line : sLines)
				{
					writer.write(line);
					writer.newLine();
				}
			}
			writer.close();
		} 
		catch (Exception ex) 
		{
			plugin.log.severe(plugin.logPrefix + "Impossible de sauvegarder le fichier global !!!");
			ex.printStackTrace();
		}
	}

	@Deprecated
	public void loadItemData()
	{
		try
		{
	        // Open the file that is the first 
	        // command line parameter
			File itemFile = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "items.txt");
			
			//If the file doesn't exist, write it.
			if(!itemFile.exists())
			{
				System.out.print("[" + plugin.name + "] Traduction des items manquante, creation...");
				writeItemDataFile();
				System.out.println("done.");
			}
			
	        FileInputStream fstream = new FileInputStream(itemFile);
	        // Get the object of DataInputStream
	        
	        DataInputStream in = new DataInputStream(fstream);
	        BufferedReader br = new BufferedReader(new InputStreamReader(in));
	        String strLine;
	        
	        //Read File Line By Line
	        int version = 0;
	        while ((strLine = br.readLine()) != null)
	        {
	        	strLine = strLine.toLowerCase(Locale.ENGLISH).trim();
	        	if(strLine.startsWith("version="))
	        	{
	        		String getVer = strLine.replace("version=","");
	        		try
	        		{
	        			version = Integer.parseInt(getVer);
	        			if (version < itemListVersion)
	        			{
	        				System.out.println("[" + plugin.name + "] Updating item list to Version:" + itemListVersion + "from Version: " + version + ".");
	        				writeItemDataFile();
	        				loadItemData();
	        				break;
	        			}
	        		}
	        		catch(Exception e)
	        		{
	        			//do nothing clearly in wrong format
	        		}
	        		
	        	}
	        	else if(!strLine.startsWith("#"))
	        	{
	            	String[] brokeText = strLine.split(":");
	            	try
	            	{
	            		int id = Integer.parseInt(brokeText[0]);
	            		String name = brokeText[1];
	            		int stackSize = Integer.parseInt(brokeText[2]);
	            		Boutique.itemMaxIdStack.put(id, stackSize);
	            		Boutique.itemNameId.put(name, id);
	            		Boutique.itemIdName.put(id, name);
	            	}
	           		catch (Exception e)
	           		{
	           			System.err.println(unexpectedFormat);
	            	}
	        	}
	       	}
	        in.close(); //Close the input stream
	    }
		catch (Exception e)
		{
			//Catch exception if any
			e.printStackTrace();
	    }
	}
	
	@Deprecated
	public void writeItemDataFile()
	{
		String[] s = getItemsText();
		try 
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter((plugin.makeFolder.getAbsolutePath() + File.separator + "items.txt")));
			for(int i=0;i<s.length;i++)
			{
				writer.write(s[i]);
				writer.newLine();
			}
			writer.close();
		} 
		catch (Exception ex) 
		{
			System.out.println(ex);
		}
	}
	
	@Deprecated
	private String[] getItemsText() 
	{
		ArrayList<String> s = new ArrayList<String>();
		s.add("Version=" + itemListVersion);
		s.add("# Put a # in front of items you wish to blacklist");
		s.add("# Make sure itemNames are no longer than 11 char,");
		s.add("# otherwise there will be issues.");
		s.add("# BLOCKS");
		s.add("1:Pierre:64");
		s.add("2:Herbe:64");
		s.add("3:Bouse:64");
		s.add("4:Cobble:64");
		s.add("5:Planche:64");
		s.add("6:Sapling:64");
		s.add("#7:Bedrock:64");
		s.add("#8:Eau:64");
		s.add("#9:Source eau:64");
		s.add("#10:Lave:64");
		s.add("#11:Source Lave:64");
		s.add("12:Sable:64");
		s.add("13:Gravier:64");
		s.add("14:Gold Ore:64");
		s.add("15:Iron Ore:64");
		s.add("16:Coal Ore:64");
		s.add("17:Bois:64");
		s.add("18:Feuilles:64");
		s.add("19:Eponge:64");
		s.add("20:Verre:64");
		s.add("21:Lapiz Ore:64");
		s.add("22:Lapiz Block:64");
		s.add("23:Dispenser:64");
		s.add("24:Sandstone:64");
		s.add("25:Note Block:64");
		s.add("#26:Bed Block:1");
		s.add("35:White Wool:64");
		s.add("37:Yellow Flr:64");
		s.add("38:Red Rose:64");
		s.add("39:Champimaron:64");
		s.add("40:Champirouge:64");
		s.add("41:Or (bloc):64");
		s.add("42:Fer (bloc):64");
		s.add("43:Double Slab:64");
		s.add("44:Stone Slab:64");
		s.add("45:Brick Block:64");
		s.add("46:TNT:64");
		s.add("47:Bookshelf:64");
		s.add("48:Moss Stone:64");
		s.add("49:Obsidian:64");
		s.add("50:Torch:64");
		s.add("#51:Fire:64");
		s.add("#52:Spawner:64");
		s.add("53:Wood Stairs:64");
		s.add("54:Chest:64");
		s.add("#55:Red Wire:64");
		s.add("56:Diamond Ore:64");
		s.add("57:Diam Block:64");
		s.add("58:Workbench:64");
		s.add("#59:Crops:64");
		s.add("#60:Farmland:64");
		s.add("61:Furnace:64");
		s.add("#62:Brn Furnace:64");
		s.add("#63:Sign Post:1");
		s.add("#64:Wood Door:1");
		s.add("65:Echelle:64");
		s.add("66:Cart Tracks:64");
		s.add("67:Cob Stairs:64");
		s.add("#68:Wall Sign:64");
		s.add("69:Levier:64");
		s.add("70:Stone Pad:64");
		s.add("#71:Iron Door:1");
		s.add("72:Wood Pad:64");
		s.add("73:Redstne Ore:64");
		s.add("#74:GRedstneOre:64");
		s.add("75:Redstn Trch:64");
		s.add("#76:Redstn Trch:64");
		s.add("77:Bouton:64");
		s.add("78:Neige:64");
		s.add("79:Glace:64");
		s.add("80:Neige (bloc):64");
		s.add("81:Cactus:64");
		s.add("82:Clay:64");
		s.add("83:Canesucre:64");
		s.add("84:Jukebox:64");
		s.add("85:Barrière:64");
		s.add("86:Citrouille:1");
		s.add("87:Netherrack:64");
		s.add("88:Soul Sand:64");
		s.add("89:Glowstone:64");
		s.add("#90:Portail:64");
		s.add("91:Jack-O-Lant:1");
		s.add("#92:Cake Block:1");
		s.add("#93:Diode Off:64");
		s.add("#94:Diode On:64");
		s.add("#");
		s.add("#ITEMS");
		s.add("256:Pelle(fer):1");
		s.add("257:Pioche(fer):1");
		s.add("258:Hache(fer):1");
		s.add("259:FlintNSteel:1");
		s.add("260:Apple:1");
		s.add("261:Bow:1");
		s.add("262:Arrow:64");
		s.add("263:Charbon:64");
		s.add("264:Diamant:64");
		s.add("265:Iron Ingot:64");
		s.add("266:Gold Ingot:64");
		s.add("267:Iron Sword:1");
		s.add("268:Wood Sword:1");
		s.add("269:Wood Shovel:1");
		s.add("270:Wood Pick:1");
		s.add("271:Wood Axe:1");
		s.add("272:Stne Sword:1");
		s.add("273:Stne Shovel:1");
		s.add("274:Stne Pick:1");
		s.add("275:Stne Axe:1");
		s.add("276:Diam Sword:1");
		s.add("277:Diam Shovel:1");
		s.add("278:Diam Pick:1");
		s.add("279:Diam Axe:1");
		s.add("280:Stick:64");
		s.add("281:Bowl:64");
		s.add("282:Shroom Soup:1");
		s.add("283:Gold Sword:1");
		s.add("284:Gold Shovel:1");
		s.add("285:Gold Pick:1");
		s.add("286:Gold Axe:1");
		s.add("287:Ficelle:64");
		s.add("288:Cuir:64");
		s.add("289:Sulphur:64");
		s.add("290:Wooden Hoe:1");
		s.add("291:Stone Hoe:1");
		s.add("292:Iron Hoe:1");
		s.add("293:Diamond Hoe:1");
		s.add("294:Gold Hoe:1");
		s.add("295:Graines:64");
		s.add("296:Wheat:64");
		s.add("297:Pain:1");
		s.add("298:Lther Helm:1");
		s.add("299:Lther Chest:1");
		s.add("300:Lther Leggs:1");
		s.add("301:Lther Boots:1");
		s.add("302:Mail Helmet:1");
		s.add("303:Mail Chest:1");
		s.add("304:Mail Leggs:1");
		s.add("305:Mail Boots:1");
		s.add("306:Iron Helmet:1");
		s.add("307:Iron Chest:1");
		s.add("308:Iron Leggs:1");
		s.add("309:Iron Boots:1");
		s.add("310:Dimnd Helm:1");
		s.add("311:Dimnd Chest:1");
		s.add("312:Dimnd Leggs:1");
		s.add("313:Diamond Boots:1");
		s.add("314:Gold Helmet:1");
		s.add("315:Gold Chest:1");
		s.add("316:Gold Leggs:1");
		s.add("317:Gold Boots:1");
		s.add("318:Flint:1");
		s.add("319:Chippo:1");
		s.add("320:Merguez:1");
		s.add("321:Cadre:64");
		s.add("322:Gold apple:1");
		s.add("323:Panneau:1");
		s.add("324:Porte (bois):1");
		s.add("325:Sceau:1");
		s.add("326:Watr Bucket:1");
		s.add("327:Lava Bucket:1");
		s.add("328:Minecart:1");
		s.add("329:Saddle:1");
		s.add("330:Porte (fer):1");
		s.add("331:Redstone:64");
		s.add("332:Snowball:16");
		s.add("333:Boat:1");
		s.add("334:Leather:64");
		s.add("335:Lait:1");
		s.add("336:Clay Brick:64");
		s.add("337:Clay Balls:64");
		s.add("338:Sugar Cane:64");
		s.add("339:Papier:64");
		s.add("340:Livre:64");
		s.add("341:Slimeball:16");
		s.add("342:Strg cart:1");
		s.add("343:Power Cart:1");
		s.add("344:Oeuf:16");
		s.add("345:Boussole:1");
		s.add("346:Canne:1");
		s.add("347:Montre:1");
		s.add("348:Glwstn Dust:64");
		s.add("349:Raw Fish:1");
		s.add("350:Cooked Fish:1");
		s.add("351:Encre:64");
		s.add("352:Os:64");
		s.add("353:Sucre:64");
		s.add("354:Gateau:1");
		s.add("355:Bed:1");
		s.add("356:Diode:1");
		s.add("2256:Gold Disc:1");
		s.add("2257:Green Disc:1");
		s.add("35001:Orange Wool:64");
		s.add("35002:Mgenta Wool:64");
		s.add("35003:LgtBlu Wool:64");
		s.add("35004:Yellow Wool:64");
		s.add("35005:LmeGrn Wool:64");
		s.add("35006:Pink Wool:64");
		s.add("35007:Gray Wool:64");
		s.add("35008:LgtGry Wool:64");
		s.add("35009:Cyan Wool:64");
		s.add("35010:Purple Wool:64");
		s.add("35011:Blue Wool:64");
		s.add("35012:Brown Wool:64");
		s.add("35013:Green Wool:64");
		s.add("35014:Red Wool:64");
		s.add("35015:Black Wool:64");
		s.add("35016:PurpEr Wool:64");
		
		String[] str = new String[s.size()];
		int i = 0;
		for (String addStr : s){
			str[i] = addStr;
			i++;
		}
		
		return str;
	}
}
