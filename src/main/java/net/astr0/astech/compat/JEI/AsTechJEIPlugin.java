package net.astr0.astech.compat.JEI;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.registration.*;
import net.astr0.astech.AsTech;
import net.astr0.astech.Fluid.ModFluids;
import net.astr0.astech.block.AdvancedAssembler.AdvancedAssemblerScreen;
import net.astr0.astech.block.Assembler.AssemblerScreen;
import net.astr0.astech.block.ChemicalMixer.ChemicalMixerScreen;
import net.astr0.astech.block.EUVMachine.EUVMachineScreen;
import net.astr0.astech.block.ElectrolyticSeperator.ElectrolyticSeperatorScreen;
import net.astr0.astech.block.ModBlocks;
import net.astr0.astech.block.PyrolysisChamber.PyrolysisChamberScreen;
import net.astr0.astech.block.ReactionChamber.ChemicalReactorScreen;
import net.astr0.astech.recipe.*;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

@JeiPlugin
public class AsTechJEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(AsTech.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ChemicalMixerCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new AssemblerCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ChemicalReactorCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ElectrolyticSeperatorCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new PyrolysisChamberCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new EUVMachineCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<ChemicalMixerRecipe> polishingRecipes = recipeManager
                .getAllRecipesFor(ModRecipes.CHEMICAL_MIXER_RECIPE_TYPE.get());
        registration.addRecipes(ChemicalMixerCategory.CHEMICAL_MIXER_TYPE, polishingRecipes);

        List<AssemblerRecipe> assemblyRecipes = recipeManager
                .getAllRecipesFor(ModRecipes.ASSEMBLER_RECIPE_TYPE.get());
        registration.addRecipes(AssemblerCategory.ASSEMBLER_TYPE, assemblyRecipes);

        List<ChemicalReactorRecipe> reactorRecipes = recipeManager
                .getAllRecipesFor(ModRecipes.CHEMICAL_REACTOR_RECIPE_TYPE.get());
        registration.addRecipes(ChemicalReactorCategory.REACTOR_TYPE, reactorRecipes);

        List<ElectrolyticSeperatorRecipe> seperatorRecipes = recipeManager
                .getAllRecipesFor(ModRecipes.ELECTROLYTIC_SEPERATOR_RECIPE_TYPE.get());
        registration.addRecipes(ElectrolyticSeperatorCategory.SEPERATOR_TYPE, seperatorRecipes);

        List<PyrolysisChamberRecipe> pyroRecipes = recipeManager
                .getAllRecipesFor(ModRecipes.PYROLYSIS_CHAMBER_RECIPE_TYPE.get());
        registration.addRecipes(PyrolysisChamberCategory.PYROLYSIS_CHAMBER_TYPE, pyroRecipes);

        List<EUVMachineRecipe> euvRecipes = recipeManager
                .getAllRecipesFor(ModRecipes.EUV_MACHINE_RECIPE_TYPE.get());
        registration.addRecipes(EUVMachineCategory.EUV_MACHINE_TYPE, euvRecipes);

        registration.addIngredientInfo(new ItemStack(ModBlocks.STIPNICIUM_ORE.get().asItem()), VanillaTypes.ITEM_STACK, Component.literal("Can be found underground on mars in small veins"));
        registration.addIngredientInfo(new ItemStack(ModBlocks.BROMINE_ORE.get().asItem()), VanillaTypes.ITEM_STACK, Component.literal("Can be found underground on mars in small veins"));
        registration.addIngredientInfo(new ItemStack(ModBlocks.COBALT_ORE.get().asItem()), VanillaTypes.ITEM_STACK, Component.literal("Can be found underground on mars in small veins"));
        //#anchor INFO_REGION
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_ACETIC_ACID.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("The main component of vinegar apart from water."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_ACETONE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A common solvent used in nail polish remover."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_ACROLEIN.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used in the production of acrylic acid and its esters."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_ADAMANTIUM.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A fictional virtually indestructible metal alloy from the Marvel universe."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_ALUMINIUM_HYDROXIDE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used as an antacid and in the manufacture of aluminum compounds."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_AMMONIA.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used in fertilizers and as a refrigerant gas. Environmental toxicity; can be explosive but kinda rarely"));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_AQUA_REGIA.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A mixture of nitric acid and hydrochloric acid capable of dissolving gold."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_BENZENE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("An important industrial solvent and precursor in the production of various chemicals. Cat 1 carcinogen"));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_BROMINE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used in flame retardants and some types of medication."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_CARBONADIUM.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A malleable and resilient fictional alloy used in various sci-fi contexts."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_CHLORINE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used as a disinfectant and in the production of PVC."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_CRYPTIC_ACID.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Hmm, what is this strange substrance"));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_CYANOGEN.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A highly toxic gas used in chemical synthesis and fumigation."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_DICHLOROMETHANE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A solvent used in paint removers and degreasing agents."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_DILITHIUM.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A fictional element used as a power source in the Star Trek universe."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_DIMETHYL_SULFOXIDE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A solvent with the ability to penetrate biological membranes."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_ETHANE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A component of natural gas used as a feedstock for ethylene production."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_ETHYLENE_GLYCOL.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used as antifreeze in cooling and heating systems."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_FLUOROSILICIC_ACID.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used in water fluoridation and in the production of ceramics."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_FORMALDEHYDE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used in the production of resins and as a preservative."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_HEXAFLUOROPROPYLENE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used in the production of fluoropolymers."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_HEXANE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used as a solvent in the extraction of vegetable oils."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_HYDRAZINE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used as a rocket propellant and in fuel cells."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_HYDROCHLORIC_ACID.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A strong acid used in many industrial processes including metal refining."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_HYDROGEN.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("The lightest and most abundant chemical element in the universe."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_HYDROGEN_SULFIDE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A toxic gas with a characteristic smell of rotten eggs."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_ISOPROPANOL.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Commonly known as rubbing alcohol used as a disinfectant."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_METHANOL.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used as a solvent antifreeze and fuel."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_METHYL_CHLORIDE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used as a refrigerant and in the production of silicone polymers."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_METHYL_ETHYL_KETONE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A solvent used in the production of plastics and textiles."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_NEON.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A noble gas used in neon signs and high-voltage indicators."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_NITROGEN.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Makes up 78% of the Earth's atmosphere and is used in the production of ammonia."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_PHOSGENE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A highly toxic gas used in the production of plastics and pesticides."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_PHOSPHORIC_ACID.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used in fertilizers, food flavoring, and rust removal."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_PIRANHA.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Fully hectic acid; used similarly to aqua regia and HF"));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_PISS_WATER.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A slang term for urine which is primarily water with dissolved waste products."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_POLYTETRAFLUOROETHYLENE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A polymer commonly used in non-stick coatings for cookware."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_POLYVINYL_CHLORIDE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("The world's third-most widely produced synthetic polymer of plastic."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_PROPYLENE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A key raw material in the production of polypropylene plastics."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_RADON.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A radioactive noble gas used in some types of cancer treatment."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_SODIUM_HYDROXIDE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A strong base used in soap making and chemical manufacturing."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_SODIUM_HYPOCHLORITE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used as a bleaching agent and disinfectant."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_STYRENE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used in the production of polystyrene plastics and resins."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_SULFURIC_ACID.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A highly corrosive acid used in industrial processes and battery acid."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_TETRACHLOROETHYLENE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used as a solvent in dry cleaning and metal degreasing."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_TETRAHYDROFURAN.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A solvent used in the production of polymers."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_TRICHLOROETHYLENE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used as a solvent and in the manufacturing of hydrofluorocarbon refrigerants."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_TRICHLOROMETHANE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("This be chloroform"));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_UNOBTANIUM.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A rare and highly valuable fictional material with exceptional properties."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_XENON.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A noble gas used in high-intensity lamps and ion propulsion systems in spacecraft."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_XYLENE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used as a solvent in the printing, rubber and leather industries."));
        //#end INFO_REGION
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CHEMICAL_MIXER.get()),
                ChemicalMixerCategory.CHEMICAL_MIXER_TYPE);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ASSEMBLER.get()),
                AssemblerCategory.ASSEMBLER_TYPE);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ADVANCED_ASSEMBLER.get()),
                AssemblerCategory.ASSEMBLER_TYPE);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CHEMICAL_REACTOR.get()),
                ChemicalReactorCategory.REACTOR_TYPE);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ELECTROLYTIC_SEPERATOR.get()),
                ElectrolyticSeperatorCategory.SEPERATOR_TYPE);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.PYROLYSIS_CHAMBER.get()),
                PyrolysisChamberCategory.PYROLYSIS_CHAMBER_TYPE);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.EUV_MACHINE.get()),
                EUVMachineCategory.EUV_MACHINE_TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ChemicalMixerScreen.class, 85, 30, 20, 30,
                ChemicalMixerCategory.CHEMICAL_MIXER_TYPE);

        registration.addRecipeClickArea(AssemblerScreen.class, 74, 48, 28, 8,
                AssemblerCategory.ASSEMBLER_TYPE);

        registration.addRecipeClickArea(AdvancedAssemblerScreen.class, 74, 48, 28, 8,
                AssemblerCategory.ASSEMBLER_TYPE);

        registration.addRecipeClickArea(ChemicalReactorScreen.class, 74, 40, 28, 8,
                ChemicalReactorCategory.REACTOR_TYPE);

        registration.addRecipeClickArea(ElectrolyticSeperatorScreen.class, 69, 40, 28, 8,
                ElectrolyticSeperatorCategory.SEPERATOR_TYPE);

        registration.addRecipeClickArea(PyrolysisChamberScreen.class, 69, 40, 28, 8,
                PyrolysisChamberCategory.PYROLYSIS_CHAMBER_TYPE);

        registration.addRecipeClickArea(EUVMachineScreen.class, 75, 40, 28, 8,
                EUVMachineCategory.EUV_MACHINE_TYPE);
    }

    public static IRecipeSlotTooltipCallback defaultOutputTooltip(FluidIngredient fluid) {

        return (recipeSlotView, tooltip) -> {
            TagKey<Fluid> tag = fluid.getFluidTagKey();
            if(tag != null) {
                tooltip.add(Component.literal(String.format("§7Accepts any %s", tag.location().toString())));
            }
        };
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
//        registration.addRecipeTransferHandler(
//                AssemblerMenu.class,
//                ModMenuTypes.ASSEMBLER_MENU.get(),
//                AssemblerCategory.ASSEMBLER_TYPE,
//                AssemblerMenu.TE_INVENTORY_FIRST_SLOT_INDEX, 5,
//                AssemblerMenu.VANILLA_FIRST_SLOT_INDEX,
//                AssemblerMenu.VANILLA_SLOT_COUNT
//        );
    }
}