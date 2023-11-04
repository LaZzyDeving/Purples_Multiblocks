package net.purple.pmblock.common.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.purple.pmblock.PMBlock;
import net.purple.pmblock.common.block.fluidSolidifier.FluidSolidifierBlock;
import net.purple.pmblock.common.pmblocks.Controller_HubBlock;
import net.purple.pmblock.common.block.multiblock.MimicBlock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class BlockRegistry {

    public static final BlockBehaviour.Properties DEFAULT_PROPERTIES = BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK);

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PMBlock.MOD_ID);

    public static final Collection<RegistryObject<Block>> BLOCKS_DATAGEN = new ArrayList<>();

    /******************************************************************
     *
     *  Multiblock Blocks
     *
     ***************************************************************/

    public static final RegistryObject<Block> MIMIC = registerBlockSimple("mimic",
            () -> new MimicBlock(DEFAULT_PROPERTIES));


    public static final RegistryObject<Block> FLUID_SOLIDIFIER = registerBlockSimple("fluid_solidifier",
            ()-> new FluidSolidifierBlock(DEFAULT_PROPERTIES));


    /******************************************************************
     *
     *  Single Blocks
     *
     ***************************************************************/



    // Creates a new Block with the id "examplemod:example_block", combining the namespace and path
    public static final RegistryObject<Block> EXAMPLE_BLOCK = registerBlock("example_block",
            ()-> new Controller_HubBlock(BlockBehaviour.Properties.copy(Blocks.COAL_BLOCK)));






    // Adds Block to list that should be Data Genned
    private static <T extends Block> RegistryObject<T> registerBlockSimple(String name,Supplier<T> block){
        RegistryObject<T> toReturn = registerBlock(name,block);
        BLOCKS_DATAGEN.add((RegistryObject<Block>) toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name,block);
        registerBlockItem(name,toReturn);
        return toReturn;
    }
    // Adding BlockItem
    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block){
        return ItemRegistry.ITEMS.register(name, ()-> new BlockItem(block.get(), new Item.Properties()));
    }

}
