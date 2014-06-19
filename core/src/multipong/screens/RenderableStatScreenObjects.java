package multipong.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import multipong.board.boardobjects.Player;
import multipong.font.Fonts;
import multipong.match.TournamentStats;
import multipong.rendering.RenderableString;

public class RenderableStatScreenObjects {

	public static RenderableString countDownMsg(Rectangle board, float number) {
		RenderableString r = new RenderableString();
		r.font = Fonts.fontSmall;
		r.color = Color.WHITE;
		r.text = "Players must sign up in " + Integer.toString((int) number)
				+ " seconds.";
		float x = board.x;
		float y = board.y;
		float width = board.getWidth();
		float height = board.getHeight();
		float midPointX = x + width / 2;
		float xOffset = midPointX - r.getWidth() / 2;
		float yOffset = y + r.getHeight() + height / 50;
		r.pos = new Vector2(xOffset, yOffset);
		return r;
	}

	public static void updateCountDownMsg(RenderableString renderableString,
			float newNumber) {
		RenderableString r = renderableString;
		float previousStringLength = r.getWidth();
		int newNumberInt = (int) newNumber;
		r.text = "Players must sign up in " + Integer.toString(newNumberInt)
				+ " seconds.";
		float sizeDiff = previousStringLength - r.getWidth();
		r.pos.x += sizeDiff / 2;
	}

	public static void setCountDownMsgToIdle(RenderableString renderableString) {
		RenderableString r = renderableString;
		float previousStringLength = r.getWidth();
		r.text = "Waiting for players...";
		float sizeDiff = previousStringLength - r.getWidth();
		r.pos.x += sizeDiff / 2;
	}

	public static void setCountDownMsgToFinished(
			RenderableString renderableString) {
		RenderableString r = renderableString;
		float previousStringLength = r.getWidth();
		r.text = "Tournament is finished, all active players have met each other.";
		float sizeDiff = previousStringLength - r.getWidth();
		r.pos.x += sizeDiff / 2;
	}

	public static List<RenderableString> statsTable(Rectangle board,
			TournamentStats stats) {
		List<RenderableString> strings = new ArrayList<RenderableString>();

		float x = board.x;
		float y = board.y;
		float width = board.getWidth();
		float height = board.getHeight();
		float midPointX = x + width / 2;

		RenderableString mainHeader = new RenderableString();
		mainHeader.color = Color.WHITE;
		mainHeader.font = Fonts.fontSmall;
		mainHeader.text = "Statistics";
		float mainheaderXpos = midPointX - mainHeader.getWidth() / 2;
		float mainheaderYpos = y + height - mainHeader.getHeight();
		mainHeader.pos = new Vector2(mainheaderXpos, mainheaderYpos);

		float yPadding = height / 50;
		float namesXpos = x + 1f / 6f * width;
		float winsXpos = x + 2f / 6f * width;
		float lossesXpos = x + 3f / 6f * width;
		float matchesPlayedXpos = x + 4f / 6f * width;
		float headerRowYoffset = mainheaderYpos - mainHeader.getHeight()
				- yPadding;

		RenderableString namesHeader = new RenderableString();
		namesHeader.color = Color.WHITE;
		namesHeader.font = Fonts.fontSmall;
		namesHeader.text = "Name";
		namesHeader.pos = new Vector2(namesXpos, headerRowYoffset);

		RenderableString winsHeader = new RenderableString();
		winsHeader.color = Color.WHITE;
		winsHeader.font = Fonts.fontSmall;
		winsHeader.text = "Wins";
		winsHeader.pos = new Vector2(winsXpos, headerRowYoffset);

		RenderableString lossesHeader = new RenderableString();
		lossesHeader.color = Color.WHITE;
		lossesHeader.font = Fonts.fontSmall;
		lossesHeader.text = "Losses";
		lossesHeader.pos = new Vector2(lossesXpos, headerRowYoffset);

		RenderableString matchesPlayedHeader = new RenderableString();
		matchesPlayedHeader.color = Color.WHITE;
		matchesPlayedHeader.font = Fonts.fontSmall;
		matchesPlayedHeader.text = "Matches played";
		matchesPlayedHeader.pos = new Vector2(matchesPlayedXpos,
				headerRowYoffset);

		List<Player> players = stats.getPlayersSortedByWinsLossesGamesPlayed();
		int numberOfPlayers = players.size();
		if (numberOfPlayers == 0) {
			return strings;
		}

		strings.add(mainHeader);
		strings.add(namesHeader);
		strings.add(winsHeader);
		strings.add(lossesHeader);
		strings.add(matchesPlayedHeader);

		float currentYoffset = headerRowYoffset - namesHeader.getHeight()
				- yPadding;

		for (Player player : players) {
			Color rowColor;
			if (stats.playerIsActive(player)) {
				rowColor = Color.WHITE;
			} else {
				rowColor = Color.GRAY;
			}

			RenderableString name = new RenderableString();
			name.color = rowColor;
			name.font = Fonts.fontSmall;
			name.text = player.name;
			name.pos = new Vector2(namesXpos, currentYoffset);

			RenderableString wins = new RenderableString();
			wins.font = Fonts.fontSmall;
			wins.color = Color.WHITE;
			wins.text = Integer.toString(stats.getPlayerWonMatches(player));
			wins.pos = new Vector2(winsXpos, currentYoffset);

			RenderableString losses = new RenderableString();
			losses.font = Fonts.fontSmall;
			losses.color = Color.WHITE;
			losses.text = Integer.toString(stats.getPlayerLostMatches(player));
			losses.pos = new Vector2(lossesXpos, currentYoffset);

			RenderableString matchesPlayed = new RenderableString();
			matchesPlayed.font = Fonts.fontSmall;
			matchesPlayed.color = Color.WHITE;
			matchesPlayed.text = Integer.toString(stats
					.getPlayerMatchesPlayed(player));
			matchesPlayed.pos = new Vector2(matchesPlayedXpos, currentYoffset);

			currentYoffset -= (name.getHeight() + yPadding);

			strings.add(name);
			strings.add(wins);
			strings.add(losses);
			strings.add(matchesPlayed);
		}
		return strings;

	}
}
