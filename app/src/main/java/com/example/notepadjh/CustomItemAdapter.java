package com.example.notepadjh;

//Controller

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import java.util.List;

public class CustomItemAdapter extends ArrayAdapter<Note> {
    CardView cardView;
    EventManager eventManager;
    Note note;

    public CustomItemAdapter(Context context, List notes) {
        super(context, 0, notes);
    }

    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.board_item, parent, false);
        }

        note = getItem(position);

        eventManager = new EventManager(getContext());

        TextView tv = view.findViewById(R.id.text_note_title);
        cardView = (CardView) view.findViewById(R.id.cardview);

        tv.setText(note.getNoteTitle());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomItemAdapter custom = new CustomItemAdapter(getContext(), EventManager.notes);
                Intent intent = new Intent(getContext(), NoteActivity.class);

                getItem(position);
                String s = eventManager.readTitleContent(eventManager.getTitleFile(getItem(position)));
                String s2 = eventManager.readTextContent(eventManager.getTextFile(getItem(position)));
                intent.putExtra("getTitle", s);
                intent.putExtra("getText", s2);

                custom.getContext().startActivity(intent);
            }
        });

        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.setAlpha(0.4f);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setIcon(R.drawable.delete);

                builder.setMessage("Do you want to delete note with title: " + getItem(position).getNoteTitle().toUpperCase());

                builder.setTitle("Delete note?");

                builder.setCancelable(false);

                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    v.setAlpha(1);

                    dialog.cancel();
                });
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    eventManager.deleteNote(getItem(position));
                    remove(getItem(position));
                    v.setAlpha(1);

                });
                AlertDialog alertDialog = builder.create();

                alertDialog.show();

                return true;
            }
        });
        return view;
    }
}




