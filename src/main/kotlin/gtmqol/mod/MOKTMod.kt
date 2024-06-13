package gtmqol.mod

import com.epimorphismmc.monomorphism.proxy.base.ICommonProxyBase
import com.epimorphismmc.monomorphism.registry.registrate.MORegistrate
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialRegistryEvent
import com.lowdragmc.lowdraglib.networking.INetworking
import com.lowdragmc.lowdraglib.networking.LDLNetworking
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.server.ServerAboutToStartEvent
import net.minecraftforge.event.server.ServerStartingEvent
import net.minecraftforge.event.server.ServerStoppedEvent
import net.minecraftforge.event.server.ServerStoppingEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.LogicalSide
import net.minecraftforge.fml.event.lifecycle.*
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.fml.loading.FMLEnvironment
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.callWhenOn
import java.util.concurrent.Callable
import java.util.function.Supplier


abstract class MOKTMod<P : ICommonProxyBase> {
    private var logger: Logger? = null
    private var network: INetworking? = null
    private var proxy: P? = null
    private var registrate: MORegistrate? = null

    init {
        //Populate static mod instance
        this.onModConstructed()
        //Create logger
        this.logger = LoggerFactory.getLogger(getModName())
        // Create network
        this.network = LDLNetworking.createNetworking(ResourceLocation(getModId(), "networking"), "0.0.1")
        // Create proxy
        this.proxy = this.createProxy()
        this.registrate = MORegistrate.create(getModId())
        // Register FML mod loading cycle listeners
        val bus = MOD_BUS
        bus.addListener { event: FMLCommonSetupEvent? ->
            this.onCommonSetupEvent(
                event
            )
        }
        bus.addListener { event: FMLClientSetupEvent? ->
            this.onClientSetupEvent(
                event
            )
        }
        bus.addListener { event: FMLDedicatedServerSetupEvent? ->
            this.onDedicatedServerSetupEvent(
                event
            )
        }
        bus.addListener { event: InterModEnqueueEvent? ->
            this.onInterModEnqueueEvent(
                event
            )
        }
        bus.addListener { event: InterModProcessEvent? ->
            this.onInterModProcessEvent(
                event
            )
        }
        bus.addListener { event: FMLLoadCompleteEvent? ->
            this.onModLoadCompleteEvent(
                event
            )
        }
        proxy!!.registerModBusEventHandlers(bus)
        MinecraftForge.EVENT_BUS.register(this)
        registrate!!.registerEventListeners(bus)
        addDataGenerator(registrate)
        // Register capabilities
        proxy!!.registerCapabilities()
        this.afterInitializing()
    }

    open fun afterInitializing() {

    }

    private fun createProxy(): P {
        var proxy: P? = callWhenOn(
            Dist.CLIENT
        ) { this.createClientProxy() }
        if (proxy == null) {
            proxy = callWhenOn(
                Dist.DEDICATED_SERVER
            ) { this.createServerProxy() }
        }
        if (proxy == null) {
            // Can only happen if the mod fails to correctly implement the createClientProxy and/or the createServerProxy methods
            throw RuntimeException("Failed to create SidedProxy for mod " + this.getModId() + " on side: " + FMLEnvironment.dist.name)
        }
        return proxy
    }

    private fun init() {
        // Register event handlers
        proxy!!.registerEventHandlers()
        // Register messages
        registerPackets(network)
    }

    private fun initClient() {
    }

    fun getLogger(): Logger? {
        return this.logger
    }

    fun getNetwork(): INetworking? {
        return this.network
    }

    fun getProxy(): P {
        return proxy!!
    }

    fun getRegistrate(): MORegistrate? {
        return this.registrate
    }

    /**
     * @return The mod ID of the mod
     */
    abstract fun getModId(): String

    /**
     * @return The name of the mod
     */
    abstract fun getModName(): String?

    /**
     * Provides access to the instantiated mod object, for instance to store it in a static field
     */
    protected abstract fun onModConstructed()

    /**
     * @return Creates the client proxy object for this mod
     */
    @OnlyIn(Dist.CLIENT)
    protected abstract fun createClientProxy(): P

    /**
     * @return Creates the server proxy object for this mod
     */
    @OnlyIn(Dist.DEDICATED_SERVER)
    protected abstract fun createServerProxy(): P

