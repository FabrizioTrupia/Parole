package it.fabriziotrupia.trupia.service;

import it.fabriziotrupia.trupia.entity.Word;
import it.fabriziotrupia.trupia.payload.request.RequestScore;
import it.fabriziotrupia.trupia.payload.request.RequestWord;
import it.fabriziotrupia.trupia.payload.response.WordResponse;
import it.fabriziotrupia.trupia.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Questa classe gestisce la logica di business relativa alle operazioni delle parole.
 */
@Service
@RequiredArgsConstructor
public class WordService {

    /**
     * Inietto un'istanza di WordRepository nel service, in questo modo il Bean è gestito da SpringBoot.
     */
    private final WordRepository wordRepository;


    /**
     * Metodo per creare una nuova parola passata come paraemtro.
     */
    public ResponseEntity<?> createWord(RequestWord requestWord) {
        //Verifico se la parola è gia presente nel db e in questo caso restituisce una Bad Request
        if (wordRepository.existsByWord(requestWord.getWord()))
            return new ResponseEntity<>("La parola già esiste" , HttpStatus.BAD_REQUEST);

        // Verifico se la parola è nulla, contiene spazi o numeri. Anche in questo caso restituisce una Bad Request
        if (requestWord.getWord().contains(" ") || !requestWord.getWord().matches("[a-zA-Z]+"))
            return new ResponseEntity<>("La parola non è valida", HttpStatus.BAD_REQUEST);


        //Se la parola passata come parametro non esiste e rispecchia i vincoli che ho dato allora la salva nel db
        Word w = new Word(requestWord.getWord());
        wordRepository.save(w);
        //Restituisce CREATED
        return new ResponseEntity<>("Parola creata" , HttpStatus.CREATED);
    }

    /**
     * Metodo per visualizzare dettagli di una parola che identifico dall'id che passo come paraemtro.
     * Altrimenti, raccoglie informazioni sulla parola, come la lunghezza totale,
     * se è palindroma, il conteggio di vocali, consonanti e la frequenza delle lettere.
     * Restituisce un oggetto WordResponse contenente queste informazioni con uno status HTTP OK.
     *
     * @param wordId L'ID della parola da cercare nel repository
     * @return Una risposta HTTP contenente i dettagli della parola o un errore se l'ID non corrisponde a nessuna parola
     */
    public ResponseEntity<?> viewWord(long wordId) {
        //Recupero la parola presente nel db tramite l'id passato come paramentro
        Optional<Word> w = wordRepository.findById(wordId);

        //Se l'id non corrisponde a nessun id presente nel db restituisce un errore
        if (w.isEmpty())
            new ResponseEntity<>("L'id non esiste", HttpStatus.BAD_REQUEST);

        //Se passa il controllo allora raccogliamo tutte le informazioni necessarie
        //La parola relativo all'id passato
        String word = w.get().getWord();
        //Per sapere se la parola è palindroma, attravarso il metodo checkPalindrome
        boolean isPalindrome = checkPalindrome(word);
        //La lunghezza della parola
        int totalCharacters = word.length();
        //Per sapere il numero di vocali presnti nella parola grazie al metodo countVowel
        int vowelCount = countVowel(word);
        //Varibile per sapere il numero di consonanti presenti nella parola
        int consonantCount = totalCharacters - vowelCount;
        //Costruisco una mappa per sapere la frequenza delle lettere presenti nella parola grazie al metodo frequency
        Map<Character, Integer> letterFrequency = frequency(word);

        //Costruisco un oggetto WordResponse contenente i dettagli della parola
        WordResponse wordResponse = new WordResponse();
        wordResponse.setWord(word);
        wordResponse.setPalindrome(isPalindrome);
        wordResponse.setTotalCharacters(totalCharacters);
        wordResponse.setVowelCount(vowelCount);
        wordResponse.setConsonantCount(consonantCount);
        wordResponse.setLetterFrequency(letterFrequency);

        //Ritorna la risposta HTTP con lo stato OK e l'oggetto WordResponse contenente tutti i dati
        return new ResponseEntity<>(wordResponse, HttpStatus.OK);
    }

