package com.ppmdev.agriman.activity;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ppmdev.agriman.R;

import com.ppmdev.agriman.callbacks.RecyclerBlogPageAdapterCallbacks;
import com.ppmdev.agriman.fragment.HomeFragment;
import com.ppmdev.agriman.fragment.BlogFragment;
import com.ppmdev.agriman.fragment.ProfileFragment;
import com.ppmdev.agriman.model.BlogModel;
import com.ppmdev.agriman.model.UserModel;
import com.ppmdev.agriman.utils.AndroidUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class HomePage extends AppCompatActivity
        implements RecyclerBlogPageAdapterCallbacks {

    private DrawerLayout drawerLayout;
    private ImageButton buttonDrawerToggle;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;

    private TextView tvUsername;
    private ImageView profilePic;
    private TextView tvWeather;
    private Uri profilePicUri;
    private UserModel currentUserModel;
    private TextView toolbarName;

    private ArrayList<BlogModel> blogModelArrayList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homepage);


        HomeFragment homefragment = new HomeFragment();
        ProfileFragment profilefragment = new ProfileFragment();
        BlogFragment blogfragment = new BlogFragment();
        init();

        getAllBlogData();

        // Apply system window insets to DrawerLayout
        drawerLayout.setOnApplyWindowInsetsListener((v, insets) -> {
            // Adjust padding based on system insets
            v.setPadding(insets.getSystemWindowInsetLeft(),
                    insets.getSystemWindowInsetTop(),
                    insets.getSystemWindowInsetRight(),
                    insets.getSystemWindowInsetBottom());
            return insets.consumeSystemWindowInsets();
        });
        buttonDrawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int itemId = menuItem.getItemId();

                if (itemId == R.id.navAboutUs){
                    //Toast.makeText(HomePage.this, "About Us Clicked", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),AboutUs.class));
                }
                if (itemId == R.id.navContactUs){
                    //Toast.makeText(HomePage.this, "Contact Us Clicked", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),ContactUs.class));
                }
                if (itemId == R.id.navFAQ){
                    //Toast.makeText(HomePage.this, "FAQ Clicked", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Faq.class));
                }

                drawerLayout.close();

                return false;
            }
        });
       // getUserData();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,homefragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int itemId = menuItem.getItemId();


                if(itemId == R.id.bottom_nav_home){
                    toolbarName.setText("AgriMan");

                    getSupportFragmentManager().beginTransaction().replace(R.id.container,homefragment).commit();

                }
                if(itemId == R.id.bottom_nav_blog){
                    toolbarName.setText("Blog");
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,blogfragment).commit();

                }
                if(itemId == R.id.bottom_nav_profile){
                    toolbarName.setText("Profile");
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,profilefragment).commit();

                }

                // Clear all checked states
                for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
                    bottomNavigationView.getMenu().getItem(i).setChecked(false);
                }

                // Set the current item as checked
                menuItem.setChecked(true);
                return true;

            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void getAllBlogData() {
        retrieveBlogListFromSharedPreferences();
    }
    private void retrieveBlogListFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("blogList", null);
        Type type = new TypeToken<ArrayList<BlogModel>>() {}.getType();
        blogModelArrayList = gson.fromJson(json, type);

        if (blogModelArrayList == null) {
            //AndroidUtil.showToast(getApplicationContext(),"blog data found");
            blogModelArrayList = new ArrayList<>();
        }else{
            //AndroidUtil.showToast(getApplicationContext(),"blog data not found");
        }
    }

    /*private void getUserData() {
        FirebaseUtil.getImgFromFirebaseStorage(FirebaseUtil.currentUserId()).getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        profilePicUri= task.getResult();
                        if(profilePicUri!=null){
                            AndroidUtil.setProfilePicture(getApplicationContext(),profilePicUri,profilePic);
                        }

                    }
                });

        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            currentUserModel = task.getResult().toObject(UserModel.class);
            if(currentUserModel!=null){
                tvUsername.setText(currentUserModel.getUserName());
            }
        });
    }*/

    private void init(){
        drawerLayout= (DrawerLayout) findViewById(R.id.drawerLayout);
        buttonDrawerToggle=findViewById(R.id.nav_drawer);
        navigationView=findViewById(R.id.navigationView);
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        tvWeather=findViewById(R.id.tvWeather);

        tvUsername=findViewById(R.id.navdrawer_username);
        profilePic=findViewById(R.id.navdrawer_pofilePic);

        toolbarName= findViewById(R.id.toolbar_name);

    }

    @Override
    public void onBlogItemClick(int index) {
        Intent intent = new Intent(getApplicationContext(), IndividualBlogPage.class);
        intent.putExtra("blogTitle",  blogModelArrayList.get(index).getBlogTitle().toString());
        intent.putExtra("blogImage",  blogModelArrayList.get(index).getBlogImageUrl().toString());
        intent.putExtra("blogContent",  blogModelArrayList.get(index).getBlogContent().toString());
        intent.putExtra("blogWriterName",  blogModelArrayList.get(index).getBlogWriterName().toString());

        //AndroidUtil.showToast(getApplicationContext(),"Index- "+index); //intent.putExtra("blog", (Parcelable) blogModelArrayList.get(index));
        startActivity(intent);
    }



}