    /**
     * Register all messages added by this mod
     * @param network NetworkWrapper instance to register messages to
     */
    fun registerPackets(network: INetworking?) {}

    open fun addDataGenerator(registrate: MORegistrate?) {}

    /**
     * --------------------------
     * FML Mod Loading Listeners
     * --------------------------
     */
    fun onCommonSetupEvent(event: FMLCommonSetupEvent?) {
        //self init
        this.init()
        //forward to proxy
        proxy!!.onCommonSetupEvent(event)
    }

    fun onClientSetupEvent(event: FMLClientSetupEvent?) {
        //self init
        this.initClient()
        //forward to proxy
        proxy!!.onClientSetupEvent(event)
    }

    fun onDedicatedServerSetupEvent(event: FMLDedicatedServerSetupEvent?) {
        //forward to proxy
        proxy!!.onDedicatedServerSetupEvent(event)
    }

    fun onInterModEnqueueEvent(event: InterModEnqueueEvent?) {
        //forward to proxy
        proxy!!.onInterModEnqueueEvent(event)
    }

    fun onInterModProcessEvent(event: InterModProcessEvent?) {
        //forward to proxy
        proxy!!.onInterModProcessEvent(event)
    }

    fun onModLoadCompleteEvent(event: FMLLoadCompleteEvent?) {
        //forward to proxy
        proxy!!.onModLoadCompleteEvent(event)
    }

    @SubscribeEvent
    @Suppress("unused")
    fun onServerStartingEvent(event: ServerStartingEvent?) {
        //forward to proxy
        proxy!!.onServerStartingEvent(event)
    }

    @SubscribeEvent
    @Suppress("unused")
    fun onServerAboutToStartEvent(event: ServerAboutToStartEvent?) {
        //forward to proxy
        proxy!!.onServerAboutToStartEvent(event)
    }

    @SubscribeEvent
    @Suppress("unused")
    fun onServerStoppingEvent(event: ServerStoppingEvent?) {
        //forward to proxy
        proxy!!.onServerStoppingEvent(event)
    }

    @SubscribeEvent
    @Suppress("unused")
    fun onServerStoppedEvent(event: ServerStoppedEvent?) {
        //forward to proxy
        proxy!!.onServerStoppedEvent(event)
    }

    @SubscribeEvent
    fun registerMaterialRegistry(event: MaterialRegistryEvent?) {
    }


    /**
     * --------------------------
     * Proxy utility method calls
     * --------------------------
     */
    /**
     * @return The physical side, is always Side.SERVER on the server and Side.CLIENT on the client
     */
    fun getPhysicalSide(): Dist {
        return proxy!!.physicalSide
    }

    /**
     * @return The effective side, on the server, this is always Side.SERVER, on the client it is dependent on the thread
     */
    fun getEffectiveSide(): LogicalSide {
        return proxy!!.logicalSide
    }

    /**
     * @return The minecraft server instance
     */
    fun getMinecraftServer(): MinecraftServer {
        return proxy!!.minecraftServer
    }

    /**
     * @return a registry access object based on the logical side
     */
    fun getRegistryAccess(): RegistryAccess {
        return proxy!!.registryAccess
    }

    /**
     * @return the instance of the EntityPlayer on the client, null on the server
     */
    fun getClientPlayer(): Player {
        return proxy!!.clientPlayer
    }

    /**
     * @return the client World object on the client, null on the server
     */
    fun getClientWorld(): Level {
        return proxy!!.clientWorld
    }

    /**
     * @return the client World object on the client, null on the server
     */
    fun getWorldFromDimension(dimension: ResourceKey<Level?>?): Level {
        return proxy!!.getWorldFromDimension(dimension)
    }

    /**
     * @return  the entity in that World object with that id
     */
    fun getEntityById(world: Level?, id: Int): Entity {
        return proxy!!.getEntityById(world, id)
    }

    /**
     * @return  the entity in that World object with that id
     */
    fun getEntityById(dimension: ResourceKey<Level?>?, id: Int): Entity {
        return proxy!!.getEntityById(dimension, id)
    }

    /** Queues a task to be executed on this side  */
    fun queueTask(task: Runnable?) {
        proxy!!.queueTask(task)
    }

    /** Registers an event handler  */
    fun registerEventHandler(handler: Any?) {
        proxy!!.registerEventHandler(handler)
    }
}