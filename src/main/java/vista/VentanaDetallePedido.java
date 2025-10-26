package vista;

import model.LineaPedido;
import model.Pedido;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaDetallePedido extends JDialog {
    private Pedido pedido;

    public VentanaDetallePedido(Pedido pedido) {
        this.pedido = pedido;
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setTitle("Detalle del Pedido #" + pedido.getId());
        setModal(true);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel de información general del pedido
        JPanel panelInfo = crearPanelInformacion();

        // Panel de líneas del pedido
        JPanel panelLineas = crearPanelLineas();

        // Panel de botones
        JPanel panelBotones = crearPanelBotones();

        add(panelInfo, BorderLayout.NORTH);
        add(panelLineas, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private JPanel crearPanelInformacion() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Información del Pedido"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(Color.WHITE);

        // Información del pedido
        panel.add(crearEtiqueta("ID Pedido:"));
        panel.add(crearCampoTexto(String.valueOf(pedido.getId())));

        panel.add(crearEtiqueta("Cliente:"));
        panel.add(crearCampoTexto(pedido.getClienteNombre()));

        panel.add(crearEtiqueta("Dirección:"));
        panel.add(crearCampoTexto(pedido.getDireccion()));

        panel.add(crearEtiqueta("Fecha Entrega:"));
        panel.add(crearCampoTexto(pedido.getFechaEntrega().toString()));

        return panel;
    }

    private JPanel crearPanelLineas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Líneas del Pedido"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        String[] columnas = {"Producto", "Cantidad", "PVP Unitario", "PVP Total"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tablaLineas = new JTable(modeloTabla);
        tablaLineas.setRowHeight(25);
        tablaLineas.getTableHeader().setBackground(new Color(52, 152, 219));
        tablaLineas.getTableHeader().setForeground(Color.WHITE);
        tablaLineas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Cargar líneas en la tabla
        for (LineaPedido linea : pedido.getLineas()) {
            double pvpTotal = linea.getCantidad() * linea.getPvpLinea();
            modeloTabla.addRow(new Object[]{
                    linea.getProducto().getNombreProducto(),
                    linea.getCantidad(),
                    String.format("€%.2f", linea.getPvpLinea()),
                    String.format("€%.2f", pvpTotal)
            });
        }

        JScrollPane scrollPane = new JScrollPane(tablaLineas);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel de totales
        JPanel panelTotales = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTotales.add(new JLabel("Total Pedido: "));
        JLabel lblTotal = new JLabel(String.format("€%.2f", pedido.getPvp()));
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotal.setForeground(new Color(39, 174, 96));
        panelTotales.add(lblTotal);

        panel.add(panelTotales, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());

        // Estilo del botón
        btnCerrar.setBackground(new Color(149, 165, 166));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setPreferredSize(new Dimension(100, 35));

        panel.add(btnCerrar);
        return panel;
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return label;
    }

    private JTextField crearCampoTexto(String texto) {
        JTextField campo = new JTextField(texto);
        campo.setEditable(false);
        campo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        campo.setBackground(new Color(240, 240, 240));
        return campo;
    }

    private void cargarDatos() {
        // Los datos ya se cargan en initComponents()
    }
}
