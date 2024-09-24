package com.callerid.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.callerid.R;
import com.callerid.model.ExtraModel;
import com.callerid.model.NoteModel;
import com.callerid.model.TaskModel;
import com.callerid.utils.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

        private ArrayList<TaskModel> mData;
        private LayoutInflater mInflater;

        Context mContext;
        public boolean loadmore = true;
        // data is passed into the constructor
        public TaskAdapter(Context context, ArrayList<TaskModel> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;
            mContext = context;
        }

        // inflates the cell layout from xml when needed
        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_task_layout, parent, false);
            return new ViewHolder(view);
        }
    private String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }
    private String convertTime(String capString){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        dateFormat.setTimeZone(timeZone);
        Date date = null;//You will get date object relative to server/client timezone wherever it is parsed

        String dateStr2="";
        try {
            date = dateFormat.parse(capString);


            TimeZone t1= TimeZone.getTimeZone("Asia/Kolkata");
            DateFormat formatter2 = new SimpleDateFormat("dd MMM yyyy - hh:mm a"); //If you need time just put specific format for time like 'HH:mm:ss'
            formatter2.setTimeZone(t1);
            dateStr2 = formatter2.format(date);


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr2;

    }
        // binds the data to the TextView in each cell
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            TaskModel task = mData.get(position);
           // Log.d("here","here");
            holder.creator_name.setText("Owner: "+task.getCreated_by_name());

        switch (task.getType().toLowerCase()) {
            case "follow_up":
                holder.taskimg.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.followup));
                holder.taskimg.setImageTintList(ColorStateList.valueOf(Color.parseColor("#F320CF")));
                holder.taskimg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FBBEF1")));
                break;

            case "email":
                holder.taskimg.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_email));
                ImageViewCompat.setImageTintList(holder.taskimg,null);
                holder.taskimg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffcc91")));
                break;

            case "send":
                holder.taskimg.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_redo));
                holder.taskimg.setImageTintList(ColorStateList.valueOf(Color.parseColor("#03a31d")));
                holder.taskimg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#8df29d")));
                break;

            case "call":
                holder.taskimg.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_call));
                ImageViewCompat.setImageTintList(holder.taskimg,null);
                holder.taskimg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#c4e4ff")));


                break;
            case "video_call":
                holder.taskimg.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_video));
                ImageViewCompat.setImageTintList(holder.taskimg,null);
                holder.taskimg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffc7c7")));
                break;
            case "message":
                holder.taskimg.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_message));
              //  holder.taskimg.setImageTintList(ColorStateList.valueOf(Color.parseColor("#1387ed")));
                ImageViewCompat.setImageTintList(holder.taskimg,null);
                holder.taskimg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#75bfff")));

                break;
            default:  holder.taskimg.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_baseline_refresh_24));
                ImageViewCompat.setImageTintList(holder.taskimg,null);
                holder.taskimg.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FBBEF1")));
                break;
        }

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            TimeZone timeZone = TimeZone.getTimeZone("GMT");
            dateFormat.setTimeZone(timeZone);
            Date date = null;//You will get date object relative to server/client timezone wherever it is parsed
holder.dateview.setText("");
holder.taskdesc.setText(task.getType().toUpperCase());
            try {
                date = dateFormat.parse(task.getTo_be_performed_at());

            DateFormat formatter = new SimpleDateFormat("dd MMM"); //If you need time just put specific format for time like 'HH:mm:ss'

                TimeZone t1= TimeZone.getTimeZone("Asia/Kolkata");
            formatter.setTimeZone(t1);
                String dateStr = formatter.format(date);
            holder.dateview.setText(dateStr);
                DateFormat formatter2 = new SimpleDateFormat("hh:mm a"); //If you need time just put specific format for time like 'HH:mm:ss'
                formatter2.setTimeZone(t1);
                String dateStr2 = formatter2.format(date);
                holder.taskdesc.setText(capitalize(task.getType().replace("_"," "))+" - "+dateStr2);

            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (task.getLead().size()>0){
                holder.leadname.setVisibility(View.VISIBLE);
                holder.leadname.setText(task.getLead().get(0).getName());
            }else holder.leadname.setVisibility(View.GONE);



            try {
                date = dateFormat.parse(task.getCreatedAt());
                DateFormat formatter = new SimpleDateFormat("dd MMMM yyyy hh:mm a"); //If you need time just put specific format for time like 'HH:mm:ss'

                TimeZone t1= TimeZone.getTimeZone("Asia/Kolkata");
                formatter.setTimeZone(t1);
                String dateStr = formatter.format(date);
                holder.note.setText(dateStr);
            } catch (Exception e) {
                holder.note.setText(task.getCreatedAt());
            }

//            try {
//                if(Utils.isInvalidString(extraModel.getDescription())) {
//                    holder.note.setVisibility(View.GONE);
//                } else {
//                    holder.note.setVisibility(View.VISIBLE);
//                    holder.note.setText("Note: "+extraModel.getDescription());
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        }

        // total number of cells
        @Override
        public int getItemCount() {
            return mData.size();
        }


        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder {




            private TextView dateview;
            private TextView taskdesc;
            private TextView leadname;
            private TextView creator_name;
            TextView note;
            ImageView taskimg;

            ViewHolder(View view) {
                super(view);
                dateview = (TextView)view. findViewById(R.id.dateview);
                taskdesc = (TextView)view. findViewById(R.id.taskdesc);
                leadname = (TextView)view. findViewById(R.id.leadname);
                creator_name = (TextView)view. findViewById(R.id.creator_name);
                note = view.findViewById(R.id.note);
                taskimg = view.findViewById(R.id.taskimg);
            }






        }


    }
