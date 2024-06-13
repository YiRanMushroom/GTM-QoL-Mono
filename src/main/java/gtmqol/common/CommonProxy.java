package gtmqol.common;

import gtmqol.GTMQoL;

import com.epimorphismmc.monomorphism.proxy.base.ICommonProxyBase;

public class CommonProxy implements ICommonProxyBase {

    public CommonProxy() {
        GTMQoL.logger().info("ExampleMod's Initialization Completed!");
    }

    @Override
    public void registerEventHandlers() {}

    @Override
    public void registerCapabilities() {}
}
