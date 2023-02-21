package com.example.milkysoft.Modelo;

public class Producto {
    String id;
    String nombreProducto;
    String codigoProducto;
    String pesoProducto;
    String stock;
    String cantidad;
    String contenido;
    String categoria;
    String presentacion;
    String contenidoAzucar;
    String contenidoSal;
    String contenidoGrasa;
    String descripcion;
    String precioCompra;
    String precioVenta;
    String photo;
    String unidadMedida;

    public Producto(String nombreProducto, String codigoProducto, String pesoProducto, String stock, String contenido, String categoria, String presentacion, String contenidoAzucar, String contenidoSal, String contenidoGrasa, String descripcion, String precioCompra, String precioVenta, String photo, String unidadMedida) {
        this.nombreProducto = nombreProducto;
        this.codigoProducto = codigoProducto;
        this.pesoProducto = pesoProducto;
        this.stock = stock;
        this.contenido = contenido;
        this.categoria = categoria;
        this.presentacion = presentacion;
        this.contenidoAzucar = contenidoAzucar;
        this.contenidoSal = contenidoSal;
        this.contenidoGrasa = contenidoGrasa;
        this.descripcion = descripcion;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.photo = photo;
        this.unidadMedida = unidadMedida;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }



    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    public Producto() {
    }




    public Producto(String id, String nombreProducto, String codigoProducto, String pesoProducto, String stock, String contenido, String categoria, String presentacion, String contenidoAzucar, String contenidoSal, String contenidoGrasa, String descripcion, String precioCompra, String precioVenta, String photo, String unidadMedida) {
        this.id = id;
        this.nombreProducto = nombreProducto;
        this.codigoProducto = codigoProducto;
        this.pesoProducto = pesoProducto;
        this.stock = stock;
        this.contenido = contenido;
        this.categoria = categoria;
        this.presentacion = presentacion;
        this.contenidoAzucar = contenidoAzucar;
        this.contenidoSal = contenidoSal;
        this.contenidoGrasa = contenidoGrasa;
        this.descripcion = descripcion;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.photo = photo;
        this.unidadMedida = unidadMedida;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getPesoProducto() {
        return pesoProducto;
    }

    public void setPesoProducto(String pesoProducto) {
        this.pesoProducto = pesoProducto;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getContenido() {
        return contenido;
    }



    public Producto(String nombreProducto, String codigoProducto, String pesoProducto, String stock, String contenido, String categoria, String presentacion, String contenidoAzucar, String contenidoSal, String contenidoGrasa, String descripcion, String precioCompra, String precioVenta) {
        this.nombreProducto = nombreProducto;
        this.codigoProducto = codigoProducto;
        this.pesoProducto = pesoProducto;
        this.stock = stock;
        this.contenido = contenido;
        this.categoria = categoria;
        this.presentacion = presentacion;
        this.contenidoAzucar = contenidoAzucar;
        this.contenidoSal = contenidoSal;
        this.contenidoGrasa = contenidoGrasa;
        this.descripcion = descripcion;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
    }

    public Producto(String nombreProducto, String codigoProducto, String pesoProducto, String stock, String contenido, String categoria, String presentacion, String contenidoAzucar, String contenidoSal, String contenidoGrasa, String descripcion, String precioCompra, String precioVenta, String photo) {
        this.nombreProducto = nombreProducto;
        this.codigoProducto = codigoProducto;
        this.pesoProducto = pesoProducto;
        this.stock = stock;
        this.contenido = contenido;
        this.categoria = categoria;
        this.presentacion = presentacion;
        this.contenidoAzucar = contenidoAzucar;
        this.contenidoSal = contenidoSal;
        this.contenidoGrasa = contenidoGrasa;
        this.descripcion = descripcion;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.photo=photo;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getContenidoAzucar() {
        return contenidoAzucar;
    }

    public void setContenidoAzucar(String contenidoAzucar) {
        this.contenidoAzucar = contenidoAzucar;
    }

    public String getContenidoSal() {
        return contenidoSal;
    }

    public void setContenidoSal(String contenidoSal) {
        this.contenidoSal = contenidoSal;
    }

    public String getContenidoGrasa() {
        return contenidoGrasa;
    }

    public void setContenidoGrasa(String contenidoGrasa) {
        this.contenidoGrasa = contenidoGrasa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(String precioCompra) {
        this.precioCompra = precioCompra;
    }

    public String getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(String precioVenta) {
        this.precioVenta = precioVenta;
    }
}
