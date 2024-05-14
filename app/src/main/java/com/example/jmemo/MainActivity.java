package com.example.jmemo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listViewMemos;
    private ArrayAdapter<Memo> memoAdapter;
    private MemoDatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewMemos = findViewById(R.id.listViewMemos);
        db = new MemoDatabaseHelper(this);

        loadMemos();

        FloatingActionButton fab = findViewById(R.id.fabAddMemo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditMemoActivity.class);
                startActivity(intent);
            }
        });

        listViewMemos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Memo selectedMemo = (Memo) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, AddEditMemoActivity.class);
                intent.putExtra("MEMO_ID", selectedMemo.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMemos();
    }

    private void loadMemos() {
        List<Memo> memos = db.getAllMemos();
        memoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, memos);
        listViewMemos.setAdapter(memoAdapter);
    }
}
