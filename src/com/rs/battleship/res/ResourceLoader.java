/**
 * @author Ryan
 * @date Sep 25, 2013
*/
package com.rs.battleship.res;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ResourceLoader 
{
	public static Image TITLE_IMAGE = null,
						WATER_IMAGE = null,
						SHIP_IMAGE = null,
						SHIP_VERT_IMAGE = null,
						BG_IMAGE = null,
						BG2_IMAGE = null;
	
	public static Image DOT_RED = null,
						DOT_BLUE = null;
	
	final String title_path = "/com/rs/battleship/res/logo.png",
				 water_path = "/com/rs/battleship/res/water.jpg",
				 ship_path = "/com/rs/battleship/res/ship.png",
				 ship_vert_path = "/com/rs/battleship/res/shipvert.png",
				 rdot_path = "/com/rs/battleship/res/dot.PNG",
				 bdot_path = "/com/rs/battleship/res/dotblue.PNG",
				 bg_path = "/com/rs/battleship/res/bg.png",
				 bg2_path = "/com/rs/battleship/res/bg2.png";
	
	/**
	 * Loads all resources from a static
	 * context
	 */
	public static void load()
	{
		new ResourceLoader().loadRes();
	}
	
	/**
	 * Loads all resources
	 */
	public void loadRes()
	{
		try {
			InputStream in = getClass().getResourceAsStream(title_path);
			TITLE_IMAGE = ImageIO.read(in);
			
			in = getClass().getResourceAsStream(water_path);
			WATER_IMAGE = ImageIO.read(in);

			in = getClass().getResourceAsStream(ship_path);
			SHIP_IMAGE = ImageIO.read(in);
			
			in = getClass().getResourceAsStream(ship_vert_path);
			SHIP_VERT_IMAGE = ImageIO.read(in);
			
			in = getClass().getResourceAsStream(rdot_path);
			DOT_RED = ImageIO.read(in);
			
			in = getClass().getResourceAsStream(bdot_path);
			DOT_BLUE = ImageIO.read(in);
			
			in = getClass().getResourceAsStream(bg_path);
			BG_IMAGE = ImageIO.read(in);
			
			in = getClass().getResourceAsStream(bg2_path);
			BG2_IMAGE = ImageIO.read(in);
			
			in.close();

		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}

}
