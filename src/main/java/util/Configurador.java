package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Configurador {
    private static Properties properties = new Properties();

    static {
        cargarConfiguracion();
    }

    public static void cargarConfiguracion() {
        //Este método aplica SOLO para lectua, NO para escritura
        //Lectura mas robusta: Funciona en JARSy no depende de rutas absolitas o relativas
        try (InputStream iStream = Configurador.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (iStream == null) {
                System.out.println("No se encuentra fichero configuración");
                return;
            }
            // Cargar las propiedades desde el archivo de propiedades
            properties.load(iStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String leerPropiedad(String key) {
        return properties.getProperty(key);
    }

    public static Connection getConnection() throws SQLException {
        try {
            // Cargar el driver de MariaDB
            Class.forName("org.mariadb.jdbc.Driver");
            String url = leerPropiedad("servidor_url") + leerPropiedad("base_datos");
            String user = leerPropiedad("usuario");
            String pass = leerPropiedad("clave");
            // Obtener conexión usando DriverManager
            return DriverManager.getConnection(
                    url,
                    user,
                    pass
            );
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver de MariaDB no encontrado", e);
        }
    }


}