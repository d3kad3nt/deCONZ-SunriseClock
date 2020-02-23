package org.asdfgamer.sunriseClock.model.endpoint.deconz;

import org.asdfgamer.sunriseClock.model.endpoint.MasterEndpoint;
import org.asdfgamer.sunriseClock.model.light.BaseLightEndpoint;

public class DeconzMasterEndpoint extends MasterEndpoint {

    public BaseLightEndpoint baseLightEndpoint = new DeconzLightEndpoint();

}
