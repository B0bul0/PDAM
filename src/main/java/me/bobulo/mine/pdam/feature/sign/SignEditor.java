package me.bobulo.mine.pdam.feature.sign;

import me.bobulo.mine.pdam.PDAM;
import me.bobulo.mine.pdam.config.ConfigProperty;
import me.bobulo.mine.pdam.feature.sign.window.SignViewerWindow;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Feature that provides enhanced sign editing capabilities.
 */
public final class SignEditor {

    public static final String FEATURE_ID = "sign_editor";

    /**
     * If true, overrides the default sign GUI with a custom one.
     */
    public static final ConfigProperty<Boolean> OVERRIDE_SIGN_GUI = ConfigProperty.of(FEATURE_ID + ".override_gui", true);

    /**
     * If true, enables the sign viewer GUI when right-clicking a sign while sneaking.
     */
    public static final ConfigProperty<Boolean> VIEW_GUI = ConfigProperty.of(FEATURE_ID + ".view_gui", true);

    public static boolean isFeatureEnabled() {
        return PDAM.getFeatureService().isFeatureEnabled(FEATURE_ID);
    }

}
