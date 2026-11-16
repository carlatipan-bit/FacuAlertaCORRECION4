package uce.edu.ec.dominio.consola;

import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.*;

public class MenuTest {

    @Test
    public void testSalirDirectamente() {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        Menu menu = new Menu(LectorTest.crear("0\n", salida));
        menu.ejecutar();
        assertTrue(salida.toString().contains("FACUALERTA"));
        assertTrue(salida.toString().contains("Hasta pronto"));
    }

    @Test
    public void testNavegaPorTodosLosSubmenus() {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        Menu menu = new Menu(LectorTest.crear("1\n0\n2\n0\n3\n0\n4\n0\n9\n0\n", salida));
        menu.ejecutar();
        String texto = salida.toString();
        assertTrue(texto.contains("GESTION DE USUARIOS"));
        assertTrue(texto.contains("CATEGORIAS"));
        assertTrue(texto.contains("INCIDENTES"));
        assertTrue(texto.contains("Primero debe iniciar sesion"));
        assertNotNull(menu.getSubMenu());
    }
}
