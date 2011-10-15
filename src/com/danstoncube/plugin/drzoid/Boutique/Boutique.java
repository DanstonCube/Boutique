package com.danstoncube.plugin.drzoid.Boutique;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;



import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

public class Boutique extends JavaPlugin
{
	public final Logger log = Logger.getLogger("Minecraft");
	public final String logPrefix = "[Boutique] ";
	
	public final String name = "Boutique";
	public final String displayname = "Boutique";
	public final String version = "2.0.0";
	
	//File Handler
	public File makeFolder;
	
	FileOperations fileIO = new FileOperations(this);
	CommandOperator co = new CommandOperator(this);
	
	//Mapping
	public static HashMap<Integer,Integer> itemMaxIdStack = new HashMap<Integer,Integer>(); // Contains itemId and max size of stack
	public static HashMap<String,Integer> itemNameId = new HashMap<String,Integer>(); // Contains the name and id of the item associated with it
	public static HashMap<Integer,String> itemIdName = new HashMap<Integer,String>(); // Contains the name and id of the item associated with it
	
	public static HashMap<String,String> signLocs = new HashMap<String,String>(); //Contains Sign location, and playerName
	public static HashMap<String,String> SignChest = new HashMap<String,String>(); // Contains Sign location and chest Location.
	public static HashMap<String,String> SignSlab = new HashMap<String,String>(); // Contains Sign location and stone slab (showcase).
	
	public static HashMap<String,String> signLine1 = new HashMap<String,String>(); //Contains Sign location, and playerName
	public static HashMap<String,String> signLine2 = new HashMap<String,String>(); //Contains Sign location, and playerName
	public static HashMap<String,String> signLine3 = new HashMap<String,String>(); //Contains Sign location, and playerName
	
	
	
	// Listeners
	public final BoutiqueBlockListener blockListener = new BoutiqueBlockListener(this);
	public final BoutiquePlayerListener playerListener = new BoutiquePlayerListener(this);
	public final BoutiqueServerListener serverListener = new BoutiqueServerListener(this);

	
	
	public SignManager sm = new SignManager(this);
	
	public BoutiqueDb db = new BoutiqueDb(this);
	
	public Configuration config;
	
	
	public void onEnable() 
	{
		log.info(logPrefix + "Chargement");
			
		
		
		makeFolder = this.getDataFolder();
		config = this.getConfiguration();
		
		fileIO.checkDataFolder();
		fileIO.loadItemData();
		signLocs = fileIO.loadGlobalSignData();
		
		
		if(!db.setup())
		{
			log.info(logPrefix + "version " + version + " désactivé.");			
			return;
		}
		
		
		EconomyHandler.setupEconomy(this);
		
		PluginManager pm = this.getServer().getPluginManager();

		
		pm.registerEvent(Event.Type.SIGN_CHANGE, this.blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, this.blockListener, Event.Priority.High, this);
		pm.registerEvent(Event.Type.BLOCK_PLACE, this.blockListener, Event.Priority.High, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, this.playerListener, Event.Priority.Normal, this);
        
        //pour être au courant de l'activation/désactivation d'autres plugins (iConomy surtout)
        pm.registerEvent(Event.Type.PLUGIN_ENABLE, this.serverListener, Priority.Monitor, this);
        pm.registerEvent(Event.Type.PLUGIN_DISABLE, this.serverListener, Priority.Monitor, this);
        
        
		log.info(logPrefix + "version " + version + " activé.");
	}
	
	public void onDisable() 
	{
		db.close();
		log.info(logPrefix + "version " + version + " désactivé.");
	}
	
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) 
	{	
		return co.command(sender, command, commandLabel, args);
	}
}
