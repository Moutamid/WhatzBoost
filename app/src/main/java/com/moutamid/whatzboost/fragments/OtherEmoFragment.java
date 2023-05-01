package com.moutamid.whatzboost.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.adapters.EmotionsAdapter;
import com.moutamid.whatzboost.databinding.FragmentOtherEmoBinding;


public class OtherEmoFragment extends Fragment {
    private String[] ascii = new String[]{"( ཀ ʖ̯ ཀ)", "(╥﹏╥)", "( ͡°ᴥ ͡° ʋ)", "ಠ_ಥ", "ಠ~ಠ", "ʕ◉.◉ʔ", "༼ つ ◕_◕ ༽つ", "(ಥ﹏ಥ)", "ヾ(⌐■_■)ノ", "( ︶︿︶)", "(✖╭╮✖)", "༼ つ ಥ_ಥ ༽つ", "(\"º _ º)", "┬─┬ノ( º _ ºノ)", "(._.) ( l: ) ( .-. ) ( :l ) (._.)", "༼ ºل͟º ༼ ºل͟º ༼ ºل͟º ༽ ºل͟º ༽ ºل͟º ༽", "┬┴┬┴┤(･_├┬┴┬┴", "(°ロ°)☝", "(▀̿Ĺ̯▀̿ ̿)", "﴾͡๏̯͡๏﴿ O'RLY?", "⚆ _ ⚆", "ಠ‿↼", "☼.☼", "◉_◉", "( ✧≖ ͜ʖ≖)", "( ͠° ͟ʖ ͡°)", "( ‾ ʖ̫ ‾)", "(͡ ͡° ͜ つ ͡͡°)", "( ﾟдﾟ)", "┬──┬ ノ( ゜-゜ノ)", "¯\\(°_o)/¯", "(ʘᗩʘ')", "☜(⌒▽⌒)☞", "(;´༎ຶД༎ຶ`)", "̿̿ ̿̿ ̿̿ ̿'̿'\\̵͇̿̿\\з= ( ▀ ͜͞ʖ▀) =ε/̵͇̿̿/’̿’̿ ̿ ̿̿ ̿̿ ̿̿", "[̲̅$̲̅(̲̅5̲̅)̲̅$̲̅]", "(ᇂﮌᇂ)", "(≧ω≦)", "►_◄", "أ ̯ أ", "ლ(╹ε╹ლ)", "ᇂ_ᇂ", "＼(￣ー＼)(／ー￣)／", "♪┏ ( ･o･) ┛♪┗ (･o･ ) ┓♪┏(･o･)┛♪", "╘[◉﹃◉]╕", "( ･_･)♡", "(¤﹏¤)", "( ˘︹˘ )"};
    FragmentOtherEmoBinding binding;
    public OtherEmoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOtherEmoBinding.inflate(getLayoutInflater(), container, false);

        binding.recycler.setHasFixedSize(false);
        binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        EmotionsAdapter adapter = new EmotionsAdapter(ascii, requireContext());
        binding.recycler.setAdapter(adapter);

        return binding.getRoot();
    }
}