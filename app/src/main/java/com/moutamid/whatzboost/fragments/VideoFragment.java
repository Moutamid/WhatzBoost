package com.moutamid.whatzboost.fragments;

import static android.os.Build.VERSION.SDK_INT;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.fxn.stash.Stash;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.adapters.StatusAdapter;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.databinding.FragmentVideoBinding;
import com.moutamid.whatzboost.models.StatusItem;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class VideoFragment extends Fragment {
    FragmentVideoBinding binding;
    List<StatusItem> statusItems = new ArrayList<>();
    StatusAdapter adapter;
    private ProgressBar progressBar;

    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVideoBinding.inflate(getLayoutInflater(), container, false);
        binding.recycler.setLayoutManager(new GridLayoutManager(requireContext(), 1));
        binding.recycler.setHasFixedSize(false);

        progressBar = binding.pbProgress;

        return binding.getRoot();
    }

    private void getItems() {
        List<StatusItem> allVideoItems = new ArrayList<>();

        statusItems.clear();
        allVideoItems = Constants.allWAVideoItems;

        List<StatusItem> finalAllVideoItems = allVideoItems;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (StatusItem item : finalAllVideoItems) {
                    if (new File(Constants.SAVED_FOLDER, item.getFileName()).exists())
                        item.setDownloaded(true);
                    statusItems.add(item);
                    //recentAdapter.notifyItemChanged(statusItems.size() - 1);
                }

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!statusItems.isEmpty()) binding.noStatusLayout.setVisibility(View.GONE);
                        else binding.noStatusLayout.setVisibility(View.VISIBLE);
                        adapter = new StatusAdapter(getContext(), statusItems);
                        binding.recycler.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        }).start();
    }

    private void getStatus() {
        DocumentFile[] allDocumentFiles = getFromSdcard();
        List<StatusItem> tempItems = new ArrayList<>();
        binding.noStatusLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (allDocumentFiles != null) {

                    for (DocumentFile documentFile : allDocumentFiles) {
                        if (!documentFile.getUri().toString().contains(".nomedia"))
                            if (documentFile.getUri().toString().endsWith(".mp4")
                                    || documentFile.getUri().toString().endsWith(".3gp")) {

                                StatusItem item = new StatusItem();
                                item.setFileUri(documentFile.getUri());
                                item.setFileName(documentFile.getName());
                                item.setTimeStamp(documentFile.lastModified());
                                item.setImage(false);
                                if (new File(Constants.SAVED_FOLDER, item.getFileName()).exists())
                                    item.setDownloaded(true);

                                tempItems.add(item);
                            }
                    }
                }

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        statusItems.clear();
                        statusItems.addAll(tempItems);
                        if (SDK_INT >= Build.VERSION_CODES.N) {
                            Collections.sort(statusItems, Comparator.comparing(StatusItem::getTimeStamp));
                        }
                        Collections.reverse(statusItems);
                        Constants.allWAVideoItems.clear();
                        Constants.allWAVideoItems.addAll(statusItems);

                        progressBar.setVisibility(View.GONE);
                        if (!statusItems.isEmpty()) binding.noStatusLayout.setVisibility(View.GONE);
                        else binding.noStatusLayout.setVisibility(View.VISIBLE);
                        adapter = new StatusAdapter(getContext(), statusItems);
                        binding.recycler.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        }).start();
    }

    private void getItemsonLower() {
        File whatsAppFolderStatus = new File(Environment.getExternalStorageDirectory().getPath() + "/WhatsApp/Media/.Statuses");
        File path = whatsAppFolderStatus;
        if (path.listFiles() != null) {
            File[] files = path.listFiles();
            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            for (File mfile : files) {
                if (!mfile.getName().startsWith(".")) {
                    if (!statusItems.contains(mfile) && !mfile.isDirectory()) {

                        StatusItem mediaModel = new StatusItem();
                        mediaModel.setFilePath(mfile.getPath());
                        mediaModel.setFileName(mfile.getName());
                        mediaModel.setSelected(false);
                        mediaModel.setImage(false);
                        mediaModel.setTimeStamp(mfile.lastModified());
                        statusItems.add(mediaModel);
                        if (!statusItems.isEmpty()) binding.noStatusLayout.setVisibility(View.GONE);
                        else binding.noStatusLayout.setVisibility(View.VISIBLE);
                        adapter = new StatusAdapter(getContext(), statusItems);
                        binding.recycler.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    private DocumentFile[] getFromSdcard() {
        String str = Stash.getString(Constants.WaSavedRoute);

        DocumentFile documentFile = DocumentFile.fromTreeUri(requireContext().getApplicationContext(), Uri.parse(str));
        return (documentFile != null && documentFile.exists()) ? documentFile.listFiles() : null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SDK_INT >= Build.VERSION_CODES.R) {
            getStatus();
        } else {
            //getItems();
            getItemsonLower();
        }
    }

}