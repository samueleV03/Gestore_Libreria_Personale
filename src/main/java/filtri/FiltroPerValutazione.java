package filtri;

import builder.Libro;

import java.util.ArrayList;
import java.util.List;

public class FiltroPerValutazione implements Filtro{

    private int valutazione;

    public FiltroPerValutazione(int v) {
        this.valutazione = v;
    }

    @Override
    public List<Libro> filtra(List<Libro> libri) {
        List<Libro> filtrati = new ArrayList<>();
        for (Libro libro : libri) {
            if (libro.getValutazione()==this.valutazione) {
                filtrati.add(libro);
            }
        }
        return filtrati;
    }
}
