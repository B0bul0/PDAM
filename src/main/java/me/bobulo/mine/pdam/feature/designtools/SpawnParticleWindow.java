package me.bobulo.mine.pdam.feature.designtools;

import imgui.ImGui;
import imgui.ImGuiTextFilter;
import imgui.extension.imguizmo.flag.Mode;
import imgui.flag.ImGuiColorEditFlags;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiKey;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import me.bobulo.mine.pdam.feature.particle.Particle;
import me.bobulo.mine.pdam.feature.particle.ParticleAnimation;
import me.bobulo.mine.pdam.feature.particle.mapper.ParticleMapper;
import me.bobulo.mine.pdam.imgui.guizmo.GuizmoImGui;
import me.bobulo.mine.pdam.imgui.util.ImGuiNotificationDrawer;
import me.bobulo.mine.pdam.imgui.window.AbstractRenderItemWindow;
import me.bobulo.mine.pdam.util.Position;
import me.bobulo.mine.pdam.util.UniqueHistory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.ImGuiDrawUtil.keepInScreen;
import static me.bobulo.mine.pdam.imgui.util.ImGuiHelper.tooltip;

public final class SpawnParticleWindow extends AbstractRenderItemWindow {

    private static final Logger log = LogManager.getLogger(SpawnParticleWindow.class);
    private static final EnumParticleTypes[] ALL_PARTICLES = EnumParticleTypes.values();
    private static final String NONE_PARTICLE = "none";

    private ParticleMapper particleMapper = ParticleMapper.VANILLA;
    private final ImGuiNotificationDrawer notification = new ImGuiNotificationDrawer();

    private final ParticleAnimation animation = new ParticleAnimation(new Particle(), 20);

    private String particleToSpawn = NONE_PARTICLE;
    private final float[] position = new float[3];
    private final float[] speed = new float[]{0f};
    private final ImInt count = new ImInt(1);
    private final float[] offset = new float[3];
    private final ImInt intervalTicks = new ImInt(20);

    private final ImInt blockId = new ImInt(0);
    private final ImInt blockData = new ImInt(0);

    private final ImGuiTextFilter particleFilter = new ImGuiTextFilter();
    private final ImBoolean manipuleControls = new ImBoolean();

    private final UniqueHistory<Particle> spawnParticleEntries = new UniqueHistory<>(25);

    public SpawnParticleWindow() {
        super("Spawn Particles");
    }

    @Override
    public void renderGui() {
        ImGui.setNextWindowSize(525, 347, ImGuiCond.FirstUseEver);
        ImGui.setNextWindowPos(50, 60, ImGuiCond.FirstUseEver);

        if (begin("Spawn Particles##SpawnParticleWindow", isVisible)) {
            keepInScreen();
            renderContent();
        }

        end();

        if (isVisible.get() && Minecraft.getMinecraft().theWorld != null) {
            try {
                if (manipuleControls.get()) {
                    GuizmoImGui.manipulateTranslation(Mode.WORLD, position);
                    syncParticleAnimationPosition();
                }
            } catch (Exception exception) {
                log.error("An error occurred while updating Spawn Particle Window", exception);
            }
        }
    }

    private void renderContent() {
        notification.draw();

        separatorText("Particle Name Mapping");

        if (radioButton("Vanilla", particleMapper == ParticleMapper.VANILLA)) {
            setParticleMapper(ParticleMapper.VANILLA);
        }
        sameLine();
        if (radioButton("Client ENUM", particleMapper == ParticleMapper.CLIENT_ENUM)) {
            setParticleMapper(ParticleMapper.CLIENT_ENUM);
        }
        sameLine();
        if (radioButton("Particle ID", particleMapper == ParticleMapper.PARTICLE_ID)) {
            setParticleMapper(ParticleMapper.PARTICLE_ID);
        }
        sameLine();
        if (radioButton("Bukkit 1.8 Effect", particleMapper == ParticleMapper.BUKKIT_1_8)) {
            setParticleMapper(ParticleMapper.BUKKIT_1_8);
        }

        separator();
        renderLocationContent();
        renderSpawnParticleContent();
        renderActionsContent();
    }

    private void renderLocationContent() {
        Minecraft mc = Minecraft.getMinecraft();

        separatorText("Particle Location");
        setNextItemWidth(190);
        if (inputFloat3("##Location", position, "%.2f")) {
            syncParticleAnimation();
        }

        sameLine();

        if (button("Set to Player Position")) {
            if (mc.thePlayer == null) {
                notification.error("You are not in a world!");
            } else {
                position[0] = (float) mc.thePlayer.posX;
                position[1] = (float) mc.thePlayer.posY;
                position[2] = (float) mc.thePlayer.posZ;
                syncParticleAnimation();
            }
        }

        sameLine();

        if (checkbox("Show Movement Handles", manipuleControls.get())) {
            manipuleControls.set(!manipuleControls.get());
        }

        spacing();
        separator();
    }

