package com.superb.warfare.expanded.registry;

import com.superb.warfare.expanded.item.GrenadeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SWEItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "superb_warfare_expanded");

    public static final RegistryObject<Item> GRENADE = ITEMS.register("grenade",
            () -> new GrenadeItem(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> WAYPOINT = ITEMS.register("waypoint",
            () -> new BlockItem(SWEBlocks.WAYPOINT.get(), new Item.Properties()));

    public static final RegistryObject<Item> LANDMINE = ITEMS.register("landmine",
            () -> new BlockItem(SWEBlocks.LANDMINE.get(), new Item.Properties()));

    public static final RegistryObject<Item> FLAG = ITEMS.register("flag",
            () -> new BlockItem(SWEBlocks.FLAG.get(), new Item.Properties()));
}
