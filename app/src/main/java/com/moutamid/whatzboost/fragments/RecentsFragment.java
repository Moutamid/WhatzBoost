package com.moutamid.whatzboost.fragments;

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
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
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
import com.moutamid.whatzboost.adapters.SearchAdapter;
import com.moutamid.whatzboost.adsense.Ads;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.FragmentRecentsBinding;
import com.moutamid.whatzboost.listners.SearchLister;
import com.moutamid.whatzboost.models.SearchModel;
import com.moutamid.whatzboost.ui.BlankMessageActivity;
import com.moutamid.whatzboost.ui.CaptionListActivity;
import com.moutamid.whatzboost.ui.DeletedMessageActivity;
import com.moutamid.whatzboost.ui.DirectActivity;
import com.moutamid.whatzboost.ui.EmotionsActivity;
import com.moutamid.whatzboost.ui.FontFunActivity;
import com.moutamid.whatzboost.ui.InstaReshareActivity;
import com.moutamid.whatzboost.ui.MakeProfileActivity;
import com.moutamid.whatzboost.ui.MakeStoryActivity;
import com.moutamid.whatzboost.ui.QrGeneratorActivity;
import com.moutamid.whatzboost.ui.QrScannerActivity;
import com.moutamid.whatzboost.ui.RepeaterActivity;
import com.moutamid.whatzboost.ui.ShayariActivity;
import com.moutamid.whatzboost.ui.StatusSaverActivity;
import com.moutamid.whatzboost.ui.TextToEmojiActivity;
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

public class RecentsFragment extends Fragment {
    FragmentRecentsBinding binding;
    ProgressDialog progressDialog;
    public RecentsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRecentsBinding.inflate(getLayoutInflater(), container, false);

