package io.github.maydevbe;

import com.google.common.collect.ImmutableSet;
import io.github.maydevbe.reflect.ScoreboardReflection;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class PlayerScoreboard {

    private final Player player;
    private final Scoreboard scoreboard;
    private final Objective objective;

    private final Set<String> createdTeams = new HashSet<>();
    private final Set<String> scores = new HashSet<>();

    private final Map<String, Integer> displayedScores = new HashMap<>();
    private final Map<String, String> prefixes = new HashMap<>();
    private final Map<String, String> suffixes = new HashMap<>();

    public PlayerScoreboard(Player player) {
        this.player = player;

        this.scoreboard = ScoreboardReflection.getOrNewScoreboard(player);
        this.objective = ScoreboardReflection.getObjective(scoreboard);

        player.setScoreboard(scoreboard);
    }

    public void updateTitle(String title) {
        title = ChatColor.translateAlternateColorCodes('&', title);

        if (title.length() > 32) {
            title = title.substring(0, 32);
        }

        if (!objective.getDisplayName().equals(title)) {
            objective.setDisplayName(title);
        }
    }

    public void updateLines(List<String> lines) {
        lines = lines.stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());

        if (lines.size() > 15) {
            lines.subList(0, 15);
        }

        updateDisplayedScores(lines);
    }

    private void updateDisplayedScores(List<String> lines) {
        scores.clear();
        Collections.reverse(lines);

        for (int index = 0; index < lines.size(); index++) {
            String currentLine = lines.get(index);
            int currentSlot = index + 1; // so so

            String[] splitString = ScoreboardReflection.splitString(currentSlot, currentLine);
            updateTeamsAndScores(currentSlot, splitString[1], splitString[0], splitString[2]);
        }

        removeObsoleteScores();
    }

    private void updateTeamsAndScores(int index, String score, String prefix, String suffix) {
        scores.add(score);

        if (!createdTeams.contains(score)) {
            ScoreboardReflection.sendPacket(player, ScoreboardReflection.newScoreboardTeam(score, "_", "_", new ArrayList<>(), 0));
            ScoreboardReflection.sendPacket(player, ScoreboardReflection.newScoreboardTeam(score, Collections.singletonList(score), 3));
            createdTeams.add(score);
        }

        if (!displayedScores.containsKey(score) || displayedScores.get(score) != index) {
            ScoreboardReflection.sendPacket(player, ScoreboardReflection.newScoreboardScore(score, objective.getName(), index, ScoreboardReflection.EnumScoreboardAction.CHANGE));
            displayedScores.put(score, index);
        }

        if (!prefixes.containsKey(score) || !prefixes.get(score).equals(prefix) || !suffixes.get(score).equals(suffix)) {
            ScoreboardReflection.sendPacket(player, ScoreboardReflection.newScoreboardTeam(score, prefix, suffix));
            prefixes.put(score, prefix);
            suffixes.put(score, suffix);
        }
    }

    private void removeObsoleteScores() {
        for (String displayedScore : ImmutableSet.copyOf(displayedScores.keySet())) {
            if (!scores.contains(displayedScore)) {
                displayedScores.remove(displayedScore);
                prefixes.remove(displayedScore);
                suffixes.remove(displayedScore);
                ScoreboardReflection.sendPacket(player, ScoreboardReflection.newScoreboardScore(displayedScore));
            }
        }
    }
}
