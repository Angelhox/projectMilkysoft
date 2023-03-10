package com.example.milkysoft;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.milkysoft.Adapter.itemAdapter;
import com.example.milkysoft.Adapter.pedidosAdapter;
import com.example.milkysoft.Modelo.Items;
import com.example.milkysoft.Modelo.Pedidos;
import com.example.milkysoft.databinding.FragmentFragmentdetallespedidoBinding;
import com.example.milkysoft.databinding.FragmentListaPedidosBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;


public class fragmentdetallespedido extends DialogFragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {
        private Button btnEnviarPedido,btnUbicacionEntrega;
        private EditText txtLatitud,txtLongitud;
        GoogleMap mMap;
        private FragmentFragmentdetallespedidoBinding binding;
        private TextView tvTotalPagar,tvNombreEntrega, tvCorreoEntrega, tvTelefonoEntrega,
                tvDireccionEntrega, tvCedulaEntrega,tvlatitudEntrega,tvlongitudEntrega;
        private View root;
    FragmentManager fm;
        SearchView srvItems;
        RecyclerView mRecyclerView;
        itemAdapter mItemAdapter;
        FirebaseFirestore mFirestore;
        FirebaseAuth mAuth;
        FirebaseUser mUser;
        String usuarioAccion="";
        String id_pedido="";
        String total_pagar="00.01";
        Query query;
        String nombrefac,correofact,telefonofact,direccionfact,cedulafact,latitudfact,longitudfact;
        DatabaseReference reference;
        String token= "fudsnxtoT5qifE8x0cc83h:APA91bGsCGtUdCo1JuONndP_cirBTye24iBtn9SMCNLGcKVxD6O8t-Xvv7Raola2zdajR-6RKRjTPVsmuQtbhfXeAPDBjPQ4c2DHi5N0gTSacuiV6KIP2SGEtGpxSz4IrehIO7eKUNjN";


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                id_pedido=getArguments().getString("id_pedido");
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            binding = FragmentFragmentdetallespedidoBinding.inflate(inflater, container, false);
            root = binding.getRoot();
            //SupportMapFragment mapFragment = (SupportMapFragment) getContext().get.getChildFragmentManager().findFragmentById(R.id.map);


            FirebaseMessaging.getInstance().subscribeToTopic("all");
            mAuth=FirebaseAuth.getInstance();
            mUser=mAuth.getCurrentUser();
            usuarioAccion=mUser.getUid();
            SharedPreferences preferencesfact = getActivity().getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
            //preferences.getString("mode", cargoUsuario);
            //preferences.getString("id_cliente", id_cliente);
            //preferences.getString("logeado", true);
            nombrefac=preferencesfact.getString("nombreUsuario","no encontrado");
            correofact=preferencesfact.getString("correoUsuario","no encontrado");
            cedulafact=preferencesfact.getString("cedulaUsuario","no encontrado");
            telefonofact=preferencesfact.getString("telefonoUsuario","no encontrado");
            direccionfact=preferencesfact.getString("direccionUsuario","no encontrado");
            latitudfact=preferencesfact.getString("latitudUsuario","no encontrado");
            longitudfact=preferencesfact.getString("longitudUsuario","no encontrado");

            SharedPreferences preferencias = this.getActivity().getSharedPreferences("Here",0);
            SharedPreferences.Editor editor = preferencias.edit();
            editor.putString("Here","ItemsPedido");
            editor.commit();
            iniciarControles();
            valoresfact();
           /* SupportMapFragment mapFragment= (SupportMapFragment) getActivity()
                    .getSupportFragmentManager().findFragmentById(R.id.map);*/

            mFirestore=FirebaseFirestore.getInstance();
            mRecyclerView=root.findViewById(R.id.rvItemsPedidos);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            query=mFirestore.collection("Pedidos2").document(usuarioAccion).
                    collection("Pedidos").
                    document(id_pedido).collection("Detalles");
        /*query.
                document(
                "v7SYYtTJ7kFmNvvNh3qD"
                ).collection("Pedidos");*/

