package com.biblioteca.util;

import java.time.Year;
import java.util.regex.Pattern;

/**
 * Classe utilitária com validações de entrada (RNF04).
 */
public class Validador {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    private Validador() {
        // classe utilitária: não deve ser instanciada
    }

    public static boolean textoNaoVazio(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    public static boolean cpfValido(String cpf) {
        if (cpf == null) return false;
        String limpo = cpf.replaceAll("[^0-9]", "");
        return limpo.length() == 11;
    }

    public static boolean emailValido(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean anoValido(int ano) {
        return ano > 0 && ano <= Year.now().getValue();
    }
}
