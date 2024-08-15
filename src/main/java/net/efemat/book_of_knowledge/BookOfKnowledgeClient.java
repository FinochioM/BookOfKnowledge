package net.efemat.book_of_knowledge;

import net.efemat.book_of_knowledge.gui.CameraHudOverlay;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class BookOfKnowledgeClient implements ClientModInitializer{
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new CameraHudOverlay());
    }
}
