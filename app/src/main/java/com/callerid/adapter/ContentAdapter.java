package com.callerid.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.callerid.R;
import com.callerid.model.ContentModel;
import com.callerid.model.Content_detailsModel;
import com.callerid.model.LinkRequestModel;
import com.callerid.model.NoteModel;
import com.callerid.utils.Utils;
import com.google.android.material.chip.Chip;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {

    private ArrayList<ContentModel> mData;
    private LayoutInflater mInflater;
    ClickListener clickListener;
    Context mContext;
    public boolean loadmore = true;

    // data is passed into the constructor
    public ContentAdapter(Context context, ArrayList<ContentModel> data, ClickListener clickListener1) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        mContext = context;
        clickListener = clickListener1;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_content_layout, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null;//You will get date object relative to server/client timezone wherever it is parsed

        ContentModel note = mData.get(position);

        Content_detailsModel content_detailsModel = note.getContent_details();


        holder.contentdesc.setText(content_detailsModel.getTitle());
        if (!Utils.checkStr(note.getContent_details().getDescription()).equals("")) {
            holder.contenttype.setVisibility(View.VISIBLE);
            holder.contenttype.setText(note.getContent_details().getDescription());
        } else {
            holder.contenttype.setVisibility(View.GONE);
        }

        try {
            date = dateFormat.parse(note.getCreated_at());
            DateFormat formatter = new SimpleDateFormat("dd MMMM yyyy hh:mm a"); //If you need time just put specific format for time like 'HH:mm:ss'

            TimeZone t1= TimeZone.getTimeZone("Asia/Kolkata");
            formatter.setTimeZone(t1);
            String dateStr = formatter.format(date);
            holder.timeview.setText(dateStr);
        } catch (Exception e) {
            holder.timeview.setText(note.getCreated_at());
        }


        holder.typecontent.setText(note.getType().toUpperCase());

        holder.icWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (note.getType().equals("message") && !Utils.isInvalidString(content_detailsModel.getMessage())) {

                    clickListener.onWhatsappClick(content_detailsModel.getMessage());
                    return;
                } else if (note.getType().equals("file")) {


                    LinkRequestModel linkRequestModel = new LinkRequestModel();
                    linkRequestModel.setContent_id(note.getId());
                    linkRequestModel.setContent_type("file");
                    linkRequestModel.setPerformed_by("user");
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    TimeZone timeZone = TimeZone.getTimeZone("GMT");
                    dateFormat.setTimeZone(timeZone);
                    Date date = new Date();

                    String dateStr = dateFormat.format(date);
                    linkRequestModel.setPerformed_at(dateStr);


                    clickListener.onGenerateLink(linkRequestModel, true, content_detailsModel.getFile().getName());
                    return;
                } else if (note.getType().equals("page")) {


                    LinkRequestModel linkRequestModel = new LinkRequestModel();
                    linkRequestModel.setContent_id(note.getId());
                    linkRequestModel.setContent_type("page");
                    linkRequestModel.setPerformed_by("user");
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    TimeZone timeZone = TimeZone.getTimeZone("GMT");
                    dateFormat.setTimeZone(timeZone);
                    Date date = new Date();

                    String dateStr = dateFormat.format(date);
                    linkRequestModel.setPerformed_at(dateStr);


                    clickListener.onGenerateLink(linkRequestModel, true, content_detailsModel.getTitle());
                    return;
                }
                clickListener.onWhatsappClick(content_detailsModel.getTitle() + "\n" + content_detailsModel.getDescription());
            }
        });

        holder.icMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (note.getType().equals("message") && !Utils.isInvalidString(content_detailsModel.getMessage())) {
                    clickListener.onMessageClick(content_detailsModel.getMessage());
                    return;
                } else if (note.getType().equals("file")) {

                    LinkRequestModel linkRequestModel = new LinkRequestModel();
                    linkRequestModel.setContent_id(note.getId());
                    linkRequestModel.setContent_type("file");
                    linkRequestModel.setPerformed_by("user");
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    TimeZone timeZone = TimeZone.getTimeZone("GMT");
                    dateFormat.setTimeZone(timeZone);
                    Date date = new Date();

                    String dateStr = dateFormat.format(date);
                    linkRequestModel.setPerformed_at(dateStr);


                    clickListener.onGenerateLink(linkRequestModel, false, content_detailsModel.getFile().getName());
                    return;
                } else if (note.getType().equals("page")) {

                    LinkRequestModel linkRequestModel = new LinkRequestModel();
                    linkRequestModel.setContent_id(note.getId());
                    linkRequestModel.setContent_type("page");
                    linkRequestModel.setPerformed_by("user");
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    TimeZone timeZone = TimeZone.getTimeZone("GMT");
                    dateFormat.setTimeZone(timeZone);
                    Date date = new Date();

                    String dateStr = dateFormat.format(date);
                    linkRequestModel.setPerformed_at(dateStr);


                    clickListener.onGenerateLink(linkRequestModel, false, content_detailsModel.getTitle());
                    return;
                }
                clickListener.onMessageClick(content_detailsModel.getTitle() + "\n" + content_detailsModel.getDescription());

            }
        });

        holder.tag1.setVisibility(View.GONE);

        holder.tag2.setVisibility(View.GONE);

        holder.tag3.setVisibility(View.GONE);


        List<Content_detailsModel.Tags> taglist = content_detailsModel.getSelectedTags();
        if (taglist != null && taglist.size() > 2) {
            holder.tag1.setVisibility(View.VISIBLE);
            holder.tag1.setText(taglist.get(0).getTag());
            holder.tag2.setVisibility(View.VISIBLE);
            holder.tag2.setText(taglist.get(1).getTag());
            holder.tag3.setVisibility(View.VISIBLE);
            holder.tag3.setText(taglist.get(2).getTag());
        } else if (taglist != null && taglist.size() > 1) {
            holder.tag1.setVisibility(View.VISIBLE);
            holder.tag1.setText(taglist.get(0).getTag());
            holder.tag2.setVisibility(View.VISIBLE);
            holder.tag2.setText(taglist.get(1).getTag());
            holder.tag3.setVisibility(View.GONE);

        } else if (taglist != null && taglist.size() > 0) {
            holder.tag1.setVisibility(View.VISIBLE);
            holder.tag1.setText(taglist.get(0).getTag());
            holder.tag2.setVisibility(View.GONE);

            holder.tag3.setVisibility(View.GONE);

        }


    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView contentdesc;
        private TextView contenttype;
        private ImageView icWhatsapp;
        private TextView timeview;
        private ImageView icMessage;
        Chip typecontent, tag1, tag2, tag3;


        ViewHolder(View view) {
            super(view);
            contentdesc = (TextView) view.findViewById(R.id.contentdesc);
            contenttype = (TextView) view.findViewById(R.id.contenttype);
            icWhatsapp = (ImageView) view.findViewById(R.id.icWhatsapp);
            icMessage = (ImageView) view.findViewById(R.id.icMessage);
            typecontent = view.findViewById(R.id.typecontent);
            timeview = view.findViewById(R.id.timeview);
            tag1 = view.findViewById(R.id.tag1);
            tag2 = view.findViewById(R.id.tag2);
            tag3 = view.findViewById(R.id.tag3);
        }


    }

    public interface ClickListener {
        void onWhatsappClick(String text);

        void onMessageClick(String text);

        void onGenerateLink(LinkRequestModel model, boolean isWhatsapp, String title);
    }


}
