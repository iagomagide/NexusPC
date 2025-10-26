package controlador;

import criteria.ProductoCriteria;
import model.Pedido;
import model.LineaPedido;
import model.Producto;
import service.CatalogoService;
import service.PedidoService;
import vista.PedidoView;
import vista.VentanaDetallePedido;
import vista.VentanaNuevoPedido;
import criteria.PedidoCriteria;

import javax.swing.*;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class PedidoController {
    private final PedidoService pedidoService;
    private final CatalogoService catalogoService;
    private final PedidoView pedidoView;

    public PedidoController(PedidoService pedidoService, CatalogoService catalogoService) {
        this.pedidoService = pedidoService;
        this.catalogoService = catalogoService;
        this.pedidoView = new PedidoView();
        setupEventListeners();
        cargarTodosLosPedidos();
    }

    private void setupEventListeners() {
        pedidoView.getBtnBuscar().addActionListener(e -> buscarPedidos());
        pedidoView.getBtnLimpiar().addActionListener(e -> {
            pedidoView.limpiarCampos();
            cargarTodosLosPedidos();
        });
        pedidoView.getBtnAnadir().addActionListener(e -> abrirVentanaNuevoPedido());
        pedidoView.getTablaPedidos().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Doble click
                    abrirDetallePedido();
                }
            }
        });
    }

    private void buscarPedidos() {
        try {

            PedidoCriteria criteria = new PedidoCriteria();

            // ID
            String idTxt = pedidoView.getTxtId().getText().trim();
            if (!idTxt.isEmpty()) criteria.setId(Integer.parseInt(idTxt));

            // Nombre cliente
            String cliente = pedidoView.getTxtCliente().getText().trim();
            if (!cliente.isEmpty()) criteria.setNombreCliente(cliente);

            //FechaEntrega
            LocalDate fechaEntrega = pedidoView.getFechaPedidoSeleccionada();
            if (fechaEntrega != null) {
                criteria.setFechaCreacion(fechaEntrega);// Necesitas agregar este campo a PedidoCriteria
            }

            List<Pedido> pedidos = pedidoService.buscarPedidosPorCriteria(criteria);
            for (Pedido p: pedidos){
                System.out.println(p);
            }
            actualizarTabla(pedidos);
        } catch (Exception e) {
            mostrarError("Error al buscar pedidos: " + e.getMessage());
        }
    }

    private void abrirVentanaNuevoPedido() {
        VentanaNuevoPedido ventana = new VentanaNuevoPedido();

        try {
            // Necesitas tener un servicio de productos
            ProductoCriteria crit = new ProductoCriteria();
            List<Producto> productos = catalogoService.buscarProductosPorCriteria(crit);
            ventana.cargarProductos(productos);
        } catch (Exception e) {
            mostrarError("Error al cargar productos: " + e.getMessage());
            return;
        }

        ventana.getBtnGuardar().addActionListener(e -> {
            try {
                Pedido pedido = ventana.construirPedido();
                boolean ok = pedidoService.crearPedido(pedido);
                if (ok) {
                    JOptionPane.showMessageDialog(ventana, "Pedido creado correctamente");
                    ventana.dispose();
                    cargarTodosLosPedidos();
                } else {
                    mostrarError("Error al crear pedido");
                }
            } catch (Exception ex) {
                mostrarError("Error: " + ex.getMessage());
            }
        });
        ventana.setVisible(true);
    }

    private void cargarTodosLosPedidos() {
        try {
            List<Pedido> pedidos = pedidoService.buscarPedidosPorCriteria(new PedidoCriteria());
            actualizarTabla(pedidos);
        } catch (Exception e) {
            mostrarError("Error al cargar pedidos: " + e.getMessage());
        }
    }

    private void actualizarTabla(List<Pedido> pedidos) {
        var modelo = pedidoView.getModeloTabla();
        modelo.setRowCount(0);
        for (Pedido p : pedidos) {
            modelo.addRow(new Object[]{
                    p.getId(),
                    p.getFechaPedido() != null ? p.getFechaPedido().toLocalDate() : "",
                    p.getFechaEntrega(),
                    p.getClienteNombre(),
                    p.getDireccion(),
                    String.format(Locale.US,"%.2f€", p.getPvp())
            });
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(pedidoView, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public PedidoView getVista() {
        return pedidoView;
    }

    private void abrirDetallePedido() {
        int filaSeleccionada = pedidoView.getTablaPedidos().getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(pedidoView, "Seleccione un pedido para ver los detalles",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Obtener el ID del pedido seleccionado
        int idPedido = (int) pedidoView.getModeloTabla().getValueAt(filaSeleccionada, 0);

        try {
            // Buscar el pedido completo con sus líneas
            Optional<Pedido> pedidoOpt = pedidoService.buscarPedidoPorId(idPedido);
            if (pedidoOpt.isPresent()) {
                Pedido pedido = pedidoOpt.get();
                VentanaDetallePedido ventana = new VentanaDetallePedido(pedido);
                ventana.setVisible(true);
            } else {
                mostrarError("No se pudo cargar el pedido seleccionado");
            }
        } catch (Exception e) {
            mostrarError("Error al cargar el detalle del pedido: " + e.getMessage());
        }
    }

}

