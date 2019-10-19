package com.androlord.prayaas;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.androlord.prayaas.NavigationFragments.BooksAvailable;
import com.androlord.prayaas.NavigationFragments.Chats;
import com.androlord.prayaas.NavigationFragments.Ebooks;
import com.androlord.prayaas.NavigationFragments.YourBooks;
import com.androlord.prayaas.Support.Login;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity {

    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth mAuth;
    BottomNavigationView bottomNavigation;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.Logout:
                signOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        mAuth.signOut();
        mGoogleSignInClient.signOut();
    }

    //Button signout,profile;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu,menu);
        return true;
    }

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Log.e("ak47","on Start");
        super.onStart();
        Log.e("ak47","on Start after super");
        mAuth.addAuthStateListener(authStateListener);
        Log.e("ak47","on Start Ends");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();


    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth=FirebaseAuth.getInstance();
        bottomNavigation=findViewById(R.id.navigationView);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        BooksAvailable booksAvailable=new BooksAvailable();
        fragmentTransaction.add(R.id.container,booksAvailable);
        fragmentTransaction.commit();
        nav();


        //startActivity(new Intent(MainActivity.this, Login.class));

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null)
                {
                    Log.e("ak47","user null");
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                }
                else
                {
                    Log.e("ak47","user not null");
                }

            }
        };


    }
    private void nav() {


        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentManager fragmentManager=getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

                switch (menuItem.getItemId())
                {
                    case R.id.navigation_book:
                        BooksAvailable booksAvailable=new BooksAvailable();
                        fragmentTransaction.replace(R.id.container,booksAvailable);
                        fragmentTransaction.commit();
                        break;
                    case R.id.navigation_addbook:

                        YourBooks yourBooks=new YourBooks();
                        fragmentTransaction.replace(R.id.container,yourBooks);
                        fragmentTransaction.commit();
                        break;
                    case R.id.navigation_chat:

                        Chats chats=new Chats();
                        fragmentTransaction.replace(R.id.container,chats);
                        fragmentTransaction.commit();
                        break;
                    case R.id.ebooks:

                        Ebooks ebooks=new Ebooks();
                        fragmentTransaction.replace(R.id.container,ebooks);
                        fragmentTransaction.commit();
                        break;


                }
                return true;

            }
        });
    }



}
