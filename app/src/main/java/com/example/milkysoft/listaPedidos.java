package com.example.milkysoft;

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
import com.example.milkysoft.Adapter.pedidosAdapter;
import com.example.milkysoft.Modelo.Pedidos;
import com.example.milkysoft.databinding.FragmentListaPedidosBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class listaPedidos extends Fragment {
    private Button btnAgregarPedidos;
    private FragmentListaPedidosBinding binding;
    private View root;
    SearchView srvPedidos;
    RecyclerView mRecyclerView;
    pedidosAdapter mPedidosAdapter;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String usuarioAccion="";
    Query query;
    DatabaseReference reference;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentListaPedidosBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        usuarioAccion=mUser.getUid();
        SharedPreferences preferencias = this.getActivity().getSharedPreferences("Here",0);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("Here","listaPedidos");
        editor.commit();
        iniciarControles();
        mFirestore=FirebaseFirestore.getInstance();
        mRecyclerView=root.findViewById(R.id.rvlistaPedidos);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        query=mFirestore.collection("Pedidos2").document(usuarioAccion).collection("Pedidos");
        /*query.
                document(
                "v7SYYtTJ7kFmNvvNh3qD"
                ).collection("Pedidos");*/

        FirestoreRecyclerOptions<Pedidos>firestoreRecyclerOptions=
                new FirestoreRecyclerOptions.Builder<Pedidos>()
                        .setQuery(query,Pedidos.class).build();
        mPedidosAdapter=new pedidosAdapter(firestoreRecyclerOptions, this.getActivity(),getActivity().getSupportFragmentManager());
        mPedidosAdapter.notifyDataSetChanged();
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setAdapter(mPedidosAdapter);
        searchView();
        /*btnAgregarPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //btnAgregarProducto.setVisibility(View.GONE);
                FragmentTransaction fr= getParentFragmentManager().beginTransaction();
                fr.replace(R.id.layoutLista,new fragmentAddProduct()).commit();
            }
        });*/

        return root;
    }
    private void searchView(){
        srvPedidos.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        FirestoreRecyclerOptions<Pedidos>firestoreRecyclerOptions=
                new FirestoreRecyclerOptions.Builder<Pedidos>().setQuery(query.orderBy("fechaPedido")
                        .startAt(s).endAt(s+"~"),Pedidos.class).build();
        mPedidosAdapter=new pedidosAdapter(firestoreRecyclerOptions,this.getActivity(),getActivity().getSupportFragmentManager());
        mPedidosAdapter.startListening();
        mRecyclerView.setAdapter(mPedidosAdapter);
    }

    private void iniciarControles(){
        btnAgregarPedidos=root.findViewById(R.id.btnAgregarPedidos);
        srvPedidos=root.findViewById(R.id.srvPedidos);
    }
    @Override
    public void onStart() {
        super.onStart();
        mPedidosAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPedidosAdapter.stopListening();
    }

}