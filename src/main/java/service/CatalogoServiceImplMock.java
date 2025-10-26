package service;

import criteria.FabricanteCriteria;
import criteria.ProductoCriteria;
import model.Categoria;
import model.Fabricante;
import model.Producto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CatalogoServiceImplMock implements CatalogoService {
    private List<Producto> productos;
    private List<Fabricante> fabricantes;
    private List<Categoria> categorias;
    private int nextProductoId = 5; // Empezamos desde 5 porque ya tenemos 4 productos

    public CatalogoServiceImplMock() {
        inicializarDatos();
    }

    private void inicializarDatos() {
        // Inicializar categorías
        categorias = new ArrayList<>();
        categorias.add(new Categoria(1, "Portátiles"));
        categorias.add(new Categoria(2, "Smartphones"));

        // Inicializar fabricantes
        fabricantes = new ArrayList<>();
        fabricantes.add(new Fabricante(1, "Apple"));
        fabricantes.add(new Fabricante(2, "Samsung"));

        // Inicializar productos
        productos = new ArrayList<>();
        productos.add(new Producto(1, "MacBook Pro 14\"", "Portátil Apple con chip M1 Pro", 1999.99, 10, fabricantes.get(0), categorias.get(0)));
        productos.add(new Producto(2, "iPhone 13", "Smartphone Apple con pantalla OLED", 799.99, 50, fabricantes.get(0), categorias.get(1)));
        productos.add(new Producto(3, "Galaxy S21", "Smartphone Samsung con cámara avanzada", 699.99, 40,fabricantes.get(1), categorias.get(1)));
        productos.add(new Producto(4, "Dell XPS 13", "Portátil Dell ultraligero", 999.99, 15,fabricantes.get(1), categorias.get(0)));
    }

    // ========== CATEGORÍAS ==========
    @Override
    public List<Categoria> recuperarTodasLasCategorias() {
        return new ArrayList<>(categorias);
    }

    // ========== FABRICANTES ==========
    @Override
    public List<Fabricante> recuperarTodosLosFabricantes() {
        return new ArrayList<>(fabricantes);
    }

    @Override
    public Optional<Fabricante> buscarFabricantePorId(int id) {
        return fabricantes.stream()
                .filter(f -> f.getId() == id)
                .findFirst();
    }

    @Override
    public List<Fabricante> buscarFabricantesPorCriteria(FabricanteCriteria criteria) {
        List<Fabricante> resultados = new ArrayList<>();

        for (Fabricante fabricante : fabricantes) {
            boolean coincide = true;

            // Filtro por ID
            if (criteria.tieneId() && fabricante.getId() != criteria.getId()) {
                coincide = false;
            }

            // Filtro por nombre (búsqueda parcial case-insensitive)
            if (criteria.tieneNombre() &&
                    !fabricante.getNombre().toLowerCase().contains(criteria.getNombre().toLowerCase())) {
                coincide = false;
            }

            if (coincide) {
                resultados.add(fabricante);
            }
        }

        return resultados;
    }

    // ========== PRODUCTOS ==========
    @Override
    public List<Producto> buscarProductosPorCriteria(ProductoCriteria criteria) {
        List<Producto> resultados = new ArrayList<>();

        for (Producto producto : productos) {
            boolean coincide = true;

            // Filtro por ID
            if (criteria.tieneId() && producto.getId() != criteria.getId()) {
                coincide = false;
            }

            // Filtro por nombre (búsqueda parcial case-insensitive)
            if (criteria.tieneNombreProducto() &&
                    !producto.getNombreProducto().toLowerCase().contains(criteria.getNombreProducto().toLowerCase())) {
                coincide = false;
            }

            // Filtro por categoría (comparando IDs)
            if (criteria.tieneIdCategoria() && producto.getCategoria().getId() != criteria.getIdCategoria()) {
                coincide = false;
            }

            // Filtro por fabricante (comparando IDs)
            if (criteria.tieneIdFabricante() && producto.getFabricante().getId() != criteria.getIdFabricante()) {
                coincide = false;
            }

            if (coincide) {
                resultados.add(producto);
            }
        }

        return resultados;
    }

    @Override
    public Optional<Producto> buscarProductoPorId(int id) {
        return productos.stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    @Override
    public boolean crearProducto(Producto producto) {
        try {
            // Validaciones básicas
            if (producto.getNombreProducto() == null || producto.getNombreProducto().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del producto es obligatorio");
            }

            if (producto.getPvp() <= 0) {
                throw new IllegalArgumentException("El PVP debe ser mayor a 0");
            }

            if (producto.getStock() < 0) {
                throw new IllegalArgumentException("El stock no puede ser negativo");
            }

            // Asignar ID automático
            producto.setId(nextProductoId++);

            // Verificar que la categoría y fabricante existen
            boolean categoriaExiste = categorias.stream()
                    .anyMatch(c -> c.getId() == producto.getCategoria().getId());
            boolean fabricanteExiste = fabricantes.stream()
                    .anyMatch(f -> f.getId() == producto.getFabricante().getId());

            if (!categoriaExiste || !fabricanteExiste) {
                throw new IllegalArgumentException("Categoría o fabricante no válidos");
            }

            productos.add(producto);
            return true;

        } catch (Exception e) {
            System.err.println("Error creando producto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean modificarProducto(Producto productoModificado) {
        try {
            // Buscar el producto existente
            Optional<Producto> productoExistenteOpt = buscarProductoPorId(productoModificado.getId());
            if (productoExistenteOpt.isEmpty()) {
                throw new IllegalArgumentException("Producto no encontrado");
            }

            Producto productoExistente = productoExistenteOpt.get();

            // Validaciones
            if (productoModificado.getNombreProducto() == null || productoModificado.getNombreProducto().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del producto es obligatorio");
            }

            if (productoModificado.getPvp() <= 0) {
                throw new IllegalArgumentException("El PVP debe ser mayor a 0");
            }

            if (productoModificado.getStock() < 0) {
                throw new IllegalArgumentException("El stock no puede ser negativo");
            }

            // Verificar que la categoría y fabricante existen
            boolean categoriaExiste = categorias.stream()
                    .anyMatch(c -> c.getId() == productoModificado.getCategoria().getId());
            boolean fabricanteExiste = fabricantes.stream()
                    .anyMatch(f -> f.getId() == productoModificado.getFabricante().getId());

            if (!categoriaExiste || !fabricanteExiste) {
                throw new IllegalArgumentException("Categoría o fabricante no válidos");
            }

            // Actualizar el producto
            productoExistente.setNombreProducto(productoModificado.getNombreProducto());
            productoExistente.setDescripcion(productoModificado.getDescripcion());
            productoExistente.setPvp(productoModificado.getPvp());
            productoExistente.setStock(productoModificado.getStock());
            productoExistente.setCategoria(productoModificado.getCategoria());
            productoExistente.setFabricante(productoModificado.getFabricante());

            return true;

        } catch (Exception e) {
            System.err.println("Error modificando producto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean borrarProducto(int id) {
        try {
            // Buscar el producto
            Optional<Producto> productoOpt = buscarProductoPorId(id);
            if (productoOpt.isEmpty()) {
                throw new IllegalArgumentException("Producto no encontrado");
            }

            // Verificar que no esté en ningún pedido (en un sistema real)
            // Por ahora solo eliminamos directamente

            boolean eliminado = productos.removeIf(p -> p.getId() == id);
            return eliminado;

        } catch (Exception e) {
            System.err.println("Error borrando producto: " + e.getMessage());
            return false;
        }
    }
}