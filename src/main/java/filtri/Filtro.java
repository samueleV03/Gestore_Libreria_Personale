package filtri;

import builder.Libro;

import java.util.List;

public interface Filtro {

    List<Libro> filtra(List<Libro> libri);
}
