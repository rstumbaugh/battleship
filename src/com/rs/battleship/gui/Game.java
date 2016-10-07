package com.rs.battleship.gui;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.rs.battleship.game.Block;
import com.rs.battleship.game.Grid;
import com.rs.battleship.game.Player;
import com.rs.battleship.game.Ship;
import com.rs.battleship.main.Main;


@SuppressWarnings("serial")
public abstract class Game extends JPanel implements ActionListener, MouseListener
{
	/**
	 * Abstract method to perform when Quit
	 * button is pressed
	 */
	public abstract void performOnQuit();
	/**
	 * Abstract method to perform when Ready
	 * button is pressed
	 */
	public abstract void performOnReady();
	
	public static final int WIDTH = Main.WIDTH,
						    HEIGHT = Main.HEIGHT;
	
	public GameGui gui = new GameGui();
	
	public Player player = new Player(),
				  enemy = new Player();
	
	public Grid playerGrid = new Grid(),
		        enemyGrid = new Grid();
	
	public boolean placingShips = true;
	
	public JButton quitButton = new JButton("QUIT"),
				   readyButton = new JButton("READY"),
				   randomShipsButton = new JButton("PLACE SHIPS RANDOMLY");
	
	private Rectangle playerGridBounds;
	private Rectangle enemyGridBounds;
	
	Timer timer;
	boolean drawPlayerGrid = false;
	boolean drawEnemyGrid = false;
	
	public Game()
	{
		enemy.setEnemy(true);
		
		this.setLayout(null);
		this.addMouseListener(this);
		
		playerGridBounds = new Rectangle(0, 0, Grid.WIDTH, Grid.WIDTH);
		enemyGridBounds = new Rectangle(Grid.WIDTH, 0, Grid.WIDTH, Grid.WIDTH);
		
		Rectangle quitButtonBounds = new Rectangle(GameGui.WIDTH - 225, GameGui.HEIGHT - 125, 200, 50);
		Rectangle randomShipsButtonBounds = new Rectangle((GameGui.WIDTH / 2) - 100, 135, 200, 25);
		Rectangle readyButtonBounds = new Rectangle(randomShipsButtonBounds.x, randomShipsButtonBounds.y + 30, 200, 25);
		
		gui.addComponent(quitButton, quitButtonBounds);
		gui.addComponent(readyButton, readyButtonBounds);
		gui.addComponent(randomShipsButton, randomShipsButtonBounds);
		
		quitButton.addActionListener(this);
		readyButton.addActionListener(this);
		randomShipsButton.addActionListener(this);
		
		playerGrid.setBounds(playerGridBounds);
		enemyGrid.setBounds(enemyGridBounds);
		gui.setBounds(0, Grid.WIDTH, GameGui.WIDTH, GameGui.HEIGHT);
		
		add(playerGrid);
		add(enemyGrid);
		add(gui);
		
		timer = new Timer(30, this);
		timer.start();
	}
	
	/**
	 * Starts drawing the player and enemy Grids
	 */
	public void start()
	{
		drawPlayerGrid = true;
		drawEnemyGrid = true;
	}
	
	/**
	 * Pauses drawing of player and enemy Grids
	 */
	public void pause() 
	{
		drawPlayerGrid = false;
		drawEnemyGrid = false;
	}
	
	/**
	 * Updates graphics on Grids
	 */
	public void updateGrids()
	{
		playerGrid.update();
		
		enemyGrid.update();
	}
	
	/**
	 * Resets all GUIs and variables for
	 * a new game
	 */
	public void newGame()
	{
		player.reset(playerGrid);
		enemy.reset(enemyGrid);
		
		enemy.setReady(false);
		player.setReady(false);
		
		playerGrid.clear();
		enemyGrid.clear();
		
		placingShips = true;
		gui.updateMode(false);
		
		readyButton.setEnabled(true);
		randomShipsButton.setEnabled(true);
		
	}
	
