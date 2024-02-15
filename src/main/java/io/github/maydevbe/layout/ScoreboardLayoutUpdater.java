package io.github.maydevbe.layout;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.github.maydevbe.PlayerScoreboard;
import io.github.maydevbe.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.*;

public class ScoreboardLayoutUpdater implements Runnable {

    private final ThreadFactory threadFactory = new ThreadFactoryBuilder()
            .setNameFormat("Score Thread - %s")
            .build();

    private final ScoreboardManager scoreboardManager;
    private ScheduledExecutorService executor;
    private ScheduledFuture<?> updater;

    public ScoreboardLayoutUpdater(ScoreboardManager scoreboardManager) {
        this.scoreboardManager = scoreboardManager;
        Bukkit.getScheduler().runTaskLater(scoreboardManager.getPluginInstanced(), this::initialize, 10L);
    }

    private void initialize() {
        this.executor = Executors.newScheduledThreadPool(1, threadFactory);
        this.updater = this.executor.scheduleAtFixedRate(this, 0L, 200L, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerScoreboard scoreboard = scoreboardManager.getPlayerScoreboard(player);
            if (scoreboard == null) continue;

            ScoreboardLayoutManager layoutManager = scoreboardManager.getAbstractScoreboardLayout().createLayout(player);

            scoreboard.updateTitle(layoutManager.getTitle());
            scoreboard.updateLines(layoutManager.getLines());
        }
    }

    public void shutdown() {
        if (this.updater != null) {
            this.updater.cancel(true);
        }
        if (this.executor != null) {
            this.executor.shutdownNow();
        }
    }
}
