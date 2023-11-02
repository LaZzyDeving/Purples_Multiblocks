package net.purple.pmblock.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static net.purple.pmblock.PMBlock.MOD_ID;

public class Registry {
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace







    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    // Actual Creative Mode Tab
    public static final RegistryObject<CreativeModeTab> MOD_CREATIVE_TAB = CREATIVE_MODE_TABS.register("pmblock_tab",
            () -> CreativeModeTab.builder().icon(()-> new ItemStack(ItemRegistry.EXAMPLE_ITEM.get()))
                    .title(Component.translatable("creativetab.pbmblock_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(BlockRegistry.EXAMPLE_BLOCK.get());
                        ItemRegistry.ITEMS.getEntries().forEach(itemRegistryObject -> pOutput.accept(itemRegistryObject.get()));
                        //BlockRegistry.BLOCKS.getEntries().forEach(itemRegistryObject -> pOutput.accept(itemRegistryObject.get()));

                    })
                    .build());

    public static void registerDeferredRegisters(IEventBus modEventBus){
        // Register the Deferred Register to the mod event bus so blocks get registered
        BlockRegistry.BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ItemRegistry.ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);
        // Register Block Entities to the mod even bus
        BlockEntityRegistry.BLOCK_ENTITIES.register(modEventBus);

    }

    // Add the example block item to the building blocks tab
    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey()==CreativeModeTabs.INGREDIENTS){
            event.accept(ItemRegistry.EXAMPLE_ITEM);
        }
    }


    // Creates a creative tab with the id "examplemod:example_tab" for the example item, that is placed after the combat tab

    /*
    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());

    */



}
