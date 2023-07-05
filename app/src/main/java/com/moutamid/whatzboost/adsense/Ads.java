package com.moutamid.whatzboost.adsense;

import static com.facebook.ads.AdSettings.IntegrationErrorMode.INTEGRATION_ERROR_CRASH_DEBUG_MODE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.ads.Ad;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;
import com.fxn.stash.Stash;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.moutamid.whatzboost.R;

public class Ads {

    // Ad Mob Variables
    public static InterstitialAd mInterstitialAd;
    public static RewardedAd rewardedAd;
    private static RewardedInterstitialAd rewardedInterstitialAd;
    public static AdRequest adRequest = new AdRequest.Builder().build();

    // Facebook Ads Variables
    public static com.facebook.ads.InterstitialAd finterstitialAd;
    public static RewardedVideoAd frewardedVideoAd;

    // Facebook Listeners
    public static InterstitialAdListener interstitialAdListener;
    public static RewardedVideoAdListener rewardedVideoAdListener;

    // Constants

    public static String TAG = "ADS_MANAG";
    public static String IS_ADMOB = "IS_ADMOB";
    public static String ADMOB_INTERSTITIAL = "ADMOB_INTERSTITIAL";
    public static String ADMOB_REWARDED_VIDEO = "ADMOB_REWARDED_VIDEO";
    public static String FACEBOOK_INTERSTITIAL = "FACEBOOK_INTERSTITIAL";
    public static String FACEBOOK_REWARDED_VIDEO = "FACEBOOK_REWARDED_VIDEO";

    public static String api = "https://raw.githubusercontent.com/Moutamid/WhatzBoost/master/app/app.txt";

    public static void calledIniti(Context context){
        MobileAds.initialize(context, initializationStatus -> {
            // loadRewardedInterstitialAd(context);
        });

    }

    public static void facebookInititalize(Context context){
        AudienceNetworkAds.initialize(context);
        AdSettings.setIntegrationErrorMode(INTEGRATION_ERROR_CRASH_DEBUG_MODE);
        finterstitialAd = new com.facebook.ads.InterstitialAd(context, Stash.getString(FACEBOOK_INTERSTITIAL));
        frewardedVideoAd = new RewardedVideoAd(context, Stash.getString(FACEBOOK_REWARDED_VIDEO));
        String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        AdSettings.addTestDevice(deviceId);
    }


