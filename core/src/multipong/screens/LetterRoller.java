package multipong.screens;

public class LetterRoller {

	public String[] letterList = { "a", "b", "c", "d", "e", "f", "g", "h", "i",
			"j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
			"x", "y", "z", "<-" };

	private int selected = 0;
	private int last = letterList.length - 1;

	private String letters;

	public void delete() {
		if (letters.length() != 0) {
			letters.substring(0, letters.length() - 1);
		}
	}

	public void down() {
		if (selected == last) {
			selected = 0;
		} else {
			selected++;
		}
	}

	public void enter() {
		letters += letterList[selected];
	}

	public String getLetters() {
		return letters;
	}

	public void up() {
		if (selected == 0) {
			selected = last;
		} else {
			selected--;
		}
	}
}
