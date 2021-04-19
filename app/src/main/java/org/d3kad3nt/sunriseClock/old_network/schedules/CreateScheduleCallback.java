package org.d3kad3nt.sunriseClock.old_network.schedules;

import org.d3kad3nt.sunriseClock.old_network.utils.response.custDeserializer.model.Success;
import org.d3kad3nt.sunriseClock.old_network.utils.response.genericCallback.DeconzCreateCallback;

/**
 * Nothing special: This action does not return anything special, apart from the HTTP codes
 * defined in the super class. This interface could be used to define additional callbacks for
 * this request.
 * To maintain a clean file structure even these empty files (subclassed from their super class)
 * are added.
 *
 * @see DeconzCreateCallback
 */
public interface CreateScheduleCallback extends DeconzCreateCallback<Success> {

}
