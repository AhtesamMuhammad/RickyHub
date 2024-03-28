package com.example.rickyhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.rickyhub.Character.Character;
import com.example.rickyhub.databinding.CharacterDetailBinding;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CharacterDetailFragment extends Fragment {
    private CharacterDetailBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = CharacterDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Character character = (Character) requireArguments().getSerializable("character");
        binding.characterNameText.setText(Objects.requireNonNull(character).getName());
        binding.specieText.setText(Objects.requireNonNull(character).getSpecie());
        binding.genderText.setText(Objects.requireNonNull(character).getGender());
        binding.originText.setText(mapToString(Objects.requireNonNull(character).getOrigin()).replace("name: ", "").split("\n")[0]);
        binding.locationText.setText(mapToString(Objects.requireNonNull(character).getLocation()).replace("name: ", "").split("\n")[0]);
        binding.episodesText.setText(listToString(Objects.requireNonNull(character).getEpisodes()).replace("https://rickandmortyapi.com/api/episode/", "Episode "));
        Glide.with(requireContext())
                .load(character.getImage())
                .placeholder(R.drawable.default_image)
                .into(binding.characterImage);
    }

    private String mapToString(Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            stringBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return stringBuilder.toString();
    }

    private String listToString(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String episode : list) {
            stringBuilder.append("•  ").append(episode).append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
