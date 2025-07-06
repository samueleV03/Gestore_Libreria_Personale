import builder.Libro;
import decorator.*;
import filtri.FiltroPerAutore;
import filtri.FiltroPerGenere;
import filtri.FiltroPerValutazione;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import persistenza.PersistenzaImpl;
import strategy.OrdinaPerAutore;
import strategy.OrdinaPerTitolo;
import strategy.OrdinaPerValutazione;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
//test per provare funzioni di libreria
public class TestLibreria {
    //test per provare funzioni base della libreria
    @Nested
    class TestComandiLibreria {
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

        void cercaLibro(){
            Libro l= libreria.cercaLibro("002");
            assertEquals("1984", l.getTitolo());

        }
        void provaModifiche()
        {
            HashMap<String, String> modifiche=new HashMap<>();
            modifiche.put("valutazione", "1");
            Libro l= libreria.cercaLibro("002"); //vogliamo cambiare la valutazione di Doppia Verità
            libreria.modificaLibro(l, modifiche);
            assertEquals(1, libreria.cercaLibro("002").getValutazione());

            modifiche.clear();
            modifiche.put("titolo", "1984 edizione speciale");
            Libro l2= libreria.cercaLibro("001");
            libreria.modificaLibro(l2, modifiche);
            assertEquals("1984 edizione speciale", libreria.cercaLibro("001").getTitolo());

            modifiche.clear();
            modifiche.put("stato", "DaLeggere");
            Libro l3= libreria.cercaLibro("003");
            libreria.modificaLibro(l3, modifiche);
            assertEquals("DaLeggere", String.valueOf(libreria.cercaLibro("003").getStatoLettura()));
        }
    }
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

    @Nested
    class TestFiltri{
        private Libreria libreria;

        //prima di tutto
        @BeforeEach
        void setUp() {
            PersistenzaImpl persistenza = PersistenzaImpl.INSTANCE;
            persistenza.reset();
            libreria = new LibreriaImpl(persistenza);
            Libro libro1 = new Libro.Builder("Doppia Verità", "Michael Connelly", "002").Valutazione(3).Stato(Libro.Stato.InLettura).Genere("Poliziesco").build();
            Libro libro2 = new Libro.Builder("L'Alchimista", "Paulo Coelho", "003").Valutazione(5).Stato(Libro.Stato.Letto).Genere("Romanzo").build();
            Libro libro3 = new Libro.Builder("1984", "George Orwell", "001").Valutazione(4).Stato(Libro.Stato.DaLeggere).Genere("Romanzo").build();
            Libro libro4 = new Libro.Builder("La fattoria degli animali", "George Orwell", "004").Valutazione(5).Stato(Libro.Stato.Letto).Genere("Fantastico").build();
            libreria.aggiungiLibro(libro1);
            libreria.aggiungiLibro(libro2);
            libreria.aggiungiLibro(libro3);
            libreria.aggiungiLibro(libro4);
        }

        @Test
        void testFiltroPerAutore(){
            FiltroPerAutore filtroPerAutore = new FiltroPerAutore("George Orwell");
            libreria.setFiltro(filtroPerAutore);
            List<Libro> libri=libreria.getLibriFiltrati();
            assertEquals("George Orwell", libri.get(0).getAutore());
            assertEquals("George Orwell", libri.get(1).getAutore());
            String titolo = libri.get(0).getTitolo();
            assertTrue(titolo.equals("1984") || titolo.equals("La fattoria degli animali"));
            String titolo2 = libri.get(1).getTitolo();
            assertTrue(titolo2.equals("1984") || titolo2.equals("La fattoria degli animali"));
        }

        @Test
        void testFiltroPerValutazione(){
            FiltroPerValutazione filtroPerValutazione=new FiltroPerValutazione(5);
            libreria.setFiltro(filtroPerValutazione);
            List<Libro> libri=libreria.getLibriFiltrati();
            assertEquals(5, libri.get(0).getValutazione());
            assertEquals(5, libri.get(1).getValutazione());
            String titolo = libri.get(0).getTitolo();
            assertTrue(titolo.equals("La fattoria degli animali") || titolo.equals("L'Alchimista"));
            String titolo2 = libri.get(1).getTitolo();
            assertTrue(titolo2.equals("La fattoria degli animali") || titolo2.equals("L'Alchimista"));
        }

