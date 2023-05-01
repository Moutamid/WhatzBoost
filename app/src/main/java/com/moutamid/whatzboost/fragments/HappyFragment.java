package com.moutamid.whatzboost.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.adapters.EmotionsAdapter;
import com.moutamid.whatzboost.databinding.FragmentHappyBinding;

public class HappyFragment extends Fragment {
    private String[] ascii = new String[]{"¯\\_(ツ)_/¯", "(☞ﾟヮﾟ)☞", "(◕‿◕)", "(｡◕‿◕｡)", "( ಠ ͜ʖರೃ)", "(⊙﹏⊙)", "(◠﹏◠)", "(ﾉ◕ヮ◕)ﾉ", "( ͡° ͜ʖ ͡°)", "( ͡°( ͡° ͜ʖ( ͡° ͜ʖ ͡°)ʖ ͡°) ͡°)", "┬┴┬┴┤ ͜ʖ ͡°) ├┬┴┬┴", "(◕‿◕✿)", "ᕕ(✿◕‿◕)ᕗ", "ᕕ(  ◕‿◕)ᕗ", "(ᵔᴥᵔ)", "(づ￣ ³￣)づ", "\\ (•◡•) /", "\\(◕◡◕)/", "(☞ﾟヮﾟ)☞ ☜(ﾟヮﾟ☜)", "☜(ﾟヮﾟ☜)", "♪~ ᕕ(ᐛ)ᕗ", "༼ʘ̚ل͜ʘ̚༽", "ʘ‿ʘ", "~ (˘▾˘~)", "(~˘▾˘)~", "(͡o‿O͡)", "(❍ᴥ❍ʋ)", "| (• ◡•)| (❍ᴥ❍ʋ)", "─=≡Σᕕ( ͡° ͜ʖ ͡°)ᕗ", "( ͡° ͜ʖ ͡°)>⌐■-■", "( ͡ᶢ ͜ʖ ͡ᶢ)", "( ͡^ ͜ʖ ͡^)", "( ͡ᵔ ͜ʖ ͡ᵔ )", "( ͡° ͜ ͡°)", "(˵ ͡° ͜ʖ ͡°˵)", "(∩ ͡° ͜ʖ ͡°)⊃━☆ﾟ", "ᕦ( ͡° ͜ʖ ͡°)ᕤ", "（╯ ͡° ▽ ͡°）╯︵ ┻━┻", "( ͡°╭͜ʖ╮͡° )", "༼ つ  ͡° ͜ʖ ͡° ༽つ", "(｡◕‿‿◕｡)", "(ง°ل͜°)ง", "ಠ⌣ಠ", "♡‿♡", "(●´ω｀●)", "(╹◡╹)", "ლ(╹◡╹ლ)", "(づ｡◕‿‿◕｡)づ", "┏(＾0＾)┛┗(＾0＾) ┓"};
    FragmentHappyBinding binding;
    public HappyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHappyBinding.inflate(getLayoutInflater(), container, false);

        binding.recycler.setHasFixedSize(false);
        binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        EmotionsAdapter adapter = new EmotionsAdapter(ascii, requireContext());
        binding.recycler.setAdapter(adapter);

        return binding.getRoot();
    }
}