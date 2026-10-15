import java.time.LocalDate;
import java.util.LinkedList;

public class Empresa {

    private final LinkedList<Sede> sedes = new LinkedList<>();
    private final LinkedList<Cliente> clientes = new LinkedList<>();
    private final LinkedList<Operador> operadores = new LinkedList<>();
    private final LinkedList<Reserva> reservas = new LinkedList<>();

    // ---------------------------------------------------------------- registros

    public boolean registrarSede(Sede sede) {
        if (sede == null || buscarSede(sede.getCodigo()) != null) {
            return false;
        }
        sedes.add(sede);
        return true;
    }

    /** El código del equipo debe ser único en toda la empresa (todas las sedes). */
    public boolean registrarEquipo(String codSede, EquipoAudiovisual equipo) {
        if (equipo == null) {
            return false;
        }
        Sede sede = buscarSede(codSede);
        if (sede == null || buscarEquipo(equipo.getCodigo()) != null) {
            return false;
        }
        return sede.agregarEquipo(equipo);
    }

    public boolean registrarCliente(Cliente cliente) {
        if (cliente == null || buscarCliente(cliente.getIdentificacion()) != null) {
            return false;
        }
        clientes.add(cliente);
        return true;
    }

    public boolean registrarOperador(Operador operador) {
        if (operador == null || buscarOperador(operador.getIdentificacion()) != null) {
            return false;
        }
        operadores.add(operador);
        return true;
    }

    /** El código debe ser único y el cliente debe estar registrado. */
    public boolean registrarReserva(Reserva reserva) {
        if (reserva == null || buscarReserva(reserva.getCodigo()) != null) {
            return false;
        }
        if (reserva.getCliente() == null || buscarCliente(reserva.getCliente().getIdentificacion()) == null) {
            return false;
        }
        if (reserva.getFecha() == null || reserva.getTurno() == null) {
            return false;
        }
        reservas.add(reserva);
        return true;
    }

    // --------------------------------------------------------------- operaciones

    public boolean agregarEquipoAReserva(String codReserva, String codEquipo, int horas) {
        Reserva reserva = buscarReserva(codReserva);
        EquipoAudiovisual equipo = buscarEquipo(codEquipo);
        if (reserva == null || equipo == null) {
            return false;
        }
        return reserva.agregarEquipo(equipo, horas);
    }

    public boolean asignarOperador(String codReserva, String idOperador) {
        Reserva reserva = buscarReserva(codReserva);
        Operador operador = buscarOperador(idOperador);
        if (reserva == null || operador == null) {
            return false;
        }
        return reserva.asignarOperador(operador);
    }

    /**
     * ningún equipo puede estar en otra
     * reserva CONFIRMADA con la misma fecha y turno. 
     */
    public boolean cambiarEstadoReserva(String codReserva, EstadoReserva nuevoEstado) {
        Reserva reserva = buscarReserva(codReserva);
        if (reserva == null || nuevoEstado == null) {
            return false;
        }
        if (nuevoEstado == EstadoReserva.CONFIRMADA && hayConflictoDeEquipos(reserva)) {
            return false;
        }
        return reserva.cambiarEstado(nuevoEstado);
    }

    // ----------------------------------------------------------------- consultas

    /** Equipos en condición disponible y que no estén en una reserva confirmada de esa fecha y turno. */
    public LinkedList<EquipoAudiovisual> consultarEquiposDisponibles(LocalDate fecha, Turno turno) {
        LinkedList<EquipoAudiovisual> disponibles = new LinkedList<>();
        for (Sede sede : sedes) {
            for (EquipoAudiovisual equipo : sede.getEquipos()) {
                if (equipo.estaDisponible() && !estaOcupado(equipo.getCodigo(), fecha, turno, null)) {
                    disponibles.add(equipo);
                }
            }
        }
        return disponibles;
    }

    public LinkedList<Reserva> consultarReservasConfirmadas(String idCliente) {
        LinkedList<Reserva> resultado = new LinkedList<>();
        for (Reserva r : reservas) {
            if (r.getEstado() == EstadoReserva.CONFIRMADA
                    && r.getCliente().getIdentificacion().equals(idCliente)) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    /** Retorna el valor total de la reserva, o -1 si no existe. */
    public double obtenerValorReserva(String codReserva) {
        Reserva reserva = buscarReserva(codReserva);
        if (reserva == null) {
            return -1;
        }
        return reserva.calcularValorTotal();
    }

    // ------------------------------------------------------------------ búsquedas

    public Sede buscarSede(String codigo) {
        for (Sede s : sedes) {
            if (s.getCodigo().equals(codigo)) {
                return s;
            }
        }
        return null;
    }

    public EquipoAudiovisual buscarEquipo(String codigo) {
        for (Sede s : sedes) {
            EquipoAudiovisual e = s.buscarEquipo(codigo);
            if (e != null) {
                return e;
            }
        }
        return null;
    }

    public Cliente buscarCliente(String identificacion) {
        for (Cliente c : clientes) {
            if (c.getIdentificacion().equals(identificacion)) {
                return c;
            }
        }
        return null;
    }

    public Operador buscarOperador(String identificacion) {
        for (Operador o : operadores) {
            if (o.getIdentificacion().equals(identificacion)) {
                return o;
            }
        }
        return null;
    }

    public Reserva buscarReserva(String codigo) {
        for (Reserva r : reservas) {
            if (r.getCodigo().equals(codigo)) {
                return r;
            }
        }
        return null;
    }

    // ------------------------------------------------------------------- privados

    private boolean hayConflictoDeEquipos(Reserva reserva) {
        for (DetalleReserva d : reserva.getDetalles()) {
            if (estaOcupado(d.getEquipo().getCodigo(), reserva.getFecha(), reserva.getTurno(), reserva)) {
                return true;
            }
        }
        return false;
    }

    /** ¿El equipo está en una reserva confirmada (distinta de 'excluir') para esa fecha y turno? */
    private boolean estaOcupado(String codEquipo, LocalDate fecha, Turno turno, Reserva excluir) {
        for (Reserva r : reservas) {
            if (r != excluir
                    && r.getEstado() == EstadoReserva.CONFIRMADA
                    && r.getFecha().equals(fecha)
                    && r.getTurno() == turno
                    && r.contieneEquipo(codEquipo)) {
                return true;
            }
        }
        return false;
    }
}
