package ch.uzh.supersede.feedbacklibrary.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.models.EditImageItem;

/**
 * Created by Skidan Oleg on 25.06.2017.
 */

public class EditImageViewAdapter extends RecyclerView.Adapter<EditImageViewAdapter.ViewHolder> {

    private List<EditImageItem> items;
    private Context context;

    public EditImageViewAdapter (List<EditImageItem> items){
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.edit_dialog_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final EditImageItem item = items.get(position);
        final String title = item.getTitle();

        final SharedPreferences sharedPreferences = context.getSharedPreferences("ImageEdit", Context.MODE_PRIVATE);
        boolean isFavorite = sharedPreferences.getBoolean(title, false);

        holder.elementImageView.setImageResource(item.getElementResource());
        holder.titleTextView.setText(item.getTitle());

        holder.starCheckBox.setChecked(isFavorite);
        holder.starCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(title, b);
                editor.apply();
            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.click();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        View view;

        ImageView elementImageView;
        TextView titleTextView;
        CheckBox starCheckBox;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            elementImageView = (ImageView)itemView.findViewById(R.id.edil_element_iv);
            starCheckBox = (CheckBox)itemView.findViewById(R.id.edil_star_cb);
            titleTextView = (TextView) itemView.findViewById(R.id.edil_title_tv);
        }
    }
}
