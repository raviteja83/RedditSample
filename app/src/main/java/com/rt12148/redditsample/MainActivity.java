package com.rt12148.redditsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;

public class MainActivity extends AppCompatActivity {

    String[] subreddits = {"alternativeart", "pics", "gifs", "adviceanimals", "cats", "images", "photoshopbattles", "hmmm", "all", "aww"};
    List<SubReddit> subRedditList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm realm =  MyApplication.getInstance().getRealm();
        if(realm.where(SubReddit.class).count() > 0) {
            subRedditList = realm.where(SubReddit.class).equalTo("active",true).findAll();
        }else {
            realm.beginTransaction();
            for(String str: subreddits) {
                SubReddit subReddit = new SubReddit();
                subReddit.setSubreddit(str);
                subReddit.setActive(true);
                subRedditList.add(subReddit);
                realm.copyToRealm(subReddit);
            }
            realm.commitTransaction();
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.subreddit_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(dividerItemDecoration);
        final SubredditListAdapter adapter = new SubredditListAdapter(this,subRedditList);
        recyclerView.setAdapter(adapter);
        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm element) {
                subRedditList = element.where(SubReddit.class).equalTo("active",true).findAll();
                adapter.notifyDataSetChanged();
            }
        });

    }
}
