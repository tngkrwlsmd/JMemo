package com.example.jmemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MemoAdapter adapter;
    private MemoDatabaseHelper dbHelper;
    private List<Memo> memoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("메모 목록");

        dbHelper = new MemoDatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab_add_memo);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddEditMemoActivity.class);
            startActivity(intent);
        });

        loadMemos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMemos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_folders) {
            Intent intent = new Intent(MainActivity.this, FolderActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.action_search) {
            Intent intent = new Intent(MainActivity.this, SearchMemoActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadMemos() {
        memoList = dbHelper.getAllMemos();

        if (adapter == null) {
            adapter = new MemoAdapter(memoList, dbHelper, this);
            recyclerView.setAdapter(adapter);

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new MemoItemTouchHelperCallback(adapter));
            itemTouchHelper.attachToRecyclerView(recyclerView);

            adapter.setOnItemClickListener(position -> {
                Memo memo = memoList.get(position);
                Intent intent = new Intent(MainActivity.this, AddEditMemoActivity.class);
                intent.putExtra(AddEditMemoActivity.EXTRA_MEMO_ID, memo.getId());
                startActivity(intent);
            });
        } else {
            adapter.setMemoList(memoList);
            adapter.notifyDataSetChanged();
        }
    }
}
