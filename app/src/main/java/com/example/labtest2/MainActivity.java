package com.example.labtest2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private EditText NameEdt, contactEdt, emailEdt, addressEdt;
    private Spinner typeofcontact;
    private Button addContactBtn,searchContactBtn;
    private ImageView profile_image;
    private final int SELECT_IMAGE = 200;
    private DBHandler dbHandler;
   private byte[] byteArray;
     private Uri selectedImageUri;
     boolean isUpdate =  false;
     int id =0;

    // below line is to get data from all edit text fields.
    String Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initializing all our variables.
        NameEdt = findViewById(R.id.name);
        contactEdt = findViewById(R.id.telnumber);
        emailEdt = findViewById(R.id.email);
        addressEdt = findViewById(R.id.homeaddress);
        addContactBtn = findViewById(R.id.addcontact);
        typeofcontact = findViewById(R.id.spinner);
        profile_image=findViewById(R.id.profile_image);
        searchContactBtn = findViewById(R.id.searchcontact);
        // creating a new dbhandler class
        // and passing our context to it.
        dbHandler = new DBHandler(MainActivity.this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            isUpdate = true;
            id = bundle.getInt(DBHandler.ID_COL);
           Cursor result=dbHandler.getRecord(id);
            if(result != null)
            {
                if (result.moveToFirst()) {

                    byte[] imgByte = result.getBlob(result.getColumnIndexOrThrow(DBHandler.COLUMN_PICTURE));
                    if (imgByte != null) {
                        Bitmap contactpicture = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                        profile_image.setImageBitmap(contactpicture);
                    }

                    NameEdt.setText(result.getString(result.getColumnIndexOrThrow(DBHandler.NAME_COL)));
                    contactEdt.setText(result.getString(result.getColumnIndexOrThrow(DBHandler.PHNNUMBER_COL)));
                    emailEdt.setText(result.getString(result.getColumnIndexOrThrow(DBHandler.EMailAddress_COL)));
                    addressEdt.setText(result.getString(result.getColumnIndexOrThrow(DBHandler.Address_COL)));
                    typeofcontact.setSelection(0);

                }
                result.close();
            }
        }
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
            }
        });

        // below line is to add on click listener for our add course button.
        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Name = NameEdt.getText().toString();
                String Email = emailEdt.getText().toString();
                String Contact = contactEdt.getText().toString();
                String Homeaddress = addressEdt.getText().toString();
                String ContactType = typeofcontact.toString();
                // validating if the text fields are empty or not.
                if (Name.isEmpty() && Email.isEmpty() && Contact.isEmpty() && Homeaddress.isEmpty() && ContactType.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter all the data..", Toast.LENGTH_SHORT).show();
                    return;
                }

                else if(Contact.length()<10 || Contact.length()>13){
                    Toast.makeText(MainActivity.this, "Phone Number must contain 10 digits", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!Email.contains("@") && !Email.contains(".com")){
                    Toast.makeText(MainActivity.this, "Email Address must contain @  and .com ", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!Email.contains(".com")){
                    Toast.makeText(MainActivity.this, "Email Address must contain .com ", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {

                    // on below line we are calling a method to add new
                    // course to sqlite data and pass all our values to it.
                   if(isUpdate){
                       dbHandler.updateContact(Name, Contact, Email, Homeaddress,byteArray,id);
                   }
                   else{
                       dbHandler.addNewContact(Name, Contact, Email, Homeaddress,byteArray);
                   }


                    // after adding the data we are displaying a toast message.
                    Toast.makeText(MainActivity.this, "Contact has been added.", Toast.LENGTH_SHORT).show();

                   MainActivity.this.finish();
                }
            }
        });

      searchContactBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              //Check if Name field is not empty
              Name = NameEdt.getText().toString();
              if(!Name.isEmpty()) {
                  // opening a new activity via a intent.
                  Intent i = new Intent(MainActivity.this, SearchContacts.class);
                  i.putExtra("name", Name);
                  startActivity(i);
              } else {
                  Toast.makeText(MainActivity.this, "Please provide search string in Enter Name field", Toast.LENGTH_SHORT).show();
              }
          }
      });

       


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_IMAGE) {
                // Get the url of the image from data
                selectedImageUri=data.getData();

                if (null != selectedImageUri) {
                    // update the preview image in the layout

                        profile_image.setImageURI(selectedImageUri);
                    try {
                       Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byteArray = stream.toByteArray();

                        profile_image.setImageURI(selectedImageUri);
                    } catch (IOException e) {
//                        e.printStackTrace();
                    }
                    }

                }
            }
        }
    }


