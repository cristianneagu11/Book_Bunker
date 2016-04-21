package neagucristian.bookbunker;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;



import java.util.ArrayList;


import neagucristian.bookbunker.LibraryContract.BookEntry;

public class LibraryFragment extends android.support.v4.app.Fragment {

    private ListAdapter adapter;

    public LibraryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_library, container, false);

        BookProvider provider = new BookProvider(getContext());

        Cursor c = provider.getEntireList();

        ListView list = (ListView) rootView.findViewById(R.id.library_list);


        ArrayList<ListEntry> objects = new ArrayList<ListEntry>();
        ListEntry object;
        if(c != null) {
            while(c.moveToNext()) {
                object = new ListEntry(c.getInt(0), c.getString(1), c.getString(2), c.getInt(4));
                objects.add(object);
            }
        adapter = new ListAdapter(getContext(), objects);
        list.setAdapter(adapter);
        View emptyView = rootView.findViewById(R.id.library_oops);
            list.setEmptyView(emptyView);
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                int id = adapter.getItem(position).getId();
                android.support.v4.app.Fragment fragment = null;
                fragment = (android.support.v4.app.Fragment) ItemFragment.newInstance(id);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flcontainer, fragment)
                        .addToBackStack(null).commit();
            }
        });

        c.close();
        provider.closer();
        return rootView;
    }

}