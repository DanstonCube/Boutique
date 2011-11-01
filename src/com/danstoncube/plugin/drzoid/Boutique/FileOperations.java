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
import java.util.Map.Entry;

import com.danstoncube.plugin.drzoid.Boutique.SignTypes.BoutiqueSign;


public class FileOperations 
{
	private final Boutique plugin;
	
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
				plugin.log.info(plugin.logPrefix + "Creation du fichier items...");
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
	        		plugin.log.warning(plugin.logPrefix + "boutiqueitems.txt: ligne incorrecte: " + strLine);
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
				plugin.log.info(plugin.logPrefix + "Creation du fichier db...");
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
	

	
	
	

}
