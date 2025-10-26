package vista;


import javax.swing.*;
import javax.swing.border.EmptyBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class VentanaEdicionProducto extends JDialog {
    private JTextField txtNombre, txtDescripcion, txtPvp, txtStock;
    private JComboBox<String> comboFabricantes, comboCategorias;
    private JButton btnGuardar, btnActualizar, btnBorrar, btnCancelar;
    private boolean modoEdicion; // true = editar, false = nuevo

    // Paleta de colores consistente con las otras vistas
    private final Color COLOR_PRIMARIO = new Color(41, 128, 185);
    private final Color COLOR_FONDO = new Color(236, 240, 241);
    private final Color COLOR_TEXTO = new Color(44, 62, 80);
    private final Color COLOR_BOTON_DANGER = new Color(231, 76, 60);
    private final Color COLOR_BOTON_SECUNDARIO = new Color(149, 165, 166);
    private final Font FUENTE_TEXTO = new Font("Segoe UI", Font.PLAIN, 13);

    // 👉 Constructor para crear un nuevo producto
    public VentanaEdicionProducto() {
        this(0, "", "", 0.0, 0, "", "", false, new ArrayList<>(), new ArrayList<>());
    }


    // 👉 Constructor para editar un producto existente
    public VentanaEdicionProducto(int id, String nombre, String descripcion, double pvp,
                                  int stock, String fabricante, String categoria, List<String> lFab, List<String> lCat) {
        this(id, nombre, descripcion, pvp, stock, fabricante, categoria, true, lFab, lCat);
    }

    // Constructor general (usado internamente)
    private VentanaEdicionProducto(int id, String nombre, String descripcion, double pvp,
                                   int stock, String fabricante, String categoria, boolean modoEdicion, List<String> lFab, List<String> lCat) {
        this.modoEdicion = modoEdicion;

        setTitle(modoEdicion ? "Editar Producto - ID: " + id : "Nuevo Producto");
        setModal(true);
        setSize(520, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(COLOR_FONDO);

        initComponents();
        layoutComponents();

        // Cargar combos completos
        comboFabricantes.removeAllItems();
        lFab.forEach(comboFabricantes::addItem);
        comboFabricantes.setSelectedItem(fabricante);

        comboCategorias.removeAllItems();
        lCat.forEach(comboCategorias::addItem);
        comboCategorias.setSelectedItem(categoria);


        txtNombre.setText(nombre);
        txtDescripcion.setText(descripcion);
        txtPvp.setText(String.format(Locale.US,"%.2f", pvp));
        txtStock.setText(String.valueOf(stock));

        configurarModo();
    }

    private void initComponents() {
        txtNombre = crearCampoTexto();
        txtDescripcion = crearCampoTexto();
        txtPvp = crearCampoTexto();
        txtStock = crearCampoTexto();

        comboFabricantes = new JComboBox<>();
        comboCategorias = new JComboBox<>();

        btnGuardar = crearBoton("GUARDAR", COLOR_PRIMARIO);
        btnActualizar = crearBoton("ACTUALIZAR", COLOR_PRIMARIO);
        btnBorrar = crearBoton("BORRAR", COLOR_BOTON_DANGER);
        btnCancelar = crearBoton("CANCELAR", COLOR_BOTON_SECUNDARIO);
    }

    private void layoutComponents() {
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARIO, 2),
                new EmptyBorder(25, 30, 25, 30)
        ));
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));

        // Título
        JLabel lblTitulo = new JLabel(modoEdicion ? "Editar Producto" : "Nuevo Producto", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(COLOR_PRIMARIO);
        lblTitulo.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Panel formulario
        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 10, 15));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.add(crearLabel("Nombre:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(crearLabel("Descripción:"));
        panelFormulario.add(txtDescripcion);
        panelFormulario.add(crearLabel("PVP (€):"));
        panelFormulario.add(txtPvp);
        panelFormulario.add(crearLabel("Stock:"));
        panelFormulario.add(txtStock);
        panelFormulario.add(crearLabel("Fabricante:"));
        panelFormulario.add(comboFabricantes);
        panelFormulario.add(crearLabel("Categoría:"));
        panelFormulario.add(comboCategorias);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnBorrar);
        panelBotones.add(btnCancelar);

        panelPrincipal.add(lblTitulo);
        panelPrincipal.add(panelFormulario);
        panelPrincipal.add(Box.createVerticalStrut(20));
        panelPrincipal.add(panelBotones);

        add(panelPrincipal, BorderLayout.CENTER);
    }

    private void cargarDatos(int id, String nombre, String descripcion, double pvp,
                             int stock, String fabricante, String categoria) {
        txtNombre.setText(nombre);
        txtDescripcion.setText(descripcion);
        txtPvp.setText(String.format("%.2f", pvp));
        txtStock.setText(String.valueOf(stock));

        // Limpiar y cargar fabricantes
        comboFabricantes.removeAllItems();
        comboFabricantes.addItem(fabricante); // añadir el fabricante actual
        comboFabricantes.setSelectedItem(fabricante);

        // Limpiar y cargar categorías
        comboCategorias.removeAllItems();
        comboCategorias.addItem(categoria); // añadir la categoría actual
        comboCategorias.setSelectedItem(categoria);
    }

    private void configurarModo() {
        if (modoEdicion) {
            btnGuardar.setVisible(false); // solo mostrar actualizar/borrar
        } else {
            btnActualizar.setVisible(false);
            btnBorrar.setVisible(false);
        }
    }

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(COLOR_PRIMARIO);
        return label;
    }

    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField(20);
        campo.setFont(FUENTE_TEXTO);
        campo.setForeground(COLOR_TEXTO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        campo.setBackground(new Color(250, 250, 250));
        return campo;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(110, 38));

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

    // Getters públicos
    public JTextField getTxtNombre() { return txtNombre; }
    public JTextField getTxtDescripcion() { return txtDescripcion; }
    public JTextField getTxtPvp() { return txtPvp; }
    public JTextField getTxtStock() { return txtStock; }
    public JComboBox<String> getComboFabricantes() { return comboFabricantes; }
    public JComboBox<String> getComboCategorias() { return comboCategorias; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnActualizar() { return btnActualizar; }
    public JButton getBtnBorrar() { return btnBorrar; }
    public JButton getBtnCancelar() { return btnCancelar; }
}
