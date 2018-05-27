package com.example.lavishbansal.demose2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class newspaper extends AppCompatActivity {

    public void htOnClick(View view){

        Intent intent= new Intent(getApplicationContext(), particularNewspaper.class);
        intent.putExtra("http", "https://www.hindustantimes.com/");
        startActivity(intent);

    }

    public void ttOnClick(View view){

        Intent intent= new Intent(getApplicationContext(), particularNewspaper.class);
        intent.putExtra("http", "http://www.tribuneindia.com/");
        startActivity(intent);

    }

    public void dbOnClick(View view){

        Intent intent= new Intent(getApplicationContext(), particularNewspaper.class);
        intent.putExtra("http", "https://www.bhaskar.com/");
        startActivity(intent);

    }

    public void thOnClick(View view){

        Intent intent= new Intent(getApplicationContext(), particularNewspaper.class);
        intent.putExtra("http", "http://www.thehindu.com/");
        startActivity(intent);

    }

    public void toiOnClick(View view){

        Intent intent= new Intent(getApplicationContext(), particularNewspaper.class);
        intent.putExtra("http", "https://timesofindia.indiatimes.com/");
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newspaper);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.news:
                        break;

                    case R.id.magzine:
                        Intent intent1 = new Intent(newspaper.this, MagazineActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.user:
                        Intent intent2 = new Intent(newspaper.this, ProfileActivity.class);
                        startActivity(intent2);
                        break;
                }


                return false;
            }
        });

    }
}
