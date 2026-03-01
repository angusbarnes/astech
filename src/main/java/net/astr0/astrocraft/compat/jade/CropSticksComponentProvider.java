package net.astr0.astrocraft.compat.jade;

import net.astr0.astrocraft.block.CropSticksBlockEntity;
import net.astr0.astrocraft.farming.CropGenome;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum CropSticksComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(
            ITooltip tooltip,
            BlockAccessor accessor,
            IPluginConfig config
    ) {
        if (accessor.getServerData().contains("genes")) {
            tooltip.add(
                    Component.literal(accessor.getServerData().getString("genes"))
            );
        }
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        CropSticksBlockEntity sticks = (CropSticksBlockEntity) accessor.getBlockEntity();
        CropGenome genetics = sticks.getGenes();
        if (genetics != null) {
            data.putString("genes", sticks.getGenes().genome());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadePlugin.CROP_PROVIDER;
    }
}
