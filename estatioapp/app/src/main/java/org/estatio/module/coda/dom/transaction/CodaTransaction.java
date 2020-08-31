package org.estatio.module.coda.dom.transaction;


/*
// ITA OUTGOING
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<PostRequest xmlns="http://www.coda.com/efinance/schemas/inputext/input-12.0/webservice" xmlns:ns2="http://www.coda.com/efinance/schemas/transaction" xmlns:ns4="http://www.coda.com/efinance/schemas/browsedetails" xmlns:ns3="http://www.coda.com/efinance/schemas/inputext" xmlns:ns6="http://www.coda.com/efinance/schemas/inputtemplate" xmlns:ns5="http://www.coda.com/efinance/schemas/common" xmlns:ns8="http://www.coda.com/efinance/schemas/elementmaster" xmlns:ns7="http://www.coda.com/efinance/schemas/documentmaster" xmlns:ns13="http://www.coda.com/common/schemas/attachment" xmlns:ns9="http://www.coda.com/efinance/schemas/matching" xmlns:ns12="http://www.coda.com/efinance/schemas/selector" xmlns:ns11="http://www.coda.com/efinance/schemas/selectormaster" xmlns:ns10="http://www.coda.com/efinance/schemas/association">
    <PostOptions postto="intray"/>
    <Transaction>
        <ns2:Header>
            <ns2:Key>
                <ns2:CmpCode>IT04</ns2:CmpCode>
                <ns2:Code>FE-EMI</ns2:Code>
            </ns2:Key>
            <ns2:TimeStamp>0</ns2:TimeStamp>
            <ns2:Period>2020/14</ns2:Period>
            <ns2:CurCode>EUR</ns2:CurCode>
            <ns2:Date>2020-08-31T00:00:00.000Z</ns2:Date>
        </ns2:Header>
        <ns2:Lines>
            <ns2:Line>
                <ns2:Number>1</ns2:Number>
                <ns2:AccountCode>IT04EUR.STAT.ITRCAR0.+.IT14000.ITCL11673</ns2:AccountCode>
                <ns2:DocValue>7254.94</ns2:DocValue>
                <ns2:LineType>summary</ns2:LineType>
                <ns2:LineSense>debit</ns2:LineSense>
                <ns2:Description>INDENN. LOCAZ.,ONE 01-08-20/31-08-20</ns2:Description>
                <ns2:DueDate>2020-08-31T00:00:00.000Z</ns2:DueDate>
                <ns2:DocSumTax>1308.27</ns2:DocSumTax>
                <ns2:MediaCode>I-BON</ns2:MediaCode>
                <ns2:BankCode>BITINTSP8770</ns2:BankCode>
                <ns2:ElmBankTag>2</ns2:ElmBankTag>
                <ns2:UserRef2>Trimestale</ns2:UserRef2>
                <ns2:UserRef3>Affitto e oneri 674 trimestre 2020</ns2:UserRef3>
                <ns2:ElmBankAccount>CAROSELLO32NBBQ</ns2:ElmBankAccount>
            </ns2:Line>
            <ns2:Line>
                <ns2:Number>2</ns2:Number>
                <ns2:AccountCode>IT04EUR.STAT.ITRCAR0.+.IT60001.ITYR2020</ns2:AccountCode>
                <ns2:DocValue>5414.43</ns2:DocValue>
                <ns2:LineType>analysis</ns2:LineType>
                <ns2:LineSense>credit</ns2:LineSense>
                <ns2:Description>INDENN. LOCAZ. 01-08-20/31-08-20</ns2:Description>
                <ns2:DueDate>2020-08-31T00:00:00.000Z</ns2:DueDate>
                <ns2:Taxes>
                    <ns2:Tax>
                        <ns2:Code>VTITV22</ns2:Code>
                        <ns2:ShortName>Iva vendite 22%</ns2:ShortName>
                        <ns2:Value>1191.18</ns2:Value>
                    </ns2:Tax>
                </ns2:Taxes>
            </ns2:Line>
            <ns2:Line>
                <ns2:Number>3</ns2:Number>
                <ns2:AccountCode>IT04EUR.STAT.ITRCAR0.+.IT60201.ITYR2020</ns2:AccountCode>
                <ns2:DocValue>532.24</ns2:DocValue>
                <ns2:LineType>analysis</ns2:LineType>
                <ns2:LineSense>credit</ns2:LineSense>
                <ns2:Description>ONERI 01-08-20/31-08-20</ns2:Description>
                <ns2:DueDate>2020-08-31T00:00:00.000Z</ns2:DueDate>
                <ns2:Taxes>
                    <ns2:Tax>
                        <ns2:Code>VTITV22</ns2:Code>
                        <ns2:ShortName>Iva vendite 22%</ns2:ShortName>
                        <ns2:Value>117.09</ns2:Value>
                    </ns2:Tax>
                </ns2:Taxes>
            </ns2:Line>
            <ns2:Line>
                <ns2:Number>4</ns2:Number>
                <ns2:AccountCode>IT04EUR.STAT.ITGGEN0.+.IT30501.VTITV22</ns2:AccountCode>
                <ns2:DocValue>1308.27</ns2:DocValue>
                <ns2:LineType>tax</ns2:LineType>
                <ns2:LineSense>credit</ns2:LineSense>
                <ns2:Description>Iva vendite 22%</ns2:Description>
                <ns2:DueDate>2020-08-31T00:00:00.000Z</ns2:DueDate>
                <ns2:TaxLineCode>VTITV22</ns2:TaxLineCode>
                <ns2:DocTaxTurnover>5946.67</ns2:DocTaxTurnover>
            </ns2:Line>
        </ns2:Lines>
    </Transaction>
    <PostData>
        <ns3:DocumentWideData>
            <ns3:ValueDate>2020-08-31T00:00:00.000Z</ns3:ValueDate>
            <ns3:ExtRef1>CAR-CC-0658</ns3:ExtRef1>
            <ns3:ExtRef2></ns3:ExtRef2>
            <ns3:ExtRef3>NON SOLO BRACE</ns3:ExtRef3>
            <ns3:ExtRef4>CAR-00032N</ns3:ExtRef4>
            <ns3:ExtRef5>CAR-BBQ-032N</ns3:ExtRef5>
        </ns3:DocumentWideData>
    </PostData>
</PostRequest>

//FRA OUTGOING
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<PostRequest xmlns="http://www.coda.com/efinance/schemas/inputext/input-12.0/webservice" xmlns:ns2="http://www.coda.com/efinance/schemas/transaction" xmlns:ns4="http://www.coda.com/efinance/schemas/browsedetails" xmlns:ns3="http://www.coda.com/efinance/schemas/inputext" xmlns:ns6="http://www.coda.com/efinance/schemas/inputtemplate" xmlns:ns5="http://www.coda.com/efinance/schemas/common" xmlns:ns8="http://www.coda.com/efinance/schemas/elementmaster" xmlns:ns7="http://www.coda.com/efinance/schemas/documentmaster" xmlns:ns13="http://www.coda.com/common/schemas/attachment" xmlns:ns9="http://www.coda.com/efinance/schemas/matching" xmlns:ns12="http://www.coda.com/efinance/schemas/selector" xmlns:ns11="http://www.coda.com/efinance/schemas/selectormaster" xmlns:ns10="http://www.coda.com/efinance/schemas/association">
    <PostOptions postto="intray"/>
    <Transaction>
        <ns2:Header>
            <ns2:Key>
                <ns2:CmpCode>FRLSM</ns2:CmpCode>
                <ns2:Code>FR_FACT_EMIS</ns2:Code>
            </ns2:Key>
            <ns2:TimeStamp>0</ns2:TimeStamp>
            <ns2:Period>2020/14</ns2:Period>
            <ns2:CurCode>EUR</ns2:CurCode>
            <ns2:Date>2020-08-05T00:00:00.000Z</ns2:Date>
        </ns2:Header>
        <ns2:Lines>
            <ns2:Line>
                <ns2:Number>1</ns2:Number>
                <ns2:AccountCode>FR01EUR.STAT.FRRHYR0.+.FR411010.FRCL10250</ns2:AccountCode>
                <ns2:DocValue>12867.82</ns2:DocValue>
                <ns2:LineType>summary</ns2:LineType>
                <ns2:LineSense>debit</ns2:LineSense>
                <ns2:Description>LOYER IO COMM 01/08/20-31/08/20</ns2:Description>
                <ns2:DueDate>2020-08-31T00:00:00.000Z</ns2:DueDate>
                <ns2:ValueDate>2020-08-05T00:00:00.000Z</ns2:ValueDate>

                <ns2:ExtRef1>AZ-9669</ns2:ExtRef1>
                <ns2:ExtRef2>01/08/2020 - 31/08/2020</ns2:ExtRef2>
                <ns2:ExtRef3>COURIR</ns2:ExtRef3>
                <ns2:ExtRef4>AZ-C10-C11</ns2:ExtRef4>
                <ns2:ExtRef5>AZ-COURIR</ns2:ExtRef5>
                <ns2:DocSumTax>2144.64</ns2:DocSumTax>
                <ns2:MediaCode>R-SDD</ns2:MediaCode>
                <ns2:BankCode>FR01BAING726</ns2:BankCode>
                <ns2:ElmBankTag>3</ns2:ElmBankTag>
                <ns2:SEPAMandateReference>AZ-COURIR</ns2:SEPAMandateReference>
            </ns2:Line>
            <ns2:Line>
                <ns2:Number>2</ns2:Number>
                <ns2:AccountCode>FR01EUR.STAT.FRRHYR0.+.FR411010.FRCL10250</ns2:AccountCode>
                <ns2:DocValue>1307.65</ns2:DocValue>
                <ns2:LineType>summary</ns2:LineType>
                <ns2:LineSense>debit</ns2:LineSense>
                <ns2:Description>CHARGES COPRO 01/08/20-31/08/20</ns2:Description>
                <ns2:DueDate>2020-08-31T00:00:00.000Z</ns2:DueDate>
                <ns2:ValueDate>2020-08-05T00:00:00.000Z</ns2:ValueDate>
                <ns2:ExtRef1>AZ-9669</ns2:ExtRef1>
                <ns2:ExtRef2>01/08/2020 - 31/08/2020</ns2:ExtRef2>
                <ns2:ExtRef3>COURIR</ns2:ExtRef3>
                <ns2:ExtRef4>AZ-C10-C11</ns2:ExtRef4>
                <ns2:ExtRef5>AZ-COURIR</ns2:ExtRef5>
                <ns2:DocSumTax>217.94</ns2:DocSumTax>
                <ns2:MediaCode>R-SDD</ns2:MediaCode>
                <ns2:BankCode>FR01BAING726</ns2:BankCode>
                <ns2:ElmBankTag>3</ns2:ElmBankTag>
                <ns2:SEPAMandateReference>AZ-COURIR</ns2:SEPAMandateReference>
            </ns2:Line>
            <ns2:Line>
                <ns2:Number>3</ns2:Number>
                <ns2:AccountCode>FR01EUR.STAT.FRRHYR0.+.FR411010.FRCL10250</ns2:AccountCode>
                <ns2:DocValue>541.70</ns2:DocValue>
                <ns2:LineType>summary</ns2:LineType>
                <ns2:LineSense>debit</ns2:LineSense>
                <ns2:Description>HONOGES 01/08/20-31/08/20</ns2:Description>
                <ns2:DueDate>2020-08-31T00:00:00.000Z</ns2:DueDate>
                <ns2:ValueDate>2020-08-05T00:00:00.000Z</ns2:ValueDate>
                <ns2:ExtRef1>AZ-9669</ns2:ExtRef1>
                <ns2:ExtRef2>01/08/2020 - 31/08/2020</ns2:ExtRef2>
                <ns2:ExtRef3>COURIR</ns2:ExtRef3>
                <ns2:ExtRef4>AZ-C10-C11</ns2:ExtRef4>
                <ns2:ExtRef5>AZ-COURIR</ns2:ExtRef5>
                <ns2:DocSumTax>90.28</ns2:DocSumTax>
                <ns2:MediaCode>R-SDD</ns2:MediaCode>
                <ns2:BankCode>FR01BAING726</ns2:BankCode>
                <ns2:ElmBankTag>3</ns2:ElmBankTag>
                <ns2:SEPAMandateReference>AZ-COURIR</ns2:SEPAMandateReference>
            </ns2:Line>
            <ns2:Line>
                <ns2:Number>4</ns2:Number>
                <ns2:AccountCode>FR01EUR.STAT.FRRHYR0.+.FR411010.FRCL10250</ns2:AccountCode>
                <ns2:DocValue>283.88</ns2:DocValue>
                <ns2:LineType>summary</ns2:LineType>
                <ns2:LineSense>debit</ns2:LineSense>
                <ns2:Description>CHARGES MKG 01/08/20-31/08/20</ns2:Description>
                <ns2:DueDate>2020-08-31T00:00:00.000Z</ns2:DueDate>
                <ns2:ValueDate>2020-08-05T00:00:00.000Z</ns2:ValueDate>
                <ns2:ExtRef1>AZ-9669</ns2:ExtRef1>
                <ns2:ExtRef2>01/08/2020 - 31/08/2020</ns2:ExtRef2>
                <ns2:ExtRef3>COURIR</ns2:ExtRef3>
                <ns2:ExtRef4>AZ-C10-C11</ns2:ExtRef4>
                <ns2:ExtRef5>AZ-COURIR</ns2:ExtRef5>
                <ns2:DocSumTax>47.31</ns2:DocSumTax>
                <ns2:MediaCode>R-SDD</ns2:MediaCode>
                <ns2:BankCode>FR01BAING726</ns2:BankCode>
                <ns2:ElmBankTag>3</ns2:ElmBankTag>
                <ns2:SEPAMandateReference>AZ-COURIR</ns2:SEPAMandateReference>
            </ns2:Line>
            <ns2:Line>
                <ns2:Number>5</ns2:Number>
                <ns2:AccountCode>FR01EUR.STAT.FRRHYR0.+.FR706000</ns2:AccountCode>
                <ns2:DocValue>10723.18</ns2:DocValue>
                <ns2:LineType>analysis</ns2:LineType>
                <ns2:LineSense>credit</ns2:LineSense>
                <ns2:Description>LOYER IO COMM 01/08/20-31/08/20</ns2:Description>
                <ns2:ValueDate>2020-08-05T00:00:00.000Z</ns2:ValueDate>
                <ns2:ExtRef1>AZ-9669</ns2:ExtRef1>
                <ns2:ExtRef2>01/08/2020 - 31/08/2020</ns2:ExtRef2>
                <ns2:ExtRef3>COURIR</ns2:ExtRef3>
                <ns2:ExtRef4>AZ-C10-C11</ns2:ExtRef4>
                <ns2:ExtRef5>AZ-COURIR</ns2:ExtRef5>
                <ns2:Taxes>
                    <ns2:Tax>
                        <ns2:Code>VTFRFE19_6</ns2:Code>
                        <ns2:Value>2144.64</ns2:Value>
                    </ns2:Tax>
                </ns2:Taxes>
            </ns2:Line>
            <ns2:Line>
                <ns2:Number>6</ns2:Number>
                <ns2:AccountCode>FR01EUR.STAT.FRRHYR0.+.FR467216.FRYR2020</ns2:AccountCode>
                <ns2:DocValue>1089.71</ns2:DocValue>
                <ns2:LineType>analysis</ns2:LineType>
                <ns2:LineSense>credit</ns2:LineSense>
                <ns2:Description>CHARGES COPRO 01/08/20-31/08/20</ns2:Description>
                <ns2:ValueDate>2020-08-05T00:00:00.000Z</ns2:ValueDate>
                <ns2:ExtRef1>AZ-9669</ns2:ExtRef1>
                <ns2:ExtRef2>01/08/2020 - 31/08/2020</ns2:ExtRef2>
                <ns2:ExtRef3>COURIR</ns2:ExtRef3>
                <ns2:ExtRef4>AZ-C10-C11</ns2:ExtRef4>
                <ns2:ExtRef5>AZ-COURIR</ns2:ExtRef5>
                <ns2:Taxes>
                    <ns2:Tax>
                        <ns2:Code>VTFRFE19_6</ns2:Code>
                        <ns2:Value>217.94</ns2:Value>
                    </ns2:Tax>
                </ns2:Taxes>
            </ns2:Line>
            <ns2:Line>
                <ns2:Number>7</ns2:Number>
                <ns2:AccountCode>FR01EUR.STAT.FRRHYR0.+.FR708802.FRYR2020</ns2:AccountCode>
                <ns2:DocValue>451.42</ns2:DocValue>
                <ns2:LineType>analysis</ns2:LineType>
                <ns2:LineSense>credit</ns2:LineSense>
                <ns2:Description>HONOGES 01/08/20-31/08/20</ns2:Description>
                <ns2:ValueDate>2020-08-05T00:00:00.000Z</ns2:ValueDate>
                <ns2:ExtRef1>AZ-9669</ns2:ExtRef1>
                <ns2:ExtRef2>01/08/2020 - 31/08/2020</ns2:ExtRef2>
                <ns2:ExtRef3>COURIR</ns2:ExtRef3>
                <ns2:ExtRef4>AZ-C10-C11</ns2:ExtRef4>
                <ns2:ExtRef5>AZ-COURIR</ns2:ExtRef5>
                <ns2:Taxes>
                    <ns2:Tax>
                        <ns2:Code>VTFRFE19_6</ns2:Code>
                        <ns2:Value>90.28</ns2:Value>
                    </ns2:Tax>
                </ns2:Taxes>
            </ns2:Line>
            <ns2:Line>
                <ns2:Number>8</ns2:Number>
                <ns2:AccountCode>FR01EUR.STAT.FRRHYR0.+.FR467316</ns2:AccountCode>
                <ns2:DocValue>236.57</ns2:DocValue>
                <ns2:LineType>analysis</ns2:LineType>
                <ns2:LineSense>credit</ns2:LineSense>
                <ns2:Description>CHARGES MKG 01/08/20-31/08/20</ns2:Description>
                <ns2:ValueDate>2020-08-05T00:00:00.000Z</ns2:ValueDate>
                <ns2:ExtRef1>AZ-9669</ns2:ExtRef1>
                <ns2:ExtRef2>01/08/2020 - 31/08/2020</ns2:ExtRef2>
                <ns2:ExtRef3>COURIR</ns2:ExtRef3>
                <ns2:ExtRef4>AZ-C10-C11</ns2:ExtRef4>
                <ns2:ExtRef5>AZ-COURIR</ns2:ExtRef5>
                <ns2:Taxes>
                    <ns2:Tax>
                        <ns2:Code>VTFRFE19_6</ns2:Code>
                        <ns2:Value>47.31</ns2:Value>
                    </ns2:Tax>
                </ns2:Taxes>
            </ns2:Line>
            <ns2:Line>
                <ns2:Number>9</ns2:Number>
                <ns2:AccountCode>FR01EUR.STAT.FRRHYR0.+.FR445710.VTFRFE19_6</ns2:AccountCode>
                <ns2:DocValue>2500.17</ns2:DocValue>
                <ns2:LineType>tax</ns2:LineType>
                <ns2:LineSense>credit</ns2:LineSense>
                <ns2:Description>TURNOVER VAT 19.6%</ns2:Description>
                <ns2:ValueDate>2020-08-05T00:00:00.000Z</ns2:ValueDate>
                <ns2:ExtRef1>AZ-9669</ns2:ExtRef1>
                <ns2:ExtRef2>01/08/2020 - 31/08/2020</ns2:ExtRef2>
                <ns2:ExtRef3>COURIR</ns2:ExtRef3>
                <ns2:ExtRef4>AZ-C10-C11</ns2:ExtRef4>
                <ns2:ExtRef5>AZ-COURIR</ns2:ExtRef5>
                <ns2:TaxLineCode>VTFRFE19_6</ns2:TaxLineCode>
                <ns2:DocTaxTurnover>12500.88</ns2:DocTaxTurnover>
            </ns2:Line>
        </ns2:Lines>
    </Transaction>
    <PostData/>
</PostRequest>

//FRA INCOMING

* */

import java.util.SortedSet;
import java.util.TreeSet;

import lombok.Getter;
import lombok.Setter;

public class CodaTransaction {

    @Getter @Setter
    private CodaTransactionHeader header;

    @Getter @Setter
    private SortedSet<CodaTransactionLine> lines = new TreeSet<>();

    // ITA only
    // TODO: we could also leave this to Camel and just fill in the data on the transaction lines ....
    @Getter @Setter
    private CodaDocumentWideData documentWideData;

}
