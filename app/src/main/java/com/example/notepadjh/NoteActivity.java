package com.example.notepadjh;
// Controller

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class NoteActivity extends AppCompatActivity {
    EditText editTitle;
    EditText editNoteText;
    EventManager saveManager;
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Toolbar toolbar = findViewById(R.id.toolbar_menu);
        setSupportActionBar(toolbar);

        editTitle = findViewById(R.id.et_title);
        editNoteText = findViewById(R.id.et_note_text);

        // Vid edit, så hämtas objektets text via intent som skapas.
        Intent intent = getIntent();
        if (intent != null) {
            String setTheNewTitle = intent.getStringExtra("getTitle");
            String setTheNewText = intent.getStringExtra("getText");
            editTitle.setText(setTheNewTitle);
            editNoteText.setText(setTheNewText);
        }
        saveManager = new EventManager(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_save) {
            String title = editTitle.getText().toString();
            String text = editNoteText.getText().toString();
            note = new Note(title, text);

            if (!note.getNoteTitle().isEmpty()) {
                saveManager.saveNoteData(note);
                finish();

            } else {
                Toast.makeText(this, "Can't save with empty title...", Toast.LENGTH_SHORT).show();
            }
        }
        if (item.getItemId() == R.id.item_back) {
            finish();
        }
        return true;
    }
}
