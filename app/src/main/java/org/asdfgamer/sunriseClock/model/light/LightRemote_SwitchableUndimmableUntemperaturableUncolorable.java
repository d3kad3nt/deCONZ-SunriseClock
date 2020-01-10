package org.asdfgamer.sunriseClock.model.light;

import androidx.room.Entity;

@Entity(tableName = LightRemote_SwitchableUndimmableUntemperaturableUncolorable.TABLENAME)
public class LightRemote_SwitchableUndimmableUntemperaturableUncolorable extends LightBase implements ILightRemoteSwitchable {

    static final String TABLENAME = "light_switchable_undimmable_untemperaturable_uncolorable";

    public static LightRemoteDao_UnswitchableUndimmableUntemperaturableColorable dao;

    public LightRemote_SwitchableUndimmableUntemperaturableUncolorable(int id, String friendlyName, boolean on) {
        super(id, friendlyName);
        super.setOn(on);
    }

    @Override
    public boolean isOn() {
        return false;
    }

    @Override
    public void requestSetOn(boolean on) {

    }
}

