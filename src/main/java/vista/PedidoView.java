package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class PedidoView extends JPanel {

    private JTextField txtId;
    private JTextField txtCliente;
    private JDateChooser dateChooserEntrega;
    private JButton btnBuscar, btnLimpiar, btnAnadir;
    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;

    // Colores coherentes con ProductoView
    private final Color COLOR_PRIMARIO = new Color(41, 128, 185);
    private final Color COLOR_FONDO = new Color(236, 240, 241);
    private final Color COLOR_CABECERA = new Color(52, 152, 219);
    private final Color COLOR_BOTON_SECUNDARIO = new Color(149, 165, 166);
    private final Color COLOR_BOTON_SUCCESS = new Color(39, 174, 96);

    public PedidoView() {
        setBackground(COLOR_FONDO);
        initComponents();
        layoutComponents();
        aplicarEstilos();
    }

    private void initComponents() {
        txtId = new JTextField(10);
        txtCliente = new JTextField(20);

        dateChooserEntrega = new JDateChooser();
        dateChooserEntrega.setDateFormatString("yyyy-MM-dd");
        dateChooserEntrega.setPreferredSize(new Dimension(150, 35));
        dateChooserEntrega.setBackground(Color.WHITE);

        btnBuscar = crearBotonAccion("BUSCAR", COLOR_PRIMARIO);
        btnLimpiar = crearBotonAccion("LIMPIAR", COLOR_BOTON_SECUNDARIO);
        btnAnadir = crearBotonAccion("AÑADIR", COLOR_BOTON_SUCCESS);

        String[] columnas = {"ID", "Fecha Pedido", "Fecha Entrega", "Cliente", "Dirección", "PVP Total"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaPedidos = new JTable(modeloTabla);
        tablaPedidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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

        JPanel panelBusqueda = crearPanelBusqueda();
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

        // Fila 1 - Todos los campos en una sola fila
        JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        fila1.setBackground(Color.WHITE);

        JLabel lblId = new JLabel("ID Pedido:");
        JLabel lblCliente = new JLabel("Cliente:");
        JLabel lblFechaPedido = new JLabel("Fecha Pedido:");

        aplicarEstiloCampoTexto(txtId);
        aplicarEstiloCampoTexto(txtCliente);

        lblId.setForeground(COLOR_PRIMARIO);
        lblCliente.setForeground(COLOR_PRIMARIO);
        lblFechaPedido.setForeground(COLOR_PRIMARIO);
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCliente.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblFechaPedido.setFont(new Font("Segoe UI", Font.BOLD, 12));

        fila1.add(lblId);
        fila1.add(txtId);
        fila1.add(Box.createRigidArea(new Dimension(20, 0)));
        fila1.add(lblCliente);
        fila1.add(txtCliente);
        fila1.add(Box.createRigidArea(new Dimension(20, 0)));
        fila1.add(lblFechaPedido);
        fila1.add(dateChooserEntrega);

        // Fila 2 - Solo botones
        JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        fila2.setBackground(Color.WHITE);

        // Espacio para alinear los botones con los campos de arriba
        fila2.add(Box.createRigidArea(new Dimension(445, 0))); // Ajusta este valor según necesites

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

        tablaPedidos.setPreferredScrollableViewportSize(new Dimension(900, 400));

        JScrollPane scrollTabla = new JScrollPane(tablaPedidos);
        scrollTabla.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 0, 0, 0),
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(COLOR_PRIMARIO, 2),
                        "LISTA DE PEDIDOS",
                        0, 0,
                        new Font("Segoe UI", Font.BOLD, 14),
                        COLOR_PRIMARIO
                )
        ));
        scrollTabla.getViewport().setBackground(Color.WHITE);

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
        campo.setPreferredSize(new Dimension(150, 35));
    }

    private void aplicarEstilos() {
        tablaPedidos.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tablaPedidos.setRowHeight(30);
        tablaPedidos.setShowGrid(true);
        tablaPedidos.setGridColor(new Color(230, 230, 230));
        tablaPedidos.setSelectionBackground(COLOR_PRIMARIO);
        tablaPedidos.setSelectionForeground(Color.WHITE);

        JTableHeader header = tablaPedidos.getTableHeader();
        header.setBackground(COLOR_CABECERA);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setReorderingAllowed(false);
    }

    // ---------------- Getters ----------------
    public JTextField getTxtId() { return txtId; }
    public JTextField getTxtCliente() { return txtCliente; }
    public JDateChooser getDateChooserEntrega() { return dateChooserEntrega; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JButton getBtnAnadir() { return btnAnadir; }
    public JTable getTablaPedidos() { return tablaPedidos; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }

    public void limpiarCampos() {
        txtId.setText("");
        txtCliente.setText("");
        dateChooserEntrega.setDate(null);
    }

    public LocalDate getFechaPedidoSeleccionada() {
        Date date = dateChooserEntrega.getDate();
        return (date != null) ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
    }

    public void refrescarTabla() {
        firePropertyChange("tablaRefrescar", false, true);
    }
}