package com.example.milkysoft.Modelo;

public class Items {
    String id;
    String cantidad;
    String presentacionProducto;
    String precio;
    String productoNombre;
    String total;
    String photoItem;

    public Items(String id, String cantidad, String presentacionProducto, String precio, String productoNombre, String total) {
        this.id = id;
        this.cantidad = cantidad;
        this.presentacionProducto = presentacionProducto;
        this.precio = precio;
        this.productoNombre = productoNombre;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPresentacionProducto() {
        return presentacionProducto;
    }

    public void setPresentacionProducto(String presentacionProducto) {
        this.presentacionProducto = presentacionProducto;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPhotoItem() {
        return photoItem;
    }

    public void setPhotoItem(String photoItem) {
        this.photoItem = photoItem;
    }

    public Items(String id, String cantidad, String presentacionProducto, String precio, String productoNombre, String total, String photoItem) {
        this.id = id;
        this.cantidad = cantidad;
        this.presentacionProducto = presentacionProducto;
        this.precio = precio;
        this.productoNombre = productoNombre;
        this.total = total;
        this.photoItem = photoItem;
    }

    public Items() {
    }
}
