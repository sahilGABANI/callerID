package com.callerid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.callerid.R;
import com.callerid.model.NoteModel;
import com.callerid.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class NoteAdapter  extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

        private ArrayList<NoteModel> mData;
        private LayoutInflater mInflater;

        Context mContext;
        public boolean loadmore = true;
        // data is passed into the constructor
        public NoteAdapter(Context context, ArrayList<NoteModel> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;
            mContext = context;
        }

        // inflates the cell layout from xml when needed
        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_note_layout, parent, false);
            return new ViewHolder(view);
        }

        // binds the data to the TextView in each cell
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            NoteModel note = mData.get(position);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = null;//You will get date object relative to server/client timezone wherever it is parsed
            holder.creator_name.setText("Created by: "+note.getCreatedBy());

            try {
                date = dateFormat.parse(note.getCreatedAt());
                DateFormat formatter = new SimpleDateFormat("dd MMMM yyyy hh:mm a"); //If you need time just put specific format for time like 'HH:mm:ss'

                TimeZone t1= TimeZone.getTimeZone("Asia/Kolkata");
                formatter.setTimeZone(t1);
                String dateStr = formatter.format(date);
                holder.timeview.setText(dateStr);
            } catch (Exception e) {
                holder.timeview.setText(note.getCreatedAt());
            }

            holder.notedesc.setText(note.getDescription());

            holder.imgCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.getCopy(note.getDescription(),holder.itemView.getContext());
                }
            });
        }

        // total number of cells
        @Override
        public int getItemCount() {
            return mData.size();
        }


        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder {


            private TextView notedesc;
            private TextView timeview;
            private TextView creator_name;
            private  ImageView imgCopy;


            ViewHolder(View view) {
                super(view);
                notedesc = (TextView)view. findViewById(R.id.notedesc);
                timeview = (TextView)view. findViewById(R.id.timeview);
                creator_name = (TextView)view. findViewById(R.id.creator_name);
                imgCopy = (ImageView)view. findViewById(R.id.imgCopy);

            }






        }


    }
