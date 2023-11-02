package net.purple.pmblock.common.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.purple.pmblock.PMBlock;
import net.purple.pmblock.common.block.cobbleGen.CobbleGenBlockEntity;
import net.purple.pmblock.common.block.multiblock.MimicBlockEntity;


public class BlockEntityRegistry {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, PMBlock.MOD_ID);


    /******************************************************************
     *
     *  Multiblock Blockentity
     *
     ***************************************************************/

    public static final RegistryObject<BlockEntityType<MimicBlockEntity>> MIMIC = BLOCK_ENTITIES.register("mimic",
            () -> BlockEntityType.Builder.of(MimicBlockEntity::new, BlockRegistry.MIMIC.get()).build(null));


    public static final RegistryObject<BlockEntityType<CobbleGenBlockEntity>> COBBLE_GEN = BLOCK_ENTITIES.register("cobble_gen",
            ()-> BlockEntityType.Builder.of(CobbleGenBlockEntity::new, BlockRegistry.COBBLE_GEN.get()).build(null));



    /******************************************************************
     *
     *  Single Blockentity
     *
     ***************************************************************/





}