        @Test
        void testFiltroPerGenere(){
            FiltroPerGenere filtroPerGenere=new FiltroPerGenere("Romanzo");
            libreria.setFiltro(filtroPerGenere);
            List<Libro> libri=libreria.getLibriFiltrati();
            assertEquals("Romanzo", libri.get(0).getGenere());
            assertEquals("Romanzo", libri.get(1).getGenere());
            String titolo = libri.get(0).getTitolo();
            assertTrue(titolo.equals("1984") || titolo.equals("L'Alchimista"));
            String titolo2 = libri.get(1).getTitolo();
            assertTrue(titolo2.equals("1984") || titolo2.equals("L'Alchimista"));
        }
    }


    @Nested
    class FiltriDecorator {

        private Libreria libreria;

        @BeforeEach
        void setUp() {
            PersistenzaImpl persistenza = PersistenzaImpl.INSTANCE;
            persistenza.reset();
            libreria = new LibreriaImpl(persistenza);

            Libro libro1 = new Libro.Builder("Doppia Verità", "Michael Connelly", "002")
                    .Valutazione(3).Stato(Libro.Stato.InLettura).Genere("Poliziesco").build();

            Libro libro2 = new Libro.Builder("L'Alchimista", "Paulo Coelho", "003")
                    .Valutazione(5).Stato(Libro.Stato.Letto).Genere("Romanzo").build();

            Libro libro3 = new Libro.Builder("1984", "George Orwell", "001")
                    .Valutazione(4).Stato(Libro.Stato.DaLeggere).Genere("Romanzo").build();

            Libro libro4 = new Libro.Builder("La fattoria degli animali", "George Orwell", "004")
                    .Valutazione(5).Stato(Libro.Stato.Letto).Genere("Fantastico").build();

            Libro libro5 = new Libro.Builder("Il mastino di Baskerville", "Arthur Conan Doyle", "005")
                    .Valutazione(3).Stato(Libro.Stato.Letto).Genere("Poliziesco").build();

            libreria.aggiungiLibro(libro1);
            libreria.aggiungiLibro(libro2);
            libreria.aggiungiLibro(libro3);
            libreria.aggiungiLibro(libro4);
            libreria.aggiungiLibro(libro5);
        }

        @Test
        void testSoloFiltroAutore() {
            FiltroDecorator filtro = new FiltroAutoreDecorator(new FiltroVuoto(), "George Orwell");
            libreria.setFiltroDecorator(filtro);
            List<Libro> filtrati = libreria.getLibriFiltratiDecorator();
            assertEquals(2, filtrati.size());
            for (Libro l : filtrati) {
                assertEquals("George Orwell", l.getAutore());
            }
        }

        @Test
        void testFiltroCompostoAutoreEGenere() {
            FiltroDecorator filtro = new FiltroGenereDecorator(
                    new FiltroAutoreDecorator(new FiltroVuoto(), "George Orwell"),
                    "Romanzo");
            libreria.setFiltroDecorator(filtro);
            List<Libro> filtrati = libreria.getLibriFiltratiDecorator();

            assertEquals(1, filtrati.size());
            assertEquals("1984", filtrati.get(0).getTitolo());
        }

        @Test
        void testFiltroCompostoGenereEValutazione() {
            FiltroDecorator filtro = new FiltroValutazioneDecorator(
                    new FiltroGenereDecorator(new FiltroVuoto(), "Poliziesco"),
                    3);
            libreria.setFiltroDecorator(filtro);
            List<Libro> filtrati = libreria.getLibriFiltratiDecorator();

            assertEquals(2, filtrati.size());
            String titolo = filtrati.get(0).getTitolo();
            assertTrue(titolo.equals("Doppia Verità") || titolo.equals("Il mastino di Baskerville"));
            String titolo2 = filtrati.get(1).getTitolo();
            assertTrue(titolo2.equals("Doppia Verità") || titolo2.equals("Il mastino di Baskerville"));
        }

        @Test
        void testFiltroTriplo() {
            FiltroDecorator filtro = new FiltroValutazioneDecorator(
                    new FiltroAutoreDecorator(
                            new FiltroGenereDecorator(new FiltroVuoto(), "Fantastico"),
                            "George Orwell"),
                    5);
            libreria.setFiltroDecorator(filtro);
            List<Libro> filtrati = libreria.getLibriFiltratiDecorator();

            assertEquals(1, filtrati.size());
            assertEquals("La fattoria degli animali", filtrati.get(0).getTitolo());
        }
    }

}
