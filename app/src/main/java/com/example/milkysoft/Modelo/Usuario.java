package com.example.milkysoft.Modelo;

public class Usuario {
    String id;
    String codigoUsuario;
    String nombreUsuario;
    String apellidoUsuario;
    String cedulaUsuario;
    String telefonoUsuario;
    String correoUsuario;
    String provinciaUsuario;
    String cantonUsuario;
    String callesUsuario;
    String fechaNacimiento;
    String fechaRegistro;
    String photoUsuario;
    String contraseniaUsuario;
    String cargoUsuario;

    public String getContraseniaUsuario() {
        return contraseniaUsuario;
    }

    public void setContraseniaUsuario(String contraseniaUsuario) {
        this.contraseniaUsuario = contraseniaUsuario;
    }

    public String getCargoUsuario() {
        return cargoUsuario;
    }

    public void setCargoUsuario(String cargoUsuario) {
        this.cargoUsuario = cargoUsuario;
    }

    public Usuario() {
    }

    public Usuario(String id, String codigoUsuario, String nombreUsuario, String apellidoUsuario, String cedulaUsuario, String telefonoUsuario, String correoUsuario, String provinciaUsuario, String cantonUsuario, String callesUsuario, String fechaNacimiento, String fechaRegistro, String photoUsuario) {
        this.id = id;
        this.codigoUsuario = codigoUsuario;
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.cedulaUsuario = cedulaUsuario;
        this.telefonoUsuario = telefonoUsuario;
        this.correoUsuario = correoUsuario;
        this.provinciaUsuario = provinciaUsuario;
        this.cantonUsuario = cantonUsuario;
        this.callesUsuario = callesUsuario;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaRegistro = fechaRegistro;
        this.photoUsuario = photoUsuario;

    }

    public String getPhotoUsuario() {
        return photoUsuario;
    }

    public void setPhotoUsuario(String photoUsuario) {
        this.photoUsuario = photoUsuario;
    }

    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getApellidoUsuario() {
        return apellidoUsuario;
    }

    public void setApellidoUsuario(String apellidoUsuario) {
        this.apellidoUsuario = apellidoUsuario;
    }

    public String getCedulaUsuario() {
        return cedulaUsuario;
    }

    public void setCedulaUsuario(String cedulaUsuario) {
        this.cedulaUsuario = cedulaUsuario;
    }

    public String getTelefonoUsuario() {
        return telefonoUsuario;
    }

    public void setTelefonoUsuario(String telefonoUsuario) {
        this.telefonoUsuario = telefonoUsuario;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public String getProvinciaUsuario() {
        return provinciaUsuario;
    }

    public void setProvinciaUsuario(String provinciaUsuario) {
        this.provinciaUsuario = provinciaUsuario;
    }

    public String getCantonUsuario() {
        return cantonUsuario;
    }

    public void setCantonUsuario(String cantonUsuario) {
        this.cantonUsuario = cantonUsuario;
    }

    public String getCallesUsuario() {
        return callesUsuario;
    }

    public void setCallesUsuario(String callesUsuario) {
        this.callesUsuario = callesUsuario;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Usuario(String id, String codigoUsuario, String nombreUsuario, String apellidoUsuario, String cedulaUsuario, String telefonoUsuario, String correoUsuario, String provinciaUsuario, String cantonUsuario, String callesUsuario, String fechaNacimiento, String fechaRegistro) {
        this.id = id;
        this.codigoUsuario = codigoUsuario;
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.cedulaUsuario = cedulaUsuario;
        this.telefonoUsuario = telefonoUsuario;
        this.correoUsuario = correoUsuario;
        this.provinciaUsuario = provinciaUsuario;
        this.cantonUsuario = cantonUsuario;
        this.callesUsuario = callesUsuario;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaRegistro = fechaRegistro;
    }

    public Usuario(String id,String codigoUsuario, String nombreUsuario, String apellidoUsuario,
                   String cedulaUsuario, String telefonoUsuario, String correoUsuario,
                   String provinciaUsuario, String cantonUsuario, String callesUsuario,
                   String fechaNacimiento,
                   String fechaRegistro,String contraseniaUsuario,String cargoUsuario) {
        this.id=id;
        this.codigoUsuario = codigoUsuario;
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.cedulaUsuario = cedulaUsuario;
        this.telefonoUsuario = telefonoUsuario;
        this.correoUsuario = correoUsuario;
        this.provinciaUsuario = provinciaUsuario;
        this.cantonUsuario = cantonUsuario;
        this.callesUsuario = callesUsuario;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaRegistro = fechaRegistro;
        this.contraseniaUsuario=contraseniaUsuario;
        this.cargoUsuario=cargoUsuario;
    }


}
