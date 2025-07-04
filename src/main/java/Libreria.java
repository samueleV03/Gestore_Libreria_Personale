import builder.Libro;
import decorator.FiltroDecorator;
import filtri.Filtro;
import strategy.OrdinaLibreria;

import java.util.List;

public interface Libreria {

    void aggiungiLibro(Libro libro);
    void rimuoviLibro(Libro libro);

    void setCriterioOrdine(OrdinaLibreria criterio);
    List<Libro> getLibriOrdinati();



    void setFiltro(Filtro filtro);
    List<Libro> getLibriFiltrati();

    void setFiltroDecorator(FiltroDecorator filtro);
    List<Libro> getLibriFiltratiDecorator();
}
