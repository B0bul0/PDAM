package me.bobulo.mine.pdam.feature.skin;

import com.mojang.authlib.GameProfile;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HeadWorldSkinInfoListener {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK || !event.world.isRemote) {
            return;
        }

        World world = event.world;
        BlockPos pos = event.pos;

        if (world.getBlockState(pos).getBlock() != Blocks.skull) {
            return;
        }

        TileEntity tileEntity = world.getTileEntity(pos);
        if (!(tileEntity instanceof TileEntitySkull)) {
            return;
        }

        TileEntitySkull skullTileEntity = (TileEntitySkull) tileEntity;
        GameProfile gameProfile = skullTileEntity.getPlayerProfile();

        if (gameProfile == null || !gameProfile.getProperties().containsKey("textures")) {
            return;
        }

        event.setCanceled(true);
        SkinInfoUtils.sendSkinInfoMessage(gameProfile);
    }

}