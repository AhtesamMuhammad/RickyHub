package com.example.rickyhub.Character;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rickyhub.R;

public class CharacterViewHolder extends RecyclerView.ViewHolder {
    private ImageView characterImageView;
    private TextView characterNameTextView;
    private TextView characterSpecieTextView;
    private TextView characterGenderTextView;

    public ImageView getCharacterImageView() {
        return characterImageView;
    }

    public void setCharacterImageView(ImageView characterImageView) {
        this.characterImageView = characterImageView;
    }

    public TextView getCharacterNameTextView() {
        return characterNameTextView;
    }

    public void setCharacterNameTextView(TextView characterNameTextView) {
        this.characterNameTextView = characterNameTextView;
    }

    public TextView getCharacterGenderTextView() {
        return characterGenderTextView;
    }

    public void setCharacterGenderTextView(TextView characterGenderTextView) {
        this.characterGenderTextView = characterGenderTextView;
    }

    public TextView getCharacterSpecieTextView() {
        return characterSpecieTextView;
    }

    public void setCharacterSpecieTextView(TextView characterSpecieTextView) {
        this.characterSpecieTextView = characterSpecieTextView;
    }

    public CharacterViewHolder(View view) {
        super(view);
        setCharacterImageView(view.findViewById(R.id.characterImageThumbnail));
        setCharacterNameTextView(view.findViewById(R.id.characterName));
        setCharacterSpecieTextView(view.findViewById(R.id.characterSpecie));
        setCharacterGenderTextView(view.findViewById(R.id.characterGender));
    }

    public void bind(final Character character, final CharacterAdapter.OnItemClickListener listener) {
        itemView.setOnClickListener(v -> listener.onItemClick(character));
    }
}
