package multipong.board;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;

public class BoardHandler {
	List<Board> visibleBoards = new ArrayList<Board>();
	List<Board> hiddenBoards = new ArrayList<Board>();

	int width, height;

	public BoardHandler(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void addBoard(Board board) {
		if (visibleBoards.isEmpty()) {
			visibleBoards.add(board);
		} else {
			hiddenBoards.add(board);
		}
	}

	public void updateBoards(float deltaTime) {
		for (Board board : visibleBoards) {
			board.update(deltaTime);
		}
	}

	public void addNewPlayer(KeyMap newPlayerKeys) {

		Board boardWithoutChallanger = getBoardWithoutChallanger();

		if (boardWithoutChallanger != null) {

			// There is a shown or hidden board without challanger
			Gdx.app.debug("Found board without challanger",
					boardWithoutChallanger.toString());
			boardWithoutChallanger.addChallanger(newPlayerKeys);

			// Unpause the board if it is visible, otherwise, set all boards to
			// pause when current rounds are finished.
			if (visibleBoards.contains(boardWithoutChallanger)) {
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
		int totalAmountBoards = visibleBoards.size() + hiddenBoards.size();
		if (totalAmountBoards > 1 && (totalAmountBoards % 2) != 0) {
			hiddenBoards.add(new Board());
			Gdx.app.debug("Added extra hidden board", hiddenBoard.toString());
		}


	}
	
	public void showHiddenBoards() {
		if (!hiddenBoards.isEmpty() && allBoardsArePaused()) {
			recalculateAndShowBoards();
			resumeAllPlayableBoards();
		}
	}

	private void recalculateAndShowBoards() {
		// recalc boards
		visibleBoards.addAll(hiddenBoards);
		hiddenBoards.clear();

		List<Board> recalculatedBoards = new ArrayList<Board>();

		int amountBoards = visibleBoards.size();

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
				if (visibleBoards.isEmpty()) {
					break;
				}

				Board currentBoard = visibleBoards.get(0);
				visibleBoards.remove(0);
				currentBoard.recalculateBoardGeometry(x * boardWidth, y
						* boardHeight, boardWidth, boardHeight);

				Gdx.app.debug("Setting board geometry ", String.format(
						"x=%s y=%s width=%s height=%s", x * boardWidth, y
								* boardHeight, boardWidth, boardHeight));
				recalculatedBoards.add(currentBoard);
			}
		}
		visibleBoards.addAll(recalculatedBoards);
	}

	private Board getEmptyBoard() {
		for (Board board : visibleBoards) {
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
		for (Board board : visibleBoards) {
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
		for (Board board : visibleBoards) {
			if (!board.isPaused()) {
				return false;
			}
		}
		return true;
	}

	private void pauseAllBoardsWhenRoundWon() {
		for (Board board : visibleBoards) {
			board.pauseWhenRoundWon();
		}
	}

	private void resumeAllPlayableBoards() {
		for (Board board : visibleBoards) {
			if (board.isPlayable()) {
				board.resume();
			}
		}
	}
}
