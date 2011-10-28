/**
 * 
 */
package com.danstoncube.plugin.drzoid.Boutique;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import org.bukkit.Location;


/**
 * @author miko
 *
 */
public class BoutiqueDb
{
	private final Boutique plugin;

	private DatabaseHandler manageDB;
	
	public Boolean enabled = false;
	
	//overidé par la config
	private String Boutique_TablePrefix = "Boutique_";
	
	
	BoutiqueDb(Boutique plugin)
	{
		this.plugin = plugin;
	}
	
	
		
	public boolean setup()
	{
		Boutique_TablePrefix = plugin.configuration.sqlTablePrefix;
		
		if (plugin.configuration.sqlTablePrefix.equals(null)) 	{plugin.log.severe(plugin.logPrefix + "MySQL table prefix is not defined"); }		
		if (plugin.configuration.sqlHost.equals(null)) 			{plugin.log.severe(plugin.logPrefix + "MySQL host is not defined"); }
		if (plugin.configuration.sqlUser.equals(null)) 			{plugin.log.severe(plugin.logPrefix + "MySQL username is not defined"); }
		if (plugin.configuration.sqlPass.equals(null)) 			{plugin.log.severe(plugin.logPrefix + "MySQL password is not defined"); }
		if (plugin.configuration.sqlDb.equals(null)) 			{plugin.log.severe(plugin.logPrefix + "MySQL database is not defined"); }
		
		if(!plugin.configuration.useMysql) 
		{
			enabled = false;
			return false;
		}
		
		initialize();
		try 
		{
			if(!checkConnection())
			{
				plugin.log.severe(plugin.logPrefix + "Connexion impossible à Mysql ! (" + plugin.configuration.sqlUser + "/" + plugin.configuration.sqlHost+ ")");
				close();
				return false;
			}
			else
			{
				enabled = true;
			}
			checkTables();
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		close();
		
		return true;
	}
	
	
	
	
	public Boolean checkTables() throws MalformedURLException, InstantiationException, IllegalAccessException 
	{
		//Table transaction
		if (!checkTable(Boutique_TablePrefix + "transactions")) 
		{
			writeInfo("Création de la table " + Boutique_TablePrefix + "transactions");
			String query = "" + 
			"CREATE TABLE " + Boutique_TablePrefix + "transactions (" +
				"id INT NOT NULL AUTO_INCREMENT, " +
				"PRIMARY KEY(id), " +
				"ts TIMESTAMP, "+
				"location VARCHAR(255), " +
				"trade_from VARCHAR(255), " +
				"trade_to VARCHAR(255), " +
				"item_id INT, " +
				"item_damage INT, " +
				"item_quantity INT, " +
				"item_price DOUBLE, " +
				"item_name VARCHAR(255), " +
				"item_shortname VARCHAR(12)" + 
			");";
			createTable(query.toString());
		}

		return true;		
	}
	
	
	public boolean wa_AddToStock(String p, int itemid, int itemdamage, int quantity)
	{
		String sql = "SELECT count(*) as compteur FROM WA_Items WHERE player='" + p + "' and name=" + itemid + " and damage = " + itemdamage;
		
		boolean ret = true;
		int compteur = 0;
		
		try
		{
			this.openConnection();
			ResultSet myResults = this.sqlQuery(sql);
			
			if(myResults.next())
			{
				compteur = myResults.getInt(1);
			}
			
			
			if(compteur > 0)
			{
				String sqlupdate = "UPDATE WA_Items SET quantity = quantity + " + quantity + " WHERE player='" + p + "' and name=" + itemid + " and damage=" + itemdamage;
				this.updateQuery(sqlupdate);
			}
			else
			{
				/*id 	name 	damage 	player 	quantity*/
				String sqlinsert = "INSERT INTO WA_Items(name,damage,player,quantity) VALUES("+ itemid + ", " + itemdamage + ", '" + p + "', " + quantity+")"; 
				this.insertQuery(sqlinsert);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
			ret  = false;
		}
		
		
		this.close();
		
		return ret;
	}
	
	
	
	public Boolean wa_RemoveFromStock(String p, Integer itemid, Integer itemdamage, Integer quantity)
	{
		
		plugin.log.info("DEBUG: wa_RemoveFromStock - " + p + " " + itemid + " " + itemdamage +  " " + quantity );
		
		if (itemdamage == null || itemdamage < 0)
			itemdamage = 0;
		
		Integer qtyStock = this.wa_CountItem(p, itemid, itemdamage);
		
		String updateSql = "UPDATE WA_Items SET quantity = quantity - " + quantity + " WHERE player='" + p + "' and name=" + itemid + " and damage=" + itemdamage;
		String deleteSql = "DELETE FROM WA_Items WHERE player='" + p + "' and name=" + itemid + " and damage=" + itemdamage;
		
		boolean ret = true;
		
		try
		{
			this.openConnection();
			
			//TODO: statement perso et compter les update, si = 0 return false !
			
			if(qtyStock>quantity)
			{
				this.updateQuery(updateSql);
			}
			else if(qtyStock == quantity)
			{
				this.deleteQuery(deleteSql);
			}
			else
			{
				return false;
			}
			
				
			
			
			plugin.log.info("DEBUG: wa_RemoveFromStock 2");
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret = false;
		}
		
		close();
		
	
		
		return ret;
	}
	
	
	public Boolean wa_HasEnoughItem(String p, Integer itemid, Integer itemdamage, Integer quantity)
	{
		
		Integer count = wa_CountItem(p,itemid,itemdamage);
		
		plugin.log.info(plugin.logPrefix + "DEBUG: count=" + count);
		
		return (count>=quantity);
	}
	
	public int wa_CountItem(String p, Integer itemid, Integer itemdamage)
	{
		String sql = "SELECT quantity FROM WA_Items WHERE player='" + p + "' and name=" + itemid + " and damage = " + itemdamage;
		Integer count = -1;
		
		try
		{
			this.openConnection();
			ResultSet myResults = this.sqlQuery(sql);
			if(myResults.next())
			{
				plugin.log.info(plugin.logPrefix + "DEBUG: wasnull=false");
				count = myResults.getInt(1);
			}
			else
			{
				plugin.log.info(plugin.logPrefix + "DEBUG: wasnull=true");
				count = 0;
			}
		}
		catch (SQLException e1)
		{
			e1.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		close();
		
		return count;
	}
	
	
	public Boolean logTransaction(Location location, String trade_from, String trade_to, int item_id, int item_damage, int item_qty, Double costAmount, String item_shortname, String item_name) throws SQLException, MalformedURLException, InstantiationException, IllegalAccessException
	{
		String sLocation = location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getWorld().getName();
		return logTransaction(sLocation, trade_from, trade_to, item_id, item_damage, item_qty, costAmount, item_shortname, item_name);
	}
	
	public Boolean logTransaction(String location, String trade_from, String trade_to, int item_id, int item_damage, int item_qty, Double costAmount, String item_shortname, String item_name) throws SQLException, MalformedURLException, InstantiationException, IllegalAccessException
	{
		if(!enabled) return false;
		
		String query=""+
		"INSERT INTO " + Boutique_TablePrefix + "transactions" +
		"(location, trade_from, trade_to, item_id, item_damage, item_quantity, item_price, item_name, item_shortname,ts)" +
		"VALUES (?,?,?,?,?,?,?,?,?,?);";
		
		if(trade_from.isEmpty())		trade_from = "[Serveur]";
		if(trade_to.isEmpty())			trade_to = "[Serveur]";
		

		Connection conn = openConnection();
		if(conn==null)
		{
			plugin.log.severe(plugin.logPrefix + " Mysql: impossible d'insèrer, connexion incorrecte.");
			return false;
		}
		
		PreparedStatement stmt = conn.prepareStatement(query);
		
		stmt.setString(1, location);
		stmt.setString(2, trade_from);
		stmt.setString(3, trade_to);
		stmt.setInt(4, item_id);
		stmt.setInt(5, item_damage);
		stmt.setInt(6, item_qty);
		stmt.setDouble(7, costAmount);
		stmt.setString(8, item_name);
		stmt.setString(9, item_shortname);
		stmt.setTimestamp(10, new Timestamp(Calendar.getInstance().getTimeInMillis()));
	
		Boolean insertOk =  (stmt.executeUpdate() == 1);

		//debug
		//plugin.log.info(plugin.logPrefix + "INSERTION MYSQL");
		
		close();

		if(insertOk)
		{
			plugin.log.info(plugin.logPrefix + "[Mysql] Transaction entre " + trade_from + " et " + trade_to + " @" + location +  " enregistrée.");
			return true;
		}
		else
		{
			plugin.log.severe(plugin.logPrefix + "[Mysql] Echec de l'enregistrement de la transaction entre " + trade_from + " et " + trade_to + " @" + location +  " ! :(");
		}
		
		return false;
	}
	
	
	
	
	
	
	
	public Boolean initialize() 
	{
		this.manageDB = new DatabaseHandler(this, plugin.configuration.sqlHost, plugin.configuration.sqlDb, plugin.configuration.sqlUser, plugin.configuration.sqlPass);
		return false;
	}
	
	
	public void writeInfo(String toWrite) 
	{
		if (toWrite != null) 
		{
			plugin.log.info(plugin.logPrefix + toWrite);
		}
	}
	
	public void writeError(String toWrite, Boolean severe) 
	{
		if (severe) 
		{
			if (toWrite != null) 
			{
				plugin.log.severe(plugin.logPrefix + toWrite);
			}
		} 
		else 
		{
			if (toWrite != null) 
			{
				plugin.log.warning(plugin.logPrefix + toWrite);
			}
		}
	}
	
	public ResultSet sqlQuery(String query) throws MalformedURLException, InstantiationException, IllegalAccessException {
		return this.manageDB.sqlQuery(query);
	}
	
	public Boolean createTable(String query) {
		return this.manageDB.createTable(query);
	}
	
	public void insertQuery(String query) throws MalformedURLException, InstantiationException, IllegalAccessException {
		this.manageDB.insertQuery(query);
	}
	
	public void updateQuery(String query) throws MalformedURLException, InstantiationException, IllegalAccessException {
		this.manageDB.updateQuery(query);
	}
	
	public void deleteQuery(String query) throws MalformedURLException, InstantiationException, IllegalAccessException {
		this.manageDB.deleteQuery(query);
	}
	
	public Boolean checkTable(String table) throws MalformedURLException, InstantiationException, IllegalAccessException {
		return this.manageDB.checkTable(table);
	}
	
	public Boolean wipeTable(String table) throws MalformedURLException, InstantiationException, IllegalAccessException {
		return this.manageDB.wipeTable(table);
	}
	
	
	public Connection openConnection() throws MalformedURLException, InstantiationException, IllegalAccessException, SQLException {
		return this.manageDB.openConnection();
	}
	
	public Connection getConnection() throws MalformedURLException, InstantiationException, IllegalAccessException {
		return this.manageDB.getConnection();
	}
	
	public void close() 
	{
		if(this.manageDB!=null)
			this.manageDB.closeConnection();
	}
	
	public Boolean checkConnection() throws MalformedURLException, InstantiationException, IllegalAccessException 
	{
		if(this.manageDB!=null)
			return this.manageDB.checkConnection();
		else
			return false;
	}



	public void loadGlobalSignData()
	{
		// TODO Auto-generated method stub
	}



	
	
	
	
	
	
	

}



