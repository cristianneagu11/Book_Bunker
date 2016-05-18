package neagucristian.bookbunker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<ListEntry> objects;

    private class ViewHolder {
        TextView textView1;
        TextView textView2;
        RatingBar rating;
    }

    public ListAdapter(Context context, ArrayList<ListEntry> objects) {
        inflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    public int getCount() {
        return objects.size();
    }

    public ListEntry getItem(int position) {
        return objects.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_book, null);
            holder.textView1 = (TextView) convertView.findViewById(R.id.list_item_textView1);
            holder.textView2 = (TextView) convertView.findViewById(R.id.list_item_textView2);
            holder.rating = (RatingBar) convertView.findViewById(R.id.list_rating);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView1.setText( objects.get(position).getTitle());
        holder.textView2.setText("by " + objects.get(position).getAuthor());
        holder.rating.setRating(objects.get(position).getRating());
        return convertView;
    }
}