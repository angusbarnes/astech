package net.astr0.astrocraft.compat.JEI;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.registration.*;
import net.astr0.astrocraft.Astrocraft;
import net.astr0.astrocraft.Fluid.ModFluids;
import net.astr0.astrocraft.block.AdvancedAssembler.AdvancedAssemblerScreen;
import net.astr0.astrocraft.block.Assembler.AssemblerMenu;
import net.astr0.astrocraft.block.Assembler.AssemblerScreen;
import net.astr0.astrocraft.block.ChemicalMixer.ChemicalMixerScreen;
import net.astr0.astrocraft.block.EUVMachine.EUVMachineScreen;
import net.astr0.astrocraft.block.ElectrolyticSeperator.ElectrolyticSeperatorScreen;
import net.astr0.astrocraft.block.ModBlocks;
import net.astr0.astrocraft.block.PyrolysisChamber.PyrolysisChamberScreen;
import net.astr0.astrocraft.block.ReactionChamber.ChemicalReactorScreen;
import net.astr0.astrocraft.gui.AsTechGuiScreen;
import net.astr0.astrocraft.gui.ModMenuTypes;
import net.astr0.astrocraft.item.ModItems;
import net.astr0.astrocraft.recipe.*;
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
public class AstrocraftJEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Astrocraft.MODID, "jei_plugin");
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
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.useNbtForSubtypes(ModItems.FLUID_CELL.get());
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
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_ACETONE.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal("A common solvent used in nail polish remover."),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict EXPLOSION. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_ACROLEIN.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used in the production of acrylic acid and its esters."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_ADAMANTIUM.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A fictional virtually indestructible metal alloy from the Marvel universe."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_AEROGEL.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A weird half air half gel material which is extremely flame resistant"));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_ALUMINIUM_HYDROXIDE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used as an antacid and in the manufacture of aluminum compounds."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_AMMONIA.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used in fertilizers and as a refrigerant gas. Environmental toxicity; can be explosive but kinda rarely"));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_AMMONIUM_CHLORIDE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal(""));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_ANTIMONY.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Rare earth metal which is useful at enhancing the hardness of alloys"));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_AQUA_REGIA.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal("A mixture of nitric acid and hydrochloric acid capable of dissolving gold."),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict EXPLOSION. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_BENZENE.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal("An important industrial solvent and precursor in the production of various chemicals. Cat 1 carcinogen"),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict EXTREME. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_BIOLUMINESCENT_COON_JUICE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal(""));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_BROMINE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used in flame retardants and some types of medication."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_CARBONADIUM.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A malleable and resilient fictional alloy used in various sci-fi contexts."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_CHLORINE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used as a disinfectant and in the production of PVC."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_CHLOROBENZENE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A common solvent used in the production of a large number of industrial compounds"));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_CHLORODIFLUOROMETHANE.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal("A hydrocarbon refridgerant and propellant"),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict EXPLOSION. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_COBALT.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal(""));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_CRYPTIC_ACID.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Hmm, what is this strange substrance"));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_CYANOGEN.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal("A highly toxic gas used in chemical synthesis and fumigation."),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict SUFFOCATE. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_DICHLOROMETHANE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A solvent used in paint removers and degreasing agents."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_DILITHIUM.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal("A fictional element used as a power source in the Star Trek universe."),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict FREEZE. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_DIMETHYL_SULFOXIDE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A solvent with the ability to penetrate biological membranes."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_ETHANE.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal("A component of natural gas used as a feedstock for ethylene production."),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict EXPLOSION. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_ETHYLENE_GLYCOL.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used as antifreeze in cooling and heating systems."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_FENTANYL.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("THIS SHIT GETS YOU HYPE LETS GOOOOOO"));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_FLUOROANTIMONIC_ACID.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal("One of the strongest known acids, classified as a super acid. This will burn your soul out of your body."),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict EXTREME. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_FLUOROSILICIC_ACID.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used in water fluoridation and in the production of ceramics."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_FORMALDEHYDE.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal("Used in the production of resins and as a preservative."),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict EXTREME. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_GELID_CRYOTHEUM.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal("An insane refridgerant with non-newtonian characteristics"),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict FREEZE. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_GHASTLY_LIQUID.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal("A hellish sludge which is comprised of the worst co-analytes of netherite, suspended in an aqueous solution"),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict SUFFOCATE. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_GRAPHENE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal(""));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_HELIUM.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Voice go squeeeeeeaaakyyyyy"));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_HEXAFLUOROPROPYLENE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used in the production of fluoropolymers."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_HEXANE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used as a solvent in the extraction of vegetable oils."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_HIGH_ENTROPY_ALLOY.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal("This is an incredibly strong metal alloy. Despite being critically unstable on its own, controlled industrial processes and additives can be used to subdue its explosive tendancies"),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict EXPLOSION. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_HYDRAZINE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used as a rocket propellant and in fuel cells."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_HYDROCHLORIC_ACID.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A strong acid used in many industrial processes including metal refining."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_HYDROGEN_PEROXIDE.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal("Bleach your hair or something"),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict EXTREME. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_HYDROGEN.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal("The lightest and most abundant chemical element in the universe."),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict EXPLOSION. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_HYDROGEN_FLUORIDE.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal("A basic bitch acid which high acidity"),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict HEAT. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_HYDROGEN_SULFIDE.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal("A toxic gas with a characteristic smell of rotten eggs."),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict SUFFOCATE. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_ISOPROPANOL.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Commonly known as rubbing alcohol used as a disinfectant."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_IUMIUM.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal("A high pothetical element which was named after scientists ran out of all the other -ium names"),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict RADIO. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_METHANOL.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used as a solvent antifreeze and fuel."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_METHYL_CHLORIDE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used as a refrigerant and in the production of silicone polymers."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_METHYL_ETHYL_KETONE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A solvent used in the production of plastics and textiles."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_NEON.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A noble gas used in neon signs and high-voltage indicators."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_NETHERITE.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal(""),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict HEAT. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_NITROGEN.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Makes up 78% of the Earth's atmosphere and is used in the production of ammonia."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_PHOSGENE.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal("A highly toxic gas used in the production of plastics and pesticides."),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict SUFFOCATE. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_PHOSPHORIC_ACID.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used in fertilizers, food flavoring, and rust removal."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_PIRANHA.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal("Fully hectic acid; used similarly to aqua regia and HF"),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict EXTREME. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_PISS_WATER.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A slang term for urine which is primarily water with dissolved waste products."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_POLYTETRAFLUOROETHYLENE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A polymer commonly used in non-stick coatings for cookware."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_POLYVINYL_CHLORIDE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("The world's third-most widely produced synthetic polymer of plastic."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_PROPYLENE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A key raw material in the production of polypropylene plastics."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_RADON.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal("A radioactive noble gas used in some types of cancer treatment."),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict RADIO. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_ROCKET_FUEL.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Does what it says on the can"));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_SILICOALUMINOPHOSPHATE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A micro pore zeolite used in the production of advanced chemical catalysts"));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_SODIUM.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal(""),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict EXPLOSION. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_SODIUM_HYDROXIDE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A strong base used in soap making and chemical manufacturing."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_SODIUM_HYPOCHLORITE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used as a bleaching agent and disinfectant."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_SODIUM_SULFATE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Sodium sulfate is a white, crystalline powder, also known as Glauber's salt, and is the sodium salt of sulfuric acid. It's highly soluble in water and has various applications, including use as a laxative, a drying agent in laboratories, and in the production of detergents, glass, and textiles."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_SOLDERING_FLUX.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal(""));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_STIPNICIUM.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("STIP NIC is a long since forgotten meme that only Astr0 cares about anymore. He turned it into a metal to commemorate"));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_STYRENE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used in the production of polystyrene plastics and resins."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_STYRENE_BUTADIENE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Styrene-butadiene rubber (SBR) is a widely used synthetic rubber produced by copolymerizing styrene and butadiene. It's known for its good abrasion resistance, aging stability, and various other properties that make it suitable for different applications."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_SULFURIC_ACID.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A highly corrosive acid used in industrial processes and battery acid."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_TETRACHLOROETHYLENE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Used as a solvent in dry cleaning and metal degreasing."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_TETRAETHYL_ORTHOSILICATE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A precursor for synthesizing various silica-based materials, including silicon dioxide, silicon oxycarbides, silanol, siloxane polymers, and organosilicon thin films"));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_TETRAHYDROFURAN.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A solvent used in the production of polymers."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_TRICHLOROETHYLENE.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal("Used as a solvent and in the manufacturing of hydrofluorocarbon refrigerants."),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict EXTREME. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_TRICHLOROMETHANE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("This be chloroform"));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_UNOBTANIUM.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal("A rare and highly valuable fictional material with exceptional properties."),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict RADIO. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_CATALYSED_URANIUM.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal(""));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_XENON.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("A noble gas used in high-intensity lamps and ion propulsion systems in spacecraft."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_XYLENE.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal("Used as a solvent in the printing, rubber and leather industries."),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict FREEZE. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_RP_1.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("RP-1 is a highly refined kerosene used as rocket fuel, particularly in liquid-fueled rocket engines. It's a kerolox fuel, meaning it's used with liquid oxygen."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_ANTIMONY_PENTAFLUORIDE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal(""));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_POLYMETHYL_METHACRYLATE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal("Polymethyl methacrylate (PMMA), also known as acrylic or Plexiglas, is a synthetic resin derived from methyl methacrylate."));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_GASEOUS_HYRDOCARBONS.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal(""));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_ACETYLENE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal(""));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_PHOSPHORUS.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal(""));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_NATURAL_GAS.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal(""));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_SILICON_TETRACHLORIDE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal(""));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_ENERGISED_NAQADAH.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal(""));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_MONOCRYSTALINE_SILICON.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal(""),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict SUFFOCATE. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_REFINED_SILICON.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal(""));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_TREATED_BIODIESEL.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal(""));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_POTASSIUM_DICHROMATE.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal(""),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict SUFFOCATE. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_HIGH_CARBON_STEEL_52100.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal(""));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_ENGINEERED_ALLOY.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal(""));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_GOOD_BEER.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal(""));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_FOSTERS_BEER.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal("Foster's Lager is an internationally distributed brand of Australian lager. It is owned by the Japanese brewing group Asahi Group Holdings. It's also dogshit."),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict SUFFOCATE. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_PH_STABLISED_BESKAR.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal(""));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_MODIFIED_PHOSPHINIC_ACID.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal(""));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_PHOSPHINIC_ACID.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal(""));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_BESKAR_HYDROXIDE.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal(""));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_SUPERHEATED_BESKAR_SLURRY.get().getSource(), 1000), 
            ForgeTypes.FLUID_STACK, 
            Component.literal(""),
            Component.literal("\nThis chemical is §c§nHazardous§r and may inflict HEAT. §6Chemical protection§r§0 is recommended.")
            );
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_BESKAR.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal(""));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_SEA_WATER.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal(""));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_ICE_SLURRY.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal(""));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_PH_BALANCED_PURIFIED_WATER.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal(""));
registration.addIngredientInfo(new FluidStack(ModFluids.FLOWING_PURIFIED_WATER.get().getSource(), 1000), ForgeTypes.FLUID_STACK, Component.literal(""));
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


        registration.addGhostIngredientHandler(AsTechGuiScreen.class, new GhostIngredientHandler());
    }

    //TODO: Finish cleaning this up
    public static IRecipeSlotTooltipCallback defaultOutputTooltip(FluidIngredient fluid) {

        return (recipeSlotView, tooltip) -> {
            TagKey<Fluid> tag = fluid.getFluidTagKey();
//            if(tag != null) {
//            }
        };
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(
                AssemblerMenu.class,
                ModMenuTypes.ASSEMBLER_MENU.get(),
                AssemblerCategory.ASSEMBLER_TYPE,
                AssemblerMenu.TE_INVENTORY_FIRST_SLOT_INDEX, 5,
                AssemblerMenu.VANILLA_FIRST_SLOT_INDEX,
                AssemblerMenu.VANILLA_SLOT_COUNT
        );
    }
}