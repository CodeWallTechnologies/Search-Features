package com.example.retrofittesting.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.retrofittesting.R;
import com.example.retrofittesting.models.Data;
import com.example.retrofittesting.models.MainData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> implements Filterable {

    // TODO initialize variable
    private ArrayList<MainData> arrayList;
    private ArrayList<MainData> searchArraylist;
    private Activity activity;


    public ItemAdapter(ArrayList<MainData> arrayList, Activity activity) {
        this.arrayList = arrayList;
        this.activity = activity;
        this.searchArraylist = new ArrayList<>(arrayList);

    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<MainData> list = new ArrayList<>();
            if(constraint.toString().isEmpty()){
                list.addAll(searchArraylist);
            }else {
//                for (MainData mainData : searchArraylist) {
//                    if(mainData.getName().toLowerCase().contains(constraint.toString().toLowerCase(Locale.ROOT))){
//                        list.add(mainData);
//                    }
//                }
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (MainData mainData : searchArraylist) {
                    if(mainData.getName().toLowerCase().contains(filterPattern)){
                        list.add(mainData);
                    }
                }

            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = list;
            filterResults.count= list.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
                arrayList.clear();
                arrayList.addAll((Collection<? extends MainData>) results.values);
                notifyDataSetChanged();
        }
    };

    class ItemViewHolder extends RecyclerView.ViewHolder{
         ImageView imageView;
         TextView textView;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view1);
            textView = itemView.findViewById(R.id.text_view1);



        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.item,parent,false);
        ItemViewHolder obj = new ItemViewHolder(v);
        return obj;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            MainData currentObject = arrayList.get(position);

        Glide.with(activity)
                .load(currentObject.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);

        //holder.imageView.setImageResource(currentObject.getImage1());
        holder.textView.setText(currentObject.getName());
       holder.imageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(activity, Data.class);
               intent.putExtra("image",currentObject.getImage());
               activity.startActivity(intent);
           }
       });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


}
