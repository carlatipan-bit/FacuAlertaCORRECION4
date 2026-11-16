package uce.edu.ec.dominio.modelo;

import uce.edu.ec.dominio.util.Validador;

public class Estudiante extends Usuario {
    private int idEstudiante;
    private String facultad;
    private int semestre;

    public Estudiante() {
        super();
        setTipoUsuario("ESTUDIANTE");
        this.idEstudiante = 0;
        this.facultad = "";
        this.semestre = 0;
    }

    public Estudiante(int idUsuario, String nombreUsuario, String correo, String contrasenaHash, int idEstudiante,
                      String facultad, int semestre) {
        super(idUsuario, nombreUsuario, correo, contrasenaHash, "ESTUDIANTE");
        setIdEstudiante(idEstudiante);
        setFacultad(facultad);
        setSemestre(semestre);
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = Validador.exigirId(idEstudiante, "El id de estudiante");
    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = Validador.exigirFacultad(facultad, "La facultad");
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = Validador.exigirRango(semestre, 1, 12, "El semestre");
    }

    // Validacion de fondo: el tipo de usuario de esta clase no puede cambiarse
    @Override
    public void setTipoUsuario(String tipoUsuario) {
        Validador.exigirDeLista(tipoUsuario, new String[]{"ESTUDIANTE"}, "El tipo de usuario de Estudiante");
        super.setTipoUsuario(tipoUsuario);
    }

    @Override
    public String toString() {
        return super.toString() + " Estudiante{idEstudiante=" + idEstudiante + ", facultad='" + facultad
                + "', semestre=" + semestre + "}";
    }
}
