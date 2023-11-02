package net.purple.pmblock.client.model;

import net.minecraft.resources.ResourceLocation;
import net.purple.pmblock.common.block.cobbleGen.CobbleGenBlockEntity;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

import static net.purple.pmblock.PMBlock.MOD_ID;

public class CobbleGenModel extends DefaultedBlockGeoModel<CobbleGenBlockEntity> {

    public CobbleGenModel() {
        super(new ResourceLocation(MOD_ID,"cobble_gen_mb"));
    }


    // TODO once tested working
    @Override
    public void setCustomAnimations(CobbleGenBlockEntity animatable, long instanceId, AnimationState<CobbleGenBlockEntity> animationState) {
        /*CoreGeoBone geoBone = getAnimationProcessor().getBone("rotating_part");
        if(!animationState.getController().isPlayingTriggeredAnimation()) {
            geoBone.setRotY(animatable.boneSnapshots.getOrDefault(geoBone.getName(), geoBone.getInitialSnapshot()).getRotY() + (0.02F*animatable.getSpeedRatio()));
            animatable.boneSnapshots.put(geoBone.getName(), geoBone.saveSnapshot());
        } else {
            animatable.boneSnapshots.put(geoBone.getName(), geoBone.getInitialSnapshot());
        }*/
    }
}
