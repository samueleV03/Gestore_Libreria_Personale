package gui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import builder.Libro;
import decorator.*;
import libreria.LibreriaImpl;
import mediator.AggiungiLibroMediator;
import observer.Observer;
import persistenza.LibreriaPersistente;
import persistenza.PersistenzaImpl;
import strategy.OrdinaPerAutore;
import strategy.OrdinaPerTitolo;
import strategy.OrdinaPerValutazione;


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

            JPanel pannelloBottoni = new JPanel();
            pannelloBottoni.setLayout(new BoxLayout(pannelloBottoni, BoxLayout.Y_AXIS));
            pannelloBottoni.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // margine
            pannelloBottoni.setBackground(Color.LIGHT_GRAY);
            //bottone per aggiungere i libri
            JButton bottoneAggiungi = new JButton("Aggiungi libro");
            //bottone per applicare i filtri
            JButton bottoneApplicaFiltri = new JButton("Applica filtri");
            //bottone per rimuovere i filtri e tornare alla visione non filtrata
            JButton bottoneResetFiltri = new JButton("Mostra tutti");
            //bottone per ordinare
            JButton ordinaButton = new JButton("Ordina");

            //faccio tutti i bottoni della stessa dimensioni
            Dimension dimensioneBottoni = new Dimension(150, 30);
            for (JButton btn : List.of(bottoneAggiungi, bottoneResetFiltri, ordinaButton, bottoneApplicaFiltri)) {
                btn.setPreferredSize(dimensioneBottoni);
                btn.setMaximumSize(dimensioneBottoni);
                btn.setMinimumSize(dimensioneBottoni);
                btn.setAlignmentX(Component.CENTER_ALIGNMENT); //per centrare nel pannello verticale
                pannelloBottoni.add(btn);
                pannelloBottoni.add(Box.createRigidArea(new Dimension(0, 10))); //spazio verticale
            }


            //pannello per contenere i libri
            pannelloLibri = new JPanel();
            pannelloLibri.setLayout(new BoxLayout(pannelloLibri, BoxLayout.Y_AXIS));
            pannelloLibri.setBackground(Color.LIGHT_GRAY); //colore sfondo pannello
            //scroll per il pannello
            JScrollPane scrollPane = new JScrollPane(pannelloLibri);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);

            //aggiunta dei vari pannelli a quello principale
            pannelloPrincipale.add(pannelloRicerca, BorderLayout.NORTH); //ricerca in alto
            pannelloPrincipale.add(pannelloBottoni, BorderLayout.EAST);  //bottoni a destra
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

            //listener per cerca
            bottoneCerca.addActionListener(e -> mostraLibriFiltrati());

            //listener per aggiunta
            bottoneAggiungi.addActionListener(e -> aggiungiLibro());

            //listener per filtri
            bottoneApplicaFiltri.addActionListener(e -> filtra());

            //listener per azzerare
            bottoneResetFiltri.addActionListener(e -> {
                campoRicerca.setText("");       // svuota campo ricerca
                aggiornaVistaLibri(libreria.getLibriOrdinati());  // mostra tutti i libri
            });

            //listener per ordinamento
            ordinaButton.addActionListener(e -> mostraOpzioniOrdinamento(frame));

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
            nessunRisultato.setFont(new Font("Arial", Font.BOLD, 20));
            nessunRisultato.setForeground(Color.BLACK);
            nessunRisultato.setAlignmentX(Component.CENTER_ALIGNMENT);
            pannelloLibri.setPreferredSize(new Dimension(400, 100));
            pannelloLibri.setLayout(new BoxLayout(pannelloLibri, BoxLayout.Y_AXIS));
            pannelloLibri.add(Box.createVerticalGlue()); //centrare verticalmente
            pannelloLibri.add(nessunRisultato);
            pannelloLibri.add(Box.createVerticalGlue());
            pannelloLibri.revalidate();
            pannelloLibri.repaint();
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

                //bottone per rimuovere libri
                JButton bottoneRimuovi = new JButton("Rimuovi libro");
                bottoneRimuovi.addActionListener(e -> {
                    int conferma = JOptionPane.showConfirmDialog(null,
                            "Sei sicuro di voler rimuovere \"" + libro.getTitolo() + "\"?",
                            "Conferma rimozione",
                            JOptionPane.YES_NO_OPTION);

                    if (conferma == JOptionPane.YES_OPTION) {
                        boolean rimosso = libreria.rimuoviLibro(libro);
                        if (rimosso) {
                            SwingUtilities.invokeLater(() -> aggiornaVistaLibri(libreria.getLibriOrdinati()));
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "Errore nella rimozione del libro.",
                                    "Errore",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                infoPanel.add(bottoneRimuovi);

                //bottone per la modifica
                JButton bottoneModifica = new JButton("Modifica libro");
                bottoneModifica.addActionListener(e -> {
                    // Chiama il metodo per mostrare la finestra di modifica
                    modifica(libro);
                });
                infoPanel.add(bottoneModifica);
            }

            pannelloLibri.revalidate();
            pannelloLibri.repaint();
        }
    }


    @Override
    public void update() {
        System.out.println("Update chiamato");
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



    private static void filtra() {
        JTextField filtroAutoreDialog = new JTextField(15);

        String[] generi = {"Tutti", "Romanzo", "Saggio", "Fantasy", "Biografia", "Default genere"};
        JComboBox<String> filtroGenereDialog = new JComboBox<>(generi);

        JComboBox<Libro.Stato> filtroStatoDialog = new JComboBox<>(Libro.Stato.values());
        filtroStatoDialog.insertItemAt(null, 0);
        filtroStatoDialog.setSelectedIndex(0);

        JSlider filtroValutazioneDialog = new JSlider(0, 5, 0);
        filtroValutazioneDialog.setMajorTickSpacing(1);
        filtroValutazioneDialog.setPaintTicks(true);
        filtroValutazioneDialog.setPaintLabels(true);

        JPanel pannelloDialog = new JPanel();
        pannelloDialog.setLayout(new BoxLayout(pannelloDialog, BoxLayout.Y_AXIS));
        pannelloDialog.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pannelloDialog.add(new JLabel("Autore:"));
        pannelloDialog.add(filtroAutoreDialog);

        pannelloDialog.add(Box.createVerticalStrut(10));
        pannelloDialog.add(new JLabel("Genere:"));
        pannelloDialog.add(filtroGenereDialog);

        pannelloDialog.add(Box.createVerticalStrut(10));
        pannelloDialog.add(new JLabel("Stato:"));
        pannelloDialog.add(filtroStatoDialog);

        pannelloDialog.add(Box.createVerticalStrut(10));
        pannelloDialog.add(new JLabel("Valutazione:"));
        pannelloDialog.add(filtroValutazioneDialog);

        JButton bottoneApplicaFiltriDialog = new JButton("Applica filtri");
        pannelloDialog.add(Box.createVerticalStrut(15));
        pannelloDialog.add(bottoneApplicaFiltriDialog);

        JDialog dialog = new JDialog((Frame) null, "Filtra libri", true);
        dialog.getContentPane().add(pannelloDialog);
        dialog.pack();
        dialog.setLocationRelativeTo(null);

        bottoneApplicaFiltriDialog.addActionListener(e -> {
            String autoreFiltro = filtroAutoreDialog.getText().trim();
            String genereFiltro = (String) filtroGenereDialog.getSelectedItem();
            Libro.Stato statoFiltro = (Libro.Stato) filtroStatoDialog.getSelectedItem();
            int valutazione = filtroValutazioneDialog.getValue();

            FiltroDecorator filtro = new FiltroVuoto();

            if (!autoreFiltro.isEmpty()) {
                filtro = new FiltroAutoreDecorator(filtro, autoreFiltro);
            }

            if (genereFiltro != null && !genereFiltro.equals("Tutti")) {
                filtro = new FiltroGenereDecorator(filtro, genereFiltro);
            }

            if (statoFiltro != null) {
                filtro = new FiltroStatoDecorator(filtro, statoFiltro);
            }

            if (valutazione > 0) {
                filtro = new FiltroValutazioneDecorator(filtro, valutazione);
            }

            List<Libro> tuttiLibri = libreria.getLibriOrdinati();
            List<Libro> filtrati = filtro.filtra(tuttiLibri);

            aggiornaVistaLibri(filtrati);

            dialog.dispose();
        });

        dialog.setVisible(true);
    }


    private static void modifica(Libro libro) {
        JPanel panel = new JPanel(new GridLayout(0, 2));

        JTextField campoTitolo = new JTextField(libro.getTitolo());
        JTextField campoAutore = new JTextField(libro.getAutore());
        JTextField campoGenere = new JTextField(libro.getGenere());
        JTextField campoValutazione = new JTextField(String.valueOf(libro.getValutazione()));
        JComboBox<String> campoStato = new JComboBox<>(new String[] {
                "DaLeggere", "InLettura", "Letto"
        });
        campoStato.setSelectedItem(libro.getStatoLettura().name());

        panel.add(new JLabel("Titolo:"));
        panel.add(campoTitolo);
        panel.add(new JLabel("Autore:"));
        panel.add(campoAutore);
        panel.add(new JLabel("Genere:"));
        panel.add(campoGenere);
        panel.add(new JLabel("Valutazione (1-5):"));
        panel.add(campoValutazione);
        panel.add(new JLabel("Stato lettura:"));
        panel.add(campoStato);

        int result = JOptionPane.showConfirmDialog(null, panel, "Modifica Libro",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Map<String, String> modifiche = new HashMap<>();
            modifiche.put("titolo", campoTitolo.getText());
            modifiche.put("autore", campoAutore.getText());
            modifiche.put("genere", campoGenere.getText());
            modifiche.put("valutazione", campoValutazione.getText());
            modifiche.put("stato", (String) campoStato.getSelectedItem());
            libreria.modificaLibro(libro, modifiche);

        }
    }


    private void mostraOpzioniOrdinamento(JFrame frame) {
        String[] opzioni = {"Titolo", "Autore", "Valutazione"};

        String scelta = (String) JOptionPane.showInputDialog(
                frame,
                "Scegli un criterio di ordinamento:",
                "Ordinamento Libreria",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opzioni,
                opzioni[0]
        );

        if (scelta != null) {
            if (scelta.equals("Titolo")) {
                libreria.setCriterioOrdine(new OrdinaPerTitolo());
            } else if (scelta.equals("Autore")) {
                libreria.setCriterioOrdine(new OrdinaPerAutore());
            } else if (scelta.equals("Valutazione")) {
                libreria.setCriterioOrdine(new OrdinaPerValutazione());
            }

            aggiornaVistaLibri(libreria.getLibriOrdinati());
        }
    }



}






