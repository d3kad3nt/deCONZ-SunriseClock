package org.d3kad3nt.sunriseClock.data.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.RenameColumn;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.d3kad3nt.sunriseClock.data.model.endpoint.EndpointConfig;
import org.d3kad3nt.sunriseClock.data.model.group.DbGroup;
import org.d3kad3nt.sunriseClock.data.model.groupWithLights.DbGroupLightCrossref;
import org.d3kad3nt.sunriseClock.data.model.light.DbLight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Room database for this app.
 */
@Database(entities = {DbLight.class, DbGroup.class, DbGroupLightCrossref.class, EndpointConfig.class},
    version = 6,
    autoMigrations = {@AutoMigration(from = 2,
        to = 3), @AutoMigration(from = 3,
        to = 4), @AutoMigration(from = 4,
        to = 5,
        spec = AppDatabase.Migration4To5.class), @AutoMigration(from = 5,
        to = 6,
        spec = AppDatabase.Migration5To6.class)})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "sunriseclock-db-DEV.db";
    private static final List<Migration> migrations = new ArrayList<>(Arrays.asList(new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL(
                "CREATE TABLE new_endpoint (" + "endpointId INTEGER PRIMARY KEY NOT NULL," + "date_added INTEGER," +
                    "config TEXT," + "type INTEGER," + "name TEXT NOT NULL DEFAULT " + "'Unnamed" + " Endpoint')");
            database.execSQL("INSERT INTO new_endpoint (endpointId, date_added, config, type) " + "SELECT " +
                "endpointId, date_added, config, type FROM endpoint");
            database.execSQL("DROP TABLE endpoint");
            database.execSQL("ALTER TABLE new_endpoint RENAME TO endpoint");
        }
    }));
    private static volatile AppDatabase INSTANCE;

    /**
     * Using singleton pattern as of now. With dependency injection (Dagger, ...) this class could be mocked when unit
     * testing.
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
        return Room.databaseBuilder(context, AppDatabase.class, DB_NAME).addMigrations(allMigrations()).build();
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public static Migration[] allMigrations() {
        Migration[] migrationsArray = new Migration[migrations.size()];
        return migrations.toArray(migrationsArray);
    }

    @RenameColumn(tableName = "light",
        fromColumnName = "light_id",
        toColumnName = "id")
    static class Migration4To5 implements AutoMigrationSpec {

    }

    @RenameColumn.Entries(value = {@RenameColumn(tableName = "light",
        fromColumnName = "endpoint_light_id",
        toColumnName = "id_on_endpoint"), @RenameColumn(tableName = "group",
        fromColumnName = "endpoint_light_id",
        toColumnName = "id_on_endpoint")})
    static class Migration5To6 implements AutoMigrationSpec {

    }

    public abstract DbLightDao dbLightDao();

    public abstract DbGroupDao dbGroupDao();

    public abstract EndpointConfigDao endpointConfigDao();
}
