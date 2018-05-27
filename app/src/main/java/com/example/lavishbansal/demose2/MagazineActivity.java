package com.example.lavishbansal.demose2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MagazineActivity extends AppCompatActivity {

    public void etOnClick(View view){

        Intent intent= new Intent(getApplicationContext(), particularNewspaper.class);
        intent.putExtra("http", "https://economictimes.indiatimes.com/headlines.cms");
        startActivity(intent);

    }

    public void vgOnClick(View view){

        Intent intent= new Intent(getApplicationContext(), particularNewspaper.class);
        intent.putExtra("http", "https://www.vogue.in/news/");
        startActivity(intent);

    }

    public void tgOnClick(View view){

        Intent intent= new Intent(getApplicationContext(), particularNewspaper.class);
        intent.putExtra("http", "http://www.topgear.com/india/");
        startActivity(intent);

    }

    public void espnOnClick(View view){

        Intent intent= new Intent(getApplicationContext(), particularNewspaper.class);
        intent.putExtra("http", "http://www.espn.in/");
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magazine);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.news:
                        Intent intent1 = new Intent(MagazineActivity.this, newspaper.class);
                        startActivity(intent1);
                        break;

                    case R.id.magzine:
                        break;

                    case R.id.user:
                        Intent intent2 = new Intent(MagazineActivity.this, ProfileActivity.class);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });
    }
}
