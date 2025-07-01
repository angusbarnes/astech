package net.astr0.astech;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundRegistry {

    public static final DeferredRegister<SoundEvent> SOUND_REG = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, AsTech.MODID);

    public static final RegistryObject<SoundEvent> help_of = SOUND_REG.register("help_of", () -> makeSound("help_of"));
    public static final RegistryObject<SoundEvent> as_an_llm = SOUND_REG.register("as_an_llm", () -> makeSound("as_an_llm"));
    public static final RegistryObject<SoundEvent> run_nic_run_disc = SOUND_REG.register("run_nic_run_disc", () -> makeSound("run_nic_run_disc"));
    public static final RegistryObject<SoundEvent> stolen_identity = SOUND_REG.register("stolen_identity", () -> makeSound("stolen_identity"));
    public static final RegistryObject<SoundEvent> bangarang = SOUND_REG.register("bangarang", () -> makeSound("bangarang"));
    public static final RegistryObject<SoundEvent> laser_machine = SOUND_REG.register("laser_machine", () -> makeSound("laser_machine"));
    public static final RegistryObject<SoundEvent> generic_machine = SOUND_REG.register("generic_machine", () -> makeSound("generic_machine"));
    public static final RegistryObject<SoundEvent> wet_machine = SOUND_REG.register("wet_machine", () -> makeSound("wet_machine"));
    public static final RegistryObject<SoundEvent> electric_machine = SOUND_REG.register("electric_machine", () -> makeSound("electric_machine"));
    public static final RegistryObject<SoundEvent> final_song = SOUND_REG.register("nevergonna", () -> makeSound("nevergonna"));
    public static final RegistryObject<SoundEvent> beep = SOUND_REG.register("beep", () -> makeSound("beep"));
    public static final RegistryObject<SoundEvent> airhorn = SOUND_REG.register("airhorn", () -> makeSound("airhorn"));


    public static void register(IEventBus eventBus) {
        SOUND_REG.register(eventBus);
    }


    static SoundEvent makeSound(String name) {
        return SoundEvent.createVariableRangeEvent(new ResourceLocation(AsTech.MODID, name));
    }
}
