package gtmqol.client;

import com.epimorphismmc.monomorphism.proxy.base.IClientProxyBase;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import gtmqol.common.CommonProxy;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy implements IClientProxyBase {
    public ClientProxy() {
        super();
    }
}
