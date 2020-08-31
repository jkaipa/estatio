package org.estatio.module.coda.dom.transaction;

//<PostData>
//        <ns3:DocumentWideData>
//            <ns3:ValueDate>2020-08-31T00:00:00.000Z</ns3:ValueDate>
//            <ns3:ExtRef1>CAR-CC-0658</ns3:ExtRef1>
//            <ns3:ExtRef2></ns3:ExtRef2>
//            <ns3:ExtRef3>NON SOLO BRACE</ns3:ExtRef3>
//            <ns3:ExtRef4>CAR-00032N</ns3:ExtRef4>
//            <ns3:ExtRef5>CAR-BBQ-032N</ns3:ExtRef5>
//        </ns3:DocumentWideData>
//    </PostData>

import org.joda.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

public class CodaDocumentWideData {

    @Getter @Setter
    public LocalDate valueDate;

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

}
