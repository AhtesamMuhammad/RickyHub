package com.example.rickyhub.Character;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Character implements Serializable {
    private int id;
    private String name;
    private String species;
    private String gender;
    private String image;
    private Map<String, String> origin;
    private Map<String, String> location;
    private List<String> episode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        if (name.isEmpty()) {
            setName("N/A");
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecie() {
        if (species.isEmpty())
            setSpecie("N/A");
        return species;
    }

    public void setSpecie(String specie) {
        this.species = specie;
    }

    public String getGender() {
        if (gender.isEmpty())
            setGender("N/A");
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Map<String, String> getOrigin() {
        return origin;
    }

    public void setOrigin(Map<String, String> origin) {
        this.origin = origin;
    }

    public Map<String, String> getLocation() {
        return location;
    }

    public void setLocation(Map<String, String> location) {
        this.location = location;
    }

    public List<String> getEpisodes() {
        return episode;
    }

    public void setEpisodes(List<String> episodes) {
        this.episode = episodes;
    }
}
