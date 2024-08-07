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
}
