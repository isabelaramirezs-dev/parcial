public abstract class EquipoAudiovisual {

    private String codigo;
    private String marca;
    private String modelo;
    private CondicionEquipo condicion;

    public EquipoAudiovisual(String codigo, String marca, String modelo, CondicionEquipo condicion) {
        this.codigo = codigo;
        this.marca = marca;
        this.modelo = modelo;
        this.condicion = condicion;
    }

    // Un equipo se puede usar solo si esta DISPONIBLE
    public boolean estaDisponible() {
        return condicion == CondicionEquipo.DISPONIBLE;
    }

    // Cada hijo (Camara, Dron, KitIluminacion) lo calcula a su manera
    public abstract double calcularCosto(int horas);

    public String getCodigo() {
        return codigo;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public CondicionEquipo getCondicion() {
        return condicion;
    }

    public String toString() {
        return codigo + " - " + marca + " " + modelo + " (" + condicion + ")";
    }
}
