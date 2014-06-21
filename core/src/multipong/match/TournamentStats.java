package multipong.match;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import multipong.board.boardobjects.Player;

/**
 * A tournament statistics class. Keeps track of which players have played
 * against each other, how many times different players have met, what the
 * scores of the matches were, how many matches a player has won, whether or not
 * a player has dropped-out, dropped-in, and so on.
 * 
 * It does not decide who has won a tournament, since it does not keep track of
 * what kind of tournament it is. Instead, use its pairing methods to generate
 * lists of pairings for a specific kind of tournament. When the pairings
 * methods returns an empty list, the tournament is over.
 * 
 * A new instance of this class should be created after a tournament is
 * finished, or when switching tournament type, since it depends on earlier
 * statistics when creating new pairings.
 * 
 * This class does not support ties.
 * 
 * This class is not thread-safe.
 */
public class TournamentStats {

	/**
	 * This holds the scores of a match.
	 */
	public class MatchResult {
		public int playerScore;
		public int opponentScore;

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

		public boolean isActive = true;

		public Map<Player, List<MatchResult>> matchResultMap = new LinkedHashMap<Player, List<MatchResult>>();

		public void addMatchResult(Player opponent, int playerScore,
				int opponentScore) {

			matchesPlayed++;
			if (playerScore > opponentScore) {
				matchesWon++;
			} else if (playerScore < opponentScore) {
				matchesLost++;
			}

			MatchResult result = new MatchResult(playerScore, opponentScore);

			if (matchResultMap.containsKey(opponent)) {
				matchResultMap.get(opponent).add(result);

			} else {
				List<MatchResult> resultList = new ArrayList<MatchResult>();
				resultList.add(result);
				matchResultMap.put(opponent, resultList);
			}
		}

		public boolean playerMetOpponent(Player opponent) {
			return matchResultMap.containsKey(opponent);
		}
	}

	private String className = this.getClass().getSimpleName();
	private Map<Player, PlayerStats> stats = new LinkedHashMap<Player, PlayerStats>();
	private List<Player> leftOutPlayers = new ArrayList<Player>();

	/**
	 * Reactivate all deactivated players.
	 */
	public void activateAllPlayers() {
		for (Entry<Player, PlayerStats> entry : stats.entrySet()) {
			entry.getValue().isActive = true;
		}
	}

	/**
	 * Reactivate a deactivated player. If player is not in the tournament it
	 * does nothing.
	 * 
	 * @param player
	 */
	public void activatePlayer(Player player) {
		if (playerIsRegistered(player)) {
			stats.get(player).isActive = true;
		}
	}

	/**
	 * Activate all players in the list. If player is not in the tournament it
	 * does nothing.
	 * 
	 * @param players
	 */
	public void activatePlayers(List<Player> players) {
		for (Player player : players) {
			activatePlayer(player);
		}
	}

	/**
	 * Adds the result of a match between two players to the statistics. If a
	 * player does not exist, it registers the player first.
	 * 
	 * @param leftPlayer
	 * @param rightPlayer
	 * @param leftScore
	 * @param rightScore
	 */
	public void addMatchResult(Player leftPlayer, Player rightPlayer,
			int leftScore, int rightScore) {
		if (leftPlayer == null || rightPlayer == null) {
			return;
		}
		if (!playerIsRegistered(leftPlayer)) {
			addPlayerToTournament(leftPlayer);
		}
		PlayerStats leftPlayerStats = stats.get(leftPlayer);
		leftPlayerStats.addMatchResult(rightPlayer, leftScore, rightScore);

		if (!playerIsRegistered(rightPlayer)) {
			addPlayerToTournament(rightPlayer);
		}
		PlayerStats rightPlayerStats = stats.get(rightPlayer);
		rightPlayerStats.addMatchResult(leftPlayer, rightScore, leftScore);
	}

	/**
	 * Adds a player to the tournament. If the player already exists, it
	 * activates the player.
	 * 
	 * @param player
	 */
	public void addPlayerToTournament(Player player) {
		if (player == null) {
			return;
		}
		if (!playerIsRegistered(player)) {
			stats.put(player, new PlayerStats());
		} else {
			activatePlayer(player);
		}
	}

