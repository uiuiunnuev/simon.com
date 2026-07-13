package com.superb.warfare.expanded;

import com.superb.warfare.expanded.client.FlagBlockRenderer;
import com.superb.warfare.expanded.entity.CommanderEntity;
import com.superb.warfare.expanded.entity.SniperEntity;
import com.superb.warfare.expanded.entity.SoldierEntity;
import com.superb.warfare.expanded.registry.SWEBlocks;
import com.superb.warfare.expanded.registry.SWEEntities;
import com.superb.warfare.expanded.registry.SWEItems;
import com.superb.warfare.expanded.registry.SWERegistries;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@Mod("superb_warfare_expanded")
public class SuperbWarfareExpanded {

    public SuperbWarfareExpanded(IEventBus modEventBus) {
        SWEBlocks.BLOCKS.register(modEventBus);
        SWEBlocks.BLOCK_ENTITIES.register(modEventBus);
        SWEItems.ITEMS.register(modEventBus);
        SWEEntities.ENTITIES.register(modEventBus);
        SWERegistries.PROCESSORS.register(modEventBus);
    }

    @EventBusSubscriber(modid = "superb_warfare_expanded", bus = EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onAttributeCreation(EntityAttributeCreationEvent event) {
            event.put(SWEEntities.SOLDIER.get(), SoldierEntity.createAttributes().build());
            event.put(SWEEntities.SNIPER.get(), SniperEntity.createAttributes().build());
            event.put(SWEEntities.COMMANDER.get(), CommanderEntity.createAttributes().build());
        }
    }

    @EventBusSubscriber(modid = "superb_warfare_expanded", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(SWEBlocks.FLAG_ENTITY_TYPE.get(), FlagBlockRenderer::new);
            event.registerEntityRenderer(SWEEntities.THROWN_GRENADE.get(), ThrownItemRenderer::new);
        }
    }
}
