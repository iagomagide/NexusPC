package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoView extends JPanel {
    private JTextField txtId, txtNombre;
    private JComboBox<String> comboFabricantes, comboCategorias;
    private JButton btnBuscar, btnLimpiar, btnAnadir;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;

    private final Color COLOR_PRIMARIO = new Color(41, 128, 185);
    private final Color COLOR_FONDO = new Color(236, 240, 241);
    private final Color COLOR_CABECERA = new Color(52, 152, 219);
    private final Color COLOR_BOTON_SECUNDARIO = new Color(149, 165, 166);
    private final Color COLOR_BOTON_SUCCESS = new Color(39, 174, 96);

    public ProductoView() {
        setBackground(COLOR_FONDO);
        initComponents();
        layoutComponents();
        aplicarEstilos();
    }

    private void initComponents() {
        txtId = new JTextField(10);
        txtNombre = new JTextField(20);

        comboFabricantes = new JComboBox<>();
        comboCategorias = new JComboBox<>();

        comboFabricantes.addItem("Todos los fabricantes");
        comboCategorias.addItem("Todas las categorías");

        // Botones con estilo
        btnBuscar = crearBotonAccion("BUSCAR", COLOR_PRIMARIO);
        btnLimpiar = crearBotonAccion("LIMPIAR", COLOR_BOTON_SECUNDARIO);
        btnAnadir = crearBotonAccion("AÑADIR", COLOR_BOTON_SUCCESS);

        String[] columnas = {"ID", "Nombre", "Descripción", "PVP", "Stock", "Fabricante", "Categoría"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private JButton crearBotonAccion(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(new EmptyBorder(10, 20, 10, 20));
        boton.setPreferredSize(new Dimension(100, 40));

        // Efecto hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(color.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(color);
            }
        });

        return boton;
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Panel de búsqueda
        JPanel panelBusqueda = crearPanelBusqueda();

        // Panel de tabla
        JPanel panelTabla = crearPanelTabla();

        add(panelBusqueda, BorderLayout.NORTH);
        add(panelTabla, BorderLayout.CENTER);
    }

    private JPanel crearPanelBusqueda() {
        JPanel panelBusqueda = new JPanel(new GridLayout(2, 1, 10, 10));
        panelBusqueda.setBackground(Color.WHITE);
        panelBusqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panelBusqueda.setMaximumSize(new Dimension(Short.MAX_VALUE, 160));

        // Fila 1: ID y Nombre
        JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        fila1.setBackground(Color.WHITE);

        JLabel lblId = new JLabel("ID Producto:");
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblId.setForeground(COLOR_PRIMARIO);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNombre.setForeground(COLOR_PRIMARIO);

        aplicarEstiloCampoTexto(txtId);
        aplicarEstiloCampoTexto(txtNombre);

        fila1.add(lblId);
        fila1.add(txtId);
        fila1.add(Box.createRigidArea(new Dimension(20, 0)));
        fila1.add(lblNombre);
        fila1.add(txtNombre);

        // Fila 2: Fabricante, Categoría y Botones
        JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        fila2.setBackground(Color.WHITE);

        JLabel lblFabricante = new JLabel("Fabricante:");
        lblFabricante.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblFabricante.setForeground(COLOR_PRIMARIO);

        JLabel lblCategoria = new JLabel("Categoría:");
        lblCategoria.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCategoria.setForeground(COLOR_PRIMARIO);

        aplicarEstiloComboBox(comboFabricantes);
        aplicarEstiloComboBox(comboCategorias);

        fila2.add(lblFabricante);
        fila2.add(comboFabricantes);
        fila2.add(Box.createRigidArea(new Dimension(20, 0)));
        fila2.add(lblCategoria);
        fila2.add(comboCategorias);
        fila2.add(Box.createRigidArea(new Dimension(30, 0)));
        fila2.add(btnBuscar);
        fila2.add(btnLimpiar);
        fila2.add(btnAnadir);

        panelBusqueda.add(fila1);
        panelBusqueda.add(fila2);

        return panelBusqueda;
    }

    private JPanel crearPanelTabla() {
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(COLOR_FONDO);
        panelTabla.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Configurar tabla
        tablaProductos.setPreferredScrollableViewportSize(new Dimension(900, 400));

        JScrollPane scrollTabla = new JScrollPane(tablaProductos);
        scrollTabla.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 0, 0, 0),
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(COLOR_PRIMARIO, 2),
                        "LISTA DE PRODUCTOS",
                        0, 0,
                        new Font("Segoe UI", Font.BOLD, 14),
                        COLOR_PRIMARIO
                )
        ));
        scrollTabla.getViewport().setBackground(Color.WHITE);
        scrollTabla.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        panelTabla.add(scrollTabla, BorderLayout.CENTER);

        return panelTabla;
    }

    private void aplicarEstiloCampoTexto(JTextField campo) {
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        campo.setBackground(new Color(250, 250, 250));
        campo.setPreferredSize(new Dimension(campo.getPreferredSize().width, 35));
    }

    private void aplicarEstiloComboBox(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        combo.setPreferredSize(new Dimension(180, 35));
    }

    private void aplicarEstilos() {
        // Estilo para la tabla
        tablaProductos.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tablaProductos.setRowHeight(30);
        tablaProductos.setShowGrid(true);
        tablaProductos.setGridColor(new Color(230, 230, 230));
        tablaProductos.setSelectionBackground(COLOR_PRIMARIO);
        tablaProductos.setSelectionForeground(Color.WHITE);

        // Ajustar anchos de columnas
        tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(150); // Nombre
        tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(200); // Descripción
        tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(80);  // PVP
        tablaProductos.getColumnModel().getColumn(4).setPreferredWidth(60);  // Stock
        tablaProductos.getColumnModel().getColumn(5).setPreferredWidth(120); // Fabricante
        tablaProductos.getColumnModel().getColumn(6).setPreferredWidth(100); // Categoría

        // Estilo para el header de la tabla
        JTableHeader header = tablaProductos.getTableHeader();
        header.setBackground(COLOR_CABECERA);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setReorderingAllowed(false);
    }

    // ---------------- Getters ----------------
    public JTextField getTxtId() { return txtId; }
    public JTextField getTxtNombre() { return txtNombre; }
    public JComboBox<String> getComboFabricantes() { return comboFabricantes; }
    public JComboBox<String> getComboCategorias() { return comboCategorias; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JButton getBtnAnadir() { return btnAnadir; }
    public JTable getTabla() { return tablaProductos; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }

    public void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        comboFabricantes.setSelectedIndex(0);
        comboCategorias.setSelectedIndex(0);
    }

    public void cargarFabricantes(List<String> fabricantes) {
        comboFabricantes.removeAllItems();
        comboFabricantes.addItem("Todos los fabricantes");
        fabricantes.forEach(comboFabricantes::addItem);
    }

    public void cargarCategorias(List<String> categorias) {
        comboCategorias.removeAllItems();
        comboCategorias.addItem("Todas las categorías");
        categorias.forEach(comboCategorias::addItem);
    }

    public void refrescarTabla() {
        firePropertyChange("tablaRefrescar", false, true);
    }
}