package com.Backend;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Utils {

    private static final DecimalFormat BRL_FORMATTER;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.'); // Ponto para separar milhares
        symbols.setDecimalSeparator(','); // Vírgula para separar decimais

        BRL_FORMATTER = new DecimalFormat("#,##0.00", symbols); // Formato de exemplo: 1.234,56
    }

    /**
     * Formata um valor monetário no padrão brasileiro.
     *
     * @param valor O valor a ser formatado.
     * @return Uma String no formato "1.234,56".
     */
    public static String formatarMonetario(double valor) {
        return BRL_FORMATTER.format(valor);
    }
}