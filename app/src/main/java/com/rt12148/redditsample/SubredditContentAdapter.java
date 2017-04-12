package com.rt12148.redditsample;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

class SubredditContentAdapter extends RecyclerView.Adapter<SubredditContentAdapter.ViewHolder> {

    private Context context;
    private List<SubredditContent> data;

    SubredditContentAdapter(Context context, List<SubredditContent> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.subreddit_content_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SubredditContent content = data.get(position);
        if(TextUtils.isEmpty(content.getThumbnail()) || content.getThumbnail().equals("default")) {
            holder.subredditImage.setVisibility(View.GONE);
        } else {
            holder.subredditImage.setVisibility(View.VISIBLE);
            Picasso.with(context).load(content.getThumbnail()).into(holder.subredditImage);
        }
        holder.subredditTitle.setText(content.getTitle());
        holder.subredditComments.setText(String.valueOf(content.getNumComments()));
        holder.subredditUps.setText(String.valueOf(content.getUps()));
        holder.subredditDowns.setText(String.valueOf(content.getDowns()));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView subredditTitle;
        TextView subredditComments;
        TextView subredditUps;
        TextView subredditDowns;
        ImageView subredditImage;

        ViewHolder(View itemView) {
            super(itemView);
            subredditImage = (ImageView) itemView.findViewById(R.id.subreddit_content_image);
            subredditTitle = (TextView) itemView.findViewById(R.id.subreddit_content_title);
            subredditComments = (TextView) itemView.findViewById(R.id.subreddit_content_comments);
            subredditUps = (TextView) itemView.findViewById(R.id.subreddit_content_ups);
            subredditDowns = (TextView) itemView.findViewById(R.id.subreddit_content_downs);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SubredditContent subredditContent = data.get(getLayoutPosition());
                    SubredditContent.Preview preview = subredditContent.getPreview();
                    List<SubredditContent.Images> images = preview.getImages();
                    if(images == null || images.isEmpty()) return;
                    SubredditContent.Source source = images.get(0).getSource();
                    String url =  source.getUrl();
                    if(!TextUtils.isEmpty(url) && url.matches(Patterns.WEB_URL.pattern())) {
                        Intent intent = new Intent(context, PreviewActivity.class);
                        intent.putExtra("url", url);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "No Preview Available", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