	/**
	 * True if all active players have met each other.
	 * 
	 * @return
	 */
	public boolean allActivePlayersHaveMetEachOther() {
		// TODO: Use something more efficient, like Tarjan's Algorithm.
		List<Player> activePlayers = getActivePlayers();

		for (Player player : activePlayers) {

			PlayerStats playerStats = stats.get(player);

			for (Player opponent : activePlayers) {
				if (player == opponent) {
					continue;
				}
				if (!playerStats.playerMetOpponent(opponent)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Deactivates all players in the tournament.
	 */
	public void deactivateAllPlayers() {
		for (Entry<Player, PlayerStats> entry : stats.entrySet()) {
			entry.getValue().isActive = false;
		}
	}

	/**
	 * Deactivates a player from the tournament. If player is not in the
	 * tournament it does nothing.
	 * 
	 * @param player
	 */
	public void deactivatePlayer(Player player) {
		if (playerIsRegistered(player)) {
			stats.get(player).isActive = false;
		}
	}

	/**
	 * List all active players.
	 * 
	 * @return
	 */
	public List<Player> getActivePlayers() {
		List<Player> activePlayers = new ArrayList<Player>();
		for (Entry<Player, PlayerStats> entry : stats.entrySet()) {
			if (entry.getValue().isActive) {
				activePlayers.add(entry.getKey());
			}
		}
		return activePlayers;
	}

	/**
	 * A map with matches won as key, and as value, all the players who have won
	 * this many matches.
	 * 
	 * @return
	 */
	private Map<Integer, List<Player>> getActivePlayersMatchesWonMap() {

		Map<Integer, List<Player>> matchesWonMap = new TreeMap<Integer, List<Player>>();

		for (Entry<Player, PlayerStats> entry : stats.entrySet()) {

			Player player = entry.getKey();
			PlayerStats playerStats = entry.getValue();

			if (!playerStats.isActive) {
				continue;
			}

			int matchesWon = playerStats.matchesWon;

			if (!matchesWonMap.containsKey(matchesWon)) {
				List<Player> players = new ArrayList<Player>();
				players.add(player);
				matchesWonMap.put(matchesWon, players);
			} else {
				matchesWonMap.get(matchesWon).add(player);
			}
		}

		return matchesWonMap;
	}

	/**
	 * Get a list of active players which the player has met fewer than a
	 * maximum amount of times. If the player has met all active players more
	 * than max times, the list is empty.
	 * 
	 * @param player
	 * @param maxTimes
	 * @return
	 */
	public List<Player> getActivePlayersMetFewerTimesThanMax(Player player,
			int maxTimes) {
		List<Player> playersMetFewerTimes = new ArrayList<Player>();
		List<Player> activePlayers = getActivePlayers();

		PlayerStats playerStats = stats.get(player);
		for (Player opponent : activePlayers) {
			if (opponent == player) {
				// Opponent is the same as player
				continue;
			}
			if (playerStats.playerMetOpponent(opponent)) {
				// Player has met opponent
				List<MatchResult> matchesAgainstOpponent = playerStats.matchResultMap
						.get(opponent);
				int timesPlayedAgainstOpponent = matchesAgainstOpponent.size();

				if (timesPlayedAgainstOpponent < maxTimes) {
					playersMetFewerTimes.add(opponent);
				}

			} else {
				// Player has not met opponent
				playersMetFewerTimes.add(opponent);
			}
		}
		return playersMetFewerTimes;
	}

	/**
	 * List all players, active and inactive.
	 * 
	 * @return
	 */
	public List<Player> getAllPlayers() {
		List<Player> allPlayers = new ArrayList<Player>();
		for (Entry<Player, PlayerStats> entry : stats.entrySet()) {
			allPlayers.add(entry.getKey());
		}
		return allPlayers;
	}

	/**
	 * Get the player with the highest amount of matches won. If there are
	 * multiple players with the highest amount of matches won, they will all be
	 * returned.
	 * 
	 * @return
	 */
	public List<Player> getAllPlayersWithMostMatchesWon() {
		int mostMatchesWon = 0;
		List<Player> winners = new ArrayList<Player>();

		for (Entry<Player, PlayerStats> entry : stats.entrySet()) {

			Player player = entry.getKey();
			PlayerStats playerStats = entry.getValue();

			if (playerStats.matchesWon > mostMatchesWon) {
				mostMatchesWon = playerStats.matchesWon;
				winners.clear();
				winners.add(player);

			} else if (playerStats.matchesWon == mostMatchesWon) {
				winners.add(player);
			}
		}
		return winners;
	}

	/**
	 * Get a list of drop-ins, players who are active, but have played less
	 * games than the one(s) who have played the most games. This obviously
	 * gives wrong results in elimination tournaments...
	 * 
	 * @return
	 */
	private List<Player> getDropIns() {
		List<Player> dropIns = new ArrayList<Player>();
		int mostMatches = getMostMatchesPlayedByAnyPlayer();

		for (Entry<Player, PlayerStats> entry : stats.entrySet()) {
			Player player = entry.getKey();
			PlayerStats playerStats = entry.getValue();

			if (playerStats.matchesPlayed < mostMatches) {
				dropIns.add(player);
			}
		}
		return dropIns;
	}

	public List<Player> getInactivePlayers() {
		List<Player> inactivePlayers = new ArrayList<Player>();

		for (Entry<Player, PlayerStats> entry : stats.entrySet()) {
			Player player = entry.getKey();
			PlayerStats playerStats = entry.getValue();
			if (!playerStats.isActive) {
				inactivePlayers.add(player);
			}
		}
		return inactivePlayers;
	}

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
			if (playerStats.playerMetOpponent(opponent)) {
				// Players have met
				int timesMet = playerStats.matchResultMap.get(opponent).size();
				timesMetMap.put(new Integer(timesMet), opponent);
			} else {
				timesMetMap.put(new Integer(0), opponent);
			}
		}

		if (timesMetMap.isEmpty()) {
			// Player is the only one active.
			return leastMetPlayers;
		}

		/*
		 * Add the players met fewest times, e.g. if the players has met three
		 * opponents one time, five opponents two times, one opponent three
		 * times, add only the three met once.
		 */
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
				// Since the map is sorted by how many times players have met,
				// we do not need to continue if timesMet is larger than before.
				break;
			}
		}
		return leastMetPlayers;
	}

