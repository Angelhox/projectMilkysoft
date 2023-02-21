package com.example.milkysoft;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import com.example.milkysoft.Adapter.productosAdapter;
import com.example.milkysoft.Modelo.Producto;
import com.example.milkysoft.databinding.FragmentListaProductosBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class listaProductosact extends AppCompatActivity {
    private Button btnAgregarProducto;
    private FragmentListaProductosBinding binding;
    private View root;
    SearchView srvProductos;
    RecyclerView mRecyclerView;
    productosAdapter mProductosAbdapter;
    FirebaseFirestore mFirestore;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productosact);
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



    }
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
        btnAgregarProducto=findViewById(R.id.btnAgregarProductoAct);
        srvProductos=findViewById(R.id.srvProductosAct);
        mRecyclerView=findViewById(R.id.rvlistaProductosAct);
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

}