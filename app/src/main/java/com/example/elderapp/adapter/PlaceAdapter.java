package com.example.elderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.elderapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;





public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    private List<Place> data;
    private Context context;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public PlaceAdapter(Context context, List<Place> data) {
        this.data = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    // ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, desc;
        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_placeTitle);
            desc =  itemView.findViewById(R.id.tv_placeDesc);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
//            Toast.makeText(mContext, title.getText().toString()+"hi", Toast.LENGTH_SHORT).show();
        }
        public void setData(Place data){
            title.setText(data.placeTitle+" -");
            desc.setText(data.placeDesc);
        }
    }


    // convenience method for getting data at click position
    public String getTitle(int id) {
        return data.get(id).placeTitle;
    }
    public String getDesc(int id) {
        return data.get(id).placeDesc;
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
