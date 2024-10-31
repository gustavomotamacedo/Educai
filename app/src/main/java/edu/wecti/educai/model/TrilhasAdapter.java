package edu.wecti.educai.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.wecti.educai.R;

public class TrilhasAdapter extends RecyclerView.Adapter<TrilhasAdapter.MyViewHolder> {

    Context context;
    ArrayList<TrilhaModel> trilhaModelList;

    public TrilhasAdapter(Context context, ArrayList<TrilhaModel> trilhaModelList) {
        this.context = context;
        this.trilhaModelList = trilhaModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.trilhas_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtTrilhaNome.setText(trilhaModelList.get(position).getNome());
    }

    @Override
    public int getItemCount() {
        return trilhaModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTrilhaNome;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTrilhaNome = itemView.findViewById(R.id.txtTrilhaNome);
        }
    }
}
