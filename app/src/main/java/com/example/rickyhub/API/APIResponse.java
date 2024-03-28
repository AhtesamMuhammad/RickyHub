package com.example.rickyhub.API;

import com.example.rickyhub.Character.Character;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class APIResponse {
    @SerializedName("results")
    private List<Character> characters;

    public List<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }
}
