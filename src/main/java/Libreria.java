import builder.Libro;
import strategy.OrdinaLibreria;

import java.util.List;

public interface Libreria {

    void aggiungiLibro(Libro libro);
    void rimuoviLibro(Libro libro);

    void setCriterioOrdine(OrdinaLibreria criterio);
    List<Libro> getLibriOrdinati();

    void aggiungiFiltro();
}
