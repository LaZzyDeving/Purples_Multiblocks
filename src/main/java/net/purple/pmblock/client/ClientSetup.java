package net.purple.pmblock.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.purple.pmblock.PMBlock;
import net.purple.pmblock.client.renderer.CobbleGenRenderer;
import net.purple.pmblock.common.registry.BlockEntityRegistry;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

import static net.purple.pmblock.PMBlock.MOD_ID;

public class ClientSetup {

    @Nullable
    private static ShaderInstance rendertypeHologramShader;

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntityRegistry.COBBLE_GEN.get(), pContext -> new CobbleGenRenderer());

    }

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) {
        ResourceProvider provider = event.getResourceProvider();
        try {
            event.registerShader(new ShaderInstance(provider, new ResourceLocation(MOD_ID, "hologram"), DefaultVertexFormat.BLOCK), shaderInstance -> {
                rendertypeHologramShader = shaderInstance;
            });
        } catch (IOException e) {
            PMBlock.LOGGER.warn("Failed to load shader", e);
        }
    }

    public static ShaderInstance getHologramShader() {
        return Objects.requireNonNull(rendertypeHologramShader, "Attempted to call getHologramShader before shaders have finished loading.");
    }
}
