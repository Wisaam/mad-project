package como.example.noman.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.gson.Gson;

public class HomeActivity extends AppCompatActivity {

    private boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        WebService server = new WebService(this);
        server.initialize_server();
        server.getAllHostels(new WebService.Callback<WebService.HostelObjectList>() {
            @Override
            public void callbackFunction(WebService.HostelObjectList result) {
                Toast.makeText(getApplicationContext(), Float.toString(result.hostelsStored.get(3).rating), Toast.LENGTH_LONG).show();
            }
        });

        //Creating an initial Database of Hostels if it doesn't exist

        SharedPreferences hostelPref = getSharedPreferences("hostelInfo", MODE_PRIVATE);
        String h = hostelPref.getString("hostels", null);
        if (h == null)
        {
            String[] hostelNames = {"Paradise Hostel", "Premium Alcazaba Hostel", "El Machico Hostel", "Einstein Hostel"};
            String[] hostelAddress = {"Muslim Town, Lahore", "Johar Town, Lahore", "Gulshan-e-Ravi, Lahore", "Ferozpur Road, Lahore"};
            String[] hostelCity = {"Muslim Town, Lahore", "Johar Town, Lahore", "Gulshan-e-Ravi, Lahore", "Ferozpur Road, Lahore"};
            String[] hostelRatings = {"4.2", "3.5", "2.3", "3.3"};
            Integer[] hostelImagesId = {R.drawable.img_1, R.drawable.img_2, R.drawable.img_3, R.drawable.img_4};
            Integer[] hostelRooms = {20, 10, 16, 18};
            Integer[] hostelFloors = {6, 4, 3, 3};
            String[] hostelExtras = {"AC, Heater, Refrigerator", "AC, Heater, Refrigerator", "AC, Heater, Refrigerator", "AC, Heater, Refrigerator"};
            String[] hostelOwnerMail = {"test1@test.com", "test2@test.com", "test3@test.com", "test4@test.com"};
            int[] hostelIds = {0, 1, 2, 3};

            HostelDataList hostelList = new HostelDataList();
            for (int i = 0; i < 4; i++)
            {
                HostelDataClass hClass = new HostelDataClass(hostelNames[i], hostelAddress[i], hostelCity[i], hostelExtras[i], hostelRooms[i], hostelFloors[i], hostelOwnerMail[i], hostelImagesId[i], hostelRatings[i], hostelIds[i]);
                hostelList.hostelsStored.add(hClass);
            }

            String hListJson = (new Gson()).toJson(hostelList);
            hostelPref.edit().putString("hostels", hListJson).putInt("lastKey", 3).apply();
        }

        /////////////////////////////////////////////////////////////


        //check if user is logged in or not
        SharedPreferences mpef = getSharedPreferences("info", MODE_PRIVATE);
        if (mpef.getString("logged_in", null) == null)
            isLoggedIn = false;
        else
            isLoggedIn  = true;
        ///////////////////////////////////

        Fragment fragment = new HomeFragment();
        //Fragment fragment = new AddHostel();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        if (!isLoggedIn)
        {
            menu.removeItem(R.id.action_logout);
        }
        else
        {
            menu.removeItem(R.id.action_login);
            menu.removeItem(R.id.action_signup);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            SharedPreferences mpef = getSharedPreferences("info", MODE_PRIVATE);
            mpef.edit().putString("logged_in", null).apply();
            Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(i);
            return true;
        }
        else if (id == R.id.action_login) {
            Intent i = new Intent(getApplicationContext(), Login.class);
            startActivity(i);
            return true;
        }
        else if (id == R.id.action_signup) {
            Intent i = new Intent(getApplicationContext(), Signup.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
