package decorator;

import builder.Libro;

import java.util.ArrayList;
import java.util.List;

public class FiltroGenereDecorator extends Decorator {

    private String genere;

    public FiltroGenereDecorator(FiltroDecorator filtro, String genere) {
        super(filtro);
        this.genere = genere;
    }

    @Override
    public List<Libro> filtra(List<Libro> libri) {
        List<Libro> precedenti = super.filtra(libri); //dobbiamo applicare i filtri precedenti
        List<Libro> filtro = filtraPerGenere(precedenti);
        return filtro;
    }

    private List<Libro> filtraPerGenere(List<Libro> libri) {
        List<Libro> filtrati = new ArrayList<>();
        for (Libro libro : libri) {
            if (libro.getGenere().equalsIgnoreCase(genere)) {
                filtrati.add(libro);
            }
        }
        return filtrati;
    }
}
