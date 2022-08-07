package com.example.labtest2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class ViewContacts extends AppCompatActivity {

    private static final int REQUEST_CODE = 0;
    private DBHandler dbHandler;
    private RecyclerView rv;
    private Cursor cursor;
    private ContactAdapter adapter;
    private FloatingActionButton fabtn, search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contacts);
        dbHandler = new DBHandler(ViewContacts.this);
        rv = findViewById(R.id.rv);
        fabtn = findViewById(R.id.fab);
        search = findViewById(R.id.search);

        fabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewContacts.this, MainActivity.class);
                startActivityForResult(i, REQUEST_CODE);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewContacts.this, MainActivity.class);
                startActivity(i);
            }
        });
        cursor = dbHandler.getRecords();
        setAdapter();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction==ItemTouchHelper.LEFT) {
                    removeItem((int) viewHolder.itemView.getTag());
                }
                else if (direction==ItemTouchHelper.RIGHT){
                    // Update
                    editItem((int) viewHolder.itemView.getTag());

                }
            }

            private void editItem(int tag) {
                Intent i = new Intent(ViewContacts.this, MainActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt(DBHandler.ID_COL,tag);
                i.putExtras(mBundle);

                startActivityForResult(i, REQUEST_CODE);

            }


            private void removeItem(int tag) {

                    dbHandler.deleteRecord(tag);
                cursor = dbHandler.getRecords();
                adapter.swapCursor(cursor);

            }
        }).attachToRecyclerView(rv);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cursor = dbHandler.getRecords();
        adapter.swapCursor(cursor);
    }

    private void setAdapter() {
        if (adapter != null) {
            adapter.swapCursor(cursor);
        } else {
            adapter = new ContactAdapter(ViewContacts.this, cursor);

            rv.setLayoutManager(new LinearLayoutManager(ViewContacts.this, RecyclerView.VERTICAL, false));
            rv.addItemDecoration(new DividerItemDecoration(rv.getContext(), DividerItemDecoration.VERTICAL));
            rv.setAdapter(adapter);
        }
    }
}