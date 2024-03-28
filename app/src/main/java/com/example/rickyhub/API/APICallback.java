package com.example.rickyhub.API;

import com.example.rickyhub.Character.Character;

import java.util.List;

public interface APICallback {
    void onSuccess(List<Character> characters);

    void onFailure(Throwable t);
}
