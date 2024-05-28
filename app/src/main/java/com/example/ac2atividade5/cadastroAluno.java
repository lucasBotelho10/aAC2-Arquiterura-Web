package com.example.ac2atividade5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ac2atividade5.api.AlunoService;
import com.example.ac2atividade5.model.Aluno;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class cadastroAluno extends AppCompatActivity {

    AlunoService apiService;
    private EditText TextNome, TextRA, TextCEP, TextLogradouro, TextComplemento,
            TextBairro, TextCidade, TextUF;
    private Button btnCadastrar;
    private static final String VIA_CEP_URL = "https://viacep.com.br/ws/%s/json/";
    private static final String MOCKAPI_URL = "https://6654efee3c1d3b602937d747.mockapi.io/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_aluno);

        TextNome = findViewById(R.id.TextNome);
        TextRA = findViewById(R.id.TextRA);
        TextCEP = findViewById(R.id.TextCEP);
        TextLogradouro = findViewById(R.id.TextLogradouro);
        TextComplemento = findViewById(R.id.TextComplemento);
        TextBairro = findViewById(R.id.TextBairro);
        TextCidade = findViewById(R.id.TextCidade);
        TextUF = findViewById(R.id.TextUF);

        btnCadastrar = findViewById(R.id.btnCadastrar);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MOCKAPI_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(AlunoService.class);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarAluno();
            }
        });


        TextCEP.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    buscarEnderecoPorCEP();
                }
            }
        });
    }

    private void buscarEnderecoPorCEP() {
        String cep = TextCEP.getText().toString().trim();
        if (cep.length() == 8) { // O CEP no Brasil tem 8 dígitos
            String url = "https://viacep.com.br/ws/" + cep + "/json/";
            new BuscarEnderecoTask().execute(url);
        } else {
            Toast.makeText(this, "CEP inválido", Toast.LENGTH_SHORT).show();
        }
    }

    private void cadastrarAluno() {

        String ra = TextRA.getText().toString();
        String nome = TextNome.getText().toString();
        String cep = TextCEP.getText().toString();
        String logradouro = TextLogradouro.getText().toString();
        String complemento = TextComplemento.getText().toString();
        String bairro = TextBairro.getText().toString();
        String cidade = TextCidade.getText().toString();
        String uf = TextUF.getText().toString();


        Aluno aluno = new Aluno(ra, nome, cep, logradouro, complemento, bairro, cidade, uf);

        Call<Aluno> call = apiService.postAluno(aluno);
        call.enqueue(new Callback<Aluno>() {
            @Override
            public void onResponse(Call<Aluno> call, Response<Aluno> response) {
                if(response.isSuccessful()){
                    Toast.makeText(cadastroAluno.this,"Aluno cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Log.e("Inserir", "Erro ao criar: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Aluno> call, Throwable t) {
                Log.e("Inserir", "Erro ao criar: " + t.getMessage());
            }
        });
    }

    private void inserirAluno(Aluno aluno) {
        Call<Aluno> call = apiService.postAluno(aluno);
        call.enqueue(new Callback<Aluno>() {
            @Override
            public void onResponse(Call<Aluno> call, Response<Aluno> response) {
                if (response.isSuccessful()) {

                    Aluno createdPost = response.body();
                    Toast.makeText(cadastroAluno.this, "Inserido com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {

                    Log.e("Inserir", "Erro ao criar: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Aluno> call, Throwable t) {

                Log.e("Inserir", "Erro ao criar: " + t.getMessage());
            }
        });
    }


    private class BuscarEnderecoTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    TextLogradouro.setText(jsonObject.getString("logradouro"));
                    TextComplemento.setText(jsonObject.getString("complemento"));
                    TextBairro.setText(jsonObject.getString("bairro"));
                    TextCidade.setText(jsonObject.getString("localidade"));
                    TextUF.setText(jsonObject.getString("uf"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(cadastroAluno.this, "Erro ao buscar endereço", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private static void cadastrarAluno(Aluno aluno) throws IOException {
        String urlStr = MOCKAPI_URL + "Aluno";
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        JSONObject alunoJson = new JSONObject((Map) aluno);

        conn.getOutputStream().write(alunoJson.toString().getBytes());

        if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
            throw new RuntimeException("Falha ao cadastrar aluno: Código de erro HTTP " + conn.getResponseCode());
        }

        System.out.println("Aluno cadastrado com sucesso!");
        conn.disconnect();
    }
}
