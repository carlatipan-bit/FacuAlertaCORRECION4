package uce.edu.ec.dominio.modelo;

import uce.edu.ec.dominio.util.Validador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Incidente implements Serializable {
    private int idIncidente;
    private String fechaHoraReporte;
    private String fechaHoraActualizacion;
    private String estado;
    private String titulo;
    private String descripcion;
    private String nivelGravedad;

    // Relaciones de asociacion (se representan como atributos de objeto)
    private Usuario usuarioReporta;          // Incidente * --- 1 Usuario
    private Categoria categoria;             // Incidente * --- 1 Categoria
    private String facultad;                // Facultad donde se reporta el incidente
    private List<Evidencia> evidencias;      // Incidente 1 --- * Evidencia

    public Incidente() {
        this.idIncidente = 0;
        this.fechaHoraReporte = "";
        this.fechaHoraActualizacion = "";
        this.estado = "";
        this.titulo = "";
        this.descripcion = "";
        this.nivelGravedad = "";
        this.usuarioReporta = new Usuario();
        this.categoria = new Categoria();
        this.facultad = "";
        this.evidencias = new ArrayList<>();
    }

    public Incidente(int idIncidente, String fechaHoraReporte, String fechaHoraActualizacion, String estado,
                     String titulo, String descripcion, String nivelGravedad, Usuario usuarioReporta,
                     Categoria categoria, String facultad) {
        this.evidencias = new ArrayList<>();
        setIdIncidente(idIncidente);
        setFechaHoraReporte(fechaHoraReporte);
        setFechaHoraActualizacion(fechaHoraActualizacion);
        setEstado(estado);
        setTitulo(titulo);
        setDescripcion(descripcion);
        setNivelGravedad(nivelGravedad);
        setUsuarioReporta(usuarioReporta);
        setCategoria(categoria);
        setFacultad(facultad);
    }

    public int getIdIncidente() {
        return idIncidente;
    }

    public void setIdIncidente(int idIncidente) {
        this.idIncidente = Validador.exigirId(idIncidente, "El id de incidente");
    }

    public String getFechaHoraReporte() {
        return fechaHoraReporte;
    }

    public void setFechaHoraReporte(String fechaHoraReporte) {
        String nueva = Validador.exigirFechaHoraNoFutura(fechaHoraReporte, "La fecha y hora de reporte");
        // Validacion de fondo: el reporte no puede ser posterior a la actualizacion
        if (this.fechaHoraActualizacion != null && !this.fechaHoraActualizacion.isEmpty()) {
            Validador.exigirNoAnterior(this.fechaHoraActualizacion, nueva,
                    "La fecha y hora de actualizacion", "la fecha y hora de reporte");
        }
        this.fechaHoraReporte = nueva;
    }

    public String getFechaHoraActualizacion() {
        return fechaHoraActualizacion;
    }

    public void setFechaHoraActualizacion(String fechaHoraActualizacion) {
        String nueva = Validador.exigirFechaHoraNoFutura(fechaHoraActualizacion,
                "La fecha y hora de actualizacion");
        // Validacion de fondo: la actualizacion no puede ser anterior al reporte
        Validador.exigirNoAnterior(nueva, this.fechaHoraReporte,
                "La fecha y hora de actualizacion", "la fecha y hora de reporte");
        this.fechaHoraActualizacion = nueva;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        String nuevo = Validador.exigirDeLista(estado, Validador.ESTADOS_INCIDENTE, "El estado");
        if ("RESUELTO".equals(this.estado) && !"RESUELTO".equals(nuevo))
            throw new IllegalArgumentException("Un incidente resuelto no puede reabrirse");
        this.estado = nuevo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = Validador.exigirTitulo(titulo, "El titulo");
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = Validador.exigirTitulo(descripcion, "La descripcion");
    }

    public String getNivelGravedad() {
        return nivelGravedad;
    }

    public void setNivelGravedad(String nivelGravedad) {
        this.nivelGravedad = Validador.exigirDeLista(nivelGravedad, Validador.NIVELES_GRAVEDAD, "El nivel de gravedad");
    }

    public Usuario getUsuarioReporta() {
        return usuarioReporta;
    }

    public void setUsuarioReporta(Usuario usuarioReporta) {
        this.usuarioReporta = Validador.exigirNoNulo(usuarioReporta, "El usuario que reporta");
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = Validador.exigirNoNulo(categoria, "La categoria");
    }

    public String getFacultad() { return facultad; }

    public void setFacultad(String facultad) {
        this.facultad = Validador.exigirFacultad(facultad, "La facultad");
    }

    public List<Evidencia> getEvidencias() {
        return Collections.unmodifiableList(evidencias);
    }

    public void setEvidencias(List<Evidencia> evidencias) {
        this.evidencias = new ArrayList<>(Validador.exigirNoNulo(evidencias, "La lista de evidencias"));
    }

    public void agregarEvidencia(Evidencia evidencia) {
        this.evidencias.add(Validador.exigirNoNulo(evidencia, "La evidencia"));
    }

    @Override
    public String toString() {
        return "Incidente{idIncidente=" + idIncidente + ", fechaHoraReporte='" + fechaHoraReporte
                + "', fechaHoraActualizacion='" + fechaHoraActualizacion + "', estado='" + estado
                + "', titulo='" + titulo + "', descripcion='" + descripcion + "', nivelGravedad='" + nivelGravedad
                + "', idUsuarioReporta=" + usuarioReporta.getIdUsuario()
                + ", categoria='" + categoria.getNombreCategoria()
                + "', facultad='" + facultad
                + "', totalEvidencias=" + evidencias.size() + "}";
    }
}