    public static void loadRewardedInterstitialAd(Context context) {
        // Use the test ad unit ID to load an ad.
        RewardedInterstitialAd.load(context, "ca-app-pub-3940256099942544/5354046379",
                new AdRequest.Builder().build(),  new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(RewardedInterstitialAd ad) {
                        Log.d(TAG, "Ad was loaded.");
                        rewardedInterstitialAd = ad;
                        ServerSideVerificationOptions options = new ServerSideVerificationOptions
                                .Builder()
                                .setCustomData("SAMPLE_CUSTOM_DATA_STRING")
                                .build();
                        rewardedInterstitialAd.setServerSideVerificationOptions(options);
                    }
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        Log.d(TAG, loadAdError.toString());
                        rewardedInterstitialAd = null;
                    }
                });
    }

    public static void showRewardedInterstitialAd(Context context, Activity activity, Class intent){
        if (rewardedInterstitialAd != null){
            rewardedInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    // Called when a click is recorded for an ad.
                    Log.d(TAG, "Ad was clicked.");
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Set the ad reference to null so you don't show the ad a second time.
                    Log.d(TAG, "Ad dismissed fullscreen content.");
                    rewardedInterstitialAd = null;
                    context.startActivity(new Intent(context, intent));
                    activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when ad fails to show.
                    Log.e(TAG, "Ad failed to show fullscreen content.");
                    rewardedInterstitialAd = null;
                }

                @Override
                public void onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    Log.d(TAG, "Ad recorded an impression.");
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    Log.d(TAG, "Ad showed fullscreen content.");
                }
            });

            rewardedInterstitialAd.show(activity, rewardItem -> {
                Log.i(TAG, "User earned reward.");
            });
        } else {
            context.startActivity(new Intent(context, intent));
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

    }

    public static void loadFacebookRewarded(){
        frewardedVideoAd.loadAd(
                frewardedVideoAd.buildLoadAdConfig()
                        .withAdListener(rewardedVideoAdListener)
                        .build());

    }

    public static void showFacebookRewarded(Context context, Activity activity, Class intent){
        if(frewardedVideoAd.isAdInvalidated()) {
            context.startActivity(new Intent(context, intent));
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else {
            if(frewardedVideoAd != null || frewardedVideoAd.isAdLoaded()) {
                frewardedVideoAd.show();
            } else {
                context.startActivity(new Intent(context, intent));
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }
    }

    public static void setFacebookRewardedListener(Context context, Activity activity, Class intent){
        rewardedVideoAdListener = new RewardedVideoAdListener() {
            @Override
            public void onError(Ad ad, com.facebook.ads.AdError error) {
                // Rewarded video ad failed to load
                Log.e(TAG, "Rewarded video ad failed to load: " + error.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Rewarded video ad is loaded and ready to be displayed
                Log.d(TAG, "Rewarded video ad is loaded and ready to be displayed!");
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Rewarded video ad clicked
                Log.d(TAG, "Rewarded video ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Rewarded Video ad impression - the event will fire when the
                // video starts playing
                Log.d(TAG, "Rewarded video ad impression logged!");
            }

            @Override
            public void onRewardedVideoCompleted() {
                // Rewarded Video View Complete - the video has been played to the end.
                // You can use this event to initialize your reward
                Log.d(TAG, "Rewarded video completed!");

                // Call method to give reward
                // giveReward();
            }

            @Override
            public void onRewardedVideoClosed() {
                // The Rewarded Video ad was closed - this can occur during the video
                // by closing the app, or closing the end card.
                Log.d(TAG, "Rewarded video ad closed!");
                context.startActivity(new Intent(context, intent));
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        };
    }

    public static void setFacebookInterstitialListener(Context context, Activity activity, Class intent){
        interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
                context.startActivity(new Intent(context, intent));
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }

            @Override
            public void onError(Ad ad, com.facebook.ads.AdError adError) {
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        };
    }

    public static void loadFacebookIntersAd(){
        finterstitialAd.loadAd(
                finterstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());

    }

    public static void showFacebookInters(Context context, Activity activity, Class intent){
        if(finterstitialAd.isAdInvalidated()) {
            context.startActivity(new Intent(context, intent));
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else {
            if(finterstitialAd != null || finterstitialAd.isAdLoaded()) {
                finterstitialAd.show();
            } else {
                context.startActivity(new Intent(context, intent));
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }
    }

    public static void loadIntersAD(Context context) {

        InterstitialAd.load(context, Stash.getString(ADMOB_INTERSTITIAL), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;

                    }
                });
    }

    public static void showInterstitial(Context context, Activity activity, Class intent){
        if (mInterstitialAd != null) {
            mInterstitialAd.show(activity);
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    context.startActivity(new Intent(context, intent));
                    activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            });
        } else {
            context.startActivity(new Intent(context, intent));
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    public static void loadRewardedAD(Context context){
        RewardedAd.load(context, Stash.getString(ADMOB_REWARDED_VIDEO),
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, "Error Rewarded : " + loadAdError.toString());
                        rewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        Log.d(TAG, "Ad was loaded.");
                        ServerSideVerificationOptions options = new ServerSideVerificationOptions
                                .Builder()
                                .setCustomData("SAMPLE_CUSTOM_DATA_STRING")
                                .build();
                        rewardedAd.setServerSideVerificationOptions(options);

                    }
                });
    }

    public static void showRewarded(Context context, Activity activity, Class intent){
        if (rewardedAd != null) {
            setCallBack(context, activity, intent);
            rewardedAd.show(activity, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d(TAG, "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                }
            });
        } else {
            context.startActivity(new Intent(context, intent));
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    private static void setCallBack(Context context, Activity activity, Class intent) {
        rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad dismissed fullscreen content.");
                rewardedAd = null;
                context.startActivity(new Intent(context, intent));
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.");
                rewardedAd = null;
            }

            @Override
            public void onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.");
            }
        });
    }

}
