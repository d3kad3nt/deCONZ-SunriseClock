package org.d3kad3nt.sunriseClock.backend.data.model.group;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.d3kad3nt.sunriseClock.backend.data.model.DbEndpointEntity;
import org.d3kad3nt.sunriseClock.backend.data.model.endpoint.EndpointConfig;
import org.jetbrains.annotations.Contract;

@Entity(
        tableName = DbGroup.TABLENAME,
        indices = {
            @Index(
                    value = {"endpoint_id", "id_on_endpoint"},
                    unique = true)
        },
        // A DbGroup is always bound to a single endpoint. It cannot exist without one:
        // Therefore Room is instructed to delete this DbGroup if the endpoint gets deleted.
        foreignKeys =
                @ForeignKey(
                        entity = EndpointConfig.class,
                        parentColumns = "endpointId",
                        childColumns = "endpoint_id",
                        onDelete = ForeignKey.CASCADE))
public class DbGroup extends DbEndpointEntity {

    @Ignore
    public static final String TABLENAME = "group";

    /**
     * Create a new entity that represents a group in the app's Room database. This constructor has to be public for
     * Room to be able to create an object. This should not be otherwise accessed!
     */
    public DbGroup(long endpointId, String endpointEntityId, String name) {
        super(endpointId, endpointEntityId, name);
    }

    @Override
    public String getTABLENAME() {
        return DbGroup.TABLENAME;
    }

    @NonNull
    @Contract("_ -> new")
    public static DbGroup from(@NonNull RemoteGroup remoteGroup) {
        return RemoteGroup.toDbGroup(remoteGroup);
    }

    /** @noinspection unused*/
    @NonNull
    @Contract("_ -> new")
    public static List<DbGroup> from(@NonNull List<RemoteGroup> remoteGroups) {
        return remoteGroups.stream().map(remoteGroup -> from(remoteGroup)).collect(Collectors.toList());
    }

    // Room requires equals() and hashcode() to be implemented:
    // The key of the provided method's multimap return type must implement equals() and hashCode().
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final DbGroup dbGroup)) {
            return false;
        }
        return super.equals(dbGroup);
    }

    // Room requires equals() and hashcode() to be implemented:
    // The key of the provided method's multimap return type must implement equals() and hashCode().
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }
}
