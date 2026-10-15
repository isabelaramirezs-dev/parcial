import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

    // Un solo Scanner para todo el programa
    static Scanner teclado = new Scanner(System.in);
    static Empresa pixelRent = new Empresa();

    public static void main(String[] args) {
        int opcion = -1;

        while (opcion != 0) {
            mostrarMenu();
            opcion = leerEntero("Elija una opcion: ");
            System.out.println();

            if (opcion == 1) {
                registrarSede();
            } else if (opcion == 2) {
                registrarEquipo();
            } else if (opcion == 3) {
                registrarCliente();
            } else if (opcion == 4) {
                registrarOperador();
            } else if (opcion == 5) {
                registrarReserva();
            } else if (opcion == 6) {
                agregarEquipoAReserva();
            } else if (opcion == 7) {
                asignarOperador();
            } else if (opcion == 8) {
                cambiarEstado();
            } else if (opcion == 9) {
                consultarDisponibles();
            } else if (opcion == 10) {
                consultarConfirmadas();
            } else if (opcion == 11) {
                consultarValor();
            } else if (opcion == 0) {
                System.out.println("Hasta luego.");
            } else {
                System.out.println("Opcion no valida. Intente de nuevo.");
            }
        }
    }

    static void mostrarMenu() {
        System.out.println();
        System.out.println("===== PIXELRENT =====");
        System.out.println(" 1. Registrar sede");
        System.out.println(" 2. Registrar equipo");
        System.out.println(" 3. Registrar cliente");
        System.out.println(" 4. Registrar operador");
        System.out.println(" 5. Registrar reserva");
        System.out.println(" 6. Agregar equipo a una reserva");
        System.out.println(" 7. Asignar operador a una reserva");
        System.out.println(" 8. Cambiar estado de una reserva");
        System.out.println(" 9. Consultar equipos disponibles (fecha y turno)");
        System.out.println("10. Consultar reservas confirmadas de un cliente");
        System.out.println("11. Consultar valor total de una reserva");
        System.out.println(" 0. Salir");
    }

    // ---------------------------------------------------------- opciones del menu

    static void registrarSede() {
        String codigo = leerTexto("Codigo de la sede: ");
        String nombre = leerTexto("Nombre: ");
        String direccion = leerTexto("Direccion: ");

        Sede sede = new Sede(codigo, nombre, direccion);
        if (pixelRent.registrarSede(sede)) {
            System.out.println("Sede registrada correctamente.");
        } else {
            System.out.println("No se pudo registrar: ya existe una sede con ese codigo.");
        }
    }

    static void registrarEquipo() {
        String codSede = leerTexto("Codigo de la sede donde queda el equipo: ");
        if (pixelRent.buscarSede(codSede) == null) {
            System.out.println("No existe una sede con ese codigo. Registrela primero (opcion 1).");
            return;
        }

        System.out.println("Tipo de equipo: 1. Camara  2. Dron  3. Kit de iluminacion");
        int tipo = leerEntero("Tipo: ");
        if (tipo < 1 || tipo > 3) {
            System.out.println("Tipo no valido.");
            return;
        }

        String codigo = leerTexto("Codigo del equipo: ");
        String marca = leerTexto("Marca: ");
        String modelo = leerTexto("Modelo: ");

        System.out.println("Condicion: 1. Disponible  2. En mantenimiento  3. Fuera de servicio");
        int c = leerEntero("Condicion: ");
        CondicionEquipo condicion;
        if (c == 2) {
            condicion = CondicionEquipo.EN_MANTENIMIENTO;
        } else if (c == 3) {
            condicion = CondicionEquipo.FUERA_DE_SERVICIO;
        } else {
            condicion = CondicionEquipo.DISPONIBLE;
        }

        EquipoAudiovisual equipo;
        if (tipo == 1) {
            String resolucion = leerTexto("Resolucion maxima (ej. 4K): ");
            boolean estabilizador = leerSiNo("Tiene estabilizador? (s/n): ");
            equipo = new Camara(codigo, marca, modelo, condicion, resolucion, estabilizador);
        } else if (tipo == 2) {
            int autonomia = leerEntero("Autonomia de vuelo en minutos: ");
            boolean bateria = leerSiNo("Tiene bateria adicional? (s/n): ");
            equipo = new Dron(codigo, marca, modelo, condicion, autonomia, bateria);
        } else {
            int luces = leerEntero("Cantidad de luces: ");
            boolean control = leerSiNo("Tiene control inalambrico? (s/n): ");
            equipo = new KitIluminacion(codigo, marca, modelo, condicion, luces, control);
        }

        if (pixelRent.registrarEquipo(codSede, equipo)) {
            System.out.println("Equipo registrado correctamente.");
        } else {
            System.out.println("No se pudo registrar: ya existe un equipo con ese codigo.");
        }
    }

    static void registrarCliente() {
        String nombre = leerTexto("Nombre completo: ");
        String identificacion = leerTexto("Numero de identificacion: ");
        String telefono = leerTexto("Telefono: ");
        String correo = leerTexto("Correo electronico: ");

        Cliente cliente = new Cliente(nombre, identificacion, telefono, correo);
        if (pixelRent.registrarCliente(cliente)) {
            System.out.println("Cliente registrado correctamente.");
        } else {
            System.out.println("No se pudo registrar: ya existe un cliente con esa identificacion.");
        }
    }

    static void registrarOperador() {
        String identificacion = leerTexto("Numero de identificacion: ");
        String nombre = leerTexto("Nombre completo: ");
        String certificacion = leerTexto("Certificacion: ");
        int anios = leerEntero("Anios de experiencia: ");

        Operador operador = new Operador(identificacion, nombre, certificacion, anios);
        if (pixelRent.registrarOperador(operador)) {
            System.out.println("Operador registrado correctamente.");
        } else {
            System.out.println("No se pudo registrar: ya existe un operador con esa identificacion.");
        }
    }

    static void registrarReserva() {
        String codigo = leerTexto("Codigo de la reserva: ");
        String idCliente = leerTexto("Identificacion del cliente: ");

        Cliente cliente = pixelRent.buscarCliente(idCliente);
        if (cliente == null) {
            System.out.println("No existe un cliente con esa identificacion. Registrelo primero (opcion 3).");
            return;
        }

        LocalDate fecha = leerFecha("Fecha de la reserva (aaaa-mm-dd): ");
        Turno turno = leerTurno();

        Reserva reserva = new Reserva(codigo, cliente, fecha, turno);
        if (pixelRent.registrarReserva(reserva)) {
            System.out.println("Reserva registrada. Estado inicial: " + reserva.getEstado());
        } else {
            System.out.println("No se pudo registrar: ya existe una reserva con ese codigo.");
        }
    }

    static void agregarEquipoAReserva() {
        String codReserva = leerTexto("Codigo de la reserva: ");
        String codEquipo = leerTexto("Codigo del equipo: ");
        int horas = leerEntero("Horas de uso: ");

        if (pixelRent.agregarEquipoAReserva(codReserva, codEquipo, horas)) {
            System.out.println("Equipo agregado a la reserva.");
        } else {
            System.out.println("No se pudo agregar. Revise que:");
            System.out.println(" - la reserva y el equipo existan");
            System.out.println(" - las horas sean mayores que 0");
            System.out.println(" - el equipo este disponible y no este ya en la reserva");
            System.out.println(" - la reserva siga en estado SOLICITADA");
        }
    }

    static void asignarOperador() {
        String codReserva = leerTexto("Codigo de la reserva: ");
        String idOperador = leerTexto("Identificacion del operador: ");

        if (pixelRent.asignarOperador(codReserva, idOperador)) {
            System.out.println("Operador asignado a la reserva.");
        } else {
            System.out.println("No se pudo asignar. Revise que la reserva y el operador existan,");
            System.out.println("que la reserva no tenga ya un operador y que no este cancelada o finalizada.");
        }
    }

    static void cambiarEstado() {
        String codReserva = leerTexto("Codigo de la reserva: ");
        System.out.println("Nuevo estado: 1. Solicitada  2. Confirmada  3. Cancelada  4. Finalizada");
        int e = leerEntero("Estado: ");

        EstadoReserva nuevo;
        if (e == 1) {
            nuevo = EstadoReserva.SOLICITADA;
        } else if (e == 2) {
            nuevo = EstadoReserva.CONFIRMADA;
        } else if (e == 3) {
            nuevo = EstadoReserva.CANCELADA;
        } else if (e == 4) {
            nuevo = EstadoReserva.FINALIZADA;
        } else {
            System.out.println("Estado no valido.");
            return;
        }

        if (pixelRent.cambiarEstadoReserva(codReserva, nuevo)) {
            System.out.println("Estado cambiado a " + nuevo + ".");
        } else {
            System.out.println("No se pudo cambiar el estado. Recuerde que para confirmar:");
            System.out.println(" - la reserva debe tener al menos un equipo");
            System.out.println(" - si tiene un dron, debe tener operador");
            System.out.println(" - ningun equipo puede estar en otra reserva confirmada (misma fecha y turno)");
            System.out.println(" - una reserva cancelada o finalizada no cambia de estado");
        }
    }

    static void consultarDisponibles() {
        LocalDate fecha = leerFecha("Fecha (aaaa-mm-dd): ");
        Turno turno = leerTurno();

        LinkedList<EquipoAudiovisual> lista = pixelRent.consultarEquiposDisponibles(fecha, turno);
        if (lista.size() == 0) {
            System.out.println("No hay equipos disponibles para esa fecha y turno.");
        } else {
            System.out.println("Equipos disponibles:");
            for (EquipoAudiovisual e : lista) {
                System.out.println(" - " + e);
            }
        }
    }

    static void consultarConfirmadas() {
        String idCliente = leerTexto("Identificacion del cliente: ");

        LinkedList<Reserva> lista = pixelRent.consultarReservasConfirmadas(idCliente);
        if (lista.size() == 0) {
            System.out.println("Ese cliente no tiene reservas confirmadas.");
        } else {
            System.out.println("Reservas confirmadas:");
            for (Reserva r : lista) {
                System.out.println(" - " + r);
            }
        }
    }

    static void consultarValor() {
        String codReserva = leerTexto("Codigo de la reserva: ");

        double valor = pixelRent.obtenerValorReserva(codReserva);
        if (valor < 0) {
            System.out.println("La reserva no existe.");
        } else {
            System.out.println("Valor total de la reserva " + codReserva + ": $" + (long) valor);
        }
    }

    // ------------------------------------------------- metodos para leer datos
    // Estos metodos los creamos nosotros para no repetir codigo: piden el dato
    // y lo vuelven a pedir mientras el usuario lo escriba mal.

    // Pide un texto y lo repite mientras este vacio
    static String leerTexto(String mensaje) {
        String texto = "";
        while (texto.trim().equals("")) {
            System.out.print(mensaje);
            texto = teclado.nextLine();
            if (texto.trim().equals("")) {
                System.out.println("  Este dato no puede estar vacio.");
            }
        }
        return texto.trim();
    }

    // Pide un numero entero y lo repite si no es un numero
    static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = teclado.nextLine();
            try {
                return Integer.parseInt(texto.trim());
            } catch (NumberFormatException e) {
                System.out.println("  Escriba un numero entero valido.");
            }
        }
    }

    // Pide una fecha con formato aaaa-mm-dd y la repite si esta mal escrita
    static LocalDate leerFecha(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = teclado.nextLine();
            try {
                return LocalDate.parse(texto.trim());
            } catch (Exception e) {
                System.out.println("  Fecha no valida. Ejemplo: 2026-11-20");
            }
        }
    }

    // Muestra los turnos y pide el numero hasta que sea 1, 2 o 3
    static Turno leerTurno() {
        System.out.println("Turno: 1. Manana  2. Tarde  3. Noche");
        while (true) {
            int t = leerEntero("Turno: ");
            if (t == 1) {
                return Turno.MANANA;
            } else if (t == 2) {
                return Turno.TARDE;
            } else if (t == 3) {
                return Turno.NOCHE;
            }
            System.out.println("  Escriba 1, 2 o 3.");
        }
    }

    // Pide s o n y devuelve true si es s, false si es n
    static boolean leerSiNo(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = teclado.nextLine().trim().toLowerCase();
            if (texto.equals("s")) {
                return true;
            } else if (texto.equals("n")) {
                return false;
            }
            System.out.println("  Responda con s o n.");
        }
    }
}
