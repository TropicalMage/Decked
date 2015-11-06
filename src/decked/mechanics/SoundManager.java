package decked.mechanics;

import java.util.HashMap;
import com.agpfd.decked.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
 
public class SoundManager {
	public boolean SOUND_ON = true;
	public final int S1 = R.raw.wow;
	public final int cardPlace = R.raw.cardslap;
	public final int hit = R.raw.strike;
	
	private SoundPool soundPool;
	private HashMap<Integer, Integer> soundPoolMap;
	
	/** Populate the SoundPool*/
	@SuppressLint("UseSparseArrays")
	public void initSounds(Context context) {
		soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>();    

		soundPoolMap.put( S1, soundPool.load(context, R.raw.wow, 1) );
		soundPoolMap.put( cardPlace, soundPool.load(context, R.raw.cardslap, 1) );
		soundPoolMap.put( hit, soundPool.load(context, R.raw.strike, 1) );
	}
	public void playSound(Context context, int soundID) {
		if (SOUND_ON) {
			if (soundPool == null || soundPoolMap == null){
				initSounds(context);
			}
			float volume = (float) 0.5;
			 
			// Params: Sound, LVol, RVol, Priority, Repeats, PlaybackRate
			soundPool.play(soundPoolMap.get(soundID), volume, volume, 1, 0, 1f);
		}
	}
	
	public void toggleSounds() {
		if (SOUND_ON)
			SOUND_ON = false;
		else
			SOUND_ON = true;
	}
}