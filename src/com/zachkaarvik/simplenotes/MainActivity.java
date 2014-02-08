package com.zachkaarvik.simplenotes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends Activity {

    public final static String ENTRY = "com.zacharykaarvik.simplenotes.Entry";

    protected static EntryDataSource dataSource;

    private ArrayAdapter<Entry> mainAdapter;
    private static List<Entry> allShells;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        dataSource = new EntryDataSource(this);
        dataSource.open();


        allShells = dataSource.getAllShells();

        ListView mainList = (ListView) findViewById(R.id.mainList);
        mainAdapter = new ArrayAdapter<Entry>(this, android.R.layout.simple_list_item_1, android.R.id.text1, allShells);
        mainList.setAdapter(mainAdapter);

        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Entry selection = (Entry) parent.getItemAtPosition(position);

                Intent intent = new Intent(getBaseContext(), EditActivity.class);
                intent.putExtra(ENTRY, selection);

                startActivity(intent);
            }
        });
        mainList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Entry selection = (Entry) parent.getItemAtPosition(position);

                //----> Building and displaying the delete dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.dialog_message);
                builder.setPositiveButton(R.string.dialog_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dataSource.deleteEntry(selection);
                        allShells.remove(allShells.indexOf(selection));
                        mainAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.create().show();
                //<----- End delete dialog

                return true;
            }
        });

	}

    @Override
    protected void onResume(){
        super.onResume();

        //Clear name field
        EditText nameField = (EditText) findViewById(R.id.nameField);
        nameField.setText("");
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    @Override
    public void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }

    /*Called when New button is pressed
    * Pass information to create new DB entry
    * Then proceed to edit view*/
    public void createNew(View view) {
        Intent intent = new Intent(this, EditActivity.class);

        //Gathering text from field to add to intent
        EditText nameField = (EditText) findViewById(R.id.nameField);

        if(nameField.getText().length() == 0){
            //No text in the name field, make toast
            Context context = getApplicationContext();
            CharSequence errorText = "Please give your note a name";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, errorText, duration);
            toast.show();

            return;
        }

        Entry newEntry = dataSource.createEntry(nameField.getText().toString());
        allShells.add(0, newEntry.makeShell());
        mainAdapter.notifyDataSetChanged();

        intent.putExtra(ENTRY, newEntry);

        //Send intent and start second activity
        startActivity(intent);
    }
}
