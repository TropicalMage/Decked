package decked.mechanics;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;

/**
 * A player of the game, has health, energy, and cards.
 * @version v0.20
 */
public class Player {
	private int currentPos;
	private int health;
	private int maxHealth;
	private int energy;
	private int maxEnergy;
	private boolean isBlocking;
	private List<Card> cardsInPlay;
	
	/** Basic constructor for a player */
	public Player() {
		health = 100;
		maxHealth = 100;
		energy = 100;
		maxEnergy = 100;
		isBlocking = false;
		cardsInPlay = new ArrayList<Card>();
	}

	/** Gettors for a player */
	public int getCurrentPos() { return currentPos; }
	public int getHP() { return health; }
	public int getMaxHP() { return maxHealth; }
	public int getEnergy() { return energy; }
	public int getMaxEnergy() { return maxEnergy; }
	public List<Card> getCardsInPlay() { return cardsInPlay; }

	/** Setters for a player */
	public void setCurrentPos(int newPosition) { currentPos = newPosition; }
	public void setIsBlocking(boolean bool) { isBlocking = bool; }
	
	/** Has the player lose health based on damage */
	public void takeDamage(int damage) {
		if(isBlocking == true) {
			// Block blocks 15 of opponent's damage
			damage = damage - 15;
			if(damage < 0) {
				damage = 0;
			}
		}
		health = health - damage;
		if(health < 0) {
			health = 0;
		}
	}

	/** Decrease energy */
	public void loseEnergy(int energyLost) {
		energy = energy - energyLost;
		if(energy < 0)
			energy = 0;
	}

	/** Increase energy */
	public void regainEnergy(int energyGained) {
		energy = energy + energyGained;
		if(energy > 100)
			energy = 100;
	}
	
	/** Creates a players 20 cards and sets its relative range */
	public void initCards(Context context, Resources src, int scaledCardW, int scaledCardH, int screenW, int screenH) {
		int x, y, image;
		Rect rect;
		String name;
		
		// Initialize Top Row
		for (int i = 0; i < 5; i++) {
			x = (screenW/5 - scaledCardW)/2 + ((i % 5) * screenW/5);
			y = (int) (0.25 * screenH);
			rect = new Rect(x, y, x + scaledCardW, y + scaledCardH);
			
			// Create utility card and put it at the end of the row
			if(i == 4) {
				name = "Utility Card 1";
				image = src.getIdentifier("uti00", "drawable", context.getPackageName());
				UtilityCard tempCard = new UtilityCard(context, image, cardsInPlay.size(), name, rect);
				cardsInPlay.add(tempCard);
			// Create movement cards
			} else {
				name = "Move Card " + (i + 1);
				image = src.getIdentifier("mov0" + i, "drawable", context.getPackageName());
				MoveCard tempCard = new MoveCard(context, image, cardsInPlay.size(), name, rect);
				cardsInPlay.add(tempCard);
			}
		}
		
		// Initialize Bottom Row
		for (int i = 0; i < 5; i++) {
			x = (screenW/5 - scaledCardW)/2 + ((i % 5) * screenW/5);
			y = (int) (0.45 * screenH);
			rect = new Rect(x, y, x + scaledCardW, y + scaledCardH);
			
			// Create utility card and put it at the end of the row
			if(i == 4) {
				name = "Utility Card 2";
				image = src.getIdentifier("uti01", "drawable", context.getPackageName());
				UtilityCard tempCard = new UtilityCard(context, image, cardsInPlay.size(), name, rect);
				cardsInPlay.add(tempCard);
			// Create attack cards
			} else {
				name = "Attack Card " + (i + 1);
				image = src.getIdentifier("atk0" + i, "drawable", context.getPackageName());
				AttackCard tempCard = new AttackCard(context, image, cardsInPlay.size(), name, rect);
				cardsInPlay.add(tempCard);
			}
		}
	}
}
