package net.astr0.astrocraft.compat.jade;

import net.astr0.astrocraft.Astrocraft;
import net.astr0.astrocraft.block.CropSticksBlock;
import net.astr0.astrocraft.block.CropSticksBlockEntity;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {
    public static final ResourceLocation CROP_PROVIDER = new ResourceLocation(Astrocraft.MODID, "crop");
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(CropSticksComponentProvider.INSTANCE, CropSticksBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(CropSticksComponentProvider.INSTANCE, CropSticksBlock.class);
    }
}