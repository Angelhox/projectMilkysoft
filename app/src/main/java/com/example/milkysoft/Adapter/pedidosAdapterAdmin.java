package com.example.milkysoft.Adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.milkysoft.Modelo.Pedidos;
import com.example.milkysoft.R;
import com.example.milkysoft.fragmentdetallespedido;
import com.example.milkysoft.fragmentdetallespedidoAdmin;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;


public class pedidosAdapterAdmin extends FirestoreRecyclerAdapter<Pedidos, pedidosAdapterAdmin.ViewHolder> {
    Activity activity;
    FragmentManager fm;

    public pedidosAdapterAdmin(@NonNull FirestoreRecyclerOptions<Pedidos> options, Activity activity, FragmentManager fm) {
        super(options);
        this.activity=activity;
        this.fm=fm;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pedidos_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Pedidos model) {
        DocumentSnapshot documentSnapshot=getSnapshots().getSnapshot(holder.getAbsoluteAdapterPosition());
        final String id=documentSnapshot.getId();
        holder.codigoPedido.setText(model.getCodigoPedido());
        holder.responsablePedido.setText(model.getResponsablePedido());
        holder.estadoPedido.setText(model.getEstadoPedido());
        holder.clientePedido.setText(model.getClientePedido());
        holder.fechaPedido.setText(model.getFechaPedido());
        /*holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // deleteProdcuto();
            }
        });
        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentAddProduct frm=new fragmentAddProduct();
                openFragment(v,id,frm);
            }
        });
        holder.btnCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carritofragment carritofragment= new carritofragment();
                Bundle bundle= new Bundle();
                bundle.putString("id",id);
                bundle.putString("numeroPedido","pedido"+String.valueOf(numeroPedido));
                bundle.putString("codigoProducto", model.getCodigoProducto());
                carritofragment.setArguments(bundle);
                carritofragment.show(fm,"open fragment");
            }
        });

*/
        holder.btnDetallesPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferenciasusuarioaccion=activity.getSharedPreferences("preferenciasusuarioaccion",0);
                String usuarioaccion=preferenciasusuarioaccion.getString("usuarioaccion","null");

                fragmentdetallespedidoAdmin frdetallespedidoAdmin= new fragmentdetallespedidoAdmin();
                Bundle bundle= new Bundle();
                SharedPreferences preferencias = activity.getSharedPreferences("pedidoCuestion",0);
                SharedPreferences.Editor editor = preferencias.edit();
                editor.putString("pedidoCuestion",id);
                editor.commit();
                bundle.putString("id_pedido",id);
                bundle.putString("usuarioaccion",usuarioaccion);
                frdetallespedidoAdmin.setArguments(bundle);
                frdetallespedidoAdmin.show(fm,"open fragment");
            }
        });
    }


    public  class ViewHolder extends RecyclerView.ViewHolder {
        TextView codigoPedido, fechaPedido,estadoPedido,responsablePedido,
               clientePedido;
        Button btnDetallesPedido;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            codigoPedido = itemView.findViewById(R.id.tvCodigoPedido);
            estadoPedido = itemView.findViewById(R.id.tvEstadoPedido);
            responsablePedido=itemView.findViewById(R.id.tvResponsablePedido);
            clientePedido=itemView.findViewById(R.id.tvClientePedido);
            fechaPedido=itemView.findViewById(R.id.tvFechaPedido);
            btnDetallesPedido=itemView.findViewById(R.id.btnDetallesPedido);
        }
    }
}