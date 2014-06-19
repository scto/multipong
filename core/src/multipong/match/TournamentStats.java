package multipong.match;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import multipong.board.boardobjects.Player;

public class TournamentStats {

	/**
	 * This holds the scores of a match.
	 */
	public class MatchResult {
		int playerScore;
		int opponentScore;

		public MatchResult(int playerScore, int opponentScore) {
			this.playerScore = playerScore;
			this.opponentScore = opponentScore;
		}
	}

	/**
	 * This holds data about a single player, wins, losses and ties against a
	 * specific opponent, as well as total amount of matches played, won,
	 * lost... Also whether or not a player has had a specific opponent.
	 */
	public class PlayerStats {

		public int matchesPlayed = 0;
		public int matchesWon = 0;
		public int matchesLost = 0;
		public int matchesTied = 0;

		public boolean isActive = true;

		public LinkedHashMap<Player, List<MatchResult>> stats = new LinkedHashMap<Player, List<MatchResult>>();

		public void addMatchResult(Player opponent, int playerScore,
				int opponentScore) {

			matchesPlayed++;
			if (playerScore > opponentScore) {
				matchesWon++;
			} else if (playerScore < opponentScore) {
				matchesLost++;
			} else {
				matchesTied++;
			}

			MatchResult result = new MatchResult(playerScore, opponentScore);

			if (stats.containsKey(opponent)) {
				stats.get(opponent).add(result);

			} else {
				List<MatchResult> resultList = new ArrayList<MatchResult>();
				resultList.add(result);
				stats.put(opponent, resultList);
			}
		}

		public boolean playedOpponent(Player opponent) {
			return stats.containsKey(opponent);
		}
	}

	private String className = this.getClass().getSimpleName();
	private LinkedHashMap<Player, PlayerStats> stats = new LinkedHashMap<Player, PlayerStats>();
	private List<Player> leftOutPlayers = new ArrayList<Player>();

	public void addMatchResult(Player leftPlayer, Player rightPlayer,
			int leftScore, int rightScore) {
		if (leftPlayer == null || rightPlayer == null) {
			return;
		}
		if (!stats.containsKey(leftPlayer)) {
			addPlayerToTournament(leftPlayer);
		}
		PlayerStats leftPlayerStats = stats.get(leftPlayer);
		leftPlayerStats.addMatchResult(rightPlayer, leftScore, rightScore);

		if (!stats.containsKey(rightPlayer)) {
			addPlayerToTournament(rightPlayer);
		}
		PlayerStats rightPlayerStats = stats.get(rightPlayer);
		rightPlayerStats.addMatchResult(leftPlayer, rightScore, leftScore);
	}

	public void addPlayerToTournament(Player player) {
		if (player == null) {
			return;
		}
		if (!stats.containsKey(player)) {
			stats.put(player, new PlayerStats());
		} else {
			activatePlayer(player);
		}
	}

	public void activatePlayer(Player player) {
		if (stats.containsKey(player)) {
			stats.get(player).isActive = true;
		}
	}

	public void deactivatePlayer(Player player) {
		if (stats.containsKey(player)) {
			stats.get(player).isActive = false;
		}
	}

	public void deactivateAllPlayers() {
		for (Entry<Player, PlayerStats> entry : stats.entrySet()) {
			entry.getValue().isActive = false;
		}
	}

	public List<Player> getActivePlayers() {
		List<Player> activePlayers = new ArrayList<Player>();
		for (Entry<Player, PlayerStats> entry : stats.entrySet()) {
			if (entry.getValue().isActive) {
				activePlayers.add(entry.getKey());
			}
		}
		return activePlayers;
	}

	public int getPlayerWins(Player player) {
		if (!stats.containsKey(player)) {
			return 0;
		}
		return stats.get(player).matchesWon;
	}

	public int getPlayerLosses(Player player) {
		if (!stats.containsKey(player)) {
			return 0;
		}
		return stats.get(player).matchesLost;
	}

