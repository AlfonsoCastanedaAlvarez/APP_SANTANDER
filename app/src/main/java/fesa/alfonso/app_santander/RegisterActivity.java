package fesa.alfonso.app_santander;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {

    Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnRegistrar = findViewById(R.id.btnRegistrar);

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        EditText etNombre = findViewById(R.id.etNombre);
        EditText etApellido = findViewById(R.id.etApellido);
        EditText etCorreo = findViewById(R.id.etCorreo);
        EditText etTelefono = findViewById(R.id.etTelefono);
        EditText etPassword = findViewById(R.id.etPassword);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nombre = etNombre.getText().toString();
                String apellido = etApellido.getText().toString();
                String correo = etCorreo.getText().toString();
                String telefono = etTelefono.getText().toString();
                String password = etPassword.getText().toString();

                if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || telefono.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Llena todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dbHelper.correoExiste(correo)) {
                    Toast.makeText(RegisterActivity.this, "Este correo ya está registrado", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean insertado = dbHelper.insertarUsuario(nombre, apellido, correo, telefono, password);

                if (insertado) {
                    Toast.makeText(RegisterActivity.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                } else {
                    Toast.makeText(RegisterActivity.this, "Error al registrar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}