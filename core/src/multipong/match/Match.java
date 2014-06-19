package multipong.match;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import multipong.board.Board;
import multipong.board.BoardUpdater;
import multipong.board.boardobjects.Player;
import multipong.rendering.RenderableRectangle;
import multipong.rendering.RenderableString;
import multipong.settings.Settings;

public class Match {

	public Board board;
	private Player leftPlayer;
	private Player matchWinner;
	
	private int leftPlayerScore = 0;
	private int rightPlayerScore = 0;

	private boolean paused = true;

	private boolean pauseWhenRoundWon = false;
	public float redrawCountDown = 0;

	public List<RenderableRectangle> renderableRectangles = new ArrayList<RenderableRectangle>();
	public List<RenderableString> renderableStrings = new ArrayList<RenderableString>();
	private RenderableString countDownMsg;

	private Player rightPlayer;
	private float stateTime = 0;
	private float timeSinceMatchFinished = 0;

	private String className = this.getClass().getSimpleName();

	private boolean needsRefreshAfterStartCountdown = true;

	private boolean needsRefreshAfterRedrawCountdown = true;

	public Match() {
		board = new Board();
	}

	public Match(float x, float y, float width, float height) {
		recalculateBoardGeometry(x, y, width, height);
	}
	
	public void addPlayers(Player leftPlayer, Player rightPlayer) {
		this.leftPlayer = leftPlayer;
		this.rightPlayer = rightPlayer;
		board.setPlayers(leftPlayer, rightPlayer);
		refreshRenderables();
		paused = true;
	}

	public void addLeftPlayer(Player player) {
		leftPlayer = player;
		board.setPlayers(leftPlayer, rightPlayer);
		refreshRenderables();
		paused = true;
	}

	public void addRightPlayer(Player player) {
		rightPlayer = player;
		board.setPlayers(leftPlayer, rightPlayer);
		refreshRenderables();
		paused = true;
	}

	private boolean checkForRoundWinner() {

		boolean roundWon = false;
		Player roundWinner = BoardUpdater.getRoundWinner(board);

		if (roundWinner != null) {

			if (Settings.padResetsToCenterAfterRound) {
				board.leftPad.reset();
				board.rightPad.reset();
			}

			if (roundWinner == leftPlayer) {

				leftPlayerScore++;

				if (Settings.ballResetsInRoundWinnerDirection) {
					board.ball.resetWithLeftPlayerDirection(board.rightPad
							.getMidPointFacingBoard());
				} else {
					board.ball.resetWithRightPlayerDirection(board.leftPad
							.getMidPointFacingBoard());
				}

			} else {

				rightPlayerScore++;

				if (Settings.ballResetsInRoundWinnerDirection) {
					board.ball.resetWithRightPlayerDirection(board.leftPad
							.getMidPointFacingBoard());
				} else {
					board.ball.resetWithLeftPlayerDirection(board.rightPad
							.getMidPointFacingBoard());
				}
			}

			roundWon = true;
		}
		return roundWon;

	}

	public Player getLeftPlayer() {
		return leftPlayer;
	}

	public Player getMatchWinner() {
		return matchWinner;
	}

	public Player getRightPlayer() {
		return rightPlayer;
	}

	public float getTimeSinceMatchFinished() {
		return timeSinceMatchFinished;
	}

	public boolean hasLeftPlayer() {
		return leftPlayer != null;
	}

	public boolean hasRedrawCountDown() {
		return redrawCountDown > 0;
	}

	public boolean hasRightPlayer() {
		return rightPlayer != null;
	}

	public boolean hasStartCountDown() {
		return stateTime <= Settings.timeMatchStartCountDownFrom;
	}

	public boolean isFinished() {
		if (leftPlayer == null || rightPlayer == null) {
			return false;
		}
		return leftPlayerScore == Settings.matchScoreToWin
				|| rightPlayerScore == Settings.matchScoreToWin;
	}

	public boolean isPaused() {
		return paused;
	}

	public boolean isPlayable() {
		return board != null && leftPlayer != null && rightPlayer != null;
	}

	public void pauseWhenRoundWon() {
		pauseWhenRoundWon = true;
	}

	public void recalculateBoardGeometry(float x, float y, float width,
			float height) {
		// TODO: maybe set pads to previous place if applicable...
		board = new Board(x, y, width, height);
		board.setPlayers(leftPlayer, rightPlayer);
		refreshRenderables();
	}

