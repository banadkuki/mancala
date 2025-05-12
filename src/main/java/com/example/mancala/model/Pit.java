package com.example.mancala.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pit {
    private int index;
    private int stones;
    private boolean isBigPit;
    private String ownerId;
}