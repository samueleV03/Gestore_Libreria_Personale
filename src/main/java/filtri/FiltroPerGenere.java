package filtri;

import builder.Libro;

import java.util.ArrayList;
import java.util.List;

public class FiltroPerGenere implements Filtro{

    private String genere;

    public FiltroPerGenere(String genere) {
        this.genere = genere;
    }

    @Override
    public List<Libro> filtra(List<Libro> libri) {
        List<Libro> filtrati = new ArrayList<>();
        for (Libro libro : libri) {
            if (libro.getGenere().equalsIgnoreCase(genere)) {
                filtrati.add(libro);
            }
        }
        return filtrati;
    }
}
