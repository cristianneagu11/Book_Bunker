package neagucristian.bookbunker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;


public class SettingsFragment extends android.support.v4.app.Fragment {

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        ListView list = (ListView) rootView.findViewById(R.id.settings_list);
        ArrayList<String> settings = new ArrayList<String>();
        settings.add("Delete database");
        settings.add("Backup database");
        settings.add("Restore database");
        settings.add("View app info");
        final ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, settings);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (position == 0) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    LibraryDbHelper mDbHelper = new LibraryDbHelper(getContext());
                                    SQLiteDatabase db = mDbHelper.getReadableDatabase();
                                    String clearDBQuery = "DELETE FROM " + LibraryContract.BookEntry.TABLE_NAME;
                                    db.execSQL(clearDBQuery);
                                    db.close();
                                    Toast toast = Toast.makeText(getContext(), "Database deleted", Toast.LENGTH_LONG);
                                    toast.show();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
                if (position == 1) {
                    try {
                        File sd = Environment.getExternalStorageDirectory();
                        File data = Environment.getDataDirectory();

                        if (sd.canWrite()) {
                            String currentDBPath = "//data//" + getContext().getPackageName() + "//databases//" + "books.db" + "";
                            String backupDBPath = "BookBunker.db";
                            File currentDB = new File(data, currentDBPath);
                            File backupDB = new File(sd, backupDBPath);

                            if (currentDB.exists()) {
                                FileChannel src = new FileInputStream(currentDB).getChannel();
                                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                                dst.transferFrom(src, 0, src.size());
                                src.close();
                                dst.close();
                            }
                        }
                    } catch (Exception e) {

                    }
                    Toast toast = Toast.makeText(getContext(), "Database saved on internal memory", Toast.LENGTH_LONG);
                    toast.show();
                }
                if (position == 3) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("App developed by Neagu Cristian\nContact:\ncristian_neagu11@yahoo.com")
                            .setPositiveButton("Ok", dialogClickListener)
                            .show();
                }
                if (position == 2) {
                    try {
                        File sd = Environment.getExternalStorageDirectory();
                        File data = Environment.getDataDirectory();

                        if (sd.canWrite()) {
                            String currentDBPath = "//data//" + getContext().getPackageName() + "//databases//" + "books.db" + "";
                            String backupDBPath = "BookBunker.db";
                            File currentDB = new File(data, currentDBPath);
                            File backupDB = new File(sd, backupDBPath);

                            if (currentDB.exists()) {
                                FileChannel src = new FileInputStream(backupDB).getChannel();
                                FileChannel dst = new FileOutputStream(currentDB).getChannel();
                                dst.transferFrom(src, 0, src.size());
                                src.close();
                                dst.close();
                            }
                        }
                    } catch (Exception e) {
                    }
                    Toast toast = Toast.makeText(getContext(), "Database restored", Toast.LENGTH_LONG);
                    toast.show();


                }
            }
        });

        return rootView;
    }

}