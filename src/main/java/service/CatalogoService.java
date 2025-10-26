package service;

import criteria.FabricanteCriteria;
import criteria.ProductoCriteria;
import model.Categoria;
import model.Fabricante;
import model.Producto;

import java.util.List;
import java.util.Optional;

public interface CatalogoService {

    // CATEGORIAS
    List<Categoria> recuperarTodasLasCategorias();
    // FABRICANTES
    List<Fabricante> recuperarTodosLosFabricantes();
    Optional<Fabricante> buscarFabricantePorId(int id);
    List<Fabricante> buscarFabricantesPorCriteria(FabricanteCriteria criteria);
    //PRODUCTOS
    List<Producto> buscarProductosPorCriteria(ProductoCriteria criteria);
    Optional<Producto> buscarProductoPorId(int id);
    boolean crearProducto(Producto producto);
    boolean modificarProducto(Producto producto);
    boolean borrarProducto(int id);
}
