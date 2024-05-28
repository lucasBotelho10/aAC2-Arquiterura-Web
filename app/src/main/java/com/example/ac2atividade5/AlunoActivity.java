package com.example.ac2atividade5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AlunoActivity extends AppCompatActivity {

    private EditText editTextNome, editTextRA, editTextCEP;
    private TextView textViewLogradouro, textViewComplemento, textViewBairro, textViewCidade, textViewUF;
    private Button buttonCadastrar;
    private static final String VIA_CEP_URL = "https://viacep.com.br/ws/%s/json/";
    private static final String MOCKAPI_URL = "https://mockapi.io/api/v1/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno);
    }
}