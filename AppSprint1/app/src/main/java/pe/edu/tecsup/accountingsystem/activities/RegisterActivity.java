package pe.edu.tecsup.accountingsystem.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import pe.edu.tecsup.accountingsystem.R;


public class RegisterActivity extends AppCompatActivity {

    Button regLog;

    private EditText userEmail, userPassword, userPassword2, userName;
    private ProgressBar loadingProgress;
    private Button regBtn;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    private String name="";
    private String email="";
    private String password="";
    private String password2="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        getSupportActionBar().hide();

        userEmail = findViewById(R.id.regEmail);
        userPassword = findViewById(R.id.regPassword);
        userPassword2 = findViewById(R.id.regPassword2);
        userName = findViewById(R.id.regName);

        loadingProgress = findViewById(R.id.regProgressBar);
        regBtn = findViewById(R.id.regBtn);
        loadingProgress.setVisibility(View.INVISIBLE);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = userName.getText().toString();
                email = userEmail.getText().toString();
                password = userPassword.getText().toString();
                password2 = userPassword2.getText().toString();
                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !password2.isEmpty()){
                    if (password.length() >= 6){
                        registrarusuario();
                    }else{
                        Toast.makeText(RegisterActivity.this, "El password debe tener almenos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "Debe Completar los Campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        regLog = (Button) findViewById(R.id.regLoginbtn);
        regLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Regresar
                onBackPressed();
            }
        });

    }
    private void registrarusuario(){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Map<String,Object> map = new HashMap<>();
                    map.put("nombre",name);
                    map.put("email",email);
                    map.put("password",password);
                    String id = mAuth.getCurrentUser().getUid();
                    mDatabase.child("user").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                onBackPressed();
                                //startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                //finish();
                            }else {
                                Toast.makeText(RegisterActivity.this, "No se crearon datos en la bd correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(RegisterActivity.this, "No se pudo registrar este usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
