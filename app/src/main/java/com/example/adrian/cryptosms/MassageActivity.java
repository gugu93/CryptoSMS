package com.example.adrian.cryptosms;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.util.Base64;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class MassageActivity extends ActionBarActivity {

    private EditText messageText;
    private Button cancelButton;
    private Button sendButton;
    private Button encrypteButton;
    private Button decryptButton;
    private String KEY="key phrase used for XOR-ing";
    public static final String DEFAULT_ENCODING="UTF-8";
    //static BASE64Encoder enc=new BASE64Encoder();
    //static BASE64Decoder dec=new BASE64Decoder();

    public static String base64encode(String text){
        try {
            //String rez = enc.encode( text.getBytes( DEFAULT_ENCODING ) );
            String rez = Base64.encodeToString(text.getBytes(DEFAULT_ENCODING),Base64.DEFAULT);
            return rez;
        }
        catch ( UnsupportedEncodingException e ) {
            return null;
        }
    }//base64encode

    public static String base64decode(String text){

        try {
            //return new String(dec.decodeBuffer( text ),DEFAULT_ENCODING);
            byte[] dec = Base64.decode(text,Base64.DEFAULT);
            return new String(dec,"UTF-8");
        }
        catch ( IOException e ) {
            return null;
        }

    }//base64decode

//    public static void main(String[] args){
//        String txt="some text to be encrypted" ;
//        String key="key phrase used for XOR-ing";
//        System.out.println(txt+" XOR-ed to: "+(txt=xorMessage( txt, key )));
//        String encoded=base64encode( txt );
//        System.out.println( " is encoded to: "+encoded+" and that is decoding to: "+ (txt=base64decode( encoded )));
//        System.out.print( "XOR-ing back to original: "+xorMessage( txt, key ) );
//
//    }

    public static String xorMessage(String message, String KEY){
        try {
            if (message==null || KEY==null ) return null;

            char[] keys=KEY.toCharArray();
            char[] mesg=message.toCharArray();

            int ml=mesg.length;
            int kl=keys.length;
            char[] newmsg=new char[ml];

            for (int i=0; i<ml; i++){
                newmsg[i]=(char)(mesg[i]^keys[i%kl]);
            }//for i
            mesg=null; keys=null;
            return new String(newmsg);
        }
        catch ( Exception e ) {
            return null;
        }
    }//xorMessage
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_massage);
        messageText= (EditText) this.findViewById(R.id.messageText);
        cancelButton = (Button) this.findViewById(R.id.back);
        sendButton = (Button) this.findViewById(R.id.send);
        encrypteButton = (Button) this.findViewById(R.id.encrypt);
        decryptButton = (Button) this.findViewById(R.id.decrypt);


        if(this.getIntent().getStringExtra("SMS_BODY")!=null){
            messageText.setText(this.getIntent().getStringExtra("SMS_BODY"));
            encrypteButton.setEnabled(false);
            sendButton.setText("Reply");
            sendButton.setEnabled(true);
            decryptButton.setEnabled(true);
        }

        decryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decryptButton.setEnabled(false);
                encrypteButton.setEnabled(true);
                String txtEncoded = messageText.getText().toString();
                String xor = base64decode(txtEncoded);
                String txt = xorMessage(xor, KEY);
                messageText.setText(txt);
            }
        });
        encrypteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decryptButton.setEnabled(true);
                encrypteButton.setEnabled(false);
                String txt = messageText.getText().toString();
                String xor = (txt=xorMessage( txt, KEY ));
                String encoded = base64encode(xor);
                messageText.setText(encoded);

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sendButton.getText()!="Reply") {
                    if (getIntent().getStringExtra("SMS_NUMBER") == null) {
                        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                        smsIntent.setType("vnd.android-dir/mms-sms");
                        smsIntent.putExtra("sms_body", messageText.getText().toString());
                        startActivity(smsIntent);
                    }
                    else{
                        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                        smsIntent.setType("vnd.android-dir/mms-sms");
                        smsIntent.putExtra("sms_body", messageText.getText().toString());
                        smsIntent.putExtra("address", getIntent().getStringExtra("SMS_NUMBER"));
                        startActivity(smsIntent);
                    }
                }
                else{
                    sendButton.setText("Send");
                    messageText.setText("");
                }
            }
        });



//        String txt="some text to be encrypted" ;
//
//
//        String frase1 = (txt+" XOR-ed to: "+(txt=xorMessage( txt, KEY )));
//        String encoded=base64encode( txt );
//        String frase2 = ( " is encoded to: "+encoded+" and that is decoding to: "+ (txt=base64decode( encoded )));
//        String frase3 = ( "XOR-ing back to original: "+xorMessage( txt, KEY ) );
//        messageText.setText(frase1+"-------"+frase2+"==========="+frase3);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_massage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
