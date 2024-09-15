package net.astr0.astech.recipe;

import net.astr0.astech.AsTech;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, AsTech.MODID);

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, AsTech.MODID);

    public static final RegistryObject<AsTechRecipeType<ChemicalMixerRecipe>> CHEMICAL_MIXER_RECIPE_TYPE = registerType(ModRecipes.CHEMICAL_MIXER, AsTechRecipeType::new);
    public static final RegistryObject<RecipeSerializer<ChemicalMixerRecipe>> CHEMICAL_MIXER_SERIALIZER =
            SERIALIZERS.register(ModRecipes.CHEMICAL_MIXER, ChemicalMixerRecipe.Serializer::getInstance);

    public static final RegistryObject<AsTechRecipeType<AssemblerRecipe>> ASSEMBLER_RECIPE_TYPE = registerType(ModRecipes.ASSEMBLER, AsTechRecipeType::new);
    public static final RegistryObject<RecipeSerializer<AssemblerRecipe>> ASSEMBLER_SERIALIZER =
            SERIALIZERS.register(ModRecipes.ASSEMBLER, AssemblerRecipe.Serializer::getInstance);

    public static final RegistryObject<AsTechRecipeType<ChemicalReactorRecipe>> CHEMICAL_REACTOR_RECIPE_TYPE = registerType(ModRecipes.CHEMICAL_REACTOR, AsTechRecipeType::new);
    public static final RegistryObject<RecipeSerializer<ChemicalReactorRecipe>> CHEMICAL_REACTOR_SERIALIZER =
            SERIALIZERS.register(ModRecipes.CHEMICAL_REACTOR, ChemicalReactorRecipe.Serializer::getInstance);

    public static final RegistryObject<AsTechRecipeType<ElectrolyticSeperatorRecipe>> ELECTROLYTIC_SEPERATOR_RECIPE_TYPE = registerType(ModRecipes.ELECTROLYTIC_SEPERATOR, AsTechRecipeType::new);
    public static final RegistryObject<RecipeSerializer<ElectrolyticSeperatorRecipe>> ELECTROLYTIC_SEPERATOR_SERIALIZER =
            SERIALIZERS.register(ModRecipes.ELECTROLYTIC_SEPERATOR, ElectrolyticSeperatorRecipe.Serializer::getInstance);

    public static final RegistryObject<AsTechRecipeType<PyrolysisChamberRecipe>> PYROLYSIS_CHAMBER_RECIPE_TYPE = registerType(ModRecipes.PYROLYSIS_CHAMBER, AsTechRecipeType::new);
    public static final RegistryObject<RecipeSerializer<PyrolysisChamberRecipe>> PYROLYSIS_CHAMBER_SERIALIZER =
            SERIALIZERS.register(ModRecipes.PYROLYSIS_CHAMBER, PyrolysisChamberRecipe.Serializer::getInstance);

    public static final RegistryObject<RecipeSerializer<GemPolishingRecipe>> GEM_POLISHING_SERIALIZER =
            SERIALIZERS.register("gem_polishing", () -> GemPolishingRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        RECIPE_TYPES.register(eventBus);
    }

    private static <T extends AsTechRecipeType<?>> RegistryObject<T> registerType(String name, Function<String, T> factory) {
        return RECIPE_TYPES.register(name, () -> factory.apply(name));
    }


    // Recipe Types

    public static final String CHEMICAL_MIXER = "chemical_mixer";
    public static final String ASSEMBLER = "assembler";
    public static final String REACTION_CHAMBER = "reaction_chamber";
    public static final String CHEMICAL_REACTOR = "chemical_reactor";
    public static final String ELECTROLYTIC_SEPERATOR = "electrolytic_seperator";
    public static final String PYROLYSIS_CHAMBER = "pyrolysis_chamber";
}
