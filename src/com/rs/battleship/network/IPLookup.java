package com.rs.battleship.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;

public class IPLookup 
{
	BufferedReader in;
	String ip = "";
	
	/**
	 * Gets external IP via checkip.amazonaws.com
	 */
	public void openConnection()
	{
		try {
			URL findIP = new URL("http://checkip.amazonaws.com/");
			in = new BufferedReader(new InputStreamReader(findIP.openStream()));
		
			String inputLine;
			
			while((inputLine = in.readLine()) != null)
			{
				ip = inputLine;
				in.close();
				return;
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns external IP
	 * @return ip
	 */
	public String getExternalIP()
	{
		openConnection();
		return ip;
	}
	
	public String getLocalIP()
	{
		String ip = "";
		try{
			InetAddress inet = InetAddress.getLocalHost();
			ip = inet.getHostAddress(); 
		} catch(Exception e) { e.printStackTrace(); }
		
		return ip;
	}
}
