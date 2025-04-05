package org.d3kad3nt.sunriseClock.backend.data.model.group;

import androidx.annotation.NonNull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.d3kad3nt.sunriseClock.backend.data.model.ListItemType;
import org.d3kad3nt.sunriseClock.backend.data.model.UIEndpointEntity;
import org.d3kad3nt.sunriseClock.util.LogUtil;
import org.jetbrains.annotations.Contract;

public final class UIGroup extends UIEndpointEntity<UIGroup> {

    private UIGroup(long groupId, long endpointId, String name) {
        super(groupId, endpointId, name);
    }

    @NonNull
    @Contract("_ -> new")
    public static UIGroup from(@NonNull DbGroup dbGroup) {
        // Place for conversion logic (if UI needs other data types or value ranges).
        UIGroup uiGroup = new UIGroup(dbGroup.getId(), dbGroup.getEndpointId(), dbGroup.getName());
        LogUtil.v(
                "Converted DbGroup with groupId %d (endpointId %d, endpointGroupId %s) to UIGroup.",
                dbGroup.getId(), dbGroup.getEndpointId(), dbGroup.getEndpointEntityId());
        return uiGroup;
    }

    @NonNull
    @Contract("_ -> new")
    public static List<UIGroup> from(@NonNull List<DbGroup> dbGroups) {
        return dbGroups.stream().map(dbGroup -> from(dbGroup)).collect(Collectors.toList());
    }

    public static UIGroup.UIGroupChangePayload getSingleChangePayload(
            @NonNull UIGroup oldItem, @NonNull UIGroup newItem) {
        if (!Objects.equals(oldItem.getId(), newItem.getId())) {
            return new UIGroup.UIGroupChangePayload.GroupId(newItem.getId());
        } else if (!Objects.equals(oldItem.getEndpointId(), newItem.getEndpointId())) {
            return new UIGroup.UIGroupChangePayload.EndpointId(newItem.getEndpointId());
        } else if (!Objects.equals(oldItem.getName(), newItem.getName())) {
            return new UIGroup.UIGroupChangePayload.GroupName(newItem.getName());
        }
        return null;
    }

    public ListItemType getType() {
        return ListItemType.GROUP;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final UIGroup uiGroup)) {
            return false;
        }
        return super.equals(uiGroup);
    }

    public interface UIGroupChangePayload {

        class GroupId implements UIGroup.UIGroupChangePayload {

            public final long groupId;

            GroupId(long groupId) {
                this.groupId = groupId;
            }
        }

        class EndpointId implements UIGroup.UIGroupChangePayload {

            public final long endpointId;

            EndpointId(long endpointId) {
                this.endpointId = endpointId;
            }
        }

        class GroupName implements UIGroup.UIGroupChangePayload {

            public final String groupName;

            GroupName(String groupName) {
                this.groupName = groupName;
            }
        }
    }
}
