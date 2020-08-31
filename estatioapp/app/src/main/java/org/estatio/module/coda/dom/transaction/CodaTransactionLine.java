package org.estatio.module.coda.dom.transaction;

// <ns2:Lines>
//            <ns2:Line>
//                <ns2:Number>1</ns2:Number>
//                <ns2:AccountCode>IT04EUR.STAT.ITRCAR0.+.IT14000.ITCL11673</ns2:AccountCode>
//                <ns2:DocValue>7254.94</ns2:DocValue>
//                <ns2:LineType>summary</ns2:LineType>
//                <ns2:LineSense>debit</ns2:LineSense>
//                <ns2:Description>INDENN. LOCAZ.,ONE 01-08-20/31-08-20</ns2:Description>
//                <ns2:DueDate>2020-08-31T00:00:00.000Z</ns2:DueDate>
//
//
//                <ns2:DocSumTax>1308.27</ns2:DocSumTax>
//                <ns2:MediaCode>I-BON</ns2:MediaCode>
//                <ns2:BankCode>BITINTSP8770</ns2:BankCode>
//                <ns2:ElmBankTag>2</ns2:ElmBankTag>
//                <ns2:UserRef2>Trimestale</ns2:UserRef2>
//                <ns2:UserRef3>Affitto e oneri 674 trimestre 2020</ns2:UserRef3>
//                <ns2:ElmBankAccount>CAROSELLO32NBBQ</ns2:ElmBankAccount>



//            </ns2:Line>
//            <ns2:Line>
//                <ns2:Number>2</ns2:Number>
//                <ns2:AccountCode>IT04EUR.STAT.ITRCAR0.+.IT60001.ITYR2020</ns2:AccountCode>
//                <ns2:DocValue>5414.43</ns2:DocValue>
//                <ns2:LineType>analysis</ns2:LineType>
//                <ns2:LineSense>credit</ns2:LineSense>
//                <ns2:Description>INDENN. LOCAZ. 01-08-20/31-08-20</ns2:Description>
//                <ns2:DueDate>2020-08-31T00:00:00.000Z</ns2:DueDate>


//                <ns2:Taxes>
//                    <ns2:Tax>
//                        <ns2:Code>VTITV22</ns2:Code>
//                        <ns2:ShortName>Iva vendite 22%</ns2:ShortName>
//                        <ns2:Value>1191.18</ns2:Value>
//                    </ns2:Tax>
//                </ns2:Taxes>


//            </ns2:Line>
//            <ns2:Line>
//                <ns2:Number>3</ns2:Number>
//                <ns2:AccountCode>IT04EUR.STAT.ITRCAR0.+.IT60201.ITYR2020</ns2:AccountCode>
//                <ns2:DocValue>532.24</ns2:DocValue>
//                <ns2:LineType>analysis</ns2:LineType>
//                <ns2:LineSense>credit</ns2:LineSense>
//                <ns2:Description>ONERI 01-08-20/31-08-20</ns2:Description>
//                <ns2:DueDate>2020-08-31T00:00:00.000Z</ns2:DueDate>
//                <ns2:Taxes>
//                    <ns2:Tax>
//                        <ns2:Code>VTITV22</ns2:Code>
//                        <ns2:ShortName>Iva vendite 22%</ns2:ShortName>
//                        <ns2:Value>117.09</ns2:Value>
//                    </ns2:Tax>
//                </ns2:Taxes>
//            </ns2:Line>
//            <ns2:Line>
//                <ns2:Number>4</ns2:Number>
//                <ns2:AccountCode>IT04EUR.STAT.ITGGEN0.+.IT30501.VTITV22</ns2:AccountCode>
//                <ns2:DocValue>1308.27</ns2:DocValue>
//                <ns2:LineType>tax</ns2:LineType>
//                <ns2:LineSense>credit</ns2:LineSense>
//                <ns2:Description>Iva vendite 22%</ns2:Description>
//                <ns2:DueDate>2020-08-31T00:00:00.000Z</ns2:DueDate>
//
//
//                <ns2:TaxLineCode>VTITV22</ns2:TaxLineCode>
//                <ns2:DocTaxTurnover>5946.67</ns2:DocTaxTurnover>
//            </ns2:Line>
//        </ns2:Lines>

import java.math.BigDecimal;

import org.joda.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

public class CodaTransactionLine {

    // all

    @Getter @Setter
    private CodaTransaction transaction;

    @Getter @Setter
    private int number;

    @Getter @Setter
    private String accountCode;

    @Getter @Setter
    private BigDecimal docValue;

    @Getter @Setter
    private CodaLineType lineType;

    @Getter @Setter
    private CodaLineSense lineSense;

    @Getter @Setter
    private String description;

    @Getter @Setter
    private LocalDate dueDate;

    // FRA only (Ita doc wide)
    @Getter @Setter
    private LocalDate valueDate;

    @Getter @Setter
    private String extRef1;

    @Getter @Setter
    private String extRef2;

    @Getter @Setter
    private String extRef3;

    @Getter @Setter
    private String extRef4;

    @Getter @Setter
    private String extRef5;


    // summary only

    @Getter @Setter
    private BigDecimal docSumTax;

    @Getter @Setter
    private String mediaCode;

    @Getter @Setter
    private String bankCode;

    @Getter @Setter
    private int elmBankTag;

    // Ita summary only
    @Getter @Setter
    private String userRef2;

    @Getter @Setter
    private String userRef3;

    @Getter @Setter
    private String elmBankAccount;

    // Fra summary only

    @Getter @Setter
    private String sepaMandateReference;


    // analysis only

    @Getter @Setter
    private String taxCode;

    @Getter @Setter
    private BigDecimal taxValue;

    // Ita analysis only
    @Getter @Setter
    private String taxShortName;


    // tax only

    @Getter @Setter
    private String taxLineCode;

    @Getter @Setter
    private BigDecimal docTaxTurnover;

}
