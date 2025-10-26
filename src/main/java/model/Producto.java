package model;

import java.util.ArrayList;
import java.util.List;

public class Producto {
    private int id;
    private String nombreProducto;
    private String descripcion;
    private double pvp;
    private int stock;
    private Fabricante fabricante;
    private Categoria categoria;

    // Constructores

    public Producto(){
        this.id = -1;
        this.nombreProducto = "";
        this.descripcion = "";
        this.pvp = 0;
        this.stock = 0;
        this.fabricante = null;
        this.categoria = null;
    }

    public Producto(int id, String nombreProducto, String descripcion,
                    double pvp, int stock, Fabricante fabricante, Categoria cat) {
        this.id = id;
        this.nombreProducto = nombreProducto;
        this.descripcion = descripcion;
        this.pvp = pvp;
        this.stock = stock;
        this.fabricante = fabricante;
        this.categoria = cat;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPvp() { return pvp; }
    public void setPvp(double pvp) { this.pvp = pvp; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public Fabricante getFabricante() { return fabricante; }
    public void setFabricante(Fabricante fabricante) { this.fabricante = fabricante; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }



    public boolean tieneCategoria(Categoria categoria) {
        return this.categoria==null;
    }

    // Método para verificar si hay stock suficiente
    public boolean tieneStockSuficiente(int cantidad) {
        return stock >= cantidad;
    }

    // Método para descontar stock
    public void descontarStock(int cantidad) {
        if (tieneStockSuficiente(cantidad)) {
            stock -= cantidad;
        }
    }

    @Override
    public String toString() {
        return String.format("%s (Stock: %d, PVPR: %.2f€)", nombreProducto, stock, pvp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return this.getId() == producto.getId();
    }

    @Override
    public int hashCode() {
        return id;
    }
}
