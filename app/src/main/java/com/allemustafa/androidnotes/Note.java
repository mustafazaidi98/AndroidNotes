package com.allemustafa.androidnotes;
import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Note implements Serializable{
    private final String title;
    private final String description;
    private final String date;
    private static int counter=1;
    public Note(){
        this.title="Notes"+counter;
        this.description = "Notes"+counter;
        SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd, hh:mm a");
        this.date = sdf.format(new Date());
        counter++;
    }
    public Note(String title, String description){
        this.title=title;
        this.description = description;
        SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd, hh:mm a");
        this.date = sdf.format(new Date());
        counter++;
    }

    public Note(String title, String desc, String date) {
        this.title=title;
        this.description = desc;
        this.date = date;
        counter++;
    }

    public String getTitle() {
        return title;
    }
    public String getDate() {
        return date;
    }
    public String getDescription() {
        return description;
    }
    public String getCompressedDescription() {
        if(description.length()>80)
            return description.substring(0, 80)+"...";
        return description;
    }
    public String toString() {
        return title + " (" + description+ "), " ;
    }

    public JSONObject toJSON() {

        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("title", getTitle());
            jsonObject.put("description", getDescription());
            jsonObject.put("date", getDate());

            return jsonObject;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
