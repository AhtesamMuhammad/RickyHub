package com.example.rickyhub.API;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {
    @GET("character")
    Call<APIResponse> getCharacters(
            @Query("page") int page,
            @Query("name") String name
    );
}
