package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainView extends JFrame {
    private JButton btnPrincipal, btnSecundario, btnTerciario;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    // Colores modernos para tienda de informática
    private final Color COLOR_PRIMARIO = new Color(41, 128, 185);    // Azul profesional
    private final Color COLOR_SECUNDARIO = new Color(52, 73, 94);    // Gris azulado oscuro
    private final Color COLOR_FONDO = new Color(236, 240, 241);      // Gris claro profesional
    private final Color COLOR_TEXTO = new Color(44, 62, 80);         // Gris oscuro para texto
    private final Color COLOR_EXITO = new Color(46, 204, 113);       // Verde para éxitos
    private final Color COLOR_AVISO = new Color(241, 196, 15);       // Amarillo para características

    public MainView() {
        setTitle("NexusPC - Tienda de Tecnología");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 800);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);

        initComponents();
        layoutComponents();
        aplicarEstilos();
    }

    private void initComponents() {
        // Botones con estilo moderno
        btnPrincipal = crearBotonModerno("Fabricantes");
        btnSecundario = crearBotonModerno("Productos");
        btnTerciario = crearBotonModerno("Pedidos");

        // Configurar CardLayout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(COLOR_FONDO);
    }

    private JButton crearBotonModerno(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(new EmptyBorder(12, 20, 12, 20));
        return boton;
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());

        // Panel lateral moderno
        JPanel panelLateral = crearPanelLateral();

        // Panel central con CardLayout
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(COLOR_FONDO);

        // Panel de bienvenida (card principal)
        JPanel panelBienvenida = crearPanelBienvenida();

        // Agregar cards al panel
        cardPanel.add(panelBienvenida, "BIENVENIDA");

        panelCentral.add(cardPanel, BorderLayout.CENTER);

        add(panelLateral, BorderLayout.WEST);
        add(panelCentral, BorderLayout.CENTER);
    }

    private JPanel crearPanelLateral() {
        JPanel panelLateral = new JPanel();
        panelLateral.setLayout(new BoxLayout(panelLateral, BoxLayout.Y_AXIS));
        panelLateral.setBackground(COLOR_SECUNDARIO);
        panelLateral.setBorder(new EmptyBorder(30, 20, 30, 20));
        panelLateral.setPreferredSize(new Dimension(220, 0));

        // Logo/Header del panel lateral
        JLabel lblLogo = new JLabel("NEXUS PC");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLogo.setBorder(new EmptyBorder(0, 0, 30, 0));

        // Línea separadora
        JSeparator separador = new JSeparator();
        separador.setForeground(COLOR_PRIMARIO);
        separador.setMaximumSize(new Dimension(180, 2));

        panelLateral.add(lblLogo);
        panelLateral.add(separador);
        panelLateral.add(Box.createRigidArea(new Dimension(0, 30)));
        panelLateral.add(btnPrincipal);
        panelLateral.add(Box.createRigidArea(new Dimension(0, 12)));
        panelLateral.add(btnSecundario);
        panelLateral.add(Box.createRigidArea(new Dimension(0, 12)));
        panelLateral.add(btnTerciario);

        return panelLateral;
    }

    private JPanel crearPanelBienvenida() {
        JPanel panelBienvenida = new JPanel(new BorderLayout());
        panelBienvenida.setBackground(COLOR_FONDO);
        panelBienvenida.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Panel principal de contenido
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBackground(COLOR_FONDO);

        // Título principal
        JLabel lblTitulo = new JLabel("Bienvenido a NexusPC");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitulo.setForeground(COLOR_PRIMARIO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Subtítulo
        JLabel lblSubtitulo = new JLabel("Tu tienda de tecnología de confianza");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblSubtitulo.setForeground(COLOR_TEXTO);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSubtitulo.setBorder(new EmptyBorder(0, 0, 50, 0));

        // Panel de características
        JPanel panelCaracteristicas = crearPanelCaracteristicas();

        // Mensaje de bienvenida
        JPanel panelMensaje = crearPanelMensajeBienvenida();

        panelContenido.add(lblTitulo);
        panelContenido.add(lblSubtitulo);
        panelContenido.add(panelCaracteristicas);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 40)));
        panelContenido.add(panelMensaje);

        panelBienvenida.add(panelContenido, BorderLayout.CENTER);

        return panelBienvenida;
    }

    private JPanel crearPanelCaracteristicas() {
        JPanel panelCaracteristicas = new JPanel(new GridLayout(1, 3, 25, 0));
        panelCaracteristicas.setBackground(COLOR_FONDO);
        panelCaracteristicas.setBorder(new EmptyBorder(0, 20, 0, 20));
        panelCaracteristicas.setMaximumSize(new Dimension(900, 180)); // Altura fija aumentada

        // Característica 1 - Tecnología
        JPanel card1 = crearCardCaracteristica("TECNOLOGÍA", "Avanzada",
                "Productos de última generación con las mejores especificaciones del mercado");

        // Característica 2 - Precios
        JPanel card2 = crearCardCaracteristica("PRECIOS", "Competitivos",
                "Las mejores ofertas y relaciones calidad-precio del sector");

        // Característica 3 - Soporte
        JPanel card3 = crearCardCaracteristica("SOPORTE", "Experto",
                "Asesoramiento profesional y soporte técnico especializado");

        panelCaracteristicas.add(card1);
        panelCaracteristicas.add(card2);
        panelCaracteristicas.add(card3);

        return panelCaracteristicas;
    }

    private JPanel crearCardCaracteristica(String tituloPrincipal, String tituloSecundario, String descripcion) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(25, 20, 25, 20) // Más padding vertical
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(280, 160)); // Tamaño fijo

        // Panel superior con títulos
        JPanel panelTitulo = new JPanel();
        panelTitulo.setLayout(new BoxLayout(panelTitulo, BoxLayout.Y_AXIS));
        panelTitulo.setBackground(Color.WHITE);
        panelTitulo.setBorder(new EmptyBorder(0, 0, 20, 0)); // Más espacio abajo

        JLabel lblTituloPrimario = new JLabel(tituloPrincipal);
        lblTituloPrimario.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTituloPrimario.setForeground(COLOR_PRIMARIO);
        lblTituloPrimario.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTituloSecundario = new JLabel(tituloSecundario);
        lblTituloSecundario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTituloSecundario.setForeground(COLOR_AVISO);
        lblTituloSecundario.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTituloSecundario.setBorder(new EmptyBorder(5, 0, 0, 0));

        panelTitulo.add(lblTituloPrimario);
        panelTitulo.add(lblTituloSecundario);

        // Descripción - SIN SCROLL, texto ajustado
        JTextArea txtDescripcion = new JTextArea(descripcion);
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDescripcion.setForeground(COLOR_TEXTO);
        txtDescripcion.setBackground(Color.WHITE);
        txtDescripcion.setEditable(false);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setBorder(new EmptyBorder(10, 10, 10, 10));
        txtDescripcion.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Fijar tamaño para que no aparezca scroll
        txtDescripcion.setPreferredSize(new Dimension(240, 60)); // Altura fija
        txtDescripcion.setMinimumSize(new Dimension(240, 60));
        txtDescripcion.setMaximumSize(new Dimension(240, 60));

        JPanel panelDescripcion = new JPanel(new BorderLayout());
        panelDescripcion.setBackground(Color.WHITE);
        panelDescripcion.add(txtDescripcion, BorderLayout.CENTER);

        card.add(panelTitulo, BorderLayout.NORTH);
        card.add(panelDescripcion, BorderLayout.CENTER);

        return card;
    }

    private JPanel crearPanelMensajeBienvenida() {
        JPanel panelMensaje = new JPanel(new BorderLayout());
        panelMensaje.setBackground(Color.WHITE);
        panelMensaje.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARIO, 2),
                BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));
        panelMensaje.setMaximumSize(new Dimension(800, 140)); // Altura fija

        JLabel lblTituloMensaje = new JLabel("¿Por qué elegir NexusPC?");
        lblTituloMensaje.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTituloMensaje.setForeground(COLOR_PRIMARIO);
        lblTituloMensaje.setBorder(new EmptyBorder(0, 0, 15, 0));

        JTextArea txtMensaje = new JTextArea();
        txtMensaje.setText("En NexusPC nos especializamos en ofrecerte la mejor tecnología al precio más competitivo. " +
                "Desde portátiles gaming de alto rendimiento hasta smartphones de última generación, " +
                "accesorios y componentes de calidad. Nuestro compromiso es tu satisfacción total.");
        txtMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtMensaje.setForeground(COLOR_TEXTO);
        txtMensaje.setBackground(Color.WHITE);
        txtMensaje.setEditable(false);
        txtMensaje.setLineWrap(true);
        txtMensaje.setWrapStyleWord(true);
        txtMensaje.setBorder(new EmptyBorder(5, 0, 0, 0));

        // Fijar tamaño para consistencia
        txtMensaje.setPreferredSize(new Dimension(740, 60)); // Altura fija
        txtMensaje.setMinimumSize(new Dimension(740, 60));
        txtMensaje.setMaximumSize(new Dimension(740, 60));

        panelMensaje.add(lblTituloMensaje, BorderLayout.NORTH);
        panelMensaje.add(txtMensaje, BorderLayout.CENTER);

        return panelMensaje;
    }

    private void aplicarEstilos() {
        actualizarEstilosBotones("BIENVENIDA");
    }

    public void actualizarEstilosBotones(String vistaActual) {
        // Reiniciar estilos de todos los botones
        btnPrincipal.setBackground(COLOR_SECUNDARIO);
        btnPrincipal.setForeground(Color.WHITE);
        btnSecundario.setBackground(COLOR_SECUNDARIO);
        btnSecundario.setForeground(Color.WHITE);
        btnTerciario.setBackground(COLOR_SECUNDARIO);
        btnTerciario.setForeground(Color.WHITE);

        // Aplicar estilo activo según la vista
        switch (vistaActual) {
            case "BIENVENIDA":
                btnPrincipal.setBackground(COLOR_PRIMARIO);
                btnPrincipal.setForeground(Color.WHITE);
                btnPrincipal.setText("Fabricantes");
                btnSecundario.setText("Productos");
                btnTerciario.setText("Pedidos");
                break;
            case "FABRICANTES":
                btnPrincipal.setBackground(COLOR_PRIMARIO);
                break;
            case "PRODUCTOS":
                btnSecundario.setBackground(COLOR_PRIMARIO);
                break;
            case "PEDIDOS":
                btnTerciario.setBackground(COLOR_PRIMARIO);
                break;
        }

        // Aplicar efectos hover a todos los botones
        aplicarEfectoHover(btnPrincipal);
        aplicarEfectoHover(btnSecundario);
        aplicarEfectoHover(btnTerciario);
    }

    private void aplicarEfectoHover(JButton boton) {
        Color colorOriginal = boton.getBackground();

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!boton.getBackground().equals(COLOR_PRIMARIO)) {
                    boton.setBackground(COLOR_PRIMARIO.brighter());
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!boton.getBackground().equals(COLOR_PRIMARIO)) {
                    boton.setBackground(colorOriginal);
                }
            }
        });
    }

    // Método para agregar una vista al CardLayout
    public void agregarVista(String nombre, JPanel panel) {
        cardPanel.add(panel, nombre);
    }

    // Método para mostrar una vista específica
    public void mostrarVista(String nombre) {
        cardLayout.show(cardPanel, nombre);
        actualizarEstilosBotones(nombre);
    }

    // Getters para los botones
    public JButton getBtnPrincipal() { return btnPrincipal; }
    public JButton getBtnSecundario() { return btnSecundario; }
    public JButton getBtnTerciario() { return btnTerciario; }

    public void setMensajeBienvenida(String mensaje) {
        // Este método se mantiene por compatibilidad
    }
}