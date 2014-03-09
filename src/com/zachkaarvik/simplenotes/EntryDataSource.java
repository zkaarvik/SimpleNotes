package com.zachkaarvik.simplenotes;

/**
 * Created by Zach on 28/01/14.
 */
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class EntryDataSource {

    // Database fields
    private SQLiteDatabase database;
    private EntryDB dbHelper;
    private String[] allColumns = {
            EntryDB.COLUMN_ID,
            EntryDB.COLUMN_NAME,
            EntryDB.COLUMN_CONTENT};

    public EntryDataSource(Context context) {
        dbHelper = new EntryDB(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Entry createEntry(String name) {
        ContentValues values = new ContentValues();
        values.put(EntryDB.COLUMN_NAME, name);
        values.putNull(EntryDB.COLUMN_CONTENT);

        long insertId = database.insert(EntryDB.TABLE_ENTRIES, null,
                values);

        Cursor cursor = database.query(EntryDB.TABLE_ENTRIES,
                allColumns, EntryDB.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Entry newEntry = cursorToEntry(cursor);
        cursor.close();
        return newEntry;
    }

    public void updateEntry(Entry entry) {
        ContentValues newValues = new ContentValues();

        long id = entry.getId();
        String name = entry.getName();
        String content = entry.getContent();

        newValues.put(EntryDB.COLUMN_NAME, name);
        newValues.put(EntryDB.COLUMN_CONTENT, content);

        database.update(EntryDB.TABLE_ENTRIES, newValues, EntryDB.COLUMN_ID + " = " + id, null);
    }

    public void deleteEntry(Entry entry) {
        long id = entry.getId();
        System.out.println("Entry deleted with id: " + id);
        database.delete(EntryDB.TABLE_ENTRIES, EntryDB.COLUMN_ID
                + " = " + id, null);
    }

    //Gets entries such that the newest will be at the top
    public List<Entry> getAllShells() {
        List<Entry> entryShells = new ArrayList<Entry>();

        String[] columnNames = {EntryDB.COLUMN_ID, EntryDB.COLUMN_NAME};
        Cursor cursor = database.query(EntryDB.TABLE_ENTRIES, columnNames , null, null, null, null, EntryDB.COLUMN_ID + " DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Entry currentShell = cursorToShell(cursor);
            entryShells.add(currentShell);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return entryShells;
    }

    public Entry getEntryByID(long id) {
        String[] columnNames = {EntryDB.COLUMN_ID, EntryDB.COLUMN_NAME, EntryDB.COLUMN_CONTENT};

        Cursor cursor = database.query(EntryDB.TABLE_ENTRIES, columnNames, EntryDB.COLUMN_ID + " = " + id, null, null, null, null);
        cursor.moveToFirst();

        Entry entry = cursorToEntry(cursor);

        cursor.close();

        return  entry;
    }

    private Entry cursorToEntry(Cursor cursor) {
        Entry entry = new Entry();
        entry.setId(cursor.getLong(0));
        entry.setName(cursor.getString(1));
        entry.setContent( (cursor.getString(2) == null) ? "" : cursor.getString(2) );
        return entry;
    }
    private Entry cursorToShell(Cursor cursor) {
        Entry entry = new Entry(cursor.getLong(0), cursor.getString(1));
        return entry;
    }
}