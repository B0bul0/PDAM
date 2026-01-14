package me.bobulo.mine.pdam.feature.scoreboard;

import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiTableFlags;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImString;
import me.bobulo.mine.pdam.imgui.window.AbstractRenderItemWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.scoreboard.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static imgui.ImGui.*;

public final class ScoreboardWindow extends AbstractRenderItemWindow {

    private final ImString sidebarTitle = new ImString();
    private final ImString sidebarText = new ImString();

    public ScoreboardWindow() {
        super("Scoreboard Debugger");
    }

    @Override
    public void renderGui() {
        setNextWindowSize(200, 260, ImGuiCond.FirstUseEver);
        setNextWindowPos(50, 50, ImGuiCond.FirstUseEver);

        if (begin("Scoreboard Debugger###ScoreboardWindow", isVisible)) {
            renderContent();
        }

        end();
    }

    private void renderContent() {
        if (beginTabBar("ScoreboardTabs")) {
            if (beginTabItem("Current Score Sidebar")) {
                currentSidebar();
                endTabItem();
            }

            if (beginTabItem("Current Score Bellow Name")) {
                currentBellowName();
                endTabItem();
            }

            if (beginTabItem("Current Score List")) {
                currentList();
                endTabItem();
            }

            if (beginTabItem("Current Score")) {
                currentScore();
                endTabItem();
            }

            if (beginTabItem("Teams")) {
                currentTeams();
                endTabItem();
            }

            endTabBar();
        }
    }

    private void currentSidebar() {
        refreshSidebar();

        text("Sidebar Title:");
        text(sidebarTitle.get());
        separator();
        text("Sidebar Content:");
        inputTextMultiline("##SidebarText", sidebarText, -1, -1, ImGuiInputTextFlags.ReadOnly);
    }

    private void currentBellowName() {
        Scoreboard scoreboard = getScoreboard();
        if (scoreboard == null) {
            return;
        }

        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(2); // bellow name slot
        if (objective == null) {
            return;
        }

        text("Bellow Name Title:");
        text(objective.getDisplayName());
        separator();
        text("Bellow Name Content:");
        List<Score> scores = new ArrayList<>(scoreboard.getSortedScores(objective));
        Collections.reverse(scores);
        if (beginTable("BellowNameTable", 2, ImGuiTableFlags.RowBg | ImGuiTableFlags.Borders)) {
            tableSetupColumn("Player");
            tableSetupColumn("Score");
            tableHeadersRow();
            for (Score score : scores) {
                tableNextRow();
                tableNextColumn();
                ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
                if (team != null) {
                    String prefix = team.getColorPrefix();
                    String suffix = team.getColorSuffix();
                    text(prefix + score.getPlayerName()+ suffix);
                } else {
                    text(score.getPlayerName());
                }
                tableNextColumn();
                text(Integer.toString(score.getScorePoints()));
            }
            endTable();
        }
    }

    private void currentList() {
        Scoreboard scoreboard = getScoreboard();
        if (scoreboard == null) {
            return;
        }

        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(0); // list slot
        if (objective == null) {
            return;
        }

        text("List Title:");
        text(objective.getDisplayName());
        separator();
        text("List Content:");
        List<Score> scores = new ArrayList<>(scoreboard.getSortedScores(objective));
        Collections.reverse(scores);
        if (beginTable("ListTable", 2, ImGuiTableFlags.RowBg | ImGuiTableFlags.Borders)) {
            tableSetupColumn("Player");
            tableSetupColumn("Score");
            tableHeadersRow();
            for (Score score : scores) {
                tableNextRow();
                tableNextColumn();
                ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
                if (team != null) {
                    String prefix = team.getColorPrefix();
                    String suffix = team.getColorSuffix();
                    text(prefix + score.getPlayerName() + suffix);
                } else {
                    text(score.getPlayerName());
                }
                tableNextColumn();
                text(Integer.toString(score.getScorePoints()));
            }
            endTable();
        }

    }

    private void currentScore() {
        Scoreboard scoreboard = getScoreboard();
        if (scoreboard == null) {
            return;
        }

        for (String playerName : scoreboard.getObjectiveNames()) {
            text("Player:");
            sameLine();
            text(playerName);

            for (ScoreObjective objective : scoreboard.getScoreObjectives()) {
                Score score = scoreboard.getValueFromObjective(playerName, objective);
                text(objective.getName() + ":");
                sameLine();
                text(Integer.toString(score.getScorePoints()));
            }

            separator();
        }
    }

    private void currentTeams() {
        Scoreboard scoreboard = getScoreboard();
        if (scoreboard == null) {
            return;
        }

        for (ScorePlayerTeam team : scoreboard.getTeams()) {
            text("Team:");
            sameLine();
            text(team.getTeamName());

            text("Prefix:");
            sameLine();
            text(team.getColorPrefix());

            text("Suffix:");
            sameLine();
            text(team.getColorSuffix());

            text("Name Tag Visibility:");
            sameLine();
            text(team.getNameTagVisibility().name());

            text("Allow Friendly Fire:");
            sameLine();
            text(Boolean.toString(team.getAllowFriendlyFire()));

            text("Can See Friendly Invisibles:");
            sameLine();
            text(Boolean.toString(team.getSeeFriendlyInvisiblesEnabled()));

            text("Members:");
            for (String member : team.getMembershipCollection()) {
                bulletText(member);
            }

            separator();
        }
    }

    private void refreshSidebar() {
        Scoreboard scoreboard = getScoreboard();
        if (scoreboard == null) {
            return;
        }

        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1); // sidebar slot
        if (objective == null) {
            return;
        }

        sidebarTitle.set(objective.getDisplayName());
        StringBuilder sb = new StringBuilder();

        List<Score> scores = new ArrayList<>(scoreboard.getSortedScores(objective));
        Collections.reverse(scores);
        for (Score score : scores) {
            ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
            if (team != null) {
                String prefix = team.getColorPrefix();
                String suffix = team.getColorSuffix();
                sb.append(prefix).append(score.getPlayerName()).append(suffix).append("\n");
            } else {
                sb.append(score.getPlayerName()).append("\n");
            }
        }

        sidebarText.set(sb.toString().trim());
    }

    private Scoreboard getScoreboard() {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player == null) {
            return null;
        }

        return player.getWorldScoreboard();
    }

}
