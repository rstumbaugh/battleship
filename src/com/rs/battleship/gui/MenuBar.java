/**
 * @author Ryan
 * @date Aug 24, 2013
*/
package com.rs.battleship.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.rs.battleship.settings.Settings;


@SuppressWarnings("serial")
public class MenuBar extends JMenuBar implements ActionListener
{
	private JMenu settingsMenu = new JMenu("Settings");
		private JMenuItem soundItem = new JMenuItem("Sound...");
		
	public MenuBar()
	{
		this.add(settingsMenu);
			settingsMenu.add(soundItem);
			
		soundItem.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == soundItem)
		{
			String sound = Settings.soundOn() ? "ON" : "OFF";
			String opp = Settings.soundOn() ? "OFF" : "ON";
			
			String msg = "Sound is currently "+sound+".\nWould you like to turn sound "+opp+"?";
			
			int n = JOptionPane.showConfirmDialog(
				    null,
				    msg,
				    "Sound",
				    JOptionPane.YES_NO_OPTION);
			
			if(n == 0)
			{
				Settings.setSound(!Settings.soundOn());
			}
		}
	}
}