	/**
	 * Places a Ship at the Block on Point mouse,
	 * returns Ship
	 * @param mouse
	 * @return ship
	 */
	public Ship placeShip(Point mouse)
	{
		Ship returnShip = null;
		
		Block blockClicked = playerGrid.getBlockOn(mouse);
		
		if(blockClicked == null || !Grid.isValidLocation(blockClicked.getLoc()))
		{
			JOptionPane.showMessageDialog(null,
				    "You must click on a block!",
				    "",
				    JOptionPane.ERROR_MESSAGE);
			
			return null;
		}
	
		
		String location = blockClicked.getLoc();
		Ship shipSelected = gui.getSelectedShip(player);
		
		if(shipSelected.isPlaced())
		{
			playerGrid.removeShip(shipSelected);
		}
		
		String oldOrientation = shipSelected.getOrientation();
		shipSelected.setOrientation(gui.getSelectedOrientation());

		if(shipSelected.canFitAt(playerGrid, location))
		{
			playerGrid.placeShip(shipSelected, location);
			returnShip = shipSelected;
		}
		else
		{
			if(shipSelected.isPlaced())
			{
				shipSelected.setOrientation(oldOrientation);
				playerGrid.placeShip(shipSelected, shipSelected.getFirstBlock());
			}
			
			JOptionPane.showMessageDialog(null,
				    shipSelected.toString().toUpperCase()+" cannot fit at "+location,
				    "",
				    JOptionPane.ERROR_MESSAGE);
			
		}

		updateGrids();
		return returnShip;
	}
	
	/**
	 * Fires a shot at the Block on Point mouse,
	 * returns location
	 * @param mouse
	 * @return location
	 */
	public String fire(Point mouse)
	{
		int x = (int) (mouse.getX() - Grid.WIDTH);
		int y = (int) mouse.getY();
		
		Block blockClicked = enemyGrid.getBlockOn(new Point(x, y));
		
		if(blockClicked == null || !Grid.isValidLocation(blockClicked.getLoc()))
		{		
			JOptionPane.showMessageDialog(null,
				    "You must click on a block!",
				    "",
				    JOptionPane.ERROR_MESSAGE);
			return "";
		}
		
		String location = blockClicked.getLoc();
		
		if(!player.isGuessed(location))
		{
			enemyGrid.fire(location, enemy);
			player.shotFired(location);
			
			updateGrids();
			return location;
		}
		else
		{
			JOptionPane.showMessageDialog(null,
				    "You have already guessed "+location+"!",
				    "",
				    JOptionPane.ERROR_MESSAGE);
			
			return "";
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == timer)
		{
			if(drawPlayerGrid)
			{
				playerGrid.update();
			}
			if(drawEnemyGrid)
			{
				enemyGrid.update();
			}
		}
		else if(e.getSource() == quitButton)
		{
			performOnQuit();
		}
		else if(e.getSource() == readyButton)
		{
			if(player.allShipsPlaced())
			{
				randomShipsButton.setEnabled(false);
				
				performOnReady();
			}
			
			else
			{
				JOptionPane.showMessageDialog(
						null,
						"You must place all your ships first!",
						"",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		else if(e.getSource() == randomShipsButton)
		{
			player.reset(playerGrid);
			player.placeRandomShips(playerGrid);
		}
	}

	public void mousePressed(MouseEvent e) 
	{
		Point mouse = e.getPoint();
		
		if(playerGridBounds.contains(mouse))
		{
			if(!placingShips)
			{
				JOptionPane.showMessageDialog(null,
					    "You cannot fire in your own grid!",
					    "",
					    JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				placeShip(mouse);
			}
		}
		else if(enemyGridBounds.contains(mouse))
		{
			if(placingShips)
			{
				JOptionPane.showMessageDialog(null,
					    "You must place ships in your grid!",
					    "",
					    JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				fire(mouse);
			}
		}
	}
	
	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
	
}
