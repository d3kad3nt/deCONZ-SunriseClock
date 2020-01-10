package org.asdfgamer.sunriseClock.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import org.asdfgamer.sunriseClock.model.light.ILightBaseDao;
import org.asdfgamer.sunriseClock.model.light.LightBase;
import org.asdfgamer.sunriseClock.model.light.LightRemoteDao_UnswitchableUndimmableUntemperaturableColorable;
import org.asdfgamer.sunriseClock.model.light.LightRemote_SwitchableUndimmableUntemperaturableUncolorable;
import org.asdfgamer.sunriseClock.model.light.LightRemote_UnswitchableUndimmableUntemperaturableColorable;

import java.lang.reflect.Field;

@Database(entities = {LightBase.class,
        LightRemote_UnswitchableUndimmableUntemperaturableColorable.class},
        version = 1,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context,
                    AppDatabase.class, "sunriseclock-db-DEV.db")
                    .allowMainThreadQueries() //TODO: Only for testing purposes. Could lock the main thread, therefore not for production!
                    .build();
        }

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public abstract LightRemoteDao_UnswitchableUndimmableUntemperaturableColorable LightRemoteDao_UnswitchableUndimmableUntemperaturableColorable();

    //TODO: getDaoInstanceFor(T extends LightBase) implementieren. Oder ist das ne schlechte Idee?

}