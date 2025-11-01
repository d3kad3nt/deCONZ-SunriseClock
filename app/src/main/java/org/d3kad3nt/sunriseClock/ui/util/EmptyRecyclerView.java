package org.d3kad3nt.sunriseClock.ui.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Optional;

/**
 * This is a subclass of {@link RecyclerView} that adds the possibility to show a view when the
 * no other data is available.
 */
public class EmptyRecyclerView extends RecyclerView {

    private Optional<View> emptyView = Optional.empty();

    public EmptyRecyclerView(@NonNull final Context context) {
        super(context);
    }

    public EmptyRecyclerView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptyRecyclerView(
            @NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void updateEmptyViewVisibility() {
        if (emptyView.isPresent()) {
            if (getAdapter() == null || getAdapter().getItemCount() == 0) {
                emptyView.get().setVisibility(VISIBLE);
                EmptyRecyclerView.this.setVisibility(GONE);
            } else {
                emptyView.get().setVisibility(GONE);
                EmptyRecyclerView.this.setVisibility(VISIBLE);
            }
        }
    }

    final AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            updateEmptyViewVisibility();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            updateEmptyViewVisibility();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            updateEmptyViewVisibility();
        }
    };

    @Override
    public void setAdapter(Adapter adapter) {
        Adapter<?> oldAdapter = getAdapter();
        super.setAdapter(adapter);

        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }

        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
    }

    /**
     * Sets the view that will be shown when the adapter is empty.
     *
     * @param view The view to show.
     */
    public void setEmptyView(View view) {
        this.emptyView = Optional.of(view);
        updateEmptyViewVisibility();
    }
}
