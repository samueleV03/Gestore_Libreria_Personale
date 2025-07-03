import builder.Libro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import persistenza.PersistenzaImpl;
import strategy.OrdinaPerAutore;
import strategy.OrdinaPerTitolo;
import strategy.OrdinaPerValutazione;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
//test per provare funzioni di libreria
public class TestLibreria {
//test per provare ordinamento di libreria
    @Nested
    class TestOrdinamento {
        private Libreria libreria;

        //prima di tutto
        @BeforeEach
        void setUp() {
            PersistenzaImpl persistenza = PersistenzaImpl.INSTANCE;
            persistenza.reset();
            libreria = new LibreriaImpl(persistenza);
            Libro libro1 = new Libro.Builder("Doppia Verità", "Michael Connelly", "002").Valutazione(3).build();
            Libro libro2 = new Libro.Builder("L'Alchimista", "Paulo Coelho", "003").Valutazione(5).build();
            Libro libro3 = new Libro.Builder("1984", "George Orwell", "001").Valutazione(4).build();
            libreria.aggiungiLibro(libro1);
            libreria.aggiungiLibro(libro2);
            libreria.aggiungiLibro(libro3);
        }
        //proviamo prima l'ordinamento di default, ovvero per titolo
        @Test
        void testOrdinamentoLibreriaDIDefault(){
            List<Libro> ordinati = libreria.getLibriOrdinati();
            assertEquals("1984", ordinati.get(0).getTitolo());
            assertEquals("Doppia Verità", ordinati.get(1).getTitolo());
            assertEquals("L'Alchimista", ordinati.get(2).getTitolo());
        }

        @Test
        void testOrdinamentoPerAutore() {
            OrdinaPerAutore ordinaPerAutore = new OrdinaPerAutore();
            libreria.setCriterioOrdine(ordinaPerAutore);
            List<Libro> libri = libreria.getLibriOrdinati();
            //ordina libri per autore
            assertEquals("George Orwell", libri.get(0).getAutore());
            assertEquals("Michael Connelly", libri.get(1).getAutore());
            assertEquals("Paulo Coelho", libri.get(2).getAutore());
        }

        @Test
        void testOrdinamentoPerValutazione() {
            OrdinaPerValutazione ordinaPerValutazione = new OrdinaPerValutazione();
            libreria.setCriterioOrdine(ordinaPerValutazione);
            List<Libro> libri = libreria.getLibriOrdinati();
            //ordina per valutazione
            assertEquals(5, libri.get(0).getValutazione());
            assertEquals(4, libri.get(1).getValutazione());
            assertEquals(3, libri.get(2).getValutazione());
        }

        @Test
        void testOrdinamentoPerTitolo() {
            OrdinaPerTitolo ordinaPerTitolo = new OrdinaPerTitolo();
            libreria.setCriterioOrdine(ordinaPerTitolo);
            List<Libro> libri = libreria.getLibriOrdinati();
            //ordina libri per titolo
            assertEquals("1984", libri.get(0).getTitolo());
            assertEquals("Doppia Verità", libri.get(1).getTitolo());
            assertEquals("L'Alchimista", libri.get(2).getTitolo());
        }
    }//Test ordinamento
}
