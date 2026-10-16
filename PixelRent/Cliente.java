public class Cliente {

    // Los datos del cliente no se pueden modificar, por eso no hay metodos set
    private String nombreCompleto;
    private String identificacion;
    private String telefono;
    private String correo;

    public Cliente(String nombreCompleto, String identificacion, String telefono, String correo) {
        this.nombreCompleto = nombreCompleto;
        this.identificacion = identificacion;
        this.telefono = telefono;
        this.correo = correo;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public String toString() {
        return nombreCompleto + " (ID " + identificacion + ")";
    }
}
