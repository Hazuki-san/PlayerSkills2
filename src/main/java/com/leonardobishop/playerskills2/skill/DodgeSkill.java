package com.leonardobishop.playerskills2.skill;

import com.leonardobishop.playerskills2.PlayerSkills;
import com.leonardobishop.playerskills2.config.Config;
import com.leonardobishop.playerskills2.config.CreatorConfigValue;
import com.leonardobishop.playerskills2.player.SPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DodgeSkill extends Skill {

    public DodgeSkill(PlayerSkills plugin) {
        super(plugin, "Dodge", "dodge");

        super.getCreatorConfigValues().add(new CreatorConfigValue("max-level", 6, true));
        super.getCreatorConfigValues().add(new CreatorConfigValue("gui-slot", 13, true));
        super.getCreatorConfigValues().add(new CreatorConfigValue("percent-increase", 2, true));
        super.getCreatorConfigValues().add(new CreatorConfigValue("only-in-worlds", Arrays.asList("world", "world_nether", "world_the_end")));
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        if (this.getConfig().containsKey("only-in-worlds")) {
            List<String> listOfWorlds = (List<String>) this.getConfig().get("only-in-worlds");
            if (!listOfWorlds.contains(player.getLocation().getWorld().getName())) {
                return;
            }
        }

        SPlayer sPlayer = SPlayer.get(player.getUniqueId());

        if (sPlayer == null) {
            if (super.getPlugin().isVerboseLogging()) {
                super.getPlugin().logError("Failed event. SPlayer for " + player.getUniqueId() + " is null.");
            }
            return;
        }

        int dodgeLevel = sPlayer.getLevel(this.getConfigName());

        double chance = dodgeLevel * super.getDecimalNumber("percent-increase");

        if (ThreadLocalRandom.current().nextInt(100) < chance) {
            if (!Config.get(super.getPlugin(), "messages.dodge").getColoredString().equals("")) {
                player.sendMessage(Config.get(super.getPlugin(), "messages.dodge").getColoredString());
            }
            event.setCancelled(true);
        }
    }

    @Override
    public String getPreviousString(SPlayer player) {
        int dodgeLevel = player.getLevel(this.getConfigName());
        double damage = dodgeLevel * super.getDecimalNumber("percent-increase");
        return getPlugin().getPercentageFormat().format(damage) + "%";
    }

    @Override
    public String getNextString(SPlayer player) {
        int dodgeLevel = player.getLevel(this.getConfigName()) + 1;
        double damage = dodgeLevel * super.getDecimalNumber("percent-increase");
        return getPlugin().getPercentageFormat().format(damage) + "%";
    }
}