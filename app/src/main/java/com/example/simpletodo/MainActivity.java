package com.example.simpletodo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //declaring state objects, will be null before onCreate is called
   ArrayList<String> items;
   ArrayAdapter<String> itemsAdapter;
   ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //declare and initialize everything
        lvItems = (ListView) findViewById(R.id.lvItems);
        readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        //wire adapter to view
        lvItems.setAdapter(itemsAdapter);

        // setup the listener on creation
        setupListViewListener();
    }

    public void onAddItem(View v) {
        //make reference for inputted item
        EditText etNewitem = (EditText) findViewById(R.id.etNewItem);
        //make inputted information into a string
        String itemText = etNewitem.getText().toString();
        //add item into the list
        itemsAdapter.add(itemText);
        writeItems();
        //clear box
        etNewitem.setText(" ");

        //display notification
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
    }

    //remove items
    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                Log.i("MainActivity", "Removed item" + position);
                return true;
            }
        });
    }

    //returns file with stored data
    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    private void readItems() {
        try {
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            //print error
            e.printStackTrace();
            items = new ArrayList<>();
        }
    }

    //write items to filesystem
    private void writeItems() {
        try {
            //save list as text file
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            //print error
            e.printStackTrace();
        }
    }
}
