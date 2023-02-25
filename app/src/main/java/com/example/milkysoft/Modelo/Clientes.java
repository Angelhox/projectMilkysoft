package com.example.milkysoft.Modelo;

public class Clientes {
    String id;
    String codigoCliente;
String nombreCliente;
String apellidoCliente;
String telefonoCliente;
String celularCliente;
String correoCliente;
String photoCliente;


//Datos para facturacion
String cedulaFacturacion;
String rucFacturacion;
String direccionFacturacion;
String telefonoFacturacion;

//Datos entregas
String ubicacionEntrega;
        String latitud;
                String longitud;

    public Clientes() {
    }

    public Clientes(String correoCliente) {
        this.correoCliente = correoCliente;
    }

    public Clientes
            (String codigoCliente, String nombreCliente,
             String apellidoCliente, String telefonoCliente,
             String celularCliente, String correoCliente,
             String photoCliente, String cedulaFacturacion,
             String rucFacturacion, String direccionFacturacion,
             String telefonoFacturacion, String ubicacionEntrega,
             String latitud, String longitud) {
        this.codigoCliente = codigoCliente;
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.telefonoCliente = telefonoCliente;
        this.celularCliente = celularCliente;
        this.correoCliente = correoCliente;
        this.photoCliente = photoCliente;
        this.cedulaFacturacion = cedulaFacturacion;
        this.rucFacturacion = rucFacturacion;
        this.direccionFacturacion = direccionFacturacion;
        this.telefonoFacturacion = telefonoFacturacion;
        this.ubicacionEntrega = ubicacionEntrega;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Clientes(String id, String codigoCliente, String nombreCliente, String apellidoCliente, String telefonoCliente, String celularCliente, String correoCliente, String photoCliente, String cedulaFacturacion, String rucFacturacion, String direccionFacturacion, String telefonoFacturacion, String ubicacionEntrega, String latitud, String longitud) {
        this.id = id;
        this.codigoCliente = codigoCliente;
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.telefonoCliente = telefonoCliente;
        this.celularCliente = celularCliente;
        this.correoCliente = correoCliente;
        this.photoCliente = photoCliente;
        this.cedulaFacturacion = cedulaFacturacion;
        this.rucFacturacion = rucFacturacion;
        this.direccionFacturacion = direccionFacturacion;
        this.telefonoFacturacion = telefonoFacturacion;
        this.ubicacionEntrega = ubicacionEntrega;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Clientes(String codigoCliente, String nombreCliente, String apellidoCliente, String telefonoCliente, String celularCliente, String correoCliente) {
        this.codigoCliente = codigoCliente;
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.telefonoCliente = telefonoCliente;
        this.celularCliente = celularCliente;
        this.correoCliente = correoCliente;
    }

    public Clientes(String id, String codigoCliente, String nombreCliente, String apellidoCliente, String telefonoCliente, String celularCliente, String correoCliente, String photoCliente) {
        this.id = id;
        this.codigoCliente = codigoCliente;
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.telefonoCliente = telefonoCliente;
        this.celularCliente = celularCliente;
        this.correoCliente = correoCliente;
        this.photoCliente = photoCliente;
    }

    public Clientes(String id, String codigoCliente, String nombreCliente, String apellidoCliente, String telefonoCliente, String celularCliente, String correoCliente, String photoCliente, String cedulaFacturacion, String rucFacturacion, String direccionFacturacion, String telefonoFacturacion) {
        this.id = id;
        this.codigoCliente = codigoCliente;
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.telefonoCliente = telefonoCliente;
        this.celularCliente = celularCliente;
        this.correoCliente = correoCliente;
        this.photoCliente = photoCliente;
        this.cedulaFacturacion = cedulaFacturacion;
        this.rucFacturacion = rucFacturacion;
        this.direccionFacturacion = direccionFacturacion;
        this.telefonoFacturacion = telefonoFacturacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getApellidoCliente() {
        return apellidoCliente;
    }

    public void setApellidoCliente(String apellidoCliente) {
        this.apellidoCliente = apellidoCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public String getCelularCliente() {
        return celularCliente;
    }

    public void setCelularCliente(String celularCliente) {
        this.celularCliente = celularCliente;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public void setCorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
    }

    public String getPhotoCliente() {
        return photoCliente;
    }

    public void setPhotoCliente(String photoCliente) {
        this.photoCliente = photoCliente;
    }

    public String getCedulaFacturacion() {
        return cedulaFacturacion;
    }

    public void setCedulaFacturacion(String cedulaFacturacion) {
        this.cedulaFacturacion = cedulaFacturacion;
    }

    public String getRucFacturacion() {
        return rucFacturacion;
    }

    public void setRucFacturacion(String rucFacturacion) {
        this.rucFacturacion = rucFacturacion;
    }

    public String getDireccionFacturacion() {
        return direccionFacturacion;
    }

    public void setDireccionFacturacion(String direccionFacturacion) {
        this.direccionFacturacion = direccionFacturacion;
    }

    public String getTelefonoFacturacion() {
        return telefonoFacturacion;
    }

    public void setTelefonoFacturacion(String telefonoFacturacion) {
        this.telefonoFacturacion = telefonoFacturacion;
    }

    public String getUbicacionEntrega() {
        return ubicacionEntrega;
    }

    public void setUbicacionEntrega(String ubicacionEntrega) {
        this.ubicacionEntrega = ubicacionEntrega;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
