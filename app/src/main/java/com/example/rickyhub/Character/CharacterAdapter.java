package com.example.rickyhub.Character;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rickyhub.R;

import java.util.List;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Character character);
    }

    private List<Character> characters;
    private final OnItemClickListener listener;

    public CharacterAdapter(List<Character> characters, OnItemClickListener listener) {
        this.characters = characters;
        this.listener = listener;
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
        notifyDataSetChanged();
    }

    public OnItemClickListener getListener() {
        return listener;
    }

    @NonNull
    @Override
    public CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.characters_list_item, parent, false);
        return new CharacterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterViewHolder holder, int position) {
        Character character = characters.get(position);
        holder.bind(character, getListener());
        holder.getCharacterNameTextView().setText(character.getName());
        holder.getCharacterSpecieTextView().setText(character.getSpecie());
        holder.getCharacterGenderTextView().setText(character.getGender());
        Glide.with(holder.itemView.getContext())
                .load(character.getImage())
                .placeholder(R.drawable.default_image)
                .into(holder.getCharacterImageView());
    }

    @Override
    public int getItemCount() {
        return characters != null ? characters.size() : 0;
    }
}
