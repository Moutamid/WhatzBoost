package com.moutamid.whatzboost.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.adapters.EmotionsAdapter;
import com.moutamid.whatzboost.databinding.FragmentAngryBinding;
import com.moutamid.whatzboost.databinding.FragmentHappyBinding;

public class AngryFragment extends Fragment {
    private String[] ascii = new String[]{"(≖︿≖ )", "(ง ͠° ͟ل͜ ͡°)ง", "ಠ_ಠ", "( ͠°Д°͠ )", "ლ(ಠ益ಠლ)", "(>ლ)", "(ง'-̀'́)ง", "(ノಠ益ಠ)ノ彡┻━┻", "(╯°□°）╯︵( .o.)", "(╯°□°）╯︵ /( ‿⌓‿ )\\", "(╯°□°）╯︵ ┻━┻", "┻━┻ ︵ヽ(`Д´)ﾉ︵ ┻━┻", "(┛◉Д◉)┛彡┻━┻ ", "(¬_¬)", "ಠoಠ", "ᕙ(⇀‸↼‶)ᕗ", "ᕦ(ò_óˇ)ᕤ", "¯\\_(ʘ_ʘ)_/¯", "( ͡°_ʖ ͡°)", "( ° ͜ʖ͡°)╭∩╮", "( ͡°Ĺ̯ ͡° )", "( ͠° ͟ʖ °͠ )", "¯\\_( ͠° ͟ʖ °͠ )_/¯", "( ͡°⊖ ͡°)", "ರ_ರ", "(>人<)", "ಠ╭╮ಠ", "(◣_◢)", "(⋋▂⋌)", "〴⋋_⋌〵", "(╹◡╹)凸", "ლ(͠°◞౪◟°͠ლ)", "╭∩╮(︶︿︶)╭∩╮", "(ㆆ_ㆆ)"};
    FragmentAngryBinding binding;

    public AngryFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAngryBinding.inflate(getLayoutInflater(), container, false);

        binding.recycler.setHasFixedSize(false);
        binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        EmotionsAdapter adapter = new EmotionsAdapter(ascii, requireContext());
        binding.recycler.setAdapter(adapter);

        return binding.getRoot();
    }
}