package com.example.milkysoft;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.milkysoft.databinding.FragmentMenuInicioBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.milkysoft.databinding.ActivityContentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

public class Content extends AppCompatActivity {

    FragmentManager fragmentManager;
    listaProductos mlistaproductos=new listaProductos();
    menuInicio mMenuInicio= new menuInicio();
    fragmentAddProduct mfragmentAddProduct= new fragmentAddProduct();
    fragmentAddUsuario mfragmentAddUsuario=new fragmentAddUsuario();
    fragmentAddClientes mfragmentAddCliente=new fragmentAddClientes();
    listaPedidosAdmin mfragmentListaPedidosAdmin=new listaPedidosAdmin();
    listaPedidos mfragmentListaPedidos= new listaPedidos();
    String mode;
    MenuView.ItemView itCerraSesion;
    String perfil=null;
    String toShow="";
    String userToShow="";
    FirebaseAuth firebaseAuth;
    FirebaseUser userlogeado;
    String id_cliente;
    FirebaseFirestore mfirestore;
    boolean logeados;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityContentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onStart();
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    String  token = Objects.requireNonNull(task.getResult()).toString();
                    Log.d("toooo","token: "+token);
                }
            }
        });
        toShow= getIntent().getStringExtra("toShow");
        binding = ActivityContentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPreferences preferencias = getSharedPreferences("Here",0);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("Here","Content");
        editor.commit();
        SharedPreferences preferenciasLogeo = getSharedPreferences("preferenciasLogin",0);
        mode=preferenciasLogeo.getString("mode","invitado");
        Toast.makeText(getApplicationContext(),mode,Toast.LENGTH_LONG).show();
        iniciarControles();

        perfil=preferencias.getString("Here","Content");

        Toast.makeText(getApplicationContext(),perfil,Toast.LENGTH_LONG).show();
        setSupportActionBar(binding.appBarContent.toolbar);
        iniciarControles();
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
    private void iniciarControles(){
        final View vistaHeader=binding.navView.getHeaderView(0);
        final TextView tvUsuario=vistaHeader.findViewById(R.id.tvNombreUsuarioHead);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        itCerraSesion=findViewById(R.id.itCerrarSesion);
        try {
            userToShow = user.getEmail().toString();
        }catch(Exception e){

        }
        if(userToShow==""||userToShow==null){
            tvUsuario.setText("Invitado");
        }else {
            tvUsuario.setText(userToShow);
        }
        final TextView tvCerrarSession=vistaHeader.findViewById(R.id.tvCerrarSessionHead);
        tvCerrarSession.setText("CerrarSesion");
        tvCerrarSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("mode", "");
                editor.putString("id_cliente", "");
                editor.putBoolean("logeado", false);
                editor.commit();

                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(),"Session Cerrada",Toast.LENGTH_SHORT).show();
                goLogin();
            }
        });
    }
    private void goLogin(){
        Intent i= new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.itCerrarSesion){
            FirebaseAuth.getInstance().signOut();
            SharedPreferences preferences =getSharedPreferences("preferenciasLogin",0);
            SharedPreferences.Editor editor= preferences.edit();
            editor.clear();
            editor.commit();
            Toast.makeText(getApplicationContext(),"Session Cerrada",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            goLogin();

        }
      /*  if(id==R.id.itIniciarSesion){
            FirebaseAuth.getInstance().signOut();
            SharedPreferences preferences =getSharedPreferences("preferenciasLogin",0);
            SharedPreferences.Editor editor= preferences.edit();
            editor.clear();
            editor.commit();
            Toast.makeText(getApplicationContext(),"Inicia session o crea una cuenta nueva",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            goLogin();

        }*/
        return super.onOptionsItemSelected(item);
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
    public void onStart() {

        /* Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        FirebaseUser user=firebaseAuth.getCurrentUser();*/
        firebaseAuth= FirebaseAuth.getInstance();
        userlogeado=firebaseAuth.getCurrentUser();
        SharedPreferences preferences =getSharedPreferences("preferenciasLogin",0);
        mode=preferences.getString("mode","invitado");
        logeados=preferences.getBoolean("logeado",false);
        //id_clientelogeado=preferences.getString("id_cliente", "");
        if(logeados==true){
            irHome(userlogeado);
        }else{
            Toast.makeText(getApplicationContext(),"Heres",Toast.LENGTH_LONG).show();
        }


        super.onStart();
    } private void irHome(FirebaseUser user) {
        String estadoRegistrov = null;
        id_cliente=user.getUid();
        if (user != null) {
            //user1.setUserName(user.getEmail());
            try {
                mfirestore.collection("Clientes").document(id_cliente).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        // String correo = documentSnapshot.getString("correoCliente");
                        //if (correo != null || correo!= ""||correo!="null") {
                        if (documentSnapshot.exists()) {
                            String estadoRegistrov = documentSnapshot.getString("estadoRegistro");

                            String nombreUsuario=documentSnapshot.getString("nombreCliente")+" "
                                    +documentSnapshot.getString("apellidoCliente");
                            String cedulaUsuario=documentSnapshot.getString("cedulaFacturacion");
                            String correoUsuario=documentSnapshot.getString("correoCliente");
                            String telefonoUsuario=documentSnapshot.getString("telefonoCliente");
                            String direccionUsuario=documentSnapshot.getString("direccionFacturacion");
                            if(telefonoUsuario==""||telefonoUsuario==null){
                                telefonoUsuario=documentSnapshot.getString("celularCliente");
                            }

                            SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("mode", "cliente");
                            editor.putString("id_cliente", id_cliente);
                            editor.putBoolean("logeado", true);
                            editor.putString("nombreUsuario",nombreUsuario);
                            editor.putString("correoUsuario",correoUsuario);
                            editor.putString("cedulaUsuario",cedulaUsuario);
                            editor.putString("telefonoUsuario",telefonoUsuario);
                            editor.putString("direccionUsuario",direccionUsuario);
                            editor.commit();

                            if (estadoRegistrov.equalsIgnoreCase("sinInfo")) {
                                Intent i = new Intent(getApplicationContext(), Content.class);
                                i.putExtra("id_usuario",id_cliente);
                                i.putExtra("toShow", "modificarCliente");
                                i.putExtra("acceso", "cliente");

                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            } else {
                                Intent i = new Intent(getApplicationContext(), Content.class);
                                i.putExtra("id_usuario",id_cliente);
                                i.putExtra("toShow", "menuInicio");
                                i.putExtra("acceso", "cliente");
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }
                        } else {
                            mfirestore.collection("Usuarios").document(id_cliente).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {


                                    if (documentSnapshot.exists()) {
                                        String estadoRegistrov = documentSnapshot.getString("estadoRegistro");
                                        String cargoUsuario = documentSnapshot.getString("cargoUsuario");
                                        String nombreUsuario=documentSnapshot.getString("nombreUsuario")+" "
                                                +documentSnapshot.getString("apellidoUsuario");
                                        String cedulaUsuario=documentSnapshot.getString("cedulaUsuario");
                                        String correoUsuario=documentSnapshot.getString("correoUsuario");
                                        String telefonoUsuario=documentSnapshot.getString("telefonoUsuario");
                                        String provinciaUsuario=documentSnapshot.getString("provinciaUsuario");
                                        String cantonUsuario=documentSnapshot.getString("cantonUsuario");
                                        String callesUsuario=documentSnapshot.getString("callesUsuario");
                                        String direccionUsuario=cantonUsuario+", "+callesUsuario;
                                        if(telefonoUsuario.isEmpty()||telefonoUsuario==""||telefonoUsuario==null){
                                            telefonoUsuario="0999999999";
                                        }
                                        SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("mode", "admin");
                                        editor.putString("id_cliente", id_cliente);
                                        editor.putBoolean("logeado", true);
                                        editor.putString("nombreUsuario",nombreUsuario);
                                        editor.putString("correoUsuario",correoUsuario);
                                        editor.putString("cedulaUsuario",cedulaUsuario);
                                        editor.putString("telefonoUsuario",telefonoUsuario);
                                        editor.putString("direccionUsuario",direccionUsuario);
                                        editor.commit();
                                        Intent i = new Intent(getApplicationContext(),Content.class);
                                        i.putExtra("toShow", "menuInicio");
                                        i.putExtra("acceso", cargoUsuario);

                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);


                                        Toast.makeText(getApplicationContext(), "" + estadoRegistrov, Toast.LENGTH_LONG).show();
                                        Toast.makeText(getApplicationContext(), "Mis datos", Toast.LENGTH_LONG).show();
                                    }
                                    Toast.makeText(getApplicationContext(),"No tienes una cuenta",Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error al Cargar los datos", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        Toast.makeText(getApplicationContext(), "" + estadoRegistrov, Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "Mis datos", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error al Cargar los datos", Toast.LENGTH_LONG).show();
                    }
                });

            }catch (Exception e){

            }
        }
    }
    private void toShow(String toShow){
        if(mode=="invitado"){
            toShow="listaProductos";
        }
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
            case"nuevoCliente":
                fragmentManager=getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.drawer_layout, mfragmentAddCliente).show(mfragmentAddCliente).commit();
                break;
            case "modificarCliente":
                String idcliente1=getIntent().getStringExtra("id_usuario");
                if(idcliente1==""||idcliente1==null){
                    idcliente1=getIntent().getStringExtra("id_cliente");
                }
                Bundle bundleCliente1=new Bundle();
                bundleCliente1.putString("id_cliente",idcliente1);
                mfragmentAddCliente.setArguments(bundleCliente1);
                fragmentManager=getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.drawer_layout, mfragmentAddCliente).show(mfragmentAddCliente).commit();
                break;
            case"listaPedidos":
                fragmentManager=getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.drawer_layout, mfragmentListaPedidos).show(mfragmentListaPedidos).commit();
                break;
            case"listaPedidosAdmin":
                String id_cliente=getIntent().getStringExtra("id_cliente");
                Bundle bundlePedido=new Bundle();
                bundlePedido.putString("id_cliente",id_cliente);
                mfragmentListaPedidosAdmin.setArguments(bundlePedido);
                fragmentManager=getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.drawer_layout, mfragmentListaPedidosAdmin).show(mfragmentListaPedidosAdmin).commit();
                break;
            case"listaProductos":
                startActivity(new Intent(getApplicationContext(),listaProductosact.class));
                break;
            case "listaClientesAdmin":
                startActivity(new Intent(getApplicationContext(),listaClientesactAdmin.class));

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