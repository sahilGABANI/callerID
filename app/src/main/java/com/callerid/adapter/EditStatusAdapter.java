package com.callerid.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.callerid.R;
import com.callerid.activity.MainActivity;
import com.callerid.model.NoteModel;
import com.callerid.model.StatusModel;
import com.callerid.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class EditStatusAdapter extends RecyclerView.Adapter<EditStatusAdapter.ViewHolder> {

        private List<StatusModel> mData;
        private LayoutInflater mInflater;
        ClickListener clickListener;
        Context mContext;
        public boolean loadmore = true;
        // data is passed into the constructor
        public EditStatusAdapter(Context context, List<StatusModel> data, ClickListener clickListener1) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;
            mContext = context;
            clickListener = clickListener1;

        }

        // inflates the cell layout from xml when needed
        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_edit_status_layout, parent, false);
            return new ViewHolder(view);
        }

        // binds the data to the TextView in each cell
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            StatusModel note = mData.get(position);

            holder.txtStatusName.setText(note.getName());
            holder.imgColor.setBackgroundColor(Color.parseColor(note.getColor()));

            Drawable unwrappedDrawable = AppCompatResources.getDrawable(holder.itemView.getContext(),R.drawable.green_circle_shape);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor(note.getColor()));

            holder.imgColor.setImageDrawable(wrappedDrawable);

            holder.lytmain2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onClick(holder.getAdapterPosition());
                }
            });

        }

        // total number of cells
        @Override
        public int getItemCount() {
            return  mData.size();
        }


        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder {


            private TextView txtStatusName;
            private ImageView imgColor;
            private TextView creator_name;
            private LinearLayout lytmain2;


            ViewHolder(View view) {
                super(view);
                txtStatusName = (TextView)view. findViewById(R.id.txtStatusName);
                imgColor = (ImageView)view. findViewById(R.id.imgColor);
                lytmain2 = (LinearLayout) view. findViewById(R.id.lytmain2);
            }






        }

    public interface ClickListener {
        void onClick(int s);
    }

    }
