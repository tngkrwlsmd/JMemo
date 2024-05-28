package com.example.jmemo;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class FolderItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private final FolderAdapter mAdapter;

    public FolderItemTouchHelperCallback(FolderAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        mAdapter.moveItem(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        mAdapter.deleteItem(position);
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.LEFT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }
}

