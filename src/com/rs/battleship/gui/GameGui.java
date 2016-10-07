package com.rs.battleship.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.Timer;

import com.rs.battleship.game.Grid;
import com.rs.battleship.game.Player;
import com.rs.battleship.game.Ship;
import com.rs.battleship.res.ResourceLoader;


@SuppressWarnings("serial")
public class GameGui extends JPanel implements ActionListener 
{
	public static final int WIDTH = Game.WIDTH,
			                HEIGHT = Game.HEIGHT - Grid.WIDTH;
	
	private String selectedOrientation = "horizontal";
	
	private JComboBox<String> shipComboBox;
	private JRadioButton horizRadioButton = new JRadioButton("HORIZONTAL"),
						 vertRadioButton = new JRadioButton("VERTICAL");
	
	private JLabel chooseLabel = new JLabel("Choose ship to place"),
				   instructionLabel = new JLabel("<html>Click on a block to place <br>the selected ship.</html>");
 
	
	private Radar radar = new Radar();

	
	Timer bgTimer;
	
	public GameGui()
	{
		setBackground(new Color(189, 189, 189));
		
		String[] choices = {"PATROL", "SUBMARINE", "BATTLESHIP", "CARRIER"};
    	shipComboBox = new JComboBox<String>(choices);
    	shipComboBox.setSelectedIndex(0);
		
		createGui();
		
		bgTimer = new Timer(30, this);
		bgTimer.start();
	}
	
	/**
	 * Creates and displays GUI
	 */
	public void createGui()
	{
		this.setLayout(null);
		this.setBackground(Color.WHITE);
		
		ButtonGroup group = new ButtonGroup();
		group.add(horizRadioButton);
		group.add(vertRadioButton);
		horizRadioButton.setBackground(Color.WHITE);
		vertRadioButton.setBackground(Color.WHITE);
		horizRadioButton.setSelected(true);
		
		shipComboBox.addActionListener(this);
		horizRadioButton.addActionListener(this);
		vertRadioButton.addActionListener(this);
		
		int start = WIDTH / 2 - 100;
		int size = 200;
		
		radar.setBounds(5, 5, Radar.WIDTH, Radar.WIDTH);
		
		instructionLabel.setBounds(start, 5, size, 30);
		chooseLabel.setBounds(start, 45, size, 25);
		shipComboBox.setBounds(start, 75, size, 25);
		horizRadioButton.setBounds(start, 105, size / 2, 25);
		vertRadioButton.setBounds(start + 100, 105, size / 2, 25);
		
		this.add(radar);
		this.add(instructionLabel);
		this.add(chooseLabel);
		this.add(shipComboBox);
		this.add(horizRadioButton);
		this.add(vertRadioButton);
	}
	
	/**
	 * Returns selected orientation for Ships 
	 * being placed
	 * @return selectedOrientation
	 */
	public String getSelectedOrientation()
	{
		return selectedOrientation;
	}
	
	/**
	 * Returns current Ship selected to be placed
	 * @param player
	 * @return ship
	 */
	public Ship getSelectedShip(Player player)
	{
		String sel = (String)shipComboBox.getSelectedItem();
		sel = sel.toLowerCase();
		
		Ship s = null;
		
		for(Ship sh : player.ships)
			if(sh.toString().equals(sel))
				s = sh;
		
		return s;
	}
	
	/**
	 * Adds a dot to the Radar
	 * @param dc
	 */
	public void addDot(DotCoords dc)
	{
		radar.addDot(dc);
	}
	
	/**
	 * Adds a Component to the GUI in bounds,
	 * used for subclasses of Game
	 * @param c
	 * @param bounds
	 */
	public void addComponent(Component c, Rectangle bounds)
	{
		c.setBounds(bounds);
		add(c);
	}
	
	/**
	 * Updates GUI based on current status of the game (if placing ships
	 * or if firing).
	 * @param changeToFire
	 */
	public void updateMode(boolean changeToFire)
    {
    	if(changeToFire)
    	{
    		chooseLabel.setEnabled(false);
    		horizRadioButton.setEnabled(false);
	    	vertRadioButton.setEnabled(false);
	    	shipComboBox.setEnabled(false);
	    	
	    	
	    	instructionLabel.setText("Click on a block to fire");
    	}
    	else
    	{
    		chooseLabel.setEnabled(true);
    		horizRadioButton.setEnabled(true);
	    	vertRadioButton.setEnabled(true);
	    	shipComboBox.setEnabled(true);
	    	shipComboBox.setSelectedIndex(0);
	    	
	    	instructionLabel.setText("<html>Click on a block to place <br>the selected ship.</html>");
    	}
    }
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		g.drawImage(ResourceLoader.BG2_IMAGE, 0, 0, WIDTH, HEIGHT, null);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == horizRadioButton)
			selectedOrientation = "horizontal";
		else if(e.getSource() == vertRadioButton)
			selectedOrientation = "vertical";
		else if(e.getSource() == shipComboBox)
		{
			horizRadioButton.setSelected(true);
			selectedOrientation = "horizontal";
		}
		else if(e.getSource() == bgTimer)
			this.repaint();
		
	}
	
}
