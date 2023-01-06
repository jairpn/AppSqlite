package br.com.japasoft.appsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CadastroActivity extends AppCompatActivity {

    EditText edtnome;
    Button botao;
    SQLiteDatabase bancoDados;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        edtnome = (EditText) findViewById(R.id.edtNome);
        botao = (Button) findViewById(R.id.btnAlterar);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrar();
            }
        });

    }

    public void cadastrar() {
        if (!TextUtils.isEmpty(edtnome.getText().toString())) {
            try {
                bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
                String sql = "INSERT INTO fornecedores (nome) VALUES(?)";
                SQLiteStatement stmt = bancoDados.compileStatement(sql);
                stmt.bindString(1, edtnome.getText().toString());
                stmt.executeInsert();
                finish();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}