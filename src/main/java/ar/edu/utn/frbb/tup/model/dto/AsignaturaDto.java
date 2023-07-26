package ar.edu.utn.frbb.tup.model.dto;

public class AsignaturaDto {
    private int nota;

    private String condicion;

    public void setNota(int nota) {
        this.nota = nota;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public int getNota() {
        return nota;
    }

    public String getCondicion() {
        return this.condicion;
    }


}