        Stash.put(Constants.RECENTS, true);
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        //getData();

        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        ArrayList<SearchModel> list = new ArrayList<>();
        ArrayList<SearchModel> temp = Stash.getArrayList(Constants.RECENTS_LIST, SearchModel.class);
       // Collections.reverse(temp);
        if (temp.size() > 6){
            for (int i =0; i<6; i++) {
                list.add(temp.get(i));
            }
        } else {
            list = new ArrayList<>(temp);
        }
        SearchAdapter adapter = new SearchAdapter(requireContext(), list, searchLister);
        binding.recycler.setAdapter(adapter);
    }

    SearchLister searchLister = new SearchLister() {
        @Override
        public void click(String Name, boolean showAd, View dot, TextView view_counter) {
            switch (Name) {
                case "Deleted\nMessages":
                    if (showAd){
                        startCounter(view_counter, DeletedMessageActivity.class, dot);
                    } else startActivity(new Intent(requireContext(), DeletedMessageActivity.class));
                    //finish();
                    break;
                case "Video\nSplitter":
                    if (showAd){
                        startCounter(view_counter, VideoSplitterActivity.class, dot);
                    } else startActivity(new Intent(requireContext(), VideoSplitterActivity.class));
                    // finish();
                    break;
                case "Whatsapp\nWeb":
                    if (showAd){
                        startCounter(view_counter, WhatsWebActivity.class, dot);
                    } else startActivity(new Intent(requireContext(), WhatsWebActivity.class));
                    //  finish();
                    break;
                case "Open\nWA Profile":
                    if (showAd){
                        startCounter(view_counter, DirectActivity.class, dot);
                    } else startActivity(new Intent(requireContext(), DirectActivity.class));
                    // finish();
                    break;
                case "Status\nSaver":
                    if (showAd){
                        startCounter(view_counter, StatusSaverActivity.class, dot);
                    } else startActivity(new Intent(requireContext(), StatusSaverActivity.class));
                    // finish();
                    break;
                case "Text\nRepeater":
                    if (showAd){
                        startCounter(view_counter, RepeaterActivity.class, dot);
                    } else startActivity(new Intent(requireContext(), RepeaterActivity.class));
                    //  finish();
                    break;
                case "Fake\nProfile":
                    if (showAd){
                        startCounter(view_counter, MakeProfileActivity.class, dot);
                    } else startActivity(new Intent(requireContext(), MakeProfileActivity.class));
                    //finish();
                    break;
                case "Fake\nStories":
                    if (showAd){
                        startCounter(view_counter, MakeStoryActivity.class, dot);
                    } else startActivity(new Intent(requireContext(), MakeStoryActivity.class));
                    // finish();
                    break;
                case "Stickers\n ":
                    if (showAd) {
                        if (Stash.getBoolean(Constants.GUIDE_AD, true)) {
                            dot.setVisibility(View.GONE);
                            view_counter.setVisibility(View.VISIBLE);
                            showAdGuide(view_counter);
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
                case "Caption\n ":
                    if (showAd){
                        startCounter(view_counter, CaptionListActivity.class, dot);
                    } else startActivity(new Intent(requireContext(), CaptionListActivity.class));
                    //finish();
                    break;
                case "Emotions\n ":
                    if (showAd){
                        startCounter(view_counter, EmotionsActivity.class, dot);
                    } else startActivity(new Intent(requireContext(), EmotionsActivity.class));
                    // finish();
                    break;
                case "Poetry\n ":
                    if (showAd){
                        startCounter(view_counter, ShayariActivity.class, dot);
                    } else startActivity(new Intent(requireContext(), ShayariActivity.class));
                    // finish();
                    break;
                case "Text-to-Emoji":
                    if (showAd){
                        startCounter(view_counter, TextToEmojiActivity.class, dot);
                    } else startActivity(new Intent(requireContext(), TextToEmojiActivity.class));
                    // finish();
                    break;
                case "Qr\nGenerator":
                    if (showAd){
                        startCounter(view_counter, QrGeneratorActivity.class, dot);
                    } else startActivity(new Intent(requireContext(), QrGeneratorActivity.class));
                    // finish();
                    break;
                case "Qr\nScanner":
                    if (showAd){
                        startCounter(view_counter, QrScannerActivity.class, dot);
                    } else startActivity(new Intent(requireContext(), QrScannerActivity.class));
                    // finish();
                    break;
                case "Blank\nMessage":
                    if (showAd){
                        startCounter(view_counter, BlankMessageActivity.class, dot);
                    } else startActivity(new Intent(requireContext(), BlankMessageActivity.class));
                    // finish();
                    break;
                case  "Font\nFun":
                    if (showAd){
                        startCounter(view_counter, FontFunActivity.class, dot);
                    } else startActivity(new Intent(requireContext(), FontFunActivity.class));
                    // finish();
                    break;
                case "Insta\nReShare":
                    if (showAd){
                        startCounter(view_counter, InstaReshareActivity.class, dot);
                    } else startActivity(new Intent(requireContext(), InstaReshareActivity.class));
                    // finish();
                    break;
                default:
                    break;
            }
        }
    };

    private void startCounter(TextView view_counter, Class intent, View dot) {
        if (Stash.getBoolean(Constants.GUIDE_AD, true)) {
            Constants.showAdGuide(requireContext(), requireActivity(), intent, dot, view_counter);
        } else {
            int inter_ratio = Stash.getInt(Constants.INTERSTITIAL_RATIO, 7);
            int rewarded_ratio = Stash.getInt(Constants.REWARDED_RATIO, 3);
            int totalRatio = inter_ratio + rewarded_ratio;
            int randomNumber = new Random().nextInt(totalRatio) + 1;
            if (Stash.getBoolean(Ads.IS_ADMOB)) {
                if (randomNumber <= inter_ratio) {
                    Ads.showInterstitial(requireContext(), requireActivity(), intent);
                } else {
                    Ads.showRewarded(requireContext(), requireActivity(), intent);
                }
            } else {
                if (randomNumber <= inter_ratio) {
                    Ads.setFacebookInterstitialListener(requireContext(), requireActivity(), intent);
                    Ads.showFacebookInters(requireContext(), requireActivity(), intent);
                } else {
                    Ads.setFacebookRewardedListener(requireContext(), requireActivity(), intent);
                    Ads.showFacebookRewarded(requireContext(), requireActivity(), intent);
                }
            }
        }
    }

    public void showAdGuide(TextView view_counter) {
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
                    view_counter.setText(String.valueOf((millisUntilFinished / 1000) + 1));
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

    public LoadListAsyncTask loadListAsyncTask;

    public class LoadListAsyncTask extends AsyncTask<Void, Void, Pair<String, ArrayList<StickerPack>>> {
        private final WeakReference<Activity> contextWeakReference;

        LoadListAsyncTask(Activity mainActivity) {
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
                showErrorMessagee((String) pair.first);
            } else {
                showStickerPackk((ArrayList) pair.second);
            }
        }
    }

    public void showErrorMessagee(String str) {
        progressDialog.dismiss();
        Log.e("Main activity", "error fetching sticker packs, " + str);
        Toast.makeText(requireContext(), "" + str, Toast.LENGTH_SHORT).show();
    }

    public void showStickerPackk(ArrayList<StickerPack> arrayList) {
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