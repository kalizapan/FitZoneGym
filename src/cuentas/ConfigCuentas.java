package cuentas;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigCuentas {
    private static final String RUTA = "/dataaccess/cuentaAdministrador.properties";
    private Properties propiedades;

    public ConfigCuentas() {
        propiedades = new Properties();
        try (InputStream input = getClass().getResourceAsStream(RUTA)) {
            if (input == null) {
                System.out.println("Lo siento, no se pudo encontrar " + RUTA);
                return;
            }
            propiedades.load(input);
        } catch (IOException ex) {
        }
    }

    public String obtenerCorreoAdministrador() {
        return propiedades.getProperty("administrador.correo");
    }

    public String obtenerContraseniaAdministrador() {
        return propiedades.getProperty("administrador.contrasenia");
    }

    public String obtenerCorreoCoordinador() {
        return propiedades.getProperty("coordinador.correo");
    }

    public String obtenerContraseniaCoordinador() {
        return propiedades.getProperty("coordinador.contrasenia");
    }
}
