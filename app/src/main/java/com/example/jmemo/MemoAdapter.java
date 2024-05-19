package com.example.jmemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoViewHolder> {
    private List<Memo> memoList;
    private final MemoDatabaseHelper dbHelper;
    private final Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public MemoAdapter(List<Memo> memoList, MemoDatabaseHelper dbHelper, Context context) {
        this.memoList = memoList;
        this.dbHelper = dbHelper;
        this.context = context;
    }

    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.memo_item, parent, false);
        return new MemoViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder holder, int position) {
        Memo memo = memoList.get(position);
        holder.title.setText(memo.getTitle());
        holder.content.setText(memo.getContent());
    }

    @Override
    public int getItemCount() {
        return memoList != null ? memoList.size() : 0;
    }

    public void setMemoList(List<Memo> memoList) {
        this.memoList = memoList;
    }

    public void showDeleteConfirmationDialog(final int position) {
        new AlertDialog.Builder(context)
                .setMessage("메모를 삭제하시겠습니까?")
                .setPositiveButton("네", (dialog, which) -> deleteItem(position))
                .setNegativeButton("아니오", (dialog, which) -> notifyItemChanged(position))
                .create()
                .show();
    }

    private void deleteItem(int position) {
        Memo memo = memoList.get(position);
        dbHelper.deleteMemo(memo);
        memoList.remove(position);
        notifyItemRemoved(position);
    }

    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(memoList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    public static class MemoViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView content;

        public MemoViewHolder(View view, final OnItemClickListener listener) {
            super(view);
            title = view.findViewById(R.id.memo_title);
            content = view.findViewById(R.id.memo_content);

            view.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
