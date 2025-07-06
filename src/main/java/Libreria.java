import builder.Libro;
import decorator.FiltroDecorator;
import filtri.Filtro;
import strategy.OrdinaLibreria;

import java.util.List;
import java.util.Map;

public interface Libreria {

    void aggiungiLibro(Libro libro);
    void rimuoviLibro(Libro libro);

    //metodo per modificare libri già presenti, gli passiamo una mappa che conterrà come chiave il nome
    //del campo che vorremmo modificare, come valore il valore del campo
    void modificaLibro(Libro libro, Map<String, String> modifiche);

    void setCriterioOrdine(OrdinaLibreria criterio);
    List<Libro> getLibriOrdinati();

    void setFiltro(Filtro filtro);
    List<Libro> getLibriFiltrati();

    void setFiltroDecorator(FiltroDecorator filtro);
    List<Libro> getLibriFiltratiDecorator();

    //restituisce il libro con quel isbn
    Libro cercaLibro(String isbn);
}
