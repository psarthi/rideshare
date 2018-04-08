package com.digitusrevolution.rideshare.common.util;

import java.util.Comparator;
import java.util.Currency;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

import com.digitusrevolution.rideshare.model.user.domain.Country;

public class RSUtil {

    public static SortedMap<Currency, Locale> currencyLocaleMap;
    
    static {
        currencyLocaleMap = new TreeMap<Currency, Locale>(new Comparator<Currency>() {
          public int compare(Currency c1, Currency c2){
              return c1.getCurrencyCode().compareTo(c2.getCurrencyCode());
          }
      });
      for (Locale locale : Locale.getAvailableLocales()) {
           try {
               Currency currency = Currency.getInstance(locale);
           currencyLocaleMap.put(currency, locale);
           }catch (Exception e){
       }
      }
    }
    public static String getCurrencySymbol(Country country) {
    		//IMP - Somehow just getting the symbol based on currency code is not working here which was not the case for Android
    		// That's why doing all this map etc. to get the currency symbol.
        Currency currency = Currency.getInstance(country.getCurrency().getName());
        return currency.getSymbol(currencyLocaleMap.get(currency));

    }
}
