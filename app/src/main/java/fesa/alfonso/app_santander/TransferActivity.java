package fesa.alfonso.app_santander;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TransferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        EditText etCorreoDestino = findViewById(R.id.etCorreoDestino);
        EditText etMonto = findViewById(R.id.etMonto);
        Button btnTransferir = findViewById(R.id.btnTransferir);

        String correoOrigen = getIntent().getStringExtra("correo");

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        btnTransferir.setOnClickListener(v -> {

            String destino = etCorreoDestino.getText().toString();
            String montoStr = etMonto.getText().toString();

            if (destino.isEmpty() || montoStr.isEmpty()) {
                Toast.makeText(this, "Llena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (destino.equals(correoOrigen)) {
                Toast.makeText(this, "No puedes transferirte a ti mismo", Toast.LENGTH_SHORT).show();
                return;
            }

            double monto = Double.parseDouble(montoStr);

            if (monto <= 0) {
                Toast.makeText(this, "Monto inválido", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean exito = dbHelper.transferir(correoOrigen, destino, monto);

            if (exito) {
                Toast.makeText(this, "Transferencia exitosa", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error en la transferencia", Toast.LENGTH_SHORT).show();
            }
        });
    }
}