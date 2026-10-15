public class Camara extends EquipoAudiovisual {

    private String resolucionMaxima;
    private boolean tieneEstabilizador;

    public Camara(String codigo, String marca, String modelo, CondicionEquipo condicion,
                  String resolucionMaxima, boolean tieneEstabilizador) {
        super(codigo, marca, modelo, condicion);
        this.resolucionMaxima = resolucionMaxima;
        this.tieneEstabilizador = tieneEstabilizador;
    }

    // $12.000 por hora, mas $15.000 si tiene estabilizador
    public double calcularCosto(int horas) {
        double costo = horas * 12000;
        if (tieneEstabilizador) {
            costo = costo + 15000;
        }
        return costo;
    }

    public String getResolucionMaxima() {
        return resolucionMaxima;
    }

    public boolean getTieneEstabilizador() {
        return tieneEstabilizador;
    }

    public String toString() {
        return "Camara: " + super.toString();
    }
}
