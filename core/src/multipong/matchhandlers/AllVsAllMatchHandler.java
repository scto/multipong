package multipong.matchhandlers;

import java.util.List;
import com.badlogic.gdx.math.Rectangle;

import multipong.board.boardobjects.Player;
import multipong.match.Match;
import multipong.match.TournamentStats;
import multipong.rendering.RenderableRectangle;
import multipong.rendering.RenderableString;
import multipong.screens.RenderableStatScreenObjects;
import multipong.settings.Settings;

public class AllVsAllMatchHandler extends DropInMatchHandler {

	TournamentStats stats = new TournamentStats();

	private RenderableString timeOutMsg;
	private List<RenderableString> statTable;
	private boolean matchesPlaying = false;
	private boolean tournamentFinished = false;

	Rectangle statsWindow;

	// private float stateTime = 0;
	private float timeOutNewPlayerSignUp = Settings.timeOutNewPlayerSignUp;

	public AllVsAllMatchHandler(int width, int height) {
		super(width, height);

		statsWindow = new Rectangle(0, 0, width, height);
		timeOutMsg = RenderableStatScreenObjects.countDownMsg(statsWindow,
				timeOutNewPlayerSignUp);
		RenderableStatScreenObjects.setCountDownMsgToIdle(timeOutMsg);
		updateStatsTableRenderables();
		visibleMatches.clear();
	}

	public void startMatchups(List<Player[]> matchups) {
		if (matchups.isEmpty()) {
			return;
		}
		visibleMatches.clear();
		hiddenMatches.clear();

		for (Player[] pair : matchups) {
			Match match = new Match(0, 0, width, height);
			match.addPlayers(pair[0], pair[1]);
			visibleMatches.add(match);
		}

		recalculateAndShowBoards();
		resumeAllPlayableMatches();

	}

	@Override
	public void addPlayer(Player player) {
		if (!signUpHasTimedOut()) {
			stats.addPlayerToTournament(player);
			resetTimeOut();
			updateStatsTableRenderables();
		}
	}

	public float getTimeOut() {
		return timeOutNewPlayerSignUp;
	}

	public boolean signUpHasTimedOut() {
		return timeOutNewPlayerSignUp <= 0;
	}

	public void resetTimeOut() {
		timeOutNewPlayerSignUp = Settings.timeOutNewPlayerSignUp;
	}

	public boolean gameInProgress() {
		return matchesPlaying;
	}

	public boolean tournamentFinished() {
		return tournamentFinished;
	}

	public void update(float deltaTime) {
		if (tournamentFinished) {
			return;
		}

		// Matches are playing so long as not all visible matches are finished.
		matchesPlaying = !allVisibleMatchesAreFinished();

		if (matchesPlaying) {
			// There are ongoing matches. Update them.
			updateBoardsInVisibleMatches(deltaTime);
			return;
		}

		// There are no ongoing matches from here on.

		if (!visibleMatches.isEmpty()) {
			// There are previous matches which stats have not been stored
			// yet. Do so.
			for (Match match : visibleMatches) {
				// stats.activatePlayer(match.getLeftPlayer());
				// stats.activatePlayer(match.getRightPlayer());
				stats.addMatchResult(match.getLeftPlayer(),
						match.getRightPlayer(), match.getLeftPlayerScore(),
						match.getRightPlayerScore());
			}
			visibleMatches.clear();
			updateStatsTableRenderables();
			updateTimeOutRenderable();
			return;
		}

		if (!signUpHasTimedOut()) {
			// Not all players may have signed up yet. Decrease the countdown.
			if (stats.getActivePlayers().size() < 2) {
				// Do not count down if less than two players are active.
				return;
			}
			timeOutNewPlayerSignUp -= deltaTime;
			updateTimeOutRenderable();
			return;
		}

		// Get the next matchup.
		List<Player[]> matchArrangement = stats
				.getArrangementAllVsAll(Settings.tournamentAllVsAllMaxTimesPlayersMeet);

		if (matchArrangement.size() == 0) {
			// Tournament is finished. Show it.
			tournamentFinished = true;
			updateStatsTableRenderables();
			updateTimeOutRenderable();

		} else {
			// Start new matches
			stats.deactivateAllPlayers();
			startMatchups(matchArrangement);
			matchesPlaying = true;
			resetTimeOut();
		}

	}

	@Override
	public int numberOfTotalPlayers() {
		return stats.getAllPlayers().size();
	}

	private void updateTimeOutRenderable() {
		if (tournamentFinished) {
			RenderableStatScreenObjects.setCountDownMsgToFinished(timeOutMsg);

		} else if (stats.getActivePlayers().size() <= 1) {
			RenderableStatScreenObjects.setCountDownMsgToIdle(timeOutMsg);

		} else {
			RenderableStatScreenObjects.updateCountDownMsg(timeOutMsg,
					timeOutNewPlayerSignUp);
		}
	}

	private void updateStatsTableRenderables() {
		statTable = RenderableStatScreenObjects.statsTable(statsWindow, stats);
	}

	public List<RenderableString> getRenderableStrings() {
		if (matchesPlaying) {
			return super.getRenderableStrings();

		} else {
			renderableStrings.clear();
			renderableStrings.add(timeOutMsg);
			renderableStrings.addAll(statTable);
			return renderableStrings;
		}
	}

	public List<RenderableRectangle> getRenderableRectangles() {
		if (matchesPlaying) {
			return super.getRenderableRectangles();
		} else {
			renderableRectangles.clear();
			return renderableRectangles;
		}
	}

}
