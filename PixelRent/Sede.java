import java.util.LinkedList;

public class Sede {

    private String codigo;
    private String nombre;
    private String direccion;
    private LinkedList<EquipoAudiovisual> equipos;

    public Sede(String codigo, String nombre, String direccion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.direccion = direccion;
        this.equipos = new LinkedList<EquipoAudiovisual>();
    }

    // Agrega el equipo a la sede. Retorna false si ya hay uno con el mismo codigo
    public boolean agregarEquipo(EquipoAudiovisual equipo) {
        if (equipo == null) {
            return false;
        }
        if (buscarEquipo(equipo.getCodigo()) != null) {
            return false;
        }
        equipos.add(equipo);
        return true;
    }

    // Busca un equipo de esta sede por su codigo. Si no existe retorna null
    public EquipoAudiovisual buscarEquipo(String codigoEquipo) {
        for (EquipoAudiovisual e : equipos) {
            if (e.getCodigo().equals(codigoEquipo)) {
                return e;
            }
        }
        return null;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public LinkedList<EquipoAudiovisual> getEquipos() {
        return equipos;
    }

    public String toString() {
        return codigo + " - " + nombre + " (" + direccion + ")";
    }
}
