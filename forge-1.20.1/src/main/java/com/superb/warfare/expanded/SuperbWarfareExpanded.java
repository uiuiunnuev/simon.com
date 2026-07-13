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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("superb_warfare_expanded")
public class SuperbWarfareExpanded {

    public SuperbWarfareExpanded() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        SWEBlocks.BLOCKS.register(modEventBus);
        SWEBlocks.BLOCK_ENTITIES.register(modEventBus);
        SWEItems.ITEMS.register(modEventBus);
        SWEEntities.ENTITIES.register(modEventBus);
        SWERegistries.PROCESSORS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @Mod.EventBusSubscriber(modid = "superb_warfare_expanded", bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onAttributeCreation(EntityAttributeCreationEvent event) {
            event.put(SWEEntities.SOLDIER.get(), SoldierEntity.createAttributes().build());
            event.put(SWEEntities.SNIPER.get(), SniperEntity.createAttributes().build());
            event.put(SWEEntities.COMMANDER.get(), CommanderEntity.createAttributes().build());
        }
    }

    @Mod.EventBusSubscriber(modid = "superb_warfare_expanded", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(SWEBlocks.FLAG_ENTITY_TYPE.get(), FlagBlockRenderer::new);
            event.registerEntityRenderer(SWEEntities.THROWN_GRENADE.get(), ThrownItemRenderer::new);
        }
    }
}
