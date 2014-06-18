package multipong.matchhandlers;

import java.util.ArrayList;
import java.util.List;

import multipong.board.boardobjects.Player;
import multipong.match.Match;
import multipong.utils.KeyMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;

public class DropInMatchHandler {
	private List<Match> visibleMatches = new ArrayList<Match>();
	private List<Match> hiddenMatches = new ArrayList<Match>();

	int width, height;
	String className = this.getClass().getSimpleName();

	public DropInMatchHandler(int width, int height) {
		this.width = width;
		this.height = height;
		Match firstMatch = new Match(0, 0, width, height);
		addMatch(firstMatch);
		Gdx.app.debug(className, "First board: " + firstMatch.toString());
	}

	public void addMatch(Match match) {
		if (getVisibleMatches().isEmpty()) {
			getVisibleMatches().add(match);
		} else {
			hiddenMatches.add(match);
		}
	}

	public void addNewPlayer(String name, KeyMap keyMap, Controller controller) {

		Match boardWithoutChallanger = getMatchWithoutChallanger();

		if (boardWithoutChallanger != null) {

			// There is a shown or hidden board without challanger
			Gdx.app.debug(className, "Found board without challanger: "
					+ boardWithoutChallanger.toString());
			boardWithoutChallanger.addRightPlayer(name, keyMap, controller);

			// Unpause the board if it is visible, otherwise, set all boards to
			// pause when current rounds are finished.
			if (getVisibleMatches().contains(boardWithoutChallanger)) {
				boardWithoutChallanger.resume();
				Gdx.app.debug(className,
						"Board without challanger is a shown board: "
								+ boardWithoutChallanger.toString());
			} else {
				Gdx.app.debug(className,
						"Board without challanger is a hidden board: "
								+ boardWithoutChallanger.toString());
				pauseAllMatchesWhenRoundWon();
			}
			return;
		}

		Match emptyBoard = getEmptyMatches();
		if (emptyBoard != null) {
			// There is a shown or hidden empty board
			Gdx.app.debug(className,
					"Found empty board: " + emptyBoard.toString());
			emptyBoard.addLeftPlayer(name, keyMap, controller);

			// Board still lacks challanger so do not unpause it
			return;
		}

		// There are no boards left for new player, create a new hidden one
		Match hiddenBoard = new Match();
		Gdx.app.debug(className,
				"Created new hidden board: " + hiddenBoard.toString());
		hiddenBoard.addLeftPlayer(name, keyMap, controller);
		hiddenMatches.add(hiddenBoard);

		// There must be an even number of boards.
		int totalAmountBoards = getVisibleMatches().size()
				+ hiddenMatches.size();
		if (totalAmountBoards > 1 && (totalAmountBoards % 2) != 0) {
			hiddenMatches.add(new Match());
			Gdx.app.debug(className,
					"Added extra hidden board: " + hiddenBoard.toString());
		}

	}

	public boolean allVisibleMatchesAreFinished() {
		for (Match match : visibleMatches) {
			if (!match.isFinished() && match.hasLeftPlayer()) {
				return false;
			}
		}
		return true;
	}

