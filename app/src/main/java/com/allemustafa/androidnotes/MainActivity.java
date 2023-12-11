package com.allemustafa.androidnotes;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {
    private RecyclerView recyclerView;
    NotesAdapter mAdapter;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    public static final String TAG = "MainActivity";
    private final List<Note> notesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#EE2C2404")));
        recyclerView = findViewById(R.id.recycler);
        mAdapter = new NotesAdapter(notesList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleResult
        );
        loadFile();
    }

    public void handleResult(ActivityResult result) {
        if (result == null || result.getData() == null) {
            Log.d(TAG, "handleResult: .");
        }
        Intent data = result.getData();
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (data.hasExtra("title") && data.hasExtra("description")) {
                String title = data.getStringExtra("title");
                String description = data.getStringExtra("description");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-EEE_HHmmss");
                int pos = data.getIntExtra("position", -1);
                if (pos != -1) {
                    notesList.remove(pos);
                    mAdapter.notifyItemRemoved(pos);
                }
                Note newNote = new Note(title, description);
                notesList.add(0, newNote);
                mAdapter.notifyItemInserted(notesList.indexOf(newNote));
            }
        }
        else if (result.getResultCode() == Activity.RESULT_FIRST_USER){
            Toast.makeText(this, getString(R.string.noteNotSaved), Toast.LENGTH_SHORT).show();
        }
        saveNotes();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.infoIcon) {
            Intent intent = new Intent(this, AboutActivity.class);
            activityResultLauncher.launch(intent);
        }
        if (item.getItemId() == R.id.addIcon) {
            Intent intent = new Intent(this, EditNotes.class);
            activityResultLauncher.launch(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_menu, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        int pos = recyclerView.getChildLayoutPosition(view);
        Note nt = notesList.get(pos);
        Intent intent = new Intent(this, EditNotes.class);
        intent.putExtra("position", pos);
        intent.putExtra("title", nt.getTitle());
        intent.putExtra("description", nt.getDescription());
        activityResultLauncher.launch(intent);
    }

    @Override
    public boolean onLongClick(View view) {
        int pos = recyclerView.getChildLayoutPosition(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Android Notes");
        builder.setMessage("Delete Note:" + notesList.get(pos).getTitle());
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Note nt = notesList.remove(pos);
                mAdapter.notifyItemRemoved(pos);
                saveNotes();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
        return false;
    }

    private void loadFile() {
        Log.d(TAG, "loadFile: Loading JSON File");
        try {
            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONArray jsonArray = new JSONArray(sb.toString());
            notesList.clear();
            Note note;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                String desc = jsonObject.getString("description");
                String date = jsonObject.getString("date");
                note = new Note(title, desc, date);
                notesList.add(note);
                Log.d(TAG, "loadFile: " + note);
            }
            getSupportActionBar().setTitle("Android Notes (" + notesList.size() + ")");
        } catch (FileNotFoundException e) {
            Toast.makeText(this, getString(R.string.no_file), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveNotes() {
        Log.d(TAG, "saveProduct: Saving JSON File");
        try {
            FileOutputStream fos = getApplicationContext().
                    openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            PrintWriter printWriter = new PrintWriter(fos);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < notesList.size(); i++) {
                JSONObject jsonObject = notesList.get(i).toJSON();
                jsonArray.put(jsonObject);
            }
            getSupportActionBar().setTitle("Android Notes (" + notesList.size() + ")");
            Log.d(TAG, "saveProduct: " + jsonArray);
            printWriter.print(jsonArray);
            printWriter.close();
            fos.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}