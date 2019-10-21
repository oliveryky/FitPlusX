package com.app.fitplusx.project.ViewActivity;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fitplusx.project.R;
import com.app.fitplusx.project.Repository.UserDataTable;
import com.app.fitplusx.project.Utils;
import com.app.fitplusx.project.ViewModel.ViewModelEditProfile;
import com.app.fitplusx.project.ViewFragment.ViewNavigationPhone;
import com.app.fitplusx.project.ViewFragment.ViewNavigationTablet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

//TODO implement logic to remove toolbar and bottom nav when creating a new user for the first time.
//TODO implement logic to change submit button text and navigate to edit regimen activity when creating a new use for the first time.
public class ViewActivityEditProfile extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        View.OnClickListener, AdapterView.OnItemSelectedListener {

    private final int GALLERY = 1;

    private ImageView profilePic;
    private EditText name, birthday;
    private int day, month, birthYear;
    private Spinner userHeight, userWeight, sexSpinner, city;
    private ArrayAdapter<String> heightAdapter;
    private ArrayAdapter<Double> weightAdapter;
    private ArrayAdapter<CharSequence> sexAdapter;

    private Button submitButton;

    private ViewModelEditProfile vmEditProfile;
    private UserDataTable userDataTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //Create the view model
        vmEditProfile = ViewModelProviders.of(this).get(ViewModelEditProfile.class);
        //Set the observer
        (vmEditProfile.getUserData()).observe(this, profileObserver);

        //grab all necessary fields
        profilePic = findViewById(R.id.editProfilePhoto);
        name = findViewById(R.id.profileName);
        birthday = findViewById(R.id.birthday);
        userHeight = findViewById(R.id.userHeight);
        userWeight = findViewById(R.id.userWeight);
//        city = findViewById(R.id.userCity);
        sexSpinner = findViewById(R.id.userSex);
        submitButton = findViewById(R.id.userProfileEditButton);

        //set listeners
        profilePic.setOnClickListener(this);
        birthday.setOnClickListener(this);
        submitButton.setOnClickListener(this);

        //fill the height spinner
        heightAdapter = setHeightAdapter();
        userHeight.setAdapter(heightAdapter);

        //fill the weight spinner
        weightAdapter = setWeightAdapter(heightAdapter);
        userWeight.setAdapter(weightAdapter);

//        String[] cities = getResources().getStringArray(R.array.cities_array); // Countries are in res folder
//        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities);
//        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        city.setAdapter(cityAdapter);

        //fill sex spinner
        sexAdapter = setSexAdapter();
        sexSpinner.setAdapter(sexAdapter);

