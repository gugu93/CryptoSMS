package com.example.adrian.cryptosms;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

    public static String xorMessage(String message, String key){
        try {
            if (message==null || key==null ) return null;

            char[] keys=key.toCharArray();
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
        String txt="some text to be encrypted" ;
        String key="key phrase used for XOR-ing";
        String frase1 = (txt+" XOR-ed to: "+(txt=xorMessage( txt, key )));
        String encoded=base64encode( txt );
        String frase2 = ( " is encoded to: "+encoded+" and that is decoding to: "+ (txt=base64decode( encoded )));
        String frase3 = ( "XOR-ing back to original: "+xorMessage( txt, key ) );
        messageText.setText(frase1+"-------"+frase2+"==========="+frase3);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_massage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
