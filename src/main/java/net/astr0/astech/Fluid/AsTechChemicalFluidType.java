package net.astr0.astech.Fluid;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.astr0.astech.Astrocraft;
import net.astr0.astech.Fluid.helpers.AsTechFluidType;
import net.astr0.astech.gui.TintColor;
import net.astr0.astech.item.HazardBehavior;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.function.Consumer;

public class AsTechChemicalFluidType extends FluidType {

    public static final ResourceLocation WATER_STILL_RL = new ResourceLocation("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = new ResourceLocation("block/water_flow");
    public static final ResourceLocation THICC_STILL_RL = new ResourceLocation(Astrocraft.MODID, "block/fluid/thick_still");
    public static final ResourceLocation THICC_FLOWING_RL = new ResourceLocation(Astrocraft.MODID, "block/fluid/thick_flow");
    public static final ResourceLocation MEDIUM_STILL_RL = new ResourceLocation(Astrocraft.MODID, "block/fluid/medium_still");
    public static final ResourceLocation MEDIUM_FLOWING_RL = new ResourceLocation(Astrocraft.MODID, "block/fluid/medium_flow");
    public static final ResourceLocation THIN_STILL_RL = new ResourceLocation(Astrocraft.MODID, "block/fluid/thin_still");
    public static final ResourceLocation THIN_FLOWING_RL = new ResourceLocation(Astrocraft.MODID, "block/fluid/thin_flow");


    private final ResourceLocation stillTexture;
    private final ResourceLocation flowingTexture;
    private final ResourceLocation overlayTexture;
    private final int tintColor;
    private final Vector3f fogColor;
    private final AsTechFluidType type;
    private final HazardBehavior hazardBehavior;


    public AsTechChemicalFluidType(TintColor tint, AsTechFluidType type, final Properties properties, HazardBehavior.BehaviorType hazardType) {
        super(properties);

        this.tintColor = tint.getTintColor();
        this.fogColor = tint.getFogColor();
        this.type = type;
        if(type == AsTechFluidType.GAS) {
            this.stillTexture = THIN_STILL_RL;
            this.flowingTexture = THIN_FLOWING_RL;
        } else {
            if (Math.random() < .5){
                this.stillTexture = THICC_STILL_RL;
                this.flowingTexture = THICC_FLOWING_RL;
            } else {
                this.stillTexture = MEDIUM_STILL_RL;
                this.flowingTexture = MEDIUM_FLOWING_RL;
            }
        }

        this.overlayTexture = WATER_STILL_RL;

        hazardBehavior = new HazardBehavior(hazardType);
    }

    public HazardBehavior getHazardBehavior() {
        return hazardBehavior;
    }

    @Override
    public boolean isVaporizedOnPlacement(Level level, BlockPos pos, FluidStack stack) {
        return (type == AsTechFluidType.GAS); // Only vanish if we are a gas
    }

    @Override
    public void onVaporize(@Nullable Player player, Level level, BlockPos pos, FluidStack stack) {
        SoundEvent sound = this.getSound(player, level, pos, SoundActions.FLUID_VAPORIZE);
        level.playSound(player, pos, sound != null ? sound : SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (level.random.nextFloat() - level.random.nextFloat()) * 0.8F);
        // Failed attempt at doing some shit. May need to broadcast as a server event or some shit. May also need to apply to player directly
        /*
        if (player != null) {
            List<Player> players = level.getNearbyPlayers(TargetingConditions.forNonCombat(), player, AABB.ofSize(pos.getCenter(), 5,5, 5));

            for(Player p : players) {
                p.addEffect(new MobEffectInstance(MobEffects.POISON, 20, 1));
            }
        }
        */

        for(int l = 0; l < 9; ++l) {
            level.addAlwaysVisibleParticle(ParticleTypes.POOF, (double)pos.getX() + Math.random(), (double)pos.getY() + Math.random(), (double)pos.getZ() + Math.random(), 0.0, 0.0, 0.0);
        }

    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return stillTexture;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return flowingTexture;
            }

            @Override
            public @Nullable ResourceLocation getOverlayTexture() {
                return overlayTexture;
            }

            @Override
            public int getTintColor() {
                return tintColor;
            }

            @Override
            public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level,
                                                    int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                return fogColor;
            }

            @Override
            public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick,
                                        float nearDistance, float farDistance, FogShape shape) {
                RenderSystem.setShaderFogStart(1f);
                RenderSystem.setShaderFogEnd(6f); // distance when the fog starts
            }
        });
    }
}