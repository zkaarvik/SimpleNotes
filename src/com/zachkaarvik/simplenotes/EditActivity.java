package com.zachkaarvik.simplenotes;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.EditText;

public class EditActivity extends Activity {

    //TODO: Remove temp variable
    private Entry entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        entry = intent.getParcelableExtra(MainActivity.ENTRY);

        //Retrieve full entry from shell passed through intent
        entry = MainActivity.dataSource.getEntryByID(entry.getId());

        //TODO: Set top bar to name of entry
        setTitle(entry.getName());

        //Set main text to content - get from database as we are usually coming to the activity being passed a shell
        EditText content = (EditText) findViewById(R.id.content);
        content.setText(entry.getContent());
    }

    protected void onPause() {
        super.onPause();

        EditText editText = (EditText) findViewById(R.id.content);

        entry.setContent(editText.getText().toString());

        MainActivity.dataSource.updateEntry(entry);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit, menu);
        return true;
    }

}
