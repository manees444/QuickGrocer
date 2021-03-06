package myproject.quickgrocer.User;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import myproject.quickgrocer.Constants;
import myproject.quickgrocer.Database.ProjectDatabase;
import myproject.quickgrocer.ForgetPassword;
import myproject.quickgrocer.MainActivity;
import myproject.quickgrocer.R;

public class NavActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.name);
        TextView navEmail = (TextView) headerView.findViewById(R.id.email);
        String email = null;
        ProjectDatabase projectDatabase = new ProjectDatabase(NavActivity.this);
        SQLiteDatabase db = projectDatabase.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT Email FROM " + Constants.user_tableName + " WHERE " +
                Constants.user_col_username + " =? ", new String[]{MainActivity.sendUser});
        if (c.moveToFirst()) {
            do {
                email = c.getString(0);

            } while (c.moveToNext());
        }
        c.close();
        db.close();
        Log.e("Email", email);
        navEmail.setText(email);
        navUsername.setText("Welcome " + MainActivity.sendUser);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.home, R.id.checkout, R.id.about, R.id.logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
           /* case R.id.checkout_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new UserHome()).commit();*/
            case R.id.logout:
                startActivity(new Intent(NavActivity.this, MainActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

   /* @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.home:
            case R.id.logout:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new UserHome()).commit();
                break;
            case R.id.checkout:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Checkout()).commit();
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
