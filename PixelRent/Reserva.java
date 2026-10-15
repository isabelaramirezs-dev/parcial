import java.time.LocalDate;
import java.util.LinkedList;

public class Reserva {

    // codigo, cliente, fecha y turno no cambian despues de crear la reserva
    private String codigo;
    private Cliente cliente;
    private LocalDate fecha;
    private Turno turno;
    private EstadoReserva estado;
    private Operador operador;
    private LinkedList<DetalleReserva> detalles;

    public Reserva(String codigo, Cliente cliente, LocalDate fecha, Turno turno) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.fecha = fecha;
        this.turno = turno;
        this.estado = EstadoReserva.SOLICITADA; // toda reserva nueva inicia solicitada
        this.operador = null;                   // al inicio no tiene operador
        this.detalles = new LinkedList<DetalleReserva>();
    }

    // Agrega un equipo con sus horas. Retorna false si algo no se cumple
    public boolean agregarEquipo(EquipoAudiovisual equipo, int horas) {
        if (equipo == null) {
            return false;
        }
        if (horas <= 0) {
            return false;
        }
        if (estado != EstadoReserva.SOLICITADA) {
            return false; // solo se agregan equipos mientras esta solicitada
        }
        if (!equipo.estaDisponible()) {
            return false; // solo equipos disponibles
        }
        if (contieneEquipo(equipo.getCodigo())) {
            return false; // el equipo no se puede repetir en la reserva
        }
        DetalleReserva detalle = new DetalleReserva(equipo, horas);
        detalles.add(detalle);
        return true;
    }

    // Dice si la reserva ya tiene un equipo con ese codigo
    public boolean contieneEquipo(String codigoEquipo) {
        for (DetalleReserva d : detalles) {
            if (d.getEquipo().getCodigo().equals(codigoEquipo)) {
                return true;
            }
        }
        return false;
    }

    // Dice si la reserva tiene al menos un dron
    public boolean contieneDron() {
        for (DetalleReserva d : detalles) {
            if (d.getEquipo() instanceof Dron) { // instanceof pregunta si el objeto es un Dron
                return true;
            }
        }
        return false;
    }

    // Maximo un operador por reserva
    public boolean asignarOperador(Operador operador) {
        if (operador == null) {
            return false;
        }
        if (this.operador != null) {
            return false; // ya tiene operador
        }
        if (estado == EstadoReserva.CANCELADA || estado == EstadoReserva.FINALIZADA) {
            return false;
        }
        this.operador = operador;
        return true;
    }

    // Cambia el estado solo si se cumplen las reglas de la reserva
    public boolean cambiarEstado(EstadoReserva nuevoEstado) {
        if (nuevoEstado == null) {
            return false;
        }
        if (nuevoEstado == estado) {
            return false; // ya esta en ese estado
        }
        if (nuevoEstado == EstadoReserva.SOLICITADA) {
            return false; // nunca se vuelve a solicitada
        }
        if (estado == EstadoReserva.CANCELADA || estado == EstadoReserva.FINALIZADA) {
            return false; // cancelada o finalizada ya no cambian
        }
        if (nuevoEstado == EstadoReserva.CONFIRMADA) {
            if (detalles.size() == 0) {
                return false; // debe tener al menos un equipo
            }
            if (contieneDron() && operador == null) {
                return false; // con dron necesita operador
            }
        }
        estado = nuevoEstado;
        return true;
    }

    // Suma el costo de todos los equipos de la reserva
    public double calcularValorTotal() {
        double total = 0;
        for (DetalleReserva d : detalles) {
            total = total + d.calcularCosto();
        }
        return total;
    }

    public String getCodigo() {
        return codigo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Turno getTurno() {
        return turno;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public Operador getOperador() {
        return operador;
    }

    public LinkedList<DetalleReserva> getDetalles() {
        return detalles;
    }

    public String toString() {
        return codigo + " | " + fecha + " | " + turno + " | " + estado + " | cliente: " + cliente.getNombreCompleto();
    }
}
