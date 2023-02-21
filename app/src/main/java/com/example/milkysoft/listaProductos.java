package com.example.milkysoft;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import com.example.milkysoft.Adapter.productosAdapter;
import com.example.milkysoft.Modelo.Producto;
//import com.example.milkysoft.databinding.FragmentAddProductBinding;
import com.example.milkysoft.databinding.FragmentListaProductosBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class listaProductos extends Fragment {
private Button btnAgregarProducto;
    private FragmentListaProductosBinding binding;
    private View root;
    SearchView srvProductos;
    RecyclerView mRecyclerView;
    productosAdapter mProductosAbdapter;
    FirebaseFirestore mFirestore;
    Query query;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentListaProductosBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        SharedPreferences preferencias = this.getActivity().getSharedPreferences("Here",0);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("Here","listaProductos");
        editor.commit();
        iniciarControles();
        mFirestore=FirebaseFirestore.getInstance();
        mRecyclerView=root.findViewById(R.id.rvlistaProductos);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        query=mFirestore.collection("Productos");
        FirestoreRecyclerOptions<Producto>firestoreRecyclerOptions=
                new FirestoreRecyclerOptions.Builder<Producto>()
                        .setQuery(query,Producto.class).build();
        mProductosAbdapter=new productosAdapter(firestoreRecyclerOptions, this.getActivity(),getActivity().getSupportFragmentManager());
        mProductosAbdapter.notifyDataSetChanged();
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setAdapter(mProductosAbdapter);
        searchView();
        btnAgregarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAgregarProducto.setVisibility(View.GONE);
                FragmentTransaction fr= getParentFragmentManager().beginTransaction();
                fr.replace(R.id.layoutLista,new fragmentAddProduct()).commit();
            }
        });

        return root;
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
        mProductosAbdapter=new productosAdapter(firestoreRecyclerOptions,this.getActivity(),getActivity().getSupportFragmentManager());
        mProductosAbdapter.startListening();
        mRecyclerView.setAdapter(mProductosAbdapter);
    }

    private void iniciarControles(){
        btnAgregarProducto=root.findViewById(R.id.btnAgregarProducto);
        srvProductos=root.findViewById(R.id.srvProductos);
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