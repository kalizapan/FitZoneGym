package logica.dominio;

import java.util.Objects;

/**
 *
 * @author Vero
 */
public class Membresia {
    
    private int idMembresia;
    private String nombreMembresia;
    private String duracion;
    private int estado;

    public Membresia() {
    }

    public Membresia(int idMembresia, String nombre, String duracion, int estado) {
        this.idMembresia = idMembresia;
        this.nombreMembresia = nombreMembresia;
        this.duracion = duracion;
        this.estado = estado;
    }

    public Membresia(String nombreMembresia, String duracion, int estado) {
        this.nombreMembresia = nombreMembresia;
        this.duracion = duracion;
        this.estado = estado;
    }
    
    

    public int getIdMembresia() {
        return idMembresia;
    }

    public void setIdMembresia(int idMembresia) {
        this.idMembresia = idMembresia;
    }

    public String getNombreMembresia() {
        return nombreMembresia;
    }

    public void setNombreMembresia(String nombreMembresia) {
        this.nombreMembresia = nombreMembresia;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Membresia other = (Membresia) obj;
        if (this.idMembresia != other.idMembresia) {
            return false;
        }
        if (!Objects.equals(this.nombreMembresia, other.nombreMembresia)) {
            return false;
        }
        if (!Objects.equals(this.duracion, other.duracion)) {
            return false;
        }
        return Objects.equals(this.estado, other.estado);
    }
    
    
    
}
