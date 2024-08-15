package net.efemat.book_of_knowledge.gui;

import net.efemat.book_of_knowledge.BookOfKnowledge;
import net.efemat.book_of_knowledge.items.CameraItem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.ChunkRenderDistanceCenterS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.UserCache;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;
import java.util.Optional;

public class CameraHudOverlay implements HudRenderCallback {
    private static final Identifier CAMERA_OVERLAY_TEXTURE = new Identifier(BookOfKnowledge.MOD_ID, "textures/gui/camera_overlay.png");

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && client.player.getMainHandStack().getItem() instanceof CameraItem && CameraItem.isHudActive()){
            client.getTextureManager().bindTexture(CAMERA_OVERLAY_TEXTURE);
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();
            
            drawContext.drawTexture(CAMERA_OVERLAY_TEXTURE, 0, 0, 0, 0, width, height, width, height);

            Entity targetEntity = getTargetEntity(client.player, 20.0);
            
            if (targetEntity instanceof LivingEntity){
                renderEntityOnScreen(drawContext.getMatrices(), - 50, height -75, 30, (LivingEntity) targetEntity, tickDelta);
                client.player.sendMessage(Text.literal("Entity found: " + targetEntity.getName().getString()), true);
            }
        }
    }
    
    private Entity getTargetEntity(PlayerEntity player,  double maxDistance){
        Vec3d startVec = player.getCameraPosVec(1.0f);
        Vec3d lookVec = player.getRotationVec(1.0f);
        Vec3d endVec = startVec.add(lookVec.multiply(maxDistance));

        List<Entity> entities = player.getEntityWorld().getOtherEntities(player, player.getBoundingBox().stretch(lookVec.multiply(maxDistance)).expand(1.0, 1.0, 1.0));
        Entity closestEntity = null;
        double closestDistance = maxDistance;
        
        for (Entity entity : entities){
            Optional<Vec3d> hitResult = entity.getBoundingBox().expand(entity.getTargetingMargin()).raycast(startVec, endVec);
            
            if (hitResult.isPresent()){
                double distance = startVec.squaredDistanceTo(hitResult.get());
                
                if (distance < closestDistance){
                    closestEntity = entity;
                    closestDistance = distance;
                }
            }
        }
        
        return closestEntity;
    }
    
    private void renderEntityOnScreen(MatrixStack matrices, int x, int y, int size, LivingEntity entity, float tickDelta){
        EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();

        // Save the previous pitch and yaw to restore them later
        float previousYaw = entity.getYaw();
        float previousPitch = entity.getPitch();

        // Adjust the entity's rotation to face the screen
        entity.setYaw(0.0F);
        entity.setPitch(0.0F);

        // Translate and scale the entity
        matrices.push();
        matrices.translate(x, y, 1050.0F);
        matrices.scale(size, size, -size);

        // Apply rotation to make the entity face the player
        Quaternionf rotation = new Quaternionf().rotateZ((float) Math.toRadians(180.0F));
        matrices.multiply(rotation);

        // Render the entity on the screen
        entityRenderDispatcher.setRenderShadows(false);
        entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, tickDelta, matrices, MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers(), 15728880);
        entityRenderDispatcher.setRenderShadows(true);

        // Restore the previous pitch and yaw
        entity.setYaw(previousYaw);
        entity.setPitch(previousPitch);

        // Pop the matrix to restore the previous state
        matrices.pop();
    }
}
