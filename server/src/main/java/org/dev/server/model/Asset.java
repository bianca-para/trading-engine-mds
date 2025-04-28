package org.dev.server.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "asset")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Symbol cannot be blank")
    @Size(max = 10, message = "Symbol cannot be longer than 10 characters")
    private String symbol;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 50, message = "Name cannot be longer than 50 characters")
    private String name;

    @PositiveOrZero(message = "Price must be zero or positive")
    private Double price;

    public Asset(String symbol, String name, Double price){
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }
}