	private int getMostMatchesPlayedByAnyPlayer() {
		int mostMatches = 0;
		for (Entry<Player, PlayerStats> entry : stats.entrySet()) {
			PlayerStats playerStats = entry.getValue();
			if (playerStats.matchesPlayed > mostMatches) {
				mostMatches = playerStats.matchesPlayed;
			}
		}
		return mostMatches;
	}

	/**
	 * How many matches player has lost.
	 * 
	 * @param player
	 * @return
	 */
	public int getNumberOfLostMatches(Player player) {
		if (!playerIsRegistered(player)) {
			return 0;
		}
		return stats.get(player).matchesLost;
	}

	/**
	 * How many matches player has made.
	 * 
	 * @param player
	 * @return
	 */
	public int getNumberOfMatchesPlayed(Player player) {
		if (!playerIsRegistered(player)) {
			return 0;
		}
		return stats.get(player).matchesPlayed;
	}

	/**
	 * How many times the two players have played against each other.
	 * 
	 * @param player
	 * @param opponent
	 * @return
	 */
	public int getNumberOfMatchesPlayed(Player player, Player opponent) {
		if (playerIsRegistered(player) && playerIsRegistered(opponent)
				&& stats.get(player).playerMetOpponent(opponent)) {
			return stats.get(player).matchResultMap.get(opponent).size();
		}
		return 0;
	}

	/**
	 * How many matches player has won.
	 * 
	 * @param player
	 * @return
	 */
	public int getNumberOfWonMatches(Player player) {
		if (!playerIsRegistered(player)) {
			return 0;
		}
		return stats.get(player).matchesWon;
	}

