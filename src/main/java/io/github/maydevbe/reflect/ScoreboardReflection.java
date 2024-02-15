package io.github.maydevbe.reflect;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ScoreboardReflection {

    private static final Class<?> SCOREBOARD_TEAM_PACKET_CLASS = Reflection.getMinecraftClass("PacketPlayOutScoreboardTeam");
    private static final Class<?> SCOREBOARD_SCORE_PACKET_CLASS = Reflection.getMinecraftClass("PacketPlayOutScoreboardScore");
    private static final Class<?> ENUM_SCOREBAORD_ACTION_CLASS = Reflection.getMinecraftClass("PacketPlayOutScoreboardScore$EnumScoreboardAction");
    private static final Class<?> MINECRAFT_SERVER_CLASS = Reflection.getMinecraftClass("MinecraftServer");
    private static final Class<?> CRAFT_SERVER_CLASS = Reflection.getCraftBukkitClass("CraftServer");
    private static final Class<?> CRAFT_SCOREBOARD_CLASS = Reflection.getCraftBukkitClass("scoreboard.CraftScoreboard");
    private static final Class<?> NMS_SCOREBOARD_CLASS = Reflection.getMinecraftClass("Scoreboard");
    private static final Class<?> NMS_SCOREBOARD_SERVER_CLASS = Reflection.getMinecraftClass("ScoreboardServer");

    private static final Class<?> CRAFT_PLAYER_CLASS = Reflection.getCraftBukkitClass("entity.CraftPlayer");

    private static final Class<?> NMS_PACKET_CLASS = Reflection.getMinecraftClass("Packet");

    private static final Class<?> NMS_ENTITY_PLAYER_CLASS = Reflection.getMinecraftClass("EntityPlayer");
    private static final Class<?> NMS_PLAYER_CONNECTION_CLASS = Reflection.getMinecraftClass("PlayerConnection");

    private static final Reflection.ConstructorInvoker CRAFT_SCOREBOARD_CONSTRUCTOR = Reflection.getConstructor(CRAFT_SCOREBOARD_CLASS, NMS_SCOREBOARD_CLASS);
    private static final Reflection.ConstructorInvoker NMS_SCOREBOARD_SERVER_CONSTRUCTOR = Reflection.getConstructor(NMS_SCOREBOARD_SERVER_CLASS, MINECRAFT_SERVER_CLASS);

    private static final Reflection.ConstructorInvoker SCOREBOARD_TEAM_PACKET_CONSTRUCTOR = Reflection.getConstructor(SCOREBOARD_TEAM_PACKET_CLASS);
    private static final Reflection.ConstructorInvoker SCOREBOARD_SCORE_NO_ARGS_PACKET_CONSTRUCTOR = Reflection.getConstructor(SCOREBOARD_SCORE_PACKET_CLASS);
    private static final Reflection.ConstructorInvoker SCOREBOARD_SCORE_PACKET_CONSTRUCTOR = Reflection.getConstructor(SCOREBOARD_SCORE_PACKET_CLASS, String.class);

    private static final Reflection.FieldAccessor<?> TEAM_NAME_FIELD = Reflection.getField(SCOREBOARD_TEAM_PACKET_CLASS, "a", String.class);
    private static final Reflection.FieldAccessor<?> TEAM_DISPLAY_NAME_FIELD = Reflection.getField(SCOREBOARD_TEAM_PACKET_CLASS, "b", String.class);
    private static final Reflection.FieldAccessor<?> TEAM_PREFIX_FIELD = Reflection.getField(SCOREBOARD_TEAM_PACKET_CLASS, "c", String.class);
    private static final Reflection.FieldAccessor<?> TEAM_SUFFIX_FIELD = Reflection.getField(SCOREBOARD_TEAM_PACKET_CLASS, "d", String.class);
    private static final Reflection.FieldAccessor<?> TEAM_PLAYER_LIST_FIELD = Reflection.getField(SCOREBOARD_TEAM_PACKET_CLASS, "g", Collection.class);
    private static final Reflection.FieldAccessor<?> TEAM_DATA_FIELD = Reflection.getField(SCOREBOARD_TEAM_PACKET_CLASS, "h", int.class);
    private static final Reflection.FieldAccessor<?> TEAM_OPTION_FIELD = Reflection.getField(SCOREBOARD_TEAM_PACKET_CLASS, "i", int.class);

    private static final Reflection.FieldAccessor<?> SCORE_NAME_FIELD = Reflection.getField(SCOREBOARD_SCORE_PACKET_CLASS, "a", String.class);
    private static final Reflection.FieldAccessor<?> SCORE_OBJECTIVE_FIELD = Reflection.getField(SCOREBOARD_SCORE_PACKET_CLASS, "b", String.class);
    private static final Reflection.FieldAccessor<?> SCORE_VALUE_FIELD = Reflection.getField(SCOREBOARD_SCORE_PACKET_CLASS, "c", int.class);
    private static final Reflection.FieldAccessor<?> SCORE_ACTION_FILED = Reflection.getField(SCOREBOARD_SCORE_PACKET_CLASS, "d", ENUM_SCOREBAORD_ACTION_CLASS);
    private static final Reflection.FieldAccessor<?> PLAYER_CONNECTION_FIELD = Reflection.getField(NMS_ENTITY_PLAYER_CLASS, NMS_PLAYER_CONNECTION_CLASS, 0);
    private static final Reflection.MethodInvoker GET_CRAFTPLAYER_HANDLE_METHOD = Reflection.getMethod(CRAFT_PLAYER_CLASS, "getHandle");
    private static final Reflection.MethodInvoker SEND_PACKET_METHOD = Reflection.getMethod(NMS_PLAYER_CONNECTION_CLASS, "sendPacket", NMS_PACKET_CLASS);
    private static final Reflection.MethodInvoker GET_CRAFT_SERVER_METHOD = Reflection.getMethod(CRAFT_SERVER_CLASS, "getServer");

    public static Object newScoreboardTeam(String name, String prefix, String suffix, Collection<String> players, int paramInt) {
        Object packet = SCOREBOARD_TEAM_PACKET_CONSTRUCTOR.invoke();

        TEAM_NAME_FIELD.set(packet, name);
        TEAM_DATA_FIELD.set(packet, paramInt);

        if (paramInt == 0 || paramInt == 2) {
            TEAM_DISPLAY_NAME_FIELD.set(packet, name);
            TEAM_PREFIX_FIELD.set(packet, prefix);
            TEAM_SUFFIX_FIELD.set(packet, suffix);
            TEAM_OPTION_FIELD.set(packet, 1);
        }

        if (paramInt == 0) {
            TEAM_PLAYER_LIST_FIELD.set(packet, players);
        }

        return packet;
    }

    public static Object newScoreboardTeam(String name, String prefix, String suffix) {
        Object packet = SCOREBOARD_TEAM_PACKET_CONSTRUCTOR.invoke();

        TEAM_NAME_FIELD.set(packet, name);
        TEAM_PREFIX_FIELD.set(packet, prefix);
        TEAM_SUFFIX_FIELD.set(packet, suffix);
        TEAM_PLAYER_LIST_FIELD.set(packet, new ArrayList<>());
        TEAM_DATA_FIELD.set(packet, 2);
        return packet;
    }

    public static Object newScoreboardTeam(String name, Collection<String> players, int paramInt) {
        Object packet = SCOREBOARD_TEAM_PACKET_CONSTRUCTOR.invoke();

        if (players == null) {
            players = new ArrayList<>();
        }

        TEAM_NAME_FIELD.set(packet, name);
        TEAM_DATA_FIELD.set(packet, paramInt);

        TEAM_PLAYER_LIST_FIELD.set(packet, players);
        return packet;
    }

    public static Object newScoreboardScore(String name, String objective, int value, EnumScoreboardAction action) {
        Object packet = SCOREBOARD_SCORE_NO_ARGS_PACKET_CONSTRUCTOR.invoke();

        SCORE_NAME_FIELD.set(packet, name);
        SCORE_OBJECTIVE_FIELD.set(packet, objective);
        SCORE_VALUE_FIELD.set(packet, value);

        SCORE_ACTION_FILED.set(packet, Reflection.getEnum(ENUM_SCOREBAORD_ACTION_CLASS, action.name()));
        return packet;
    }

    public static Object newScoreboardScore(String name) {
        return SCOREBOARD_SCORE_PACKET_CONSTRUCTOR.invoke(name);
    }

    private static Object getEntityPlayer(Player player) {
        return GET_CRAFTPLAYER_HANDLE_METHOD.invoke(player);
    }

    public static void sendPacket(Player player, Object packet) {
        Object playerConnection = PLAYER_CONNECTION_FIELD.get(getEntityPlayer(player));
        SEND_PACKET_METHOD.invoke(playerConnection, packet);
    }

    public static Scoreboard getOrNewScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        if (scoreboard == Bukkit.getScoreboardManager().getMainScoreboard()) {
            Object minecraftServer = GET_CRAFT_SERVER_METHOD.invoke(Bukkit.getServer());

            return (Scoreboard) CRAFT_SCOREBOARD_CONSTRUCTOR.invoke(NMS_SCOREBOARD_SERVER_CONSTRUCTOR.invoke(minecraftServer));
        }

        return Bukkit.getScoreboardManager().getMainScoreboard();
    }

    public static Objective getObjective(final Scoreboard scoreboard) {
        Objective objective = scoreboard.getObjective("Default");

        if (objective == null) {
            objective = scoreboard.registerNewObjective("Default", "dummy");
        }

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        return objective;
    }

    public static String[] splitString(int index, String input) {
        String prefix = "";
        String score = getCodeForIndex(index);
        String suffix = "";

        if (input.length() > 16) {
            prefix = input.substring(0, 16);

            if (hasColorCodeOverflow(prefix)) {
                prefix = removeColorCodeOverflow(prefix);
                score = getCodeForIndex(index) + getLastColors(prefix) + input.substring(prefix.length());
            } else {
                score = getCodeForIndex(index) + getLastColors(prefix) + input.substring(16);
            }

            if (score.length() > 16) {
                score = score.substring(0, 16);
                suffix = handleScoreOverflow(input, prefix, score, index);

                if (suffix.length() > 16) {
                    suffix = suffix.substring(0, 16);
                }
            }
        } else {
            prefix = input;
        }

        return new String[] { prefix, score, suffix };
    }

    private static boolean hasColorCodeOverflow(String prefix) {
        return prefix.charAt(15) == ChatColor.COLOR_CHAR || prefix.charAt(14) == ChatColor.COLOR_CHAR;
    }

    private static String removeColorCodeOverflow(String prefix) {
        return prefix.substring(0, prefix.length() - 1);
    }

    private static String handleScoreOverflow(String input, String prefix, String score, int index) {
        int start = prefix.length() + (score.length() - getCodeForIndex(index).length() - getLastColors(prefix).length());
        return input.substring(start);
    }


    private static String getLastColors(String input) {
        String lastColors = ChatColor.getLastColors(input);
        return lastColors.isEmpty() ? ChatColor.COLOR_CHAR + "r" : lastColors;
    }

    private static String getCodeForIndex(int index) {
        int tensDigit = index / 10;
        int unitsDigit = index % 10;

        return ChatColor.COLOR_CHAR + String.valueOf(tensDigit) + ChatColor.COLOR_CHAR + unitsDigit;
    }

    public enum EnumScoreboardAction {
        CHANGE;
    }
}
