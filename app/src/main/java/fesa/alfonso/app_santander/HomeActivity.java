package fesa.alfonso.app_santander;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    TextView tvSaldo;
    Button btnTransferir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvSaldo = findViewById(R.id.tvSaldo);
        btnTransferir = findViewById(R.id.btnTransferir);

        String correo = getIntent().getStringExtra("correo");

        if (correo == null) {
            finish();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        double saldo = dbHelper.obtenerSaldo(correo);

        tvSaldo.setText("Saldo: $" + saldo);

        btnTransferir.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, TransferActivity.class);
            intent.putExtra("correo", correo);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        String correo = getIntent().getStringExtra("correo");

        if (correo == null) return;

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        double saldo = dbHelper.obtenerSaldo(correo);

        tvSaldo.setText("Saldo: $" + saldo);
    }
}