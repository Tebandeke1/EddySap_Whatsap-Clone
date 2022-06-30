package com.tabutech.eddysap.View.Activities.Contact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.tabutech.eddysap.Adaptor.ContactAdaptor;
import com.tabutech.eddysap.Model.ContactsModel.ContactsModel;
import com.tabutech.eddysap.Model.Users.Users;
import com.tabutech.eddysap.R;
import com.tabutech.eddysap.databinding.ActivityContactBinding;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ContactActivity extends AppCompatActivity {

    private ActivityContactBinding binding;
    private ContactAdaptor adaptor;
    private FirebaseFirestore firestore;
    private FirebaseUser user;
    private final List<Users> users = new ArrayList<>();
    public static final int REQUEST_READ_CONTACTS = 79;
    private ListView list;
    private ArrayList<ContactsModel> mobileArray;
    private String name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_contact);

        binding.imageBack.setOnClickListener(v -> finish());
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        user = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        if (user != null){
            //getContactList();
            getContactsFromPhone();//if tey are using this app

        }

        if (mobileArray != null){
            getContactList();
        }

    }

    private void getContactsFromPhone() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            mobileArray = getAllPhoneContacts();
        } else {
            requestPermission();
        }

    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
            // show UI part if you want here to show some rationale !!!
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mobileArray = getAllPhoneContacts();
            } else finish();
            return;
        }
    }

    @SuppressLint("Range")
    private ArrayList getAllPhoneContacts() {
//        List<ContactsModel> nameList = new ArrayList<>();
//
//        //initialize uri
//        Uri uri = ContactsContract.Contacts.CONTENT_URI;
//        //sort by descending order
//        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC";
//
//        //initialize cursor
//        Cursor cur = getContentResolver().query(uri,
//                null, null, null, sort);
//        //check condition
//        if (cur.getCount() > 0) {
//            //is count is greater than 0;
//            //use while loop
//            while (cur.moveToNext()) {
//                //cursor move to next
//                //get contact id
//                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
//                //get  contact name
//                name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//
//                Cursor pCur = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                        null,
//                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?",
//                            new String[]{id}, null);
//                    while (pCur.moveToNext()) {
//                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//
//                        ContactsModel model = new ContactsModel();
//                        model.setNumber(phoneNo);
//                        model.setName(name);
//                        nameList.add(model);
//
//                    }
//                    pCur.close();
//
//            }
//            Toast.makeText(ContactActivity.this, name, Toast.LENGTH_SHORT).show();
//        }
//        cur.close();
//        return nameList;

        ArrayList<ContactsModel> contactList = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        String[] PROJECTION = new String[] {
                ContactsContract.RawContacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Photo.CONTACT_ID };

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String filter = ""+ ContactsContract.Contacts.HAS_PHONE_NUMBER + " > 0 and " + ContactsContract.CommonDataKinds.Phone.TYPE +"=" + ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
        String order = ContactsContract.Contacts.DISPLAY_NAME + " ASC";// LIMIT " + limit + " offset " + lastId + "";

        Cursor phoneCur = cr.query(uri, PROJECTION, filter, null, order);
        while(phoneCur.moveToNext()) {
            ContactsModel dto = new ContactsModel();
            name = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            dto.setName("" + name);
            dto.setNumber("" + phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            contactList.add(dto);
        }
        phoneCur.close();

        return contactList;
    }

    private void getContactList() {

        firestore.collection("Users").get().addOnSuccessListener(queryDocumentSnapshots -> {

            for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                String  id = snapshot.getString("userId");
                if (name.equals("")) {
                    name = snapshot.getString("userName");
                }
                String image = snapshot.getString("imageProfile");
                String bio = snapshot.getString("bio");
                String phone = snapshot.getString("userPhone");

                Users users1 = new Users();
                users1.setBio(bio);
                users1.setUserId(id);
                users1.setImageProfile(image);
                users1.setUserName(name);
                users1.setUserPhone(phone);


                if (id != null && !id.equals(user.getUid())){
                    for (ContactsModel num: mobileArray) {
                        if (num.getNumber().endsWith(phone.substring(6))){
                            if (!users.contains(users1)) {
                                users.add(users1);
                            }
                        }
                    }


                }

            }

            adaptor = new ContactAdaptor(users,ContactActivity.this);

            binding.recycleView.setAdapter(adaptor);


            if ( users.size() == 1){
                binding.numberOfPeople.setVisibility(View.VISIBLE);
                binding.numberOfPeople.setText(users.size()+" Contact");

            }else if( users.size() != 0 && users.size() > 1){
                binding.numberOfPeople.setVisibility(View.VISIBLE);
                binding.numberOfPeople.setText(users.size()+" Contacts");
            }
        });
    }
}