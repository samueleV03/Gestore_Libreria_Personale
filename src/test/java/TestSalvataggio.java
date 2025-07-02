import builder.Libro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistenza.PersistenzaImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestSalvataggio{
//test per provare persistenza

        private PersistenzaImpl persistenza;

        //prima di tutto
        @BeforeEach
        void setUp() {
                persistenza = PersistenzaImpl.INSTANCE;
                persistenza.reset();
        }


        @Test
        void testRimuoviLibro() {
                Libro libro1 = new Libro.Builder("Doppia Verità", "Michael Connelly", "123")
                        .Valutazione(4)
                        .Stato(Libro.Stato.InLettura)
                        .Genere("Poliziesco")
                        .build();
                persistenza.salvaLibro(libro1);
                List<Libro> libri = persistenza.ottieniLibri();
                assertEquals(1, libri.size(), "dovrebbe esserci un solo libro");
                persistenza.rimuoviLibro(libro1);
                libri = persistenza.ottieniLibri();
                assertEquals(0, libri.size(), "dovrebbero esserci 0 libri");
        }

        @Test
        void testInserisciLibro() {
                Libro libro1 = new Libro.Builder("Doppia Verità", "Michael Connelly", "123")
                        .Valutazione(4)
                        .Stato(Libro.Stato.InLettura)
                        .Genere("Poliziesco")
                        .build();
                //salvataggio libro
                persistenza.salvaLibro(libro1);
                //ottiengo lista libri e controllo che ci sia uno solo
                List<Libro> libri = persistenza.ottieniLibri();
                assertEquals(1, libri.size(), "Dovrebbe esserci un solo libro salvato");
                Libro salvato = libri.get(0);
                //controllo i campi
                assertEquals("Doppia Verità", salvato.getTitolo());
                assertEquals("Michael Connelly", salvato.getAutore());
                assertEquals("123", salvato.getIsbn());
                assertEquals(4, salvato.getValutazione());
                assertEquals(Libro.Stato.InLettura, salvato.getStatoLettura());
                assertEquals("Poliziesco", salvato.getGenere());
        }

        @Test
        void testRimuoviUnoTraDueLibri() {
                Libro libro1 = new Libro.Builder("Doppia Verità", "Michael Connelly", "123")
                        .Valutazione(4)
                        .Stato(Libro.Stato.InLettura)
                        .Genere("Poliziesco")
                        .build();
                Libro libro2 = new Libro.Builder("1984", "George Orwell", "456")
                        .Valutazione(5)
                        .Stato(Libro.Stato.Letto)
                        .Genere("Thriller")
                        .build();
                persistenza.salvaLibro(libro1);
                persistenza.salvaLibro(libro2);
                List<Libro> libri = persistenza.ottieniLibri();
                assertEquals(2, libri.size(), "dovrebbero esserci due libri");
                persistenza.rimuoviLibro(libro1);
                libri = persistenza.ottieniLibri();
                assertEquals(1, libri.size(), "dovrebbe esserci un solo libro");
                assertFalse(libri.contains(libro1), "il libro rimosso non dovrebbe essere presente");
                assertTrue(libri.contains(libro2), "l'altro libro dovrebbe essere presente");
        }

}

