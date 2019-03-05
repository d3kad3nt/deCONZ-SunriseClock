package org.asdfgamer.sunriseClock.network.lights;

import org.asdfgamer.sunriseClock.network.lights.model.Light;
import org.asdfgamer.sunriseClock.network.utils.response.genericCallback.DeconzBaseCallback;

import java.util.List;

/**
 * Nothing special: This action does not return anything special, apart from the HTTP codes
 * defined in the super class. This interface could be used to define additional callbacks for
 * this request.
 * To maintain a clean file structure even these empty files (subclassed from their super class)
 * are added.
 *
 * @see DeconzBaseCallback
 */
public interface GetLightsCallback extends DeconzBaseCallback<List<Light>> {

    /* Getting all lights from deconz (GET /lights) does not return HTTP 404 and therefore does
     * not require a specific callback to inherit from.*/

}
