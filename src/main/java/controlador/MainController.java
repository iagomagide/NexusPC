package controlador;

import service.CatalogoService;
import service.PedidoService;
import vista.MainView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MainController {
    private MainView mainView;
    private CatalogoService catalogoService;
    private PedidoService pedidoService;
   // private Connection connection;

    private ProductoController productoController;
    private FabricanteController fabricanteController;
    private PedidoController pedidoController;

    public MainController(MainView mainView, CatalogoService catalogoService, PedidoService pedidoService) {
        this.mainView = mainView;
        this.catalogoService = catalogoService;
        this.pedidoService = pedidoService;

        inicializarControladores();
        configurarEventos();
    }

    private void inicializarControladores() {
        // Inicializar controladores
        productoController = new ProductoController(catalogoService);
        fabricanteController = new FabricanteController(catalogoService);
        pedidoController = new PedidoController(pedidoService, catalogoService);

        // Agregar vistas al CardLayout
        //mainView.agregarVista("INICIO", crearPanelInicio());
        mainView.mostrarVista("BIENVENIDA");
        mainView.agregarVista("FABRICANTES", fabricanteController.getVista());
        mainView.agregarVista("PRODUCTOS", productoController.getVista());
        mainView.agregarVista("PEDIDOS", pedidoController.getVista());
    }

    private void configurarEventos() {
        // Configurar eventos de los botones - USANDO LOS NOMBRES CORRECTOS
        mainView.getBtnPrincipal().addActionListener(e -> mostrarFabricantes());
        mainView.getBtnSecundario().addActionListener(e -> mostrarProductos());
        mainView.getBtnTerciario().addActionListener(e -> mostrarPedidos());
        // Si no tienes botón de pedidos, elimina esta línea
    }

    private void mostrarInicio() {
        mainView.mostrarVista("INICIO");
    }

    private void mostrarFabricantes() {
        mainView.mostrarVista("FABRICANTES");
        // Opcional: refrescar datos de fabricantes si es necesario
    }

    private void mostrarProductos() {
        mainView.mostrarVista("PRODUCTOS");
        // Refrescar la tabla de productos
        productoController.cargarTodosLosProductos();
    }

    private void mostrarPedidos() {
        mainView.mostrarVista("PEDIDOS");
        // Opcional: refrescar datos de pedidos si es necesario
    }

    private JPanel crearPanelInicio() {
        JPanel panelInicio = new JPanel(new BorderLayout());
        panelInicio.setBackground(mainView.getBackground());
        panelInicio.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Aquí puedes agregar el contenido del panel de inicio que ya tenías
        JLabel lblBienvenida = new JLabel("Bienvenido a NexusPC", JLabel.CENTER);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblBienvenida.setForeground(new Color(41, 128, 185));

        panelInicio.add(lblBienvenida, BorderLayout.CENTER);
        return panelInicio;
    }

    public void mostrarVista() {
        mainView.setVisible(true);
        mainView.mostrarVista("BIENVENIDA");
    }
}