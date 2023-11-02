package net.purple.pmblock.common.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.purple.pmblock.PMBlock;

import static net.purple.pmblock.common.registry.BlockRegistry.BLOCKS_DATAGEN;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, PMBlock.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        System.out.printf("DEBUG 0: Sout");
        for(RegistryObject<Block> registryObject:BLOCKS_DATAGEN){
            System.out.println("DEBUG 1" + String.valueOf(registryObject.get().getName()));
            blockWithItem(registryObject);
        }
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject){
        simpleBlockWithItem(blockRegistryObject.get(),cubeAll(blockRegistryObject.get()));
    }



}
