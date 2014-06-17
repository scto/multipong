package multipong.utils;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Input.Keys;

public class KeyMap {
	public int upKey;
	public int downKey;
	public int enterKey;

	public KeyMap(int upKey, int downKey, int enterKey) {
		this.upKey = upKey;
		this.downKey = downKey;
		this.enterKey = enterKey;
	}
	
	public static List<KeyMap> loadKeyMaps() {
		List<KeyMap> availableKeyMaps = new ArrayList<KeyMap>();

		availableKeyMaps.add(new KeyMap(Keys.Q, Keys.A, Keys.NUM_1));
		availableKeyMaps.add(new KeyMap(Keys.W, Keys.S, Keys.NUM_2));

		availableKeyMaps.add(new KeyMap(Keys.E, Keys.D, Keys.NUM_3));
		availableKeyMaps.add(new KeyMap(Keys.R, Keys.F, Keys.NUM_4));

		availableKeyMaps.add(new KeyMap(Keys.T, Keys.G, Keys.NUM_5));
		availableKeyMaps.add(new KeyMap(Keys.Y, Keys.H, Keys.NUM_6));

		availableKeyMaps.add(new KeyMap(Keys.U, Keys.J, Keys.NUM_7));
		availableKeyMaps.add(new KeyMap(Keys.I, Keys.K, Keys.NUM_8));

		availableKeyMaps.add(new KeyMap(Keys.O, Keys.L, Keys.NUM_9));
		availableKeyMaps.add(new KeyMap(Keys.P, Keys.COLON, Keys.NUM_0));

		availableKeyMaps.add(new KeyMap(Keys.UP, Keys.DOWN,
				Keys.ENTER));

		return availableKeyMaps;
	}
}