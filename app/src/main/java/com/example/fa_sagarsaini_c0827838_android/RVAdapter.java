package com.example.fa_sagarsaini_c0827838_android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder> {
    Context context;
    Activity activity;
    ArrayList<String> id,name, lati, longi;
    private MyViewHolder holder;

    RVAdapter(Activity activity, Context context, ArrayList id, ArrayList name, ArrayList lati, ArrayList longi){
        this.activity=activity;
    this.context=context;
    this.id=id;
    this.name=name;
    this.lati = lati;
    this.longi = longi;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.my_row,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.idtxt.setText(String.valueOf(id.get(position)));
        holder.nametxt.setText(String.valueOf(name.get(position)));
        holder.latitxt.setText(String.valueOf(lati.get(position)));
        holder.longtxt.setText(String.valueOf(longi.get(position)));
        holder.mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, LocationActivity.class);
                intent.putExtra("id",String.valueOf(id.get(position)));
                intent.putExtra("name",String.valueOf(name.get(position)));
                intent.putExtra("lati",String.valueOf(lati.get(position)));
                intent.putExtra("longi",String.valueOf(longi.get(position)));

                activity.startActivityForResult(intent,1);
            }
        });
    }


    @Override
    public int getItemCount() {
        return name.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView idtxt,nametxt,longtxt,latitxt;
        LinearLayout mainlayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            idtxt= itemView.findViewById(R.id.displayid);
            nametxt= itemView.findViewById(R.id.displayname);
            latitxt= itemView.findViewById(R.id.displaylat);
            longtxt= itemView.findViewById(R.id.displaylong);
            mainlayout=itemView.findViewById(R.id.mainlayout);
        }
    }


}
