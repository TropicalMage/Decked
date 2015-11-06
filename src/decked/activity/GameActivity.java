package decked.activity;

import com.agpfd.decked.R;

import decked.view.GameView;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
/**
 * Creates a view for the game.
 * @version v0.10
 */
public class GameActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        GameView gView = new GameView(this);
        gView.setKeepScreenOn(true);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        					 WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawableResource(R.drawable.game_backdrop);
        setContentView(gView);

    }
}