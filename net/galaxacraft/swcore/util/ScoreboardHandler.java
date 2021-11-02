package net.galaxacraft.swcore.util;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import net.galaxacraft.swcore.util.ConfigUtil;
import net.galaxacraft.swcore.util.EconomyUtil;
import net.galaxacraft.swcore.util.GameType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guus on 7-4-2016.
 */
public class ScoreboardHandler {

    private static ScoreboardHandler instance;

    public void setScoreboard(Player player) {
        ScoreboardManager man = Bukkit.getScoreboardManager();

        Scoreboard board = man.getNewScoreboard();

        Objective obj = board.registerNewObjective(StringUtils.abbreviate(player.getName(), 16), "dummy");

        String displayName = ChatColor.translateAlternateColorCodes('&', ConfigUtil.get().getScoreboard().getString("scoreboard-title"));
        obj.setDisplayName(displayName);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        int scoreToSet = ConfigUtil.get().getScoreboard().getStringList("scoreboard-lines").size();
        String emptyLine = ChatColor.RESET.toString();

        for (String s : ConfigUtil.get().getScoreboard().getStringList("scoreboard-lines")) {
            if (s.contains("%emptyline%")) {
                s = emptyLine;
                emptyLine = emptyLine + ChatColor.RESET.toString();
            } else {
                int solokills = EconomyUtil.get().getKills(player.getUniqueId());
                int solowins = EconomyUtil.get().getWins(player.getUniqueId());
              //  int teamkills = 0; //replace teamkills
             //   int teamwins = 0; //replace teamwins
             //   int megakills = 0; //you know the drill
             //   int megawins = 0; // here aswell xD
                int coins = EconomyUtil.get().getCoins(player.getUniqueId(), GameType.SKYWARS);
                int souls = EconomyUtil.get().getSouls(player.getUniqueId());
                int maxSouls = 100;

                s = ChatColor.translateAlternateColorCodes('&', s);
                s = s.replaceAll("%solokills%", solokills + "").replaceAll("%solowins%", solowins + "");
                s = s.replaceAll("%coins%", coins + "").replaceAll("%souls%", souls + "").replaceAll("%maxSouls%", maxSouls + "");
            }

            Score score = obj.getScore(s);
            score.setScore(scoreToSet);
            scoreToSet = scoreToSet -  1;
        }

        player.setScoreboard(board);
    }

    public void removeScoreboard(Player player) {
        if (player.getScoreboard().getObjective(StringUtils.abbreviate(player.getName(), 16)) != null) {
            player.getScoreboard().getObjective(StringUtils.abbreviate(player.getName(), 16)).unregister();
        }
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    public void updateScoreboard(Player player) {
        removeScoreboard(player);

        setScoreboard(player);
    }

    public static ScoreboardHandler get() {
        if (instance == null) {
            instance = new ScoreboardHandler();
        }

        return instance;
    }
}