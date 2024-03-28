package com.example.rickyhub;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.rickyhub.API.API;
import com.example.rickyhub.API.APICallback;
import com.example.rickyhub.Character.Character;
import com.example.rickyhub.Character.CharacterAdapter;
import com.example.rickyhub.databinding.FragmentMainBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    private FragmentMainBinding binding;
    private SharedPreferences sharedPreferences;
    private CharacterAdapter characterAdapter;
    private int lastPage = 1;
    private String lastSearch;
    private String lastGridViewStatus;
    private List<Character> charactersList;
    private List<Character> charactersSearchList;
    private Runnable runnable;

    private void hideVirtualKeyboard() {
        InputMethodManager manager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
        binding.searchBox.clearFocus();
    }

    private void showSnackbarOnTop(String snackbarContent) {
        Snackbar snackbar = Snackbar.make(requireView(), snackbarContent, Snackbar.LENGTH_LONG).setTextMaxLines(3);
        View snackbarView = snackbar.getView();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) snackbarView.getLayoutParams();
        params.gravity = Gravity.TOP;
        params.topMargin = 100;
        snackbarView.setLayoutParams(params);
        snackbar.show();
    }

    private void setRecylcerViewToLinearLayout() {
        binding.charactersRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.gridStatusSwitch.setChecked(false);
        lastGridViewStatus = "false";
    }

    private void setRecylcerViewToGridLayout() {
        binding.charactersRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        binding.gridStatusSwitch.setChecked(true);
        lastGridViewStatus = "true";
    }

    private void setFeedbackTextVisibility(boolean visible) {
        if (visible) {
            binding.feedbackText.setText(getString(R.string.no_characters_found_text));
            binding.feedbackText.setVisibility(View.VISIBLE);
        } else {
            binding.feedbackText.setVisibility(View.GONE);
        }
    }

    private void setGridViewStatusConfiguration(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("grid_view_status", value);
        editor.apply();
    }

    private void updateGridViewStatus() {
        String gridViewStatus = sharedPreferences.getString("grid_view_status", "false");
        if (gridViewStatus.equals("true")) {
            setRecylcerViewToGridLayout();
        } else if (gridViewStatus.equals("false")) {
            setRecylcerViewToLinearLayout();
        } else {
            setGridViewStatusConfiguration("false");
            setRecylcerViewToLinearLayout();
        }
    }

    private boolean gridViewStatusChangedInConfiguration() {
        return !sharedPreferences.getString("grid_view_status", "false").equals(lastGridViewStatus);
    }

    private boolean searchIsEmptyOrEqualToPreviousOne() {
        String searchBoxText = binding.searchBox.getText().toString();
        if (searchBoxText.isEmpty()) {
            loadCharactersInRecyclerView(false);
            return true;
        } else if (searchBoxText.equalsIgnoreCase(lastSearch)) {
            loadCharactersInRecyclerView(true);
            return true;
        }
        return false;
    }

    private void setupCharacterAdapter() {
        characterAdapter = new CharacterAdapter(new ArrayList<>(), character -> NavHostFragment.findNavController(MainFragment.this).navigate(com.example.rickyhub.MainFragmentDirections.actionMainFragmentToCharacterDetailFragment(character)));
        binding.charactersRecyclerView.setAdapter(characterAdapter);
    }

    private void loadCharactersInRecyclerView(boolean searchExecuted) {
        binding.searchBox.setEnabled(true);
        if (searchExecuted) {
            characterAdapter.setCharacters(charactersSearchList);
            binding.loadMoreCharacters.setVisibility(View.GONE);
            setFeedbackTextVisibility(charactersSearchList.size() == 0);
        } else {
            characterAdapter.setCharacters(charactersList);
            setFeedbackTextVisibility(false);
            if (lastPage < 6) {
                binding.loadMoreCharacters.setVisibility(View.VISIBLE);
            } else {
                binding.loadMoreCharacters.setVisibility(View.GONE);
            }
        }
    }

    private void getCharactersFromAPI(String search, boolean loadMoreCharacters) {
        boolean searchExecuted = search != null;
        int page = 1;
        if (loadMoreCharacters) {
            page = lastPage + 1;
        }
        API.fetchAPI(new APICallback() {
            @Override
            public void onSuccess(List<Character> characters) {
                requireActivity().runOnUiThread(() -> {
                    if (searchExecuted) {
                        charactersSearchList = characters;
                    } else if (loadMoreCharacters) {
                        charactersList.addAll(characters);
                        lastPage++;
                    } else {
                        charactersList = characters;
                    }
                    loadCharactersInRecyclerView(searchExecuted);
                });
            }

            @Override
            public void onFailure(Throwable t) {
            }
        }, page, search);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        if (savedInstanceState != null) {
            lastPage = savedInstanceState.getInt("last_page");
            lastSearch = savedInstanceState.getString("last_search");
            charactersList = (List<Character>) savedInstanceState.getSerializable("characters_list");
            charactersSearchList = (List<Character>) savedInstanceState.getSerializable("characters_search_list");
        }
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateGridViewStatus();
        setupCharacterAdapter();

        if (charactersList == null) {
            getCharactersFromAPI(null, false);
        }

        binding.gridStatusSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setRecylcerViewToGridLayout();
                setGridViewStatusConfiguration("true");
            } else {
                setRecylcerViewToLinearLayout();
                setGridViewStatusConfiguration("false");
            }
        });

        binding.searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (runnable != null) {
                    requireView().removeCallbacks(runnable);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (searchIsEmptyOrEqualToPreviousOne()) {
                    return;
                }
                runnable = () -> {
                    String search = binding.searchBox.getText().toString();
                    getCharactersFromAPI(search, false);
                    lastSearch = search;
                };
                requireView().postDelayed(runnable, 500);
            }
        });

        binding.clearSearchButton.setOnClickListener(View -> {
            hideVirtualKeyboard();
            if (binding.searchBox.getText().toString().isEmpty()) {
                return;
            }
            binding.searchBox.setText("");
            showSnackbarOnTop(getString(R.string.searchbox_cleared_text));
        });

        binding.loadMoreCharacters.setOnClickListener(View -> {
            getCharactersFromAPI(null, true);
            showSnackbarOnTop(getString(R.string.more_characters_loaded_text));
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (gridViewStatusChangedInConfiguration()) {
            updateGridViewStatus();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putSerializable("characters_list", (Serializable) charactersList);
        bundle.putSerializable("characters_search_list", (Serializable) charactersSearchList);
        bundle.putString("last_search", lastSearch);
        bundle.putInt("last_page", lastPage);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
