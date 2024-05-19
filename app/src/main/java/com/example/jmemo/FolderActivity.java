package com.example.jmemo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class FolderActivity extends AppCompatActivity {
    private MemoDatabaseHelper dbHelper;
    private ListView listViewFolders;
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
        listViewFolders = findViewById(R.id.listViewFolders);

        FloatingActionButton fabAddFolder = findViewById(R.id.fab_add_folder);
        fabAddFolder.setOnClickListener(view -> showAddFolderDialog());

        listViewFolders.setOnItemClickListener((parent, view, position, id) -> {
            Folder folder = folderList.get(position);
            Intent intent = new Intent(FolderActivity.this, FolderMemosActivity.class);
            intent.putExtra("folderId", folder.getId());
            startActivity(intent);
        });

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

    private void loadFolders() {
        folderList = dbHelper.getAllFolders();
        ArrayAdapter<Folder> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, folderList);
        listViewFolders.setAdapter(adapter);
    }
}
