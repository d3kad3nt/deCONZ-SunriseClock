package org.d3kad3nt.sunriseClock.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import org.d3kad3nt.sunriseClock.model.endpoint.EndpointConfig;
import org.d3kad3nt.sunriseClock.model.endpoint.EndpointConfigDao;
import org.d3kad3nt.sunriseClock.model.light.BaseLight;
import org.d3kad3nt.sunriseClock.model.light.BaseLightDao;

/**
 * The Room database for this app.
 */
@Database(entities = {
        BaseLight.class,
        EndpointConfig.class},
        version = 1,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract BaseLightDao baseLightDao();
    public abstract EndpointConfigDao endpointConfigDao();

    private static final String DB_NAME = "sunriseclock-db-DEV.db";
    private static volatile AppDatabase INSTANCE;

    /**
     * Using singleton pattern as of now. With dependency injection (Dagger, ...) this class could be mocked when unit testing.
     * TODO: Dependency Injection, optional
     */
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                INSTANCE = buildDatabase(context);
            }
        }
        return INSTANCE;
    }

    private static AppDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, DB_NAME)
                .allowMainThreadQueries() //TODO: Only for testing purposes. Could lock the main thread, therefore not for production!
                .build();
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}