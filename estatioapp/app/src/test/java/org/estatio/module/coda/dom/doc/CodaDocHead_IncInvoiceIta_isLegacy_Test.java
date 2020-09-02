package org.estatio.module.coda.dom.doc;

import java.math.BigDecimal;

import com.google.common.collect.Sets;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CodaDocHead_IncInvoiceIta_isLegacy_Test {

    CodaDocHeadIncInvoiceIta codaDocHeadIncInvoiceIta;
    CodaDocLine summaryLine;
    CodaDocLine analysisLine;

    @Before
    public void setUp() throws Exception {
        codaDocHeadIncInvoiceIta = new CodaDocHeadIncInvoiceIta();
        codaDocHeadIncInvoiceIta.setCmpCode("IT01");
        codaDocHeadIncInvoiceIta.setDocCode("FR-GEN");
        codaDocHeadIncInvoiceIta.setDocNum("12345");
        codaDocHeadIncInvoiceIta.setLines(Sets.newTreeSet());

        summaryLine = newLine(this.codaDocHeadIncInvoiceIta, 1, LineType.SUMMARY);
        analysisLine = newLine(this.codaDocHeadIncInvoiceIta, 2, LineType.ANALYSIS);
    }

    private CodaDocLine newLine(
            final CodaDocHeadIncInvoiceIta codaDocHeadIncInvoiceIta,
            final int lineNum,
            final LineType lineType) {
        final CodaDocLine line = new CodaDocLine();
        line.setLineType(lineType);
        line.setDocHead(codaDocHeadIncInvoiceIta);
        line.setLineNum(lineNum);

        codaDocHeadIncInvoiceIta.getLines().add(line);
        return line;
    }

    @Test
    public void given_no_doc_value_on_analysis_line_then_treat_as_legacy() throws Exception {

        // given
        analysisLine.setDocValue(null);

        // when, then
        assertThat(codaDocHeadIncInvoiceIta.isLegacyAnalysisLineWithNullDocValue()).isTrue();

    }

    @Test
    public void given_non_null_value_on_analysis_line_then_not_legacy() throws Exception {

        // given
        analysisLine.setDocValue(BigDecimal.ONE);

        // when, then
        assertThat(codaDocHeadIncInvoiceIta.isLegacyAnalysisLineWithNullDocValue()).isFalse();
    }

    @Test
    public void given_zero_value_on_analysis_line_then_not_legacy() throws Exception {

        // given
        analysisLine.setDocValue(BigDecimal.ZERO);  // can even be zero, still not legacy.

        // when, then
        assertThat(codaDocHeadIncInvoiceIta.isLegacyAnalysisLineWithNullDocValue()).isFalse();
    }

    @Test
    public void given_no_lines_then_not_legacy() throws Exception {
        // given
        codaDocHeadIncInvoiceIta.getLines().clear();

        // when, then
        assertThat(codaDocHeadIncInvoiceIta.isLegacyAnalysisLineWithNullDocValue()).isFalse();
    }

}