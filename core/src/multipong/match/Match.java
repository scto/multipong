package multipong.match;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;

import multipong.board.Board;
import multipong.board.BoardUpdater;
import multipong.board.boardobjects.Player;
import multipong.rendering.RenderableRectangle;
import multipong.rendering.RenderableString;
import multipong.settings.Settings;
import multipong.utils.KeyMap;

public class Match {

	public Board board;
	private Player leftPlayer;
	private Player matchWinner;

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

	public void addLeftPlayer(String name, KeyMap leftPlayerKeyMap,
			Controller leftPlayerController) {
		leftPlayer = new Player(name, 0, leftPlayerKeyMap,
				leftPlayerController);
		board.setPlayers(leftPlayer, rightPlayer);
		refreshRenderables();
		paused = true;
	}

	public void addRightPlayer(String name, KeyMap rightPlayerKeyMap,
			Controller rightPlayerController) {
		rightPlayer = new Player(name, 0, rightPlayerKeyMap,
				rightPlayerController);
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

				leftPlayer.incrementScore();

				if (Settings.ballResetsInRoundWinnerDirection) {
					board.ball.resetWithLeftPlayerDirection(board.rightPad
							.getMidPointFacingBoard());
				} else {
					board.ball.resetWithRightPlayerDirection(board.leftPad
							.getMidPointFacingBoard());
				}

			} else {

				rightPlayer.incrementScore();

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
		return leftPlayer.score == Settings.matchScoreToWin
				|| rightPlayer.score == Settings.matchScoreToWin;
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
					board.getBounds(), leftPlayer));
			renderableStrings.add(RenderableMatchObjects.rightPlayerScore(
					board.getBounds(), rightPlayer));
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
		return (leftPlayer.score + rightPlayer.score != 0);
	}

	public void setRedrawCountDown() {
		redrawCountDown = Settings.timeFromRedrawToRoundBegins;
		refreshRenderables();
	}

	public void update(float deltaTime) {
		if (isFinished()) {
			timeSinceMatchFinished += deltaTime;
		}
		if (!paused) {

			stateTime += deltaTime;

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

			BoardUpdater.update(board, deltaTime);

			boolean roundWon = checkForRoundWinner();

			if (roundWon) {
				// Always set match winner when round won, even if match is not
				// finished yet.
				matchWinner = (leftPlayer.score > rightPlayer.score) ? leftPlayer
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
