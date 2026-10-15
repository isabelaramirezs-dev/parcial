public class KitIluminacion extends EquipoAudiovisual {

    private int cantidadLuces;
    private boolean tieneControlInalambrico;

    public KitIluminacion(String codigo, String marca, String modelo, CondicionEquipo condicion,
                          int cantidadLuces, boolean tieneControlInalambrico) {
        super(codigo, marca, modelo, condicion);
        this.cantidadLuces = cantidadLuces;
        this.tieneControlInalambrico = tieneControlInalambrico;
    }

    // $10.000 por hora, mas $12.000 si tiene control inalambrico
    public double calcularCosto(int horas) {
        double costo = horas * 10000;
        if (tieneControlInalambrico) {
            costo = costo + 12000;
        }
        return costo;
    }

    public int getCantidadLuces() {
        return cantidadLuces;
    }

    public boolean getTieneControlInalambrico() {
        return tieneControlInalambrico;
    }

    public String toString() {
        return "Kit de iluminacion: " + super.toString();
    }
}
