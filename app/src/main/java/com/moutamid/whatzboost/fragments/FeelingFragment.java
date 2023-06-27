package com.moutamid.whatzboost.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.FragmentFeelingBinding;
import com.moutamid.whatzboost.ui.CaptionListActivity;
import com.moutamid.whatzboost.ui.EmotionsActivity;
import com.moutamid.whatzboost.ui.QrScannerActivity;
import com.moutamid.whatzboost.ui.ShayariActivity;
import com.moutamid.whatzboost.whatsappsticker.StickerPack;
import com.moutamid.whatzboost.whatsappsticker.StickerPackDetailsActivity;
import com.moutamid.whatzboost.whatsappsticker.StickerPackListActivity;
import com.moutamid.whatzboost.whatsappsticker.StickerPackLoader;
import com.moutamid.whatzboost.whatsappsticker.StickerPackValidator;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

public class FeelingFragment extends Fragment {
    public FragmentFeelingBinding binding;
    static ProgressDialog progressDialog;
    public FeelingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFeelingBinding.inflate(getLayoutInflater(), container, false);

        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        binding.captions.setOnTouchListener(Constants.customOnTouchListner(CaptionListActivity.class, requireContext(), requireActivity()));

        binding.emotions.setOnTouchListener(Constants.customOnTouchListner(EmotionsActivity.class, requireContext(), requireActivity()));
        binding.shayari.setOnTouchListener(Constants.customOnTouchListner(ShayariActivity.class, requireContext(), requireActivity()));

        return binding.getRoot();
    }

}