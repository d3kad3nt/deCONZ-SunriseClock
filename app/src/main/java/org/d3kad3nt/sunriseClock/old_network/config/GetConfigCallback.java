package org.d3kad3nt.sunriseClock.old_network.config;

import org.d3kad3nt.sunriseClock.old_network.config.model.Config;
import org.d3kad3nt.sunriseClock.old_network.utils.response.genericCallback.DeconzBaseCallback;
import org.d3kad3nt.sunriseClock.old_network.utils.response.genericCallback.DeconzGetCallback;

/**
 * Nothing special: This action does not return anything special, apart from the HTTP codes
 * defined in the super class. This interface could be used to define additional callbacks for
 * this request.
 * To maintain a clean file structure even these empty files (subclassed from their super class)
 * are added.
 *
 * @see DeconzGetCallback
 */
public interface GetConfigCallback extends DeconzBaseCallback<Config> {

    /* Getting the config from deconz (GET /config) does not return HTTP 404 and therefore does
     * not require a specific callback to inherit from.*/

}
