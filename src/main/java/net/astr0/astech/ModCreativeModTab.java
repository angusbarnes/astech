package net.astr0.astech;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AsTech.MODID);

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

    public static final RegistryObject<CreativeModeTab> ASTECH_TAB = CREATIVE_MODE_TABS.register("astech_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.DEEZ_NUTS_ITEM.get()))
                    .title(Component.translatable("creativetab.astech_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.DEEZ_NUTS_ITEM.get());
                        output.accept(ModItems.DEEZ_BUTTS_ITEM.get());
                        output.accept(ModBlocks.NIC_BLOCK.get());

                        //#anchor TAB_REGION
output.accept(ModItems.POLYTETRAFLUOROETHYLENE_INGOT.get());
output.accept(ModItems.POLYTETRAFLUOROETHYLENE_DUST.get());
output.accept(ModItems.DIMETHYL_ETHER_INGOT.get());
output.accept(ModItems.DIMETHYL_ETHER_DUST.get());
output.accept(ModItems.HYDROCARBONIC_BROTH_INGOT.get());
output.accept(ModItems.HYDROCARBONIC_BROTH_DUST.get());
output.accept(ModItems.ETHANE_INGOT.get());
output.accept(ModItems.ETHANE_DUST.get());
output.accept(ModItems.PISS_WATER_INGOT.get());
output.accept(ModItems.PISS_WATER_DUST.get());
output.accept(ModItems.ALUMUNIUM_HYDROXIDE_INGOT.get());
output.accept(ModItems.ALUMUNIUM_HYDROXIDE_DUST.get());
output.accept(ModItems.SULFURIC_ACID_INGOT.get());
output.accept(ModItems.SULFURIC_ACID_DUST.get());
output.accept(ModItems.AMMONIA_INGOT.get());
output.accept(ModItems.AMMONIA_DUST.get());
output.accept(ModItems.BENZENE_INGOT.get());
output.accept(ModItems.BENZENE_DUST.get());
output.accept(ModItems.CHLORINE_INGOT.get());
output.accept(ModItems.CHLORINE_DUST.get());
output.accept(ModItems.ACETONE_INGOT.get());
output.accept(ModItems.ACETONE_DUST.get());
output.accept(ModItems.METHANOL_INGOT.get());
output.accept(ModItems.METHANOL_DUST.get());
output.accept(ModItems.HYDROGEN_INGOT.get());
output.accept(ModItems.HYDROGEN_DUST.get());
output.accept(ModItems.NITROGEN_INGOT.get());
output.accept(ModItems.NITROGEN_DUST.get());
output.accept(ModItems.TOLUENE_INGOT.get());
output.accept(ModItems.TOLUENE_DUST.get());
output.accept(ModItems.PROPANE_INGOT.get());
output.accept(ModItems.PROPANE_DUST.get());
output.accept(ModItems.ETHANOL_INGOT.get());
output.accept(ModItems.ETHANOL_DUST.get());
output.accept(ModItems.FORMALDEHYDE_INGOT.get());
output.accept(ModItems.FORMALDEHYDE_DUST.get());
output.accept(ModItems.HEXANE_INGOT.get());
output.accept(ModItems.HEXANE_DUST.get());
output.accept(ModItems.BUTANE_INGOT.get());
output.accept(ModItems.BUTANE_DUST.get());
output.accept(ModItems.CARBON_TETRACHLORIDE_INGOT.get());
output.accept(ModItems.CARBON_TETRACHLORIDE_DUST.get());
output.accept(ModItems.ETHYLENE_GLYCOL_INGOT.get());
output.accept(ModItems.ETHYLENE_GLYCOL_DUST.get());
output.accept(ModItems.ACETIC_ACID_INGOT.get());
output.accept(ModItems.ACETIC_ACID_DUST.get());
output.accept(ModItems.METHYL_CHLORIDE_INGOT.get());
output.accept(ModItems.METHYL_CHLORIDE_DUST.get());
output.accept(ModItems.PHOSGENE_INGOT.get());
output.accept(ModItems.PHOSGENE_DUST.get());
output.accept(ModItems.ISOPROPANOL_INGOT.get());
output.accept(ModItems.ISOPROPANOL_DUST.get());
output.accept(ModItems.ANILINE_INGOT.get());
output.accept(ModItems.ANILINE_DUST.get());
output.accept(ModItems.SODIUM_HYPOCHLORITE_INGOT.get());
output.accept(ModItems.SODIUM_HYPOCHLORITE_DUST.get());
output.accept(ModItems.HYDROGEN_SULFIDE_INGOT.get());
output.accept(ModItems.HYDROGEN_SULFIDE_DUST.get());
output.accept(ModItems.VINYL_CHLORIDE_INGOT.get());
output.accept(ModItems.VINYL_CHLORIDE_DUST.get());
output.accept(ModItems.XYLENE_INGOT.get());
output.accept(ModItems.XYLENE_DUST.get());
output.accept(ModItems.HYDROCHLORIC_ACID_INGOT.get());
output.accept(ModItems.HYDROCHLORIC_ACID_DUST.get());
output.accept(ModItems.NITRIC_ACID_INGOT.get());
output.accept(ModItems.NITRIC_ACID_DUST.get());
output.accept(ModItems.SODIUM_HYDROXIDE_INGOT.get());
output.accept(ModItems.SODIUM_HYDROXIDE_DUST.get());
output.accept(ModItems.DICHLOROMETHANE_INGOT.get());
output.accept(ModItems.DICHLOROMETHANE_DUST.get());
output.accept(ModItems.TRICHLOROETHYLENE_INGOT.get());
output.accept(ModItems.TRICHLOROETHYLENE_DUST.get());
output.accept(ModItems.PERCHLOROETHYLENE_INGOT.get());
output.accept(ModItems.PERCHLOROETHYLENE_DUST.get());
output.accept(ModItems.BROMINE_INGOT.get());
output.accept(ModItems.BROMINE_DUST.get());
output.accept(ModItems.PHOSPHORIC_ACID_INGOT.get());
output.accept(ModItems.PHOSPHORIC_ACID_DUST.get());
output.accept(ModItems.SODIUM_BICARBONATE_INGOT.get());
output.accept(ModItems.SODIUM_BICARBONATE_DUST.get());
output.accept(ModItems.DIMETHYL_SULFOXIDE_INGOT.get());
output.accept(ModItems.DIMETHYL_SULFOXIDE_DUST.get());
output.accept(ModItems.HYDRAZINE_INGOT.get());
output.accept(ModItems.HYDRAZINE_DUST.get());
output.accept(ModItems.HEXAFLUOROPROPYLENE_INGOT.get());
output.accept(ModItems.HEXAFLUOROPROPYLENE_DUST.get());
output.accept(ModItems.TETRAHYDROFURAN_INGOT.get());
output.accept(ModItems.TETRAHYDROFURAN_DUST.get());
output.accept(ModItems.STYRENE_INGOT.get());
output.accept(ModItems.STYRENE_DUST.get());
output.accept(ModItems.PROPYLENE_INGOT.get());
output.accept(ModItems.PROPYLENE_DUST.get());
output.accept(ModItems.ACROLEIN_INGOT.get());
output.accept(ModItems.ACROLEIN_DUST.get());
output.accept(ModItems.TETRACHLOROETHYLENE_INGOT.get());
output.accept(ModItems.TETRACHLOROETHYLENE_DUST.get());
output.accept(ModItems.AQUA_REGIA_INGOT.get());
output.accept(ModItems.AQUA_REGIA_DUST.get());
output.accept(ModItems.CYANOGEN_INGOT.get());
output.accept(ModItems.CYANOGEN_DUST.get());
output.accept(ModItems.FLUOROSILICIC_ACID_INGOT.get());
output.accept(ModItems.FLUOROSILICIC_ACID_DUST.get());
output.accept(ModItems.TITANIUM_TETRACHLORIDE_INGOT.get());
output.accept(ModItems.TITANIUM_TETRACHLORIDE_DUST.get());
output.accept(ModItems.METHYL_ETHYL_KETONE_INGOT.get());
output.accept(ModItems.METHYL_ETHYL_KETONE_DUST.get());
output.accept(ModItems.THIONYL_CHLORIDE_INGOT.get());
output.accept(ModItems.THIONYL_CHLORIDE_DUST.get());
output.accept(ModItems.AZOTH_INGOT.get());
output.accept(ModItems.AZOTH_DUST.get());
output.accept(ModItems.UNOBTANIUM_INGOT.get());
output.accept(ModItems.UNOBTANIUM_DUST.get());
output.accept(ModItems.DILITHIUM_INGOT.get());
output.accept(ModItems.DILITHIUM_DUST.get());
output.accept(ModItems.ADAMANTIUM_INGOT.get());
output.accept(ModItems.ADAMANTIUM_DUST.get());
output.accept(ModItems.CARBONADIUM_INGOT.get());
output.accept(ModItems.CARBONADIUM_DUST.get());
output.accept(ModItems.RADON_INGOT.get());
output.accept(ModItems.RADON_DUST.get());
output.accept(ModItems.NEON_INGOT.get());
output.accept(ModItems.NEON_DUST.get());
output.accept(ModItems.ARGON_INGOT.get());
output.accept(ModItems.ARGON_DUST.get());
output.accept(ModItems.XENON_INGOT.get());
output.accept(ModItems.XENON_DUST.get());
output.accept(ModItems.CRYPTIC_ACID_INGOT.get());
output.accept(ModItems.CRYPTIC_ACID_DUST.get());

                        //#end TAB_REGION
                    }).build());
}
