package decked.view;

import java.util.ArrayList;
import java.util.List;

import com.agpfd.decked.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import decked.activity.TitleActivity;
import decked.mechanics.TextDisplay;
import decked.mechanics.ToggleButton;
import decked.mechanics.Board;
import decked.mechanics.Card;
import decked.mechanics.CardSlot;
import decked.mechanics.BarDisplay;
import decked.mechanics.Player;
import decked.mechanics.SoundManager;
import decked.mechanics.Tuple;

/**
 * Represents the game state.
 * @version v0.10
 */
public class GameView extends View {
	private SoundManager sm;
	private Context context;
	private List<CardSlot> cardSlots = new ArrayList<CardSlot>();
	
	private int movingCardIndex = -1;
	private Tuple<Integer, Integer> movingCoords;
	private Tuple<Integer, Integer> downCoords;

	private float scale;
	private Paint paint;
	private ArrayList<TextDisplay> texts = new ArrayList<TextDisplay>();
	
	private BarDisplay plaHealth;
	private BarDisplay plaEnergy;
	private BarDisplay oppHealth;
	private BarDisplay oppEnergy;
	
	private ToggleButton readyButton;
	private ToggleButton soundIcon;
	
	private Player player = new Player();
	private Player oppPlayer = new Player();
	private Board board;
	
	private Card currentCard;
	private Card currOppCard;
	
	/** Constructor of the Game View */
	public GameView(Context newContext) {
		super(newContext);
		context = newContext;
	}
	
	/** Called when the screen size is changed */
	@Override
    public void onSizeChanged (int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int screenW = w;
        int screenH = h;
        
        int buttonOn, buttonOff, x, y, scaledWidth, scaledHeight;
        movingCoords = new Tuple<Integer, Integer>(-1, -1);
        downCoords = new Tuple<Integer, Integer>(-1, -1);
        
        sm = new SoundManager();
        
        int boardW = 4;
        int boardH = 3;
		x = screenW/2 - (boardW/2 * screenW/20);
		y = 5 * screenH/50;
		int tileSize = screenW/20;
		
        board = new Board(player, oppPlayer, x, y, tileSize, boardW, boardH);
        
        // Initialize the ready button
        scaledWidth = (int) (screenW/2);
        scaledHeight = (int) (scaledWidth/4);
		x = screenW/2 - scaledWidth/2;
		y = 9*screenH/10;
		buttonOn = getResources().getIdentifier("button_ready_a", "drawable", context.getPackageName());
		buttonOff = getResources().getIdentifier("button_ready_b", "drawable", context.getPackageName());
		readyButton = new ToggleButton(context, buttonOn, buttonOff, new Rect(x, y, x + scaledWidth, y + scaledHeight));
        
		// Initialize the Sound Icon
		scaledWidth = (int) (screenW / 10);
		scaledHeight = scaledWidth;
		x = (int) (screenW * .05);
		y = (int) (screenH * .90);
        buttonOn = getResources().getIdentifier("sound_icon_a", "drawable", context.getPackageName());
        buttonOff = getResources().getIdentifier("sound_icon_b", "drawable", context.getPackageName());
        soundIcon = new ToggleButton(context, buttonOn, buttonOff, new Rect(x, y, x + scaledWidth, y + scaledHeight));
        
        // Initialize the cards for both players
        scaledWidth = (int) (screenW/6.5);
        scaledHeight = (int) (scaledWidth*1.4);
        player.initCards(context, getResources(), scaledWidth, scaledHeight, screenW, screenH);
        oppPlayer.initCards(context, getResources(), scaledWidth, scaledHeight, screenW, screenH);

        initCardSlots();
        
        // Initialize empty CardSlots to place cards in
        x = 1 * screenW/4 - scaledWidth/2;
        y = (int) (.68 * screenH);
        cardSlots.add(new CardSlot(context, R.drawable.card_slot_1, new Rect(x, y, x + scaledWidth, y + scaledHeight)));

        x = 2 * screenW/4 - scaledWidth/2;
        cardSlots.add(new CardSlot(context, R.drawable.card_slot_2, new Rect(x, y, x + scaledWidth, y + scaledHeight)));

        x = 3 * screenW/4 - scaledWidth/2;
        cardSlots.add(new CardSlot(context, R.drawable.card_slot_3, new Rect(x, y, x + scaledWidth, y + scaledHeight)));
        
        // Ready paint for text initializing
		scale = context.getResources().getDisplayMetrics().density;
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.FILL);
		paint.setTextSize(screenH / 45);
		paint.setFakeBoldText(true);
		

		
		// Initialize the text displays
		paint.setTextAlign(Paint.Align.CENTER);
        x = (int) (1 * screenW/4);
        y = (int) (4 * screenH/50 + paint.getTextSize()/2);
        texts.add(new TextDisplay(context, "Player", paint, x, y));
		
