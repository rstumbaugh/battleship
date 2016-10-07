/**
 * @author Ryan
 * @date Aug 24, 2013
*/
package com.rs.battleship.settings;


public class Settings 
{
	private static boolean sound = false;
	
	/**
	 * Changes sound on/off
	 * @param s
	 */
	public static void setSound(boolean s)
	{
		sound = s;
	}
	
	/**
	 * Returns true if sound is on
	 * @return sound
	 */
	public static boolean soundOn()
	{
		return sound;
	}
}
