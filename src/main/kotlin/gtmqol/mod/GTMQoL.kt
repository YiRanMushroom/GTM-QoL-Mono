package gtmqol.mod

import com.epimorphismmc.monomorphism.datagen.MOProviderTypes
import com.epimorphismmc.monomorphism.datagen.lang.MOLangProvider
import com.epimorphismmc.monomorphism.registry.registrate.MORegistrate
import com.gregtechceu.gtceu.utils.FormattingUtil
import com.lowdragmc.lowdraglib.networking.INetworking
import gtmqol.client.ClientProxy
import gtmqol.common.CommonProxy
import gtmqol.config.ConfigHolder
import gtmqol.data.ExampleLangHandler
import gtmqol.misc.helloFromKotlin
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.fml.common.Mod
import org.slf4j.Logger

@Mod(GTMQoL.MODID)
class GTMQoL : MOKTMod<CommonProxy>() {
    // Define mod id in a common place for everything to reference
    companion object{
        var INSTANCE = GTMQoL()

        const val MODID: String = "gtmqol"

        const val NAME: String = "GregTech Modern Quality of Life"
    }


    override fun getModId(): String {
        return MODID
    }

    override fun getModName(): String {
        return NAME
    }

    override fun onModConstructed() {
        ConfigHolder.init()
    }

    override fun afterInitializing() {
        logger().info(helloFromKotlin())
    }

    @OnlyIn(Dist.CLIENT)
    override fun createClientProxy(): CommonProxy {
        return ClientProxy()
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    override fun createServerProxy(): CommonProxy {
        return CommonProxy()
    }

    override fun addDataGenerator(registrate: MORegistrate?) {
        registrate!!.addDataGenerator(
            MOProviderTypes.MO_LANG
        ) { provider: MOLangProvider? ->
            ExampleLangHandler.init(
                provider
            )
        }
    }

    fun id(path: String?): ResourceLocation {
        return ResourceLocation(MODID, FormattingUtil.toLowerCaseUnder(path))
    }

    fun logger(): Logger {
        return this.getLogger()!!
    }

    fun proxy(): CommonProxy {
        return this.getProxy()
    }

    fun registrate(): MORegistrate {
        return this.getRegistrate()!!
    }

    fun network(): INetworking {
        return this.getNetwork()!!
    }
}