        x = (int) (3 * screenW/4);
        texts.add(new TextDisplay(context, "Opponent", paint, x, y));

//        x = (int) (2 * screenW/20 - paint.measureText("Hp: "));
//        y = (int) (5 * screenH/50 + paint.getTextSize());
//        texts.add(new TextDisplay(context, "Hp: ", paint, x, y));
//
//        x = (int) (2 * screenW/20 - paint.measureText("Energy: "));
//        y = (int) (8 * screenH/50 + paint.getTextSize());
//        texts.add(new TextDisplay(context, "Energy: ", paint, x, y));
//
//        x = (int) (14 * screenW/20 - paint.measureText("Hp: "));
//        y = (int) (5 * screenH/50 + paint.getTextSize());
//        texts.add(new TextDisplay(context, "Hp: ", paint, x, y));
//
//        x = (int) (14 * screenW/20 - paint.measureText("Energy: "));
//        y = (int) (8 * screenH/50 + paint.getTextSize());
//        texts.add(new TextDisplay(context, "Energy: ", paint, x, y));
        
        initBars(screenW, screenH, paint);
    }

    /** Places the cards used by the player onto the canvas */
	private void initCardSlots() {
		for (int i = 0; i < 10; i++) {
			// Put the card into a CardSlot and add the CardSlot to cardSlots list
			cardSlots.add(new CardSlot(context, player.getCardsInPlay().get(i)));
		}
		
		for (int i = 0; i < cardSlots.size(); i++) {
			cardSlots.get(i).setCurrentSlot(i);
		}
	}
	
	/** Creates the Bars for energy and health for player and opponent */
	private void initBars(int screenW, int screenH, Paint paint) {
		int x1, x2, y1, y2, w, h;
		x1 = 2 * screenW/20;
		x2 = 14 * screenW/20;
		y1 = 5 * screenH/50;
		y2 = 8 * screenH/50;
		w = 2 * screenW/8;
		h = screenH/50;
		plaHealth = new BarDisplay(context, x1, y1, w, h, 0xff00ff00, "Hp: ", paint);
		plaEnergy = new BarDisplay(context, x1, y2, w, h, 0xff00ffff, "Energy: ", paint);
		oppHealth = new BarDisplay(context, x2, y1, w, h, 0xff00ff00, "Hp: ", paint);
		oppEnergy = new BarDisplay(context, x2, y2, w, h, 0xff00ffff, "Energy: ", paint);
	}

	/** Puts the cards back into their original cardSlot */
