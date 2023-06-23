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
        binding.stickers.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int duration = 300;
                switch (event.getAction()) {

                    case MotionEvent.ACTION_MOVE:
                        ObjectAnimator scaleDownXX = ObjectAnimator.ofFloat(v,
                                "scaleX", 0.6f);
                        ObjectAnimator scaleDownYY = ObjectAnimator.ofFloat(v,
                                "scaleY", 0.6f);
                        scaleDownXX.setDuration(duration);
                        scaleDownYY.setDuration(duration);

                        AnimatorSet scaleDownn = new AnimatorSet();
                        scaleDownn.play(scaleDownXX).with(scaleDownYY);

                        scaleDownn.start();

                        new Handler().postDelayed(()-> {
                            ObjectAnimator scaleDownX3 = ObjectAnimator.ofFloat(v,
                                    "scaleX", 1f);
                            ObjectAnimator scaleDownY3 = ObjectAnimator.ofFloat(v,
                                    "scaleY", 1f);
                            scaleDownX3.setDuration(duration);
                            scaleDownY3.setDuration(duration);

                            AnimatorSet scaleDown3 = new AnimatorSet();
                            scaleDown3.play(scaleDownX3).with(scaleDownY3);

                            scaleDown3.start();
                        }, 300);
                        break;

                    case MotionEvent.ACTION_DOWN:
                        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(v,
                                "scaleX", 0.8f);
                        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(v,
                                "scaleY", 0.8f);
                        scaleDownX.setDuration(duration);
                        scaleDownY.setDuration(duration);

                        AnimatorSet scaleDown = new AnimatorSet();
                        scaleDown.play(scaleDownX).with(scaleDownY);

                        scaleDown.start();
                        break;

                    case MotionEvent.ACTION_UP:
                        ObjectAnimator scaleDownX2 = ObjectAnimator.ofFloat(
                                v, "scaleX", 1f);
                        ObjectAnimator scaleDownY2 = ObjectAnimator.ofFloat(
                                v, "scaleY", 1f);
                        scaleDownX2.setDuration(duration);
                        scaleDownY2.setDuration(duration);

                        AnimatorSet scaleDown2 = new AnimatorSet();
                        scaleDown2.play(scaleDownX2).with(scaleDownY2);

                        scaleDown2.start();
                        progressDialog.show();
                        Fresco.initialize(requireContext());
                        loadListAsyncTask = new LoadListAsyncTask(requireActivity());
                        loadListAsyncTask.execute(new Void[0]);
                        break;
                }
                return true;
            }
        });

        return binding.getRoot();
    }

    public LoadListAsyncTask loadListAsyncTask;

    public class LoadListAsyncTask extends AsyncTask<Void, Void, Pair<String, ArrayList<StickerPack>>> {
        private final WeakReference<FragmentActivity> contextWeakReference;

        public LoadListAsyncTask(FragmentActivity mainActivity) {
            this.contextWeakReference = new WeakReference<>(mainActivity);
        }


        public Pair<String, ArrayList<StickerPack>> doInBackground(Void... voidArr) {
            try {
                Context context = (Context) this.contextWeakReference.get();
                if (context == null) {
                    return new Pair<>("could not fetch sticker packs", null);
                }
                ArrayList<StickerPack> fetchStickerPacks = StickerPackLoader.fetchStickerPacks(context);
                if (fetchStickerPacks.size() == 0) {
                    return new Pair<>("could not find any packs", null);
                }
                Iterator<StickerPack> it = fetchStickerPacks.iterator();
                while (it.hasNext()) {
                    StickerPackValidator.verifyStickerPackValidity(context, it.next());
                }
                return new Pair<>(null, fetchStickerPacks);
            } catch (Exception e) {
                Log.e("EntryActivity", "error fetching sticker packs", e);
                return new Pair<>(e.getMessage(), null);
            }
        }


        @Override
        public void onPostExecute(Pair<String, ArrayList<StickerPack>> pair) {
            FragmentActivity mainActivity = (FragmentActivity) this.contextWeakReference.get();
            if (mainActivity == null) {
                return;
            }
            if (pair.first != null) {
                showErrorMessage((String) pair.first);
            } else {
                showStickerPack((ArrayList) pair.second);
            }
        }
    }

    public void showErrorMessage(String str) {
        progressDialog.dismiss();
        Log.e("Main activity", "error fetching sticker packs, " + str);
        Toast.makeText(requireContext(), "" + str, Toast.LENGTH_SHORT).show();
    }

    public void showStickerPack(ArrayList<StickerPack> arrayList) {
        if (arrayList.size() > 1) {
            progressDialog.dismiss();
            Intent intent = new Intent(requireContext(), StickerPackListActivity.class);
            intent.putParcelableArrayListExtra(StickerPackListActivity.EXTRA_STICKER_PACK_LIST_DATA, arrayList);
            startActivity(intent);
            requireActivity().overridePendingTransition(0, 0);
            return;
        }
        progressDialog.dismiss();
        Intent intent2 = new Intent(requireContext(), StickerPackDetailsActivity.class);
        intent2.putExtra(StickerPackDetailsActivity.EXTRA_SHOW_UP_BUTTON, false);
        intent2.putExtra(StickerPackDetailsActivity.EXTRA_STICKER_PACK_DATA, arrayList.get(0));
        startActivity(intent2);
        requireActivity().overridePendingTransition(0, 0);
    }

}