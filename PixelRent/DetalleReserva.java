public class DetalleReserva {

    private final EquipoAudiovisual equipo;
    private final int horasUso;

    public DetalleReserva(EquipoAudiovisual equipo, int horasUso) {
        this.equipo = equipo;
        this.horasUso = horasUso;
    }

    /** Delega el cálculo en el equipo según las horas de uso. */
    public double calcularCosto() {
        return equipo.calcularCosto(horasUso);
    }

    public EquipoAudiovisual getEquipo() { return equipo; }
    public int getHorasUso() { return horasUso; }
}