	public int getPlayerTies(Player player) {
		if (!stats.containsKey(player)) {
			return 0;
		}
		return stats.get(player).matchesTied;
	}

	public int getPlayerMatchesPlayed(Player player) {
		if (!stats.containsKey(player)) {
			return 0;
		}
		return stats.get(player).matchesPlayed;
	}

	public List<Player> getAllPlayers() {
		List<Player> allPlayers = new ArrayList<Player>();
		for (Entry<Player, PlayerStats> entry : stats.entrySet()) {
			allPlayers.add(entry.getKey());
		}
		return allPlayers;
	}

	/**
	 * 
	 * @return
	 */
	public List<Player> getPlayersSortedByWinsAndLosses() {

		Comparator<Player> scoreComparator = new Comparator<Player>() {
			@Override
			public int compare(Player p1, Player p2) {
				PlayerStats p1stats = stats.get(p1);
				PlayerStats p2stats = stats.get(p2);
				if (p1stats.matchesWon > p2stats.matchesWon) {
					return -1;
				} else if (p1stats.matchesWon < p2stats.matchesWon) {
					return 1;
				} else {
					// Same number of matches won, sort on losses.
					if (p1stats.matchesLost > p2stats.matchesLost) {
						return 1;
					} else if (p1stats.matchesLost < p2stats.matchesLost) {
						return -1;
					}
				}
				return 0;
			}
		};

		List<Player> players = getAllPlayers();
		Collections.sort(players, scoreComparator);
		return players;
	}

	/**
	 * In this match arrangement, all active players meets a random unmet
	 * player. If there is an odd number of players a random player will be left
	 * out of the match. The same player will not be left out twice until all
	 * players have been left out once.
	 * 
	 * The left out player (array index [0]) will be paired with null (array
	 * index[1]) at the end of the list.
	 * 
	 * If all players have met each other once, the arrangement will be the same
	 * as if none of the players have met.
	 * 
	 * @return
	 */
	public List<Player[]> getArrangementAllVsAll() {
		List<Player[]> matchups = new ArrayList<Player[]>();

		List<Player> activePlayers = getActivePlayers();
		int numberOfPlayersToArrange = activePlayers.size();

		if (numberOfPlayersToArrange <= 1) {
			return matchups;
		}

		Player playerToLeaveOut = null;

		if (isOdd(numberOfPlayersToArrange)) {
			playerToLeaveOut = getPlayerToLeaveOut();
			activePlayers.remove(playerToLeaveOut);
			deactivatePlayer(playerToLeaveOut);
			numberOfPlayersToArrange--;
		}

		// There are now always an even number of players.
		while (true) {
			Player[] pair = new Player[2];

			// Grab a random player and add it as left player
			int leftPlayerIndex = MathUtils
					.random(numberOfPlayersToArrange - 1);
			Player leftPlayer = activePlayers.get(leftPlayerIndex);
			pair[0] = leftPlayer;
			activePlayers.remove(leftPlayerIndex);
			deactivatePlayer(leftPlayer);
			numberOfPlayersToArrange--;

			// Grab a random player among those met least amount of times.
			List<Player> leastMetActivePlayers = getLeastMetActivePlayers(leftPlayer);
			int numberOfLeastMetPlayers = leastMetActivePlayers.size();
			int rightPlayerIndex = MathUtils
					.random(numberOfLeastMetPlayers - 1);
			Player rightPlayer = leastMetActivePlayers.get(rightPlayerIndex);
			pair[1] = rightPlayer;
			activePlayers.remove(rightPlayer);
			deactivatePlayer(rightPlayer);
			numberOfPlayersToArrange--;

			matchups.add(pair);

			if (numberOfPlayersToArrange == 0) {
				break;
			} else if (numberOfPlayersToArrange == 1) {
				// This shouldn't happen.
				Gdx.app.debug(className, "One player is left over!");
				break;
			}
		}
		return matchups;

	}

	// public List<Player[]> getArrangementDoubleElimination() {
	//
	// }
	//
	// public List<Player[]> getArrangementSwiss() {
	//
	// }

