package com.moutamid.whatzboost.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.fxn.stash.Stash;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.adapters.SearchAdapter;
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
import com.moutamid.whatzboost.ui.SearchActivity;
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
import java.util.Collections;
import java.util.Iterator;

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
        getData();

        return binding.getRoot();
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
        public void click(String Name) {
            switch (Name) {
                case "Deleted Messages":
                    startActivity(new Intent(requireContext(), DeletedMessageActivity.class));
                    //finish();
                    break;
                case "Video Splitter":
                    startActivity(new Intent(requireContext(), VideoSplitterActivity.class));
                    // finish();
                    break;
                case "Whatsapp Web":
                    startActivity(new Intent(requireContext(), WhatsWebActivity.class));
                    //  finish();
                    break;
                case "Direct Chat":
                    startActivity(new Intent(requireContext(), DirectActivity.class));
                    // finish();
                    break;
                case "Status Saver":
                    startActivity(new Intent(requireContext(), StatusSaverActivity.class));
                    // finish();
                    break;
                case "Text Repeater":
                    startActivity(new Intent(requireContext(), RepeaterActivity.class));
                    //  finish();
                    break;
                case "Make Profile":
                    startActivity(new Intent(requireContext(), MakeProfileActivity.class));
                    //finish();
                    break;
                case "Make Stories":
                    startActivity(new Intent(requireContext(), MakeStoryActivity.class));
                    // finish();
                    break;
                case "Stickers":
                    progressDialog.show();
                    Fresco.initialize(requireContext());
                    loadListAsyncTask = new LoadListAsyncTask(requireActivity());
                    loadListAsyncTask.execute(new Void[0]);
                    break;
                case "Caption":
                    startActivity(new Intent(requireContext(), CaptionListActivity.class));
                    //finish();
                    break;
                case "Emotions":
                    startActivity(new Intent(requireContext(), EmotionsActivity.class));
                    // finish();
                    break;
                case "Text to Emoji":
                    startActivity(new Intent(requireContext(), TextToEmojiActivity.class));
                    // finish();
                    break;
                case "Qr Generator":
                    startActivity(new Intent(requireContext(), QrGeneratorActivity.class));
                    // finish();
                    break;
                case "Qr Scanner":
                    startActivity(new Intent(requireContext(), QrScannerActivity.class));
                    // finish();
                    break;
                case "Blank Message":
                    startActivity(new Intent(requireContext(), BlankMessageActivity.class));
                    // finish();
                    break;
                case  "Font Fun":
                    startActivity(new Intent(requireContext(), FontFunActivity.class));
                    // finish();
                    break;
                case "Insta ReShare":
                    startActivity(new Intent(requireContext(), InstaReshareActivity.class));
                    // finish();
                    break;
                default:
                    break;
            }
        }
    };

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