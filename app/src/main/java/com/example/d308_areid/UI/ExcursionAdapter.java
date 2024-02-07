package com.example.d308_areid.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308_areid.R;
import com.example.d308_areid.entities.Excursions;

import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder>{


    class ExcursionViewHolder extends RecyclerView.ViewHolder {

        private final TextView excursionItemView;
        private final TextView excursionItemView2;
        private String startDate;
        private String endDate;


        private ExcursionViewHolder(View itemView, String startDate, String endDate) {
            super(itemView);
            this.startDate = startDate;
            this.endDate = endDate;

            excursionItemView = itemView.findViewById(R.id.textView3);
            excursionItemView2 = itemView.findViewById(R.id.textView4);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Excursions current = mExcursions.get(position);

                    Intent intent = new Intent(context, ExcursionDetails.class);
                    intent.putExtra("id", current.getExcursionID());
                    intent.putExtra("name", current.getExcursionName());
                    intent.putExtra("vacationID", current.getVacationID());
                    intent.putExtra("excursionDate", current.getExcursionDate());
                    intent.putExtra("note", current.getNote());
                    intent.putExtra("startDate", startDate);
                    intent.putExtra("endDate", endDate);
                    context.startActivity(intent);
                }
            });
        }
    }

    private List<Excursions> mExcursions;
    private final Context context;
    private final LayoutInflater mInflater;
    private final String startDate;
    private final String endDate;

    public ExcursionAdapter(Context context, String startDate, String endDate) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @NonNull
    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=mInflater.inflate(R.layout.excursion_list_item,parent,false);
        return new ExcursionViewHolder(itemView, startDate, endDate);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position){
        if (mExcursions!=null) {
            Excursions current = mExcursions.get(position);
            String name = current.getExcursionName();
            int vacationID = current.getVacationID();
            holder.excursionItemView.setText(name);
            holder.excursionItemView2.setText(Integer.toString(vacationID));
        } else {
            holder.excursionItemView.setText("No excursion name");
            holder.excursionItemView2.setText("No vacation id");
        }
    }
    public void setExcursions(List<Excursions> excursions){
        mExcursions = excursions;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        if (mExcursions!=null){
            return mExcursions.size();
        } else {
            return 0;
        }
    }
}
