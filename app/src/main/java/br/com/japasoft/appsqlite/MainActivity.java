package br.com.japasoft.appsqlite;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase bancoDados;
    public ListView listViewDados;
    public Button botao;
    public ArrayList<Integer> arrayIds;
    public Integer idSelecionado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewDados = findViewById(R.id.listViewDados);
        botao = findViewById(R.id.btnCadastrar);

        botao.setOnClickListener(view -> abrirTelaCadastro());

        criarBancoDados();
        listarDados();

        listViewDados.setOnItemLongClickListener((adapterView, view, i, l) -> {
            idSelecionado = arrayIds.get(i);
            confirmarExcluir();
            return true;
        });

        listViewDados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                idSelecionado = arrayIds.get(i);
                abrirTelaAlterar();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        listarDados();
    }

    public void criarBancoDados() {

        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS fornecedores(id INTEGER PRIMARY KEY, nome VARCHAR)");
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarDados() {
        try {
            arrayIds = new ArrayList<>();
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            @SuppressLint("Recycle") Cursor meuCursor = bancoDados.rawQuery("SELECT * FROM fornecedores", null);

            ArrayList<String> linhas = new ArrayList<>();
            ArrayAdapter<String> meuAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, linhas);
            listViewDados.setAdapter(meuAdapter);
            meuCursor.moveToFirst();

            while (true) {
                linhas.add(meuCursor.getString(1));
                arrayIds.add(meuCursor.getInt(0));
                meuCursor.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void abrirTelaCadastro() {
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }

    public void confirmarExcluir() {
        AlertDialog.Builder msgBox = new AlertDialog.Builder(MainActivity.this);
        msgBox.setTitle("Excluir");
        msgBox.setIcon(android.R.drawable.ic_menu_delete);
        msgBox.setMessage("Deseja excluir este item?");
        msgBox.setPositiveButton("Sim", (dialogInterface, i) -> {
            excluirDados();
            listarDados();
        });
        msgBox.setNegativeButton("Não", (dialogInterface, i) -> {
        });
        msgBox.show();
    }


    private void excluirDados() {

        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            String sql = "DELETE FROM FORNECEDORES WHERE id =?";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindLong(1, idSelecionado);
            stmt.executeUpdateDelete();
            listarDados();
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void abrirTelaAlterar() {
        Intent intent = new Intent(this, AlterarActivity.class);
        intent.putExtra("id", idSelecionado);
        startActivity(intent);
    }


}