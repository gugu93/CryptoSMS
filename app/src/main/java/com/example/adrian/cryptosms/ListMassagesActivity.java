package com.example.adrian.cryptosms;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
public class ListMassagesActivity extends Activity implements OnClickListener {

    // GUI Widget
    Button btnSent, btnInbox, btnDraft;
    TextView lblMsg;
    TextView lblNo;
    ListView lvMsg;

    // Cursor Adapter
    SimpleCursorAdapter adapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_massages);

        // Init GUI Widget
        btnInbox = (Button) findViewById(R.id.btnInbox);
        btnInbox.setOnClickListener(this);

        btnSent = (Button) findViewById(R.id.btnSentBox);
        btnSent.setOnClickListener(this);

        btnDraft = (Button) findViewById(R.id.btnDraft);
        btnDraft.setOnClickListener(this);

        lvMsg = (ListView) findViewById(R.id.lvMsg);

    }

    @Override
    public void onClick(View v) {



        if (v == btnInbox) {

            // Create Inbox box URI
            Uri inboxURI = Uri.parse("content://sms/inbox");

            // List required columns
            String[] reqCols = new String[] { "_id", "address", "body" };

            // Get Content Resolver object, which will deal with Content
            // Provider
            ContentResolver cr = getContentResolver();

            // Fetch Inbox SMS Message from Built-in Content Provider
            Cursor c = cr.query(inboxURI, reqCols, null, null, null);

            // Attached Cursor with adapter and display in listview
            adapter = new SimpleCursorAdapter(this, R.layout.row, c,
                    new String[] { "body", "address" }, new int[] {
                    R.id.lblMsg, R.id.lblNumber });

            lvMsg.setAdapter(adapter);



        }

        if (v == btnSent) {

            // Create Sent box URI
            Uri sentURI = Uri.parse("content://sms/sent");

            // List required columns
            String[] reqCols = new String[] { "_id", "address", "body" };

            // Get Content Resolver object, which will deal with Content
            // Provider
            ContentResolver cr = getContentResolver();

            // Fetch Sent SMS Message from Built-in Content Provider
            Cursor c = cr.query(sentURI, reqCols, null, null, null);

            // Attached Cursor with adapter and display in listview
            adapter = new SimpleCursorAdapter(this, R.layout.row, c,
                    new String[] { "body", "address" }, new int[] {
                    R.id.lblMsg, R.id.lblNumber });
            lvMsg.setAdapter(adapter);

            lvMsg.setOnItemClickListener(new OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(ListMassagesActivity.this, MassageActivity.class);
                    Cursor cursor = ((SimpleCursorAdapter) adapter).getCursor();
                    cursor.moveToPosition(position);
                    String body = cursor.getString(2);
                    String number = cursor.getString(1);
                    //long categoryId = cursor.getLong(cursor.getColumnIndex(CategoryDataHelper.ID));
                    //Toast.makeText(getApplicationContext(),body,Toast.LENGTH_LONG).show();
                    i.putExtra("SMS_BODY", body);
                    i.putExtra("SMS_NUMBER", number);
                    startActivity(i);
                }
            });
        }

        if (v == btnDraft) {
            // Create Draft box URI
            Uri draftURI = Uri.parse("content://sms/draft");

            // List required columns
            String[] reqCols = new String[] { "_id", "address", "body" };

            // Get Content Resolver object, which will deal with Content
            // Provider
            ContentResolver cr = getContentResolver();

            // Fetch Sent SMS Message from Built-in Content Provider
            Cursor c = cr.query(draftURI, reqCols, null, null, null);

            // Attached Cursor with adapter and display in listview
            adapter = new SimpleCursorAdapter(this, R.layout.row, c,
                    new String[] { "body", "address" }, new int[] {
                    R.id.lblMsg, R.id.lblNumber });
            lvMsg.setAdapter(adapter);

        }

    }
}