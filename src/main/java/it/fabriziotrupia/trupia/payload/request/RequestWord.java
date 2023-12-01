package it.fabriziotrupia.trupia.payload.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * La classe rappresenta la richiesta per creare una nuova parola.
 */
@Getter
public class RequestWord {

    //La stringa non deve essere vuota e lunga da 1 a 100 caratteri
    @NotEmpty @Size(min = 1 , max = 100)
    private String word;
}
