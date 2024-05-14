package com.example.jmemo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddEditMemoActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextContent;
    private Button buttonSave;
    private MemoDatabaseHelper db;
    private int memoId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_memo);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);
        buttonSave = findViewById(R.id.buttonSavs);
        db = new MemoDatabaseHelper(this);

        memoId = getIntent().getIntExtra("MEMO_ID", -1);
        if (memoId != -1) {
            loadMemo(memoId);
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMemo();
            }
        });
    }

    private void loadMemo(int id) {
        Memo memo = db.getMemo(id);
        if (memo != null) {
            editTextTitle.setText(memo.getTitle());
            editTextContent.setText(memo.getContent());
        }
    }

    private void saveMemo() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();

        if (memoId == -1) {
            Memo newMemo = new Memo(0, title, content);
            db.addMemo(newMemo);
        } else {
            Memo updatedMemo = new Memo(memoId, title, content);
            db.updateMemo(updatedMemo);
        }
        finish();
    }
}
