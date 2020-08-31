package org.estatio.module.coda.dom.transaction;

// <ns2:Header>
//            <ns2:Key>
//                <ns2:CmpCode>IT04</ns2:CmpCode>
//                <ns2:Code>FE-EMI</ns2:Code>
//            </ns2:Key>
//            <ns2:TimeStamp>0</ns2:TimeStamp>
//            <ns2:Period>2020/14</ns2:Period>
//            <ns2:CurCode>EUR</ns2:CurCode>
//            <ns2:Date>2020-08-31T00:00:00.000Z</ns2:Date>
//        </ns2:Header>

import org.joda.time.LocalDate;

import org.estatio.module.currency.dom.Currency;

import lombok.Getter;
import lombok.Setter;

public class CodaTransactionHeader {

    @Getter @Setter
    private String cmpCode;

    @Getter @Setter
    private String docCode;

    // TODO: needed ... ?
    @Getter @Setter
    private int timeStamp;

    @Getter @Setter
    private String period;

    // TODO: needed ... ?
    @Getter @Setter
    private Currency currency;

    // TODO: needed ... ?
    @Getter @Setter
    private LocalDate date;
}
