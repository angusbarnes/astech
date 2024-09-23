package net.astr0.astech.gui;

import net.astr0.astech.AsTech;
import net.astr0.astech.block.AdvancedAssembler.AdvancedAssemblerMenu;
import net.astr0.astech.block.Assembler.AssemblerMenu;
import net.astr0.astech.block.ChemicalMixer.ChemicalMixerMenu;
import net.astr0.astech.block.EUVMachine.EUVMachineMenu;
import net.astr0.astech.block.ElectrolyticSeperator.ElectrolyticSeperatorMenu;
import net.astr0.astech.block.GemPolisher.GemPolishingStationMenu;
import net.astr0.astech.block.PyrolysisChamber.PyrolysisChamberMenu;
import net.astr0.astech.block.ReactionChamber.ChemicalReactorMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, AsTech.MODID);

    public static final RegistryObject<MenuType<GemPolishingStationMenu>> GEM_POLISHING_MENU =
            registerMenuType("gem_polishing_menu", GemPolishingStationMenu::new);

    public static final RegistryObject<MenuType<ChemicalMixerMenu>> CHEMICAL_MIXER_MENU =
            registerMenuType("chemical_mixer_menu", ChemicalMixerMenu::new);

    public static final RegistryObject<MenuType<AssemblerMenu>> ASSEMBLER_MENU =
            registerMenuType("assembler_menu", AssemblerMenu::new);

    public static final RegistryObject<MenuType<AdvancedAssemblerMenu>> ADVANCED_ASSEMBLER_MENU =
            registerMenuType("advanced_assembler_menu", AdvancedAssemblerMenu::new);

    public static final RegistryObject<MenuType<ChemicalReactorMenu>> CHEMICAL_REACTOR_MENU =
            registerMenuType("chemical_reactor_menu", ChemicalReactorMenu::new);

    public static final RegistryObject<MenuType<ElectrolyticSeperatorMenu>> ELECTROLYTIC_SEPERATOR_MENU =
            registerMenuType("electrolytic_seperator_menu", ElectrolyticSeperatorMenu::new);

    public static final RegistryObject<MenuType<PyrolysisChamberMenu>> PYROLYSIS_CHAMBER_MENU =
            registerMenuType("pyrolysis_chamber_menu", PyrolysisChamberMenu::new);

    public static final RegistryObject<MenuType<EUVMachineMenu>> EUV_MACHINE_MENU =
            registerMenuType("euv_machine_menu", EUVMachineMenu::new);

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}