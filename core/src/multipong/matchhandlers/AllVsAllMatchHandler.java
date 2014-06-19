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

	Rectangle statsWindow;

	// private float stateTime = 0;
	private float timeOutNewPlayerSignUp = Settings.timeOutNewPlayerSignUp;

	public AllVsAllMatchHandler(int width, int height) {
		super(width, height);

		statsWindow = new Rectangle(0, 0, width, height);
		timeOutMsg = RenderableStatScreenObjects.countDownMsg(statsWindow,
				timeOutNewPlayerSignUp);
		updateStatsTableRenderables();
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

	public void update(float deltaTime) {
		// stateTime += deltaTime;

		if (!signUpHasTimedOut()) {
			if (stats.getActivePlayers().size() <= 1) {
				return;
			}
			timeOutNewPlayerSignUp -= deltaTime;
			updateTimeOutRenderable();
			return;

		} else if (!matchesPlaying) {
			// Start the matches
			List<Player[]> allVsAllArrangement = stats.getArrangementAllVsAll();
			startMatchups(allVsAllArrangement);
			matchesPlaying = true;
			return;
		}

		// Matches are playing so long as not all visible matches are finished.
		matchesPlaying = !allVisibleMatchesAreFinished();
		if (!matchesPlaying) {
			resetTimeOut();
			stats.deactivateAllPlayers();
			for (Match match : visibleMatches) {
				stats.addMatchResult(match.getLeftPlayer(),
						match.getRightPlayer(), match.getLeftPlayerScore(),
						match.getRightPlayerScore());
			}
			visibleMatches.clear();
			updateStatsTableRenderables();
			updateTimeOutRenderable();
		} else {
			updateBoardsInVisibleMatches(deltaTime);
		}
	}

	@Override
	public int numberOfTotalPlayers() {
		return stats.getAllPlayers().size();
	}

	private void updateTimeOutRenderable() {
		RenderableStatScreenObjects.updateCountDownMsg(timeOutMsg,
				timeOutNewPlayerSignUp);
	}

	private void updateStatsTableRenderables() {
		statTable = RenderableStatScreenObjects.statsTable(statsWindow, stats);
	}

	public List<RenderableString> getRenderableStrings() {
		if (!signUpHasTimedOut()) {
			renderableStrings.clear();
			renderableStrings.add(timeOutMsg);
			renderableStrings.addAll(statTable);
			return renderableStrings;
		} else {
			return super.getRenderableStrings();
		}
	}

	public List<RenderableRectangle> getRenderableRectangles() {
		if (!signUpHasTimedOut()) {
			renderableRectangles.clear();
			return renderableRectangles;
		} else {
			return super.getRenderableRectangles();
		}
	}

}
