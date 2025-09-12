package me.bobulo.mine.devmod.feature.entity;

import me.bobulo.mine.devmod.DevMod;
import me.bobulo.mine.devmod.ui.InfoBox;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Rotations;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static me.bobulo.mine.devmod.util.LocaleUtils.translateToLocal;
import static net.minecraft.util.EnumChatFormatting.AQUA;

@SideOnly(Side.CLIENT)
public class EntityOverlayInfoListener {

    private final InfoBox infoBox = new InfoBox(5, 5, 5);
    private boolean isInfoBoxActive = false;

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        Minecraft mc = Minecraft.getMinecraft();
        MovingObjectPosition objectMouseOver = mc.objectMouseOver;

        if (objectMouseOver != null && objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
            List<String> lines = getEntityInfo(objectMouseOver.entityHit);
            infoBox.setLines(lines);

            if (!isInfoBoxActive) {
                DevMod.getUIManager().addElement(infoBox);
                isInfoBoxActive = true;
            }
        } else {
            if (isInfoBoxActive) {
                infoBox.setLines(Collections.emptyList()); // Limpa as linhas para evitar renderização de frame antigo
                DevMod.getUIManager().removeElement(infoBox);
                isInfoBoxActive = false;
            }
        }
    }

    private List<String> getEntityInfo(Entity entity) {
        if (entity instanceof EntityArmorStand) {
            return getArmorStandInfo((EntityArmorStand) entity);
        }

        return getGenericEntityInfo(entity);
    }

    private List<String> getGenericEntityInfo(Entity entity) {
        List<String> lines = new ArrayList<>();
        lines.add(AQUA + translateToLocal("devmod.entity.info_title"));
        lines.add(translateToLocal("devmod.entity.id") + ": " + entity.getEntityId());
        lines.add(translateToLocal("devmod.entity.name") + ": " + entity.getName());
        lines.add(translateToLocal("devmod.entity.uuid") + ": " + entity.getUniqueID());
        lines.add(translateToLocal("devmod.entity.position") + ": " + String.format("%.2f, %.2f, %.2f", entity.posX, entity.posY, entity.posZ));
        return lines;
    }

    private List<String> getArmorStandInfo(EntityArmorStand armorStand) {
        List<String> lines = getGenericEntityInfo(armorStand);
        lines.add(""); // Linha em branco para separação
        lines.add(translateToLocal("devmod.armorstand.invisible") + ": " + armorStand.isInvisible());
        lines.add(translateToLocal("devmod.armorstand.small") + ": " + armorStand.isSmall());
        lines.add(translateToLocal("devmod.armorstand.no_base_plate") + ": " + armorStand.hasNoBasePlate());
        lines.add(translateToLocal("devmod.armorstand.marker") + ": " + armorStand.hasMarker());
        lines.add(translateToLocal("devmod.armorstand.show_arms") + ": " + armorStand.getShowArms());
        lines.add(AQUA + translateToLocal("devmod.armorstand.rotations_title"));
        lines.add(translateToLocal("devmod.armorstand.head") + ": " + formatRotation(armorStand.getHeadRotation()));
        lines.add(translateToLocal("devmod.armorstand.body") + ": " + formatRotation(armorStand.getBodyRotation()));
        lines.add(translateToLocal("devmod.armorstand.left_arm") + ": " + formatRotation(armorStand.getLeftArmRotation()));
        lines.add(translateToLocal("devmod.armorstand.right_arm") + ": " + formatRotation(armorStand.getRightArmRotation()));
        lines.add(translateToLocal("devmod.armorstand.left_leg") + ": " + formatRotation(armorStand.getLeftLegRotation()));
        lines.add(translateToLocal("devmod.armorstand.right_leg") + ": " + formatRotation(armorStand.getRightLegRotation()));
        return lines;
    }

    private String formatRotation(Rotations r) {
        return String.format("(%.2f, %.2f, %.2f)", r.getX(), r.getY(), r.getZ());
    }
}