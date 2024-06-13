package gtmqol.common;

import com.epimorphismmc.monomorphism.proxy.base.ICommonProxyBase;

import gtmqol.mod.GTMQoL;

import java.util.Objects;

public class CommonProxy implements ICommonProxyBase {

    public CommonProxy() {
        Objects.requireNonNull(
                        Objects.requireNonNull(GTMQoL.Companion.getINSTANCE()).getLogger())
                .info("ExampleMod's Initialization Completed!");
    }

    @Override
    public void registerEventHandlers() {}

    @Override
    public void registerCapabilities() {}
}
