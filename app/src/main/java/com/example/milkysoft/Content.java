package com.example.milkysoft;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.example.milkysoft.databinding.FragmentMenuInicioBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.milkysoft.databinding.ActivityContentBinding;

public class Content extends AppCompatActivity {
    FragmentManager fragmentManager;
    listaProductos mlistaproductos=new listaProductos();
    menuInicio mMenuInicio= new menuInicio();
    fragmentAddProduct mfragmentAddProduct= new fragmentAddProduct();
    fragmentAddUsuario mfragmentAddUsuario=new fragmentAddUsuario();
    String perfil=null;
    String toShow="";

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityContentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toShow= getIntent().getStringExtra("toShow");
        binding = ActivityContentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPreferences preferencias = getSharedPreferences("Here",0);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("Here","Content");
        editor.commit();

        perfil=preferencias.getString("Here","Content");

        Toast.makeText(getApplicationContext(),perfil,Toast.LENGTH_LONG).show();
        setSupportActionBar(binding.appBarContent.toolbar);
        binding.appBarContent.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //showAddProduct();
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        toShow(toShow);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_content);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.content, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_content);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    /*private void showAddProduct(){
        //fragmentManager=getSupportFragmentManager();
        //fragmentManager.beginTransaction().add(R.id.drawer_layout,mfragmentAddProduct).show(mfragmentAddProduct).commit();
        fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.drawer_layout, menuInicio).show(menuInicio).commit();

    }*/

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==event.KEYCODE_BACK){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("Desea Volver al menu de Inicio").setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
            }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
        return super.onKeyDown(keyCode, event);
    }
*/
    private void toShow(String toShow){
        if(toShow==null||toShow==""){
            toShow="menuInicio";
        }
        switch (toShow){
            case "menuInicio":
                fragmentManager=getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.drawer_layout, mMenuInicio).show(mMenuInicio).commit();
            break;
                case "modificarProducto":
                    String id=getIntent().getStringExtra("id_producto");
                    Bundle bundle=new Bundle();
                    bundle.putString("id_producto",id);
                    mfragmentAddProduct.setArguments(bundle);
               fragmentManager=getSupportFragmentManager();
               fragmentManager.beginTransaction().add(R.id.drawer_layout, mfragmentAddProduct).show(mfragmentAddProduct).commit();
            break;

            case"nuevoProducto":
                fragmentManager=getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.drawer_layout, mfragmentAddProduct).show(mfragmentAddProduct).commit();
            break;
            case"nuevoUsuario":
                fragmentManager=getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.drawer_layout, mfragmentAddUsuario).show(mfragmentAddUsuario).commit();
                break;
            case "modificarUsuario":
                String iduser=getIntent().getStringExtra("id_usuario");
                Bundle bundleUsuario=new Bundle();
                bundleUsuario.putString("id_usuario",iduser);
                mfragmentAddUsuario.setArguments(bundleUsuario);
                fragmentManager=getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.drawer_layout, mfragmentAddUsuario).show(mfragmentAddUsuario).commit();
                break;
        }
    }
    @Override
    public void onBackPressed() {
        SharedPreferences preferencias = getSharedPreferences("Here",0);
        perfil=preferencias.getString("Here","Content");
        Toast.makeText(getApplicationContext(),perfil,Toast.LENGTH_LONG).show();
        switch (perfil) {
            case "Content":
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;

                case "menuInicio":
  //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    break;
            case "listaProductos":
                //startActivity(new Intent(getApplicationContext(),Content.class));
                /*fragmentManager=getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.drawer_layout, menuInicio).show(menuInicio).commit();*/

                break;
            case "AddProduct":
                //fragmentManager=getSupportFragmentManager();
                //fragmentManager.beginTransaction().show(menuInicio).commit();
        }

        super.onBackPressed();
    }
}