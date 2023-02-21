package com.example.milkysoft;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.milkysoft.databinding.FragmentAddProductBinding;
import com.example.milkysoft.databinding.FragmentListaProductosBinding;
import com.example.milkysoft.databinding.FragmentMenuInicioBinding;
import com.google.android.material.card.MaterialCardView;


public class menuInicio extends Fragment {
    String id_producto;
    private TextView txtNombreProducto;
    private FragmentMenuInicioBinding binding;
    private View root;
    private MaterialCardView btnIrProductos,btnIrUsuarios;

    public menuInicio() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding =FragmentMenuInicioBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        SharedPreferences preferencias = getActivity().getSharedPreferences("Here",0);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("Here","menuInicio");
        editor.commit();
        iniciarControles();

        btnIrProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showAddProduct();
                startActivity(new Intent(getContext(),listaProductosact.class));
            }
        });
        btnIrUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showAddProduct();
                startActivity(new Intent(getContext(),listaUsuariosact.class));
            }
        });
        return root;    }
    private void iniciarControles(){
        btnIrProductos=root.findViewById(R.id.btnIrproductos);
        btnIrUsuarios=root.findViewById(R.id.btnIrUsuarios);
        txtNombreProducto=root.findViewById(R.id.txtNombreProducto);
    }
    private void showAddProduct(){
        //fragmentManager=getSupportFragmentManager();
        //fragmentManager.beginTransaction().add(R.id.drawer_layout,mfragmentAddProduct).show(mfragmentAddProduct).commit();
        FragmentTransaction fr= getParentFragmentManager().beginTransaction();
        fr.replace(R.id.loyoutMenuInicio,new listaProductos()).addToBackStack(null).commit();

    }
}