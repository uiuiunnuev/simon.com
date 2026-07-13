package com.superb.warfare.expanded.registry;

import com.superb.warfare.expanded.item.GrenadeItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class SWEItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, "superb_warfare_expanded");

    public static final DeferredHolder<Item, GrenadeItem> GRENADE = ITEMS.register("grenade",
            () -> new GrenadeItem(new Item.Properties().stacksTo(16)));

    public static final DeferredHolder<Item, BlockItem> WAYPOINT = ITEMS.register("waypoint",
            () -> new BlockItem(SWEBlocks.WAYPOINT.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> LANDMINE = ITEMS.register("landmine",
            () -> new BlockItem(SWEBlocks.LANDMINE.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> FLAG = ITEMS.register("flag",
            () -> new BlockItem(SWEBlocks.FLAG.get(), new Item.Properties()));
}
