package decked.view;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

public class OptionsView extends View {
	/** Constructor of the Options View */
	public OptionsView(Context context) {
		super(context);
	}
	
	/** Called when the screen size is changed */
    @Override
    public void onSizeChanged (int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int screenW = w;
        int screenH = h;
    }
	@Override 
	protected void onDraw(Canvas canvas) {
	
	}
	public boolean onTouchEvent(MotionEvent event) {
		int eventaction = event.getAction();
		int touchX = (int)event.getX();
		int touchY = (int)event.getY();

		switch(eventaction) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				break;
		}
		invalidate();
		return true;
	}
}