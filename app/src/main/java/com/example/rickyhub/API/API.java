package com.example.rickyhub.API;

import androidx.annotation.NonNull;

import com.example.rickyhub.Character.Character;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://rickandmortyapi.com/api/";

    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static void fetchAPI(APICallback callback, int page, String name) {
        APIService service = getRetrofitInstance().create(APIService.class);
        Call<APIResponse> call = service.getCharacters(page, name);

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse> call, @NonNull Response<APIResponse> response) {
                if (response.isSuccessful()) {
                    APIResponse responseBody = response.body();
                    if (responseBody != null) {
                        List<Character> characters = responseBody.getCharacters();
                        callback.onSuccess(characters);
                    } else {
                        callback.onFailure(new Throwable("Error: Response body is null."));
                    }
                } else {
                    callback.onSuccess(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(@NonNull Call<APIResponse> call, @NonNull Throwable t) {
                callback.onFailure(t);
            }
        });
    }
}
