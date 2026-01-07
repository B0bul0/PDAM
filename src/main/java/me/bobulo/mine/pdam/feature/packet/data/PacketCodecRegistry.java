package me.bobulo.mine.pdam.feature.packet.data;

import me.bobulo.mine.pdam.feature.packet.ConnectionState;
import me.bobulo.mine.pdam.feature.packet.PacketDirection;
import me.bobulo.mine.pdam.feature.packet.data.client.*;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataExtractor;
import me.bobulo.mine.pdam.feature.packet.data.reader.PacketDataSerializer;
import me.bobulo.mine.pdam.feature.packet.data.server.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class PacketCodecRegistry {

    private static final Map<Class<? extends Packet<?>>, PacketDataExtractor<?, ?>> extractors = new ConcurrentHashMap<>();
    private static final Map<SerializerKey, PacketDataSerializer<?>> serializers = new ConcurrentHashMap<>();

    static {
        registerDefaults();
    }

    public static void registerDefaults() {
        // Server
        registerExtractor(S00PacketKeepAlive.class, new KeepAliveServerPacketData.Extractor());
        registerExtractor(S01PacketJoinGame.class, new JoinGameServerPacketData.Extractor());
        registerExtractor(S02PacketChat.class, new ChatMessageServerPacketData.Extractor());
        registerExtractor(S03PacketTimeUpdate.class, new TimeUpdateServerPacketData.Extractor());
        registerExtractor(S04PacketEntityEquipment.class, new EntityEquipmentServerPacketData.Extractor());
        registerExtractor(S05PacketSpawnPosition.class, new SpawnPositionServerPacketData.Extractor());
        registerExtractor(S06PacketUpdateHealth.class, new UpdateHealthServerPacketData.Extractor());
        registerExtractor(S07PacketRespawn.class, new RespawnServerPacketData.Extractor());
        registerExtractor(S08PacketPlayerPosLook.class, new PlayerPosLookServerPacketData.Extractor());
        registerExtractor(S09PacketHeldItemChange.class, new HeldItemChangeServerPacketData.Extractor());
        registerSerializer(new UseBedServerPacketData.Serializer());
        registerExtractor(S0BPacketAnimation.class, new AnimationServerPacketData.Extractor());
        registerExtractor(S0CPacketSpawnPlayer.class, new SpawnPlayerServerPacketData.Extractor());
        registerExtractor(S0DPacketCollectItem.class, new CollectItemServerPacketData.Extractor());
        registerExtractor(S0EPacketSpawnObject.class, new SpawnObjectServerPacketData.Extractor());
        registerExtractor(S0FPacketSpawnMob.class, new SpawnMobServerPacketData.Extractor());
        registerExtractor(S10PacketSpawnPainting.class, new SpawnPaintingServerPacketData.Extractor());
        registerExtractor(S11PacketSpawnExperienceOrb.class, new SpawnExperienceOrbServerPacketData.Extractor());
        registerExtractor(S12PacketEntityVelocity.class, new EntityVelocityServerPacketData.Extractor());
        registerExtractor(S13PacketDestroyEntities.class, new DestroyEntitiesServerPacketData.Extractor());
        registerSerializer(new EntityServerPacketData.Serializer());
        registerSerializer(new EntityRelMoveServerPacketData.Serializer());
        registerSerializer(new EntityLookServerPacketData.Serializer());
        registerSerializer(new EntityLookMoveServerPacketData.Serializer());
        registerExtractor(S18PacketEntityTeleport.class, new EntityTeleportServerPacketData.Extractor());
        registerSerializer(new EntityHeadLookServerPacketData.Serializer());
        registerSerializer(new EntityStatusServerPacketData.Serializer());
        registerExtractor(S1BPacketEntityAttach.class, new AttachEntityServerPacketData.Extractor());
        registerExtractor(S1CPacketEntityMetadata.class, new EntityMetadataServerPacketData.Extractor());
        registerExtractor(S1DPacketEntityEffect.class, new EntityEffectServerPacketData.Extractor());
        registerExtractor(S1EPacketRemoveEntityEffect.class, new RemoveEntityEffectServerPacketData.Extractor());
        registerExtractor(S1FPacketSetExperience.class, new SetExperienceServerPacketData.Extractor());
        registerExtractor(S20PacketEntityProperties.class, new EntityPropertiesServerPacketData.Extractor());
        registerSerializer(new ChunkDataServerPacketData.Serializer());
        registerSerializer(new MultiBlockChangeServerPacketData.Serializer());
        registerSerializer(new BlockChangeServerPacketData.Serializer());
        registerSerializer(new BlockActionServerPacketData.Serializer());
        registerExtractor(S25PacketBlockBreakAnim.class, new BlockBreakAnimServerPacketData.Extractor());
        registerSerializer(new MapChunkBulkServerPacketData.Serializer());
        registerExtractor(S27PacketExplosion.class, new ExplosionServerPacketData.Extractor());
        registerExtractor(S28PacketEffect.class, new EffectServerPacketData.Extractor());
        registerExtractor(S29PacketSoundEffect.class, new SoundEffectServerPacketData.Extractor());
        registerExtractor(S2APacketParticles.class, new ParticlesServerPacketData.Extractor());
        registerExtractor(S2BPacketChangeGameState.class, new ChangeGameStateServerPacketData.Extractor());
        registerExtractor(S2CPacketSpawnGlobalEntity.class, new SpawnGlobalEntityServerPacketData.Extractor());
        registerExtractor(S2DPacketOpenWindow.class, new OpenWindowServerPacketData.Extractor());
        registerSerializer(new CloseWindowServerPacketData.Serializer());
        registerExtractor(S2FPacketSetSlot.class, new SetSlotServerPacketData.Extractor());
        registerExtractor(S30PacketWindowItems.class, new WindowItemsServerPacketData.Extractor());
        registerExtractor(S31PacketWindowProperty.class, new WindowPropertyServerPacketData.Extractor());
        registerExtractor(S32PacketConfirmTransaction.class, new ConfirmTransactionServerPacketData.Extractor());
        registerExtractor(S33PacketUpdateSign.class, new UpdateSignServerPacketData.Extractor());
        registerSerializer(new MapsServerPacketData.Serializer());
        registerExtractor(S35PacketUpdateTileEntity.class, new UpdateTileEntityServerPacketData.Extractor());
        registerExtractor(S36PacketSignEditorOpen.class, new SignEditorOpenServerPacketData.Extractor());
        registerExtractor(S37PacketStatistics.class, new StatisticsServerPacketData.Extractor());
        registerExtractor(S38PacketPlayerListItem.class, new PlayerListItemServerPacketData.Extractor());
        registerExtractor(S39PacketPlayerAbilities.class, new PlayerAbilitiesServerPacketData.Extractor());
        registerExtractor(S3APacketTabComplete.class, new TabCompleteServerPacketData.Extractor());
        registerExtractor(S3BPacketScoreboardObjective.class, new ScoreboardObjectiveServerPacketData.Extractor());
        registerExtractor(S3CPacketUpdateScore.class, new UpdateScoreServerPacketData.Extractor());
        registerExtractor(S3DPacketDisplayScoreboard.class, new DisplayScoreboardServerPacketData.Extractor());
        registerExtractor(S3EPacketTeams.class, new TeamsServerPacketData.Extractor());
        registerExtractor(S3FPacketCustomPayload.class, new CustomPayloadServerPacketData.Extractor());
        registerExtractor(S40PacketDisconnect.class, new DisconnectServerPacketData.Extractor());
        registerExtractor(S41PacketServerDifficulty.class, new ServerDifficultyServerPacketData.Extractor());
        registerExtractor(S42PacketCombatEvent.class, new CombatEventServerPacketData.Extractor());
        registerSerializer(new CameraServerPacketData.Serializer());
        registerSerializer(new WorldBorderServerPacketData.Serializer());
        registerExtractor(S45PacketTitle.class, new TitleServerPacketData.Extractor());
        registerExtractor(S47PacketPlayerListHeaderFooter.class, new PlayerListHeaderFooterServerPacketData.Extractor());
        registerExtractor(S48PacketResourcePackSend.class, new ResourcePackSendServerPacketData.Extractor());
        registerSerializer(new UpdateEntityNBTServerPacketData.Serializer());

        // Client
        registerExtractor(C00PacketKeepAlive.class, new KeepAliveClientPacketData.Extractor());
        registerExtractor(C01PacketChatMessage.class, new ChatMessageClientPacketData.Extractor());
        registerSerializer(new UseEntityClientPacketData.Serializer());
        registerExtractor(C03PacketPlayer.class, new PlayerClientPacketData.Extractor());
        registerExtractor(C03PacketPlayer.C04PacketPlayerPosition.class, new PlayerPositionClientPacketData.PositionExtractor());
        registerExtractor(C03PacketPlayer.C05PacketPlayerLook.class, new PlayerLookClientPacketData.LookExtractor());
        registerExtractor(C03PacketPlayer.C06PacketPlayerPosLook.class, new PlayerPositionLookClientPacketData.PositionLookExtractor());
        registerExtractor(C07PacketPlayerDigging.class, new PlayerDiggingClientPacketData.Extractor());
        registerExtractor(C08PacketPlayerBlockPlacement.class, new PlayerBlockPlacementClientPacketData.Extractor());
        registerExtractor(C09PacketHeldItemChange.class, new HeldItemChangeClientPacketData.Extractor());
        registerExtractor(C0APacketAnimation.class, new AnimationClientPacketData.Extractor());
        registerSerializer(new EntityActionClientPacketData.Serializer());
        registerExtractor(C0CPacketInput.class, new SteerVehicleClientPacketData.Extractor());
        registerSerializer(new CloseWindowClientPacketData.Serializer());
        registerExtractor(C0EPacketClickWindow.class, new ClickWindowClientPacketData.Extractor());
        registerSerializer(new ConfirmTransactionClientPacketData.Serializer());
        registerExtractor(C10PacketCreativeInventoryAction.class, new CreativeInventoryActionClientPacketData.Extractor());
        registerExtractor(C11PacketEnchantItem.class, new EnchantItemClientPacketData.Extractor());
        registerExtractor(C12PacketUpdateSign.class, new UpdateSignClientPacketData.Extractor());
        registerSerializer(new PlayerAbilitiesClientPacketData.Serializer());
        registerExtractor(C14PacketTabComplete.class, new TabCompleteClientPacketData.Extractor());
        registerSerializer(new ClientSettingsClientPacketData.Serializer());
        registerExtractor(C16PacketClientStatus.class, new ClientStatusClientPacketData.Extractor());
        registerExtractor(C17PacketCustomPayload.class, new PluginMessageClientPacketData.Extractor());
        registerSerializer(new SpectateClientPacketData.Serializer());
        registerSerializer(new ResourcePackStatusClientPacketData.Serializer());
    }

    /* Extractor */

    public static <T extends PacketData, E extends Packet<?>> void registerExtractor(Class<E> packetClass, PacketDataExtractor<T, E> extractor) {
        extractors.put(packetClass, extractor);
    }

    public static <T extends PacketData, E extends Packet<?>> PacketDataExtractor<?, ?> getExtractor(Class<E> packetClass) {
        return extractors.get(packetClass);
    }

    @SuppressWarnings("unchecked")
    public static <T extends PacketData> T extract(Packet<?> packet) throws IOException {
        PacketDataExtractor<T, Packet<?>> extractor = (PacketDataExtractor<T, Packet<?>>) extractors.get(packet.getClass());
        if (extractor != null) {
            return extractor.extract(packet);
        }

        return null;
    }

    /* Serializer */

    public static <T extends PacketData> void registerSerializer(PacketDataSerializer<T> serializer) {
        serializers.put(serializer.getKey(), serializer);
    }

    @SuppressWarnings("unchecked")
    public static <T extends PacketData> PacketDataSerializer<T> getSerializer(SerializerKey key) {
        return (PacketDataSerializer<T>) serializers.get(key);
    }

    @SuppressWarnings("unchecked")
    public static <T extends PacketData> T decode(ConnectionState state, PacketDirection direction, int packetId, PacketDataBuffer buf) throws IOException {
        SerializerKey key = new SerializerKey(state, direction, packetId);

        PacketDataSerializer<T> serializer = (PacketDataSerializer<T>) serializers.get(key);
        if (serializer != null) {
            return serializer.read(buf);
        }

        return null;
    }

}
