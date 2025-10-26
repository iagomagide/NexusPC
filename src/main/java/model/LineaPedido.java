package model;

public class LineaPedido {
    private int idLinea;
    private Producto producto;
    private int cantidad;
    private double pvpLinea;

    public LineaPedido(int idLinea, Producto producto, int cantidad, double pvpLinea) {
        this.idLinea = -idLinea;
        this.producto = producto;
        this.cantidad = cantidad;
        this.pvpLinea = pvpLinea;
    }

    // Getters y Setters
    public int getIdLinea() { return idLinea; }
    public void setIdLinea(int idLinea) { this.idLinea = idLinea; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPvpLinea() { return pvpLinea; }
    public void setPvpLinea(double pvpLinea) { this.pvpLinea = pvpLinea; }

    // Método para calcular el subtotal de la línea
    public double getSubtotal() {
        return pvpLinea * cantidad;
    }

    // Método para verificar si la línea es válida
    public boolean esValida() {
        return producto != null && cantidad > 0 && pvpLinea >= 0;
    }

    // Método para verificar stock suficiente
    public boolean tieneStockSuficiente() {
        return producto != null && producto.tieneStockSuficiente(cantidad);
    }

    @Override
    public String toString() {
        return String.format("%s x%d - %.2f€",
                producto != null ? producto.getNombreProducto() : "N/A",
                cantidad, getSubtotal());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineaPedido nueva = (LineaPedido) o;
        return this.getIdLinea() == nueva.getIdLinea();
    }

}
