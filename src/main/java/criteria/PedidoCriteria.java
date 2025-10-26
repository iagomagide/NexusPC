package criteria;

import java.time.LocalDate;

import java.time.LocalDate;

public class PedidoCriteria {
    private int id;
    private LocalDate fechaCreacion;
    private String nombreCliente;

    // Constructores
    public PedidoCriteria() {
        this.id = 0;
        this.nombreCliente = "";
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDate fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente != null ? nombreCliente.trim() : "";
    }


    // Métodos de conveniencia
    public boolean tieneId() { return id > 0; }
    public boolean tieneFechaCreacion() { return fechaCreacion != null; }
    public boolean tieneNombreCliente() { return nombreCliente != null && !nombreCliente.isEmpty(); }

    public boolean tieneAlgunCriterio() {
        return (tieneId() || tieneFechaCreacion() || tieneNombreCliente());
    }

    // Validación: al menos un criterio debe estar presente
    public boolean esValido() {
        //return tieneAlgunCriterio();
        return true;
    }
}