        Intent intent = getIntent();
        if (intent.hasExtra("newUser")) {
            submitButton.setText("Continue");
        } else {
            if (isTablet()) {
                ViewNavigationTablet nav = new ViewNavigationTablet();
                FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
                fTrans.replace(R.id.editProfileNav, nav, "bottom_nav_frag");
                fTrans.commit();
            } else {
                //Create the fragment
                ViewNavigationPhone viewNavigationPhoneFragment = new ViewNavigationPhone();

                //Replace the fragment container
                FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
                fTrans.replace(R.id.editProfileBottomNav, viewNavigationPhoneFragment, "bottom_nav_frag");
                fTrans.commit();
            }
        }
    }

    private ArrayAdapter<CharSequence> setSexAdapter() {
        ArrayAdapter<CharSequence> sexAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_names, android.R.layout.simple_spinner_item);
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return sexAdapter;
    }

    private ArrayAdapter<Double> setWeightAdapter(ArrayAdapter<String> heightAdapter) {
        ArrayList<Double> weightList = new ArrayList<>();
        for (int i = 50; i < 400; i++) {
            weightList.add((double) i);
        }
        ArrayAdapter<Double> weightAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, weightList);
        heightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return weightAdapter;
    }

    private ArrayAdapter<String> setHeightAdapter() {
        ArrayList<String> heightList = new ArrayList<>();
        for (int i = 4; i < 9; i++) {
            String height = i + "\'";
//            heightList.add(height);
            for (int j = 0; j < 12; j++) {
                heightList.add(height + j);
            }
        }
        ArrayAdapter<String> heightAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, heightList);
        heightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return heightAdapter;
    }

    //create an observer that watches the LiveData<WeatherData> object
    final Observer<UserDataTable> profileObserver = new Observer<UserDataTable>() {
        @Override
        public void onChanged(@Nullable final UserDataTable userData) {
            // Update the UI if this data variable changes

            if(userData != null) {
                userDataTable = userData;
                name.setText(userDataTable.getName(), TextView.BufferType.EDITABLE);

                birthday.setText(userDataTable.getBirthday(), TextView.BufferType.EDITABLE);
                String str = Utils.heightToString(userDataTable.getHeight());
                int spinnerPosition = heightAdapter.getPosition(Utils.heightToString(userDataTable.getHeight()));
                userHeight.setSelection(spinnerPosition);

                spinnerPosition = weightAdapter.getPosition(userDataTable.getWeight());
                userWeight.setSelection((spinnerPosition));

                spinnerPosition = sexAdapter.getPosition(userDataTable.getSex());
                sexSpinner.setSelection(spinnerPosition);

                Bitmap image;
                if ((image = Utils.getBitmap(userDataTable.getImageUri())) != null) {
                    profilePic.setImageBitmap(image);
                }
            }
        }
    };

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        birthYear = year;
        month = monthOfYear;
        day = dayOfMonth;
        updateBirthdayText();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.birthday:
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                DatePickerDialog dialog = new DatePickerDialog(this, this,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                break;

            case R.id.userProfileEditButton:
                String name = this.name.getText().toString(),
                        birthday = this.birthday.getText().toString(),
                        weight = this.userWeight.getSelectedItem().toString(),
                        height = this.userHeight.getSelectedItem().toString();
                if (name.isEmpty() || birthday.isEmpty() || this.sexSpinner.getSelectedItem().toString().equals("Select Sex")) {
                    Toast toast = Toast.makeText(getApplicationContext(),"PLEASE MAKE SURE ALL FIELDS ARE FILLED!",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                }else {
                    userDataTable.setName(name);
                    userDataTable.setBirthday(birthday);
                    userDataTable.setWeight(Double.parseDouble(weight));
                    userDataTable.setHeight(Utils.stringToHeight(height));

                    userDataTable.setSex(this.sexSpinner.getSelectedItem().toString());

                    vmEditProfile.updateUserData(userDataTable);
                    //TODO: what about user image
//                    Singleton.updateOrCreateJSON();

                    if (submitButton.getText().equals("Continue")) {
                        Intent intent = new Intent(this, ViewActivityEditRegimen.class);
                        intent.putExtra("newUser", true);
                        this.startActivity(intent);
                    } else {
                        Intent profileIntent = new Intent(this, ViewActivityUserProfile.class);
                        this.startActivity(profileIntent);
                    }
                }
                break;
            case R.id.editProfilePhoto:
                Intent imageIntent = new Intent();
                imageIntent.setType("image/*");
                imageIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(imageIntent, "Select Picture"), GALLERY);
                break;
            default:
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY && resultCode != 0) {
            Uri imageUri = data.getData();
//                imagePath = getRealPathFromURI(imageUri);
            try {
                Bitmap image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
//                    image = getRoundedCroppedBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri));
                profilePic.setImageBitmap(image);
                //write to storage
                //TODO: pic stuff
//                Singleton.writeBitmapToFile(image);
                String path = this.getApplicationContext().getFilesDir().getPath() + "/profile_pic.png";
                userDataTable.setImageUri(path);
                Utils.writeBitmapToFile(image, path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void updateBirthdayText() {
        // Month is 0 based (Jan == 0).
        birthday.setText(new StringBuilder().append(month + 1).append("/").append(day).append("/").append(birthYear));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // TODO What do we want to do when the user selects a sex?
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO What do we want to do when the user doesn't select any sex?
    }

    boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }
}
