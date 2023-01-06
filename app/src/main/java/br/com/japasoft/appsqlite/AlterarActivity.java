package br.com.japasoft.appsqlite;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AlterarActivity extends AppCompatActivity {
    public SQLiteDatabase bancoDados;
    public Button btnAlterar;
    public EditText edtNome;
    public Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar);

        btnAlterar = findViewById(R.id.btnAlterar);
        edtNome = findViewById(R.id.edtNome);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        carregarDados();

        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterar();
            }
        });
    }

    public void carregarDados() {
        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM fornecedores WHERE id = " + id.toString(), null);
            cursor.moveToFirst();
            edtNome.setText(cursor.getString(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void alterar() {
        String valueNome;
        valueNome = edtNome.getText().toString();
        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            String sql = "UPDATE FORNECEDORES set nome = ? WHERE id = ?";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindString(1, valueNome);
            stmt.bindLong(2, id);
            stmt.executeUpdateDelete();
            bancoDados.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }


}