package service;

import criteria.PedidoCriteria;
import criteria.ProductoCriteria;
import model.LineaPedido;
import model.Pedido;
import model.Producto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PedidoServiceImplMock implements PedidoService {
    private List<Pedido> pedidos;
    private CatalogoService catalogoService;
    private int nextPedidoId = 3; // Empezamos desde 3 porque ya tenemos 2 pedidos
    private int nextLineaId = 5;  // Empezamos desde 5 porque ya tenemos 4 líneas

    public PedidoServiceImplMock(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
        inicializarDatos();
    }

    private void inicializarDatos() {
        pedidos = new ArrayList<>();

        // Obtener productos para las líneas
        List<Producto> productos = catalogoService.buscarProductosPorCriteria(new ProductoCriteria());
        Producto macbook = productos.get(0); // ID 1
        Producto iphone = productos.get(1);  // ID 2
        Producto galaxy = productos.get(2);  // ID 3
        Producto xps = productos.get(3);     // ID 4

        // Pedido 1
        Pedido pedido1 = new Pedido(1, LocalDate.of(2024, 1, 20), "Calle Mayor 123, Madrid", "Juan Pérez García");
        pedido1.setFechaPedido(LocalDateTime.of(2024, 1, 15, 10, 30));

        List<LineaPedido> lineas1 = new ArrayList<>();
        lineas1.add(new LineaPedido(1, macbook, 1, macbook.getPvp()));
        lineas1.add(new LineaPedido(2, iphone, 1, iphone.getPvp()));
        pedido1.setLineas(lineas1);
        pedido1.setPvp(macbook.getPvp() + iphone.getPvp());

        pedidos.add(pedido1);

        // Pedido 2
        Pedido pedido2 = new Pedido(2, LocalDate.of(2024, 1, 22), "Avenida Libertad 45, Barcelona", "María López Sánchez");
        pedido2.setFechaPedido(LocalDateTime.of(2024, 1, 16, 14, 20));

        List<LineaPedido> lineas2 = new ArrayList<>();
        lineas2.add(new LineaPedido(3, galaxy, 2, galaxy.getPvp()));
        lineas2.add(new LineaPedido(4, xps, 1, xps.getPvp()));
        pedido2.setLineas(lineas2);
        pedido2.setPvp((galaxy.getPvp() * 2) + xps.getPvp());

        pedidos.add(pedido2);
    }

    @Override
    public boolean crearPedido(Pedido pedido) {
        try {
            // Validar que el pedido tenga al menos una línea
            if (pedido.getLineas() == null || pedido.getLineas().isEmpty()) {
                throw new IllegalArgumentException("El pedido debe tener al menos una línea");
            }

            // Validar stock antes de crear el pedido
            if (!validarStockSuficiente(pedido)) {
                throw new IllegalStateException("Stock insuficiente para uno o más productos");
            }

            // Asignar IDs automáticos
            pedido.setId(nextPedidoId++);
            pedido.setFechaPedido(LocalDateTime.now());

            // Asignar IDs a las líneas y validar productos
            for (int i = 0; i < pedido.getLineas().size(); i++) {
                LineaPedido linea = pedido.getLineas().get(i);
                linea.setIdLinea(nextLineaId++);

                // Verificar que el producto existe
                Optional<Producto> productoOpt = catalogoService.buscarProductoPorId(linea.getProducto().getId());
                if (productoOpt.isEmpty()) {
                    throw new IllegalArgumentException("Producto no encontrado: ID " + linea.getProducto().getId());
                }

                // Actualizar el producto en la línea con los datos actuales
                linea.setProducto(productoOpt.get());
            }

            // Calcular PVP total
            double pvpTotal = pedido.getLineas().stream()
                    .mapToDouble(linea -> linea.getCantidad() * linea.getPvpLinea())
                    .sum();
            pedido.setPvp(pvpTotal);

            // Actualizar stock de productos
            for (LineaPedido linea : pedido.getLineas()) {
                boolean stockActualizado = catalogoService.modificarProducto(crearProductoConStockActualizado(
                        linea.getProducto(), linea.getCantidad()));
                if (!stockActualizado) {
                    throw new IllegalStateException("Error al actualizar stock del producto: " + linea.getProducto().getNombreProducto());
                }
            }

            pedidos.add(pedido);
            return true;

        } catch (Exception e) {
            System.err.println("Error creando pedido: " + e.getMessage());
            return false;
        }
    }

    private Producto crearProductoConStockActualizado(Producto producto, int cantidadDescontar) {
        Producto productoActualizado = new Producto(
                producto.getId(),
                producto.getNombreProducto(),
                producto.getDescripcion(),
                producto.getPvp(),
                producto.getStock() - cantidadDescontar,
                producto.getFabricante(),
                producto.getCategoria()

        );
        return productoActualizado;
    }

    @Override
    public List<Pedido> buscarPedidosPorCriteria(PedidoCriteria criteria) {
        List<Pedido> resultados = new ArrayList<>();

        for (Pedido pedido : pedidos) {
            boolean coincide = true;

            // Filtro por ID
            if (criteria.tieneId() && pedido.getId() != criteria.getId()) {
                coincide = false;
            }

            // Filtro por nombre del cliente (búsqueda parcial case-insensitive)
            if (criteria.tieneNombreCliente() &&
                    !pedido.getClienteNombre().toLowerCase().contains(criteria.getNombreCliente().toLowerCase())) {
                coincide = false;
            }

            // Filtro por fecha de creación (fecha exacta)
            if (criteria.tieneFechaCreacion() &&
                    !pedido.getFechaPedido().toLocalDate().isEqual(criteria.getFechaCreacion())) {
                coincide = false;
            }

            if (coincide) {
                resultados.add(pedido);
            }
        }

        return resultados;
    }

    @Override
    public Optional<Pedido> buscarPedidoPorId(int id) {
        return pedidos.stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    @Override
    public boolean validarStockSuficiente(Pedido pedido) {
        for (LineaPedido linea : pedido.getLineas()) {
            Optional<Producto> productoOpt = catalogoService.buscarProductoPorId(linea.getProducto().getId());
            if (productoOpt.isEmpty()) {
                System.err.println("Producto no encontrado: ID " + linea.getProducto().getId());
                return false;
            }

            Producto producto = productoOpt.get();
            if (producto.getStock() < linea.getCantidad()) {
                System.err.println("Stock insuficiente para " + producto.getNombreProducto() +
                        ". Stock disponible: " + producto.getStock() +
                        ", solicitado: " + linea.getCantidad());
                return false;
            }
        }
        return true;
    }

    // Método auxiliar para obtener todos los pedidos (útil para testing)
    public List<Pedido> obtenerTodosLosPedidos() {
        return new ArrayList<>(pedidos);
    }

    // Método auxiliar para limpiar datos (útil para testing)
    public void limpiarDatos() {
        pedidos.clear();
        nextPedidoId = 1;
        nextLineaId = 1;
        inicializarDatos();
    }
}
