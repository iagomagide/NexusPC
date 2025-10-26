package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int id;
    private LocalDateTime fechaPedido;
    private LocalDate fechaEntrega;
    private double pvpTotal;
    private String direccion;
    private String clienteNombre;
    private List<LineaPedido> lineas;

    public Pedido(int id, LocalDate fechaEntrega, String direccion, String clienteNombre) {
        this.id = id;
        this.fechaEntrega = fechaEntrega;
        this.direccion = direccion;
        this.clienteNombre = clienteNombre;
        this.pvpTotal = 0;
        this.lineas = new ArrayList<>();
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDateTime getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDateTime fechaPedido) { this.fechaPedido = fechaPedido; }

    public LocalDate getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDate fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public double getPvp() { return pvpTotal; }
    public void setPvp(double pvpTotal) { this.pvpTotal = pvpTotal; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public List<LineaPedido> getLineas() { return lineas; }
    public void setLineas(List<LineaPedido> lineas) {
        this.lineas = lineas != null ? lineas : new ArrayList<>();
    }

    // Métodos utilitarios para manejar líneas
    public void addLinea(LineaPedido linea) {
        if (linea != null && linea.esValida()) {
            lineas.add(linea);
            calcularTotal();
        }
    }

    public void removeLinea(LineaPedido linea) {
        if (linea != null) {
            lineas.remove(linea);
            calcularTotal();
        }
    }

    public void clearLineas() {
        lineas.clear();
        pvpTotal = 0.0;
    }

    // Método para calcular el total del pedido
    public void calcularTotal() {
        for (LineaPedido l: this.lineas){
            this.pvpTotal += l.getSubtotal();
        }
    }

    // Método para verificar si el pedido tiene líneas
    public boolean tieneLineas() {
        return !lineas.isEmpty();
    }

    // Método para obtener la cantidad total de productos en el pedido
    public int getTotalProductos() {
        int acum = 0;
        for (LineaPedido l: this.lineas){
            acum+=l.getCantidad();
        }
        return acum;
    }

    // Método para verificar si todas las líneas tienen stock suficiente
    public boolean tieneStockSuficiente() {
        boolean stock = true;
        for (LineaPedido l: this.lineas){
            stock = stock && l.tieneStockSuficiente();
        }
        return stock;
    }

    // Método para verificar si el pedido es válido
    public boolean esValido() {
        return fechaEntrega != null &&
                direccion != null && !direccion.trim().isEmpty() &&
                clienteNombre != null && !clienteNombre.trim().isEmpty() &&
                tieneLineas() &&
                tieneStockSuficiente();
    }

    @Override
    public String toString() {
        return String.format("Pedido #%d - %s - Total: %.2f€",
                id, clienteNombre, pvpTotal);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return id == pedido.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
