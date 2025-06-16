package com.example.lehoanggiang.traicaythanhsang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lehoanggiang.traicaythanhsang.R;

public class ChuyenKhoanActivity extends AppCompatActivity {

    Button btnXacNhan;
    ImageView imgQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuyen_khoan);

        btnXacNhan = (Button) findViewById(R.id.btnXacNhan);
        imgQR = (ImageView) findViewById(R.id.imgQR);

        // Gán QR từ file drawable
        imgQR.setImageResource(R.drawable.qr);

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Xác nhận xong thì quay về màn chính
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Thanh toán thành công. Cảm ơn bạn!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
