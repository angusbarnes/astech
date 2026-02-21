package net.astr0.astrocraft.compat.jade;

import net.astr0.astrocraft.block.CropSticksBlockEntity;
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
        if (accessor.getServerData().contains("Fuel")) {
            tooltip.add(
                    Component.translatable(
                            "mymod.fuel",
                            accessor.getServerData().getInt("Fuel")
                    )
            );
        }
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        CropSticksBlockEntity furnace = (CropSticksBlockEntity) accessor.getBlockEntity();
        //data.putInt("Fuel", furnace.litTime);
    }

    @Override
    public ResourceLocation getUid() {
        return JadePlugin.CROP_PROVIDER;
    }
}
