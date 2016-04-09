package neagucristian.bookbunker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import neagucristian.bookbunker.R;

public class ItemFragment extends android.support.v4.app.Fragment {
    private int position;
    public ItemFragment() {
    }

    public static final ItemFragment newInstance(int position)
    {
        ItemFragment f = new ItemFragment();
        Bundle bdl = new Bundle(2);
        bdl.putInt("position", position);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position=getArguments().getInt("position");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_item, container, false);
        TextView text = (TextView) rootView.findViewById(R.id.item_textView);
        text.setText(Integer.toString(position));
        return rootView;
    }

}