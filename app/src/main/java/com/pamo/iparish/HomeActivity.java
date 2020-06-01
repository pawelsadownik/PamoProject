package com.pamo.iparish;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.pamo.iparish.register.MainActivity;


/**
 * This activity contains a top menu and a navfragment
 * @uses NavController
 */
public class HomeActivity extends AppCompatActivity {

  NavController navController;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
  }

  @Override
  public void onStart() {
    super.onStart();
    navController = Navigation.findNavController(
      this,
      findViewById(R.id.nav_host_fragment).getId());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
      case R.id.parish_pick:
        Log.println(Log.INFO, "iparish", "parish_pick");
        navController.navigate(R.id.action_global_mapsFragment);
        return true;
      case R.id.settings:
        Log.println(Log.INFO, "iparish", "settings");
        navController.navigate(R.id.action_global_settingsFragment);
        return true;
      case R.id.app_bar_switch_darkmode:
        Log.println(Log.INFO, "iparish", "app_bar_switch_darkmode");
        navController.navigate(R.id.action_global_darkInitFragment);
        return true;
      case R.id.logout:
        Log.println(Log.INFO, "iparish", "logout");
        FirebaseAuth.getInstance().signOut();
        Intent intToMain = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intToMain);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}
