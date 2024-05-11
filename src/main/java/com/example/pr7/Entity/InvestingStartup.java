package com.example.pr7.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity()
public class InvestingStartup {
    @Id
    private UUID investingStartupId;
    private UUID startupId;
    private UUID investorId;
    private int investorStatus; //0-не метч, 1 - метч
    private int teamStatus;  //0-нет реакции, 1- метч, 2 - не метч
}
//    Статусы стартапа
//            0) Только подан на валидацию
//            1) Прошел валидацию (на рассмотрении
//            2) Не прошел валидацию