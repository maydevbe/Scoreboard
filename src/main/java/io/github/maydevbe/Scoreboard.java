package io.github.maydevbe;

import io.github.maydevbe.layout.impl.TestScoreboardLayout;
import org.bukkit.plugin.java.JavaPlugin;

public class Scoreboard extends JavaPlugin {

    @Override
    public void onEnable() {
        new ScoreboardManager(this, new TestScoreboardLayout());
    }
}
