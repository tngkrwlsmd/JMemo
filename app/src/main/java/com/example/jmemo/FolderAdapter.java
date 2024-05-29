package com.example.jmemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {
    private final List<Folder> mFolderList;
    private final MemoDatabaseHelper mDatabaseHelper;
    private final Context mContext;

    public FolderAdapter(List<Folder> folderList, Context context) {
        mFolderList = folderList;
        mContext = context;
        mDatabaseHelper = new MemoDatabaseHelper(context);
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_item, parent, false);
        return new FolderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        Folder currentFolder = mFolderList.get(position);
        holder.folderNameTextView.setText(currentFolder.getName());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, FolderMemosActivity.class);
            intent.putExtra("folderId", currentFolder.getId());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mFolderList.size();
    }

    public void deleteItem(final int position) {
        new AlertDialog.Builder(mContext)
                .setTitle("폴더 삭제")
                .setMessage("폴더를 삭제하시겠습니까? 폴더 안의 메모는 삭제되지 않습니다.")
                .setPositiveButton("삭제", (dialog, which) -> {
                    Folder folder = mFolderList.get(position);
                    mDatabaseHelper.deleteFolder(folder);
                    mFolderList.remove(position);
                    notifyItemRemoved(position);
                })
                .setNegativeButton("취소", (dialog, which) -> notifyItemChanged(position))
                .create()
                .show();
    }

    public void moveItem(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mFolderList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mFolderList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        updateFolderOrder();
    }

    public void updateFolderOrder() {
        for (int i = 0; i < mFolderList.size(); i++) {
            Folder folder = mFolderList.get(i);
            folder.setOrder(i);
            mDatabaseHelper.updateFolderOrder(folder);
        }
    }

    public static class FolderViewHolder extends RecyclerView.ViewHolder {
        public TextView folderNameTextView;

        public FolderViewHolder(View itemView) {
            super(itemView);
            folderNameTextView = itemView.findViewById(R.id.folder_name);
        }
    }
}
