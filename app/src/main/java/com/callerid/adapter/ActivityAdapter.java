package com.callerid.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Html;
import android.text.format.DateUtils;
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
import com.callerid.model.ActivityModel;
import com.callerid.utils.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private ArrayList<ActivityModel> mData;
    private LayoutInflater mInflater;

    Context mContext;
    public boolean loadmore = true;

    // data is passed into the constructor
    public ActivityAdapter(Context context, ArrayList<ActivityModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        mContext = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_activity_layout, parent, false);
        return new ViewHolder(view);
    }

    private String capitalize(String capString) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }

    private String convertTime(String capString, boolean doanything) {

        if (doanything) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            TimeZone timeZone = TimeZone.getTimeZone("GMT");
            dateFormat.setTimeZone(timeZone);
            Date date = null;//You will get date object relative to server/client timezone wherever it is parsed

            String dateStr2 = "";
            try {
                date = dateFormat.parse(capString);


                TimeZone t1 = TimeZone.getTimeZone("Asia/Kolkata");
                DateFormat formatter2 = new SimpleDateFormat("dd MMM yyyy - hh:mm a"); //If you need time just put specific format for time like 'HH:mm:ss'
                formatter2.setTimeZone(t1);
                dateStr2 = formatter2.format(date);

                //   Log.d("time", dateStr2);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            return dateStr2;
        } else {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            //   TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
            //   dateFormat.setTimeZone(timeZone);
            Date date = null;//You will get date object relative to server/client timezone wherever it is parsed

            String dateStr2 = "";
            try {
                date = dateFormat.parse(capString);


                //  TimeZone t1 = TimeZone.getTimeZone("Asia/Kolkata");
                DateFormat formatter2 = new SimpleDateFormat("dd MMM yyyy - hh:mm a"); //If you need time just put specific format for time like 'HH:mm:ss'
                // formatter2.setTimeZone(t1);
                dateStr2 = formatter2.format(date);

                //  Log.d("time", dateStr2);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return dateStr2;
        }

    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ActivityModel task = mData.get(position);
        //   Log.d("here","here");
        holder.dooffset = true;

        switch (task.getType().toLowerCase()) {
            case "outgoing":
                holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_outgoing));

                ImageViewCompat.setImageTintList(holder.imgview, null);

                holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e7efe8")));

                holder.duration.setVisibility(View.VISIBLE);
                holder.taskdesc.setText("Outgoing Call");
                if (task.getExtra_details() != null) {
                    if (task.getExtra_details().getDuration() != null)
                        holder.duration.setText("Call Duration: " + DateUtils.formatElapsedTime(Long.parseLong(Utils.checkStr(task.getExtra_details().getDuration()))));
                }
                holder.dooffset = false;

                break;
            case "incoming":
                holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_outgoing));

                ImageViewCompat.setImageTintList(holder.imgview, null);

                holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e7efe8")));

                holder.duration.setVisibility(View.VISIBLE);
                holder.taskdesc.setText("Incoming Call");
                if (task.getExtra_details() != null) {
                    if (task.getExtra_details().getDuration() != null)
                        holder.duration.setText("Call Duration: " + DateUtils.formatElapsedTime(Long.parseLong(Utils.checkStr(task.getExtra_details().getDuration()))));
                }
                holder.dooffset = false;

            case "call":
                holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_incoming));
                ImageViewCompat.setImageTintList(holder.imgview, null);

                holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e7efe8")));

                holder.duration.setVisibility(View.VISIBLE);
                holder.taskdesc.setText("Called you");
                if (task.getNote() != null)
                    holder.duration.setText("" + task.getNote());
                holder.dooffset = false;

                break;
            case "video_call":
                holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_incoming));
                ImageViewCompat.setImageTintList(holder.imgview, null);

                holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e7efe8")));

                holder.duration.setVisibility(View.VISIBLE);
                holder.taskdesc.setText("video  call");
                if (task.getNote() != null)
                    holder.duration.setText("" + task.getNote());
                holder.dooffset = false;

                break;
            case "email":
                holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_incoming));
                ImageViewCompat.setImageTintList(holder.imgview, null);

                holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e7efe8")));

                holder.duration.setVisibility(View.VISIBLE);
                holder.taskdesc.setText("email");
                if (task.getNote() != null)
                    holder.duration.setText("" + task.getNote());
                holder.dooffset = false;

                break;
            case "meeting":
                holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_incoming));
                ImageViewCompat.setImageTintList(holder.imgview, null);

                holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e7efe8")));

                holder.duration.setVisibility(View.VISIBLE);
                holder.taskdesc.setText("Meeting");
                if (task.getNote() != null)
                    holder.duration.setText("" + task.getNote());
                holder.dooffset = false;

                break;
            case "missed":
                holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_missed));


                ImageViewCompat.setImageTintList(holder.imgview, null);

                holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffe8f0")));
                holder.duration.setVisibility(View.GONE);
                holder.taskdesc.setText("Missed Call");

                holder.dooffset = false;
                break;
            case "status_update":
                holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_update));

                ImageViewCompat.setImageTintList(holder.imgview, null);
                holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFF4C7C2")));

                holder.duration.setVisibility(View.GONE);
                holder.taskdesc.setText("Lead status updated to " + Utils.checkStr(task.getExtra_details().getNext_status()).replace("_", " "));
                break;
            case "transfer_lead":
                holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.leadlabel));

                ImageViewCompat.setImageTintList(holder.imgview, null);
                holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#fabbb4")));

                holder.duration.setVisibility(View.GONE);
                holder.taskdesc.setText(Utils.checkStr(task.getExtra_details().getTransfer_by_name()) + " assigned to " + Utils.checkStr(task.getExtra_details().getTransfer_to_name()));
                break;
            case "move_lead":
                holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.leadlabel));

                ImageViewCompat.setImageTintList(holder.imgview, null);
                holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#fabbb4")));

                holder.duration.setVisibility(View.GONE);
                holder.taskdesc.setText("lead moved to " + Utils.checkStr(task.getExtra_details().getList_name()));
                break;
            case "copy_lead":
                holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.leadlabel));


                ImageViewCompat.setImageTintList(holder.imgview, ColorStateList.valueOf(Color.parseColor("#16a624")));
                holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#b8ffbf")));

                holder.duration.setVisibility(View.GONE);
                holder.taskdesc.setText("lead copied to " + Utils.checkStr(task.getExtra_details().getList_name()));
                break;
            case "label_update":
                holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.leadlabel));

                ImageViewCompat.setImageTintList(holder.imgview, null);
                holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFF4C7C2")));

                holder.duration.setVisibility(View.GONE);
                holder.taskdesc.setText("Lead label updated to " + Utils.checkStr(task.getExtra_details().getNext_label()).replace("_", " "));
                break;


            case "meeting_at_office_":
                holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.calender));
                ImageViewCompat.setImageTintList(holder.imgview, null);
                holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#c4e4ff")));
                holder.taskdesc.setText(capitalize(Utils.checkStr(task.getExtra_details().getNotes()).replace("_", " ")));
                break;

            case "follow_up":

                holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.followup));

                holder.imgview.setImageTintList(ColorStateList.valueOf(Color.parseColor("#e74c3c")));
                holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffc9e5")));

                holder.duration.setVisibility(View.GONE);
                holder.taskdesc.setText(capitalize(Utils.checkStr(task.getType()).replace("_", " ")));
                //  holder.duration.setText("Call Duration: 00:00 mm:ss");


                break;


            case "task_create":
                if (task.getExtra_details().getType().equals("follow_up")) {
                    holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.followup));
                    holder.imgview.setImageTintList(ColorStateList.valueOf(Color.parseColor("#e74c3c")));
                    holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff97cc")));
                } else if (task.getExtra_details().getType().equals("email")) {
                    holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_email));
                    ImageViewCompat.setImageTintList(holder.imgview, null);
                    holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffcc91")));
                } else if (task.getExtra_details().getType().equals("video_call")) {
                    holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_video));
                    ImageViewCompat.setImageTintList(holder.imgview, null);
                    holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffc7c7")));
                } else if (task.getExtra_details().getType().equals("call")) {
                    holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_call));
                    ImageViewCompat.setImageTintList(holder.imgview, null);
                    holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#c4e4ff")));

                } else {
                    holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.task));
                    holder.imgview.setImageTintList(ColorStateList.valueOf(Color.parseColor("#e74c3c")));
                    holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff97cc")));
                }


                holder.duration.setVisibility(View.GONE);

                holder.taskdesc.setText(capitalize(Utils.checkStr(task.getExtra_details().getType()).replace("_", "-") + ": task created"));
                //  holder.duration.setText("Call Duration: 00:00 mm:ss");


                break;

            case "task_is_complete_update":
                try {
                    if (task.getExtra_details().getType().equals("follow_up")) {
                        holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.followup));
                        holder.imgview.setImageTintList(ColorStateList.valueOf(Color.parseColor("#e74c3c")));
                        holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff97cc")));
                    } else if (task.getExtra_details().getType().equals("email")) {
                        holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_email));
                        ImageViewCompat.setImageTintList(holder.imgview, null);
                        holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffcc91")));
                    } else if (task.getExtra_details().getType().equals("video_call")) {
                        holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_video));
                        ImageViewCompat.setImageTintList(holder.imgview, null);
                        holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffc7c7")));
                    } else if (task.getExtra_details().getType().equals("call")) {
                        holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_call));
                        ImageViewCompat.setImageTintList(holder.imgview, null);
                        holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#c4e4ff")));

                    } else {
                        holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.task));
                        holder.imgview.setImageTintList(ColorStateList.valueOf(Color.parseColor("#e74c3c")));
                        holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff97cc")));
                    }

                } catch (Exception e) {
                    holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.task));

                    holder.imgview.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FF6D5E")));
                    holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFEC867B")));

                }

                String text = Utils.checkStr(task.getExtra_details().getType()) + ": marked as completed";
                holder.duration.setVisibility(View.GONE);
                holder.taskdesc.setText(capitalize(text));

                //  holder.duration.setText(capitalize(Utils.checkStr(task.getExtra_details().getType()).replace("_"," ")));
                //  holder.duration.setText("Call Duration: 00:00 mm:ss");


                break;
            case "task_due_date_update":
                try {
                    if (task.getExtra_details().getType().equals("follow_up")) {
                        holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.followup));
                        holder.imgview.setImageTintList(ColorStateList.valueOf(Color.parseColor("#e74c3c")));
                        holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff97cc")));
                    } else if (task.getExtra_details().getType().equals("email")) {
                        holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_email));
                        ImageViewCompat.setImageTintList(holder.imgview, null);
                        holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffcc91")));
                    } else if (task.getExtra_details().getType().equals("video_call")) {
                        holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_video));
                        ImageViewCompat.setImageTintList(holder.imgview, null);
                        holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffc7c7")));
                    } else if (task.getExtra_details().getType().equals("call")) {
                        holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_call));
                        ImageViewCompat.setImageTintList(holder.imgview, null);
                        holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#c4e4ff")));

                    } else {
                        holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.task));
                        holder.imgview.setImageTintList(ColorStateList.valueOf(Color.parseColor("#e74c3c")));
                        holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff97cc")));
                    }

                } catch (Exception e) {
                    holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.task));

                    holder.imgview.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FF6D5E")));
                    holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFEC867B")));

                }

                String text1 = Utils.checkStr(task.getExtra_details().getType()) + ": due date updated";
                holder.duration.setVisibility(View.GONE);
                holder.taskdesc.setText(capitalize(text1));

                //  holder.duration.setText(capitalize(Utils.checkStr(task.getExtra_details().getType()).replace("_"," ")));
                //  holder.duration.setText("Call Duration: 00:00 mm:ss");


                break;
            case "task_assign_update":
                try {
                    if (task.getExtra_details().getType().equals("follow_up")) {
                        holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.followup));
                        holder.imgview.setImageTintList(ColorStateList.valueOf(Color.parseColor("#e74c3c")));
                        holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff97cc")));
                    } else if (task.getExtra_details().getType().equals("email")) {
                        holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_email));
                        ImageViewCompat.setImageTintList(holder.imgview, null);
                        holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffcc91")));
                    } else if (task.getExtra_details().getType().equals("video_call")) {
                        holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_video));
                        ImageViewCompat.setImageTintList(holder.imgview, null);
                        holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffc7c7")));
                    } else if (task.getExtra_details().getType().equals("call")) {
                        holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_call));
                        ImageViewCompat.setImageTintList(holder.imgview, null);
                        holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#c4e4ff")));

                    } else {
                        holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.task));
                        holder.imgview.setImageTintList(ColorStateList.valueOf(Color.parseColor("#e74c3c")));
                        holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff97cc")));
                    }

                } catch (Exception e) {
                    holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.task));

                    holder.imgview.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FF6D5E")));
                    holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFEC867B")));

                }

                String text2 = Utils.checkStr(task.getExtra_details().getType()) + ": Task assigned to " + Utils.checkStr(task.getExtra_details().getTask_assigned_to_name());
                holder.duration.setVisibility(View.GONE);
                holder.taskdesc.setText(capitalize(text2));

                //  holder.duration.setText(capitalize(Utils.checkStr(task.getExtra_details().getType()).replace("_"," ")));
                //  holder.duration.setText("Call Duration: 00:00 mm:ss");


                break;
            case "checkin":
            case "chechin":
                holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_message));

                ImageViewCompat.setImageTintList(holder.imgview, null);
                holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dbefff")));

                holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_notes));
                holder.imgview.setImageTintList(ColorStateList.valueOf(Color.parseColor("#06a115")));
                holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dbefff")));

                holder.duration.setVisibility(View.VISIBLE);
                holder.duration.setText(capitalize(Utils.checkStr(task.getExtra_details().getNotes()).replace("_", " ")));
                holder.taskdesc.setText("You have checkedin");


                break;
            case "message":
                holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_message));

                ImageViewCompat.setImageTintList(holder.imgview, null);
                holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dbefff")));

                holder.duration.setVisibility(View.GONE);
