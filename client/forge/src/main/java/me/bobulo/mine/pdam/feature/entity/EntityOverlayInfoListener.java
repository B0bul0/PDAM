package me.bobulo.mine.pdam.feature.entity;

import me.bobulo.mine.pdam.PDAM;
import me.bobulo.mine.pdam.ui.InfoBox;
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

import static me.bobulo.mine.pdam.util.LocaleUtils.translateToLocal;
import static net.minecraft.util.EnumChatFormatting.AQUA;

@SideOnly(Side.CLIENT)
public final class EntityOverlayInfoListener {

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
                PDAM.getUIManager().addElement(infoBox);
                isInfoBoxActive = true;
            }
        } else {
            if (isInfoBoxActive) {
                infoBox.setLines(Collections.emptyList()); // Limpa as linhas para evitar renderização de frame antigo
                PDAM.getUIManager().removeElement(infoBox);
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
        lines.add(AQUA + translateToLocal("pdam.entity.info_title"));
        lines.add(translateToLocal("pdam.entity.id") + ": " + entity.getEntityId());
        lines.add(translateToLocal("pdam.entity.name") + ": " + entity.getName());
        lines.add(translateToLocal("pdam.entity.uuid") + ": " + entity.getUniqueID());
        lines.add(translateToLocal("pdam.entity.position") + ": " + String.format("%.2f, %.2f, %.2f", entity.posX, entity.posY, entity.posZ));
        return lines;
    }

    private List<String> getArmorStandInfo(EntityArmorStand armorStand) {
        List<String> lines = getGenericEntityInfo(armorStand);
        lines.add(""); // Linha em branco para separação
        lines.add(translateToLocal("pdam.armorstand.invisible") + ": " + armorStand.isInvisible());
        lines.add(translateToLocal("pdam.armorstand.small") + ": " + armorStand.isSmall());
        lines.add(translateToLocal("pdam.armorstand.no_base_plate") + ": " + armorStand.hasNoBasePlate());
        lines.add(translateToLocal("pdam.armorstand.marker") + ": " + armorStand.hasMarker());
        lines.add(translateToLocal("pdam.armorstand.show_arms") + ": " + armorStand.getShowArms());
        lines.add(AQUA + translateToLocal("pdam.armorstand.rotations_title"));
        lines.add(translateToLocal("pdam.armorstand.head") + ": " + formatRotation(armorStand.getHeadRotation()));
        lines.add(translateToLocal("pdam.armorstand.body") + ": " + formatRotation(armorStand.getBodyRotation()));
        lines.add(translateToLocal("pdam.armorstand.left_arm") + ": " + formatRotation(armorStand.getLeftArmRotation()));
        lines.add(translateToLocal("pdam.armorstand.right_arm") + ": " + formatRotation(armorStand.getRightArmRotation()));
        lines.add(translateToLocal("pdam.armorstand.left_leg") + ": " + formatRotation(armorStand.getLeftLegRotation()));
        lines.add(translateToLocal("pdam.armorstand.right_leg") + ": " + formatRotation(armorStand.getRightLegRotation()));
        return lines;
    }

    private String formatRotation(Rotations r) {
        return String.format("(%.2f, %.2f, %.2f)", r.getX(), r.getY(), r.getZ());
    }
}