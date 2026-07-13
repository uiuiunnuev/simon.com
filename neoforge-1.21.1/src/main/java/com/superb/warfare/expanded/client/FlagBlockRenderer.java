package com.superb.warfare.expanded.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.superb.warfare.expanded.block.FlagBlock;
import com.superb.warfare.expanded.blockentity.FlagBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

import java.util.UUID;

public class FlagBlockRenderer implements BlockEntityRenderer<FlagBlockEntity> {
    private static final ResourceLocation PIRATE_TEXTURE = ResourceLocation.parse("superb_warfare_expanded:textures/block/pirate_flag.png");

    public FlagBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(FlagBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        poseStack.translate(0.5D, 0.5D, 0.5D);

        boolean conquered = false;
        if (blockEntity.getLevel() != null) {
            conquered = blockEntity.getBlockState().getValue(FlagBlock.CONQUERED);
        }

        ResourceLocation texture = PIRATE_TEXTURE;
        if (conquered) {
            UUID uuid = blockEntity.getConquerorUUID() != null ? blockEntity.getConquerorUUID() : UUID.randomUUID();
            try {
                texture = Minecraft.getInstance().getSkinManager()
                        .getInsecureSkin(new com.mojang.authlib.GameProfile(uuid, blockEntity.getConquerorName()))
                        .texture();
            } catch (Exception e) {
                texture = DefaultPlayerSkin.get(uuid).texture();
            }
        }

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(texture));

        poseStack.mulPose(Axis.YP.rotationDegrees(0));

        Matrix4f matrix = poseStack.last().pose();

        float minU = 0.0F;
        float maxU = 1.0F;
        float minV = 0.0F;
        float maxV = 1.0F;

        if (conquered) {
            minU = 0.125F;
            maxU = 0.25F;
            minV = 0.125F;
            maxV = 0.25F;
        }

        drawQuad(consumer, matrix, -0.4F, -0.4F, 0.4F, 0.4F, 0.0F, minU, maxU, minV, maxV, packedLight);

        poseStack.popPose();
    }

    private void drawQuad(VertexConsumer consumer, Matrix4f matrix, float minX, float minY, float maxX, float maxY, float z, float minU, float maxU, float minV, float maxV, int light) {
        consumer.addVertex(matrix, minX, minY, z).setColor(255, 255, 255, 255).setUv(minU, maxV).setUv1(0, 0).setUv2(light).setNormal(0.0F, 0.0F, 1.0F);
        consumer.addVertex(matrix, maxX, minY, z).setColor(255, 255, 255, 255).setUv(maxU, maxV).setUv1(0, 0).setUv2(light).setNormal(0.0F, 0.0F, 1.0F);
        consumer.addVertex(matrix, maxX, maxY, z).setColor(255, 255, 255, 255).setUv(maxU, minV).setUv1(0, 0).setUv2(light).setNormal(0.0F, 0.0F, 1.0F);
        consumer.addVertex(matrix, minX, maxY, z).setColor(255, 255, 255, 255).setUv(minU, minV).setUv1(0, 0).setUv2(light).setNormal(0.0F, 0.0F, 1.0F);
    }
}
