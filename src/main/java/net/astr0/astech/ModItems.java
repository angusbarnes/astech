package net.astr0.astech;

import net.astr0.astech.Fluid.ModFluids;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    // This is the global instance of the items registry
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AsTech.MODID);

    // Call this function from the entry point to allow the items register to link itself to the eventBus
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    // Create a simple crafting ingredient
    private static RegistryObject<Item> SimpleIngredientItem(String name, int stack_size) {
        return ITEMS.register(name, () -> new Item(new Item.Properties().stacksTo(stack_size)));
    }

    public static final RegistryObject<Item> SOAP_WATER_BUCKET = ITEMS.register("soap_water_bucket",
            () -> new BucketItem(ModFluids.SOURCE_SOAP_WATER,
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final RegistryObject<Item> DEEZ_NUTS_ITEM = SimpleIngredientItem("deez_nuts", 16);
    public static final RegistryObject<Item> DEEZ_BUTTS_ITEM = SimpleIngredientItem("deez_butts", 32);
}