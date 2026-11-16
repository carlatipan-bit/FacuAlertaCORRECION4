package uce.edu.ec.dominio.modelo;

import uce.edu.ec.dominio.util.Validador;

public class Moderador extends Usuario {
    private int idModerador;
    private String permisos;

    public Moderador() {
        super();
        setTipoUsuario("MODERADOR");
        this.idModerador = 0;
        this.permisos = "";
    }

    public Moderador(int idUsuario, String nombreUsuario, String correo, String contrasenaHash, int idModerador,
                     String permisos) {
        super(idUsuario, nombreUsuario, correo, contrasenaHash, "MODERADOR");
        setIdModerador(idModerador);
        setPermisos(permisos);
    }

    public int getIdModerador() {
        return idModerador;
    }

    public void setIdModerador(int idModerador) {
        this.idModerador = Validador.exigirId(idModerador, "El id de moderador");
    }

    public String getPermisos() {
        return permisos;
    }

    public void setPermisos(String permisos) {
        this.permisos = Validador.exigirTexto(permisos, "Los permisos");
    }

    // Validacion de fondo: el tipo de usuario de esta clase no puede cambiarse
    @Override
    public void setTipoUsuario(String tipoUsuario) {
        Validador.exigirDeLista(tipoUsuario, new String[]{"MODERADOR"}, "El tipo de usuario de Moderador");
        super.setTipoUsuario(tipoUsuario);
    }

    @Override
    public String toString() {
        return super.toString() + " Moderador{idModerador=" + idModerador + ", permisos='" + permisos + "'}";
    }
}
