package com.appynitty.swachbharatabhiyanlibrary.adapters.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.pojos.WorkHistoryDetailPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;

import java.util.List;

/**
 * Created by Ayan Dey on 25/10/18.
 * updated by Rahul Rokade
 */

public class InflateHistoryDetailsAdapter extends ArrayAdapter<WorkHistoryDetailPojo> {

    private final List<WorkHistoryDetailPojo> workHistoryDetailPojoList;
    private final Context context;
    private View view;
    private ViewHolder holder;

    public InflateHistoryDetailsAdapter(@NonNull Context context, @NonNull List<WorkHistoryDetailPojo> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.context = context;
        this.workHistoryDetailPojoList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_history_detail_card, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.time = view.findViewById(R.id.history_details_time);
            viewHolder.id = view.findViewById(R.id.history_details_id);
            viewHolder.vehicleNo = view.findViewById(R.id.history_details_vehicle);
            viewHolder.area = view.findViewById(R.id.history_details_area);
            viewHolder.name = view.findViewById(R.id.history_details_name);

            view.setTag(viewHolder);

            viewHolder.time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String text = viewHolder.time.getText().toString();
                    AUtils.success(context, text);

                }
            });  //End of addition

        } else {
            view = convertView;
        }
        holder = (ViewHolder) view.getTag();

        if (!AUtils.isNull(workHistoryDetailPojoList) && !workHistoryDetailPojoList.isEmpty()) {
            WorkHistoryDetailPojo workHistoryDetailPojo = workHistoryDetailPojoList.get(position);

            if (workHistoryDetailPojo.getType().equals("2")) {
                holder.time.setBackgroundResource(R.drawable.rounded_pink_button);
                holder.time.setPadding(0, 0, 0, 0);
                holder.id.setText(String.format("%s %s", context.getResources().getString(R.string.point_id_txt), workHistoryDetailPojo.getRefid()));
            } else if (workHistoryDetailPojo.getType().equals("1")) {
                holder.time.setBackgroundResource(R.drawable.rounded_blue_button);
                holder.time.setPadding(0, 0, 0, 0);
                holder.id.setText(String.format("%s %s", context.getResources().getString(R.string.house_id_txt), workHistoryDetailPojo.getRefid()));
            } else if (workHistoryDetailPojo.getType().equals("4")) {
                holder.time.setBackgroundResource(R.drawable.rounded_cyan_button);
                holder.time.setPadding(0, 0, 0, 0);
                holder.id.setText(String.format("%s %s", context.getResources().getString(R.string.liquid_waste_id_txt), workHistoryDetailPojo.getRefid()));
            } else if (workHistoryDetailPojo.getType().equals("5")) {
                holder.time.setBackgroundResource(R.drawable.rounded_pink_button);
                holder.time.setPadding(0, 0, 0, 0);
                holder.id.setText(String.format("%s %s", context.getResources().getString(R.string.street_sweep_id_txt), workHistoryDetailPojo.getRefid()));
            } else if (workHistoryDetailPojo.getType().equals("6")) {
                holder.time.setBackgroundResource(R.drawable.rounded_gray_button);
                holder.time.setPadding(0, 0, 0, 0);
                holder.id.setText(String.format("%s %s", context.getResources().getString(R.string.dump_yard_vehicle_id_txt), workHistoryDetailPojo.getRefid()));
            }
            else {
                holder.time.setBackgroundResource(R.drawable.rounded_orange_button);
                holder.time.setPadding(0, 0, 0, 0);
                holder.id.setText(String.format("%s %s", context.getResources().getString(R.string.dump_yard_id_txt), workHistoryDetailPojo.getRefid()));
            }

            holder.time.setText(workHistoryDetailPojo.getTime());
            holder.vehicleNo.setText(String.format("%s %s", context.getResources().getString(R.string.vehicle_number_txt), workHistoryDetailPojo.getVehicleNumber()));
            holder.area.setText(workHistoryDetailPojo.getAreaName());
            holder.name.setText(workHistoryDetailPojo.getName());
        }

        return view;
    }

    private class ViewHolder {

        private TextView time;
        private TextView id;
        private TextView vehicleNo;
        private TextView area;
        private TextView name;
    }


}
