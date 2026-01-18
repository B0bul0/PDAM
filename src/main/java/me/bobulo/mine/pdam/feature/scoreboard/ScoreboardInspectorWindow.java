package me.bobulo.mine.pdam.feature.scoreboard;

import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiTableFlags;
import imgui.type.ImString;
import me.bobulo.mine.pdam.imgui.window.AbstractRenderItemWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.ImGuiDrawUtil.keepInScreen;

public final class ScoreboardInspectorWindow extends AbstractRenderItemWindow {

    public ScoreboardInspectorWindow() {
        super("Scoreboard Inspector");
    }

    @Override
    public void renderGui() {
        setNextWindowSize(200, 260, ImGuiCond.FirstUseEver);
        setNextWindowPos(50, 50, ImGuiCond.FirstUseEver);

        if (begin("Scoreboard Inspector##ScoreboardWindow", isVisible)) {
            keepInScreen();
            renderContent();
        }

        end();
    }

    private void renderContent() {
        if (beginTabBar("ScoreboardTabs")) {

            if (beginTabItem("Pretty Sidebar")) {
                currentPrettySidebar();
                endTabItem();
            }

            if (beginTabItem("Current Players Scores")) {
                text("Display Slots:");
                separator();

                if (beginTabBar("ScoreTabs")) {
                    drawTabItemScore("List", 0);
                    drawTabItemScore("Sidebar", 1);
                    drawTabItemScore("Bellow Name", 2);
                    endTabBar();
                }

                endTabItem();
            }

            if (beginTabItem("Teams")) {
                currentTeams();
                endTabItem();
            }

            endTabBar();
        }
    }

    private void drawTabItemScore(String label, int displaySlot) {
        boolean open = beginTabItem("Current Score " + label);

        if (isItemHovered()) {
            setTooltip("Display Slot " + displaySlot + ": " + label);
        }

        if (open) {
            currentScoreObjective(displaySlot);
            endTabItem();
        }
    }

    private void currentPrettySidebar() {
        Scoreboard scoreboard = getScoreboard();
        if (scoreboard == null) {
            text("No scoreboard found.");
            return;
        }

        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1); // sidebar slot
        if (objective == null) {
            text("No sidebar objective found.");
            return;
        }

        text("Sidebar Title:");
        text(objective.getDisplayName());
        separator();

        text("Sidebar Content:");

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

        inputTextMultiline("##SidebarText", new ImString(sb.toString()), -1, -1, ImGuiInputTextFlags.ReadOnly);
    }

    private void currentScoreObjective(int displaySlot) {
        Scoreboard scoreboard = getScoreboard();
        if (scoreboard == null) {
            text("No scoreboard found.");
            return;
        }

        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(displaySlot);
        if (objective == null) {
            text("No objective found in this display slot.");
            return;
        }

        text("Title:");
        text(objective.getDisplayName());

        separator();

        text("Content:");
        List<Score> scores = new ArrayList<>(scoreboard.getSortedScores(objective));
        Collections.reverse(scores);
        if (beginTable("ScoreTable", 2, ImGuiTableFlags.RowBg | ImGuiTableFlags.Borders)) {
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

    private void currentTeams() {
        Scoreboard scoreboard = getScoreboard();
        if (scoreboard == null) {
            text("No scoreboard found.");
            return;
        }

        Collection<ScorePlayerTeam> teams = scoreboard.getTeams();
        if (teams.isEmpty()) {
            text("No teams found.");
            return;
        }

        for (ScorePlayerTeam team : teams) {
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

    private Scoreboard getScoreboard() {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player == null) {
            return null;
        }

        return player.getWorldScoreboard();
    }

}
