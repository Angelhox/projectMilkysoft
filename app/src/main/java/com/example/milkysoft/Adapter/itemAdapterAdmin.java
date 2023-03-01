package com.example.milkysoft.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.milkysoft.Content;
import com.example.milkysoft.Modelo.Items;
import com.example.milkysoft.R;
import com.example.milkysoft.fragmentAddProduct;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class itemAdapterAdmin extends FirestoreRecyclerAdapter<Items, itemAdapterAdmin.ViewHolder> {
    private FirebaseFirestore mFirestore= FirebaseFirestore.getInstance();
    private Double precio;

    Activity activity;
    FragmentManager fm;
    FragmentManager fragmentManager;
    Double totalPagar=0.0;
    String usuarioAccion = "";
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String idParhent;
    Double cantidadNueva=0.0;
    Double totalfinalAnterior=0.0;
    private FirebaseFirestore mfirestore;
    fragmentAddProduct mfragmentAddProduct;
    int numeroPedido = (int)(Math.random() * (10000+1000)-1) + 1000;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public itemAdapterAdmin(@NonNull FirestoreRecyclerOptions<Items> options, Activity activity, FragmentManager fm) {
        super(options);
        this.activity=activity;
        this.fm=fm;


    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.template_item_pedido_admin,parent,false);
        return new ViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Items model) {
        DocumentSnapshot documentSnapshot=getSnapshots().getSnapshot(holder.getAbsoluteAdapterPosition());
        //idParhent=documentSnapshot.getReference().getParent();
        final String id=documentSnapshot.getId();
        //holder.codigo.setText(model.getCodigoProducto());
        holder.nombreItem.setText(model.getProductoNombre());
        holder.cantidaditem.setText(model.getCantidad());
        holder.presentacionItem.setText(model.getPresentacionProducto());
        holder.codigoItem.setText(id);
        holder.totalItem.setText(model.getTotal());
        precio=Double.parseDouble(model.getTotal());
        totalPagar=totalPagar+Double.parseDouble(model.getTotal());
        //holder.totalItem.setText(String.valueOf(totalPagar));
        SharedPreferences preferences= activity.getSharedPreferences("preferenciasPagar",0);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putString("totalPagar",String.valueOf(totalPagar));
        editor.commit();

        String imgItemResource=model.getPhotoItem();
        try{
            if(!imgItemResource.equals("")){
                Picasso.get()
                        .load(imgItemResource)
                        .resize(400,200)
                        .into(holder.imgItem);
            }else{
                holder.imgItem.setImageResource(R.drawable.img);
            }
        }catch(Exception e){
            //Toast.makeText(activity.getApplication(),"No pudimos cargar todas las imagenes",Toast.LENGTH_LONG).show();
            holder.imgItem.setImageResource(R.drawable.img);
        }
       /* holder.totalItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //DocumentSnapshot documentSnapshot=getSnapshots().getSnapshot(holder.getAbsoluteAdapterPosition());

                SharedPreferences preferences= activity.getSharedPreferences("pedidoCuestion",0);
                idParhent= preferences.getString("pedidoCuestion","noEncontrado");
                Toast.makeText(activity.getApplicationContext(), idParhent,Toast.LENGTH_LONG).show();
                mAuth = FirebaseAuth.getInstance();
                mUser = FirebaseAuth.getInstance().getCurrentUser();
                usuarioAccion = mUser.getUid();
                mfirestore=FirebaseFirestore.getInstance();

                String idxxx=mfirestore.collection("Pedidos2").document(usuarioAccion).
                        collection("Pedidos")
                        .document(idParhent).collection("Detalles").document().getId();
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(int i=0;i<queryDocumentSnapshots.size();i++){

                            }
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                documentSnapshot=getSnapshots().getSnapshot(holder.getAbsoluteAdapterPosition());
                                Double cantidadNuevaRecalcular=Double.parseDouble(documentSnapshot.getString("cantidad"));
                                Double precio=Double.parseDouble(documentSnapshot.getString("precio"));
                                Double nuevoTotal=cantidadNuevaRecalcular*precio;
                                Map<String,Object> nuevoValortotal=new HashMap<>();
                                nuevoValortotal.put("total",String.valueOf(nuevoTotal));
                                mfirestore.collection("Pedidos2").document(usuarioAccion).
                                        collection("Pedidos")
                                        .document(idParhent).collection("Detalles").document(id).update(nuevoValortotal);
                            }
                        });


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
        holder.cantidaditem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                SharedPreferences preferences= activity.getSharedPreferences("pedidoCuestion",0);
                idParhent= preferences.getString("pedidoCuestion","noEncontrado");
                Toast.makeText(activity.getApplicationContext(), idParhent,Toast.LENGTH_LONG).show();
                mAuth = FirebaseAuth.getInstance();
                mUser = FirebaseAuth.getInstance().getCurrentUser();
                usuarioAccion = mUser.getUid();
                mfirestore=FirebaseFirestore.getInstance();

                mfirestore.collection("Pedidos2").document(usuarioAccion).
                        collection("Pedidos")
                        .document(idParhent).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                totalfinalAnterior=documentSnapshot.getDouble("totalPedido");
                                Toast.makeText(activity.getApplicationContext(), String.valueOf(totalfinalAnterior), Toast.LENGTH_LONG).show();
                                //Aquitaba
                            }
                        });


                mfirestore.collection("Pedidos2").document(usuarioAccion).
                        collection("Pedidos")
                        .document(idParhent).collection("Detalles").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Double totalAnterior=Double.parseDouble(documentSnapshot.getString("total"));
                                Double cantidadNuevaRecalcular=Double.parseDouble(documentSnapshot.getString("cantidad"));
                                Double precio=Double.parseDouble(documentSnapshot.getString("precio"));
                                Double nuevoTotal=cantidadNuevaRecalcular*precio;
                                Map<String,Object> nuevoValortotal=new HashMap<>();
                                nuevoValortotal.put("total",String.valueOf(nuevoTotal));
                                mfirestore.collection("Pedidos2").document(usuarioAccion).
                                        collection("Pedidos")
                                        .document(idParhent).collection("Detalles").document(id).update(nuevoValortotal);
                                Double totalresta=totalfinalAnterior-totalAnterior;
                                Double nuevoTotalFinal=totalresta+nuevoTotal;
                                Map<String,Object> nuevoValortotalfinal=new HashMap<>();
                                nuevoValortotalfinal.put("totalPedido",nuevoTotalFinal);
                                mfirestore.collection("Pedidos2").document(usuarioAccion).
                                        collection("Pedidos")
                                        .document(idParhent).update(nuevoValortotalfinal);


                            }
                        });


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.btnEliminarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // deleteProdcuto();
            }
        });

        holder.btnMasItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                masItem(id);
            }
        });
        holder.btnMenosItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   carritofragment carritofragment= new carritofragment();
                Bundle bundle= new Bundle();
                bundle.putString("id",id);
                bundle.putString("numeroPedido","pedido"+String.valueOf(numeroPedido));
                bundle.putString("codigoProducto", model.getCodigoProducto());
                carritofragment.setArguments(bundle);
                carritofragment.show(fm,"open fragment");*/


            }
        });

    }
    private void openFragment(View view,String id,fragmentAddProduct frm){
      /*  listaProductos frmListaProductos= new listaProductos();
        Bundle bundle=new Bundle();
        bundle.putString("id_producto",id);
        frm.setArguments(bundle);
        FragmentTransaction fr= Content.class.beginTransaction();
        fr.replace(R.id.drawer_layout,new fragmentAddProduct()).commit();*/
      /*  Unused but important don't delete <--------------
      AppCompatActivity activity=(AppCompatActivity) view.getContext();
        fragmentAddProduct mfragmentAddProduct = new fragmentAddProduct();
        Bundle bundle=new Bundle();
        bundle.putString("id_producto",id);
        mfragmentAddProduct.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.layoutLista,mfragmentAddProduct)
                .addToBackStack(null).commit();*/
        Intent i = new Intent(activity.getApplicationContext(), Content.class);
        i.putExtra("toShow","modificarProducto");
        i.putExtra("id_producto",id);

        activity.startActivity(i);
    }
    //try
    private void masItem(String id){


        SharedPreferences preferences= activity.getSharedPreferences("pedidoCuestion",0);
        idParhent= preferences.getString("pedidoCuestion","noEncontrado");
        Toast.makeText(activity.getApplicationContext(), idParhent,Toast.LENGTH_LONG).show();
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        usuarioAccion = mUser.getUid();

        mfirestore=FirebaseFirestore.getInstance();
        mfirestore.collection("Pedidos2").document(usuarioAccion).
                collection("Pedidos")
                .document(idParhent).collection("Detalles").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Double cantidadAnterior=Double.parseDouble(documentSnapshot.getString("cantidad"));
                        cantidadNueva=cantidadAnterior+1;
                        Map<String,Object> nuevoValor=new HashMap<>();
                        nuevoValor.put("cantidad",String.valueOf(cantidadNueva));
                        mfirestore.collection("Pedidos2").document(usuarioAccion).
                                collection("Pedidos")
                                .document(idParhent).collection("Detalles").document(id).update(nuevoValor);
                    }
                });


       /* mfirestore.collection("Pedidos2").document(usuarioAccion).
                collection("Pedidos")
                .document(idParhent).collection("Detalles").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Double precioAnterior = Double.parseDouble(documentSnapshot.getString("precio"));
                        Double cantidadAnterior = Double.parseDouble(documentSnapshot.getString("cantidad"));

                        int cantidadTotal = Integer.parseInt(cantidad);
                    }
                })

        Double precioUnitario = Double.parseDouble(precio);
        int cantidadTotal = Integer.parseInt(cantidad);
        Double total = precioUnitario * cantidadTotal;
        Double totalnuevo = totalanterior + total;
        Map<String, Object> mapProductosPedido = new HashMap<>();
        mapProductosPedido.put("total", String.valueOf(total));
        mfirestore.collection("Pedidos2").document(usuarioAccion).collection("Pedidos").document(numeroPedido).
                collection("Detalles").document(codigoProducto).set(mapProductosPedido)
                .addOnSuccessListener*/


    }


    //try

    public class ViewHolder extends RecyclerView.ViewHolder{
        EditText txttest;
        TextView nombreItem,codigoItem,
                totalItem,presentacionItem,precioItem,
                cantidaditem,precioVenta,descripcion,unidadMedida,descripcionToShow;
        Button btnEliminarItem,btnMasItem,btnMenosItem;/*,imgProducto,btnCarrito;*/
        ImageView imgItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreItem=itemView.findViewById(R.id.tvNombreProductoItem);
            //codigo=itemView.findViewById(R.id.txtCodigoProducto);
            cantidaditem=itemView.findViewById(R.id.tvCantidadSolicitada);
            presentacionItem=itemView.findViewById(R.id.tvPresentacionItem);
            totalItem=itemView.findViewById(R.id.tvTotalItem);
            codigoItem=itemView.findViewById(R.id.tvCodigoProductoItem );

            imgItem=itemView.findViewById(R.id.imgItem);
            btnEliminarItem=itemView.findViewById(R.id.btnEliminarItem);
            btnMasItem=itemView.findViewById(R.id.btnMasItem);
            btnMenosItem=itemView.findViewById(R.id.btnMenosItem);

        }
        private void cantidadPrecio(String id){


        }
    }
}
