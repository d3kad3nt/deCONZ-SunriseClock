package org.asdfgamer.sunriseClock.model.light;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;

import java.util.List;

@Dao
public interface LightRemote_SwitchableUndimmableUntemperaturableUncolorableDao extends LightBaseDao<LightRemote_SwitchableUndimmableUntemperaturableUncolorable> {
    default LiveData<List<LightRemote_SwitchableUndimmableUntemperaturableUncolorable>> loadAll() {
        return this.loadWithCap(LightRemote_SwitchableUndimmableUntemperaturableUncolorable.SWITCHABLE, LightRemote_SwitchableUndimmableUntemperaturableUncolorable.DIMMABLE, LightRemote_SwitchableUndimmableUntemperaturableUncolorable.TEMPERATURABLE, LightRemote_SwitchableUndimmableUntemperaturableUncolorable.COLORABLE);
    }
}
