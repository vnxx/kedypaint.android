package com.kevinadam.paintcalculator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.TypedArrayUtils;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    ArrayList<Room> mItems;
    private onItemClickListener listener;

    public MainAdapter(ArrayList<Room> mItems) {
        this.mItems = mItems;
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    public void deleteItem(int position){
        this.mItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    };

    public void updatedItem(int position){
        notifyItemChanged(position);
    }

    public interface onItemClickListener{
        void onItemClick(Room room);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
    };

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        holder.rName.setText(mItems.get(position).getName());
        holder.rLebar.setText(mItems.get(position).getWidth().toString());
        holder.rPanjang.setText(mItems.get(position).getLength().toString());
        holder.rTinggi.setText(mItems.get(position).getHeight().toString());
        holder.rLiter.setText(String.format("%.2f", mItems.get(position).getPaintNeeds()));
    }

    @Override
    public int getItemCount() {
        return mItems.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView rName;
        public TextView rPanjang;
        public TextView rLebar;
        public TextView rTinggi;
        public TextView rLiter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rName = itemView.findViewById(R.id.r_name);
            rPanjang = itemView.findViewById(R.id.show_panjang);
            rLebar = itemView.findViewById(R.id.show_lebar);
            rTinggi =itemView.findViewById(R.id.show_tinggi);
            rLiter = itemView.findViewById(R.id.show_liter);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION ){
                        listener.onItemClick(mItems.get(position));
                    }
                }
            });
        }
    }

}
