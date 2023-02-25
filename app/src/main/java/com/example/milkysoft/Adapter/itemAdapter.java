package com.example.milkysoft.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.milkysoft.Content;
import com.example.milkysoft.Modelo.Items;
import com.example.milkysoft.Modelo.Producto;
import com.example.milkysoft.R;
import com.example.milkysoft.carritofragment;
import com.example.milkysoft.fragmentAddProduct;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class itemAdapter extends FirestoreRecyclerAdapter<Items,itemAdapter.ViewHolder> {
    private FirebaseFirestore mFirestore= FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager fm;
    FragmentManager fragmentManager;
    Double totalPagar=0.0;
    fragmentAddProduct mfragmentAddProduct;
    int numeroPedido = (int)(Math.random() * (10000+1000)-1) + 1000;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public itemAdapter(@NonNull FirestoreRecyclerOptions<Items> options, Activity activity, FragmentManager fm) {
        super(options);
        this.activity=activity;
        this.fm=fm;


    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.template_item_pedido,parent,false);
        return new ViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Items model) {
        DocumentSnapshot documentSnapshot=getSnapshots().getSnapshot(holder.getAbsoluteAdapterPosition());
        final String id=documentSnapshot.getId();
        //holder.codigo.setText(model.getCodigoProducto());
        holder.nombreItem.setText(model.getProductoNombre());
        holder.cantidaditem.setText(model.getCantidad());
        holder.presentacionItem.setText(model.getPresentacionProducto());
        holder.codigoItem.setText(id);
        holder.totalItem.setText(model.getTotal());
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
        holder.btnEliminarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // deleteProdcuto();
            }
        });

        holder.btnMasItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* fragmentAddProduct frm=new fragmentAddProduct();
                openFragment(v,id,frm);*/
            }
        });
        holder.btnMenosItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*carritofragment carritofragment= new carritofragment();
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

    public class ViewHolder extends RecyclerView.ViewHolder{
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
    }
}
