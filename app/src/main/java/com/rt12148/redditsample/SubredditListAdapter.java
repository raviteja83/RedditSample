package com.rt12148.redditsample;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.realm.Realm;

/**
 * Created by rt12148 on 23/3/17.
 **/
class SubredditListAdapter extends RecyclerView.Adapter<SubredditListAdapter.ViewHolder> {

    private Context context;
    private List<SubReddit> data;
    private Realm realm;

    SubredditListAdapter(Context context, List<SubReddit>  data) {
        this.context = context;
        this.data = data;
        realm = MyApplication.getInstance().getRealm();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.subreddit_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.subredditTitle.setText(data.get(position).getSubreddit());
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView subredditTitle;
        ImageView cancel;

        ViewHolder(View itemView) {
            super(itemView);
            subredditTitle = (TextView) itemView.findViewById(R.id.subreddit_title);
            cancel = (ImageView) itemView.findViewById(R.id.cancel_subreddit);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,SubredditContentActivity.class);
                    intent.putExtra("subreddit", data.get(getLayoutPosition()).getSubreddit());
                    context.startActivity(intent);
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(realm.where(SubReddit.class).equalTo("active", true).count() <= 3) {
                        Toast.makeText(context,"You should subscribe atleast 3 subreddits", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    realm.beginTransaction();
                    SubReddit subReddit = data.get(getLayoutPosition());
                    subReddit.setActive(false);
                    realm.insertOrUpdate(subReddit);
                    realm.commitTransaction();
                }
            });
        }
    }
}
