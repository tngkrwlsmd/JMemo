package com.example.jmemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SearchMemoActivity extends AppCompatActivity {

    private MemoAdapter memoAdapter;
    private List<Memo> memoList;
    private List<Memo> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_memo);

        MemoDatabaseHelper dbHelper = new MemoDatabaseHelper(this);
        EditText editTextSearch = findViewById(R.id.editTextSearch);
        RecyclerView recyclerViewResults = findViewById(R.id.recyclerViewResults);

        memoList = dbHelper.getAllMemos();
        filteredList = new ArrayList<>(memoList);
        memoAdapter = new MemoAdapter(filteredList, dbHelper, this);

        recyclerViewResults.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewResults.setAdapter(memoAdapter);

        memoAdapter.setOnItemClickListener(position -> {
            Memo memo = memoList.get(position);
            Intent intent = new Intent(SearchMemoActivity.this, AddEditMemoActivity.class);
            intent.putExtra(AddEditMemoActivity.EXTRA_MEMO_ID, memo.getId());
            startActivity(intent);
        });

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterMemos(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void filterMemos(String query) {
        filteredList.clear();
        for (Memo memo : memoList) {
            if (memo.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    memo.getContent().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(memo);
            }
        }
        memoAdapter.notifyDataSetChanged();
    }
}