//                    holder.taskdesc.setText(capitalize(Utils.checkStr(task.getExtra_details().getNotes()).replace("_"," ")));


                break;
            case "note_create":
                holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_notes));
                holder.imgview.setImageTintList(ColorStateList.valueOf(Color.parseColor("#06a115")));
                holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#c9ffce")));

                holder.duration.setVisibility(View.GONE);
                holder.taskdesc.setText("added new note");
                //  holder.duration.setText("Call Duration: 00:00 mm:ss");


                break;
            case "task_delete":
                holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_task));
                holder.imgview.setImageTintList(ColorStateList.valueOf(Color.parseColor("#e74c3c")));
                holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff97cc")));
                holder.duration.setVisibility(View.GONE);
                holder.taskdesc.setText("task was deleted");
                //  holder.duration.setText("Call Duration: 00:00 mm:ss");


                break;
          /*  case "share":
                if (task.getContent().getType().equals("file")) {
                    holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.pdf));

                    ImageViewCompat.setImageTintList(holder.imgview, null);
                    holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffc2c6")));

                }
                if (task.getContent().getType().equals("page")) {
                    holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.integration_setting));
                    ImageViewCompat.setImageTintList(holder.imgview, null);
                    holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dbefff")));
                } else {
                    holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_message));
                    ImageViewCompat.setImageTintList(holder.imgview, null);
                    holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dbefff")));
                }


                holder.duration.setVisibility(View.VISIBLE);
                holder.taskdesc.setText(Html.fromHtml("<b>" + task.getContent().getContent_details().getTitle() + "</b> " + task.getContent().getType() + " was shared"));
//                     holder.duration.setText(capitalize(Utils.checkStr(task.getContent().getContent_details().getSelectedTags().get(0))));


                break;*/

            default:
                holder.imgview.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_update));

                ImageViewCompat.setImageTintList(holder.imgview, null);
                holder.imgview.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFF4C7C2")));

                holder.duration.setVisibility(View.GONE);
                holder.taskdesc.setText(Html.fromHtml("<b>" + capitalize(task.getType().replace("_", " ")) + "</b> Activity performed "));


        }

        holder.creator_name.setText("Created By: " + task.getCreated_by_name());

        holder.time.setText(convertTime(task.getPerformed_at(), holder.dooffset));


    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {


        private ImageView imgview;
        private TextView taskdesc;
        private TextView duration;
        private TextView creator_name;
        private TextView time;

        boolean dooffset = true;

        ViewHolder(View view) {
            super(view);
            imgview = (ImageView) view.findViewById(R.id.imgview);
            taskdesc = (TextView) view.findViewById(R.id.taskdesc);
            duration = (TextView) view.findViewById(R.id.duration);
            creator_name = (TextView) view.findViewById(R.id.creator_name);
            time = (TextView) view.findViewById(R.id.time);
        }


    }


}
