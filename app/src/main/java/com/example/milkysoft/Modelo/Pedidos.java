package com.example.milkysoft.Modelo;

public class Pedidos {
    String codigoPedido;
    String fechaPedido;
    String estadoPedido;
    String responsablePedido;
    String clientePedido;

    public Pedidos(String codigoPedido, String fechaPedido, String estadoPedido, String responsablePedido, String clientePedido) {
        this.codigoPedido = codigoPedido;
        this.fechaPedido = fechaPedido;
        this.estadoPedido = estadoPedido;
        this.responsablePedido = responsablePedido;
        this.clientePedido = clientePedido;
    }

    public String getResponsablePedido() {
        return responsablePedido;
    }

    public void setResponsablePedido(String responsablePedido) {
        this.responsablePedido = responsablePedido;
    }

    public String getClientePedido() {
        return clientePedido;
    }

    public void setClientePedido(String clientePedido) {
        this.clientePedido = clientePedido;
    }

    public Pedidos(String codigoPedido, String fechaPedido, String estadoPedido) {
        this.codigoPedido = codigoPedido;
        this.fechaPedido = fechaPedido;
        this.estadoPedido = estadoPedido;
    }

    public Pedidos() {

    }

    public String getCodigoPedido() {
        return codigoPedido;
    }

    public void setCodigoPedido(String codigoPedido) {
        this.codigoPedido = codigoPedido;
    }

    public String getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(String fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(String estadoPedido) {
        this.estadoPedido = estadoPedido;
    }
}