	/**
	 * Returns a list of players sorted on most to least wins. If multiple
	 * players have the same amount of wins they are sorted according to:
	 * 
	 * <ol>
	 * <li>Most wins.</li>
	 * <li>Least losses.</li>
	 * <li>Least games played.</li>
	 * </ol>
	 * Otherwise the order is random.
	 * 
	 * @return
	 */
	public List<Player> getPlayersSortedByWinsLossesGamesPlayed() {

		Comparator<Player> scoreComparator = new Comparator<Player>() {
			@Override
			public int compare(Player p1, Player p2) {

				PlayerStats p1stats = stats.get(p1);
				PlayerStats p2stats = stats.get(p2);

				if (p1stats.matchesWon > p2stats.matchesWon) {
					return -1;
				} else if (p1stats.matchesWon < p2stats.matchesWon) {
					return 1;
				}
				// Same number of matches won, sort on losses.
				if (p1stats.matchesLost > p2stats.matchesLost) {
					return 1;
				} else if (p1stats.matchesLost < p2stats.matchesLost) {
					return -1;
				}
				// Same number of wins and losses, sort on games.
				if (p1stats.matchesPlayed > p2stats.matchesPlayed) {
					return 1;
				} else if (p1stats.matchesPlayed < p2stats.matchesPlayed) {
					return -1;
				}
				return 0;
			}
		};

		List<Player> players = getAllPlayers();
		Collections.sort(players, scoreComparator);
		return players;
	}

	/**
	 * Randomly chooses an active player to leave out (used when there is one
	 * player too many). Keeps track of which players have been left out of a
	 * game previously, and chooses one which has never been left out. If all
	 * active players have been left out once, it resets the tracking and
	 * chooses any active player randomly.
	 * 
	 * TODO: This does not work if a player is deactivated, all players get left
	 * out once, the list resets, then the player comes back again. The player
	 * will have been left out one time too few. It would be better to map how
	 * many times a player is left out. Not that it really matters...
	 * 
	 * @param players
	 *            List of players to choose from
	 * @return
	 */
	public Player getPlayerToLeaveOut(List<Player> players) {
		List<Player> playersNeverLeftOut = new ArrayList<Player>();

		// Grab all players never left out.
		for (Player player : players) {
			if (!leftOutPlayers.contains(player)) {
				playersNeverLeftOut.add(player);
			}
		}

		int numberOfPlayersNeverLeftOut = playersNeverLeftOut.size();

		if (numberOfPlayersNeverLeftOut == 0) {
			// All active players have been left out at least once. Grab a
			// random player.
			int numberOfActivePlayers = players.size();
			int index = MathUtils.random(numberOfActivePlayers - 1);
			Player playerToLeaveOut = players.get(index);
			leftOutPlayers.clear();
			leftOutPlayers.add(playerToLeaveOut);
			return playerToLeaveOut;

		} else {
			// Take a random player who has never been left out.
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
			if (playerStats.playerMetOpponent(opponent)) {
				// Players have met
				continue;
			}
			// Player is unmet
			unmetPlayers.add(opponent);
		}
		return unmetPlayers;
	}

	/**
	 * Increase the number of matches player has won with one. In the results it
	 * will look like the player played against itself.
	 * 
	 * @param player
	 */
	public void giveBye(Player player) {
		if (!playerIsRegistered(player)) {
			addPlayerToTournament(player);
		}
		PlayerStats playerStats = stats.get(player);
		playerStats.matchesWon++;
		playerStats.matchesPlayed++;
		playerStats.addMatchResult(player, 1, 0);
	}

	/**
	 * Check if an integer is odd.
	 * 
	 * @param num
	 * @return
	 */
	private boolean isOdd(int num) {
		return (num % 2 == 1);
	}

	/**
	 * For all active players, find the fewest times it has has met any other
	 * active player.
	 * 
	 * @return
	 */
	public int leastTimesActivePlayerMetActivePlayer() {

		int leastTimes = Integer.MAX_VALUE;
		for (Entry<Player, PlayerStats> playerEntry : stats.entrySet()) {

			Player player = playerEntry.getKey();
			PlayerStats playerStats = playerEntry.getValue();

			// Ignore inactive player
			if (!playerStats.isActive) {
				continue;
			}

			if (playerStats.matchResultMap.isEmpty()) {
				// This active player has not played any matches.
				return 0;
			}

			for (Entry<Player, List<MatchResult>> matchEntry : playerStats.matchResultMap
					.entrySet()) {

				Player opponent = matchEntry.getKey();
				List<MatchResult> results = matchEntry.getValue();

				if (!playerIsActive(opponent) || opponent == player) {
					continue;
				}

				int timesMetOpponent = results.size();
				if (timesMetOpponent < leastTimes) {
					leastTimes = timesMetOpponent;
				}
			}

		}
		return (leastTimes == Integer.MAX_VALUE) ? 0 : leastTimes;
	}

