package edu.wecti.educai.model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.wecti.educai.R;
import edu.wecti.educai.view.RoadmapActivity;

public class TrilhasAdapter extends RecyclerView.Adapter<TrilhasAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<TrilhaModel> trilhaModelArrayList;
    private String username;

    public TrilhasAdapter(Context context, ArrayList<TrilhaModel> trilhaModelArrayList, String username) {
        this.context = context;
        this.trilhaModelArrayList = trilhaModelArrayList;
        this.username = username;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.row_trilhas, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int aux = position;
        holder.txtTrilhaNome.setText(trilhaModelArrayList.get(position).getNome());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RoadmapActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("trilha", trilhaModelArrayList.get(aux).getNome());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return trilhaModelArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTrilhaNome;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTrilhaNome = itemView.findViewById(R.id.txtTrilhaNome);
        }
    }
}
