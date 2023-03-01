package com.example.milkysoft;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.milkysoft.Adapter.productosAdapter;
import com.example.milkysoft.Modelo.Producto;
import com.example.milkysoft.databinding.FragmentListaProductosBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class listaProductosact extends AppCompatActivity {
    private Button btnAgregarProducto,btnIniciarSesion;
    private FrameLayout constraintLayout;
    private LinearLayout layoutIniciarSession,layoutAddProducto;
    private FragmentListaProductosBinding binding;
    private View root;
    SearchView srvProductos;
    String compra="";
    String mode="";
    int numeroCompra=0;
    int numeroPedido = (int)(Math.random() * (10000+1000)-1) + 1000;
    RecyclerView mRecyclerView;
    productosAdapter mProductosAbdapter;
    FirebaseFirestore mFirestore;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productosact);
        SharedPreferences preferenciasLogeo = getSharedPreferences("preferenciasLogin",0);
        mode=preferenciasLogeo.getString("mode","invitado");
        Toast.makeText(getApplicationContext(),mode,Toast.LENGTH_LONG).show();
        SharedPreferences compras= getSharedPreferences("Compras",0);
        compra=compras.getString("Compra","no");
        numeroCompra=numeroCompra+compras.getInt("compraNumero",0);
        iniciarControles();

        mFirestore=FirebaseFirestore.getInstance();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        query=mFirestore.collection("Productos");
        FirestoreRecyclerOptions<Producto>firestoreRecyclerOptions=
                new FirestoreRecyclerOptions.Builder<Producto>()
                        .setQuery(query,Producto.class).build();
        mProductosAbdapter=new productosAdapter(firestoreRecyclerOptions, this,getSupportFragmentManager());
        mProductosAbdapter.notifyDataSetChanged();
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setAdapter(mProductosAbdapter);
        searchView();
        btnAgregarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(),Content.class);

                i.putExtra("toShow","nuevoProducto");
                startActivity(i);

            }
        });
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });



    }

    @Nullable


    private void searchView(){
        srvProductos.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                textSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                textSearch(s);
                return false;
            }
        });
    }




    private void textSearch(String s) {

        FirestoreRecyclerOptions<Producto>firestoreRecyclerOptions=
                new FirestoreRecyclerOptions.Builder<Producto>().setQuery(query.orderBy("nombreProducto")
                        .startAt(s).endAt(s+"~"),Producto.class).build();
        mProductosAbdapter=new productosAdapter(firestoreRecyclerOptions,this,getSupportFragmentManager());
        mProductosAbdapter.startListening();
        mRecyclerView.setAdapter(mProductosAbdapter);
    }

    private void iniciarControles(){
        mRecyclerView = findViewById(R.id.rvlistaProductosAct);
        srvProductos = findViewById(R.id.srvProductosAct);
        btnAgregarProducto = findViewById(R.id.btnAgregarProductoAct);
        srvProductos = findViewById(R.id.srvProductosAct);
        constraintLayout = findViewById(R.id.layoutListaAct);
        btnIniciarSesion = findViewById(R.id.btnIniciarSession);
        layoutIniciarSession=findViewById(R.id.layoutIniciarSession);
        if(mode=="invitado") {
            btnAgregarProducto = findViewById(R.id.btnAgregarProductoAct);
            srvProductos = findViewById(R.id.srvProductosAct);
            constraintLayout = findViewById(R.id.layoutListaAct);
            btnIniciarSesion = findViewById(R.id.btnIniciarSession);
            layoutIniciarSession=findViewById(R.id.layoutIniciarSession);
            //layoutAddProducto=findViewById(R.id.layoutAddProducto);
           // layoutIniciarSession.setVisibility(View.GONE);
            btnAgregarProducto.setVisibility(View.GONE);
            //layoutAddProducto.setVisibility(View.GONE);

        }else if(mode=="admin"){
            btnAgregarProducto = findViewById(R.id.btnAgregarProductoAct);
            srvProductos = findViewById(R.id.srvProductosAct);
            constraintLayout = findViewById(R.id.layoutListaAct);
            btnIniciarSesion = findViewById(R.id.btnIniciarSession);
            layoutIniciarSession=findViewById(R.id.layoutIniciarSession);
            //layoutAddProducto=findViewById(R.id.layoutAddProducto);
            layoutIniciarSession.setVisibility(View.GONE);
            //btnAgregarProducto.setVisibility(View.GONE);
            //layoutAddProducto.setVisibility(View.GONE);

        }else if(mode=="cliente"){
            btnAgregarProducto = findViewById(R.id.btnAgregarProductoAct);
            srvProductos = findViewById(R.id.srvProductosAct);
            constraintLayout = findViewById(R.id.layoutListaAct);
            btnIniciarSesion = findViewById(R.id.btnIniciarSession);
            layoutIniciarSession=findViewById(R.id.layoutIniciarSession);
           // layoutAddProducto=findViewById(R.id.layoutAddProducto);
            layoutIniciarSession.setVisibility(View.GONE);
            btnAgregarProducto.setVisibility(View.GONE);
            //layoutAddProducto.setVisibility(View.GONE);

        }
    }
    @Override
    public void onStart() {
        super.onStart();
        mProductosAbdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mProductosAbdapter.stopListening();
    }
    public void customizeSnackbar(){
        final Snackbar snackbar= Snackbar.make(constraintLayout,"",Snackbar.LENGTH_INDEFINITE);
        View customize=getLayoutInflater().inflate(R.layout.customsnackbar,null);
        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackbarLayout=(Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.setPadding(0,0,0,0);
        (customize.findViewById(R.id.Sucribecustom)).setOnClickListener(View -> {
            Toast.makeText(getApplicationContext(),"Se habre los pedidos",Toast.LENGTH_LONG).show();

            snackbar.dismiss();
        });
        snackbarLayout.addView(customize,0);
        snackbar.show();
    }


}
