package net.purple.pmblock.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.purple.pmblock.client.renderer.PMRenderTypes;
import net.purple.pmblock.common.block.multiblock.ControllerBlockEntity;
import net.purple.pmblock.common.registry.BlockRegistry;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public abstract class ControllerRenderer<T extends ControllerBlockEntity> extends GeoBlockRenderer<T> {

    protected ControllerRenderer(GeoModel<T> model) {
        super(model);
    }

    protected void renderController(PoseStack poseStack, T animatable, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        BlockState state = Blocks.IRON_BLOCK.defaultBlockState(); // TODO - Change for real Block. Tested > works with Iron Block
        poseStack.translate(0.5, 0, 0.5);
        poseStack.mulPose(Axis.YP.rotation((float) Math.toRadians(180 - getFacing(animatable).toYRot())));
        poseStack.translate(-0.5, 0, -0.5);
        blockRenderer.getModelRenderer().tesselateBlock(animatable.getLevel(), blockRenderer.getBlockModel(state), state, animatable.getBlockPos(), poseStack, bufferSource.getBuffer(RenderType.cutoutMipped()), false, RandomSource.create(), state.getSeed(animatable.getBlockPos()), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.cutoutMipped());
        poseStack.popPose();

        renderGuide(poseStack, animatable, bufferSource, packedLight);
    }

    protected void renderGuide(PoseStack poseStack, T animatable, MultiBufferSource bufferSource, int packedLight) {
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        poseStack.pushPose();
        float rot = (float) Math.toRadians(180 - getFacing(animatable).toYRot());

        poseStack.translate(0.5, 0, 0.5);
        poseStack.mulPose(Axis.YP.rotation(rot));
        poseStack.translate(-0.5, 0, -0.5);

        Vec3i v = ControllerBlockEntity.getControllerOffset(animatable.getBlockPattern());
        poseStack.translate(v.getX(), v.getY(), v.getZ());
        for (int i = 0; i < animatable.getBlockPattern().length; i++) {
            for (int j = 0; j < animatable.getBlockPattern()[0].length; j++) {
                for (int k = 0; k < animatable.getBlockPattern()[0][0].length; k++) {
                    BlockState state = animatable.getBlockPattern()[i][j][k];
                    if(state != null && !state.isAir()) {
                        poseStack.pushPose();
                        poseStack.translate(i, j, k);
                        poseStack.scale(0.8F, 0.8F, 0.8F);
                        poseStack.translate(0.1F, 0.1F, 0.1F);
                        RenderType renderType = PMRenderTypes.hologram();
                        blockRenderer.renderSingleBlock(state, poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType);
                        poseStack.popPose();
                    }

                }
            }
        }
        poseStack.popPose();
    }

}
