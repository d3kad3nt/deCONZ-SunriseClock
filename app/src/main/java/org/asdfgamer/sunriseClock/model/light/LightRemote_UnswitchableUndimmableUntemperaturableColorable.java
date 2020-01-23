package org.asdfgamer.sunriseClock.model.light;

import androidx.room.Entity;

@Entity()
public class LightRemote_UnswitchableUndimmableUntemperaturableColorable extends BaseLight implements ILightRemoteColorable {

    private static final boolean SWITCHABLE = false;
    private static final boolean DIMMABLE = false;
    private static final boolean TEMPERATURABLE = false;

    public LightRemote_UnswitchableUndimmableUntemperaturableColorable(int id, String friendlyName, int color) {
        super(id, friendlyName, SWITCHABLE, DIMMABLE, TEMPERATURABLE, COLORABLE);
        super.requestSetColor(color);
    }

    @Override
    public int getColor() {
        return super.getColor();
    }

    @Override
    public void requestSetColor(int color) {
        super.requestSetColor(color);
    }

}

