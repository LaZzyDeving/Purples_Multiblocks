package net.purple.pmblock.client.model;

import net.minecraft.resources.ResourceLocation;
import net.purple.pmblock.common.block.fluidSolidifier.FluidSolidifierBlockEntity;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

import static net.purple.pmblock.PMBlock.MOD_ID;

public class FluidSolidifierModel extends DefaultedBlockGeoModel<FluidSolidifierBlockEntity> {

    public FluidSolidifierModel() {
        super(new ResourceLocation(MOD_ID,"fluid_solidifier_mb"));
    }


    // TODO once tested working
    @Override
    public void setCustomAnimations(FluidSolidifierBlockEntity animatable, long instanceId, AnimationState<FluidSolidifierBlockEntity> animationState) {
        /*CoreGeoBone geoBone = getAnimationProcessor().getBone("rotating_part");
        if(!animationState.getController().isPlayingTriggeredAnimation()) {
            geoBone.setRotY(animatable.boneSnapshots.getOrDefault(geoBone.getName(), geoBone.getInitialSnapshot()).getRotY() + (0.02F*animatable.getSpeedRatio()));
            animatable.boneSnapshots.put(geoBone.getName(), geoBone.saveSnapshot());
        } else {
            animatable.boneSnapshots.put(geoBone.getName(), geoBone.getInitialSnapshot());
        }*/
    }
}
