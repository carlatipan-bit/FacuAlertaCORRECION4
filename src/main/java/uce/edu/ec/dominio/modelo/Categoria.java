package uce.edu.ec.dominio.modelo;

import uce.edu.ec.dominio.util.Validador;

import java.io.Serializable;

public class Categoria implements Serializable {
    private int idCategoria;
    private String nombreCategoria;
    private String descripcion;

    public Categoria() {
        this.idCategoria = 0;
        this.nombreCategoria = "";
        this.descripcion = "";
    }

    public Categoria(int idCategoria, String nombreCategoria, String descripcion) {
        setIdCategoria(idCategoria);
        setNombreCategoria(nombreCategoria);
        setDescripcion(descripcion);
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = Validador.exigirId(idCategoria, "El id de categoria");
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = Validador.exigirLetras(nombreCategoria, "El nombre de la categoria");
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = Validador.exigirTextoSinNumeros(descripcion, "La descripcion");
    }

    @Override
    public String toString() {
        return "Categoria{idCategoria=" + idCategoria + ", nombreCategoria='" + nombreCategoria
                + "', descripcion='" + descripcion + "'}";
    }
}
