package com.rs.battleship.gui;

import java.awt.Point;

import javax.swing.JOptionPane;

import com.rs.battleship.main.Main;



@SuppressWarnings("serial")
public class LocalGame extends Game
{
	public LocalGame()
	{
		super();
		
		enemy.placeRandomShips(enemyGrid);
	}
	
	public void performOnQuit()
	{
		Main.backToMain();
	}

	public void performOnReady()
	{
		placingShips = false;
		gui.updateMode(true);
		readyButton.setEnabled(false);
	}

	public void newGame()
	{
		super.newGame();
		enemy.newTurn();
		enemy.placeRandomShips(enemyGrid);
	}

	public String fire(Point mouse)
	{
		String l = super.fire(mouse);
		
		if(enemy.allShipsDestroyed())
		{
			int n = JOptionPane.showConfirmDialog(
				    null,
				    "Congratulations, you win!\nPlay again?",
				    "Game Over!",
				    JOptionPane.YES_NO_OPTION);
			
			if(n == 0)
			{
				newGame();
			}
			else 
			{
				Main.backToMain();
			}
			
			return "";
		}
		if(!l.equals(""))
		{
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			enemyMove();
		}
		
		return l;
	}

	/**
	 * CPU AI logic
	 */
	public void enemyMove()
    {
		if(!super.placingShips)
		{
			switch(enemy.getState())
	    	{
	    		case RANDOM:
					enemy.randomMove(playerGrid, player);
	    			break;
	    		case HORIZ_RIGHT:
	    			enemy.horizRightMove(playerGrid, player);
					break;
				case HORIZ_LEFT:
					enemy.horizLeftMove(playerGrid, player);
					break;
				case VERT_BELOW:
					enemy.vertBelowMove(playerGrid, player);
					break;
				case VERT_ABOVE:
					enemy.vertAboveMove(playerGrid, player);
					break;
	    	}
		}
		
		if(player.allShipsDestroyed())
		{
			int n = JOptionPane.showConfirmDialog(
				    null,
				    "Sorry, you lose!\nPlay again?",
				    "Game Over!",
				    JOptionPane.YES_NO_OPTION);
			
			if(n == 0)
			{
				newGame();
			}
			else 
			{
				Main.backToMain();
			}
		}
    }
}
