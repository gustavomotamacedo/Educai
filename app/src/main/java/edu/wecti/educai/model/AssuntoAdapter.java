package edu.wecti.educai.model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.wecti.educai.R;
import edu.wecti.educai.view.AssuntoActivity;

public class AssuntoAdapter extends RecyclerView.Adapter<AssuntoAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<AssuntoModel> assuntoModelArrayList;
    private String username;
    private String trilha;

    public AssuntoAdapter(Context context, ArrayList<AssuntoModel> assuntoModelArrayList, String trilha, String username) {
        this.context = context;
        this.assuntoModelArrayList = assuntoModelArrayList;
        this.username = username;
        this.trilha = trilha;
    }

    @NonNull
    @Override
    public AssuntoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.row_assuntos, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AssuntoAdapter.MyViewHolder holder, int position) {
        holder.btnAssunto.setText(assuntoModelArrayList.get(position).getNome());
        if (assuntoModelArrayList.get(position).isCompletado()) {
            holder.btnAssunto.setBackground(context.getDrawable(R.drawable.assunto_completo));
        } else {
            holder.btnAssunto.setBackground(context.getDrawable(R.drawable.assunto_incompleto));
        }
        holder.btnAssunto.setOnClickListener(v -> {
            Intent intent = new Intent(context, AssuntoActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("trilha", trilha);
            intent.putExtra("assunto", assuntoModelArrayList.get(position).getNome());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return assuntoModelArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private Button btnAssunto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btnAssunto = itemView.findViewById(R.id.btnAssunto);
        }
    }
}
