package org.asdfgamer.sunriseClock.model.light;

import androidx.room.Entity;

@Entity(tableName = LightRemote_UnswitchableUndimmableUntemperaturableColorable.TABLENAME)
public class LightRemote_UnswitchableUndimmableUntemperaturableColorable extends LightBase implements ILightRemoteColorable {

    static final String TABLENAME = "light_unswitchable_undimmable_untemperaturable_colorable";

    public static ILightBaseDao<LightRemote_UnswitchableUndimmableUntemperaturableColorable> dao;

    public LightRemote_UnswitchableUndimmableUntemperaturableColorable(int id, String friendlyName, int color) {
        super(id, friendlyName);
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

