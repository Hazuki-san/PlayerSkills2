package me.hsgamer.playerskills2.player;

import me.hsgamer.playerskills2.config.MainConfig;
import me.hsgamer.playerskills2.util.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SPlayer {
    private static final HashMap<UUID, SPlayer> players = new HashMap<>();

    private final UUID player;
    private final Map<String, Integer> skills;
    private int points;

    public SPlayer(UUID player) {
        this.player = player;
        this.skills = new HashMap<>();
    }

    public static void load(UUID uuid) {
        SPlayer sPlayer = MainConfig.OPTIONS_PLAYER_STORAGE.getValue().load(uuid);
        players.put(uuid, sPlayer);
    }

    public static SPlayer get(UUID uuid) {
        return players.get(uuid);
    }

    public static void unload(UUID uuid) {
        players.remove(uuid);
    }

    public static void save(SPlayer sPlayer) {
        if (sPlayer == null) {
            if (MainConfig.isVerboseLogging()) {
                Utils.logInfo("Plugin tried to save an SPlayer for a null player.");
            }
            return;
        }
        MainConfig.OPTIONS_PLAYER_STORAGE.getValue().save(sPlayer);
    }

    public static HashMap<UUID, SPlayer> getPlayers() {
        return players;
    }

    public UUID getPlayer() {
        return player;
    }

    public Map<String, Integer> getSkills() {
        return skills;
    }

    public int getLevel(String skill) {
        return skills.getOrDefault(skill, 0);
    }

    public void setLevel(String skill, int level) {
        skills.put(skill, level);
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getNextPointPrice() {
        int base = MainConfig.POINTS_PRICE.getValue();
        if (MainConfig.POINTS_DYNAMIC_PRICE_ENABLED.getValue()) {
            int playerPoints = getPoints();
            for (int i : getSkills().values()) {
                playerPoints += i;
            }
            return base + (playerPoints * MainConfig.POINTS_DYNAMIC_PRICE_PRICE_INCREASE_PER_POINT.getValue());
        } else {
            return base;
        }
    }
}