    /**
     * Questo metodo calcola lo score di similarità tra due parole identificate dai rispettivi ID.
     * Altrimenti, calcola lo score di similarità tra le due parole e costruisce una mappa di risposta
     * contenente le parole e lo score di similarità.
     * Restituisce una risposta HTTP contenente le informazioni delle parole e lo score.
     *
     * @param requestScore Un oggetto contenente gli ID delle due parole da confrontare
     * @return Una risposta HTTP contenente le due parole e lo score di similarità o un errore se le parole non esistono
     */
    public ResponseEntity<?> score(RequestScore requestScore) {
        //Recupero le parole dal repository attraverso gli ID
        Optional<Word> w1 = wordRepository.findById(requestScore.getFirstId());
        Optional<Word> w2 = wordRepository.findById(requestScore.getSecondId());

        //Verifico se gli id sono presenti nel repository, se non sono presenti ritorna una BAD REQUEST
        if (w1.isEmpty() || w2.isEmpty()) {
            return new ResponseEntity<>("Una o entrambe le parole non esistono", HttpStatus.BAD_REQUEST);
        }


        // Calcola lo score di similarità tra le due parole grazie al metodo calculateSimilarity (che mette a confronto
        //la lunghezza delle due parole
        int similarityScore = calculateSimilarity(w1.get().getWord(), w2.get().getWord());

        // Costruisco una mappa di risposta con le parole e lo score di similarità
        Map<String, Object> response = new HashMap<>();
        response.put("Prima parola: ", w1.get().getWord());
        response.put("Seconda parola: ", w2.get().getWord());
        response.put("Score: ", similarityScore);

        // Restituisce le parole e lo score di similarità
        return new ResponseEntity<>(response, HttpStatus.OK);
    }




    /**
     * Attraverso questo metodo verifico se una parola è palindroma.
     */
    private boolean checkPalindrome(String word) {
        //Per prima cosa verifico la lunghezza della parola.
        int length = word.length();

        //Ciclo metà della lunghezza della parola (i < length/2)
        for (int i = 0; i < length / 2; i++) {
            //con un if, confronto la prima lettera della parola con l'ultima, la seconda con la penultima
            //e cosi via (grazie alla condizione (word.charAt(i) != word.charAt(length - i - 1)) se uno di questi confronti dovesse
            //risultare diverso allora restituisce false
            if (word.charAt(i) != word.charAt(length - i - 1)) {
                return false;
            }
        }
        //sennò restituisce true.
        return true;
    }


    /**
     * Metodo per contare il numero di vocali presenti nella parola
     */
    private int countVowel(String word) {
        //creo una variabile per contare il numero di vocali e la imposto a 0
        int count = 0;

        // Ciclo for per iterare i caratteri della parola
        for (int i = 0 ; i < word.length() ; i++){
            //il carattere della parola presente nella posizione i di ogni iterazione
            char c = word.charAt(i);

            // Verifico se il carattere corrente è una vocale
            if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
                count++; // Se il carattere è una vocale, incremento il contatore
            }
        }
        return count;
    }

    /**
     * Metodo per calcolare la frequenza delle lettere presenti nella parola
     */
    private Map<Character, Integer> frequency(String word) {
        //Creo la mappa
        Map<Character, Integer> letterFrequency = new HashMap<>();

        // ciclo for each per Iterare ogni carattere della parola
        for (char c : word.toCharArray()) {
                // converto il carattere in lettera minuscola
                char lowercaseChar = Character.toLowerCase(c);

            // Aggiorno la mappa delle frequenze
            // Se la lettera è già presente nella mappa incremento il valore della frequenza, altrimenti imposto 0 come valore predefinito
            letterFrequency.put(lowercaseChar, letterFrequency.getOrDefault(lowercaseChar, 0) + 1);
        }
        return letterFrequency;
    }



    /**
     * Metodo per calcolare la similarità tra due stringhe..
     */
    public int calculateSimilarity(String word1, String word2) {
        // Ottengo la lunghezza delle due stringhe
        int length1 = word1.length();
        int length2 = word2.length();

        //Calcolo la differenza in valore assoluto tra le lunghezze delle due stringhe
        int difference = Math.abs(length1 - length2);

        //Calcolo l'indice di similarità: (1 - differenza/lunghezza massima) X 100)
        //Il risultato sarà compreso tra 0 a 100, se il numero di caratteri sono uguali la differenza sarà 0 e quindi la similarità a 100
        //piu la differenza aumenta e piu il valore di similarità sarà vicino a 0
        double similarity = (1 - (double) difference / Math.max(length1, length2)) * 100;

        // Restituisce l'indice di similarità come valore intero
        return (int) similarity;
    }

}
