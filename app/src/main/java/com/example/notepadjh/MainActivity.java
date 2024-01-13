package com.example.notepadjh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

// Arkitektur - MVC
// Controller
public class MainActivity extends AppCompatActivity {
    GridView gridView;
    ConstraintLayout headLayout;
    TextView boardTitle;
    EventManager loadManager;
    CustomItemAdapter adapter;
    SwitchCompat switchLight;
    TextView boardModeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadManager = new EventManager(this);
        FloatingActionButton addNoteButton = findViewById(R.id.fab_add_note);
        switchLight = findViewById(R.id.switch_light);
        headLayout = findViewById(R.id.head_layout);
        boardTitle = findViewById(R.id.textView_board_title);
        boardModeText = findViewById(R.id.board_mode_text);
        gridView = findViewById(R.id.gridview);
        adapter = new CustomItemAdapter(this, EventManager.notes);

        gridView.setAdapter(adapter);

        loadManager.loadNoteFromFile();

        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                startActivity(intent);
            }
        });

        switchLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchLight.isChecked()) {
                    switchLight.setChecked(true);
                    headLayout.setBackgroundColor(getResources().getColor(R.color.white, null));
                    boardTitle.setTextColor(getResources().getColor(R.color.background, null));
                    boardModeText.setTextColor(getResources().getColor(R.color.background, null));

                } else if (!switchLight.isChecked()) {
                    switchLight.setChecked(false);
                    headLayout.setBackgroundColor(getResources().getColor(R.color.background, null));
                    boardTitle.setTextColor(getResources().getColor(R.color.white, null));
                    boardModeText.setTextColor(getResources().getColor(R.color.white, null));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}