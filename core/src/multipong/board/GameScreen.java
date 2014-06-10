package multipong.board;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen extends AbstractGameScreen {

	int width, height;

	List<KeyMap> availableKeyMaps;

	List<Board> shownBoards = new ArrayList<Board>();
	List<Board> hiddenBoards = new ArrayList<Board>();

	BoardRenderer renderer;

	public GameScreen(Game game, int width, int height) {
		super(game);
		this.width = width;
		this.height = height;

		renderer = new BoardRenderer(width, height, shownBoards);

		availableKeyMaps = loadKeyMaps();

		Board firstBoard = new Board(0, 0, width, height);
		shownBoards.add(firstBoard);
		Gdx.app.debug("First board", firstBoard.toString());
	}

	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		deltaTime = Math.min(0.06f, Gdx.graphics.getDeltaTime());

		KeyMap newPlayerKeys = getNewPlayerKeys();

		if (newPlayerKeys != null) {
			addNewPlayer(newPlayerKeys);
		}

		if (!hiddenBoards.isEmpty() && allBoardsArePaused()) {
			showHiddenBoards();
			resumeAllPlayableBoards();
		}

		for (Board board : shownBoards) {
			board.update(deltaTime);
		}

		renderer.render(deltaTime);

	}

	private void addNewPlayer(KeyMap newPlayerKeys) {

		Board boardWithoutChallanger = getBoardWithoutChallanger();

		if (boardWithoutChallanger != null) {

			// There is a shown or hidden board without challanger
			Gdx.app.debug("Found board without challanger",
					boardWithoutChallanger.toString());
			boardWithoutChallanger.addChallanger(newPlayerKeys);

			// Unpause the board if it is visible, otherwise, set all boards to
			// pause when current rounds are finished.
			if (shownBoards.contains(boardWithoutChallanger)) {
				boardWithoutChallanger.resume();
				Gdx.app.debug("Board without challanger is a shown board",
						boardWithoutChallanger.toString());
			} else {
				Gdx.app.debug("Board without challanger is a hidden board",
						boardWithoutChallanger.toString());
				pauseAllBoardsWhenRoundWon();
			}
			return;
		}

		Board emptyBoard = getEmptyBoard();
		if (emptyBoard != null) {
			// There is a shown or hidden empty board
			Gdx.app.debug("Found empty board", emptyBoard.toString());
			emptyBoard.addFirstPlayer(newPlayerKeys);

			// Board still lacks challanger so do not unpause it
			return;
		}

		// There are no boards left for new player, create a new hidden one
		Board hiddenBoard = new Board();
		Gdx.app.debug("Created new hidden board", hiddenBoard.toString());
		hiddenBoard.addFirstPlayer(newPlayerKeys);
		hiddenBoards.add(hiddenBoard);

		// There must be an even number of boards.
		int totalAmountBoards = shownBoards.size() + hiddenBoards.size();
		if (totalAmountBoards > 1 && (totalAmountBoards % 2) != 0) {
			hiddenBoards.add(new Board());
			Gdx.app.debug("Added extra hidden board", hiddenBoard.toString());
		}

	}

	private void showHiddenBoards() {
		// recalc boards
		shownBoards.addAll(hiddenBoards);
		hiddenBoards.clear();

		List<Board> recalculatedBoards = new ArrayList<Board>();

		int amountBoards = shownBoards.size();

		int rows = (amountBoards > 2) ? 2 : 1;
		int columns;
		if (amountBoards == 1) {
			columns = 1;
		} else if (amountBoards == 2) {
			columns = 2;
		} else {
			columns = amountBoards / 2;
		}

		Gdx.app.debug("Amount shown boards" + amountBoards, "rows " + rows
				+ " columns " + columns);

		float boardWidth = width / columns;
		float boardHeight = height / rows;

		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < columns; x++) {
				if (shownBoards.isEmpty()) {
					break;
				}

				Board currentBoard = shownBoards.get(0);
				shownBoards.remove(0);
				currentBoard.recalculateBoardGeometry(x * boardWidth, y
						* boardHeight, boardWidth, boardHeight);

				Gdx.app.debug("Setting board geometry ", String.format(
						"x=%s y=%s width=%s height=%s", x * boardWidth, y
								* boardHeight, boardWidth, boardHeight));
				recalculatedBoards.add(currentBoard);
			}
		}
		shownBoards.addAll(recalculatedBoards);
	}

	@Override
	public void show() {
	}

	private KeyMap getNewPlayerKeys() {
		for (KeyMap keys : availableKeyMaps) {
			if (Gdx.input.isKeyPressed(keys.enterKey)) {
				// TODO: simultaneous press possible, use a list probably
				availableKeyMaps.remove(keys);
				return keys;
			}
		}
		return null;
	}

	private Board getEmptyBoard() {
		for (Board board : shownBoards) {
			if (!board.hasFirstPlayer()) {
				return board;
			}
		}
		for (Board board : hiddenBoards) {
			if (!board.hasFirstPlayer()) {
				return board;
			}
		}
		return null;
	}

	private Board getBoardWithoutChallanger() {
		for (Board board : shownBoards) {
			if (board.hasFirstPlayer() && !board.hasChallanger()) {
				return board;
			}
		}
		for (Board board : hiddenBoards) {
			if (board.hasFirstPlayer() && !board.hasChallanger()) {
				return board;
			}
		}
		return null;
	}

	private boolean allBoardsArePaused() {
		for (Board board : shownBoards) {
			if (!board.isPaused()) {
				return false;
			}
		}
		return true;
	}

	private void pauseAllBoardsWhenRoundWon() {
		for (Board board : shownBoards) {
			board.pauseWhenRoundWon();
		}
	}

	private void resumeAllPlayableBoards() {
		for (Board board : shownBoards) {
			if (board.isPlayable()) {
				board.resume();
			}
		}
	}

	@Override
	public void hide() {
		renderer.dispose();
	}

	private static List<KeyMap> loadKeyMaps() {
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

		availableKeyMaps.add(new KeyMap(Keys.LEFT_BRACKET, Keys.APOSTROPHE,
				Keys.MINUS));
		availableKeyMaps.add(new KeyMap(Keys.RIGHT_BRACKET, Keys.ENTER,
				Keys.EQUALS));
		return availableKeyMaps;
	}
}
