package me.bobulo.mine.pdam.feature.packet.data;

import com.google.common.collect.ImmutableList;
import me.bobulo.mine.pdam.feature.packet.data.client.*;
import me.bobulo.mine.pdam.feature.packet.data.server.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PacketNameRegistry {

    private static final Map<Class<? extends PacketData>, String> packetNameMap = new HashMap<>();

    static {
        registerDefaults();
    }

    public static void registerDefaults() {
        // Client
        registerPacketName(AnimationClientPacketData.class, "Animation");
        registerPacketName(ChatMessageClientPacketData.class, "ChatMessage");
        registerPacketName(ClickWindowClientPacketData.class, "ClickWindow");
        registerPacketName(ClientSettingsClientPacketData.class, "ClientSettings");
        registerPacketName(ClientStatusClientPacketData.class, "ClientStatus");
        registerPacketName(CloseWindowClientPacketData.class, "CloseWindow");
        registerPacketName(ConfirmTransactionClientPacketData.class, "ConfirmTransaction");
        registerPacketName(CreativeInventoryActionClientPacketData.class, "CreativeInventoryAction");
        registerPacketName(EnchantItemClientPacketData.class, "EnchantItem");
        registerPacketName(EntityActionClientPacketData.class, "EntityAction");
        registerPacketName(HeldItemChangeClientPacketData.class, "HeldItemChange");
        registerPacketName(KeepAliveClientPacketData.class, "KeepAlive");
        registerPacketName(PlayerAbilitiesClientPacketData.class, "PlayerAbilities");
        registerPacketName(PlayerBlockPlacementClientPacketData.class, "PlayerBlockPlacement");
        registerPacketName(PlayerClientPacketData.class, "Player");
        registerPacketName(PlayerDiggingClientPacketData.class, "PlayerDigging");
        registerPacketName(PlayerLookClientPacketData.class, "PlayerLook");
        registerPacketName(PlayerPositionClientPacketData.class, "PlayerPosition");
        registerPacketName(PlayerPositionLookClientPacketData.class, "PlayerPositionLook");
        registerPacketName(PluginMessageClientPacketData.class, "PluginMessage");
        registerPacketName(ResourcePackStatusClientPacketData.class, "ResourcePackStatus");
        registerPacketName(SpectateClientPacketData.class, "Spectate");
        registerPacketName(SteerVehicleClientPacketData.class, "SteerVehicle");
        registerPacketName(TabCompleteClientPacketData.class, "TabComplete");
        registerPacketName(UpdateSignClientPacketData.class, "UpdateSign");
        registerPacketName(UseEntityClientPacketData.class, "UseEntity");

        // Server
        registerPacketName(AnimationServerPacketData.class, "Animation");
        registerPacketName(AttachEntityServerPacketData.class, "AttachEntity");
        registerPacketName(BlockActionServerPacketData.class, "BlockAction");
        registerPacketName(BlockBreakAnimServerPacketData.class, "BlockBreakAnim");
        registerPacketName(BlockChangeServerPacketData.class, "BlockChange");
        registerPacketName(CameraServerPacketData.class, "Camera");
        registerPacketName(ChangeGameStateServerPacketData.class, "ChangeGameState");
        registerPacketName(ChatMessageServerPacketData.class, "ChatMessage");
        registerPacketName(ChunkDataServerPacketData.class, "ChunkData");
        registerPacketName(CloseWindowServerPacketData.class, "CloseWindow");
        registerPacketName(CollectItemServerPacketData.class, "CollectItem");
        registerPacketName(CombatEventServerPacketData.class, "CombatEvent");
        registerPacketName(ConfirmTransactionServerPacketData.class, "ConfirmTransaction");
        registerPacketName(CustomPayloadServerPacketData.class, "CustomPayload");
        registerPacketName(DestroyEntitiesServerPacketData.class, "DestroyEntities");
        registerPacketName(DisconnectServerPacketData.class, "Disconnect");
        registerPacketName(DisplayScoreboardServerPacketData.class, "DisplayScoreboard");
        registerPacketName(EffectServerPacketData.class, "Effect");
        registerPacketName(EntityEffectServerPacketData.class, "EntityEffect");
        registerPacketName(EntityEquipmentServerPacketData.class, "EntityEquipment");
        registerPacketName(EntityHeadLookServerPacketData.class, "EntityHeadLook");
        registerPacketName(EntityLookMoveServerPacketData.class, "EntityLookMove");
        registerPacketName(EntityLookServerPacketData.class, "EntityLook");
        registerPacketName(EntityMetadataServerPacketData.class, "EntityMetadata");
        registerPacketName(EntityPropertiesServerPacketData.class, "EntityProperties");
        registerPacketName(EntityRelMoveServerPacketData.class, "EntityRelMove");
        registerPacketName(EntityServerPacketData.class, "Entity");
        registerPacketName(EntityStatusServerPacketData.class, "EntityStatus");
        registerPacketName(EntityTeleportServerPacketData.class, "EntityTeleport");
        registerPacketName(EntityVelocityServerPacketData.class, "EntityVelocity");
        registerPacketName(ExplosionServerPacketData.class, "Explosion");
        registerPacketName(HeldItemChangeServerPacketData.class, "HeldItemChange");
        registerPacketName(JoinGameServerPacketData.class, "JoinGame");
        registerPacketName(KeepAliveServerPacketData.class, "KeepAlive");
        registerPacketName(MapChunkBulkServerPacketData.class, "MapChunkBulk");
        registerPacketName(MapsServerPacketData.class, "Maps");
        registerPacketName(MultiBlockChangeServerPacketData.class, "MultiBlockChange");
        registerPacketName(OpenWindowServerPacketData.class, "OpenWindow");
        registerPacketName(ParticlesServerPacketData.class, "Particles");
        registerPacketName(PlayerAbilitiesServerPacketData.class, "PlayerAbilities");
        registerPacketName(PlayerListItemServerPacketData.class, "PlayerListItem");
        registerPacketName(PlayerListHeaderFooterServerPacketData.class, "PlayerListHeaderFooter");
        registerPacketName(PlayerPosLookServerPacketData.class, "PlayerPosLook");
        registerPacketName(RemoveEntityEffectServerPacketData.class, "RemoveEntityEffect");
        registerPacketName(RespawnServerPacketData.class, "Respawn");
        registerPacketName(ResourcePackSendServerPacketData.class, "ResourcePackSend");
        registerPacketName(ScoreboardObjectiveServerPacketData.class, "ScoreboardObjective");
        registerPacketName(ServerDifficultyServerPacketData.class, "ServerDifficulty");
        registerPacketName(SetExperienceServerPacketData.class, "SetExperience");
        registerPacketName(SetSlotServerPacketData.class, "SetSlot");
        registerPacketName(SignEditorOpenServerPacketData.class, "SignEditorOpen");
        registerPacketName(SoundEffectServerPacketData.class, "SoundEffect");
        registerPacketName(SpawnExperienceOrbServerPacketData.class, "SpawnExperienceOrb");
        registerPacketName(SpawnGlobalEntityServerPacketData.class, "SpawnGlobalEntity");
        registerPacketName(SpawnMobServerPacketData.class, "SpawnMob");
        registerPacketName(SpawnObjectServerPacketData.class, "SpawnObject");
        registerPacketName(SpawnPaintingServerPacketData.class, "SpawnPainting");
        registerPacketName(SpawnPlayerServerPacketData.class, "SpawnPlayer");
        registerPacketName(SpawnPositionServerPacketData.class, "SpawnPosition");
        registerPacketName(StatisticsServerPacketData.class, "Statistics");
        registerPacketName(TabCompleteServerPacketData.class, "TabComplete");
        registerPacketName(TeamsServerPacketData.class, "Teams");
        registerPacketName(TimeUpdateServerPacketData.class, "TimeUpdate");
        registerPacketName(TitleServerPacketData.class, "Title");
        registerPacketName(UpdateEntityNBTServerPacketData.class, "UpdateEntityNBT");
        registerPacketName(UpdateHealthServerPacketData.class, "UpdateHealth");
        registerPacketName(UpdateScoreServerPacketData.class, "UpdateScore");
        registerPacketName(UpdateSignServerPacketData.class, "UpdateSign");
        registerPacketName(UpdateTileEntityServerPacketData.class, "UpdateTileEntity");
        registerPacketName(UseBedServerPacketData.class, "UseBed");
        registerPacketName(WindowItemsServerPacketData.class, "WindowItems");
        registerPacketName(WindowPropertyServerPacketData.class, "WindowProperty");
        registerPacketName(WorldBorderServerPacketData.class, "WorldBorder");
    }

    public static void registerPacketName(Class<? extends PacketData> packetClass, String packetName) {
        packetNameMap.put(packetClass, packetName);
    }

    public static String getPacketName(Class<? extends PacketData> packetData) {
        return packetNameMap.getOrDefault(packetData, packetData.getSimpleName());
    }

    public static List<String> getAllPacketNames() {
        return ImmutableList.copyOf(packetNameMap.values());
    }

}
