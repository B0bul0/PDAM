package me.bobulo.mine.pdam.feature.hologram;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class Hologram {

    private final Minecraft mc = Minecraft.getMinecraft();

    // Hologram position
    private double x;
    private double y;
    private double z;

    private final List<EntityArmorStand> entityLines = new ArrayList<>();

    public Hologram(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void addLine(@NotNull String line) {
        EntityArmorStand armorStand = createArmorStand(line);
        entityLines.add(armorStand);
        refresh();
    }

    public void addLine(int index, @NotNull String line) {
        if (index < 0 || index > entityLines.size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + entityLines.size());
        }

        EntityArmorStand armorStand = createArmorStand(line);
        entityLines.add(index, armorStand);
        refresh();
    }

    public void removeLine(int index) {
        if (index < 0 || index >= entityLines.size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + entityLines.size());
        }

        EntityArmorStand armorStand = entityLines.remove(index);
        mc.theWorld.removeEntity(armorStand);
        refresh();
    }

    public void editLine(int index, @NotNull String line) {
        if (index < 0 || index >= entityLines.size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + entityLines.size());
        }

        EntityArmorStand armorStand = entityLines.get(index);
        armorStand.setCustomNameTag(line);
        armorStand.setAlwaysRenderNameTag(!StringUtils.isEmpty(line));
    }

    public void setEntityLines(@NotNull String... entityLines) {
        setLines(Arrays.asList(entityLines));
    }

    public void setLines(List<String> lines) {
        if (lines == null) {
            clear();
            return;
        }

        if (entityLines.stream().map(Entity::getCustomNameTag).collect(Collectors.toList()).equals(lines)) {
            return;
        }

        if (lines.size() < entityLines.size()) {
            int toRemove = entityLines.size() - lines.size();
            for (int i = 0; i < toRemove; i++) {
                removeLine(entityLines.size() - 1);
            }
        }

        for (int i = 0; i < lines.size(); i++) {
            String newLine = lines.get(i);
            if (i < entityLines.size()) {
                editLine(i, newLine);
            } else {
                addLine(newLine);
            }
        }
    }

    public void setPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        refresh();
    }

    public void clear() {
        for (EntityArmorStand armorStand : entityLines) {
            mc.theWorld.removeEntity(armorStand);
        }
        entityLines.clear();
    }

    public void refresh() {
        int startY = entityLines.size();
        for (EntityArmorStand armorStand : entityLines) {
            armorStand.setPosition(x, y + (0.25 * startY), z);
            startY--;
        }
    }

    public int getLineCount() {
        return entityLines.size();
    }

    private EntityArmorStand createArmorStand(String line) {
        EntityArmorStand armorStand = new EntityArmorStand(mc.theWorld, x, y, z);
        armorStand.setCustomNameTag(line);
        armorStand.setAlwaysRenderNameTag(!StringUtils.isEmpty(line));
        armorStand.setInvisible(true);
        mc.theWorld.spawnEntityInWorld(armorStand);
        return armorStand;
    }

}