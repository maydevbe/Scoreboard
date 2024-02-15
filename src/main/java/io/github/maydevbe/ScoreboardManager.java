package io.github.maydevbe;

import io.github.maydevbe.layout.AbstractScoreboardLayout;
import io.github.maydevbe.layout.ScoreboardLayoutUpdater;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class ScoreboardManager implements Listener {

    private final Map<UUID, PlayerScoreboard> scoreboards = new HashMap<>();

    private final JavaPlugin pluginInstanced;
    private final ScoreboardLayoutUpdater scoreboardLayoutUpdater;
    private final AbstractScoreboardLayout abstractScoreboardLayout;

    public ScoreboardManager(JavaPlugin pluginInstanced, AbstractScoreboardLayout abstractScoreboardLayout) {
        this.pluginInstanced = pluginInstanced;
        this.scoreboardLayoutUpdater = new ScoreboardLayoutUpdater(this);
        this.abstractScoreboardLayout = abstractScoreboardLayout;

        pluginInstanced.getServer().getPluginManager().registerEvents(this, pluginInstanced);
    }

    private void loadScoreboard(Player player) {
        PlayerScoreboard scoreboard = new PlayerScoreboard(player);
        scoreboards.put(player.getUniqueId(), scoreboard);
    }

    public PlayerScoreboard getPlayerScoreboard(Player player) {
        return scoreboards.get(player.getUniqueId());
    }

    private void removeScoreboard(Player player) {
        scoreboards.remove(player.getUniqueId());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        loadScoreboard(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        removeScoreboard(player);
    }
}
