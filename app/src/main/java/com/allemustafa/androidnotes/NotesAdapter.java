package com.allemustafa.androidnotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder> {
    private final List<Note> noteList;
    private final MainActivity mainact;
    public NotesAdapter(List<Note> notes, MainActivity mainact){
        noteList=notes;
        this.mainact = mainact;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_list_view, parent, false);
        itemView.setOnClickListener(mainact);
        itemView.setOnLongClickListener(mainact);

        return new NotesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.title.setText(note.getTitle());
        holder.description.setText(note.getCompressedDescription());
        holder.dateTime.setText(note.getDate());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