	public List<Player[]> matchListDoubleElimination() {
		// TODO
		return null;
	}

	/**
	 * <p>
	 * In this match pairing, all active players first meets a random unmet
	 * player. If all players have met each other at least once, the arrangement
	 * will be the same as if none of the players have met.
	 * </p>
	 * <p>
	 * If there is an odd number of players a random player will be left out of
	 * the match. The same player will not be left out twice until all players
	 * have been left out once. There is no "bye", the player will get to play
	 * all others eventually.
	 * </p>
	 * <p>
	 * Limit how many times a player will meet another with
	 * {@code maxTimesTwoPlayersMeet}. If set to zero, this tournament will go
	 * on forever.
	 * </p>
	 * <p>
	 * This arrangement supports drop-in/drop-out. If a player joins an ongoing
	 * tournament with a max amount of times to play each opponent, each player
	 * will get to meet the new player, so long as they are still active.
	 * </p>
	 * <p>
	 * When all players have met each other the max number of times, this method
	 * will return an empty list. Another way to check if if a tournament of
	 * this type is over is to use:
	 * </p>
	 * <p>
	 * {@code leastTimesActivePlayerMetActivePlayer() == maxNumber && allActivePlayersHaveMetEachOther()}
	 * </p>
	 * 
	 * @param maxTimesTwoPlayersMeet
	 *            The max amount of times two players can play against each
	 *            other. 0 = infinite times.
	 * @return A list of player match pairings.
	 */
	public List<Player[]> pairingsAllVsAll(int maxTimesTwoPlayersMeet) {
		List<Player[]> pairings = new ArrayList<Player[]>();

		// TODO: Make this more readable...

		// This will be used to reactivate players who get deactivated by the
		// algorthm.
		List<Player> activePlayers = getActivePlayers();

		// Working list, players will get removed from this as they are paired
		// off.
		List<Player> worklist = getActivePlayers();

		int numberOfPlayersToArrange = worklist.size();

		if (numberOfPlayersToArrange <= 1) {
			return pairings;
		}

		if (isOdd(numberOfPlayersToArrange)) {
			Player playerToLeaveOut = getPlayerToLeaveOut(worklist);
			worklist.remove(playerToLeaveOut);
			deactivatePlayer(playerToLeaveOut);
			numberOfPlayersToArrange--;
		}

		// There are now always an even number of players.
		while (true) {
			if (numberOfPlayersToArrange == 0) {
				break;
			}
			Player[] pair = new Player[2];

			// Grab a random player and add it as left player
			int leftPlayerIndex = MathUtils
					.random(numberOfPlayersToArrange - 1);
			Player leftPlayer = worklist.get(leftPlayerIndex);
			pair[0] = leftPlayer;
			worklist.remove(leftPlayerIndex);
			deactivatePlayer(leftPlayer);
			numberOfPlayersToArrange--;

			List<Player> possibleOpponents;
			if (maxTimesTwoPlayersMeet <= 0) {
				possibleOpponents = getLeastMetActivePlayers(leftPlayer);
			} else {
				possibleOpponents = getActivePlayersMetFewerTimesThanMax(
						leftPlayer, maxTimesTwoPlayersMeet);
				if (possibleOpponents.size() == 0) {
					// Player has met all other players max amount of times.
					// Ignore player in the pairings.
					continue;
				}
			}

			int numberOfPossibleOpponents = possibleOpponents.size();
			int rightPlayerIndex = MathUtils
					.random(numberOfPossibleOpponents - 1);
			Player rightPlayer = possibleOpponents.get(rightPlayerIndex);
			pair[1] = rightPlayer;
			worklist.remove(rightPlayer);
			deactivatePlayer(rightPlayer);
			numberOfPlayersToArrange--;

			pairings.add(pair);

			if (numberOfPlayersToArrange == 0) {
				break;
			} else if (numberOfPlayersToArrange == 1) {
				// This shouldn't happen.
				Gdx.app.debug(className, "One player is left over!");
				break;
			}
		}

		activatePlayers(activePlayers);

		return pairings;
	}

