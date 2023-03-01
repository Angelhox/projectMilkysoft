package com.example.milkysoft;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.milkysoft.Adapter.itemAdapter;
import com.example.milkysoft.Adapter.itemAdapterAdmin;
import com.example.milkysoft.Modelo.Items;
import com.example.milkysoft.databinding.FragmentFragmentdetallespedidoAdminBinding;
import com.example.milkysoft.databinding.FragmentFragmentdetallespedidoBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
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


public class fragmentdetallespedidoAdmin extends DialogFragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {
        private Button btnEnviarPedido,btnUbicacionEntrega;
        private EditText txtLatitud,txtLongitud;
        GoogleMap mMap;
        private FragmentFragmentdetallespedidoAdminBinding binding;
        private TextView tvTotalPagar,tvNombreEntrega, tvCorreoEntrega, tvTelefonoEntrega,
                tvDireccionEntrega, tvCedulaEntrega,tvlatitudEntrega,tvlongitudEntrega;
        private View root;
    FragmentManager fm;
        SearchView srvItems;
        RecyclerView mRecyclerView;
        itemAdapterAdmin mItemAdapterAdmin;
        FirebaseFirestore mFirestore;
        FirebaseAuth mAuth;
        FirebaseUser mUser;
        String usuarioAccion="";
        String id_pedido="";
        String total_pagar="00.01";
        Query query;
        String nombrefac,correofact,telefonofact,direccionfact,cedulafact,latitudfact,longitudfact;
        DatabaseReference reference;
        String token= "cOq6mMIXTqGaEEVQXtkebi:APA91bGjMS_TXSu71mWshocwdnzrzbK0Cx-t5uU4M4oPdhyXx-nX07_2-HQy0PZBu5o1LmOUvm_iA4-oSrgjhftFdINY1-26Zju-Q8yMA6PWXEgQvtN4hXFA1E3C_RL3zbrMkap5obim";


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                id_pedido=getArguments().getString("id_pedido");
                usuarioAccion=getArguments().getString("usuarioaccion");

            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            binding = FragmentFragmentdetallespedidoAdminBinding.inflate(inflater, container, false);
            root = binding.getRoot();
            //SupportMapFragment mapFragment = (SupportMapFragment) getContext().get.getChildFragmentManager().findFragmentById(R.id.map);


            FirebaseMessaging.getInstance().subscribeToTopic("all");
            mAuth=FirebaseAuth.getInstance();
            mUser=mAuth.getCurrentUser();
            mFirestore=FirebaseFirestore.getInstance();
            //usuarioAccion=mUser.getUid();
          /*  SharedPreferences preferencesfact = getActivity().getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
            //preferences.getString("mode", cargoUsuario);
            //preferences.getString("id_cliente", id_cliente);
            //preferences.getString("logeado", true);
            nombrefac=preferencesfact.getString("nombreUsuario","no encontrado");
            correofact=preferencesfact.getString("correoUsuario","no encontrado");
            cedulafact=preferencesfact.getString("cedulaUsuario","no encontrado");
            telefonofact=preferencesfact.getString("telefonoUsuario","no encontrado");
            direccionfact=preferencesfact.getString("direccionUsuario","no encontrado");
            latitudfact=preferencesfact.getString("latitudUsuario","no encontrado");
            longitudfact=preferencesfact.getString("longitudUsuario","no encontrado");*/
            /*mFirestore.collection("Pedidos2").document(usuarioAccion).
                    collection("Pedidos").
                    document(id_pedido)
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    nombrefac = documentSnapshot.getString("clientePedido");
                    correofact = documentSnapshot.getString("correoPedido");
                    cedulafact = documentSnapshot.getString("cedulaPedido");
                    telefonofact =documentSnapshot.getString("telefonoPedido");
                    direccionfact = documentSnapshot.getString("direccionEntrega");
                    latitudfact = documentSnapshot.getString("latitudEntrega");
                    longitudfact = documentSnapshot.getString("longitudEntrega");
                }
                });*/
            SharedPreferences preferencias = this.getActivity().getSharedPreferences("Here",0);
            SharedPreferences.Editor editor = preferencias.edit();
            editor.putString("Here","ItemsPedido");
            editor.commit();
            iniciarControles();

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
            mItemAdapterAdmin=new itemAdapterAdmin(firestoreRecyclerOptions, this.getActivity(),getActivity().getSupportFragmentManager());
            mItemAdapterAdmin.notifyDataSetChanged();
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(mItemAdapterAdmin);
            //Ontener el total a pagar del Pedido
            mFirestore.collection("Pedidos2").document(usuarioAccion).
                    collection("Pedidos").
                    document(id_pedido).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            total_pagar=String.valueOf(documentSnapshot.getDouble("totalPedido"));
                            nombrefac = documentSnapshot.getString("clientePedido");
                            correofact = documentSnapshot.getString("correoPedido");
                            cedulafact = documentSnapshot.getString("cedulaPedido");
                            telefonofact =documentSnapshot.getString("telefonoPedido");
                            direccionfact = documentSnapshot.getString("direccionEntrega");
                            latitudfact = documentSnapshot.getString("latitudEntrega");
                            longitudfact = documentSnapshot.getString("longitudEntrega");
                            tvTotalPagar.setText(total_pagar);
                            valoresfact();

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
                        "Gracias por preferirnos!",
                        "Pronto enviaremos tu pedido :)", getActivity().getApplicationContext(),getActivity());
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
            mItemAdapterAdmin=new itemAdapterAdmin(firestoreRecyclerOptions,this.getActivity(),getActivity().getSupportFragmentManager());
            mItemAdapterAdmin.startListening();
            mRecyclerView.setAdapter(mItemAdapterAdmin);
        }

        private void iniciarControles(){
            btnEnviarPedido=root.findViewById(R.id.btnEnviarPedido);
            srvItems=root.findViewById(R.id.srvPedidos);
            tvTotalPagar=root.findViewById(R.id.tvTotalPagar);
            tvNombreEntrega=root.findViewById(R.id.tvNombreEntregaAdmin);
            tvCorreoEntrega=root.findViewById(R.id.tvCorreoEntregaAdmin);
            tvTelefonoEntrega=root.findViewById(R.id.tvTelefonoEntregaAdmin);
            tvDireccionEntrega=root.findViewById(R.id.tvDireccionEntregaAdmin);
            tvCedulaEntrega=root.findViewById(R.id.tvCedulaEntregaAdmin);
            txtLatitud=root.findViewById(R.id.txtLatitudmy);
           txtLongitud=root.findViewById(R.id.txtLongitudmy);
           tvlatitudEntrega=root.findViewById(R.id.tvLatitudEntregaAdmin);
           tvlongitudEntrega=root.findViewById(R.id.tvLongitudEntregaAdmin);
           btnUbicacionEntrega=root.findViewById(R.id.btnUbicacionEntrega);

        }
        @Override
        public void onStart() {
            super.onStart();
            mItemAdapterAdmin.startListening();
        }

        @Override
        public void onStop() {
            super.onStop();
            mItemAdapterAdmin.stopListening();
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