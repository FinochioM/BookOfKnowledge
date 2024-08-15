
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
                user.sendMessage(Text.literal("Camera HUD activated"), true);
            }else {
                user.sendMessage(Text.literal("Camera HUD deactivated"), true);
            }
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }
    
    public static boolean isHudActive(){
        return hudActive;
    }
}