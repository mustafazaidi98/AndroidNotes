package com.allemustafa.androidnotes;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditNotes extends AppCompatActivity {
    EditText title;
    EditText description;
    int position = -1;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.saveitem) {
            String ti = title.getText().toString();
            String descrip = description.getText().toString();
            if (position != -1 && getIntent().getStringExtra(getString(R.string.Title)).equalsIgnoreCase(ti)
                    && getIntent().getStringExtra(getString(R.string.Description)).equalsIgnoreCase(descrip)) {
                finish();
            } else {
                Save();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void Save() {
        String ti = title.getText().toString();
        String descrip = description.getText().toString();
        if(ti.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.AndroidNotes);
            builder.setMessage(R.string.NoWihoutTitleMessage);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    setResult(Activity.RESULT_FIRST_USER);
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    return;
                }
            });
            Dialog dialog = builder.create();
            dialog.show();
        }
        else {
            Intent intent = new Intent();
            intent.putExtra("title", ti);
            intent.putExtra("description", descrip);
            intent.putExtra("position", position);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        String ti = title.getText().toString();
        String descrip = description.getText().toString();
        if (position != -1 && getIntent().getStringExtra(getString(R.string.Title)).equalsIgnoreCase(ti)
                && getIntent().getStringExtra(getString(R.string.Description)).equalsIgnoreCase(descrip)) {
            finish();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.AndroidNotes);
            builder.setMessage(R.string.SaveCurrentNote);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Save();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            Dialog dialog = builder.create();
            dialog.show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editmenu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#EE2C2404")));
        setContentView(R.layout.activity_edit_notes);
        title = findViewById(R.id.noteTitle);
        description = findViewById(R.id.description);
        position = getIntent().getIntExtra("position", -1);
        if (position != -1) {
            title.setText(getIntent().getStringExtra(getString(R.string.Title)));
            description.setText(getIntent().getStringExtra(getString(R.string.Description)));
        }
    }
}