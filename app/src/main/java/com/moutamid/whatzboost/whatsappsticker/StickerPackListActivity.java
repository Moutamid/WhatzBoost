package com.moutamid.whatzboost.whatsappsticker;

import static com.facebook.imageutils.HeifExifUtil.TAG;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fxn.stash.Stash;
import com.google.android.material.card.MaterialCardView;
import com.moutamid.whatzboost.MainActivity;
import com.moutamid.whatzboost.R;
import com.moutamid.whatzboost.constants.Constants;
import com.moutamid.whatzboost.models.SearchModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StickerPackListActivity extends AddStickerPackkkActivity {
    public static final String EXTRA_STICKER_PACK_LIST_DATA = "sticker_pack_list";

    public StickerPackListAdapter allStickerPacksListAdapter;
    private ImageView ivBack;
//    private AdView llAds;
    private final StickerPackListAdapter.OnAddButtonClickedListener onAddButtonClickedListener = new StickerPackListAdapter.OnAddButtonClickedListener() {
        public final void onAddButtonClicked(StickerPack stickerPack) {
            addStickerPackToWhatsApp(stickerPack.identifier, stickerPack.name);
        }
    };
    private LinearLayoutManager packLayoutManager;
    private RecyclerView packRecyclerView;
    private ArrayList<StickerPack> stickerPackList;
    private TextView tv_title;
    private WhiteListCheckAsyncTask whiteListCheckAsyncTask;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_sticker_pack_list);


        MaterialCardView backBtn = findViewById(R.id.backbtn);

        ArrayList<SearchModel> recents = Stash.getArrayList(Constants.RECENTS_LIST, SearchModel.class);
        SearchModel model = new SearchModel(R.drawable.sticker, "Stickers");

        if (recents.size() == 0){
            recents.add(model);
            Stash.put(Constants.RECENTS_LIST, recents);
        } else {
            boolean check = false;
            Collections.reverse(recents);
            int size = recents.size() > 6 ? 6 : recents.size();
            for (int i=0; i<size; i++){
                if (!recents.get(i).getName().equals(model.getName())){
                    check = true;
                } else {
                    check = false;
                    break;
                }
            }
            if (check){
                recents.add(model);
                Stash.put(Constants.RECENTS_LIST, recents);
            }
        }


        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

//        this.llAds = (AdView) findViewById(R.id.ll_ads);
//        Ads.bannerad(this.llAds, this);
        this.packRecyclerView = (RecyclerView) findViewById(R.id.sticker_pack_list);
        this.stickerPackList = getIntent().getParcelableArrayListExtra(EXTRA_STICKER_PACK_LIST_DATA);
        showStickerPackList(this.stickerPackList);
    }


    @Override
    public void onResume() {
        super.onResume();
        this.whiteListCheckAsyncTask = new WhiteListCheckAsyncTask(this);
        WhiteListCheckAsyncTask whiteListCheckAsyncTask2 = this.whiteListCheckAsyncTask;
        ArrayList<StickerPack> arrayList = this.stickerPackList;
        whiteListCheckAsyncTask2.execute(arrayList.toArray(new StickerPack[arrayList.size()]));
    }


    @Override
    public void onPause() {
        super.onPause();
        WhiteListCheckAsyncTask whiteListCheckAsyncTask2 = this.whiteListCheckAsyncTask;
        if (whiteListCheckAsyncTask2 != null && !whiteListCheckAsyncTask2.isCancelled()) {
            this.whiteListCheckAsyncTask.cancel(true);
        }
    }

    private void showStickerPackList(List<StickerPack> list) {
        this.allStickerPacksListAdapter = new StickerPackListAdapter(list, this.onAddButtonClickedListener);
        this.packRecyclerView.setAdapter(this.allStickerPacksListAdapter);
        this.packLayoutManager = new LinearLayoutManager(this);
        this.packLayoutManager.setOrientation(RecyclerView.VERTICAL);
       // this.packRecyclerView.addItemDecoration(new DividerItemDecoration(this.packRecyclerView.getContext(), this.packLayoutManager.getOrientation()));
        this.packRecyclerView.setLayoutManager(this.packLayoutManager);
        this.packRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public final void onGlobalLayout() {
                StickerPackListActivity.this.recalculateColumnCount();
            }
        });
    }



    public void recalculateColumnCount() {
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.sticker_pack_list_item_preview_image_size);
        StickerPackListItemViewHolder stickerPackListItemViewHolder = (StickerPackListItemViewHolder) this.packRecyclerView.findViewHolderForAdapterPosition(this.packLayoutManager.findFirstVisibleItemPosition());
        if (stickerPackListItemViewHolder != null) {
            this.allStickerPacksListAdapter.setMaxNumberOfStickersInARow(Math.min(5, Math.max(stickerPackListItemViewHolder.imageRowView.getMeasuredWidth() / dimensionPixelSize, 1)));
        }
    }

    static class WhiteListCheckAsyncTask extends AsyncTask<StickerPack, Void, List<StickerPack>> {
        private final WeakReference<StickerPackListActivity> stickerPackListActivityWeakReference;

        WhiteListCheckAsyncTask(StickerPackListActivity stickerPackListActivity) {
            this.stickerPackListActivityWeakReference = new WeakReference<>(stickerPackListActivity);
        }


        public final List<StickerPack> doInBackground(StickerPack... stickerPackArr) {
            StickerPackListActivity stickerPackListActivity = (StickerPackListActivity) this.stickerPackListActivityWeakReference.get();
            if (stickerPackListActivity == null) {
                return Arrays.asList(stickerPackArr);
            }
            for (StickerPack stickerPack : stickerPackArr) {
                stickerPack.setIsWhitelisted(WhitelistCheck.isWhitelisted(stickerPackListActivity, stickerPack.identifier));
            }
            return Arrays.asList(stickerPackArr);
        }


        @Override
        public void onPostExecute(List<StickerPack> list) {
            StickerPackListActivity stickerPackListActivity = (StickerPackListActivity) this.stickerPackListActivityWeakReference.get();
            if (stickerPackListActivity != null) {
                stickerPackListActivity.allStickerPacksListAdapter.setStickerPackList(list);
                stickerPackListActivity.allStickerPacksListAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
