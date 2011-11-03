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
        	plugin.log.info(plugin.logPrefix + Messages.getString("IO.MAKEFOLDER")); //$NON-NLS-1$
        	plugin.makeFolder.mkdir();
        }
        
	}
	
	public void checkPropertiesFile()
	{
		if (!plugin.makeFolder.exists())
	    {
			checkDataFolder();
			
			File fWhitelist = new File(plugin.makeFolder.getAbsolutePath() + File.separator + "config.txt"); //$NON-NLS-1$
			if (!fWhitelist.exists())
			{
				plugin.log.info(plugin.logPrefix + Messages.getString("IO.CREATECONFIG")); //$NON-NLS-1$
				try
				{
					fWhitelist.createNewFile();
					System.out.println(Messages.getString("IO.DONE")); //$NON-NLS-1$
				} 
				catch (IOException ex) 
				{
					plugin.log.severe(plugin.logPrefix + Messages.getString("IO.CREATECONFIGFAILED")); //$NON-NLS-1$
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
		int compteur = 0;
		try
		{
			File itemsFiles = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "boutiqueitems.txt"); //$NON-NLS-1$
			
			if (!itemsFiles.exists())
			{ 
				plugin.log.info(plugin.logPrefix + Messages.getString("IO.CREATEITEMSDB")); //$NON-NLS-1$
				writeItemsFile(null);
			}
			
			FileInputStream fstream = new FileInputStream(itemsFiles);
			DataInputStream in = new DataInputStream(fstream);
	        BufferedReader br = new BufferedReader(new InputStreamReader(in));
	        
	        String strLine;
	        while ((strLine = br.readLine()) != null)
	        {
	        	strLine = strLine.trim();
	        	
	        	if(strLine.isEmpty() || strLine.startsWith("#")) //$NON-NLS-1$
	        		continue;
	        
	        	BoutiqueItem bi = new BoutiqueItem();
	        	
	        	
	        	if(bi.parseString(strLine))
	        	{
	        		BoutiqueItems.put(bi);
	        		compteur++;
	        	}
	        	else
	        	{
	        		plugin.log.warning(plugin.logPrefix + Messages.getString("IO.ITEMDBLINEERROR") + strLine); //$NON-NLS-1$
	        	}
        		
	        }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		plugin.log.info(plugin.logPrefix + compteur + Messages.getString("IO.ITEMCOUNTER")); //$NON-NLS-1$
	} 
		


	public void loadGlobalSignData()
	{
		try
		{
			File globalFile = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "boutiquedb.txt"); //$NON-NLS-1$
			
			if (!globalFile.exists())
			{ 
				plugin.log.info(plugin.logPrefix + Messages.getString("IO.CREATESIGNDB")); //$NON-NLS-1$
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
	        	
	        	if(strLine.isEmpty() || strLine.startsWith("#")) //$NON-NLS-1$
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
	        		plugin.log.warning(plugin.logPrefix + Messages.getString("IO.SIGNDBLINEERROR") + strLine); //$NON-NLS-1$
	        	}

	        }
	        
	        plugin.log.info(plugin.logPrefix + compteur + Messages.getString("IO.SIGNCOUNTER")); //$NON-NLS-1$
	        
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	
	private void writeItemsFile(String[] sLines)
	{
		String[] s = new String[1];
		s[0] = "#"; //$NON-NLS-1$
		
		try 
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter((plugin.makeFolder.getAbsolutePath() + File.separator + "boutiqueitems.txt"))); //$NON-NLS-1$
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
			plugin.log.severe(plugin.logPrefix + Messages.getString("IO.ITEMDBSAVEERROR")); //$NON-NLS-1$
			ex.printStackTrace();
		}
		
	}
	
	private void writeGlobalSignFile(String[] sLines) 
	{
		String[] s = new String[1];
		s[0] = "#locsign;owner;line1;line2;line3;line4;[chest]"; //$NON-NLS-1$
		try 
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter((plugin.makeFolder.getAbsolutePath() + File.separator + "boutiquedb.txt"))); //$NON-NLS-1$
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
			plugin.log.severe(plugin.logPrefix + Messages.getString("IO.SIGNDBSAVEERROR")); //$NON-NLS-1$
			ex.printStackTrace();
		}
	}
	

	
	
	

}
