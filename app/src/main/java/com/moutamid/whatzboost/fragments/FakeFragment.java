package com.moutamid.whatzboost.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.fxn.stash.Stash;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.adsense.Ads;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.FragmentFakeBinding;
import com.moutamid.whatzboost.databinding.ViewAdIndicatorBinding;
import com.moutamid.whatzboost.ui.BlankMessageActivity;
import com.moutamid.whatzboost.ui.DeletedMessageActivity;
import com.moutamid.whatzboost.ui.DirectActivity;
import com.moutamid.whatzboost.ui.MakeProfileActivity;
import com.moutamid.whatzboost.ui.MakeStoryActivity;
import com.moutamid.whatzboost.ui.StatusSaverActivity;
import com.moutamid.whatzboost.ui.VideoSplitterActivity;
import com.moutamid.whatzboost.ui.WhatsWebActivity;
import com.moutamid.whatzboost.whatsappsticker.StickerPack;
import com.moutamid.whatzboost.whatsappsticker.StickerPackDetailsActivity;
import com.moutamid.whatzboost.whatsappsticker.StickerPackListActivity;
import com.moutamid.whatzboost.whatsappsticker.StickerPackLoader;
import com.moutamid.whatzboost.whatsappsticker.StickerPackValidator;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class FakeFragment extends Fragment {
    FragmentFakeBinding binding;
    static ProgressDialog progressDialog;

    public FakeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFakeBinding.inflate(getLayoutInflater(), container, false);

        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        ViewAdIndicatorBinding[] views = {
                binding.viewDelete, binding.viewSplitter, binding.viewWeb, binding.viewWAProfile,
                binding.viewFakeProfile, binding.viewFakeStory, binding.viewStatus, binding.viewBlank, binding.viewStickers
        };

        int randomNumber = new Random().nextInt(views.length);
        ViewAdIndicatorBinding[] randomViews = Constants.pickRandomViews(views, randomNumber);

        for (ViewAdIndicatorBinding randomView : randomViews) {
            randomView.getRoot().setVisibility(View.VISIBLE);
        }


        boolean viewProfile = binding.viewFakeProfile.getRoot().getVisibility() == View.VISIBLE ? true : false;
        boolean viewStory = binding.viewFakeStory.getRoot().getVisibility() == View.VISIBLE ? true : false;
        boolean viewWeb = binding.viewWeb.getRoot().getVisibility() == View.VISIBLE ? true : false;
        boolean viewWAProfile = binding.viewWAProfile.getRoot().getVisibility() == View.VISIBLE ? true : false;
        boolean viewStatus = binding.viewStatus.getRoot().getVisibility() == View.VISIBLE ? true : false;
        boolean viewSplitter = binding.viewSplitter.getRoot().getVisibility() == View.VISIBLE ? true : false;
        boolean viewBlank = binding.viewBlank.getRoot().getVisibility() == View.VISIBLE ? true : false;
        boolean viewDelete = binding.viewDelete.getRoot().getVisibility() == View.VISIBLE ? true : false;
        boolean viewSticker = binding.viewStickers.getRoot().getVisibility() == View.VISIBLE ? true : false;

        binding.profile.setOnTouchListener(Constants.customOnTouchListner(MakeProfileActivity.class, requireContext(), requireActivity(), viewProfile, binding.viewFakeProfile.view, binding.viewFakeProfile.counter));
        binding.story.setOnTouchListener(Constants.customOnTouchListner(MakeStoryActivity.class, requireContext(), requireActivity(), viewStory, binding.viewFakeStory.view, binding.viewFakeStory.counter));
        binding.whatsWeb.setOnTouchListener(Constants.customOnTouchListner(WhatsWebActivity.class, requireContext(), requireActivity(), viewWeb, binding.viewWeb.view, binding.viewWeb.counter));
        binding.directChat.setOnTouchListener(Constants.customOnTouchListner(DirectActivity.class, requireContext(), requireActivity(), viewWAProfile, binding.viewWAProfile.view, binding.viewWAProfile.counter));
        binding.statusSaver.setOnTouchListener(Constants.customOnTouchListner(StatusSaverActivity.class, requireContext(), requireActivity(), viewStatus, binding.viewStatus.view, binding.viewStatus.counter));
        binding.blank.setOnTouchListener(Constants.customOnTouchListner(BlankMessageActivity.class, requireContext(), requireActivity(), viewBlank, binding.viewBlank.view, binding.viewBlank.counter));
        binding.videoSplitter.setOnTouchListener(Constants.customOnTouchListner(VideoSplitterActivity.class, requireContext(), requireActivity(), viewSplitter, binding.viewSplitter.view, binding.viewSplitter.counter));
        binding.deleteMessage.setOnTouchListener(Constants.customOnTouchListner(DeletedMessageActivity.class, requireContext(), requireActivity(), viewDelete, binding.viewDelete.view, binding.viewDelete.counter));
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

                        new Handler().postDelayed(() -> {
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

                        if (viewSticker) {
                            if (Stash.getBoolean(Constants.GUIDE_AD, true)) {
                                showAdGuide();
                            } else {
                                showAD();
                            }
                        } else {
                            progressDialog.show();
                            Fresco.initialize(requireContext());
                            loadListAsyncTask = new LoadListAsyncTask(requireActivity());
                            loadListAsyncTask.execute(new Void[0]);
                        }
                        break;
                }
                return true;
            }
        });

        return binding.getRoot();
    }


    public void showAdGuide() {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.advertise_dialg);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        dialog.show();

        Button ok = dialog.findViewById(R.id.ok);

        ok.setOnClickListener(v -> {
            dialog.dismiss();
            Stash.put(Constants.GUIDE_AD, false);
            CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    // Update the countdownTextView with the remaining time
                    binding.viewStickers.counter.setText(String.valueOf((millisUntilFinished / 1000) + 1));
                }

                @Override
                public void onFinish() {
                    // Countdown has finished
                    //countdownTextView.setText("Countdown Finished");
                   showAD();
                }
            };
            countDownTimer.start();
        });

    }

    private void showAD() {
        int rand = new Random().nextInt(101);
        if (Stash.getBoolean(Ads.IS_ADMOB)) {
            if (rand % 2 == 0) {
                if (Ads.mInterstitialAd != null) {
                    Ads.mInterstitialAd.show(requireActivity());
                    Ads.mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            progressDialog.show();
                            Fresco.initialize(requireContext());
                            loadListAsyncTask = new LoadListAsyncTask(requireActivity());
                            loadListAsyncTask.execute(new Void[0]);
                        }
                    });
                } else {
                    progressDialog.show();
                    Fresco.initialize(requireContext());
                    loadListAsyncTask = new LoadListAsyncTask(requireActivity());
                    loadListAsyncTask.execute(new Void[0]);
                }
            } else {
                if (Ads.rewardedAd != null) {
                    Ads.rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            progressDialog.show();
                            Fresco.initialize(requireContext());
                            loadListAsyncTask = new LoadListAsyncTask(requireActivity());
                            loadListAsyncTask.execute(new Void[0]);
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);
                            progressDialog.show();
                            Fresco.initialize(requireContext());
                            loadListAsyncTask = new LoadListAsyncTask(requireActivity());
                            loadListAsyncTask.execute(new Void[0]);
                        }
                    });

                    Ads.rewardedAd.show(requireActivity(), new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

                        }
                    });

                } else {
                    progressDialog.show();
                    Fresco.initialize(requireContext());
                    loadListAsyncTask = new LoadListAsyncTask(requireActivity());
                    loadListAsyncTask.execute(new Void[0]);
                }

            }
        } else {
            if (rand % 2 == 0) {
                if (Ads.finterstitialAd.isAdInvalidated()) {
                    progressDialog.show();
                    Fresco.initialize(requireContext());
                    loadListAsyncTask = new LoadListAsyncTask(requireActivity());
                    loadListAsyncTask.execute(new Void[0]);
                } else {
                    if (Ads.finterstitialAd != null || Ads.finterstitialAd.isAdLoaded()) {
                        Ads.finterstitialAd.show();
                    } else {
                        progressDialog.show();
                        Fresco.initialize(requireContext());
                        loadListAsyncTask = new LoadListAsyncTask(requireActivity());
                        loadListAsyncTask.execute(new Void[0]);
                    }
                }
            } else {
                if (Ads.frewardedVideoAd.isAdInvalidated()) {
                    progressDialog.show();
                    Fresco.initialize(requireContext());
                    loadListAsyncTask = new LoadListAsyncTask(requireActivity());
                    loadListAsyncTask.execute(new Void[0]);
                } else {
                    if (Ads.frewardedVideoAd != null || Ads.frewardedVideoAd.isAdLoaded()) {
                        Ads.frewardedVideoAd.show();
                    } else {
                        progressDialog.show();
                        Fresco.initialize(requireContext());
                        loadListAsyncTask = new LoadListAsyncTask(requireActivity());
                        loadListAsyncTask.execute(new Void[0]);
                    }
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        binding.viewFakeProfile.counter.setText("3");
        binding.viewFakeProfile.counter.setVisibility(View.GONE);
        binding.viewFakeProfile.view.setVisibility(View.VISIBLE);

        binding.viewFakeStory.counter.setText("3");
        binding.viewFakeStory.counter.setVisibility(View.GONE);
        binding.viewFakeStory.view.setVisibility(View.VISIBLE);

        binding.viewWeb.counter.setText("3");
        binding.viewWeb.counter.setVisibility(View.GONE);
        binding.viewWeb.view.setVisibility(View.VISIBLE);

        binding.viewWAProfile.counter.setText("3");
        binding.viewWAProfile.counter.setVisibility(View.GONE);
        binding.viewWAProfile.view.setVisibility(View.VISIBLE);

        binding.viewStatus.counter.setText("3");
        binding.viewStatus.counter.setVisibility(View.GONE);
        binding.viewStatus.view.setVisibility(View.VISIBLE);

        binding.viewSplitter.counter.setText("3");
        binding.viewSplitter.counter.setVisibility(View.GONE);
        binding.viewSplitter.view.setVisibility(View.VISIBLE);

        binding.viewBlank.counter.setText("3");
        binding.viewBlank.counter.setVisibility(View.GONE);
        binding.viewBlank.view.setVisibility(View.VISIBLE);

        binding.viewDelete.counter.setText("3");
        binding.viewDelete.counter.setVisibility(View.GONE);
        binding.viewDelete.view.setVisibility(View.VISIBLE);

        binding.viewStickers.counter.setText("3");
        binding.viewStickers.counter.setVisibility(View.GONE);
        binding.viewStickers.view.setVisibility(View.VISIBLE);
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