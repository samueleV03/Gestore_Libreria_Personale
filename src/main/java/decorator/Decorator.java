package decorator;

import builder.Libro;

import java.util.List;

abstract public class Decorator implements FiltroDecorator {

    private FiltroDecorator filtro;
    public Decorator(FiltroDecorator filtro) {this.filtro=filtro;}

    public List<Libro> filtra(List<Libro> libri) {
        List<Libro> ret=filtro.filtra(libri);
        return ret;
    }
}
