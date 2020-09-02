package org.estatio.module.coda.dom.doc;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CodaDocHead_IncInvoiceIta_isSameAs_Test {

    CodaDocHeadIncInvoiceIta codaDocHeadIncInvoiceIta;
    boolean codaDocHeadLegacyState;

    CodaDocHeadIncInvoiceIta otherDocHead;
    boolean otherDocHeadLegacyState;

    @Before
    public void setUp() throws Exception {
        codaDocHeadIncInvoiceIta = new CodaDocHeadIncInvoiceIta() {
            @Override boolean isLegacyAnalysisLineWithNullDocValue() {
                return codaDocHeadLegacyState;
            }
        };
        codaDocHeadIncInvoiceIta.setSha256("SHA256");
        codaDocHeadIncInvoiceIta.setStatPay("");
        codaDocHeadLegacyState = false;

        otherDocHead = new CodaDocHeadIncInvoiceIta() {
            @Override boolean isLegacyAnalysisLineWithNullDocValue() {
                return otherDocHeadLegacyState;
            }
        };
        otherDocHead.setSha256(codaDocHeadIncInvoiceIta.getSha256()); // start off as equal
        otherDocHead.setStatPay(codaDocHeadIncInvoiceIta.getStatPay());
        otherDocHeadLegacyState = false;
    }

    @Test
    public void when_null() throws Exception {
        // given
        otherDocHead = null;

        // when
        assertThat(codaDocHeadIncInvoiceIta.isSameAs(otherDocHead)).isFalse();
    }

    @Test
    public void when_same() throws Exception {
        // given
        otherDocHead = codaDocHeadIncInvoiceIta;

        // when
        assertThat(codaDocHeadIncInvoiceIta.isSameAs(otherDocHead)).isTrue();
    }

    @Test
    public void when_equal() throws Exception {
        // given
        assertThat(codaDocHeadIncInvoiceIta.getSha256()).isEqualTo(otherDocHead.getSha256());
        assertThat(codaDocHeadIncInvoiceIta.getStatPay()).isEqualTo(otherDocHead.getStatPay());
        assertThat(codaDocHeadIncInvoiceIta.isLegacyAnalysisLineWithNullDocValue()).isEqualTo(otherDocHead.isLegacyAnalysisLineWithNullDocValue());

        // when
        assertThat(codaDocHeadIncInvoiceIta.isSameAs(otherDocHead)).isTrue();
    }

    @Test
    public void when_different_sha256() throws Exception {
        // given
        otherDocHead.setSha256("DIFFERENT_SHA256");
        assertThat(codaDocHeadIncInvoiceIta.getStatPay()).isEqualTo(otherDocHead.getStatPay());
        assertThat(codaDocHeadIncInvoiceIta.isLegacyAnalysisLineWithNullDocValue()).isEqualTo(otherDocHead.isLegacyAnalysisLineWithNullDocValue());

        // when
        assertThat(codaDocHeadIncInvoiceIta.isSameAs(otherDocHead)).isFalse();
    }

    @Test
    public void when_different_statPay() throws Exception {
        // given
        assertThat(codaDocHeadIncInvoiceIta.getSha256()).isEqualTo(otherDocHead.getSha256());
        codaDocHeadIncInvoiceIta.setStatPay("");
        otherDocHead.setStatPay("paid");
        assertThat(codaDocHeadIncInvoiceIta.isLegacyAnalysisLineWithNullDocValue()).isEqualTo(otherDocHead.isLegacyAnalysisLineWithNullDocValue());

        // when
        assertThat(codaDocHeadIncInvoiceIta.isSameAs(otherDocHead)).isFalse();
    }

    @Test
    public void when_different_legacyState_1() throws Exception {
        // given
        assertThat(codaDocHeadIncInvoiceIta.getSha256()).isEqualTo(otherDocHead.getSha256());
        assertThat(codaDocHeadIncInvoiceIta.getStatPay()).isEqualTo(otherDocHead.getStatPay());

        codaDocHeadLegacyState = false;
        otherDocHeadLegacyState = true;

        // when, then
        assertThat(codaDocHeadIncInvoiceIta.isSameAs(otherDocHead)).isFalse();
    }

    @Test
    public void when_different_legacyState_2() throws Exception {
        // given
        assertThat(codaDocHeadIncInvoiceIta.getSha256()).isEqualTo(otherDocHead.getSha256());
        assertThat(codaDocHeadIncInvoiceIta.getStatPay()).isEqualTo(otherDocHead.getStatPay());

        codaDocHeadLegacyState = true;
        otherDocHeadLegacyState = false;

        // when, then
        assertThat(codaDocHeadIncInvoiceIta.isSameAs(otherDocHead)).isFalse();
    }


}