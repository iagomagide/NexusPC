package controlador;

import criteria.ProductoCriteria;
import model.Producto;
import model.Fabricante;
import model.Categoria;
import service.CatalogoService;
import vista.ProductoView;
import vista.VentanaEdicionProducto;

import javax.swing.*;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import javax.swing.*;
import java.awt.event.*;


public class ProductoController {
    private final ProductoView productoView;
    private final CatalogoService catalogoService;
    private List<Fabricante> fabricantes;
    private List<Categoria> categorias;

    public ProductoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
        this.productoView = new ProductoView();

        setupEventListeners();
        cargarCombos();
        cargarTodosLosProductos();
    }

    // ------------------------------------------
    // EVENTOS PRINCIPALES
    // ------------------------------------------
    private void setupEventListeners() {
        // Buscar productos
        productoView.getBtnBuscar().addActionListener(e -> buscarProductos());

        // Limpiar filtros
        productoView.getBtnLimpiar().addActionListener(e -> {
            productoView.limpiarCampos();
            cargarTodosLosProductos();
        });

        // Añadir nuevo producto
        productoView.getBtnAnadir().addActionListener(e -> abrirVentanaNuevoProducto());

        // Doble clic en tabla → editar producto
        productoView.getTabla().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int fila = productoView.getTabla().getSelectedRow();
                    if (fila >= 0) {
                        abrirVentanaEdicionProducto(fila);
                    }
                }
            }
        });

        // Refrescar tabla (desde eventos externos)
        productoView.addPropertyChangeListener("tablaRefrescar", evt -> cargarTodosLosProductos());
    }

    // ------------------------------------------
    // CARGA DE DATOS EN COMBOS
    // ------------------------------------------
    private void cargarCombos() {
        try {
            fabricantes = catalogoService.recuperarTodosLosFabricantes();
            categorias = catalogoService.recuperarTodasLasCategorias();

            productoView.cargarFabricantes(fabricantes.stream().map(Fabricante::getNombre).toList());
            productoView.cargarCategorias(categorias.stream().map(Categoria::getNombre).toList());
        } catch (Exception e) {
            mostrarError("Error al cargar datos: " + e.getMessage());
        }
    }

    // ------------------------------------------
    // BÚSQUEDA DE PRODUCTOS
    // ------------------------------------------
    private void buscarProductos() {
        try {
            ProductoCriteria criteria = new ProductoCriteria();

            String idTxt = productoView.getTxtId().getText().trim();
            if (!idTxt.isEmpty()) criteria.setId(Integer.parseInt(idTxt));

            String nombre = productoView.getTxtNombre().getText().trim();
            if (!nombre.isEmpty()) criteria.setNombreProducto(nombre);

            String fabricanteSel = (String) productoView.getComboFabricantes().getSelectedItem();
            if (!"Todos los fabricantes".equals(fabricanteSel)) {
                fabricantes.stream()
                        .filter(f -> f.getNombre().equals(fabricanteSel))
                        .findFirst()
                        .ifPresent(f -> criteria.setIdFabricante(f.getId()));
            }

            String categoriaSel = (String) productoView.getComboCategorias().getSelectedItem();
            if (!"Todas las categorías".equals(categoriaSel)) {
                categorias.stream()
                        .filter(c -> c.getNombre().equals(categoriaSel))
                        .findFirst()
                        .ifPresent(c -> criteria.setIdCategoria(c.getId()));
            }

            List<Producto> productos = catalogoService.buscarProductosPorCriteria(criteria);
            actualizarTabla(productos);

        } catch (Exception e) {
            mostrarError("Error al buscar productos: " + e.getMessage());
        }
    }

    // ------------------------------------------
    // NUEVO PRODUCTO
    // ------------------------------------------
    private void abrirVentanaNuevoProducto() {
        VentanaEdicionProducto ventana = new VentanaEdicionProducto(); // modo nuevo
        configurarVentanaEdicion(ventana, null);
        ventana.setVisible(true);
    }

    // ------------------------------------------
    // EDITAR PRODUCTO EXISTENTE
    // ------------------------------------------
    private void abrirVentanaEdicionProducto(int fila) {
        try {
            int id = (int) productoView.getModeloTabla().getValueAt(fila, 0);
            Optional<Producto> productoOpt = catalogoService.buscarProductoPorId(id);

            if (productoOpt.isPresent()) {
                Producto producto = productoOpt.get();

                VentanaEdicionProducto ventana = new VentanaEdicionProducto(
                        producto.getId(),
                        producto.getNombreProducto(),
                        producto.getDescripcion(),
                        producto.getPvp(),
                        producto.getStock(),
                        producto.getFabricante() != null ? producto.getFabricante().getNombre() : "",
                        producto.getCategoria() != null ? producto.getCategoria().getNombre() : "",
                        fabricantes.stream().map(Fabricante::getNombre).toList(),
                        categorias.stream().map(Categoria::getNombre).toList()
                );

                configurarVentanaEdicion(ventana, producto);
                ventana.setVisible(true);
            }
        } catch (Exception e) {
            mostrarError("Error al abrir producto: " + e.getMessage());
        }
    }

    // ------------------------------------------
    // CONFIGURACIÓN DE LA VENTANA DE EDICIÓN
    // ------------------------------------------
    private void configurarVentanaEdicion(VentanaEdicionProducto ventana, Producto producto) {
        cargarCombosVentanaEdicion(ventana);

        if (producto == null) {
            // GUARDAR NUEVO
            ventana.getBtnGuardar().addActionListener(e -> crearProducto(ventana));
        } else {
            // ACTUALIZAR / BORRAR
            ventana.getBtnActualizar().addActionListener(e -> actualizarProducto(producto, ventana));
            ventana.getBtnBorrar().addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(ventana,
                        "¿Desea borrar este producto?",
                        "Confirmar borrado",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) borrarProducto(producto.getId(), ventana);
            });
        }

        ventana.getBtnCancelar().addActionListener(e -> ventana.dispose());
    }

    private void cargarCombosVentanaEdicion(VentanaEdicionProducto ventana) {
        ventana.getComboFabricantes().removeAllItems();
        fabricantes.forEach(f -> ventana.getComboFabricantes().addItem(f.getNombre()));

        ventana.getComboCategorias().removeAllItems();
        categorias.forEach(c -> ventana.getComboCategorias().addItem(c.getNombre()));
    }

    // ------------------------------------------
    // CRUD: CREAR / ACTUALIZAR / BORRAR
    // ------------------------------------------
    private void crearProducto(VentanaEdicionProducto ventana) {
        try {
            Producto nuevo = construirProductoDesdeVentana(ventana);
            boolean ok = catalogoService.crearProducto(nuevo);

            if (ok) {
                JOptionPane.showMessageDialog(ventana, "Producto creado correctamente");
                ventana.dispose();
                productoView.refrescarTabla();
            } else mostrarError("Error al crear el producto");
        } catch (Exception e) {
            mostrarError("Error al crear producto: " + e.getMessage());
        }
    }

    private void actualizarProducto(Producto producto, VentanaEdicionProducto ventana) {
        try {
            Producto actualizado = construirProductoDesdeVentana(ventana);
            actualizado.setId(producto.getId());

            boolean ok = catalogoService.modificarProducto(actualizado);

            if (ok) {
                JOptionPane.showMessageDialog(ventana, "Producto actualizado correctamente");
                ventana.dispose();
                productoView.refrescarTabla();
            } else mostrarError("Error al actualizar el producto");
        } catch (Exception e) {
            mostrarError("Error al actualizar producto: " + e.getMessage());
        }
    }

    private void borrarProducto(int id, VentanaEdicionProducto ventana) {
        try {
            boolean ok = catalogoService.borrarProducto(id);
            if (ok) {
                JOptionPane.showMessageDialog(ventana, "Producto borrado correctamente");
                ventana.dispose();
                productoView.refrescarTabla();
            } else mostrarError("Error al borrar el producto");
        } catch (Exception e) {
            mostrarError("Error al borrar producto: " + e.getMessage());
        }
    }

    // ------------------------------------------
    // UTILIDADES
    // ------------------------------------------
    private Producto construirProductoDesdeVentana(VentanaEdicionProducto ventana) {
        Producto p = new Producto();
        p.setNombreProducto(ventana.getTxtNombre().getText().trim());
        p.setDescripcion(ventana.getTxtDescripcion().getText().trim());
        p.setPvp(Double.parseDouble(ventana.getTxtPvp().getText().trim()));
        p.setStock(Integer.parseInt(ventana.getTxtStock().getText().trim()));

        String fabSel = (String) ventana.getComboFabricantes().getSelectedItem();
        fabricantes.stream()
                .filter(f -> f.getNombre().equals(fabSel))
                .findFirst()
                .ifPresent(p::setFabricante);

        String catSel = (String) ventana.getComboCategorias().getSelectedItem();
        categorias.stream()
                .filter(c -> c.getNombre().equals(catSel))
                .findFirst()
                .ifPresent(p::setCategoria);

        return p;
    }

    public void cargarTodosLosProductos() {
        try {
            List<Producto> productos = catalogoService.buscarProductosPorCriteria(new ProductoCriteria());
            actualizarTabla(productos);
        } catch (Exception e) {
            mostrarError("Error al cargar productos: " + e.getMessage());
        }
    }

    private void actualizarTabla(List<Producto> productos) {
        var modelo = productoView.getModeloTabla();
        modelo.setRowCount(0);

        for (Producto p : productos) {
            modelo.addRow(new Object[]{
                    p.getId(),
                    p.getNombreProducto(),
                    p.getDescripcion(),
                     p.getPvp(),
                    p.getStock(),
                    p.getFabricante() != null ? p.getFabricante().getNombre() : "N/A",
                    p.getCategoria() != null ? p.getCategoria().getNombre() : "N/A"
            });
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(productoView, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public ProductoView getVista() {
        return productoView;
    }
}