	private void refreshRenderables() {
		if (board.field == null) {
			Gdx.app.debug(className,
					"Not refreshing renderables for " + this.toString());
			return;
		}
		Gdx.app.debug(className,
				"Refreshing renderables for " + this.toString());

		renderableRectangles.clear();
		renderableStrings.clear();

		renderableRectangles.add(RenderableMatchObjects.field(board.field
				.getBounds()));

		if (isFinished()) {
			Gdx.app.debug(className, "Refreshing with state: Match finished "
					+ this.toString());
			if (board == null || board.field == null) {
				Gdx.app.debug(className, "board");
			}
			if (matchWinner == null) {
				Gdx.app.debug(className, "winner");
			}
			renderableStrings.add(RenderableMatchObjects.matchWinnerMsg(
					board.getBounds(), getMatchWinner().name));

		} else if (!hasLeftPlayer() && !hasRightPlayer()) {
			Gdx.app.debug(className, "Refreshing with state: No players "
					+ this.toString());
			renderableStrings.add(RenderableMatchObjects
					.leftSideWaitingMsg(board.getBounds()));
			renderableStrings.add(RenderableMatchObjects
					.rightSideWaitingMsg(board.getBounds()));

		} else if (hasLeftPlayer() && !hasRightPlayer()) {
			Gdx.app.debug(className, "Refreshing with state: No right player "
					+ this.toString());
			renderableStrings.add(RenderableMatchObjects
					.rightSideWaitingMsg(board.getBounds()));
			renderableStrings.add(RenderableMatchObjects.leftPlayerName(
					board.getBounds(), leftPlayer.name));
			renderableRectangles.add(RenderableMatchObjects
					.leftPad(board.leftPad.getBounds()));

		} else if (isPlayable()) {
			Gdx.app.debug(className, "Refreshing with state: Is playable "
					+ this.toString());
			renderableStrings.add(RenderableMatchObjects.leftPlayerName(
					board.getBounds(), leftPlayer.name));
			renderableStrings.add(RenderableMatchObjects.rightPlayerName(
					board.getBounds(), rightPlayer.name));
			renderableStrings.add(RenderableMatchObjects.leftPlayerScore(
					board.getBounds(), leftPlayerScore));
			renderableStrings.add(RenderableMatchObjects.rightPlayerScore(
					board.getBounds(), rightPlayerScore));
			renderableRectangles.add(RenderableMatchObjects
					.leftPad(board.leftPad.getBounds()));
			renderableRectangles.add(RenderableMatchObjects
					.rightPad(board.rightPad.getBounds()));

			if (hasStartCountDown()) {
				countDownMsg = RenderableMatchObjects.countDownMsg(
						board.getBounds(), 0);
				renderableStrings.add(countDownMsg);

			} else if (hasRedrawCountDown()) {
				countDownMsg = RenderableMatchObjects.countDownMsg(
						board.getBounds(), redrawCountDown);
				renderableStrings.add(countDownMsg);

			} else {
				renderableRectangles.add(RenderableMatchObjects.ball(board.ball
						.getBounds()));
			}
		}
	}

	public void resume() {
		pauseWhenRoundWon = false;
		paused = false;
		refreshRenderables();
	}

	public boolean roundHasBeenPlayed() {
		return (leftPlayerScore + rightPlayerScore != 0);
	}

	public void setRedrawCountDown() {
		redrawCountDown = Settings.timeFromRedrawToRoundBegins;
		refreshRenderables();
	}
	
	public int getLeftPlayerScore() {
		return leftPlayerScore;
	}
	
	public int getRightPlayerScore() {
		return rightPlayerScore;
	}

	public void update(float deltaTime) {
		if (isFinished()) {
			timeSinceMatchFinished += deltaTime;
		}
		if (!paused) {
			stateTime += deltaTime;

			BoardUpdater.updatePads(board, deltaTime);

			if (hasStartCountDown()) {
				needsRefreshAfterStartCountdown = true;
				RenderableMatchObjects.updateCountDownMsg(countDownMsg,
						Settings.timeMatchStartCountDownFrom - stateTime);
				return;
			}

			if (needsRefreshAfterStartCountdown) {
				refreshRenderables();
				needsRefreshAfterStartCountdown = false;
			}

			if (hasRedrawCountDown()) {
				redrawCountDown -= deltaTime;
				needsRefreshAfterRedrawCountdown = true;
				RenderableMatchObjects.updateCountDownMsg(countDownMsg,
						redrawCountDown);
				return;
			}

			if (needsRefreshAfterRedrawCountdown) {
				refreshRenderables();
				needsRefreshAfterRedrawCountdown = false;
			}

			BoardUpdater.updateBall(board, deltaTime);

			boolean roundWon = checkForRoundWinner();

			if (roundWon) {
				// Always set match winner when round won, even if match is not
				// finished yet.
				matchWinner = (leftPlayerScore > rightPlayerScore) ? leftPlayer
						: rightPlayer;
				refreshRenderables();
			}

			if (roundWon && pauseWhenRoundWon) {
				paused = true;
				refreshRenderables();
			}

			if (isFinished()) {
				paused = true;
				refreshRenderables();
				return;
			}
		}
	}

}