	/**
	 * Get a list of least met active players in tournament for this player.
	 * Example: If player has met all players once and some twice, the list
	 * contains the players met only once.
	 * 
	 * @param player
	 * @return
	 */
	public List<Player> getLeastMetActivePlayers(Player player) {
		List<Player> leastMetPlayers = new ArrayList<Player>();
		List<Player> activePlayers = getActivePlayers();

		// This map will contain key "times met" value "opponent", sorted in
		// fewest times met to most.
		TreeMap<Integer, Player> timesMetMap = new TreeMap<Integer, Player>();

		PlayerStats playerStats = stats.get(player);

		for (Player opponent : activePlayers) {
			if (opponent == player) {
				// Opponent is the same as player
				continue;
			}
			if (playerStats.stats.containsKey(opponent)) {
				// Players have met
				int timesMet = playerStats.stats.get(opponent).size();
				timesMetMap.put(new Integer(timesMet), opponent);
			} else {
				timesMetMap.put(new Integer(0), opponent);
			}
		}

		if (timesMetMap.isEmpty()) {
			// Player is the only one active.
			return leastMetPlayers;
		}

		// Add the players met fewest times, e.g. if the players has met three
		// opponents one time, five opponents two times, one opponent three
		// times, add only the three met once.
		int leastNumberOfMatches = Integer.MAX_VALUE;
		for (Entry<Integer, Player> entry : timesMetMap.entrySet()) {

			int timesMet = entry.getKey();
			Player opponent = entry.getValue();

			if (timesMet < leastNumberOfMatches) {
				leastNumberOfMatches = timesMet;
				leastMetPlayers.add(opponent);

			} else if (timesMet == leastNumberOfMatches) {
				leastMetPlayers.add(opponent);

			} else {
				break;
			}
		}
		return leastMetPlayers;
	}

	private Player getPlayerToLeaveOut() {
		List<Player> activePlayers = getActivePlayers();
		List<Player> playersNeverLeftOut = new ArrayList<Player>();

		// Grab all players never left out.
		for (Player player : activePlayers) {
			if (!leftOutPlayers.contains(player)) {
				playersNeverLeftOut.add(player);
			}
		}

		int numberOfPlayersNeverLeftOut = playersNeverLeftOut.size();

		if (numberOfPlayersNeverLeftOut == 0) {
			// All active players have been left out at least once. Grab a
			// random player.
			int numberOfActivePlayers = activePlayers.size();
			int index = MathUtils.random(numberOfActivePlayers - 1);
			Player playerToLeaveOut = activePlayers.get(index);
			leftOutPlayers.clear();
			leftOutPlayers.add(playerToLeaveOut);
			return playerToLeaveOut;

		} else {
			int index = MathUtils.random(numberOfPlayersNeverLeftOut - 1);
			Player playerToLeaveOut = playersNeverLeftOut.get(index);
			leftOutPlayers.add(playerToLeaveOut);
			return playerToLeaveOut;
		}
	}

	/**
	 * Get a list of all unmet active players in tournament for this player. If
	 * player has met all active players, the list is empty.
	 * 
	 * @param player
	 * @return
	 */
	public List<Player> getUnmetActivePlayers(Player player) {
		List<Player> unmetPlayers = new ArrayList<Player>();
		List<Player> activePlayers = getActivePlayers();
		PlayerStats playerStats = stats.get(player);

		for (Player opponent : activePlayers) {
			if (opponent == player) {
				// Active player is the same as player
				continue;
			}
			if (playerStats.stats.containsKey(opponent)) {
				// Players have met
				continue;
			}
			// Player is unmet
			unmetPlayers.add(opponent);
		}
		return unmetPlayers;
	}

	private boolean isOdd(int num) {
		return (num % 2 == 1);
	}

	public boolean playerIsActive(Player player) {
		if (stats.containsKey(player)) {
			return stats.get(player).isActive;
		}
		return false;
	}

	public void reactivatePlayer(Player player) {
		if (stats.containsKey(player)) {
			stats.get(player).isActive = true;
		}
	}

}
