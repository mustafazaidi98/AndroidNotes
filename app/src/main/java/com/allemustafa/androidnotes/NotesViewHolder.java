package com.allemustafa.androidnotes;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class NotesViewHolder extends RecyclerView.ViewHolder {

    TextView title;
    TextView description;
    TextView dateTime;

    NotesViewHolder(View view) {
        super(view);
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);
        dateTime = view.findViewById(R.id.datetime);
    }
}
