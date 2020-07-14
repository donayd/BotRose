package com.innovati.cruzr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

public class ReaderPDF417 extends AppCompatActivity {

    public String barcode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader_pdf417);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {

        if(e.getAction()==KeyEvent.ACTION_DOWN){
            char pressedKey = (char) e.getUnicodeChar();
            barcode += pressedKey;
        }
        if (e.getAction()==KeyEvent.ACTION_DOWN && e.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            String[] splited = barcode.split("\\s+");
            verifyIdCard(splited[0]);
            barcode="";
        }

        return super.dispatchKeyEvent(e);
    }

    private void verifyIdCard(String cedula) {
        if( !cedula.isEmpty() ){
            try {
                Integer.valueOf(cedula);
                Intent intent = new Intent(ReaderPDF417.this, MainActivity.class);
                intent.putExtra("cedula", cedula);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Cedula invalida", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Cedula vacia", Toast.LENGTH_LONG).show();
        }
    }
}