package it.fabriziotrupia.trupia.controller;

import it.fabriziotrupia.trupia.payload.request.RequestScore;
import it.fabriziotrupia.trupia.payload.request.RequestWord;
import it.fabriziotrupia.trupia.service.WordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Questa classe l'ho utilizzata per gestire le operazioni CRUD relative alla tabella Word.
 * @RestController: permette di annotare la classe come un controller in modo da fornire risposte HTTP.
 * @RequestMapping("word"): specifica il percorso radice per tutte le richieste gestite da questo controller, quindi ogni
 * endpoint avrà l'URL che inizia con /word.
 * @RequiredArgsConstructor: crea un costruttore con parametri per le dipendenze da iniettare.
 */
@RestController @RequestMapping("word")
@RequiredArgsConstructor @Validated
public class WordController {

    //Inietto un'istanza di WordService nel controller, in questo modo il Bean è gestito da SpringBoot.
    private final WordService wordService;

    //Questo metodo permette di creare una nuova parola attraverso una POST.
    // Prende come parametro un oggetto RequestWord validato dall'annotazione @Valid e restituisce una risposta HTTP.
    //La logica dietro alla creazione di una parola è scritta nel metodo createWord situata nella classe wordService
    @PostMapping()
    public ResponseEntity<?> createWord (@RequestBody @Valid RequestWord requestWord){
        return wordService.createWord(requestWord);
    }

    // Questo endpoint gestisce una richiesta HTTP GET per visualizzare i dati relativi a una parola specifica.
    // L'identificatore dell'oggetto word viene passato come parametro nell URL.
    // La logica di business per recuperare i dati della parola associata all'id è gestita dal metodo viewWord nel WordService.
    @GetMapping("/view/{wordId}")
    public ResponseEntity<?> viewWord(@PathVariable long wordId){
        return wordService.viewWord(wordId);
    }

    //Questo metodo gestisce la richiesta POST per calcolare lo score di similarità tra due parole identificate nella
    //classe RequestScore
    @PostMapping("/score")
    public ResponseEntity<?> score(@RequestBody @Valid RequestScore requestScore){
        return wordService.score(requestScore);
    }
}
