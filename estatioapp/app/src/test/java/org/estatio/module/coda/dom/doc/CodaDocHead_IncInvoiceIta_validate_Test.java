package org.estatio.module.coda.dom.doc;

import java.util.TreeSet;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CodaDocHead_IncInvoiceIta_validate_Test {

    private CodaDocHeadIncInvoiceIta codaDocHeadIncInvoiceIta;
    private CodaDocLine line1;
    private CodaDocLine line2;

    @Before
    public void setUp() throws Exception {

        codaDocHeadIncInvoiceIta = new CodaDocHeadIncInvoiceIta("IT01", "FR-GEN", "123", (short)1, LocalDate.now(), LocalDate.now(), "2019/1", "books", "SHA256", "");
        codaDocHeadIncInvoiceIta.setLines(new TreeSet<>());

        line1 = new CodaDocLine();
        line1.setDocHead(codaDocHeadIncInvoiceIta);
        line1.setLineNum(1);

        line2 = new CodaDocLine();
        line2.setDocHead(codaDocHeadIncInvoiceIta);
        line2.setLineNum(2);

        codaDocHeadIncInvoiceIta.getLines().add(line1);
        codaDocHeadIncInvoiceIta.getLines().add(line2);
    }

    @Test
    public void all_lines_valid() throws Exception {
        // given
        assertThat(codaDocHeadIncInvoiceIta.getLineValidationStatus()).isEqualTo(ValidationStatus.NOT_CHECKED);
        line1.setReasonInvalid(null);
        line2.setReasonInvalid(null);

        // when
        codaDocHeadIncInvoiceIta.validateLines();

        // then
        assertThat(codaDocHeadIncInvoiceIta.getLineValidationStatus()).isEqualTo(ValidationStatus.VALID);
    }

    @Test
    public void some_lines_invalid() throws Exception {

        // given
        assertThat(codaDocHeadIncInvoiceIta.getLineValidationStatus()).isEqualTo(ValidationStatus.NOT_CHECKED);
        line1.setReasonInvalid("Project not found for code: 'ITPR239'\n"
                + "Charge not found for old-style work type: 'OLD005'\n"
                + "Order not found for order num: '4111/GD5/006/239'");
        line2.setReasonInvalid(null);

        // when
        codaDocHeadIncInvoiceIta.validateLines();

        // then
        assertThat(codaDocHeadIncInvoiceIta.getLineValidationStatus()).isEqualTo(ValidationStatus.INVALID);
    }

}