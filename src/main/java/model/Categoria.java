package model;

public class Categoria {
    private int id;
    private String nombre;

    // Constructores
    public Categoria() {}

    public Categoria(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria nuevo= (Categoria) o;
        return this.getId() == nuevo.getId();
    }
}
