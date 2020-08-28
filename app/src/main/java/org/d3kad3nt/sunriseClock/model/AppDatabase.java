package org.d3kad3nt.sunriseClock.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import org.d3kad3nt.sunriseClock.model.endpoint.EndpointConfig;
import org.d3kad3nt.sunriseClock.model.endpoint.EndpointConfigDao;
import org.d3kad3nt.sunriseClock.model.light.BaseLight;
import org.d3kad3nt.sunriseClock.model.light.BaseLightDao;

@Database(entities = {
        BaseLight.class,
        EndpointConfig.class},
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

    public abstract BaseLightDao baseLightDao();
    public abstract EndpointConfigDao endpointConfigDao();

}