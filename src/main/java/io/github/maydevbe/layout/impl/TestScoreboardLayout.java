package io.github.maydevbe.layout.impl;

import io.github.maydevbe.layout.AbstractScoreboardLayout;
import io.github.maydevbe.layout.ScoreboardLayoutManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;

public class TestScoreboardLayout extends AbstractScoreboardLayout {

    @Override
    public ScoreboardLayoutManager createLayout(Player player) {
        ScoreboardLayoutManager layoutManager = new ScoreboardLayoutManager();
        layoutManager.setTitle("&b&lScoreboard API");

        layoutManager.addLine(" ");
        layoutManager.addLine("&bPlayers&7: &f" + Bukkit.getOnlinePlayers().size());
        layoutManager.addLine("&bRandom Number&7: &f" + ThreadLocalRandom.current().nextInt(1000));
        layoutManager.addLine(" ");

        return layoutManager;
    }
}
