package org.asdfgamer.sunriseClock.model.light;

import androidx.room.Entity;

@Entity()
public class LightRemote_SwitchableUndimmableUntemperaturableUncolorable extends BaseLight implements ILightRemoteSwitchable {

    static final boolean DIMMABLE = false;
    static final boolean TEMPERATURABLE = false;
    static final boolean COLORABLE = false;

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

