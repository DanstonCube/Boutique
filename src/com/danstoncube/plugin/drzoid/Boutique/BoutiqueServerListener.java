package  com.danstoncube.plugin.drzoid.Boutique;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import com.danstoncube.tools.Banque.DummyBalance;


 

public class BoutiqueServerListener extends ServerListener {

	private Boutique plugin;

    public BoutiqueServerListener(Boutique plugin) {
        this.plugin = plugin;
    }

   
    @Override
    public void onPluginDisable(PluginDisableEvent event) 
    {        
    	
    	if (EconomyHandler.balance != null) 
    	{
            if (event.getPlugin().getDescription().getName().equals("iConomy")) 
            {
            	EconomyHandler.balance = new DummyBalance(this.plugin);
            	EconomyHandler.currencyEnabled = false;
                plugin.log.info("["+plugin.displayname+"] iConomy désactivé.");
            }
        }    
    	
        
       	if (PermissionsHandler.permissions != null) 
       	{      		
               if (event.getPlugin().getDescription().getName().equals("Permissions")) 
               {
            	   PermissionsHandler.permissions = null;
            	   PermissionsHandler.permissionsEnabled = false;
                   plugin.log.info("["+plugin.displayname+"] Permissions désactivé.");
               }
        }    
       	
       	
       	

    }

    @Override
    public void onPluginEnable(PluginEnableEvent event) 
    {
		//On teste si iConomy est up
		if (EconomyHandler.balance == null) 
		{
			EconomyHandler.setupEconomy();
		}
	
		//On teste la présence de permissions
		if(PermissionsHandler.permissions == null)
		{
			PermissionsHandler.setupPermissions();
		}        
	
		//On teste la présence de worldguard
	    if(WorldGuardOperator.worldguard == null)
	    {
	    	WorldGuardOperator.setupWorldGuard();
	    }       	  
	    
	    //On teste la présence de showcasestandalone
		if(ShowCaseHandler.showcase == null)
		{
			ShowCaseHandler.setupShowcase();
		}
    	
    }
}