package com.example.milkysoft;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.net.InternetDomainName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.type.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class  carritofragment extends DialogFragment {
    private FrameLayout constraintLayout;
    String id;
    String usuarioAccion = "";
    String numeroPedido = "";
    String codigoProducto = "";
    String photoProducto = "";
    String timeStamp = new SimpleDateFormat("HH:mm:ss yyyy/MM/dd").format(Calendar.getInstance().getTime());


    private Button btnAdd;
    private EditText etCantidadcarrito;
    private TextView tvNombrecarrito, tvPreciocarrito, tvPresentacioncarrito;
    private FirebaseFirestore mfirestore;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    FirebaseUser mUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString("id");
            numeroPedido = getArguments().getString("numeroPedido");
            codigoProducto = getArguments().getString("codigoProducto");
            photoProducto = getArguments().getString("photoProducto");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_carritofragment, container, false);
        View viewLista = inflater.inflate(R.layout.activity_lista_productosact, container, false);
        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        usuarioAccion = mUser.getUid();

        btnAdd = v.findViewById(R.id.btnAddcarrito);
        tvPresentacioncarrito = v.findViewById(R.id.tvPresentacionCarrito);
        tvNombrecarrito = v.findViewById(R.id.tvNombrecarrito);
        tvPreciocarrito = v.findViewById(R.id.tvPreciocarrito);
        etCantidadcarrito = v.findViewById(R.id.etCantidadcarrito);
        constraintLayout = viewLista.findViewById(R.id.layoutListaAct);
        //if(id==null||id==""){
        getProducto();

        //}

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = tvNombrecarrito.getText().toString();
                String precio = tvPreciocarrito.getText().toString();
                String cantidad = etCantidadcarrito.getText().toString();
                String presentacion = tvPresentacioncarrito.getText().toString();
                postItemcompra(nombre, precio, cantidad, presentacion);
            }
        });
        return v;

    }


    private void postItemcompra(String producto, String precio, String cantidad, String presentacion) {

        SharedPreferences preferences = getContext().getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        //Unused  preferences.getString("mode", "");
        //Take current User editor.putString("id_cliente", id_cliente);
        //Unused editor.putBoolean("logeado", true);
        String clientePedido = preferences.getString("nombreUsuario", "noEncontrado");
        String correoPedido = preferences.getString("correoUsuario", "noEncontrado");
        String cedulaPedido = preferences.getString("cedulaUsuario", "noEncontrado");
        String telefonoPedido = preferences.getString("telefonoUsuario", "noEncontrado");

        /*Double precioUnitario = Double.parseDouble(precio);
        int cantidadTotal = Integer.parseInt(cantidad);
        Double total = precioUnitario * cantidadTotal;
        Map<String, Object> mapProductosPedido = new HashMap<>();
        mapProductosPedido.put("productoNombre", producto);
        mapProductosPedido.put("precio", precio);
        mapProductosPedido.put("cantidad", cantidad);
        mapProductosPedido.put("total", String.valueOf(total));
        mapProductosPedido.put("photoItem", photoProducto);
        mapProductosPedido.put("pesentacionProducto", presentacion);
        Map<String, Object> mapProductosField = new HashMap<>();
        mapProductosField.put("responsable", usuarioAccion);*/


        Map<String, Object> mapPedidoRes = new HashMap<>();
        mapPedidoRes.put("responsable", usuarioAccion);

        mfirestore.collection("Pedidos2").document(usuarioAccion).set(mapPedidoRes);
        mfirestore.collection("Pedidos2").document(usuarioAccion).
                collection("Pedidos")
                .document(numeroPedido).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Double totalanterior = 0.0;
                            totalanterior = documentSnapshot.getDouble("totalPedido");
                            if (totalanterior == null || totalanterior == 0.0) {
                                totalanterior = 0.1;

                            } else {
                                totalanterior = documentSnapshot.getDouble("totalPedido");
                            }
                            Double precioUnitario = Double.parseDouble(precio);
                            int cantidadTotal = Integer.parseInt(cantidad);
                            Double total = precioUnitario * cantidadTotal;
                            Double totalnuevo = totalanterior + total;
                            Map<String, Object> mapProductosPedido = new HashMap<>();
                            mapProductosPedido.put("productoNombre", producto);
                            mapProductosPedido.put("precio", precio);
                            mapProductosPedido.put("cantidad", cantidad);
                            mapProductosPedido.put("total", String.valueOf(total));
                            mapProductosPedido.put("photoItem", photoProducto);
                            mapProductosPedido.put("pesentacionProducto", presentacion);
                            Map<String, Object> mapProductosField = new HashMap<>();
                            mapProductosField.put("responsable", usuarioAccion);
                            mfirestore.collection("Pedidos2").document(usuarioAccion).collection("Pedidos").document(numeroPedido).
                                    collection("Detalles").document(codigoProducto).set(mapProductosPedido)
                                    .addOnSuccessListener
                                    /*;
                mfirestore.collection("Pedidos2").document(usuarioAccion).collection("Pedidos")
                        .document(numeroPedido).collection("Detalles").document("100").collection("Datos")
                        .document().set(mapProductosPedido)*/(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getContext(), "", Toast.LENGTH_LONG).show();
                                            SharedPreferences compras = getActivity().getSharedPreferences("Compras", 0);
                                            SharedPreferences.Editor editor = compras.edit();
                                            editor.putString("Compra", "si");
                                            editor.putInt("compraNumero", 1);
                                            editor.commit();
                                            dismiss();
                                            customizeSnackbar();
                                            Map<String, Object> mapNuevoTotal = new HashMap<>();
                                            mapNuevoTotal.put("totalPedido", totalnuevo);
                                            mfirestore.collection("Pedidos2").document(usuarioAccion).collection("Pedidos").document(numeroPedido).
                                                    update(mapNuevoTotal);

                                        }
                                    })

                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });

                        } else {
                            Map<String, Object> mapPedido = new HashMap<>();
                            mapPedido.put("estadoPedido", "En espera de confirmacion");
                            mapPedido.put("codigoPedido", numeroPedido);
                            mapPedido.put("clientePedido", clientePedido);
                            mapPedido.put("responsablePedido", clientePedido);
                            mapPedido.put("correoPedido", correoPedido);
                            mapPedido.put("cedulaPedido", cedulaPedido);
                            mapPedido.put("telefonoPedido", telefonoPedido);
                            mapPedido.put("direccionEntrega","");
                            mapPedido.put("laitudEntrega","");
                            mapPedido.put("longitudEntrega","");
                            mapPedido.put("fechaPedido", timeStamp);
                            mapPedido.put("totalPedido", 0.0);
                            mfirestore.collection("Pedidos2").document(usuarioAccion).
                                    collection("Pedidos")
                                    .document(numeroPedido).set(mapPedido);
                           /* Double totalanterior = 0.0;
                            totalanterior = documentSnapshot.getDouble("totalPedido");
                            if (totalanterior == null || totalanterior == 0.0) {
                                totalanterior = 0.1;

                            } else {
                                totalanterior = documentSnapshot.getDouble("totalPedido");
                            }*/
                            Double precioUnitario = Double.parseDouble(precio);
                            int cantidadTotal = Integer.parseInt(cantidad);
                            Double total = precioUnitario * cantidadTotal;
                            Double totalnuevo = /*totalanterior + */total;
                            Map<String, Object> mapProductosPedido = new HashMap<>();
                            mapProductosPedido.put("productoNombre", producto);
                            mapProductosPedido.put("precio", precio);
                            mapProductosPedido.put("cantidad", cantidad);
                            mapProductosPedido.put("total", String.valueOf(total));
                            mapProductosPedido.put("photoItem", photoProducto);
                            mapProductosPedido.put("pesentacionProducto", presentacion);
                            Map<String, Object> mapProductosField = new HashMap<>();
                            mapProductosField.put("responsable", usuarioAccion);
                            mfirestore.collection("Pedidos2").document(usuarioAccion).collection("Pedidos").document(numeroPedido).
                                    collection("Detalles").document(codigoProducto).set(mapProductosPedido);
                            Map<String, Object> mapNuevoTotal = new HashMap<>();
                            mapNuevoTotal.put("totalPedido", totalnuevo);
                            mfirestore.collection("Pedidos2").document(usuarioAccion).collection("Pedidos").document(numeroPedido).
                                    update(mapNuevoTotal);
                        }

                    }}).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "Error al Cargar los datos", Toast.LENGTH_LONG).show();
                                        }
                                    });
                }


    private void getProducto() {
        mfirestore.collection("Productos").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = documentSnapshot.getString("nombreProducto");
                String age = documentSnapshot.getString("precioCompra");
                String presentacion = documentSnapshot.getString("presentacion");
                String contenido = documentSnapshot.getString("contenidoproducto");

                tvNombrecarrito.setText(name);
                tvPreciocarrito.setText(age);
                tvPresentacioncarrito.setText(presentacion + " de " + contenido);
                etCantidadcarrito.setText("1");
                Toast.makeText(getContext(), "Mis datos", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al Cargar los datos", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void customizeSnackbar() {
        final Snackbar snackbar = Snackbar.make(constraintLayout, "", Snackbar.LENGTH_INDEFINITE);
        View customize = getLayoutInflater().inflate(R.layout.customsnackbar, null);
        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.setPadding(0, 0, 0, 0);
        (customize.findViewById(R.id.Sucribecustom)).setOnClickListener(View -> {
            Toast.makeText(getContext(), "Se habre los pedidos", Toast.LENGTH_LONG).show();

            snackbar.dismiss();
        });
        snackbarLayout.addView(customize, 0);
        snackbar.show();
    }
}
