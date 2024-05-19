package com.example.jmemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class FolderMemosActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MemoAdapter adapter;
    private MemoDatabaseHelper dbHelper;
    private List<Memo> memoList;
    private int folderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_memos);

        // Toolbar 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("폴더의 메모");

        dbHelper = new MemoDatabaseHelper(this);
        Intent intent = getIntent();
        folderId = intent.getIntExtra("folderId", -1);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab_add_memo);
        fab.setOnClickListener(view -> {
            Intent intent1 = new Intent(FolderMemosActivity.this, AddEditMemoActivity.class);
            intent1.putExtra("folderId", folderId);
            startActivity(intent1);
        });

        loadMemos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMemos();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadMemos() {
        memoList = dbHelper.getMemosByFolder(folderId);

        if (adapter == null) {
            adapter = new MemoAdapter(memoList, dbHelper, this);
            recyclerView.setAdapter(adapter);

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new MemoItemTouchHelperCallback(adapter));
            itemTouchHelper.attachToRecyclerView(recyclerView);

            adapter.setOnItemClickListener(position -> {
                Memo memo = memoList.get(position);
                Intent intent = new Intent(FolderMemosActivity.this, AddEditMemoActivity.class);
                intent.putExtra(AddEditMemoActivity.EXTRA_MEMO_ID, memo.getId());
                startActivity(intent);
            });
        } else {
            adapter.setMemoList(memoList);
            adapter.notifyDataSetChanged();
        }
    }
}
