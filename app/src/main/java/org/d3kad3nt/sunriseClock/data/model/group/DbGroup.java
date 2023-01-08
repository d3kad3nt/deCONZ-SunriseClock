package org.d3kad3nt.sunriseClock.data.model.group;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import org.d3kad3nt.sunriseClock.data.model.DbEndpointEntity;
import org.d3kad3nt.sunriseClock.data.model.endpoint.EndpointConfig;
import org.jetbrains.annotations.Contract;

@Entity(tableName = DbGroup.TABLENAME,
    indices = {@Index(value = {"endpoint_id", "id_on_endpoint"},
        unique = true)},
    // A DbGroup is always bound to a single endpoint. It cannot exist without one:
    // Therefore Room is instructed to delete this DbGroup if the endpoint gets deleted.
    foreignKeys = @ForeignKey(entity = EndpointConfig.class,
        parentColumns = "endpointId",
        childColumns = "endpoint_id",
        onDelete = ForeignKey.CASCADE))
public class DbGroup extends DbEndpointEntity {

    @Ignore
    public static final String TABLENAME = "group";

    @Ignore
    private static final String TAG = "DbGroup";

    /**
     * Create a new object that represents a group in the app's Room database. This constructor has to be public for
     * Room to be able to create an object. This should not be otherwise accessed!
     */
    public DbGroup(long endpointId, String endpointObjectId, String name) {
        super(endpointId, endpointObjectId, name);
    }

    @NonNull
    @Contract("_ -> new")
    public static DbGroup from(@NonNull RemoteGroup remoteGroup) {
        return RemoteGroup.toDbGroup(remoteGroup);
    }
}
