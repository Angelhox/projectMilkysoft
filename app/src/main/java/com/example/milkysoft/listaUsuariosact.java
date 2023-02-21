package com.example.milkysoft;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import com.example.milkysoft.Adapter.usuariosAdapter;
import com.example.milkysoft.Modelo.Usuario;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class listaUsuariosact extends AppCompatActivity {
    private Button btnAgregarUsuario;
    private View root;
    SearchView srvUsuarios;
    RecyclerView mRecyclerView;
    usuariosAdapter mUsuariosAbdapter;
    FirebaseFirestore mFirestore;
    Query query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuariosact);
        iniciarControles();
        mFirestore=FirebaseFirestore.getInstance();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        query=mFirestore.collection("Usuarios");
        FirestoreRecyclerOptions<Usuario> firestoreRecyclerOptions=
                new FirestoreRecyclerOptions.Builder<Usuario>()
                        .setQuery(query, Usuario.class).build();
        mUsuariosAbdapter=new usuariosAdapter(firestoreRecyclerOptions, this,getSupportFragmentManager());
        mUsuariosAbdapter.notifyDataSetChanged();
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setAdapter(mUsuariosAbdapter);
        searchView();
        btnAgregarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(),Content.class);

                i.putExtra("toShow","nuevoUsuario");
                startActivity(i);

            }
        });

    }
    private void searchView(){
        srvUsuarios.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        FirestoreRecyclerOptions<Usuario>firestoreRecyclerOptions=
                new FirestoreRecyclerOptions.Builder<Usuario>().setQuery(query.orderBy("nombreUsuario")
                        .startAt(s).endAt(s+"~"),Usuario.class).build();
        mUsuariosAbdapter=new usuariosAdapter(firestoreRecyclerOptions,this,getSupportFragmentManager());
        mUsuariosAbdapter.startListening();
        mRecyclerView.setAdapter(mUsuariosAbdapter);
    }

    private void iniciarControles(){
        btnAgregarUsuario=findViewById(R.id.btnAgregarUsuariosAct);
        srvUsuarios=findViewById(R.id.srvUsuariosAct);
        mRecyclerView=findViewById(R.id.rvlistaUsuariosAct);
    }
    @Override
    public void onStart() {
        super.onStart();
        mUsuariosAbdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mUsuariosAbdapter.stopListening();
    }


}