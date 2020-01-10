package org.asdfgamer.sunriseClock.model.light;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Entity;

import java.util.List;

@Entity()
public class LightRemote_UnswitchableUndimmableUntemperaturableColorable extends LightBase implements ILightRemoteColorable {

    static final boolean SWITCHABLE = false;
    static final boolean DIMMABLE = false;
    static final boolean TEMPERATURABLE = false;
    static final boolean COLORABLE = true;

    static final String QUERY = "SELECT * FROM 'light_base' WHERE cap_dimmable = '" + SWITCHABLE + "' AND cap_dimmable = '" + DIMMABLE + "' AND cap_temperaturable = '" + TEMPERATURABLE + "' AND cap_colorable = '" + COLORABLE + "'";

    public LightRemote_UnswitchableUndimmableUntemperaturableColorable(int id, String friendlyName, int color) {
        super(id, friendlyName, SWITCHABLE, DIMMABLE, TEMPERATURABLE, COLORABLE);
        super.setColor(color);
    }

    @Override
    public int getColor() {
        return super.getColor();
    }

    @Override
    public void requestSetColor(int color) {
        super.setColor(color);
    }

}

