package net.astr0.astech.item;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = "astech", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ColorRegisterHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void registerFluidCellColors(RegisterColorHandlersEvent.Item event) {

        event.register((stack, tintIndex) -> {

            if (tintIndex == 1) { // layer1 (overlay) gets tinted
                FluidStack fluid = FluidCellItem.getFluid(stack);
                if (!fluid.isEmpty()) {
                    // Get fluid color using client extensions
                    IClientFluidTypeExtensions fluidExtensions = IClientFluidTypeExtensions.of(fluid.getFluid());
                    int fluidColor = fluidExtensions.getTintColor(fluid);

                    // If no specific tint color, try block colors as fallback
                    if (fluidColor == -1 || fluidColor == 0xFFFFFF) {
                        try {
                            BlockState fluidBlock = fluid.getFluid().defaultFluidState().createLegacyBlock();
                            fluidColor = Minecraft.getInstance().getBlockColors()
                                    .getColor(fluidBlock, null, null, 0);
                        } catch (Exception e) {
                            // Fallback to known fluid colors
                            if (fluid.getFluid() == Fluids.WATER) {
                                fluidColor = 0x3F76E4; // Water blue
                            } else if (fluid.getFluid() == Fluids.LAVA) {
                                fluidColor = 0xFF6600; // Lava orange-red
                            } else {
                                fluidColor = 0x4169E1; // Default blue for unknown fluids
                            }
                        }
                    }

                    return fluidColor;
                }
                return 0x333333; // Transparent/black when empty
            }
            return 0xFFFFFF; // No tinting for layer0 (base)
        }, ModItems.FLUID_CELL.get(), ModItems.PRESSURISED_FLUID_CELL.get(), ModItems.LARGE_FLUID_CELL.get());

    }
}