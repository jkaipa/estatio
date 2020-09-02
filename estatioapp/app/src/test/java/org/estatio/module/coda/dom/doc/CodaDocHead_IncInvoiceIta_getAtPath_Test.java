package org.estatio.module.coda.dom.doc;

import org.junit.Before;
import org.junit.Test;

import org.estatio.module.party.dom.Organisation;

import static org.assertj.core.api.Assertions.assertThat;

public class CodaDocHead_IncInvoiceIta_getAtPath_Test {

    CodaDocHeadIncInvoiceIta codaDocHeadIncInvoiceIta;
    @Before
    public void setUp() throws Exception {
        codaDocHeadIncInvoiceIta = new CodaDocHeadIncInvoiceIta();
    }

    @Test
    public void when_undefined() throws Exception {
        // given
        codaDocHeadIncInvoiceIta.setCmpCodeBuyer(null);
        codaDocHeadIncInvoiceIta.setCmpCode(null);

        // when, then
        assertThat(codaDocHeadIncInvoiceIta.getAtPath()).isEqualTo("/");
    }
    @Test
    public void when_cmpCodeBuyer_is_known() throws Exception {
        // given
        final Organisation cmpCodeBuyer = new Organisation();
        cmpCodeBuyer.setApplicationTenancyPath("/XXX");

        codaDocHeadIncInvoiceIta.setCmpCodeBuyer(cmpCodeBuyer);

        // then
        assertThat(codaDocHeadIncInvoiceIta.getAtPath()).isEqualTo("/XXX");
    }

    @Test
    public void when_cmpCodeBuyer_unknown_but_cmpCode_is_IT() throws Exception {
        // given
        codaDocHeadIncInvoiceIta.setCmpCodeBuyer(null);
        codaDocHeadIncInvoiceIta.setCmpCode("ITxxxxxxxx");

        // then
        assertThat(codaDocHeadIncInvoiceIta.getAtPath()).isEqualTo("/ITA");
    }

    @Test
    public void when_cmpCodeBuyer_unknown_but_cmpCode_is_FR() throws Exception {
        // given
        codaDocHeadIncInvoiceIta.setCmpCodeBuyer(null);
        codaDocHeadIncInvoiceIta.setCmpCode("FRxxxxxxxx");

        // then
        assertThat(codaDocHeadIncInvoiceIta.getAtPath()).isEqualTo("/FRA");
    }

    @Test
    public void when_cmpCodeBuyer_unknown_but_cmpCode_is_something_else() throws Exception {
        // given
        codaDocHeadIncInvoiceIta.setCmpCodeBuyer(null);
        codaDocHeadIncInvoiceIta.setCmpCode("ECPNV");

        // then
        assertThat(codaDocHeadIncInvoiceIta.getAtPath()).isEqualTo("/");
    }
}