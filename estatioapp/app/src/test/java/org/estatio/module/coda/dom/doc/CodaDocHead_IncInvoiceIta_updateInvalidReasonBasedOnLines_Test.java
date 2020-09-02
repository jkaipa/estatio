package org.estatio.module.coda.dom.doc;

import com.google.common.collect.Sets;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CodaDocHead_IncInvoiceIta_updateInvalidReasonBasedOnLines_Test {

    private CodaDocHeadIncInvoiceIta codaDocHeadIncInvoiceIta;
    private CodaDocLine line1;
    private CodaDocLine line2;

    @Before
    public void setUp() throws Exception {
        this.codaDocHeadIncInvoiceIta = new CodaDocHeadIncInvoiceIta("IT01","FR-GEN","12345", (short)1, null, null, null, null, "SHA256", "");
        this.codaDocHeadIncInvoiceIta.setLines(Sets.newTreeSet());

        CodaDocHeadIncInvoiceIta codaDocHeadIncInvoiceIta = this.codaDocHeadIncInvoiceIta;
        line1 = addLine(codaDocHeadIncInvoiceIta, 1);
        line2 = addLine(codaDocHeadIncInvoiceIta, 2);
    }

    @Test
    public void when_no_lines_are_invalid() throws Exception {
        // given
        assertThat(codaDocHeadIncInvoiceIta.getReasonInvalid()).isNull();

        // when
        codaDocHeadIncInvoiceIta.updateInvalidReasonBasedOnLines();

        // then
        assertThat(codaDocHeadIncInvoiceIta.getReasonInvalid()).isNull();
    }

    @Test
    public void when_some_lines_are_invalid() throws Exception {
        // given
        line1.setLineType(LineType.SUMMARY);
        line1.setReasonInvalid("line 1 is bad");
        line2.setLineType(LineType.ANALYSIS);
        line2.setReasonInvalid("line 2 is bad");
        assertThat(codaDocHeadIncInvoiceIta.getReasonInvalid()).isNull();

        // when
        codaDocHeadIncInvoiceIta.updateInvalidReasonBasedOnLines();

        // then
        assertThat(codaDocHeadIncInvoiceIta.getReasonInvalid()).isNotNull();
        assertThat(codaDocHeadIncInvoiceIta.getReasonInvalid()).isEqualTo(
                "Line #1 (SUMMARY): line 1 is bad\n" +
                "Line #2 (ANALYSIS): line 2 is bad"   );
    }


    static CodaDocLine addLine(final CodaDocHeadIncInvoiceIta codaDocHeadIncInvoiceIta, final int lineNum) {
        final CodaDocLine docLine = new CodaDocLine();
        docLine.setLineNum(lineNum);
        docLine.setDocHead(codaDocHeadIncInvoiceIta);
        codaDocHeadIncInvoiceIta.getLines().add(docLine);
        return docLine;
    }

}