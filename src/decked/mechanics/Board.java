package decked.mechanics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Represents a game board and displays it on a canvas.
 * @version v0.20
 */
public class Board {
    private int tableTileWidth;
    private int tableTileHeight;
    private int boardSize;
    private int tileSize;
    private int x, y;
    
	/** Constructor */
    public Board(Player player1, Player player2, int newX, int newY, int size, int newTileWidth, int newTileHeight) {
    	super();
    	x = newX;
    	y = newY;
    	tileSize = size;
    	tableTileWidth = newTileWidth;
    	tableTileHeight = newTileHeight;

    	boardSize = tableTileWidth * tableTileHeight;
    	player1.setCurrentPos(4);
    	player2.setCurrentPos(7);
    }

	/** Simple Constructor */
    public Board(int newWidth, int newHeight) {
    	tableTileWidth = newWidth;
    	tableTileHeight = newHeight;

    	boardSize = tableTileWidth * tableTileHeight;
    }

	/** Gettors */
    public int getBoardWidth() { return tableTileWidth; }
    public int getBoardHeight() { return tableTileHeight; }

	/** Returns a new position relative to the current position */
    public int moveRight(int curr) {
        if((curr + 1) % tableTileWidth == 0)
            return curr;
        else
            return curr + 1;
    }

	/** Returns a new position relative to the current position */
    public int moveLeft(int curr) {
        if((curr) % tableTileWidth == 0)
            return curr;
        else
            return curr - 1;
    }

	/** Returns a new position relative to the current position */
    public int moveDown(int curr) {
        if(curr + tableTileWidth >= boardSize)
            return curr;
        else
            return curr + tableTileWidth;
    }

	/** Returns a new position relative to the current position */
    public int moveUp(int curr) {
        if(curr - tableTileWidth < 0)
            return curr;
        else
            return curr - tableTileWidth;
    }

	/** Get a position based on the current position using a mathematical coordinate system */
    public int getRelativePos(int current, int x, int y) {
        int tempCurrent;
        // Check x direction
        for(int i = 0; i < Math.abs(x); i++) {
        	// To the right
            if(x > 0) {
                tempCurrent = current;
                current = moveRight(current);
                // check that the space is not off the board
                if(tempCurrent == current) {
                    return -1;
                }
            }
            // To the left
            else if(x < 0) {
                tempCurrent = current;
                current = moveLeft(current);
                // check that the space is not off the board
                if(tempCurrent == current) {
                    return -1;
                }
            }
        }

        // Check y direction
        for(int j = 0; j < Math.abs(y); j++) {
            // Above
            if(y > 0) {
                tempCurrent = current;
                current = moveUp(current);
                // check that the space is not off the board
                if(tempCurrent == current) {
                    return -1;
                }
            } 
            // Below
            else if(y < 0) {
                tempCurrent = current;
                current = moveDown(current);
                // check that the space is not off the board
                if(tempCurrent == current) {
                    return -1;
                }
            }
        }
        return current;
    }
    