    private void renderSpawnParticleContent() {
        Particle particle = animation.getParticle();

        separatorText("Particle to Spawn");

        text("Particle Type");
        sameLine();
        if (beginCombo("##SelectParticleEffect", particleToSpawn)) {

            if (isWindowAppearing()) {
                setKeyboardFocusHere();
                particleFilter.clear();
            }

            setNextItemShortcut(ImGuiKey.ModCtrl | ImGuiKey.F);

            if (particleFilter.draw("Filter")) {
                particleToSpawn = NONE_PARTICLE;
            }

            for (EnumParticleTypes particleTypes : ALL_PARTICLES) {
                String particleType = particleMapper.mapParticleId(particleTypes.getParticleID());
                if (particleType == null) {
                    continue;
                }

                if (particleFilter.passFilter(particleType) && selectable(particleType, particleType.equals(particleToSpawn))) {
                    particleToSpawn = particleType;
                    syncParticleAnimation();
                }
            }

            endCombo();
        }

        text("Count");
        sameLine();
        setNextItemWidth(100);
        if (inputInt("##Count", count)) {
            count.set(Math.max(0, count.get()));
            syncParticleAnimation();
        }

        sameLine();

        text("Speed");
        sameLine();
        setNextItemWidth(310);
        if (dragFloat("##Speed", speed, 0.01f, 0.0f, 10.0f, "%.2f")) {
            speed[0] = Math.max(0f, speed[0]);
            syncParticleAnimation();
        }

        text("Offset");
        sameLine();
        if (inputFloat3("##Offset", offset)) {
            syncParticleAnimation();
        }

        sameLine();
        if (colorEdit3("Color", offset,
          ImGuiColorEditFlags.Float | ImGuiColorEditFlags.NoInputs | ImGuiColorEditFlags.NoLabel)) {
            syncParticleAnimation();
        }
        tooltip("Uses the RGB values as offset for red, green and blue color channels.\n" +
          "Only works with certain particle types.");

        if (checkbox("Long Distance", particle.isLongDistance())) {
            particle.setLongDistance(!particle.isLongDistance());
            syncParticleAnimation();
        }

        spacing();
        if (treeNode("Block / Material Data")) {
            spacing();
            text("Block ID");
            sameLine();
            setNextItemWidth(80);
            if (inputInt("##BlockID", blockId)) {
                blockId.set(Math.max(0, blockId.get()));
                syncParticleAnimation();
            }

            sameLine();

            text("Block Data");
            sameLine();
            setNextItemWidth(80);
            if (inputInt("##BlockData", blockData)) {
                blockData.set(Math.max(0, blockData.get()));
                syncParticleAnimation();
            }

            treePop();
        }

        tooltip("Block ID and Data are only used for block-related particle types.");

    }

    private void renderActionsContent() {
        Particle particle = animation.getParticle();

        spacing();
        separatorText("Actions");

        if (button("Spawn Particle")) {
            if ("none".equals(particleToSpawn)) {
                notification.error("No particle selected to spawn!");
            } else {
                syncParticleAnimation();
                animation.spawnParticles();
                spawnParticleEntries.push(particle.clone());
            }
        }

        sameLine();

        if (button(animation.isRunning() ? "Stop Animation" : "Start Animation")) {
            syncParticleAnimation();
            if (animation.isRunning()) {
                animation.stop();
            } else {
                animation.start();
            }
        }

        sameLine();

        setNextItemWidth(80);
        if (inputInt("Interval Ticks", intervalTicks)) {
            int newInterval = Math.max(1, intervalTicks.get());
            animation.setTicksInterval(newInterval);
            intervalTicks.set(animation.getTicksInterval());
        }
    }

    private void syncParticleAnimation() {
        Particle particle = animation.getParticle();

        int particleID = particleMapper.reverseMapParticleId(particleToSpawn);
        if (particleID == -1) {
            return;
        }

        particle.setParticleId(particleID);
        particle.setSpeed(speed[0]);
        particle.setCount(count.get());
        particle.setOffsetX(offset[0]);
        particle.setOffsetY(offset[1]);
        particle.setOffsetZ(offset[2]);
        particle.setBlockId(blockId.get());
        particle.setBlockData(blockData.get());

        syncParticleAnimationPosition();
    }

    private void syncParticleAnimationPosition() {
        Particle particle = animation.getParticle();
        particle.setPosition(new Position(position[0], position[1], position[2], 0f, 0f));
    }

    private void setParticleMapper(ParticleMapper mapper) {
        int currentParticleId = animation.getParticle().getParticleId();
        this.particleMapper = mapper;
        String mappedName = particleMapper.mapParticleId(currentParticleId);
        this.particleToSpawn = mappedName != null ? mappedName : NONE_PARTICLE;
    }

}