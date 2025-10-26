package vista;


import com.toedter.calendar.JDateChooser;
import model.LineaPedido;
import model.Pedido;
import model.Producto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VentanaNuevoPedido extends JDialog {
    private JTextField txtCliente;
    private JDateChooser dateChooserEntrega;
    private JButton btnGuardar, btnCancelar, btnAgregarLinea, btnEliminarLinea;
    private JTable tablaLineas;
    private DefaultTableModel modeloTabla;
    private JComboBox<Producto> comboProductos;
    private JTextField txtCantidad;
    private List<LineaPedido> lineasPedido;

    // Colores coherentes con el proyecto
    private final Color COLOR_PRIMARIO = new Color(41, 128, 185);
    private final Color COLOR_FONDO = new Color(236, 240, 241);
    private final Color COLOR_BOTON_SECUNDARIO = new Color(149, 165, 166);
    private final Color COLOR_BOTON_SUCCESS = new Color(39, 174, 96);
    private final Color COLOR_BOTON_WARNING = new Color(243, 156, 18);

    public VentanaNuevoPedido() {
        setTitle("Nuevo Pedido");
        setModal(true);
        setSize(800, 750);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);

        this.lineasPedido = new ArrayList<>();
        initComponents();
        layoutComponents();
        aplicarEstilos();
    }

    private void initComponents() {
        txtCliente = new JTextField(25);

        dateChooserEntrega = new JDateChooser();
        dateChooserEntrega.setDateFormatString("yyyy-MM-dd");
        dateChooserEntrega.setPreferredSize(new Dimension(180, 35));
        dateChooserEntrega.setBackground(Color.WHITE);

        // Combo para productos
        comboProductos = new JComboBox<>();
        comboProductos.setPreferredSize(new Dimension(300, 35));

        txtCantidad = new JTextField(10);
        aplicarEstiloCampoTexto(txtCantidad);

        // Tabla para líneas de pedido
        String[] columnas = {"Producto", "Cantidad", "PVP Unitario", "PVP Total"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaLineas = new JTable(modeloTabla);

        // Botones - 🆕 Botón AÑADIR LÍNEA más pequeño
        btnAgregarLinea = crearBoton("AÑADIR LÍNEA", COLOR_BOTON_SUCCESS);
        btnEliminarLinea = crearBoton("ELIMINAR LÍNEA", COLOR_BOTON_WARNING);
        btnGuardar = crearBoton("GUARDAR PEDIDO", COLOR_BOTON_SUCCESS);
        btnCancelar = crearBoton("CANCELAR", COLOR_BOTON_SECUNDARIO);

        // Listeners
        btnCancelar.addActionListener(e -> dispose());
        btnAgregarLinea.addActionListener(e -> agregarLinea());
        btnEliminarLinea.addActionListener(e -> eliminarLinea());
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBackground(COLOR_FONDO);
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));

        // 🆕 Usar Box para mejor organización
        Box verticalBox = Box.createVerticalBox();
        verticalBox.add(crearPanelInformacion());
        verticalBox.add(Box.createVerticalStrut(25));
        verticalBox.add(crearPanelAgregarLinea());
        verticalBox.add(Box.createVerticalStrut(25));
        verticalBox.add(crearPanelLineas());
        verticalBox.add(Box.createVerticalStrut(25));
        verticalBox.add(crearPanelBotones());

        panelPrincipal.add(verticalBox, BorderLayout.CENTER);
        add(panelPrincipal);
    }

    private JPanel crearPanelInformacion() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblCliente = new JLabel("Cliente:");
        JLabel lblFechaEntrega = new JLabel("Fecha Entrega:");

        lblCliente.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCliente.setForeground(COLOR_PRIMARIO);
        lblFechaEntrega.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblFechaEntrega.setForeground(COLOR_PRIMARIO);

        aplicarEstiloCampoTexto(txtCliente);

        panel.add(lblCliente);
        panel.add(txtCliente);
        panel.add(lblFechaEntrega);
        panel.add(dateChooserEntrega);

        return panel;
    }

    private JPanel crearPanelAgregarLinea() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Agregar Línea de Pedido"),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // 🆕 Panel principal con GridLayout de 2 filas
        JPanel panelPrincipal = new JPanel(new GridLayout(2, 1, 15, 15));
        panelPrincipal.setBackground(Color.WHITE);

        // 🆕 Fila 1: Producto
        JPanel panelProducto = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelProducto.setBackground(Color.WHITE);

        JLabel lblProducto = new JLabel("Producto:");
        lblProducto.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblProducto.setForeground(COLOR_PRIMARIO);

        // Estilo para combo
        comboProductos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboProductos.setBackground(Color.WHITE);
        comboProductos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        panelProducto.add(lblProducto);
        panelProducto.add(comboProductos);

        // 🆕 Fila 2: Cantidad y Botón
        JPanel panelCantidadBoton = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panelCantidadBoton.setBackground(Color.WHITE);

        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCantidad.setForeground(COLOR_PRIMARIO);

        panelCantidadBoton.add(lblCantidad);
        panelCantidadBoton.add(txtCantidad);
        panelCantidadBoton.add(Box.createRigidArea(new Dimension(30, 0)));
        panelCantidadBoton.add(btnAgregarLinea);

        panelPrincipal.add(panelProducto);
        panelPrincipal.add(panelCantidadBoton);

        panel.add(panelPrincipal, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelLineas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Líneas del Pedido"),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Configurar tabla
        tablaLineas.setRowHeight(30);
        tablaLineas.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        // Configurar anchos de columnas
        tablaLineas.getColumnModel().getColumn(0).setPreferredWidth(250);
        tablaLineas.getColumnModel().getColumn(1).setPreferredWidth(80);
        tablaLineas.getColumnModel().getColumn(2).setPreferredWidth(100);
        tablaLineas.getColumnModel().getColumn(3).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(tablaLineas);
        scrollPane.setPreferredSize(new Dimension(700, 200));

        // Panel de botones para líneas
        JPanel panelBotonesLinea = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotonesLinea.setBackground(Color.WHITE);
        panelBotonesLinea.add(btnEliminarLinea);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(panelBotonesLinea, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(15, 0, 0, 0));

        panel.add(btnCancelar);
        panel.add(Box.createRigidArea(new Dimension(20, 0)));
        panel.add(btnGuardar);

        return panel;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(new EmptyBorder(8, 15, 8, 15));
        boton.setPreferredSize(new Dimension(140, 35)); // 🆕 Tamaño normal

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

    private void aplicarEstiloCampoTexto(JTextField campo) {
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        campo.setBackground(new Color(250, 250, 250));
        campo.setPreferredSize(new Dimension(100, 35));
    }

    private void aplicarEstilos() {
        // Estilo para el JDateChooser
        if (dateChooserEntrega.getCalendarButton() != null) {
            dateChooserEntrega.getCalendarButton().setBackground(COLOR_PRIMARIO);
            dateChooserEntrega.getCalendarButton().setForeground(Color.WHITE);
            dateChooserEntrega.getCalendarButton().setFocusPainted(false);
            dateChooserEntrega.getCalendarButton().setBorderPainted(false);
        }

        // Estilo para la tabla
        tablaLineas.getTableHeader().setBackground(COLOR_PRIMARIO);
        tablaLineas.getTableHeader().setForeground(Color.WHITE);
        tablaLineas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaLineas.setShowGrid(true);
        tablaLineas.setGridColor(new Color(230, 230, 230));
    }

    private void agregarLinea() {
        try {
            Producto producto = (Producto) comboProductos.getSelectedItem();
            if (producto == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un producto", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar stock disponible
            if (cantidad > producto.getStock()) {
                JOptionPane.showMessageDialog(this,
                        "Stock insuficiente. Stock disponible: " + producto.getStock(),
                        "Error de Stock", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Crear línea de pedido
            LineaPedido linea = new LineaPedido(0, producto, cantidad, producto.getPvp());
            lineasPedido.add(linea);

            // Agregar a la tabla
            double pvpTotal = cantidad * producto.getPvp();
            modeloTabla.addRow(new Object[]{
                    producto.getNombreProducto(),
                    cantidad,
                    String.format("€%.2f", producto.getPvp()),
                    String.format("€%.2f", pvpTotal)
            });

            // Limpiar campos
            txtCantidad.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarLinea() {
        int filaSeleccionada = tablaLineas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una línea para eliminar", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        lineasPedido.remove(filaSeleccionada);
        modeloTabla.removeRow(filaSeleccionada);
    }

    // Método para cargar productos en el combo
    public void cargarProductos(List<Producto> productos) {
        comboProductos.removeAllItems();
        if (productos != null) {
            for (Producto producto : productos) {
                comboProductos.addItem(producto);
            }
        }

        // Renderer personalizado para mostrar mejor la información
        comboProductos.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Producto) {
                    Producto producto = (Producto) value;
                    setText(producto.getNombreProducto() + " - Stock: " + producto.getStock() + " - €" + producto.getPvp());
                }
                return this;
            }
        });
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public Pedido construirPedido() throws Exception {
        // Validaciones
        if (txtCliente.getText().trim().isEmpty()) {
            throw new Exception("El campo Cliente es obligatorio");
        }

        if (dateChooserEntrega.getDate() == null) {
            throw new Exception("La fecha de entrega es obligatoria");
        }

        if (lineasPedido.isEmpty()) {
            throw new Exception("El pedido debe tener al menos una línea");
        }

        // Construir el pedido
        LocalDate fechaEntrega = dateChooserEntrega.getDate().toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();

        // Calcular PVP total
        double pvpTotal = lineasPedido.stream()
                .mapToDouble(linea -> linea.getCantidad() * linea.getPvpLinea())
                .sum();

        Pedido pedido = new Pedido(
                -1,
                fechaEntrega,
                "", // Dirección vacía según los cambios anteriores
                txtCliente.getText().trim()
        );

        pedido.setLineas(new ArrayList<>(lineasPedido));
        pedido.setPvp(pvpTotal);

        return pedido;
    }

    // Método para limpiar los campos
    public void limpiarCampos() {
        txtCliente.setText("");
        dateChooserEntrega.setDate(null);
        txtCantidad.setText("");
        lineasPedido.clear();
        modeloTabla.setRowCount(0);
    }
}