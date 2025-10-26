package criteria;

import java.util.List;

public class ProductoCriteria {
    private int id;
    private String nombreProducto;
    private int idCategoria;
    private int idFabricante;

    // Constructores
    public ProductoCriteria() {
        this.id = 0;
        this.idFabricante = 0;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }


    public int getIdFabricante() { return idFabricante; }
    public void setIdFabricante(int idFabricante) { this.idFabricante = idFabricante; }

    // Métodos de conveniencia
    public boolean tieneId() { return id > 0; }
    public boolean tieneNombreProducto() {
        return nombreProducto != null && !nombreProducto.trim().isEmpty();
    }
    public boolean tieneIdCategoria() {
        return idCategoria > 0;
    }
    public boolean tieneIdFabricante() { return idFabricante > 0; }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }
}