            FirestoreRecyclerOptions<Items> firestoreRecyclerOptions=
                    new FirestoreRecyclerOptions.Builder<Items>()
                            .setQuery(query,Items.class).build();
            mItemAdapter=new itemAdapter(firestoreRecyclerOptions, this.getActivity(),getActivity().getSupportFragmentManager());
            mItemAdapter.notifyDataSetChanged();
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(mItemAdapter);
            //Ontener el total a pagar del Pedido
            mFirestore.collection("Pedidos2").document(usuarioAccion).
                    collection("Pedidos").
                    document(id_pedido).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            total_pagar=String.valueOf(documentSnapshot.getDouble("totalPedido"));
                            tvTotalPagar.setText(total_pagar);

                        }
                    });
            mFirestore.collection("Pedidos2").document(usuarioAccion).
                    collection("Pedidos").
                    document(id_pedido).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            total_pagar=String.valueOf(value.getDouble("totalPedido"));
                            tvTotalPagar.setText(total_pagar);
                        }
                    });
            //SharedPreferences preferences=getActivity().getSharedPreferences("preferenciasPagar",0);
           // total_pagar=preferences.getString("totalPagar","00.00");

            searchView();
        btnEnviarPedido.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FcmNotificationsSender notificationsSender=new FcmNotificationsSender(token,
                        "Nuevo Pedido",
                        correofact+" ha enviado un nuevo Pedido ", getActivity().getApplicationContext(),getActivity());
                notificationsSender.SendNotifications();

                String direccionEntrega=tvDireccionEntrega.getText().toString();
                String latitudEntrega=tvlatitudEntrega.getText().toString();
                        String longitudEntrega=tvlongitudEntrega.getText().toString();
                Map<String, Object> mapPedido = new HashMap<>();
                mapPedido.put("estadoPedido", "Enviado");
                mapPedido.put("direccionEntrega",direccionEntrega);
                mapPedido.put("latitudEntrega",latitudEntrega);
                mapPedido.put("longitudEntrega",longitudEntrega);
                mFirestore.collection("Pedidos2").document(usuarioAccion).
                        collection("Pedidos")
                        .document(id_pedido).update(mapPedido);
                //if(!title.getText().toString().isEmpty()&&!message.getText().toString().isEmpty()&&
                       // !token.getText().toString().isEmpty()){


                Toast.makeText(getContext(),"Tu pedido se ha Enviado",Toast.LENGTH_LONG).show();
                //btnAgregarProducto.setVisibility(View.GONE);
               /* FragmentTransaction fr= getParentFragmentManager().beginTransaction();
                fr.replace(R.id.layoutLista,new fragmentAddProduct()).commit();*/
                /*AppCompatActivity activity=(AppCompatActivity) v.getContext();
                fragmentMapa mfragmentMapa = new fragmentMapa();
               // Bundle bundle=new Bundle();
                //bundle.putString("id_producto",id);
                //mfragmentAddProduct.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.layoutMapa,mfragmentMapa)
                        .addToBackStack(null).commit();

//
                FragmentTransaction fr= getParentFragment().getParentFragmentManager().beginTransaction();
                fr.replace(R.id.layoutDetalles,new fragmentMapa()).commit();*/
                //fragmentMapa frMapa= new fragmentMapa();
                //Bundle bundle= new Bundle();
                //bundle.putString("id_pedido",id);
                //frMapa.show(fm,"open fragment");

            }

        });
        btnUbicacionEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),MapActivity.class));
            }
        });
            return root;
        }
        private void searchView(){
            srvItems.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

            FirestoreRecyclerOptions<Items>firestoreRecyclerOptions=
                    new FirestoreRecyclerOptions.Builder<Items>().setQuery(query.orderBy("productoNombre")
                            .startAt(s).endAt(s+"~"),Items.class).build();
            mItemAdapter=new itemAdapter(firestoreRecyclerOptions,this.getActivity(),getActivity().getSupportFragmentManager());
            mItemAdapter.startListening();
            mRecyclerView.setAdapter(mItemAdapter);
        }

        private void iniciarControles(){
            btnEnviarPedido=root.findViewById(R.id.btnEnviarPedido);
            srvItems=root.findViewById(R.id.srvPedidos);
            tvTotalPagar=root.findViewById(R.id.tvTotalPagar);
            tvNombreEntrega=root.findViewById(R.id.tvNombreEntrega);
            tvCorreoEntrega=root.findViewById(R.id.tvCorreoEntrega);
            tvTelefonoEntrega=root.findViewById(R.id.tvTelefonoEntrega);
            tvDireccionEntrega=root.findViewById(R.id.tvDireccionEntrega);
            tvCedulaEntrega=root.findViewById(R.id.tvCedulaEntrega);
            txtLatitud=root.findViewById(R.id.txtLatitudmy);
           txtLongitud=root.findViewById(R.id.txtLongitudmy);
           tvlatitudEntrega=root.findViewById(R.id.tvLatitudEntrega);
           tvlongitudEntrega=root.findViewById(R.id.tvLongitudEntrega);
           btnUbicacionEntrega=root.findViewById(R.id.btnUbicacionEntrega);

        }
        @Override
        public void onStart() {
            super.onStart();
            mItemAdapter.startListening();
        }

        @Override
        public void onStop() {
            super.onStop();
            mItemAdapter.stopListening();
        }
        private void valoresfact(){
            tvNombreEntrega.setText(nombrefac);
            tvCorreoEntrega.setText(correofact);
            tvCedulaEntrega.setText(cedulafact);
            tvTelefonoEntrega.setText(telefonofact);
            tvDireccionEntrega.setText(direccionfact);
            tvlongitudEntrega.setText(longitudfact);
            tvlatitudEntrega.setText(latitudfact);

        }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
    mMap=googleMap;
    this.mMap.setOnMapClickListener(this);
    this.mMap.setOnMapLongClickListener(this);
    LatLng cayambe=new LatLng(0.0413934,-78.149972);
    mMap.addMarker(new MarkerOptions().position(cayambe).title("Cayambe"));
    mMap.moveCamera(CameraUpdateFactory.newLatLng(cayambe));
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
txtLatitud.setText(""+latLng.latitude);
        txtLongitud.setText(""+latLng.longitude);
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        txtLatitud.setText(""+latLng.latitude);
        txtLongitud.setText(""+latLng.longitude);

    }
    @Override
    public void onDestroyView() {
        Fragment f = getChildFragmentManager().findFragmentById(R.id.map);
        if (f!=null)
            getChildFragmentManager().beginTransaction()
                    .remove(f).commitAllowingStateLoss();
        super.onDestroyView();
    }
}