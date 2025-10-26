package service;

import criteria.PedidoCriteria;
import model.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoService {

    // Crear un nuevo pedido con sus líneas
    boolean crearPedido(Pedido pedido);

    // Buscar pedidos por criterios
    List<Pedido> buscarPedidosPorCriteria(PedidoCriteria criteria);

    // Buscar pedido por ID
    Optional<Pedido> buscarPedidoPorId(int id);

    // Validar stock antes de crear pedido (opcional)
    boolean validarStockSuficiente(Pedido pedido);
}
