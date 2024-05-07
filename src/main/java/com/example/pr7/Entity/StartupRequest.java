package com.example.pr7.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.UUID;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StartupRequest {
    private UUID teamId;
    private String name;
    public String target;
    private String description;
    private String investmentAmount;
    private int investmentType;
    private int mainInStartup;
    private int mainInMe;
    private String filesLink;
    private int maturityStage;
}
