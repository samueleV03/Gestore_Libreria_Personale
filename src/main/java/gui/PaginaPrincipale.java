package gui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;
import builder.Libro;
import libreria.LibreriaImpl;
import mediator.AggiungiLibroMediator;
import observer.Observer;
import persistenza.LibreriaPersistente;
import persistenza.PersistenzaImpl;


public class PaginaPrincipale implements Observer {
    private static JPanel pannelloLibri;
    private static JTextField campoRicerca;
    private static LibreriaImpl libreria;

    public void mostra() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Libreria");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            JPanel pannelloPrincipale = new JPanel(new BorderLayout());

            //ricerca
            JPanel pannelloRicerca = new JPanel(new FlowLayout(FlowLayout.LEFT));
            campoRicerca = new JTextField(30);
            JButton bottoneCerca = new JButton("Cerca");

            pannelloRicerca.add(new JLabel("Cerca per titolo:"));
            pannelloRicerca.add(campoRicerca);
            pannelloRicerca.add(bottoneCerca);

            bottoneCerca.addActionListener(e -> mostraLibriFiltrati());

            //pannello per contenere i libri
            pannelloLibri = new JPanel();
            pannelloLibri.setLayout(new BoxLayout(pannelloLibri, BoxLayout.Y_AXIS));
            pannelloLibri.setBackground(Color.LIGHT_GRAY); //colore sfondo pannello
            //scroll per il pannello
            JScrollPane scrollPane = new JScrollPane(pannelloLibri);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);

            pannelloPrincipale.add(pannelloRicerca, BorderLayout.NORTH);
            pannelloPrincipale.add(scrollPane, BorderLayout.CENTER);

            frame.add(pannelloPrincipale);
            frame.setVisible(true);

            //inizializzazione persistenza libreria
            LibreriaPersistente persistenza = PersistenzaImpl.INSTANCE;
            libreria = new LibreriaImpl(persistenza);
            caricaLibri();

            //registriamo observer
            libreria.attach(this);
            caricaLibri();

            //aggiunta libri
            JButton bottoneAggiungi = new JButton("Aggiungi libro");
            pannelloRicerca.add(bottoneAggiungi);
            bottoneAggiungi.addActionListener(e -> aggiungiLibro());
        });
    }

    private void caricaLibri() {
        List<Libro> libri = libreria.getLibriOrdinati();
        aggiornaVistaLibri(libri);
    }

    private static void mostraLibriFiltrati() {
        String filtro = campoRicerca.getText().toLowerCase();
        //prendiamo solo i libri che corrispondono al filtro
        List<Libro> libri = libreria.getLibriOrdinati().stream()
                .filter(libro -> libro.getTitolo().toLowerCase().contains(filtro))
                .toList();
        aggiornaVistaLibri(libri);
    }

    //metodo per visualizzare la lista dei libri, sia all'apertura che alla ricerca
    private static void aggiornaVistaLibri(List<Libro> libri) {
        //ripuliamo
        pannelloLibri.removeAll();

        if (libri.isEmpty()) { //se nessun libro è presente
            JLabel nessunRisultato = new JLabel("Libro non presente");
            nessunRisultato.setFont(new Font("Arial", Font.BOLD, 16));
            nessunRisultato.setForeground(Color.RED);
            nessunRisultato.setAlignmentX(Component.CENTER_ALIGNMENT);
            pannelloLibri.setLayout(new BoxLayout(pannelloLibri, BoxLayout.Y_AXIS));
            pannelloLibri.add(Box.createVerticalGlue()); //centrare verticalmente
            pannelloLibri.add(nessunRisultato);
            pannelloLibri.add(Box.createVerticalGlue());
        } else {
            //se abbiamo dei libri creiamo i jpanel per ogni libro
            pannelloLibri.setLayout(new BoxLayout(pannelloLibri, BoxLayout.Y_AXIS));

            for (Libro libro : libri) {
                JPanel card = new JPanel(new BorderLayout());
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(10, 10, 10, 10),
                        BorderFactory.createLineBorder(Color.BLACK, 1) //colore bordo card
                ));
                card.setBackground(Color.LIGHT_GRAY); //colore dello sfondo della card

                JLabel titolo = new JLabel("<html><b>" + libro.getTitolo() + "</b></html>");
                JLabel autore = new JLabel("Autore: " + libro.getAutore());
                JLabel isbn = new JLabel("ISBN: " + libro.getIsbn());
                JLabel genere = new JLabel("Genere: " + libro.getGenere());
                JLabel valutazione = new JLabel("Valutazione: " + libro.getValutazione());
                JLabel statoLettura = new JLabel("Stato: " + libro.getStatoLettura().name());

                JPanel infoPanel = new JPanel(new GridLayout(0, 1));
                infoPanel.setOpaque(false);
                infoPanel.add(titolo);
                infoPanel.add(autore);
                infoPanel.add(isbn);
                infoPanel.add(genere);
                infoPanel.add(valutazione);
                infoPanel.add(statoLettura);

                card.add(infoPanel, BorderLayout.CENTER); //aggiungiamo il pannello del libro
                pannelloLibri.add(card);
                pannelloLibri.add(Box.createVerticalStrut(10)); //spazio tra i pannelli
            }
        }

        pannelloLibri.revalidate();
        pannelloLibri.repaint();
    }


    @Override
    public void update() {
        SwingUtilities.invokeLater(this::caricaLibri);
    }

    private static void aggiungiLibro() {
        JTextField campoTitolo = new JTextField(20);
        JTextField campoAutore = new JTextField(20);
        JTextField campoIsbn = new JTextField(20);
        JTextField campoGenere = new JTextField(20);
        JTextField campoValutazione = new JTextField(5);
        JComboBox<Libro.Stato> comboStato = new JComboBox<>(Libro.Stato.values());
        JButton bottoneSalva = new JButton("Salva");
        bottoneSalva.setEnabled(false); //inizialmente disabilitato poiché i campi sono vuoti
        //mediator
        AggiungiLibroMediator mediator = new AggiungiLibroMediator();
        mediator.setTitolo(campoTitolo);
        mediator.setAutore(campoAutore);
        mediator.setIsbn(campoIsbn);
        mediator.setSalva(bottoneSalva);
        //Listener per ogni campo obbligatorio
        DocumentListener listenerTitolo = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { mediator.widgetCambiato(campoTitolo); }
            public void removeUpdate(DocumentEvent e) { mediator.widgetCambiato(campoTitolo); }
            public void changedUpdate(DocumentEvent e) { mediator.widgetCambiato(campoTitolo); }
        };
        campoTitolo.getDocument().addDocumentListener(listenerTitolo);

        DocumentListener listenerAutore = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { mediator.widgetCambiato(campoAutore); }
            public void removeUpdate(DocumentEvent e) { mediator.widgetCambiato(campoAutore); }
            public void changedUpdate(DocumentEvent e) { mediator.widgetCambiato(campoAutore); }
        };
        campoAutore.getDocument().addDocumentListener(listenerAutore);

        DocumentListener listenerIsbn = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { mediator.widgetCambiato(campoIsbn); }
            public void removeUpdate(DocumentEvent e) { mediator.widgetCambiato(campoIsbn); }
            public void changedUpdate(DocumentEvent e) { mediator.widgetCambiato(campoIsbn); }
        };
        campoIsbn.getDocument().addDocumentListener(listenerIsbn);
        //creiamo il pannello e aggiungiamo i vari campi
        JPanel pannello = new JPanel(new GridLayout(0, 1));
        pannello.add(new JLabel("Titolo:"));
        pannello.add(campoTitolo);
        pannello.add(new JLabel("Autore:"));
        pannello.add(campoAutore);
        pannello.add(new JLabel("ISBN:"));
        pannello.add(campoIsbn);
        pannello.add(new JLabel("Genere:"));
        pannello.add(campoGenere);
        pannello.add(new JLabel("Valutazione (1-5):"));
        pannello.add(campoValutazione);
        pannello.add(new JLabel("Stato di lettura:"));
        pannello.add(comboStato);

        pannello.add(bottoneSalva);

        JDialog dialog = new JDialog((Frame) null, "Aggiungi nuovo libro", true);
        dialog.getContentPane().add(pannello);

        bottoneSalva.addActionListener(e -> {
            String titolo = campoTitolo.getText().trim();
            String autore = campoAutore.getText().trim();
            String isbn = campoIsbn.getText().trim();
            String genere = campoGenere.getText().trim();
            String valutazioneText = campoValutazione.getText().trim();
            Libro.Stato statoSelezionato = (Libro.Stato) comboStato.getSelectedItem();

            int valutazione = 0;
            //la valutazione deve essere un numero se presente
            if (!valutazioneText.isEmpty()) {
                try {
                    valutazione = Integer.parseInt(valutazioneText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Valutazione non valida");
                }
            }

            Libro nuovoLibro = new Libro.Builder(titolo, autore, isbn)
                    .Genere(genere)
                    .Valutazione(valutazione)
                    .Stato(statoSelezionato)
                    .build();

            boolean aggiunto = libreria.aggiungiLibro(nuovoLibro);
            //non deve essere presente un libro con stesso isbn
            if (!aggiunto) {
                JOptionPane.showMessageDialog(null,
                        "Un libro con ISBN \"" + isbn + "\" è già presente nella libreria.",
                        "Libro duplicato",
                        JOptionPane.WARNING_MESSAGE);
            }
            dialog.dispose();
        });
        dialog.pack(); //adatta automaticamente finestra
        dialog.setLocationRelativeTo(null); //metta la finestra al centro
        dialog.setVisible(true);
    }




}