    /**
     * Draws the board
     * @param dimension: the dimensions of the square on the grid
     */
    public void render(Canvas canvas, int player1Pos, int player2Pos) {
    	int left = x;
    	int top = y;
    	int right = left + tileSize;
    	int bottom = top + tileSize;
    	
		Rect square = new Rect(left, top, right, bottom);
		Paint paint = new Paint();
		paint.setColor(0xff888888);
		int radius = (int)((0.75 * tileSize)/2);
		int circleX;
		int circleY;
		int index = 0;
		
		// For each grid location
		for(int i = 0; i < tableTileHeight; i++) {
			for(int j = 0; j < tableTileWidth; j++) {
				// Make a gray square
				square.left = left + j * tileSize;
				square.top = top + i * tileSize;
				square.right = right + j * tileSize;
				square.bottom = bottom + i * tileSize;
				paint.setColor(0xffaaaaaa);
				drawSquareBorder(canvas, square);
				canvas.drawRect(square.left + 1, square.top + 1, square.right - 1, square.bottom - 1, paint);
				
				// Draw the player
				if(index == player1Pos) {
					// Draw circle
					paint.setColor(0xffaa2222);
					circleX = ((int)((square.right - square.left) / 2))  + square.left;
					circleY = ((int)((square.bottom - square.top) / 2)) + square.top;
					drawCircleBorder(canvas, circleX, circleY, radius);
					canvas.drawCircle(circleX, circleY, radius, paint);
				}
				// Draw the opponent
				else if(index == player2Pos) {
					// Draw circle
					paint.setColor(0xffaaaaff);
					circleX = ((int)((square.right - square.left) / 2))  + square.left;
					circleY = ((int)((square.bottom - square.top) / 2)) + square.top;
					drawCircleBorder(canvas, circleX, circleY, radius);
					canvas.drawCircle(circleX, circleY, radius, paint);
				}
				index++;
			}
		}
    }
    
    
    
    
    /** 
     * Draws the board with ranges of a card included into it
     *  @params dimension, the dimensions of a tile square
     */
    public void renderRangeBoard(Canvas canvas, int player1Pos, int player2Pos, Card card, Card oppCard, Board board) {
    	int left = x;
    	int top = y;
    	int right = left + tileSize;
    	int bottom = top + tileSize;
    	
		Rect square = new Rect(left, top, right, bottom);
		Paint paint = new Paint();
		int index = 0;		
		int radius = (int)((0.75 * tileSize)/2);
		int circleX;
		int circleY;
		
		// For each square tile
		for(int i = 0; i < tableTileHeight; i++) {
			for(int j = 0; j < tableTileWidth; j++) {
				// Set square coords
				square.left = left + j * tileSize;
				square.top = top + i * tileSize;
				square.right = right + j * tileSize;
				square.bottom = bottom + i * tileSize;
				
				/******************* TILE COLORING *******************/
				// Both players use this tile
				if (!card.getAffectedSpaces().isEmpty() && index == card.getAffectedSpaces().get(0) && 
						!oppCard.getAffectedSpaces().isEmpty() && index == oppCard.getAffectedSpaces().get(0)) {

					// Color Double Attacks a unique color
					if (card.getCardPriority() == 1 && oppCard.getCardPriority() == 1) {
						paint.setColor(0xffbb88bb);
						drawSquareBorder(canvas, square);
						canvas.drawRect(square.left + 2, square.top + 2, square.right - 2, square.bottom - 2, paint);
						
						card.getAffectedSpaces().remove(0);
						oppCard.getAffectedSpaces().remove(0);
					}
					// Color Your Attack card with higher priority
					else if (card.getCardPriority() == 1 && oppCard.getCardPriority() != 1) {
						paint.setColor(0xffff8888);
						drawSquareBorder(canvas, square);
						canvas.drawRect(square.left + 2, square.top + 2, square.right - 2, square.bottom - 2, paint);
						
						card.getAffectedSpaces().remove(0);
						oppCard.getAffectedSpaces().remove(0);
					}
					// Color Opponent Attack card with higher priority
					else if (card.getCardPriority() != 1 && oppCard.getCardPriority() == 1) {
						paint.setColor(0xff8888ff);
						drawSquareBorder(canvas, square);
						canvas.drawRect(square.left + 2, square.top + 2, square.right - 2, square.bottom - 2, paint);
						
						card.getAffectedSpaces().remove(0);
						oppCard.getAffectedSpaces().remove(0);
					}
					// Color Movement tiles with medium priority
					else if (card.getCardPriority() == 2 || oppCard.getCardPriority() == 2) {
						paint.setColor(0xff00ff00);
						drawSquareBorder(canvas, square);
						canvas.drawRect(square.left + 2, square.top + 2, square.right - 2, square.bottom - 2, paint);
						
						card.getAffectedSpaces().remove(0);
						oppCard.getAffectedSpaces().remove(0);
					}
					// Color Utility tiles with lowest priority
					else if (card.getCardPriority() == 3 || oppCard.getCardPriority() == 3) {
						paint.setColor(0xff00ffff);
						drawSquareBorder(canvas, square);
						canvas.drawRect(square.left + 2, square.top + 2, square.right - 2, square.bottom - 2, paint);
						
						card.getAffectedSpaces().remove(0);
						oppCard.getAffectedSpaces().remove(0);
					} 
				}
				
				// Player only uses this tile
				else if (!card.getAffectedSpaces().isEmpty() && index == card.getAffectedSpaces().get(0)) {
					if (card.getCardPriority() == 1) {
						// Player Attack Color
						paint.setColor(0xffff8888);
						drawSquareBorder(canvas, square);
						canvas.drawRect(square.left + 2, square.top + 2, square.right - 2, square.bottom - 2, paint);
						
						card.getAffectedSpaces().remove(0);
					} else if (card.getCardPriority() == 2) {
						// Movement Color
						paint.setColor(0xff00ff00);
						drawSquareBorder(canvas, square);
						canvas.drawRect(square.left + 2, square.top + 2, square.right - 2, square.bottom - 2, paint);

						card.getAffectedSpaces().remove(0);
					} else if (card.getCardPriority() == 3) {
						// Utility Color
						paint.setColor(0xff00ffff);
						drawSquareBorder(canvas, square);
						canvas.drawRect(square.left + 2, square.top + 2, square.right - 2, square.bottom - 2, paint);

						card.getAffectedSpaces().remove(0);
					}
				}
				
				// Opponent only uses this tile
				else if (!oppCard.getAffectedSpaces().isEmpty() && index == oppCard.getAffectedSpaces().get(0)) {
					if (oppCard.getCardPriority() == 1) {
						// Opponent Player Attack Color
						paint.setColor(0xff8888ff);
						drawSquareBorder(canvas, square);
						canvas.drawRect(square.left + 2, square.top + 2, square.right - 2, square.bottom - 2, paint);
						
						oppCard.getAffectedSpaces().remove(0);
					} else if (oppCard.getCardPriority() == 2) {
						// Movement Color
						paint.setColor(0xff00ff00);
						drawSquareBorder(canvas, square);
						canvas.drawRect(square.left + 2, square.top + 2, square.right - 2, square.bottom - 2, paint);

						oppCard.getAffectedSpaces().remove(0);
					} else if (oppCard.getCardPriority() == 3) {
						// Utility Color
						paint.setColor(0xff00ffff);
						drawSquareBorder(canvas, square);
						canvas.drawRect(square.left + 2, square.top + 2, square.right - 2, square.bottom - 2, paint);

						oppCard.getAffectedSpaces().remove(0);
					}
				}
				// Draw the player
				if(index == player1Pos) {
					// Draw circle
					paint.setColor(0xffaa2222);
					circleX = ((int)((square.right - square.left) / 2))  + square.left;
					circleY = ((int)((square.bottom - square.top) / 2)) + square.top;
					drawCircleBorder(canvas, circleX, circleY, radius);
					canvas.drawCircle(circleX, circleY, radius, paint);
				} 
				// Draw the opponent
				else if(index == player2Pos) {
					// Draw circle
					paint.setColor(0xffaaaaff);
					circleX = ((int)((square.right - square.left) / 2))  + square.left;
					circleY = ((int)((square.bottom - square.top) / 2)) + square.top;
					drawCircleBorder(canvas, circleX, circleY, radius);
					canvas.drawCircle(circleX, circleY, radius, paint);
				}
				index++;
			}
		}
    }

    /** Draws a square given the center and the radius */
    public void drawCircleBorder(Canvas canvas, int x, int y, int radius) {
    	Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		canvas.drawCircle(x, y, radius + 1, paint);
    }
    
    /** Draws a black rectangle directly underneath a rectangle */
    public void drawSquareBorder(Canvas canvas, Rect rect) {
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, paint);
	}
}