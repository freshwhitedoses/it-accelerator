package com.example.pr7.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvestingStartupDto {
    private UUID investingStartupId;
    private Startup startup;
    private User investor;
    private int teamStatus;
    private int investorStatus;
}
