package com.rt12148.redditsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubredditContentActivity extends AppCompatActivity implements Callback<JsonObject>{

    private List<SubredditContent> mData = new ArrayList<>();
    private SubredditContentAdapter mAdapter;
    private String mSubreddit;
    private ProgressWheel mProgressWheel;
    private TextView mEmptyMessage;
    private MenuItem showList, showGrid;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        mEmptyMessage = (TextView) findViewById(R.id.empty_message);

        mRecyclerView = (RecyclerView) findViewById(R.id.subreddit_list);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        mAdapter = new SubredditContentAdapter(this, mData);
        mRecyclerView.setAdapter(mAdapter);
        mSubreddit = getIntent().getExtras().getString("subreddit","");
        setTitle(mSubreddit);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if(MyApplication.getInstance().isNetworkAvailable()) {
            getContent();
        } else {
            Realm realm =  MyApplication.getInstance().getRealm();
            RealmResults<SubredditContent> results = realm.where(SubredditContent.class).equalTo("subreddit", mSubreddit).findAll();
            for(SubredditContent content: results) {
                mData.add(content);
                mAdapter.notifyItemInserted(mData.size()-1);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.subreddit_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        showList = menu.findItem(R.id.menu_show_list);
        showGrid = menu.findItem(R.id.menu_show_grid);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_show_grid:
                showGrid.setVisible(false);
                showList.setVisible(true);
                mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                break;
            case R.id.menu_show_list:
                showGrid.setVisible(true);
                showList.setVisible(false);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void getContent(){
        mProgressWheel.setVisibility(View.VISIBLE);
        ApiService apiService = MyApplication.getInstance().getRetrofit();
        Call<JsonObject> call = apiService.getSubreddit(mSubreddit);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        mProgressWheel.setVisibility(View.GONE);
        if(response.code() == 200) {
            JsonObject jsonObject = response.body();
            JsonObject dataJson = jsonObject.get("data").getAsJsonObject();
            JsonArray children = dataJson.get("children").getAsJsonArray();
            JsonArray content = new JsonArray();
            for(int i = 0; i < children.size(); i++){
                content.add(children.get(i).getAsJsonObject().get("data").getAsJsonObject());
            }
            Gson gson = new Gson();
            List<SubredditContent> list = gson.fromJson(content,new TypeToken<List<SubredditContent>>(){}.getType());
            if(list == null || list.isEmpty()) {
                mEmptyMessage.setText(mAdapter.getItemCount() == 0 ? "No content for this subreddit" : "");
                return;
            }
            mEmptyMessage.setText(null);
            Realm realm = MyApplication.getInstance().getRealm();
            realm.beginTransaction();
            for(SubredditContent subredditContent: list){
                if(TextUtils.isEmpty(subredditContent.getSelfText()) && !TextUtils.isEmpty(subredditContent.getThumbnail())) {
                    if(subredditContent.getThumbnail().matches(Patterns.WEB_URL.pattern())) {
                        subredditContent.setSubreddit(mSubreddit);
                        realm.copyToRealm(subredditContent);
                        mData.add(subredditContent);
                        mAdapter.notifyItemInserted(mData.size() - 1);
                    }
                }
            }
            realm.commitTransaction();
            if(mData.isEmpty()) {
                mEmptyMessage.setText(mAdapter.getItemCount() == 0 ? "No content for this subreddit" : "");
            }
        } else {
            mEmptyMessage.setText(mAdapter.getItemCount() == 0 ? "Error Please try later" : "");
        }
    }

    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {
        mProgressWheel.setVisibility(View.GONE);
        mEmptyMessage.setText(mAdapter.getItemCount() == 0 ? "Error Please try later" : "");
    }
}
