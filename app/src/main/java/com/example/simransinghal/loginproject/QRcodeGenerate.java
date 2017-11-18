package com.example.simransinghal.loginproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class QRcodeGenerate extends AppCompatActivity {
    String child_id, child_name, vacc_id, vacc_name, due;
    TextView name, v_name, given_on;
    EditText due_on;
    Button generate;
    ImageView image;
    Map<String, String> map = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_generate);

        Intent i = getIntent();
        child_id = i.getStringExtra("child_id");
        child_name = i.getStringExtra("child_name");
        vacc_id = i.getStringExtra("vacc_id");
        vacc_name = i.getStringExtra("vacc_name");


        name = (TextView) findViewById(R.id.tv_name);
        v_name = (TextView) findViewById(R.id.vacc);
        given_on = (TextView) findViewById(R.id.tv_givenon);
        due_on = (EditText) findViewById(R.id.text4);
        generate = (Button) findViewById(R.id.gen_btn);
        image = (ImageView) findViewById(R.id.image);


        name.setText(child_name);
        v_name.setText(vacc_name);


        SimpleDateFormat sys = new SimpleDateFormat("dd/MM/yyyy");
        final String today = sys.format(new Date());
        //Date current = new Date();

        given_on.setText(today);


        due_on.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog mdate = new DatePickerDialog(QRcodeGenerate.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                mdate.getDatePicker().setMinDate(System.currentTimeMillis());
                mdate.show();
            }
        });


        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                due = due_on.getText().toString();

                map.put("child_id", child_id);
                map.put("vaccine_id", vacc_id);
                map.put("given_on", today);
                map.put("due_date", due);
                JSONObject obj = new JSONObject(map);
                Log.d("generate", obj.toString());

                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(obj.toString(), BarcodeFormat.QR_CODE, 200, 200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    image.setImageBitmap(bitmap);


                } catch (WriterException e) {
                    e.printStackTrace();

                }
            }
        });


    }


    //**********************Date Picker****************
    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            view.setMinDate(System.currentTimeMillis());
            updateLabel();
        }

    };

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        due_on.setText(sdf.format(myCalendar.getTime()));
    }

}
