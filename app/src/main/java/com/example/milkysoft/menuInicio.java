package com.example.milkysoft;

import android.content.Context;
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
import android.widget.Toast;

import com.example.milkysoft.databinding.FragmentAddProductBinding;
import com.example.milkysoft.databinding.FragmentListaProductosBinding;
import com.example.milkysoft.databinding.FragmentMenuInicioBinding;
import com.google.android.material.card.MaterialCardView;


public class menuInicio extends Fragment {
    String id_producto;
    private TextView txtNombreProducto;
    private FragmentMenuInicioBinding binding;
    private View root;
    String id_cliente;
    String estadoRegistro=null;
    String mode;
    FragmentManager fragmentManager;
    fragmentAddClientes mfragmentAddCliente=new fragmentAddClientes();
    private MaterialCardView btnIrProductos,btnIrUsuarios,btnIrClientes,btnIrPedidos;

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
        SharedPreferences preferenciasLogeo = getActivity().getApplicationContext().getSharedPreferences("preferenciasLogin",0);
        mode=preferenciasLogeo.getString("mode","invitado");
        Toast.makeText(getContext(),mode,Toast.LENGTH_LONG).show();

        binding =FragmentMenuInicioBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        iniciarControles();
        SharedPreferences preferencias = getActivity().getSharedPreferences("Here",0);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("Here","menuInicio");
        editor.commit();

       /* if(mode=="cliente"){
            btnIrClientes.setVisibility(View.GONE);
            btnIrUsuarios.setVisibility(View.GONE);
        }*/

        SharedPreferences preferenciaslogin = getActivity().getSharedPreferences("preferenciasLogin",0);
        estadoRegistro=preferenciaslogin.getString("estadoRegistro","Continue");
        Toast.makeText(getContext(),estadoRegistro,Toast.LENGTH_LONG).show();
        estadoRegistro(estadoRegistro);
        /*if(getArguments()!=null){
            id_cliente=getArguments().getString("id_cliente");
            if(id_cliente!=null||id_cliente!="") {
                estadoRegistro=getArguments().getString("estadoRegistro");
                estadoRegistro(estadoRegistro);
            }
        }*/
        toShow();
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
        btnIrClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),listaClientesact.class));
            }
        });
        btnIrPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(mode!="admin") {
                   Intent i = new Intent(getContext(), Content.class);
                   i.putExtra("toShow", "listaPedidos");
                   startActivity(i);
               }else{
                   Intent i = new Intent(getContext(), Content.class);
                   i.putExtra("toShow", "listaClientesAdmin");
                   startActivity(i);
               }
            }
        });

        return root;    }
    private void mode(){
        SharedPreferences preferenciasLogeo = getActivity().getSharedPreferences("preferenciasLogin",0);
        String mode=preferenciasLogeo.getString("mode","invitado");
        Toast.makeText(getContext(),mode,Toast.LENGTH_LONG).show();

        if(mode=="cliente"){
            btnIrProductos=root.findViewById(R.id.btnIrproductos);
            btnIrUsuarios=root.findViewById(R.id.btnIrUsuarios);
            btnIrClientes=root.findViewById(R.id.btnIrClientes);
            txtNombreProducto=root.findViewById(R.id.txtNombreProducto);
            btnIrClientes.setVisibility(View.GONE);
            btnIrUsuarios.setVisibility(View.GONE);
        }else /*if(mode=="admin")*/{
            btnIrProductos=root.findViewById(R.id.btnIrproductos);
            btnIrUsuarios=root.findViewById(R.id.btnIrUsuarios);
            btnIrClientes=root.findViewById(R.id.btnIrClientes);
            txtNombreProducto=root.findViewById(R.id.txtNombreProducto);
        }
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
        btnIrClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),listaClientesact.class));
            }
        });
        btnIrPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getContext(),Content.class);
                i.putExtra("toShow","listaPedidos");

                startActivity(i);
            }
        });
    }
    private void iniciarControles(){
        btnIrProductos=root.findViewById(R.id.btnIrproductos);
        btnIrUsuarios=root.findViewById(R.id.btnIrUsuarios);
        btnIrClientes=root.findViewById(R.id.btnIrClientes);
        btnIrPedidos=root.findViewById(R.id.btnIrPedidos);
        txtNombreProducto=root.findViewById(R.id.txtNombreProducto);
        if(mode=="cliente"){
            btnIrProductos=root.findViewById(R.id.btnIrproductos);
            btnIrUsuarios=root.findViewById(R.id.btnIrUsuarios);
            btnIrClientes=root.findViewById(R.id.btnIrClientes);
            txtNombreProducto=root.findViewById(R.id.txtNombreProducto);
            btnIrPedidos=root.findViewById(R.id.btnIrPedidos);
            btnIrClientes.setVisibility(View.GONE);
            btnIrUsuarios.setVisibility(View.GONE);
        }else if(mode=="admin"){
            btnIrProductos=root.findViewById(R.id.btnIrproductos);
            btnIrUsuarios=root.findViewById(R.id.btnIrUsuarios);
            btnIrClientes=root.findViewById(R.id.btnIrClientes);
            btnIrPedidos=root.findViewById(R.id.btnIrPedidos);
            txtNombreProducto=root.findViewById(R.id.txtNombreProducto);
        }else if (mode=="invitado"){
            btnIrProductos=root.findViewById(R.id.btnIrproductos);
            btnIrUsuarios=root.findViewById(R.id.btnIrUsuarios);
            btnIrClientes=root.findViewById(R.id.btnIrClientes);
            txtNombreProducto=root.findViewById(R.id.txtNombreProducto);
            btnIrPedidos=root.findViewById(R.id.btnIrPedidos);
            btnIrClientes.setVisibility(View.GONE);
            btnIrUsuarios.setVisibility(View.GONE);

        }
    }
    private void showAddProduct(){
        //fragmentManager=getSupportFragmentManager();
        //fragmentManager.beginTransaction().add(R.id.drawer_layout,mfragmentAddProduct).show(mfragmentAddProduct).commit();
        FragmentTransaction fr= getParentFragmentManager().beginTransaction();
        fr.replace(R.id.loyoutMenuInicio,new listaProductos()).addToBackStack(null).commit();

    }
    private void estadoRegistro(String estadoRegistro){
        if(estadoRegistro=="sinInfo"){
            Toast.makeText(getContext(),
                    "Vamos a completar algunos datos", Toast.LENGTH_LONG).show();
            Intent i=new Intent(getContext(),Content.class);
            i.putExtra("toShow","modificarCliente");
            i.putExtra("id_cliente",id_cliente);
            startActivity(i);
        }else if (estadoRegistro=="Continue"){
            Toast.makeText(getContext(),
                    "Continua en el menu", Toast.LENGTH_LONG).show();
        }
    }

   private void toShow(){
        if(mode=="invitado"){
            startActivity(new Intent(getContext(),listaProductosact.class));
        }
   }




}