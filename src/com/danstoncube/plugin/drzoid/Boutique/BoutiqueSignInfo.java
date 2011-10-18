package com.danstoncube.plugin.drzoid.Boutique;

public class BoutiqueSignInfo 
{
	
	public String[] lines = new String[3];
	public String location  = "";
	public String owner = "";
	public String type = "";

	BoutiqueSignInfo(String location, String owner, String line1, String line2, String line3, String line4)
	{
		this.setLine1(line1);
		this.setLine2(line2);
		this.setLine3(line3);
		this.setLine4(line4);
	}

	private void setLine4(String line4) 
	{
		this.lines[3] = line4;		
	}

	private void setLine3(String line3) 
	{
		this.lines[2] = line3;		
	}

	private void setLine2(String line2) 
	{
		this.lines[1] = line2;		
	}

	private void setLine1(String line1) 
	{
		this.lines[0] = line1;		
	}

	public String line1()
	{
		return this.lines[0];
	}
	
	public String line2()
	{
		return this.lines[0];
	}
	
	public String line3()
	{
		return this.lines[0];
	}
	
	public String line4()
	{
		return this.lines[0];
	}

	
}
