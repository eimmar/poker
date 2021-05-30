package com.poker.service;

import com.poker.model.Card;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToCardConverter implements Converter<String, Card> {
    @Override
    public Card convert(String source) {
        var suit = source.substring(0, 1).toUpperCase();
        var rank = source.substring(1).toUpperCase();

        return new Card(rank, suit);
    }
}
