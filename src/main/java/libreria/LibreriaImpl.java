package libreria;
import builder.Libro;
import decorator.FiltroDecorator;
import decorator.FiltroVuoto;
import observer.Observer;
import observer.Subject;
import persistenza.LibreriaPersistente;
import strategy.OrdinaLibreria;
import strategy.OrdinaPerTitolo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LibreriaImpl implements Libreria,Subject {

    LibreriaPersistente persistente;
    OrdinaLibreria criterio;
    FiltroDecorator filtroDecorator;
    private List<Observer> osservatori;


    public LibreriaImpl(LibreriaPersistente persistente) {
        this.persistente = persistente;
        //di default il criterio è ordine per titolo
        this.criterio= new OrdinaPerTitolo();
        this.filtroDecorator=new FiltroVuoto(); //filtro vuoto non filtra
        osservatori= new ArrayList<>();
    }

    @Override
    public boolean aggiungiLibro(Libro libro) {
        boolean aggiunto=persistente.salvaLibro(libro);
        notifyObservers();
        return aggiunto;
    }

    @Override
    public boolean rimuoviLibro(Libro libro) {
        boolean ret=persistente.rimuoviLibro(libro);
        notifyObservers();
        return ret;
    }

    //l'unico campo non modificabile, per scelta progettuale è isbn
    @Override
    public void modificaLibro(Libro libro, Map<String, String> modifiche) {
        if (modifiche.containsKey("titolo")) {
            libro.setTitolo(modifiche.get("titolo"));
        }
        if (modifiche.containsKey("autore")) {
            libro.setAutore(modifiche.get("autore"));
        }
        if (modifiche.containsKey("valutazione")) {
            libro.setValutazione(Integer.parseInt(modifiche.get("valutazione")));
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
        aggiornaLibro(libro);
        notifyObservers();
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


    //aggiungiamo i metodi dell'observer
    @Override
    public void attach(Observer o) {
        osservatori.add(o);
    }

    @Override
    public void detach(Observer o) {
        osservatori.remove(o);
    }

    @Override
    public void notifyObservers() {
        ArrayList<Observer> copia=new ArrayList<>(osservatori);
        for (Observer o : copia) {
            try {
                o.update();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //serve per far applicare le modifiche ai libri salvati
    public void aggiornaLibro(Libro libro) {
        persistente.rimuoviLibro(libro);
        persistente.salvaLibro(libro);
    }
}
