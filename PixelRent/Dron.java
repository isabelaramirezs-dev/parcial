public class Dron extends EquipoAudiovisual {

    private int autonomiaMinutos;
    private boolean tieneBateriaAdicional;

    public Dron(String codigo, String marca, String modelo, CondicionEquipo condicion,
                int autonomiaMinutos, boolean tieneBateriaAdicional) {
        super(codigo, marca, modelo, condicion);
        this.autonomiaMinutos = autonomiaMinutos;
        this.tieneBateriaAdicional = tieneBateriaAdicional;
    }

    // $18.000 por hora, mas $20.000 si tiene bateria adicional
    public double calcularCosto(int horas) {
        double costo = horas * 18000;
        if (tieneBateriaAdicional) {
            costo = costo + 20000;
        }
        return costo;
    }

    public int getAutonomiaMinutos() {
        return autonomiaMinutos;
    }

    public boolean getTieneBateriaAdicional() {
        return tieneBateriaAdicional;
    }

    public String toString() {
        return "Dron: " + super.toString();
    }
}
