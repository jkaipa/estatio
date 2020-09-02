package org.estatio.module.coda.dom.doc;

import com.google.common.collect.Sets;

import org.assertj.core.api.Assertions;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

public class CodaDocHead_IncInvoiceIta_summaryLine_Test {

    private CodaDocHeadIncInvoiceIta codaDocHeadIncInvoiceIta;

    @Before
    public void setUp() throws Exception {
        codaDocHeadIncInvoiceIta = new CodaDocHeadIncInvoiceIta("IT01", "FR-GEN", "12345", (short)1, LocalDate.now(), LocalDate.now(), "2019/1", "books", "SHA256", "");
        codaDocHeadIncInvoiceIta.setLines(Sets.newTreeSet());
    }

    @Test
    public void when_one() throws Exception {
        // given
        final CodaDocLine line1 = addLine(codaDocHeadIncInvoiceIta, 1, LineType.SUMMARY);

        // when
        final CodaDocLine codaDocLine = codaDocHeadIncInvoiceIta.summaryDocLine(LineCache.DEFAULT);

        // then
        Assertions.assertThat(codaDocLine).isNotNull();
        Assertions.assertThat(codaDocLine).isSameAs(line1);
        Assertions.assertThat(codaDocLine.getLineType()).isEqualTo(LineType.SUMMARY);
    }

    @Test
    public void when_none() throws Exception {

        // when
        final CodaDocLine codaDocLine = codaDocHeadIncInvoiceIta.summaryDocLine(LineCache.DEFAULT);

        // then
        Assertions.assertThat(codaDocLine).isNull();

    }

    @Test
    public void when_several() throws Exception {
        // given
        final CodaDocLine line0 = addLine(codaDocHeadIncInvoiceIta, 1, LineType.ANALYSIS); // force the point that it finds first
        final CodaDocLine line1 = addLine(codaDocHeadIncInvoiceIta, 2, LineType.SUMMARY);
        final CodaDocLine line2 = addLine(codaDocHeadIncInvoiceIta, 3, LineType.ANALYSIS);
        final CodaDocLine line3 = addLine(codaDocHeadIncInvoiceIta, 4, LineType.SUMMARY);

        // when
        final CodaDocLine codaDocLine = codaDocHeadIncInvoiceIta.summaryDocLine(LineCache.DEFAULT);

        // then
        Assertions.assertThat(codaDocLine).isNotNull();
        Assertions.assertThat(codaDocLine).isSameAs(line1);
        Assertions.assertThat(codaDocLine.getLineType()).isEqualTo(LineType.SUMMARY);
    }

    CodaDocLine addLine(final CodaDocHeadIncInvoiceIta codaDocHeadIncInvoiceIta, final int lineNum, final LineType lineType) {
        final CodaDocLine line = new CodaDocLine();
        line.setDocHead(codaDocHeadIncInvoiceIta);
        line.setLineNum(lineNum);

        line.setLineType(lineType);

        codaDocHeadIncInvoiceIta.getLines().add(line);
        return line;
    }


}