	/**
	 * Creates pairings for Swiss-type tournament. Winners meet winners, losers
	 * meet losers.
	 * 
	 * In this type of tournament, if there is an odd number of players, give
	 * one of them an automatic win, "bye", and leave the player out this
	 * round.The rest of the players can either win or lose a round (no ties).
	 * They will therefore be divided into even numbers of winners and losers.
	 * 
	 * If a player drops out of the game (is deactivated) it will be removed
	 * from the tournament. Players are not allowed to drop-in during this
	 * tournament (they will be ignored during pairing).
	 * 
	 * @return
	 */
	public List<Player[]> pairingSwiss() {
		List<Player[]> pairings = new ArrayList<Player[]>();

		List<Player> dropIns = getDropIns();
		for (Player dropIn : dropIns) {
			removePlayerFromTournament(dropIn);
		}
		List<Player> dropOuts = getInactivePlayers();
		for (Player dropOut : dropOuts) {
			removePlayerFromTournament(dropOut);
		}

		List<Player> activePlayers = getActivePlayers();
		int numberOfPlayersToArrange = activePlayers.size();

		if (numberOfPlayersToArrange <= 1) {
			// Only one player, no pairing possible.
			return pairings;
		}
		if (numberOfPlayersToArrange == 2) {
			Player leftPlayer = activePlayers.get(0);
			Player rightPlayer = activePlayers.get(1);

			int leftPlayerMatchesWon = getNumberOfWonMatches(leftPlayer);
			int rightPlayerMatchesWon = getNumberOfWonMatches(rightPlayer);

			if (leftPlayerMatchesWon == rightPlayerMatchesWon) {
				Player[] pair = { leftPlayer, rightPlayer };
				pairings.add(pair);
			}
			/*
			 * If the (only) two players have won an equal amount of matches
			 * they get to play each other. Otherwise, one will be the winner
			 * and the tournament is over.
			 */
			return pairings;
		}

		/*
		 * This map will contain >= 3 players. They have played an equal amount
		 * of matches. Because players may have dropped out there is no
		 * guarantee there is a player who has won an equal amount of matches as
		 * another. If onw
		 */
		Map<Integer, List<Player>> matchesWonMap = getActivePlayersMatchesWonMap();

		for (Entry<Integer, List<Player>> entry : matchesWonMap.entrySet()) {

			List<Player> playersWithCommonNumMatchesWon = entry.getValue();

			/*
			 * In this type of tournament, if there is an odd number of players,
			 * give one of them an automatic win, "bye", and leave the player
			 * out this round. The rest of the players can either win or lose a
			 * round (no ties). They will therefore be divided into even numbers
			 * of winners and losers. The player who got the bye will count
			 * among the winners. There will therefore be odd amount of winners.
			 */
			if (isOdd(playersWithCommonNumMatchesWon.size())) {
				Player playerToGiveBye = getPlayerToLeaveOut(playersWithCommonNumMatchesWon);
				playersWithCommonNumMatchesWon.remove(playerToGiveBye);
				giveBye(playerToGiveBye);
			}

		}
		return null;
	}

	/**
	 * Check if a player is currently marked as active in the tournament.
	 * 
	 * @param player
	 * @return
	 */
	public boolean playerIsActive(Player player) {
		if (playerIsRegistered(player)) {
			return stats.get(player).isActive;
		}
		return false;
	}

	/**
	 * Check if the player is registered in the tournament.
	 * 
	 * @param player
	 * @return
	 */
	public boolean playerIsRegistered(Player player) {
		return stats.containsKey(player);
	}

	public void removePlayerFromTournament(Player player) {
		if (playerIsRegistered(player)) {
			stats.remove(player);
		}
	}

}
