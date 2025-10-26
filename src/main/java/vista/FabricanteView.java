package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class FabricanteView extends JPanel {
    private JTextField txtId, txtNombre;
    private JButton btnBuscar, btnLimpiar;
    private JTable tablaFabricantes;
    private DefaultTableModel modeloTabla;

    private final Color COLOR_PRIMARIO = new Color(41, 128, 185);
    private final Color COLOR_FONDO = new Color(236, 240, 241);
    private final Color COLOR_CABECERA = new Color(52, 152, 219);
    private final Color COLOR_BOTON_SECUNDARIO = new Color(149, 165, 166);

    public FabricanteView() {
        setBackground(COLOR_FONDO);
        initComponents();
        layoutComponents();
        aplicarEstilos();
    }

    private void initComponents() {
        txtId = new JTextField(12);
        txtNombre = new JTextField(20);

        // Botones con tamaño fijo
        btnBuscar = crearBotonAccion("BUSCAR", COLOR_PRIMARIO);
        btnLimpiar = crearBotonAccion("LIMPIAR", COLOR_BOTON_SECUNDARIO);

        // Configurar tabla
        String[] columnas = {"ID", "Nombre"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaFabricantes = new JTable(modeloTabla);
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
        // Usar BoxLayout vertical para mejor control
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Panel de búsqueda - altura fija
        JPanel panelBusqueda = crearPanelBusqueda();

        // Panel de tabla - que ocupe el espacio restante
        JPanel panelTabla = crearPanelTabla();

        // Agregar con proporciones fijas
        add(panelBusqueda);
        add(Box.createRigidArea(new Dimension(0, 10))); // Espacio entre secciones
        add(panelTabla);
    }

    private JPanel crearPanelBusqueda() {
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panelBusqueda.setBackground(Color.WHITE);
        panelBusqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panelBusqueda.setMaximumSize(new Dimension(Short.MAX_VALUE, 120)); // ALTURA FIJA
        panelBusqueda.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblId = new JLabel("ID del Fabricante:");
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblId.setForeground(COLOR_PRIMARIO);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNombre.setForeground(COLOR_PRIMARIO);

        // Aplicar estilos a los campos de texto
        aplicarEstiloCampoTexto(txtId);
        aplicarEstiloCampoTexto(txtNombre);

        panelBusqueda.add(lblId);
        panelBusqueda.add(txtId);
        panelBusqueda.add(Box.createRigidArea(new Dimension(20, 0)));
        panelBusqueda.add(lblNombre);
        panelBusqueda.add(txtNombre);
        panelBusqueda.add(Box.createRigidArea(new Dimension(20, 0)));
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnLimpiar);

        return panelBusqueda;
    }

    private JPanel crearPanelTabla() {
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(COLOR_FONDO);
        panelTabla.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Hacer que la tabla ocupe el espacio restante pero con límite mínimo
        tablaFabricantes.setPreferredScrollableViewportSize(new Dimension(800, 400)); // Tamaño preferido

        JScrollPane scrollTabla = new JScrollPane(tablaFabricantes);
        scrollTabla.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 0, 0, 0),
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(COLOR_PRIMARIO, 2),
                        "LISTA DE FABRICANTES",
                        0, 0,
                        new Font("Segoe UI", Font.BOLD, 14),
                        COLOR_PRIMARIO
                )
        ));
        scrollTabla.getViewport().setBackground(Color.WHITE);
        scrollTabla.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Permitir que crezca pero con límite mínimo
        scrollTabla.setMinimumSize(new Dimension(800, 300));
        scrollTabla.setPreferredSize(new Dimension(800, 400));

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

    private void aplicarEstilos() {
        // Estilo para la tabla
        tablaFabricantes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaFabricantes.setRowHeight(30);
        tablaFabricantes.setShowGrid(true);
        tablaFabricantes.setGridColor(new Color(230, 230, 230));
        tablaFabricantes.setSelectionBackground(COLOR_PRIMARIO);
        tablaFabricantes.setSelectionForeground(Color.WHITE);

        // Ajustar anchos de columnas
        tablaFabricantes.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        tablaFabricantes.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre

        // Estilo para el header de la tabla
        JTableHeader header = tablaFabricantes.getTableHeader();
        header.setBackground(COLOR_CABECERA);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setReorderingAllowed(false);
    }

    // Getters
    public JTextField getTxtId() { return txtId; }
    public JTextField getTxtNombre() { return txtNombre; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JTable getTablaFabricantes() { return tablaFabricantes; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }

    public void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
    }
}