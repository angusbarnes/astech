package net.astr0.astech.block;

import net.astr0.astech.AsTech;
import net.astr0.astech.block.ChemicalMixer.ChemicalMixerStationBlockEntity;
import net.astr0.astech.block.GemPolisher.GemPolishingStationBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, AsTech.MODID);

    public static final RegistryObject<BlockEntityType<GemPolishingStationBlockEntity>> GEM_POLISHING_BE =
            BLOCK_ENTITIES.register("gem_polishing_be", () ->
                    BlockEntityType.Builder.of(GemPolishingStationBlockEntity::new,
                            ModBlocks.GEM_POLISHING_STATION.get()).build(null));

    public static final RegistryObject<BlockEntityType<ChemicalMixerStationBlockEntity>> CHEMICAL_MIXER_BE =
            BLOCK_ENTITIES.register("chemical_mixer", () ->
                    BlockEntityType.Builder.of(ChemicalMixerStationBlockEntity::new,
                            ModBlocks.CHEMICAL_MIXER.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
