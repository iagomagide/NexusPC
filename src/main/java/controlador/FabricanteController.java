package controlador;


import criteria.FabricanteCriteria;
import model.Fabricante;
import service.CatalogoService;
import vista.FabricanteView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

public class FabricanteController {
    private FabricanteView fabricanteView;
    private CatalogoService catalogoService;
    private Connection connection;

    public FabricanteController(CatalogoService catalogoService) {
        this.fabricanteView = new FabricanteView();
        this.catalogoService = catalogoService;
        this.connection = connection;

        setupEventListeners();
        cargarTodosLosFabricantes();
    }

    private void setupEventListeners() {
        fabricanteView.getBtnBuscar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarFabricantes();
            }
        });

        fabricanteView.getBtnLimpiar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fabricanteView.limpiarCampos();
                cargarTodosLosFabricantes();
            }
        });
    }

    private void buscarFabricantes() {
        try {
            FabricanteCriteria criteria = new FabricanteCriteria();

            String idText = fabricanteView.getTxtId().getText().trim();
            if (!idText.isEmpty()) {
                criteria.setId(Integer.parseInt(idText));
            }

            String nombre = fabricanteView.getTxtNombre().getText().trim();
            if (!nombre.isEmpty()) {
                criteria.setNombre(nombre);
            }

            List<Fabricante> fabricantes = catalogoService.buscarFabricantesPorCriteria(criteria);
            actualizarTabla(fabricantes);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(fabricanteView, "ID debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(fabricanteView, "Error al buscar fabricantes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cargarTodosLosFabricantes() {
        List<Fabricante> fabricantes = catalogoService.recuperarTodosLosFabricantes();
        actualizarTabla(fabricantes);
    }

    private void actualizarTabla(List<Fabricante> fabricantes) {
        fabricanteView.getModeloTabla().setRowCount(0);

        for (Fabricante fabricante : fabricantes) {
            Object[] fila = {
                    fabricante.getId(),
                    fabricante.getNombre()
            };
            fabricanteView.getModeloTabla().addRow(fila);
        }
    }

    // Nuevo método para obtener la vista
    public FabricanteView getVista() {
        return fabricanteView;
    }
}