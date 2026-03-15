package me.bobulo.mine.pdam.feature.packet.data;

import com.google.common.collect.ImmutableList;
import me.bobulo.mine.pdam.feature.packet.data.client.*;
import me.bobulo.mine.pdam.feature.packet.data.server.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class PacketNameRegistry {

    private static final Map<Class<? extends PacketData>, String> packetNameMap = new HashMap<>();
    private static final Map<Class<? extends PacketData>, Integer> packetIdMap = new HashMap<>();

    static {
        registerDefaults();
    }

    public static void registerDefaults() {
        // Client
        registerPacketIdName(AnimationClientPacketData.class, 0x0A, "Animation");
        registerPacketIdName(ChatMessageClientPacketData.class, 0x01, "ChatMessage");
        registerPacketIdName(ClickWindowClientPacketData.class, 0x0E, "ClickWindow");
        registerPacketIdName(ClientSettingsClientPacketData.class, 0x15, "ClientSettings");
        registerPacketIdName(ClientStatusClientPacketData.class, 0x16, "ClientStatus");
        registerPacketIdName(CloseWindowClientPacketData.class, 0x0D, "CloseWindow");
        registerPacketIdName(ConfirmTransactionClientPacketData.class, 0x0F, "ConfirmTransaction");
        registerPacketIdName(CreativeInventoryActionClientPacketData.class, 0x10, "CreativeInventoryAction");
        registerPacketIdName(EnchantItemClientPacketData.class, 0x11, "EnchantItem");
        registerPacketIdName(EntityActionClientPacketData.class, 0x0B, "EntityAction");
        registerPacketIdName(HeldItemChangeClientPacketData.class, 0x09, "HeldItemChange");
        registerPacketIdName(KeepAliveClientPacketData.class, 0x00, "KeepAlive");
        registerPacketIdName(PlayerAbilitiesClientPacketData.class, 0x13, "PlayerAbilities");
        registerPacketIdName(PlayerBlockPlacementClientPacketData.class, 0x08, "PlayerBlockPlacement");
        registerPacketIdName(PlayerClientPacketData.class, 0x03, "Player");
        registerPacketIdName(PlayerDiggingClientPacketData.class, 0x07, "PlayerDigging");
        registerPacketIdName(PlayerLookClientPacketData.class, 0x05, "PlayerLook");
        registerPacketIdName(PlayerPositionClientPacketData.class, 0x04, "PlayerPosition");
        registerPacketIdName(PlayerPositionLookClientPacketData.class, 0x06, "PlayerPositionLook");
        registerPacketIdName(PluginMessageClientPacketData.class, 0x17, "PluginMessage");
        registerPacketIdName(ResourcePackStatusClientPacketData.class, 0x19, "ResourcePackStatus");
        registerPacketIdName(SpectateClientPacketData.class, 0x18, "Spectate");
        registerPacketIdName(SteerVehicleClientPacketData.class, 0x0C, "SteerVehicle");
        registerPacketIdName(TabCompleteClientPacketData.class, 0x14, "TabComplete");
        registerPacketIdName(UpdateSignClientPacketData.class, 0x12, "UpdateSign");
        registerPacketIdName(UseEntityClientPacketData.class, 0x02, "UseEntity");

        // Server
        registerPacketIdName(AnimationServerPacketData.class, 0x0B, "Animation");
        registerPacketIdName(AttachEntityServerPacketData.class, 0x1B, "AttachEntity");
        registerPacketIdName(BlockActionServerPacketData.class, 0x24, "BlockAction");
        registerPacketIdName(BlockBreakAnimServerPacketData.class, 0x25, "BlockBreakAnim");
        registerPacketIdName(BlockChangeServerPacketData.class, 0x23, "BlockChange");
        registerPacketIdName(CameraServerPacketData.class, 0x43, "Camera");
        registerPacketIdName(ChangeGameStateServerPacketData.class, 0x2B, "ChangeGameState");
        registerPacketIdName(ChatMessageServerPacketData.class, 0x02, "ChatMessage");
        registerPacketIdName(ChunkDataServerPacketData.class, 0x21, "ChunkData");
        registerPacketIdName(CloseWindowServerPacketData.class, 0x2E, "CloseWindow");
        registerPacketIdName(CollectItemServerPacketData.class, 0x0D, "CollectItem");
        registerPacketIdName(CombatEventServerPacketData.class, 0x42, "CombatEvent");
        registerPacketIdName(ConfirmTransactionServerPacketData.class, 0x32, "ConfirmTransaction");
        registerPacketIdName(CustomPayloadServerPacketData.class, 0x3F, "CustomPayload");
        registerPacketIdName(DestroyEntitiesServerPacketData.class, 0x13, "DestroyEntities");
        registerPacketIdName(DisconnectServerPacketData.class, 0x40, "Disconnect");
        registerPacketIdName(DisplayScoreboardServerPacketData.class, 0x3D, "DisplayScoreboard");
        registerPacketIdName(EffectServerPacketData.class, 0x28, "Effect");
        registerPacketIdName(EntityEffectServerPacketData.class, 0x1D, "EntityEffect");
        registerPacketIdName(EntityEquipmentServerPacketData.class, 0x04, "EntityEquipment");
        registerPacketIdName(EntityHeadLookServerPacketData.class, 0x19, "EntityHeadLook");
        registerPacketIdName(EntityLookMoveServerPacketData.class, 0x17, "EntityLookMove");
        registerPacketIdName(EntityLookServerPacketData.class, 0x16, "EntityLook");
        registerPacketIdName(EntityMetadataServerPacketData.class, 0x1C, "EntityMetadata");
        registerPacketIdName(EntityPropertiesServerPacketData.class, 0x20, "EntityProperties");
        registerPacketIdName(EntityRelMoveServerPacketData.class, 0x15, "EntityRelMove");
        registerPacketIdName(EntityServerPacketData.class, 0x14, "Entity");
        registerPacketIdName(EntityStatusServerPacketData.class, 0x1A, "EntityStatus");
        registerPacketIdName(EntityTeleportServerPacketData.class, 0x18, "EntityTeleport");
        registerPacketIdName(EntityVelocityServerPacketData.class, 0x12, "EntityVelocity");
        registerPacketIdName(ExplosionServerPacketData.class, 0x27, "Explosion");
        registerPacketIdName(HeldItemChangeServerPacketData.class, 0x09, "HeldItemChange");
        registerPacketIdName(JoinGameServerPacketData.class, 0x01, "JoinGame");
        registerPacketIdName(KeepAliveServerPacketData.class, 0x00, "KeepAlive");
        registerPacketIdName(MapChunkBulkServerPacketData.class, 0x26, "MapChunkBulk");
        registerPacketIdName(MapsServerPacketData.class, 0x34, "Maps");
        registerPacketIdName(MultiBlockChangeServerPacketData.class, 0x22, "MultiBlockChange");
        registerPacketIdName(OpenWindowServerPacketData.class, 0x2D, "OpenWindow");
        registerPacketIdName(ParticlesServerPacketData.class, 0x2A, "Particles");
        registerPacketIdName(PlayerAbilitiesServerPacketData.class, 0x39, "PlayerAbilities");
        registerPacketIdName(PlayerListItemServerPacketData.class, 0x38, "PlayerListItem");
        registerPacketIdName(PlayerListHeaderFooterServerPacketData.class, 0x47, "PlayerListHeaderFooter");
        registerPacketIdName(PlayerPosLookServerPacketData.class, 0x08, "PlayerPosLook");
        registerPacketIdName(RemoveEntityEffectServerPacketData.class, 0x1E, "RemoveEntityEffect");
        registerPacketIdName(RespawnServerPacketData.class, 0x07, "Respawn");
        registerPacketIdName(ResourcePackSendServerPacketData.class, 0x48, "ResourcePackSend");
        registerPacketIdName(ScoreboardObjectiveServerPacketData.class, 0x3B, "ScoreboardObjective");
        registerPacketIdName(ServerDifficultyServerPacketData.class, 0x41, "ServerDifficulty");
        registerPacketIdName(SetExperienceServerPacketData.class, 0x1F, "SetExperience");
        registerPacketIdName(SetSlotServerPacketData.class, 0x2F, "SetSlot");
        registerPacketIdName(SignEditorOpenServerPacketData.class, 0x36, "SignEditorOpen");
        registerPacketIdName(SoundEffectServerPacketData.class, 0x29, "SoundEffect");
        registerPacketIdName(SpawnExperienceOrbServerPacketData.class, 0x11, "SpawnExperienceOrb");
        registerPacketIdName(SpawnGlobalEntityServerPacketData.class, 0x2C, "SpawnGlobalEntity");
        registerPacketIdName(SpawnMobServerPacketData.class, 0x0F, "SpawnMob");
        registerPacketIdName(SpawnObjectServerPacketData.class, 0x0E, "SpawnObject");
        registerPacketIdName(SpawnPaintingServerPacketData.class, 0x10, "SpawnPainting");
        registerPacketIdName(SpawnPlayerServerPacketData.class, 0x0C, "SpawnPlayer");
        registerPacketIdName(SpawnPositionServerPacketData.class, 0x05, "SpawnPosition");
        registerPacketIdName(StatisticsServerPacketData.class, 0x37, "Statistics");
        registerPacketIdName(TabCompleteServerPacketData.class, 0x3A, "TabComplete");
        registerPacketIdName(TeamsServerPacketData.class, 0x3E, "Teams");
        registerPacketIdName(TimeUpdateServerPacketData.class, 0x03, "TimeUpdate");
        registerPacketIdName(TitleServerPacketData.class, 0x45, "Title");
        registerPacketIdName(UpdateEntityNBTServerPacketData.class, 0x49, "UpdateEntityNBT");
        registerPacketIdName(UpdateHealthServerPacketData.class, 0x06, "UpdateHealth");
        registerPacketIdName(UpdateScoreServerPacketData.class, 0x3C, "UpdateScore");
        registerPacketIdName(UpdateSignServerPacketData.class, 0x33, "UpdateSign");
        registerPacketIdName(UpdateTileEntityServerPacketData.class, 0x35, "UpdateTileEntity");
        registerPacketIdName(UseBedServerPacketData.class, 0x0A, "UseBed");
        registerPacketIdName(WindowItemsServerPacketData.class, 0x30, "WindowItems");
        registerPacketIdName(WindowPropertyServerPacketData.class, 0x31, "WindowProperty");
        registerPacketIdName(WorldBorderServerPacketData.class, 0x44, "WorldBorder");
    }

    public static void registerPacketIdName(Class<? extends PacketData> packetClass, int packetId, String packetName) {
        packetNameMap.put(packetClass, packetName);
        packetIdMap.put(packetClass, packetId);
    }

    public static int getPacketId(Class<? extends PacketData> packetData) {
        return packetIdMap.getOrDefault(packetData, -1);
    }

    public static String getPacketName(Class<? extends PacketData> packetData) {
        return packetNameMap.getOrDefault(packetData, packetData.getSimpleName());
    }

    public static String getPacketIdName(Class<? extends PacketData> packetData) {
        int packetId = getPacketId(packetData);
        String packetName = getPacketName(packetData);

        if (packetId == -1) {
            return packetName;
        } else {
            return String.format("0x%02X", packetId) + " " + packetName;
        }
    }

    public static List<String> getAllPacketNames() {
        return ImmutableList.copyOf(packetNameMap.values());
    }

    public static List<String> getAllPacketIdNames() {
        return packetNameMap.entrySet().stream()
          .map(entry -> {
              int packetId = packetIdMap.getOrDefault(entry.getKey(), -1);
              String packetName = entry.getValue();
              if (packetId == -1) {
                  return packetName;
              } else {
                  return String.format("0x%02X", packetId) + " " + packetName;
              }
          })
          .collect(Collectors.toList());
    }

}
