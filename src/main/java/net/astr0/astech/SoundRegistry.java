package net.astr0.astech;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundRegistry {

    public static final DeferredRegister<SoundEvent> SOUND_REG = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, AsTech.MODID);

    public static RegistryObject<SoundEvent> help_of = SOUND_REG.register("help_of", () -> makeSound("help_of"));
    public static RegistryObject<SoundEvent> as_an_llm = SOUND_REG.register("as_an_llm", () -> makeSound("as_an_llm"));
    public static RegistryObject<SoundEvent> run_nic_run_disc = SOUND_REG.register("run_nic_run_disc", () -> makeSound("run_nic_run_disc"));
    public static RegistryObject<SoundEvent> stolen_identity = SOUND_REG.register("stolen_identity", () -> makeSound("stolen_identity"));
    public static RegistryObject<SoundEvent> bangarang = SOUND_REG.register("bangarang", () -> makeSound("bangarang"));

    public static void register(IEventBus eventBus) {
        SOUND_REG.register(eventBus);
    }


    static SoundEvent makeSound(String name) {
        return SoundEvent.createVariableRangeEvent(new ResourceLocation(AsTech.MODID, name));
    }
}
