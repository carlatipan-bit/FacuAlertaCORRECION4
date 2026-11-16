package uce.edu.ec.dominio.consola;

/**
 * Menu principal (nivel 0).
 */
public class Menu {
    private final Lector lector;
    private final SubMenu subMenu;

    public Menu(Lector lector) {
        this.lector = lector;
        this.subMenu = new SubMenu(lector);
    }

    public SubMenu getSubMenu() {
        return subMenu;
    }

    public void ejecutar() {
        int opcion;
        do {
            lector.mostrar("\n===== FACUALERTA =====");
            lector.mostrar("1. Gestion de usuarios");
            lector.mostrar("2. Categorias");
            lector.mostrar("3. Incidentes");
            lector.mostrar("4. Notificaciones");
            lector.mostrar("5. Iniciar sesion");
            lector.mostrar("6. Cerrar sesion");
            lector.mostrar("0. Salir");
            opcion = lector.leerOpcion("Opcion", 0, 6);
            switch (opcion) {
                case 1 -> subMenu.menuUsuarios();
                case 2 -> subMenu.menuCategorias();
                case 3 -> subMenu.menuIncidentes();
                case 4 -> subMenu.menuNotificaciones();
                case 5 -> subMenu.iniciarSesion();
                case 6 -> subMenu.cerrarSesion();
                default -> lector.mostrar("Hasta pronto.");
            }
        } while (opcion != 0);
    }
}
