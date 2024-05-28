package com.example.jmemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FolderActivity extends AppCompatActivity {
    private MemoDatabaseHelper dbHelper;
    private FolderAdapter mAdapter;
    private List<Folder> folderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        // Toolbar 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("폴더 관리");

        dbHelper = new MemoDatabaseHelper(this);
        RecyclerView recyclerViewFolders = findViewById(R.id.recycler_view_folders);
        recyclerViewFolders.setLayoutManager(new LinearLayoutManager(this));

        folderList = new ArrayList<>();
        mAdapter = new FolderAdapter(folderList, this);
        recyclerViewFolders.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new FolderItemTouchHelperCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerViewFolders);

        FloatingActionButton fabAddFolder = findViewById(R.id.fab_add_folder);
        fabAddFolder.setOnClickListener(view -> showAddFolderDialog());

        loadFolders();
    }

    private void showAddFolderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("새 폴더 추가");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("추가", (dialog, which) -> {
            String folderName = input.getText().toString().trim();
            if (!folderName.isEmpty()) {
                dbHelper.addFolder(new Folder(folderName));
                loadFolders();
            }
        });

        builder.setNegativeButton("취소", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadFolders() {
        folderList.clear();
        folderList.addAll(dbHelper.getAllFolders());
        mAdapter.notifyDataSetChanged();
    }
}
