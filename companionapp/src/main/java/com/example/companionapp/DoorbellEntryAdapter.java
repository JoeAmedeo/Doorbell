package com.example.companionapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by joe on 11/4/17.
 */

public class DoorbellEntryAdapter extends FirebaseRecyclerAdapter<DoorbellEntry, DoorbellEntryAdapter.DoorbellEntryViewHolder> {
    static class DoorbellEntryViewHolder extends RecyclerView.ViewHolder {
        public final ImageView image;
        public final TextView time;
        public final TextView metaData;

        public DoorbellEntryViewHolder(View view){
            super(view);
            image = (ImageView) view.findViewById(R.id.imageView1);
            time = (TextView) view.findViewById(R.id.textView1);
            metaData = (TextView) view.findViewById(R.id.textView2);
        }
    }

    private Context myApplicationContext;

    public DoorbellEntryAdapter(Context context, DatabaseReference ref){
        super(DoorbellEntry.class, R.layout.doorbell_entry, DoorbellEntryViewHolder.class, ref);
        myApplicationContext = context.getApplicationContext();
    }

    //method for updating our view with database info
    @Override
    protected void populateViewHolder(DoorbellEntryViewHolder viewHolder, DoorbellEntry entry, int position){
        CharSequence timeSequence = DateUtils.getRelativeDateTimeString(myApplicationContext, entry.getTimestamp(), DateUtils.SECOND_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0);
        viewHolder.time.setText(timeSequence);

        //conditions for how to handle no image being sent
        if(entry.getImage() != null){
            byte[] imageBytes = Base64.decode(entry.getImage(), Base64.NO_WRAP | Base64.URL_SAFE);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            if(bitmap != null){
                viewHolder.image.setImageBitmap(bitmap);
            }else{
                Drawable placeholder = ContextCompat.getDrawable(myApplicationContext, R.drawable.fail_image);
                viewHolder.image.setImageDrawable(placeholder);
            }
        }

        //conditions for no annotations being sent
        if(entry.getAnnotations() != null){
            ArrayList<String> keywords = new ArrayList<>(entry.getAnnotations().keySet());
            int limit = Math.min(keywords.size(), 3);
            viewHolder.metaData.setText(TextUtils.join("\n", keywords.subList(0, limit)));
        }else{
            viewHolder.metaData.setText("No Annotations");
        }
    }


}
