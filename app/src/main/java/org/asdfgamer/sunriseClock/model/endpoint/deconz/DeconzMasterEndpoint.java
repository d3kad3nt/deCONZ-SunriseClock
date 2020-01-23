package org.asdfgamer.sunriseClock.model.endpoint.deconz;

import org.asdfgamer.sunriseClock.model.endpoint.BaseMasterEndpoint;
import org.asdfgamer.sunriseClock.model.light.BaseLightEndpoint;

public class DeconzMasterEndpoint extends BaseMasterEndpoint {

    public BaseLightEndpoint baseLightEndpoint = new DeconzLightEndpoint();

}
