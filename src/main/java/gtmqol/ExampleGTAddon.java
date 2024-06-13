package gtmqol;

import com.epimorphismmc.monomorphism.MOGTAddon;

import com.gregtechceu.gtceu.api.addon.GTAddon;

@GTAddon
public class ExampleGTAddon extends MOGTAddon {

    public ExampleGTAddon() {
        super(GTMQoL.MODID);
    }

    @Override
    public void initializeAddon() {}
}
