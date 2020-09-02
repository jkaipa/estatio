package org.estatio.module.coda.dom.doc;

import java.util.Map;

import org.incode.module.document.dom.impl.paperclips.Paperclip;

import org.estatio.module.capex.dom.invoice.IncomingInvoice;

import lombok.Getter;

public class Memento {

    @Getter
    private final CodaDocHeadIncInvoiceIta codaDocHeadIncInvoiceItaIfAny;
    @Getter
    private final IncomingInvoice incomingInvoiceIfAny;
    @Getter
    private final Map<Integer, LineData> analysisLineDataByLineNumberIfAny;
    @Getter
    private final String documentNameIfAny;
    @Getter
    private final Paperclip paperclipIfAny;

    public Memento(
            final CodaDocHeadIncInvoiceIta codaDocHeadIncInvoiceItaIfAny,
            final DerivedObjectLookup derivedObjectLookup) {

        this.codaDocHeadIncInvoiceItaIfAny = codaDocHeadIncInvoiceItaIfAny;

        if(this.codaDocHeadIncInvoiceItaIfAny == null) {
            this.incomingInvoiceIfAny = null;
            this.analysisLineDataByLineNumberIfAny = null;
            this.documentNameIfAny = null;
            this.paperclipIfAny = null;
        } else {
            this.incomingInvoiceIfAny = codaDocHeadIncInvoiceItaIfAny.getIncomingInvoice();
            this.analysisLineDataByLineNumberIfAny = codaDocHeadIncInvoiceItaIfAny.getAnalysisLineDataByLineNumber();
            this.documentNameIfAny = derivedObjectLookup.documentNameIfAnyFrom(codaDocHeadIncInvoiceItaIfAny);
            this.paperclipIfAny = derivedObjectLookup.paperclipIfAnyFrom(codaDocHeadIncInvoiceItaIfAny);
        }
    }

}
