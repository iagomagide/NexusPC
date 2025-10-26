


import controlador.MainController;
import service.*;
import util.Configurador;
import vista.MainView;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class App {
    public static void main(String[] args) {
        try {
            // Establecer look and feel
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    iniciarAplicacion();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al iniciar la aplicación: " + e.getMessage());
        }
    }

    private static void iniciarAplicacion() {
            /*===============ZONA DE CÓDIGO MODIFICABLE===============*/
            // Inicializar DAOs

            // Inicializar servicio
            CatalogoService catalogoService = new CatalogoServiceImplMock();
            PedidoService pedidoService = new PedidoServiceImplMock(catalogoService);
            /*===============FIN ZONA DE CÓDIGO MODIFICABLE===============*/
            // Inicializar vista principal
            MainView mainView = new MainView();

            // Inicializar controlador principal
            MainController mainController = new MainController(mainView, catalogoService, pedidoService);

            // Mostrar aplicación
            mainController.mostrarVista();

    }
}