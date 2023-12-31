package net.purple.pmblock.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.purple.pmblock.common.block.fluidSolidifier.FluidSolidifierBlockEntity;
import net.purple.pmblock.client.model.FluidSolidifierModel;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;

public class FluidSolidifierRenderer extends ControllerRenderer<FluidSolidifierBlockEntity> {

    public FluidSolidifierRenderer(GeoModel model) {
        super(model);
    }

    public FluidSolidifierRenderer() {
        super(new FluidSolidifierModel());
    }


    // Render is called before defaultRender TODO - Check if true ?


    // These are needed to show the Unassembled Controller, otherwise all Blocks would just show the asssembled. Also rotates the the MB to face the correct side
    @Override
    public void defaultRender(PoseStack poseStack, FluidSolidifierBlockEntity animatable, MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, float yaw, float partialTick, int packedLight) {
        if(animatable.isAssembled()){
            poseStack.pushPose();
            Vec3 offset = new Vec3(0, 0, 1).yRot((float) Math.toRadians(180 - getFacing(animatable).toYRot()));
            poseStack.translate(offset.x, offset.y, offset.z);
            super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedLight);
            renderItem(poseStack, animatable, bufferSource, packedLight);


            // Fluid Solidiier specifics
            FluidStack[] fluidStacks = animatable.getFluids();
            FluidStack fluidStackBase = fluidStacks[0];
            FluidStack fluidStackAccel = fluidStacks[1];
            if(!fluidStackBase.isEmpty()){
                this.renderFluidBase();
            }
            if(!fluidStackAccel.isEmpty()){
                this.renderFluidAccel();
            }


            poseStack.popPose(); // Allways needs to be the end
        } else {
            renderController(poseStack, animatable, bufferSource, packedLight);
        }
    }


    // TODO - Can make this more generic ?

    // FIXME - ADD Implementation
    private void renderFluidAccel() {
    }

    // FIXME - ADD Implementation
    private void renderFluidBase() {
    }

    private void renderItem(PoseStack poseStack, FluidSolidifierBlockEntity animatable, MultiBufferSource bufferSource, int packedLight) {
        ItemStack stack = animatable.getItem(0);
        if(!stack.isEmpty()) {
            float rot = animatable.boneSnapshots.get("rotating_part").getRotY();
            Vec3 v = new Vec3(0.75 * animatable.getSpeedRatio(), 1, 0).yRot(rot);
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            poseStack.pushPose();
            poseStack.translate(0.5, 0, 0.5);
            poseStack.mulPose(Axis.YP.rotation((float) Math.toRadians(180 - getFacing(animatable).toYRot())));
            poseStack.translate(v.x, v.y, v.z);
            itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, animatable.getLevel(), animatable.hashCode());
            poseStack.popPose();
        }
    }




}
