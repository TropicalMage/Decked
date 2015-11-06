package decked.view;

import decked.activity.GameActivity;
import decked.activity.OptionsActivity;
import decked.mechanics.ToggleButton;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

/**
 * Represents the title state.
 * @version v0.10
 */
public class TitleView extends View {
//	private Bitmap titleGraphic;
	private Context context;
	private ToggleButton playButton;
	private ToggleButton optionsButton;
	
	
	public TitleView(Context newContext) {
		super(newContext);
		context = newContext;
//		titleGraphic = BitmapFactory.decodeResource(getResources(), R.drawable.title_screen);
	}
	
    @Override
    public void onSizeChanged (int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
		int x, y, sWidth, sHeight, buttonUp, buttonDown;
		Rect rect;
        int screenW = w;
        int screenH = h;
        
		sWidth = (int) (screenW/2);
        sHeight = (int) (sWidth/4);
		x = screenW/2 - sWidth/2;
		y = 7*screenH/10;
		
		System.out.println("X: " + x + ", Y: " + y + ", W: " + sWidth + ", H: " + sHeight);
		rect = new Rect(x, y, x + sWidth, y + sHeight);
		buttonUp = getResources().getIdentifier("button_play_a", "drawable", context.getPackageName());
		buttonDown = getResources().getIdentifier("button_play_b", "drawable", context.getPackageName());
		playButton = new ToggleButton(context, buttonUp, buttonDown, rect);

		y = 8*screenH/10;
		rect = new Rect(x, y, x + sWidth, y + sHeight);
		buttonUp = getResources().getIdentifier("button_options_a", "drawable", context.getPackageName());
		buttonDown = getResources().getIdentifier("button_options_b", "drawable", context.getPackageName());
		optionsButton = new ToggleButton(context, buttonUp, buttonDown, rect);
    }
	
	@Override 
	protected void onDraw(Canvas canvas) {
		playButton.render(canvas);
		optionsButton.render(canvas);
//		canvas.drawBitmap(titleGraphic, (screenW-titleGraphic.getWidth())/2, (float) (screenH*0.05), null);
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		int eventaction = event.getAction();
		int touchX = (int)event.getX();
		int touchY = (int)event.getY();
		
		switch(eventaction) {
		case MotionEvent.ACTION_DOWN:
        	if (playButton.getDim().contains(touchX, touchY)) {
        		playButton.turnOn();
        	}
	    	if (optionsButton.getDim().contains(touchX, touchY)) {
	    		optionsButton.turnOn();
	    	}
			break;
			
		case MotionEvent.ACTION_MOVE:
			break;
			
		case MotionEvent.ACTION_UP:
			if(playButton.isOn()) {
				Intent gameIntent = new Intent(context, GameActivity.class);
				context.startActivity(gameIntent);
			}
			if(optionsButton.isOn()) {
				Intent optionsIntent = new Intent(context, OptionsActivity.class);
				context.startActivity(optionsIntent);
			}
			playButton.turnOff();
			optionsButton.turnOff();
			break;
		}
		invalidate();
		return true;
	}
}
