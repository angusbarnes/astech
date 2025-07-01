package net.astr0.astech;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class ModTags {

    public static ResourceLocation RL(String path) {
        return new ResourceLocation("astech", path);
    }

    public static final TagKey<Block> BLOCK_CABLE = BlockTags.create(RL("cable_block"));
    public static TagKey<Block> MACHINE_CASING = BlockTags.create(RL("casing"));

    public static final TagKey<Item> CHEMICAL_PROTECTION = ItemTags.create(RL("chemical_protection"));


    public static final TagKey<Fluid> TIER_1_COOLANT = FluidTags.create(new ResourceLocation("forge", "tier_1_coolant"));
    public static final TagKey<Fluid> TIER_2_COOLANT = FluidTags.create(new ResourceLocation("forge", "tier_2_coolant"));

    public static final TagKey<Fluid> PHOTORESIST_TAG = FluidTags.create(RL("photoresist"));
    public static final TagKey<Fluid> TIER_1_PHOTORESIST = FluidTags.create(RL("photoresist/tier_1"));
    public static final TagKey<Fluid> TIER_2_PHOTORESIST = FluidTags.create(RL("photoresist/tier_2"));
}
