package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // obtain a reference to the ListView created with the layout
        lvItems = (ListView) findViewById(R.id.lvItems);

        // initialize the items list
        // items = new ArrayList<>();
        readItems();

        // initialize the adapter using the items list
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);

        // wire the adapter to the view
        lvItems.setAdapter(itemsAdapter);

//         add some mock items to the list
//        items.add("First todo item");
//        items.add("Second todo item");

        // setup the listener on creation
        setupListViewListener();
    }

    public void onAddItem(View v) {

        // obtain a reference to the EditText created with the layout
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);

        // grab the EditText's content as a String
        String itemText = etNewItem.getText().toString();

        // add the item to the list via the adapter
        itemsAdapter.add(itemText);

        // store the updated list
        writeItems();

        // clear the EditText by setting it to an empty String
        etNewItem.setText("");

        // Display a notif to the user.
        String message = "\"" + itemText + "\" was added to the list.";
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    private void setupListViewListener() {
        // set the ListView's itemLongClickListener
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // remove the item in the list at the index given by position
                items.remove(position);

                // notify the adapter that the underlying dataset changed
                itemsAdapter.notifyDataSetChanged();

                // store the updated list
                writeItems();

                Log.i("MainActivity", "Removed item " + position);

                // return true to tell the framework that the long click was consumed
                return true;
            }
        });
    }


    // returns the file in which the data is stored
    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    // read the items from the file system
    private void readItems() {
        try {
            // create the array using the content in the file
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            // print the error to the console
            e.printStackTrace();
            // just load an empty list
            items = new ArrayList<>();
        }
    }

    // write the items to the filesystem
    private void writeItems() {
        try {
            // save the item list as a line-delimited text file
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            // print the error to the console
            e.printStackTrace();
        }
    }


}
