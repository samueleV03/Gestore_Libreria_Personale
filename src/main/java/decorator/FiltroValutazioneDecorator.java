package decorator;

import builder.Libro;

import java.util.ArrayList;
import java.util.List;

public class FiltroValutazioneDecorator extends Decorator {

    private int valutazione;

    public FiltroValutazioneDecorator(FiltroDecorator filtro, int val) {
        super(filtro);
        this.valutazione = val;
    }

    @Override
    public List<Libro> filtra(List<Libro> libri) {
        List<Libro> precedenti = super.filtra(libri); //dobbiamo applicare i filtri precedenti
        List<Libro> filtro = filtraPerValutazione(precedenti);
        return filtro;
    }

    private List<Libro> filtraPerValutazione(List<Libro> libri) {
        List<Libro> filtrati = new ArrayList<>();
        for (Libro libro : libri) {
            if (libro.getValutazione()==this.valutazione) {
                filtrati.add(libro);
            }
        }
        return filtrati;
    }
}
