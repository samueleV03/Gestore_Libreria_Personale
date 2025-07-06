import builder.Libro;
import decorator.FiltroDecorator;
import decorator.FiltroVuoto;
import filtri.Filtro;
import persistenza.LibreriaPersistente;
import strategy.OrdinaLibreria;
import strategy.OrdinaPerTitolo;

import java.util.List;
import java.util.Map;

public class LibreriaImpl implements Libreria {

    LibreriaPersistente persistente;
    OrdinaLibreria criterio;
    Filtro filtro;
    FiltroDecorator filtroDecorator;


    public LibreriaImpl(LibreriaPersistente persistente) {
        this.persistente = persistente;
        //di default il criterio è ordine per titolo
        this.criterio= new OrdinaPerTitolo();
        this.filtroDecorator=new FiltroVuoto(); //filtro vuoto non filtra
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
    public void modificaLibro(Libro libro, Map<String, String> modifiche) {
        if (modifiche.containsKey("titolo")) {
            libro.setTitolo(modifiche.get("titolo"));
        }
        if (modifiche.containsKey("autore")) {
            libro.setAutore(modifiche.get("autore"));
        }
        if (modifiche.containsKey("valutazione")) {
            libro.setValutazione(Integer.parseInt(modifiche.get("anno")));
        }
        if (modifiche.containsKey("isbn")) {
            libro.setIsbn(modifiche.get("isbn"));
        }
        if (modifiche.containsKey("genere")) {
            libro.setGenere(modifiche.get("genere"));
        }
        if (modifiche.containsKey("stato")) {
            try{
                Libro.Stato stato = Libro.Stato.valueOf(modifiche.get("stato"));
                libro.setStatoLettura(stato);
            }
            catch(Exception e){
                System.out.println("Stato non valido");
            }
        }
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

    @Override
    public Libro cercaLibro(String isbn) {
        return persistente.trovaLibro(isbn);
    }


}
