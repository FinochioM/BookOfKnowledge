
package net.efemat.book_of_knowledge.items;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class CameraItem extends Item {
    private static boolean hudActive = false;
    
    public CameraItem() {
        super(new Item.Settings().maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            hudActive = !hudActive;
            
            MinecraftClient client = MinecraftClient.getInstance();
            
            if (hudActive){
                /*if (client.player != null && client.player.getMainHandStack().getItem() instanceof CameraItem){
                    Framebuffer framebuffer = client.getFramebuffer();
                    File screenshotDir = new File(client.runDirectory,"screenshots");

                    if (!screenshotDir.exists()) screenshotDir.mkdirs();

                    ScreenshotRecorder.saveScreenshot(screenshotDir, framebuffer, (message) -> {
                        user.sendMessage(Text.literal(message.getString()), false);
                    });
                }*/
                user.sendMessage(Text.literal("Camera HUD activated"), true);
            }else {
                user.sendMessage(Text.literal("Camera HUD deactivated"), true);
            }

            Entity targetEntity = getTargetEntity(user, 20.0);
            
            if (targetEntity instanceof LivingEntity){
                user.sendMessage(Text.literal("You are looking at: " + targetEntity.getName().getString()), true);
            }else {
                user.sendMessage(Text.literal("You are not looking at a living entity"), true);
            }
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }
    
    private Entity getTargetEntity(PlayerEntity player, double maxDistance){
        Vec3d startVec = player.getCameraPosVec(1.0f);
        Vec3d lookVec = player.getRotationVec(1.0f);
        Vec3d endVec = startVec.add(lookVec.multiply(maxDistance));

        Box box = player.getBoundingBox().stretch(lookVec.multiply(maxDistance)).expand(1.0, 1.0, 1.0);
        List<Entity> entities = player.getEntityWorld().getOtherEntities(player, box);
        
        Entity closestEntity = null;
        double closestDistance = maxDistance;
        
        for (Entity entity : entities){
            Box entityBox = entity.getBoundingBox().expand(entity.getTargetingMargin());
            Optional<Vec3d> hitResult = entityBox.raycast(startVec, endVec);
            
            if (entityBox.contains(startVec)){
                if(closestDistance >= 0.0){
                    closestEntity = entity;
                    closestDistance = 0.0;
                }
            }else if (hitResult.isPresent()){
                double distance = startVec.squaredDistanceTo(hitResult.get());
                
                if (distance < closestDistance || closestDistance == 0.0){
                    closestEntity = entity;
                    closestDistance = distance;
                }
            }
        }
        
        return closestEntity;
    }
    
    public static boolean isHudActive(){
        return hudActive;
    }
}