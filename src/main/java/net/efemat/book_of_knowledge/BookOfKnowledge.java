package net.efemat.book_of_knowledge;

import net.efemat.book_of_knowledge.items.CameraItem;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookOfKnowledge implements ModInitializer {
	public static final String MOD_ID = "book_of_knowledge";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	public static final Item CAMERA_ITEM = new CameraItem();

	@Override
	public void onInitialize() {
		//Registry.register(Item, new Identifier("book_of_knowledge", "camera"), CAMERA_ITEM);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "camera"), CAMERA_ITEM);
		

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
			entries.add(new ItemStack(CAMERA_ITEM));
		});
	}
}