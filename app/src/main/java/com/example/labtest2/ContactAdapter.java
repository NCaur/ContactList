package com.example.labtest2;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.SparseBooleanArray;

import android.content.Context;
import android.database.Cursor;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;


public class ContactAdapter extends CursorRecyclerViewAdapter<ContactAdapter.MyViewHolder>
        {
private Context context;
private Cursor cursor;
private int mRowIDColumn;

private ContactAdapter.ItemClicked itemClicked;
private SparseBooleanArray selectedItems;

public ContactAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.context = context;
        this.cursor = cursor;

        selectedItems = new SparseBooleanArray();
        mRowIDColumn = cursor != null ? this.cursor.getColumnIndexOrThrow(DBHandler.ID_COL) : -1;
        }


@Override
public long getItemId(int position) {
        if (cursor != null) {
        if (cursor.moveToPosition(position)) {
        return cursor.getLong(mRowIDColumn);
        } else {
        return 0;
        }
        } else {
        return 0;
        }
        }

@Override
public void onBindViewHolder(ContactAdapter.MyViewHolder viewHolder, Cursor cursor) {

    byte[] imgByte = cursor.getBlob(cursor.getColumnIndexOrThrow(DBHandler.COLUMN_PICTURE));
    if (imgByte != null) {
        Bitmap contactpicture = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);

        viewHolder.imageContact.setImageBitmap(contactpicture);
    }

        viewHolder.textName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHandler.NAME_COL)));
        viewHolder.textNumber.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHandler.PHNNUMBER_COL)));
        viewHolder.textEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHandler.EMailAddress_COL)));
        viewHolder.textAddress.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHandler.Address_COL)));
        viewHolder.textTypeofContact.setText("Personal");

        int Id= cursor.getInt(cursor.getColumnIndexOrThrow(DBHandler.ID_COL));
        viewHolder.clParent.setSelected(selectedItems.get(Id, false));
        viewHolder.itemView.setTag(Id);
        }

@NonNull
@Override
public ContactAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.contact_item, parent, false);
        return new ContactAdapter.MyViewHolder(itemView);
        }

class MyViewHolder extends RecyclerView.ViewHolder {
    RelativeLayout clParent;
    CircleImageView imageContact;
    TextView textName,textNumber,textTypeofContact,textEmail,textAddress;

    MyViewHolder(@NonNull View view) {
        super(view);
        clParent = view.findViewById(R.id.clParent);
        imageContact = view.findViewById(R.id.imageContact);
        textName = view.findViewById(R.id.textName);
        textNumber = view.findViewById(R.id.textNumber);
        textTypeofContact = view.findViewById(R.id.textTypeofContact);
        textEmail = view.findViewById(R.id.textEmail);
        textAddress = view.findViewById(R.id.textAddress);

    }
}

public interface ItemClicked {
        void onSelectedItems(boolean selectedItems);
}

    private void notifySelectedItems(int id){
        if (selectedItems.get(id, false)){
            selectedItems.put(id,false);
        }else {
            selectedItems.put(id,true);
        }
        notifyDataSetChanged();
    }

    private boolean containsTrueValue() {
        boolean containsBoolean = false;
        for (int i = 0; i < selectedItems.size(); i++) {
            if (selectedItems.valueAt(i)) {
                containsBoolean = true;
                break;
            }
        }
        return containsBoolean;
    }


    public String getSelectedItems(){
        StringBuilder stringBuilder=new StringBuilder();
        for (int i = 0; i < selectedItems.size(); i++) {
            if (selectedItems.valueAt(i)) {
                stringBuilder.append(selectedItems.keyAt(i)).append(",");
            }
        }
        return stringBuilder.length()>0?stringBuilder.substring(0,stringBuilder.length()-1):"";
    }

    public void clearSelectedItems(){
        selectedItems.clear();
        notifyDataSetChanged();
    }
}