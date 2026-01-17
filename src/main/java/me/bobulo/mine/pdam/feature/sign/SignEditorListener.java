package me.bobulo.mine.pdam.feature.sign;

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

public class SignEditorListener {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK || !event.world.isRemote) {
            return;
        }

        World world = event.world;
        BlockPos pos = event.pos;
        Block block = world.getBlockState(pos).getBlock();

        if (block != Blocks.standing_sign && block != Blocks.wall_sign) {
            return;
        }

        TileEntity tileEntity = world.getTileEntity(pos);
        if (!(tileEntity instanceof TileEntitySign)) {
            return;
        }

        TileEntitySign signTileEntity = (TileEntitySign) tileEntity;

        if (!event.entityPlayer.isSneaking()) { // require sneaking to open sign viewer
            return;
        }

        // ignore empty signs
        boolean isEmpty = true;
        for (IChatComponent line : signTileEntity.signText) {
            if (!line.getUnformattedText().trim().isEmpty()) {
                isEmpty = false;
                break;
            }
        }

        if (isEmpty) {
            return;
        }

        event.setCanceled(true);
        SignViewerWindow.openSignEditor(signTileEntity);
    }

}