//	private void resetCards() {
//		for(int i = 0; i < cardSlots.size(); i++) {
//			cardSlots.get(i).removeCCard();
//		}
//		for(int i = 0; i < player.getCardsInPlay().size(); i++) {
//			cardSlots.get(i).setCCard(player.getCardsInPlay().get(i));
//		}
//	}

	/** The render method for the GameView */
	@Override
	protected void onDraw(Canvas canvas) {
		// draw Player and Opponent Text
		for (TextDisplay text : texts) {
			text.render(canvas);
		}

		// draw bar display
		plaHealth.render(canvas, paint, player.getHP(), player.getMaxHP());
		plaEnergy.render(canvas, paint, player.getEnergy(), player.getMaxEnergy());
		oppHealth.render(canvas, paint, oppPlayer.getHP(), oppPlayer.getMaxHP());
		oppEnergy.render(canvas, paint, oppPlayer.getEnergy(), oppPlayer.getMaxEnergy());
		
		// draw Board and RenderBoard on top if applicable
		board.render(canvas, player.getCurrentPos(), oppPlayer.getCurrentPos());
		
		if(currentCard != null) {
			board.renderRangeBoard(canvas, player.getCurrentPos(), oppPlayer.getCurrentPos(), currentCard,  currOppCard, board);
		}

		readyButton.render(canvas);
		soundIcon.render(canvas);
		
		// draw CardSlots
		for (int i = 0; i < cardSlots.size(); i++) {
				cardSlots.get(i).render(canvas);
		}
		
		// draw your hand
		int j = -1;
		for (int i = 0; i < player.getCardsInPlay().size(); i++) {
			if (i != movingCardIndex) {
				Card card = player.getCardsInPlay().get(i);
				card.render(canvas);
			} else {
				j = i;
			}
		}
		// draw moving card last so it's on top of everything
		if (j != -1)
			player.getCardsInPlay().get(j).render(canvas, movingCoords);
	}
	
	/** Runs a card with higher priority first */
	private void runByPriority(Card playerCard, Card oppCard) {
		byte playerHP = (byte) player.getHP();
		byte oppHP = (byte) player.getHP();
		
		// If player's card has lower priority, it goes second
		if(playerCard.getCardPriority() < oppCard.getCardPriority()) {
			oppCard.runCardAction(oppCard.getId(), oppPlayer, player, board);
			playerCard.runCardAction(playerCard.getId(), player, oppPlayer, board);
		} else {
			// Otherwise it goes first (in a tie, player goes first)
			playerCard.runCardAction(playerCard.getId(), player, oppPlayer, board);
			oppCard.runCardAction(oppCard.getId(), oppPlayer, player, board);
		}
		
		// If they take damage player the damage sound
		if (playerHP != (byte) player.getHP() || oppHP != (byte) oppPlayer.getHP())
			sm.playSound(context, sm.hit);
	}
	
	private void showAreYouSureDialog() {
		// Set up Dialog settings
		final Dialog sureDialog = new Dialog(context);
		sureDialog.setCanceledOnTouchOutside(false);
		sureDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		sureDialog.setContentView(R.layout.areyousure);
		// 2 Buttons
		Button yesButton = (Button) sureDialog.findViewById(R.id.yesButton);
		Button noButton = (Button) sureDialog.findViewById(R.id.noButton);
		
		// Create a listener for if the yes button is chosen
		yesButton.setOnClickListener(new View.OnClickListener() {
			// If so...
			public void onClick(final View view) {
				sureDialog.dismiss();
				
				/* Randomize 3 unique cards for the opponent to play */
				// Make 3 random cards for opponent to choose and ensure that they are 3 different cards
				int rand1 = (int) Math.floor(Math.random()*10);
				int tempRand = (int) Math.floor(Math.random()*10);
				// first card must be different from second
				while(rand1 == tempRand) {
					tempRand = (int) Math.floor(Math.random()*10);
				}
				final int rand2 = tempRand;
				
				tempRand = (int) Math.floor(Math.random()*10);
				// first and second cards must be different from third
				while(rand1 == tempRand || rand2 == tempRand) {
					tempRand = (int) Math.floor(Math.random()*10);
				}
				final int rand3 = tempRand;
				
				// Play the first round by
                //     setting the currentCard to the card. This allows currentCard's range to be drawn onto the board
				currentCard = cardSlots.get(10).getCCard();
				currOppCard = oppPlayer.getCardsInPlay().get(rand1);
				
				//     setting the range of the card to wherever the player is currently (for hitting an opponent)
				currentCard.setRelativeRange(currentCard.getId(), player.getCurrentPos(), board);
				currOppCard.setRelativeRange(currOppCard.getId(), oppPlayer.getCurrentPos(), board);
				
				//     perform both your attack and your opponent's attack
				runByPriority(currentCard, currOppCard);
				
				Toast.makeText(context, "You played " + currentCard.getName(), Toast.LENGTH_SHORT).show();
				// reset blocking in case player played a block
				player.setIsBlocking(false);
				oppPlayer.setIsBlocking(false);
				
				// Need to check if the enemy's HP is 0 after each card played for a victory Toast
				if(oppPlayer.getHP() == 0) {
					endGameDialog(true);
				} else if(player.getHP() == 0) {
					endGameDialog(false);
				}
				// If you didn't win, play the other two cards.
				else {
					// Threads are used to properly get a delay after each card without ruining the design
					view.post(new Runnable() {
				        public void run() {
			            	// Two second delay between each card
			                SystemClock.sleep(2000);
			                // Play second card similar to playing the first card: range, setting currentCard, and playing the card
							currentCard = cardSlots.get(11).getCCard();
							currOppCard = oppPlayer.getCardsInPlay().get(rand2);
							currentCard.setRelativeRange(currentCard.getId(), player.getCurrentPos(), board);
							currOppCard.setRelativeRange(currOppCard.getId(), oppPlayer.getCurrentPos(), board);
							runByPriority(currentCard, currOppCard);
							Toast.makeText(context, "You played " + currentCard.getName(), Toast.LENGTH_SHORT).show();
														
							// reset blocking in case player played a block
							player.setIsBlocking(false);
							oppPlayer.setIsBlocking(false);
							
							// Victory Condition
							if(oppPlayer.getHP() == 0) {
								endGameDialog(true);
							} else if(player.getHP() == 0) {
								endGameDialog(false);
							}
							// If you didn't win, play the last card
							else {
								// New thread for last card
				                view.post(new Runnable() {
							        public void run() {
						            	// Two second delay between each card
						                SystemClock.sleep(2000);
										// Play the third card like the first two cards
										currentCard = cardSlots.get(12).getCCard();
										currentCard.setRelativeRange(currentCard.getId(), player.getCurrentPos(), board);
										
										currOppCard = oppPlayer.getCardsInPlay().get(rand3);
										currOppCard.setRelativeRange(currOppCard.getId(), oppPlayer.getCurrentPos(), board);
										
										runByPriority(currentCard, currOppCard);
										
										Toast.makeText(context, "You played " + currentCard.getName(), Toast.LENGTH_SHORT).show();
										
										// reset blocking in case player played a block
										player.setIsBlocking(false);
										oppPlayer.setIsBlocking(false);
										
										if(oppPlayer.getHP() == 0) {
											endGameDialog(true);
										} else if(player.getHP() == 0) {
											endGameDialog(false);
										}
									 	// Draw onto the screen
										invalidate();
										
										// End the inner thread
							            try {
									        this.finalize();
							            } catch (Exception e) {
							                e.printStackTrace();
							            } catch (Throwable e) {
											e.printStackTrace();
										}
							        }
							    });
							}
							invalidate();
								
				            try {
						        this.finalize();
				            } catch (Exception e) {
				                e.printStackTrace();
				            } catch (Throwable e) {
								e.printStackTrace();
							}
				        }
				    });			
				}
						player.regainEnergy(10);
						oppPlayer.regainEnergy(10);
						// TODO: Fix resetCards
//						resetCards();

						invalidate();
			        }
				});
		
		// Create a listener for the no button and have it just dismiss the dialog
		noButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				sureDialog.dismiss();
			}
		});
		sureDialog.show();
	}
	
	
	
	public void endGameDialog(boolean victory) {
		final Dialog endGameDialog = new Dialog(context);
		endGameDialog.setCanceledOnTouchOutside(false);
		endGameDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		if(victory) {
			endGameDialog.setContentView(R.layout.endgame);
		} else {
			endGameDialog.setContentView(R.layout.endgameopp);
		}
		// 1 Buttons
		Button playAgainButton = (Button) endGameDialog.findViewById(R.id.playAgainButton);
		
		// Create a listener for if the yes button is chosen
		playAgainButton.setOnClickListener(new View.OnClickListener() {
			// If so...
			public void onClick(final View view) {
				//endGameDialog.dismiss();
				Intent titleIntent = new Intent(context, TitleActivity.class);
				context.startActivity(titleIntent);
	        }
			
		
		});
		endGameDialog.show();

	}

	
	/** Is called when the screen is being interacted with in some way */
	public boolean onTouchEvent(MotionEvent event) {
		int eventaction = event.getAction();
		// Coordinates of where it is touched
		int touchX = (int) event.getX();
		int touchY = (int) event.getY();
		
		switch(eventaction) {
			// A finger is pressed down onto the screen
			case MotionEvent.ACTION_DOWN:
				System.out.println("Touching Down: (" + touchX + ", " + touchY + ")");
				downCoords.set(touchX, touchY);
				
				// Detected on the Ready Button
	        	if (readyButton.getDim().contains(touchX, touchY)) {
	        		readyButton.turnOn();
	        	}
				
				// CARD COLLISION DETECTION
				for (int i = 0; i < player.getCardsInPlay().size(); i++) {
					if (player.getCardsInPlay().get(i).getDim().contains(touchX, touchY)) {
						movingCardIndex = i;
						movingCoords.set(touchX - (int) (30*scale), touchY - (int) (70*scale));
					}
				}
				break;
				
			// A finger is moving across the screen, so we just change the coordinates
			case MotionEvent.ACTION_MOVE:
				movingCoords.set(touchX - (int) (30*scale), touchY - (int) (70*scale));
				cardSlots.get(movingCardIndex).getCCard().setPos(movingCoords);
				break;
				
			// When the finger is released
			case MotionEvent.ACTION_UP:
				System.out.println("Touching Up");
				currentCard = null;
	
				// Ready Button Collision: Show confirmation dialog
				if(readyButton.isOn()) {
					if(cardSlots.get(10).getCCard() != null && cardSlots.get(11).getCCard() != null && cardSlots.get(12).getCCard() != null) {
						showAreYouSureDialog();
					} else {
						Toast.makeText(context, "You must select 3 cards before you fight!", Toast.LENGTH_LONG).show();
					}
				}
				readyButton.turnOff();
				
				// Sound Icon Collision: Toggle volume
				if (soundIcon.getDim().contains(touchX, touchY)) {
					soundIcon.toggle();
					sm.toggleSounds();
				}
				
				// Card Slot Collision: Move the moving card to a certain place
				if(movingCardIndex != -1) {
					for(int i = 0; i < cardSlots.size(); i++) {
						if (cardSlots.get(i).getDim().contains(touchX, touchY) && cardSlots.get(i).getCCard() == null) {
							System.out.println("Moving card index: " + movingCardIndex);
							// This uses movingCardIndex as an index for cardSlots, even though movingCardIndex is an index in cardsInPlay.
							// This means that we have to ensure all empty CardSlots in cardSlots are at the end of cardSlots
							cardSlots.get(cardSlots.get(movingCardIndex).getCurrentSlot()).removeCCard();
							cardSlots.get(movingCardIndex).setCurrentSlot(i);
							
							// Move the MovingCard to the newSlot
							cardSlots.get(i).setCCard(player.getCardsInPlay().get(movingCardIndex));
							
							// Play Sound
							sm.playSound(context, sm.cardPlace);
						}
					}
				}
				movingCardIndex = -1;
				break;
		}
		invalidate();
		return true;
	}
}
