package net.efemat.book_of_knowledge.gui;

import net.efemat.book_of_knowledge.BookOfKnowledge;
import net.efemat.book_of_knowledge.items.CameraItem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

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
        }
    }
}
