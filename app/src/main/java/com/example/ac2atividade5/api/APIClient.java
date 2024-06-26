package com.example.ac2atividade5.api;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class APIClient {
    private static final String BASE_URL = "https://6654efee3c1d3b602937d747.mockapi.io/";
    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static AlunoService getUsuarioService() {
        return getClient().create(AlunoService.class);
    }
}
