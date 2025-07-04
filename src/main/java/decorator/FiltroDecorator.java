package decorator;

import builder.Libro;

import java.util.List;

public interface FiltroDecorator {

    List<Libro> filtra(List<Libro> libri);
}
