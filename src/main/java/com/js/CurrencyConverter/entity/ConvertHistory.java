package com.js.CurrencyConverter.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="convertHistory")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConvertHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String baseCurrency;
    String targetCurrency;
    Double amount;

    Double result;
    LocalDateTime dateTime;

}
