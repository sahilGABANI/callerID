package com.callerid.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.callerid.R;
import com.callerid.model.LabelModel;
import com.callerid.model.LinkRequestModel;
import com.callerid.model.NoteModel;
import com.callerid.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class EditlabelAdapter extends RecyclerView.Adapter<EditlabelAdapter.ViewHolder> {

        private List<LabelModel> mData;
        private LayoutInflater mInflater;
        ClickListener clickListener;
        Context mContext;
        public boolean loadmore = true;
        // data is passed into the constructor

    public EditlabelAdapter(Context context, List<LabelModel> mData, ClickListener clickListener1) {
        this.mInflater = LayoutInflater.from(context);
            this.mData = mData;
        mContext = context;
        clickListener = clickListener1;
    }

    // inflates the cell layout from xml when needed
        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_edit_label_layout, parent, false);
            return new ViewHolder(view);
        }

        // binds the data to the TextView in each cell
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            LabelModel note = mData.get(position);

            holder.checkbox.setText(note.getName());
            holder.imgColor.setBackgroundColor(Color.parseColor(note.getColor()));

            Drawable unwrappedDrawable = AppCompatResources.getDrawable(holder.itemView.getContext(),R.drawable.green_circle_shape);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor(note.getColor()));

            holder.imgColor.setImageDrawable(wrappedDrawable);

            holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (holder.checkbox.isChecked()){
                        clickListener.onClick(holder.getAdapterPosition(),true);
                    }else{
                        clickListener.onClick(holder.getAdapterPosition(),false);
                    }
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


            private CheckBox checkbox;
            private ImageView imgColor;
            private TextView creator_name;
            private LinearLayout lytmain;


            ViewHolder(View view) {
                super(view);
                checkbox = (CheckBox)view. findViewById(R.id.checkbox);
                imgColor = (ImageView)view. findViewById(R.id.imgColor);
                lytmain = (LinearLayout) view. findViewById(R.id.lytmain);

            }






        }


    public interface ClickListener {
        void onClick(int s, Boolean b);
    }


}
