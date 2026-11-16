package uce.edu.ec.dominio.consola;

import uce.edu.ec.dominio.modelo.Categoria;
import uce.edu.ec.dominio.modelo.Evidencia;
import uce.edu.ec.dominio.modelo.Incidente;
import uce.edu.ec.dominio.modelo.Notificacion;
import uce.edu.ec.dominio.modelo.Moderador;
import uce.edu.ec.dominio.modelo.Usuario;
import uce.edu.ec.dominio.util.Validador;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Submenus (nivel 1), uno por caso de uso. Los datos se guardan en memoria.
 */
public class SubMenu {
    private final Lector lector;
    private final IngresoUsuario ingresoUsuario;
    private final IngresoIncidente ingresoIncidente;
    private final IngresoNotificacion ingresoNotificacion;

    private final List<Usuario> usuarios = new ArrayList<>();
    private final List<Categoria> categorias = new ArrayList<>();
    private final List<Incidente> incidentes = new ArrayList<>();
    private final List<Notificacion> notificaciones = new ArrayList<>();

    private Usuario sesionActual;

    private int siguienteIdUsuario = 1;
    private int siguienteIdCategoria = 1;
    private int siguienteIdIncidente = 1;
    private int siguienteIdEvidencia = 1;
    private int siguienteIdNotificacion = 1;

