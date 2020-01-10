package org.asdfgamer.sunriseClock.model.light;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Entity;

import java.util.List;

@Entity()
public class LightRemote_SwitchableUndimmableUntemperaturableUncolorable extends LightBase implements ILightRemoteSwitchable {

    static final boolean SWITCHABLE = true;
    static final boolean DIMMABLE = false;
    static final boolean TEMPERATURABLE = false;
    static final boolean COLORABLE = false;

    static final String QUERY = "SELECT * FROM 'light_base' WHERE cap_switchable = 0 AND cap_dimmable = 0 AND cap_temperaturable = 0 AND cap_colorable = 0";

    public LightRemote_SwitchableUndimmableUntemperaturableUncolorable(int id, String friendlyName, boolean on) {
        super(id, friendlyName, SWITCHABLE, DIMMABLE, TEMPERATURABLE, COLORABLE);
        super.setOn(on);
    }

    @Override
    public boolean isOn() {
        return super.isOn();
    }

    @Override
    public void requestSetOn(boolean on) {
        super.setOn(on);
    }
}