	private boolean allVisibleMatchesArePaused() {
		for (Match match : getVisibleMatches()) {
			if (!match.isPaused()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * TODO
	 * 
	 * @return The finished players
	 */
	public List<Player> closeFinishedMatches() {
		List<Player> finishedPlayers = new ArrayList<Player>();
		List<Match> finishedMatches = new ArrayList<Match>();
		for (Match match : visibleMatches) {
			if (match.isFinished()) {
				finishedMatches.add(match);
				finishedPlayers.add(match.getLeftPlayer());
				finishedPlayers.add(match.getRightPlayer());
			}
		}
		for (Match finishedMatch : finishedMatches) {
			visibleMatches.remove(finishedMatch);
		}
		return finishedPlayers;
	}

	private Match getEmptyMatches() {
		for (Match match : getVisibleMatches()) {
			if (!match.hasLeftPlayer()) {
				return match;
			}
		}
		for (Match match : hiddenMatches) {
			if (!match.hasLeftPlayer()) {
				return match;
			}
		}
		return null;
	}

	private Match getMatchWithoutChallanger() {
		for (Match match : getVisibleMatches()) {
			if (match.hasLeftPlayer() && !match.hasRightPlayer()) {
				return match;
			}
		}
		for (Match match : hiddenMatches) {
			if (match.hasLeftPlayer() && !match.hasRightPlayer()) {
				return match;
			}
		}
		return null;
	}

	public List<Match> getVisibleMatches() {
		return visibleMatches;
	}

	public boolean matchesArePending() {
		for (Match hiddenMatch : hiddenMatches) {
			if (hiddenMatch.hasLeftPlayer()) {
				return true;
			}
		}
		return false;
	}

	public boolean matchHasBeenStartedSinceCreation() {
		for (Match match : visibleMatches) {
			if (match.hasLeftPlayer()) {
				return true;
			}
		}
		return false;
	}

	private void pauseAllMatchesWhenRoundWon() {
		for (Match match : getVisibleMatches()) {
			match.pauseWhenRoundWon();
		}
	}

	private void recalculateAndShowBoards() {
		getVisibleMatches().addAll(hiddenMatches);
		hiddenMatches.clear();

		List<Match> recalcMatches = new ArrayList<Match>();

		int amountBoards = getVisibleMatches().size();

		int rows = (amountBoards > 2) ? 2 : 1;
		int columns;
		if (amountBoards == 1) {
			columns = 1;
		} else if (amountBoards == 2) {
			columns = 2;
		} else {
			columns = amountBoards / 2;
		}

		Gdx.app.debug(className, "Amount shown boards " + amountBoards
				+ " rows " + rows + " columns " + columns);

		float boardWidth = width / columns;
		float boardHeight = height / rows;

		recalcLoop: for (int y = rows - 1; y >= 0; y--) {
			for (int x = 0; x < columns; x++) {
				if (getVisibleMatches().isEmpty()) {
					break recalcLoop;
				}

				Match matchToRecalc = getVisibleMatches().get(0);
				getVisibleMatches().remove(0);

				float nx = x * boardWidth;
				float ny = y * boardHeight;
				matchToRecalc.recalculateBoardGeometry(nx, ny, boardWidth,
						boardHeight);

				recalcMatches.add(matchToRecalc);

				Gdx.app.debug(className, matchToRecalc.toString() + " x=" + nx
						+ " y=" + ny + " width=" + boardWidth + " height="
						+ boardHeight);
			}
		}
		getVisibleMatches().addAll(recalcMatches);
	}

	private void resumeAllPlayableMatches() {
		for (Match match : getVisibleMatches()) {
			if (match.isPlayable()) {
				match.resume();
				if (match.roundHasBeenPlayed()) {
					match.setRedrawCountDown();
				}
			}
		}
	}

	public int numberOfTotalPlayers() {
		int players = 0;
		for (Match match : hiddenMatches) {
			if (match.hasLeftPlayer()) {
				players++;
			}
			if (match.hasRightPlayer()) {
				players++;
			}
		}
		for (Match match : visibleMatches) {
			if (match.hasLeftPlayer()) {
				players++;
			}
			if (match.hasRightPlayer()) {
				players++;
			}
		}
		return players;
	}

	public int numberOfWaitingPlayers() {
		if (hiddenMatches.isEmpty()) {
			return 0;
		}
		int players = 0;
		for (Match match : hiddenMatches) {
			if (match.hasLeftPlayer()) {
				players++;
			}
			if (match.hasRightPlayer()) {
				players++;
			}
		}
		return players;
	}

	public void showBoardsInHiddenMatches() {
		if (!hiddenMatches.isEmpty() && allVisibleMatchesArePaused()) {
			recalculateAndShowBoards();
			resumeAllPlayableMatches();
		}
	}

	public float timeSinceLastFinishedMatchEnded() {
		float minTime = Float.MAX_VALUE;
		for (Match match : visibleMatches) {
			if (match.isFinished()
					&& match.getTimeSinceMatchFinished() < minTime) {
				minTime = match.getTimeSinceMatchFinished();
			}
		}
		return minTime;
	}

	public void updateBoardsInVisibleMatches(float deltaTime) {
		for (Match match : getVisibleMatches()) {
			match.update(deltaTime);
		}
	}

}
