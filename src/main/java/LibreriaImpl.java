import builder.Libro;
import persistenza.LibreriaPersistente;
import strategy.OrdinaLibreria;
import strategy.OrdinaPerTitolo;

import java.util.List;

public class LibreriaImpl implements Libreria {

    LibreriaPersistente persistente;
    OrdinaLibreria criterio;

    public LibreriaImpl(LibreriaPersistente persistente) {
        this.persistente = persistente;
        //di default il criterio è ordine per titolo
        this.criterio= new OrdinaPerTitolo();
    }

    @Override
    public void aggiungiLibro(Libro libro) {
        persistente.salvaLibro(libro);
    }

    @Override
    public void rimuoviLibro(Libro libro) {
        persistente.rimuoviLibro(libro);
    }

    @Override
    public void setCriterioOrdine(OrdinaLibreria criterio) {
        this.criterio = criterio;
    }

    @Override
    public List<Libro> getLibriOrdinati() {
        List<Libro> libri = persistente.ottieniLibri();
        criterio.ordina(libri);
        return libri;
    }

    @Override
    public void aggiungiFiltro() {

    }

}
