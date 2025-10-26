package criteria;

public class FabricanteCriteria {
    private int id;
    private String nombre;

    // Constructores
    public FabricanteCriteria() {
        this.id = 0;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    // Métodos de conveniencia
    public boolean tieneId() { return id > 0; }
    public boolean tieneNombre() {
        return nombre != null && !nombre.trim().isEmpty();
    }
}
