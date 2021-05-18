package com.solvedev.haloskin.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class RupiahConvert {

     public String convertStringToRupiah(String nominal){

        double jumlah = Double.parseDouble(nominal);

        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);

        return kursIndonesia.format(jumlah);
    }


}