    public SubMenu(Lector lector) {
        this.lector = lector;
        this.ingresoUsuario = new IngresoUsuario(lector);
        this.ingresoIncidente = new IngresoIncidente(lector);
        this.ingresoNotificacion = new IngresoNotificacion(lector);

        // Cuenta de moderacion predeterminada para iniciar sesion desde el primer uso.
        usuarios.add(new Moderador(siguienteIdUsuario++, "Moderador", "moderador@uce.edu.ec",
                "Facu2026", 1, "Validar incidentes, cambiar estados y enviar notificaciones"));

        // Cuatro categorias disponibles desde el inicio para reportar incidentes.
        categorias.add(new Categoria(siguienteIdCategoria++, "Bajo", "Incidente de riesgo bajo"));
        categorias.add(new Categoria(siguienteIdCategoria++, "Medio", "Incidente de riesgo medio"));
        categorias.add(new Categoria(siguienteIdCategoria++, "Alto", "Incidente de riesgo alto"));
        categorias.add(new Categoria(siguienteIdCategoria++, "Critico", "Incidente de riesgo critico"));
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public List<Incidente> getIncidentes() {
        return incidentes;
    }

    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    // Iniciar/cerrar sesion y separar los permisos de cada rol.
    public Usuario getSesionActual() { return sesionActual; }

    public void iniciarSesion() {
        String correo = lector.leerCorreo("Correo institucional");
        String clave = lector.leerContrasena("Contrasena (letras y numeros)");
        for (Usuario usuario : usuarios) {
            if (usuario.getCorreo().equalsIgnoreCase(correo) && usuario.getContrasenaHash().equals(clave)) {
                sesionActual = usuario;
                lector.mostrar("Sesion iniciada: " + usuario.getNombreUsuario() + " (" + usuario.getTipoUsuario() + ")");
                return;
            }
        }
        lector.mostrar("Credenciales incorrectas. No se inicio sesion.");
    }

    public void cerrarSesion() {
        sesionActual = null;
        lector.mostrar("Sesion cerrada.");
    }

    private boolean autenticado() {
        if (sesionActual != null) return true;
        lector.mostrar("Primero debe iniciar sesion.");
        return false;
    }

    private boolean esModerador() {
        if (!autenticado()) return false;
        if ("MODERADOR".equals(sesionActual.getTipoUsuario())) return true;
        lector.mostrar("Operacion exclusiva del moderador.");
        return false;
    }

    public void menuUsuarios() {
        int opcion;
        do {
            boolean moderador = sesionActual != null && "MODERADOR".equals(sesionActual.getTipoUsuario());
            boolean inicio = usuarios.stream().noneMatch(u -> "MODERADOR".equals(u.getTipoUsuario()));
            lector.mostrar("\n--- GESTION DE USUARIOS ---");
            lector.mostrar("1. Registrar estudiante");
            lector.mostrar("2. Registrar trabajador");
            if (moderador || inicio) lector.mostrar("3. Registrar moderador" + (inicio ? " (configuracion inicial)" : ""));
            if (moderador) {
                lector.mostrar("4. Registrar gestor de riesgo");
                lector.mostrar("5. Listar usuarios");
            }
            lector.mostrar("0. Volver");
            opcion = lector.leerOpcion("Opcion", 0, 5);
            switch (opcion) {
                case 1 -> registrarUsuario(ingresoUsuario.ingresarEstudiante(siguienteIdUsuario));
                case 2 -> registrarUsuario(ingresoUsuario.ingresarTrabajador(siguienteIdUsuario));
                case 3 -> {
                    if (moderador || inicio) registrarUsuario(ingresoUsuario.ingresarModerador(siguienteIdUsuario));
                    else lector.mostrar("No tiene permisos para registrar moderadores.");
                }
                case 4 -> { if (esModerador()) registrarUsuario(ingresoUsuario.ingresarGestorDeRiesgo(siguienteIdUsuario)); }
                case 5 -> { if (esModerador()) listarUsuarios(); }
                default -> { }
            }
        } while (opcion != 0);
    }

    private void listarUsuarios() {
        if (usuarios.isEmpty()) { lector.mostrar("No hay usuarios registrados."); return; }
        for (Usuario usuario : usuarios) lector.mostrar("  ID " + usuario.getIdUsuario() + " - " + usuario.getNombreUsuario() + " (" + usuario.getTipoUsuario() + ")");
    }

    private void registrarUsuario(Usuario usuario) {
        for (Usuario existente : usuarios) {
            if (existente.getCorreo().equalsIgnoreCase(usuario.getCorreo())) {
                lector.mostrar("Ya existe un usuario con ese correo. No se registro.");
                return;
            }
        }
        usuarios.add(usuario);
        siguienteIdUsuario++;
        lector.mostrar("Usuario registrado con id " + usuario.getIdUsuario() + ".");
    }

    // ------------------------------------------------------------------
    // Caso de uso: Gestionar categorias
    // ------------------------------------------------------------------
    public void menuCategorias() {
        int opcion;
        do {
            lector.mostrar("\n--- CATEGORIAS ---");
            lector.mostrar("1. Registrar categoria");
            lector.mostrar("2. Listar categorias");
            lector.mostrar("0. Volver");
            opcion = lector.leerOpcion("Opcion", 0, 2);
            if (opcion == 1) {
                if (esModerador()) {
                    Categoria categoria = ingresoIncidente.ingresarCategoria(siguienteIdCategoria++);
                    categorias.add(categoria);
                    lector.mostrar("Categoria registrada con id " + categoria.getIdCategoria() + ".");
                }
            } else if (opcion == 2) {
                listar(categorias, "No hay categorias registradas.");
            }
        } while (opcion != 0);
    }

    // ------------------------------------------------------------------
    // Caso de uso: Reportar y gestionar incidentes
    // ------------------------------------------------------------------
    public void menuIncidentes() {
        int opcion;
        do {
            lector.mostrar("\n--- INCIDENTES ---");
            lector.mostrar("1. Reportar incidente");
            lector.mostrar("2. Agregar evidencia a un incidente");
            lector.mostrar("3. Cambiar estado de un incidente");
            lector.mostrar("4. Listar incidentes");
            lector.mostrar("0. Volver");
            opcion = lector.leerOpcion("Opcion", 0, 4);
            switch (opcion) {
                case 1 -> reportarIncidente();
                case 2 -> agregarEvidencia();
                case 3 -> { if (esModerador()) cambiarEstado(); }
                case 4 -> listarIncidentesVisibles();
                default -> {
                }
            }
        } while (opcion != 0);
    }

    private void reportarIncidente() {
        if (!autenticado()) return;
        if (categorias.isEmpty()) {
            lector.mostrar("Primero un moderador debe registrar al menos una categoria.");
            return;
        }
        Categoria categoria = elegirCategoria();
        String facultad = lector.leerFacultad("Facultad donde ocurrio el incidente");
        Incidente incidente = ingresoIncidente.ingresarIncidente(siguienteIdIncidente, sesionActual, categoria, facultad);
        Evidencia evidencia = ingresoIncidente.ingresarEvidencia(siguienteIdEvidencia);
        incidente.agregarEvidencia(evidencia);
        siguienteIdEvidencia++;
        siguienteIdIncidente++;
        incidentes.add(incidente);
        lector.mostrar("Incidente reportado con id " + incidente.getIdIncidente() + ".");
    }

    private void agregarEvidencia() {
        if (!autenticado()) return;
        Incidente incidente = elegirIncidente();
        if (incidente == null) {
            return;
        }
        Evidencia evidencia = ingresoIncidente.ingresarEvidencia(siguienteIdEvidencia++);
        incidente.agregarEvidencia(evidencia);
        lector.mostrar("Evidencia agregada al incidente " + incidente.getIdIncidente() + ".");
    }

    private void cambiarEstado() {
        Incidente incidente = elegirIncidente();
        if (incidente == null) {
            return;
        }
        String nuevoEstado = lector.leerDeLista("Nuevo estado", Validador.ESTADOS_INCIDENTE);
        try {
            incidente.setEstado(nuevoEstado);
            incidente.setFechaHoraActualizacion(
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            lector.mostrar("Estado actualizado.");
        } catch (IllegalArgumentException e) {
            lector.mostrar("No se pudo cambiar el estado: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // Caso de uso: Notificaciones
    // ------------------------------------------------------------------
    public void menuNotificaciones() {
        if (!autenticado()) return;
        int opcion;
        do {
            lector.mostrar("\n--- NOTIFICACIONES ---");
            if ("MODERADOR".equals(sesionActual.getTipoUsuario())) lector.mostrar("1. Enviar notificacion");
            lector.mostrar("2. Listar notificaciones");
            lector.mostrar("0. Volver");
            opcion = lector.leerOpcion("Opcion", 0, 2);
            switch (opcion) {
                case 1 -> { if (esModerador()) enviarNotificacion(); }
                case 2 -> listarNotificacionesVisibles();
                default -> { }
            }
        } while (opcion != 0);
    }

    private void enviarNotificacion() {
        if (!esModerador()) return;
        Incidente incidente = elegirIncidente();
        if (incidente == null) return;
        Notificacion notificacion = ingresoNotificacion.ingresarNotificacion("N" + siguienteIdNotificacion, incidente);
        // No enviar a destinatarios inexistentes, ni exponer datos privados.
        String destinos = notificacion.getDestinatarioCorreo();
        if (!"@Todos".equalsIgnoreCase(destinos)) {
            for (String correo : destinos.split(",")) {
                boolean existe = usuarios.stream().anyMatch(u -> u.getCorreo().equalsIgnoreCase(correo.trim()));
                if (!existe) { lector.mostrar("Destinatario no registrado: " + correo.trim()); return; }
            }
        }
        siguienteIdNotificacion++;
        notificaciones.add(notificacion);
        lector.mostrar("Notificacion " + notificacion.getIdNotificacion() + " registrada para " + destinos + ".");
    }

    private void listarNotificacionesVisibles() {
        boolean moderador = "MODERADOR".equals(sesionActual.getTipoUsuario());
        int vistas = 0;
        for (Notificacion n : notificaciones) {
            boolean destinatario = "@Todos".equalsIgnoreCase(n.getDestinatarioCorreo());
            for (String correo : n.getDestinatarioCorreo().split(","))
                if (correo.trim().equalsIgnoreCase(sesionActual.getCorreo())) destinatario = true;
            if (moderador || destinatario) {
                lector.mostrar("  " + n.getIdNotificacion() + " - " + n.getTitulo()
                        + " - Incidente #" + n.getIncidente().getIdIncidente()
                        + " (" + n.getIncidente().getTitulo() + ")");
                vistas++;
            }
        }
        if (vistas == 0) lector.mostrar("No hay notificaciones.");
    }

    // ------------------------------------------------------------------
    // Utilitarios
    // ------------------------------------------------------------------
    private Categoria elegirCategoria() {
        for (Categoria c : categorias) lector.mostrar("  ID " + c.getIdCategoria() + " - " + c.getNombreCategoria());
        while (true) {
            int id = lector.leerEntero("Id de la categoria");
            for (Categoria categoria : categorias) {
                if (categoria.getIdCategoria() == id) {
                    return categoria;
                }
            }
            lector.mostrar("No existe una categoria con ese id.");
        }
    }

    private void listarIncidentesVisibles() {
        if (!autenticado()) return;
        int vistos = 0;
        for (Incidente i : incidentes) {
            if ("MODERADOR".equals(sesionActual.getTipoUsuario()) ||
                    i.getUsuarioReporta().getIdUsuario() == sesionActual.getIdUsuario()) {
                lector.mostrar("  ID " + i.getIdIncidente() + " - " + i.getTitulo() + " - " + i.getEstado()
                        + " - " + i.getFacultad());
                vistos++;
            }
        }
        if (vistos == 0) lector.mostrar("No hay incidentes visibles.");
    }

    private Incidente elegirIncidente() {
        if (!autenticado()) return null;
        if (incidentes.stream().noneMatch(i -> "MODERADOR".equals(sesionActual.getTipoUsuario())
                || i.getUsuarioReporta().getIdUsuario() == sesionActual.getIdUsuario())) return null;
        int id = lector.leerEntero("Id del incidente");
        for (Incidente incidente : incidentes) {
            if (incidente.getIdIncidente() == id && ("MODERADOR".equals(sesionActual.getTipoUsuario())
                    || incidente.getUsuarioReporta().getIdUsuario() == sesionActual.getIdUsuario())) return incidente;
        }
        lector.mostrar("No existe un incidente accesible con ese id.");
        return null;
    }

    private void listar(List<?> lista, String mensajeVacio) {
        if (lista.isEmpty() && !mensajeVacio.isEmpty()) {
            lector.mostrar(mensajeVacio);
            return;
        }
        for (Object elemento : lista) {
            lector.mostrar("  " + elemento);
        }
    }
}
