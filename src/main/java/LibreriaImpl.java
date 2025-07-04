import builder.Libro;
import decorator.FiltroDecorator;
import filtri.Filtro;
import persistenza.LibreriaPersistente;
import strategy.OrdinaLibreria;
import strategy.OrdinaPerTitolo;

import java.util.List;

public class LibreriaImpl implements Libreria {

    LibreriaPersistente persistente;
    OrdinaLibreria criterio;
    Filtro filtro;
    FiltroDecorator filtroDecorator;

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
    public void setFiltro(Filtro filtro) {
        this.filtro = filtro;
    }

    @Override
    public List<Libro> getLibriFiltrati() {
        List<Libro> libri=persistente.ottieniLibri();
        List<Libro> ret=filtro.filtra(libri);
        return ret;
    }

    @Override
    public void setFiltroDecorator(FiltroDecorator filtro) {
        this.filtroDecorator =filtro;
    }

    @Override
    public List<Libro> getLibriFiltratiDecorator() {
        List<Libro> libri=persistente.ottieniLibri();
        List<Libro> ret= filtroDecorator.filtra(libri);
        return ret;
    }


}
