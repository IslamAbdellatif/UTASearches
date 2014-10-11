package com.example.abdellatif.islam.utasearches;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;


public class MyActivity extends Activity {

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


        loadNames();

        //handle list view selected item
        ListView listView = (ListView) findViewById(R.id. listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //get the search tag. the clicked item
                final String tag = (String) parent.getItemAtPosition(position);

                //get the search query from the shared preferences
                SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);

                //get the value from the shared preferences
                String query = sharedPreferences.getString(tag,"");

                // show the web browser.
                String utaUrl = "http://www.uta.edu/search/?q=" + query;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(utaUrl));
                startActivity(browserIntent);



            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final String tag = (String) parent.getItemAtPosition(position);

                final String choices[] = {"Share","Edit","Delete"};


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Share, Edit, or Delete the search tagged as \""+tag+"\"")
                        .setItems(choices, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (choices[which].toString().equals("Share")) {

                                    AlertDialog.Builder shareBulder = new AlertDialog.Builder(context);
                                    shareBulder.setTitle("Share search to");
                                    final String sharechoices[] = {"Email", "Messaging"};
                                    shareBulder.setItems(sharechoices, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });

                                    AlertDialog alertShareDialog = shareBulder.create();
                                    // show it
                                    alertShareDialog.show();

                                } else if (choices[which].toString().equals("Edit")) {
                                    EditText textLine2 = (EditText) findViewById(R.id.editText2);
                                    textLine2.requestFocus();

                                } else {
                                    // this is the Delete button clicked
                                    // show the user another dialog to confirm the delete
                                    AlertDialog.Builder confirmBulder = new AlertDialog.Builder(context);
                                    confirmBulder.setTitle("Are you sure you want to delete\"" + tag + "\"")
                                            .setPositiveButton("Yes",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog,
                                                                            int id) {
                                                            // if this button is clicked, Delete
                                                            // the tag and its query from the shared preferences.
                                                            SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                            editor.remove(tag);

                                                            editor.commit();

                                                            loadNames();
                                                        }
                                                    })
                                            .setNegativeButton("No",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog,
                                                                            int id) {
                                                            // if this button is clicked, just close
                                                            // the dialog box and do nothing
                                                            dialog.cancel();
                                                        }
                                                    });
                                    AlertDialog alertDeleteDialog = confirmBulder.create();
                                    // show it
                                    alertDeleteDialog.show();
                                }
                            }
                        })

                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        // if this button is clicked, just close
                                        // the dialog box and do nothing
                                        dialog.cancel();
                                    }
                                });

                builder.create().show();





                return true;


            }
        });
    }


    public  void saveButtonClick(View view)
    {
        EditText textLine2 = (EditText) findViewById(R.id.editText2); // this is the query text
        String query = String.valueOf(textLine2.getText());

        EditText textLine3 = (EditText) findViewById(R.id.editText3);
        String tag = String.valueOf(textLine3.getText());

        // get the shared preferences object
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        // get the editor so we can add values to the sharedpreferences object
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // add the search query to the shared preferences
        editor.putString(tag, query);

        //store in the memory/shared perferences
        editor.commit();
//this is to refresh the list
        loadNames();

        // hide the keyboard.
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private void loadNames() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);

        final ArrayList list = new ArrayList<String>();
        list.addAll(sharedPreferences.getAll().keySet());

        Collections.sort(list, String.CASE_INSENSITIVE_ORDER);

        //a list of data. to popular a listview
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, list);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
