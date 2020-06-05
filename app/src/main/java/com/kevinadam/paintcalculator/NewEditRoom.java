package com.kevinadam.paintcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewEditRoom extends AppCompatActivity {
    public static final String EXTRA_ID = "com.kevinadam.paintcalculator.EXTRA_ID";
    public static final String EXTRA_RNAME = "com.kevinadam.paintcalculator.EXTRA_RNAME";
    public static final String EXTRA_PANJANG = "com.kevinadam.paintcalculator.EXTRA_PANJANG";
    public static final String EXTRA_LEBAR= "com.kevinadam.paintcalculator.EXTRA_LEBAR";
    public static final String EXTRA_TINGGI = "com.kevinadam.paintcalculator.EXTRA_TINGGI";

    private EditText r_name;
    private EditText r_panjang;
    private EditText r_tinggi;
    private EditText r_lebar;

    Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_room);

        r_name = findViewById(R.id.r_name);
        r_panjang = findViewById(R.id.r_panjang);
        r_lebar = findViewById(R.id.r_lebar);
        r_tinggi = findViewById(R.id.r_tinggi);

        btn_save = findViewById(R.id.btn_add);


        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)){
            setTitle("Ubah Ruangan");
            r_name.setText(intent.getStringExtra(EXTRA_RNAME));
            r_panjang.setText(intent.getStringExtra(EXTRA_PANJANG));
            r_lebar.setText(intent.getStringExtra(EXTRA_LEBAR));
            r_tinggi.setText(intent.getStringExtra(EXTRA_TINGGI));

            btn_save.setText("Ubah Data");

        }else{
            setTitle("Tambah Baru");
        }


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_data();
            }
        });
    }

    private void save_data(){
        String d_panjang = r_panjang.getText().toString();
        String d_tinggi = r_tinggi.getText().toString();
        String d_lebar = r_lebar.getText().toString();
        String d_rname = r_name.getText().toString();

        if (d_panjang.isEmpty() || d_lebar.isEmpty() || d_tinggi.isEmpty() || d_rname.isEmpty()){
            Toast.makeText(this, "Isi Panjang, lebar dan Tinggi", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_PANJANG, d_panjang);
        data.putExtra(EXTRA_LEBAR, d_lebar);
        data.putExtra(EXTRA_TINGGI, d_tinggi);
        data.putExtra(EXTRA_RNAME, d_rname);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);

        if (id != -1){
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }
}
