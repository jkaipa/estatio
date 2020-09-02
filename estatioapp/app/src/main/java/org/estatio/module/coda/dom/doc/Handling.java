package org.estatio.module.coda.dom.doc;

import org.estatio.module.capex.dom.invoice.IncomingInvoice;

public enum Handling {
    /**
     * The {@link CodaDocHeadIncInvoiceIta Coda document} has been marked as to be ignored; it will not be validated nor sync'd.
     */
    EXCLUDED("Excluded"),
    /**
     * The {@link CodaDocHeadIncInvoiceIta Coda document} should not be ignored.
     *
     * <p>
     *     At this point it may or may not have validation errors (see {@link CodaDocHeadIncInvoiceIta#isValid()}), but it has
     *     not been sync'd.
     * </p>
     */
    INCLUDED("Estatio invoice not yet created"),
    /**
     * The {@link CodaDocHeadIncInvoiceIta Coda document} was {@link CodaDocHeadIncInvoiceIta#isValid() valid} and an
     * {@link IncomingInvoice Estatio invoice} (and related objects) has been created for it.
     *
     * <p>
     *     It is no longer possible to exclude a Coda document once it has sync'd.
     * </p>
     */
    SYNCED("Estatio invoice created"),
    ;

    private final String title;

    Handling(final String title) {
        this.title = title;
    }
    public String title() {
        return title;
    }
    public String toString() {
        return title;
    }

}
