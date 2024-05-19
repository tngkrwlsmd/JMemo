package com.example.jmemo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class AddEditMemoActivity extends AppCompatActivity {
    public static final String EXTRA_MEMO_ID = "com.example.jmemo.EXTRA_MEMO_ID";

    private MemoDatabaseHelper dbHelper;
    private Memo currentMemo;
    private EditText editTextTitle;
    private EditText editTextContent;
    private Spinner spinnerFolders;
    private List<Folder> folderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_memo);

        // Toolbar 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new MemoDatabaseHelper(this);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextContent = findViewById(R.id.edit_text_content);
        Button saveButton = findViewById(R.id.button_save);
        Button deleteButton = findViewById(R.id.btn_delete);
        Button addFolderButton = findViewById(R.id.btn_add_folder);
        spinnerFolders = findViewById(R.id.spinner_folders);

        loadFolders();

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_MEMO_ID)) {
            setTitle("메모 수정");
            int memoId = intent.getIntExtra(EXTRA_MEMO_ID, -1);
            currentMemo = dbHelper.getMemo(memoId);
            editTextTitle.setText(currentMemo.getTitle());
            editTextContent.setText(currentMemo.getContent());
            spinnerFolders.setSelection(getFolderPositionById(currentMemo.getFolderId()));
        } else {
            setTitle("새 메모");
            currentMemo = null;
            spinnerFolders.setSelection(0); // 기본값을 "없음"으로 설정
        }

        saveButton.setOnClickListener(v -> saveMemo());

        deleteButton.setOnClickListener(v -> {
            if (currentMemo == null) showValidationDialog();
            else showDeleteConfirmationDialog();
        });

        addFolderButton.setOnClickListener(v -> showAddFolderDialog());
    }

    private void loadFolders() {
        folderList = dbHelper.getAllFolders();
        List<String> folderNames = new ArrayList<>();
        folderNames.add("없음"); // 기본값 추가
        for (Folder folder : folderList) {
            folderNames.add(folder.getName());
        }
        ArrayAdapter<String> folderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, folderNames);
        folderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFolders.setAdapter(folderAdapter);
    }

    private int getFolderPositionById(int folderId) {
        if (folderId == -1) return 0; // "없음" 위치 반환
        for (int i = 0; i < folderList.size(); i++) {
            if (folderList.get(i).getId() == folderId) {
                return i + 1; // "없음"을 고려하여 인덱스 조정
            }
        }
        return 0;
    }

    private void saveMemo() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();
        int folderPosition = spinnerFolders.getSelectedItemPosition();
        int folderId = (folderPosition == 0) ? -1 : folderList.get(folderPosition - 1).getId(); // "없음" 처리

        if (title.isEmpty() || content.isEmpty()) {
            showValidationDialog();
            return;
        }

        if (currentMemo == null) {
            // 새로운 메모 추가
            Memo newMemo = new Memo(title, content, folderId);
            dbHelper.addMemo(newMemo);
        } else {
            // 기존 메모 업데이트
            currentMemo.setTitle(title);
            currentMemo.setContent(content);
            currentMemo.setFolderId(folderId);
            dbHelper.updateMemo(currentMemo);
        }
        finish();
    }

    private void showValidationDialog() {
        new AlertDialog.Builder(this)
                .setMessage("제목과 내용을 모두 입력해 주세요.")
                .setPositiveButton("확인", (dialog, which) -> {
                    // 확인 버튼 클릭 시 수행할 작업
                })
                .create()
                .show();
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setMessage("메모를 삭제하시겠습니까?")
                .setPositiveButton("네", (dialog, which) -> deleteCurrentMemo())
                .setNegativeButton("아니오", null)
                .create()
                .show();
    }

    private void deleteCurrentMemo() {
        if (currentMemo != null) {
            dbHelper.deleteMemo(currentMemo);
            finish();
        }
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
}
