package com.example.mannas.ytask ;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mannas.ytask.Content.Contract;
import com.example.mannas.ytask.Content.Feed;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mannas on 6/19/2017.
 */

public class MainFeedsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Feed> mFeeds;
    OnFeedClickListener m_onFeedClickListener;

    public MainFeedsAdapter(ArrayList<Feed> Feeds , OnFeedClickListener feedClickListener) {
        mFeeds = Feeds;
        m_onFeedClickListener = feedClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                return new Holder1(LayoutInflater.from(parent.getContext()).inflate(R.layout.row1, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 1: {
                final Feed f = mFeeds.get(position);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        m_onFeedClickListener.OnClick(f);
                    }
                });


                if(f.getMultimedia().length < 4) {
                    if(f.getMultimedia().length >=1){
                        Picasso.with(holder.itemView.getContext())
                                .load(f.getMultimedia()[0].getUrl()).placeholder(R.drawable.loading).error(R.drawable.error)
                                .into(((Holder1) holder).img);
                        ((Holder1) holder).copyrights.setText(f.getMultimedia()[0].getCopyright());
                    }
                    else{
                        Picasso.with(holder.itemView.getContext()).load(R.drawable.error).into(((Holder1) holder).img);

                    }

                }
                else{
                    Picasso.with(holder.itemView.getContext())
                            .load(f.getMultimedia()[3].getUrl()).placeholder(R.drawable.loading).error(R.drawable.error)
                            .into(((Holder1) holder).img);
                    ((Holder1) holder).copyrights.setText(f.getMultimedia()[3].getCopyright());
                }

                ((Holder1) holder).title.setText(f.getTitle());
                ((Holder1) holder).abstrac.setText(f.getAbstrac());
                ((Holder1) holder).share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String shareBody = f.getUrl();
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        //sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        v.getContext().startActivity(Intent.createChooser(sharingIntent, "Share Using ?" ) );
                    }
                });

                ((Holder1) holder).date.setText(getDateDiffrence(f.getUpdated_date()));


                break;
            }
        }
    }
    private String getDateDiffrence(String date)  {
        DateFormat m_ISO8601Local = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date past = null;
        try {
            past = m_ISO8601Local.parse(date);
            Date now = new Date();
            String s = "";
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
            if(days >0 ){
                if(days==1)
                    return "Yesterday";
                return past.getDay()+" "+ android.text.format.DateFormat.format("MMM",past);
            }
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            if(hours >0){
                return hours+"hrs";
            }
            long minutes =TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            if(minutes >0 ){
                return minutes+"mins";
            }
            return "Just now";
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getItemCount() {
        return mFeeds == null ? 0 : mFeeds.size();
    }

    public void changeDataSet(ArrayList<Feed> nData) {
        if (nData != null) {
            mFeeds = nData;
            super.notifyDataSetChanged();
        }
    }

    class Holder1 extends RecyclerView.ViewHolder {
        ImageView img;
        TextView copyrights;
        TextView title;
        TextView abstrac;
        TextView date;
        View share;


        public Holder1(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            copyrights = (TextView) itemView.findViewById(R.id.copyrights);
            title = (TextView) itemView.findViewById(R.id.title);
            abstrac = (TextView) itemView.findViewById(R.id.abstrac);
            date = (TextView) itemView.findViewById(R.id.date);
            share = itemView.findViewById(R.id.share);
        }
    }

    public interface OnFeedClickListener{
        void OnClick(Feed f);
    }

}
