package com.example.pr7.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Startup")
@Getter
@Setter
public class Startup {
    @Id
    private UUID startupId;
    private UUID teamId;
    private UUID analystId;
    private String name;
    public String target;
    private String description;
    private String investmentAmount;
    private int status;
    private String analystComment;
    private int investmentType;
    private int mainInStartup;
    private int mainInMe;
    private String filesLink;
    private int maturityStage; //стадии 0-прототип, mvp, первые продажи, готовое решение, перезапуск проекта

    public Startup(StartupRequest startupRequest) {
        this.teamId = startupRequest.getTeamId();
        this.analystId = null;
        this.name = startupRequest.getName();
        this.target = startupRequest.getTarget();
        this.description = startupRequest.getDescription();
        this.investmentAmount = startupRequest.getInvestmentAmount();
        this.status = 0;
        this.analystComment = null;
        this.investmentType = startupRequest.getInvestmentType();
        this.mainInStartup = startupRequest.getMainInStartup();
        this.mainInMe = startupRequest.getMainInMe();
        this.filesLink = startupRequest.getFilesLink();
        this.maturityStage = startupRequest.getMaturityStage();
    }
}
