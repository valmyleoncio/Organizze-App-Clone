package com.example.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.config.ConfiguracaoFirebase;
import com.example.organizze.helper.Base64Custom;
import com.example.organizze.helper.DateCustom;
import com.example.organizze.model.Movimentacao;
import com.example.organizze.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReceitasActivity extends AppCompatActivity
{
    private EditText campoValor;
    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private Double receitaTotal;
    private Double receitaGerada;
    private Double receitaAtualizada;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        campoValor = findViewById(R.id.editValor);
        campoData = findViewById(R.id.editaData);
        campoCategoria = findViewById(R.id.editCategoria);
        campoDescricao = findViewById(R.id.editDescricao);

        //preenche o campo data com a data atual
        campoData.setText(DateCustom.dataAtual());

        recuperarReceitaTotal();
    }

    public void salvarReceitas(View view)
    {
        String idUsuario = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail());
        DatabaseReference usuarioRef = firebaseRef.child("Usuarios").child( idUsuario );

        if (validarCamposReceita())
        {
            movimentacao = new Movimentacao();
            movimentacao.setValor(Double.parseDouble(campoValor.getText().toString()));
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(campoData.getText().toString());
            movimentacao.setTipo("r");

            receitaGerada = Double.parseDouble(campoValor.getText().toString());
            receitaAtualizada = receitaTotal + receitaGerada;

            usuarioRef.child("receitaTotal").setValue(receitaAtualizada);
            movimentacao.salvar(movimentacao.getData());

            finish();
        }

    }

    public Boolean validarCamposReceita() {
        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textoCategoria = campoCategoria.getText().toString();
        String textodescricao = campoDescricao.getText().toString();

        if (!textoValor.isEmpty()) {

            if (!textoData.isEmpty()) {

                if (!textoCategoria.isEmpty()) {

                    if (!textodescricao.isEmpty()) {

                        return true;

                    } else {
                        Toast.makeText(ReceitasActivity.this, "Descrição não foi preenchida!", Toast.LENGTH_SHORT).show();
                        return false;
                    }


                } else {
                    Toast.makeText(ReceitasActivity.this, "Categoria não foi preenchida!", Toast.LENGTH_SHORT).show();
                    return false;
                }


            } else {
                Toast.makeText(ReceitasActivity.this, "Data não foi preenchida!", Toast.LENGTH_SHORT).show();
                return false;
            }


        } else {
            Toast.makeText(ReceitasActivity.this, "Valor não foi preenchido!", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public void recuperarReceitaTotal()
    {
        String idUsuario = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail());
        DatabaseReference usuarioRef = firebaseRef.child("Usuarios").child( idUsuario );

        usuarioRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Usuario usuario = dataSnapshot.getValue( Usuario.class );
                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
}
