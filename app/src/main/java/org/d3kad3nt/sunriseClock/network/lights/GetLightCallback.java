package org.d3kad3nt.sunriseClock.network.lights;

import org.d3kad3nt.sunriseClock.network.lights.model.Light;
import org.d3kad3nt.sunriseClock.network.utils.response.genericCallback.DeconzGetCallback;

/**
 * Nothing special: This action does not return anything special, apart from the HTTP codes
 * defined in the super class. This interface could be used to define additional callbacks for
 * this request.
 * To maintain a clean file structure even these empty files (subclassed from their super class)
 * are added.
 *
 * @see DeconzGetCallback
 */
public interface GetLightCallback extends DeconzGetCallback<Light> {

}
