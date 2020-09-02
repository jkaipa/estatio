package org.estatio.module.coda.dom.doc;

import java.math.BigDecimal;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;

import org.estatio.module.capex.dom.invoice.IncomingInvoice;
import org.estatio.module.capex.dom.invoice.approval.IncomingInvoiceApprovalState;
import org.estatio.module.financial.dom.BankAccount;

import static org.assertj.core.api.Assertions.assertThat;

public class CodaDocHead_IncInvoiceIta_compareWith_Test {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(JUnitRuleMockery2.Mode.INTERFACES_AND_CLASSES);

    CodaDocHeadIncInvoiceIta codaDocHeadIncInvoiceIta;
    CodaDocLine summaryDocLine;
    CodaDocLine analysisDocLine;
    CodaDocLine analysisDocLine2;

    CodaDocHeadIncInvoiceIta existing;
    CodaDocLine existingSummaryDocLine;
    CodaDocLine existingAnalysisDocLine;
    CodaDocLine existingAnalysisDocLine2;

    @Before
    public void setUp() throws Exception {

        existing = new CodaDocHeadIncInvoiceIta("IT01", "FR-GEN", "1234", (short)1, null, null, null, null, "SHA256_1", "");

        existingSummaryDocLine = addLine(existing, 1, LineType.SUMMARY);
        existingAnalysisDocLine = addLine(existing, 2, LineType.ANALYSIS);


        codaDocHeadIncInvoiceIta = new CodaDocHeadIncInvoiceIta("IT01", "FR-GEN", "1234", (short)2, null, null, null, null, "SHA256_2", "");

        summaryDocLine = addLine(codaDocHeadIncInvoiceIta, 1, LineType.SUMMARY);
        analysisDocLine = addLine(codaDocHeadIncInvoiceIta, 2, LineType.ANALYSIS);
    }

    CodaDocLine addLine(final CodaDocHeadIncInvoiceIta docHead, final int lineNum, final LineType lineType) {
        CodaDocLine codaDocLine = new CodaDocLine(docHead, lineNum, lineType, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        docHead.getLines().add(codaDocLine);
        return codaDocLine;
    }

    @Test
    public void when_no_previous() throws Exception {

        // when
        CodaDocHeadIncInvoiceIta.Comparison comparison = codaDocHeadIncInvoiceIta.compareWith(null);

        // then
        assertThat(comparison.getType()).isEqualTo(CodaDocHeadIncInvoiceIta.Comparison.Type.NO_PREVIOUS);
    }

    @Test
    public void when_same() throws Exception {
        // when
        CodaDocHeadIncInvoiceIta.Comparison comparison = codaDocHeadIncInvoiceIta.compareWith(codaDocHeadIncInvoiceIta);

        // then
        assertThat(comparison.getType()).isEqualTo(CodaDocHeadIncInvoiceIta.Comparison.Type.SAME);
    }

    @Test
    public void when_differs_but_same_by_sha256() throws Exception {
        // given
        existing.setSha256(codaDocHeadIncInvoiceIta.getSha256());

        // when
        CodaDocHeadIncInvoiceIta.Comparison comparison = codaDocHeadIncInvoiceIta.compareWith(existing);

        // then
        assertThat(comparison.getType()).isEqualTo(CodaDocHeadIncInvoiceIta.Comparison.Type.SAME);
    }

    @Test
    public void when_replacement_missing_summary_line() throws Exception {

        // given
        codaDocHeadIncInvoiceIta.getLines().remove(summaryDocLine);

        // when
        CodaDocHeadIncInvoiceIta.Comparison comparison = codaDocHeadIncInvoiceIta.compareWith(existing);

        // then
        assertThat(comparison.getType()).isEqualTo(CodaDocHeadIncInvoiceIta.Comparison.Type.DIFFERS_INVALIDATING_APPROVALS);
        assertThat(comparison.getReason()).isEqualTo("Replacement has no summary doc line");
    }


    @Test
    public void when_replacement_adds_summary_line() throws Exception {
        // given
        existing.getLines().remove(existingSummaryDocLine);

        // when
        CodaDocHeadIncInvoiceIta.Comparison comparison = codaDocHeadIncInvoiceIta.compareWith(existing);

        // then
        assertThat(comparison.getType()).isEqualTo(CodaDocHeadIncInvoiceIta.Comparison.Type.DIFFERS_INVALIDATING_APPROVALS);
        assertThat(comparison.getReason()).isEqualTo("Previous had no summary doc line");
    }

    @Test
    public void when_supplier_bank_account_differs_and_incoming_invoice_in_state_of_payable_or_paid() throws Exception {

        // given
        summaryDocLine.setSupplierBankAccount(new BankAccount());
        summaryDocLine.setSupplierBankAccountValidationStatus(ValidationStatus.VALID);
        existingSummaryDocLine.setSupplierBankAccount(new BankAccount());
        existingSummaryDocLine.setSupplierBankAccountValidationStatus(ValidationStatus.VALID);
        IncomingInvoice incomingInvoice = new IncomingInvoice();
        incomingInvoice.setApprovalState(IncomingInvoiceApprovalState.PAYABLE);
        existing.setIncomingInvoice(incomingInvoice);

        // when
        CodaDocHeadIncInvoiceIta.Comparison comparison = codaDocHeadIncInvoiceIta.compareWith(existing);

        // then
        assertThat(comparison.getType()).isEqualTo(CodaDocHeadIncInvoiceIta.Comparison.Type.DIFFERS_INVALIDATING_APPROVALS);
        assertThat(comparison.getReason()).isEqualTo("Line #1 (SUMMARY): Supplier bank account has changed");

        // and when
        incomingInvoice.setApprovalState(IncomingInvoiceApprovalState.PAID);
        comparison = codaDocHeadIncInvoiceIta.compareWith(existing);
        // then also
        assertThat(comparison.getType()).isEqualTo(CodaDocHeadIncInvoiceIta.Comparison.Type.DIFFERS_INVALIDATING_APPROVALS);
        assertThat(comparison.getReason()).isEqualTo("Line #1 (SUMMARY): Supplier bank account has changed");

        // but when
        incomingInvoice.setApprovalState(IncomingInvoiceApprovalState.PENDING_CODA_BOOKS_CHECK); // last state before payable
        comparison = codaDocHeadIncInvoiceIta.compareWith(existing);
        // then
        assertThat(comparison.getType()).isEqualTo(CodaDocHeadIncInvoiceIta.Comparison.Type.DIFFERS_RETAIN_APPROVALS);
    }

    @Test
    public void when_supplier_bank_account_not_checked() throws Exception {

        // given
        summaryDocLine.setSupplierBankAccount(null);
        summaryDocLine.setSupplierBankAccountValidationStatus(ValidationStatus.NOT_CHECKED);
        existingSummaryDocLine.setSupplierBankAccount(new BankAccount());
        existingSummaryDocLine.setSupplierBankAccountValidationStatus(ValidationStatus.VALID);

        // when
        CodaDocHeadIncInvoiceIta.Comparison comparison = codaDocHeadIncInvoiceIta.compareWith(existing);

        // then
        assertThat(comparison.getType()).isEqualTo(CodaDocHeadIncInvoiceIta.Comparison.Type.DIFFERS_RETAIN_APPROVALS);
        assertThat(comparison.getReason()).isNull();
    }

    @Test
    public void when_doc_sum_tax_differs() throws Exception {

        // given

        summaryDocLine.setDocSumTax(BigDecimal.ONE);
        existingSummaryDocLine.setDocSumTax(BigDecimal.TEN);

        // when
        CodaDocHeadIncInvoiceIta.Comparison comparison = codaDocHeadIncInvoiceIta.compareWith(existing);

        // then
        assertThat(comparison.getType()).isEqualTo(CodaDocHeadIncInvoiceIta.Comparison.Type.DIFFERS_INVALIDATING_APPROVALS);
        assertThat(comparison.getReason()).isEqualTo("Line #1 (SUMMARY): Doc sum tax (VAT amount) has changed");
    }


    @Test
    public void when_doc_value_differs() throws Exception {

        // given
        summaryDocLine.setDocValue(BigDecimal.ONE);
        existingSummaryDocLine.setDocValue(BigDecimal.TEN);

        // when
        CodaDocHeadIncInvoiceIta.Comparison comparison = codaDocHeadIncInvoiceIta.compareWith(existing);

        // then
        assertThat(comparison.getType()).isEqualTo(CodaDocHeadIncInvoiceIta.Comparison.Type.DIFFERS_INVALIDATING_APPROVALS);
        assertThat(comparison.getReason()).isEqualTo("Line #1 (SUMMARY): Doc value (gross amount) has changed");
    }


    @Test
    public void when_media_code_differs() throws Exception {

        // given
        summaryDocLine.setMediaCode("X");
        summaryDocLine.setMediaCodeValidationStatus(ValidationStatus.VALID);
        existingSummaryDocLine.setMediaCode("Y");
        existingSummaryDocLine.setMediaCodeValidationStatus(ValidationStatus.VALID);

        // when
        CodaDocHeadIncInvoiceIta.Comparison comparison = codaDocHeadIncInvoiceIta.compareWith(existing);

        // then
        assertThat(comparison.getType()).isEqualTo(CodaDocHeadIncInvoiceIta.Comparison.Type.DIFFERS_INVALIDATING_APPROVALS);
        assertThat(comparison.getReason()).isEqualTo("Line #1 (SUMMARY): Media code (payment method) has changed");
    }


    @Test
    public void when_media_code_differs_not_checked() throws Exception {

        // given
        summaryDocLine.setMediaCode(null);
        summaryDocLine.setMediaCodeValidationStatus(ValidationStatus.NOT_CHECKED);
        existingSummaryDocLine.setMediaCode("Y");
        existingSummaryDocLine.setMediaCodeValidationStatus(ValidationStatus.VALID);

        // when
        CodaDocHeadIncInvoiceIta.Comparison comparison = codaDocHeadIncInvoiceIta.compareWith(existing);

        // then
        assertThat(comparison.getType()).isEqualTo(CodaDocHeadIncInvoiceIta.Comparison.Type.DIFFERS_RETAIN_APPROVALS);
        assertThat(comparison.getReason()).isNull();
    }


    @Test
    public void when_due_date_differs() throws Exception {

        // given
        summaryDocLine.setDueDate(LocalDate.now());
        existingSummaryDocLine.setDueDate(LocalDate.now().plusDays(-1));

        // when
        CodaDocHeadIncInvoiceIta.Comparison comparison = codaDocHeadIncInvoiceIta.compareWith(existing);

        // then
        assertThat(comparison.getType()).isEqualTo(CodaDocHeadIncInvoiceIta.Comparison.Type.DIFFERS_INVALIDATING_APPROVALS);
        assertThat(comparison.getReason()).isEqualTo("Line #1 (SUMMARY): Due date has changed");
    }


    @Test
    public void when_value_date_differs() throws Exception {

        // given
        summaryDocLine.setValueDate(LocalDate.now());
        existingSummaryDocLine.setValueDate(LocalDate.now().plusDays(-1));

        // when
        CodaDocHeadIncInvoiceIta.Comparison comparison = codaDocHeadIncInvoiceIta.compareWith(existing);

        // then
        assertThat(comparison.getType()).isEqualTo(CodaDocHeadIncInvoiceIta.Comparison.Type.DIFFERS_INVALIDATING_APPROVALS);
        assertThat(comparison.getReason()).isEqualTo("Line #1 (SUMMARY): Value date has changed");
    }


    @Test
    public void when_user_ref_1_differs() throws Exception {

        // given
        summaryDocLine.setUserRef1("10010010");
        existingSummaryDocLine.setUserRef1("20020020");

        // when
        CodaDocHeadIncInvoiceIta.Comparison comparison = codaDocHeadIncInvoiceIta.compareWith(existing);

        // then
        assertThat(comparison.getType()).isEqualTo(CodaDocHeadIncInvoiceIta.Comparison.Type.DIFFERS_INVALIDATING_APPROVALS);
        assertThat(comparison.getReason()).isEqualTo("Line #1 (SUMMARY): User Ref 1 (bar code) has changed");
    }


    @Test
    public void when_description_differs() throws Exception {

        // given
        summaryDocLine.setDescription("some description");
        existingSummaryDocLine.setDescription("some previous description");

        // when
        CodaDocHeadIncInvoiceIta.Comparison comparison = codaDocHeadIncInvoiceIta.compareWith(existing);

        // then
        assertThat(comparison.getType()).isEqualTo(CodaDocHeadIncInvoiceIta.Comparison.Type.DIFFERS_INVALIDATING_APPROVALS);
        assertThat(comparison.getReason()).isEqualTo("Line #1 (SUMMARY): Description has changed");
    }


    @Test
    public void when_some_other_difference() throws Exception {

        // given
        assertThat(codaDocHeadIncInvoiceIta.getCodaTimeStamp()).isNotEqualTo(existing.getCodaTimeStamp());

        // when
        CodaDocHeadIncInvoiceIta.Comparison comparison = codaDocHeadIncInvoiceIta.compareWith(existing);

        // then
        assertThat(comparison.getType()).isEqualTo(CodaDocHeadIncInvoiceIta.Comparison.Type.DIFFERS_RETAIN_APPROVALS);
    }

    @Test
    public void when_fewer_analysis_lines() throws Exception {

        // given
        existingAnalysisDocLine2 = addLine(existing, 3, LineType.ANALYSIS);

        // when
        CodaDocHeadIncInvoiceIta.Comparison comparison = codaDocHeadIncInvoiceIta.compareWith(existing);

        // then
        assertThat(comparison.getType()).isEqualTo(CodaDocHeadIncInvoiceIta.Comparison.Type.DIFFERS_RETAIN_APPROVALS);
        assertThat(comparison.getReason()).isNull();
    }

    @Test
    public void when_more_analysis_lines() throws Exception {
        // given
        analysisDocLine2 = addLine(codaDocHeadIncInvoiceIta, 3, LineType.ANALYSIS);

        // when
        CodaDocHeadIncInvoiceIta.Comparison comparison = codaDocHeadIncInvoiceIta.compareWith(existing);

        // then
        assertThat(comparison.getType()).isEqualTo(CodaDocHeadIncInvoiceIta.Comparison.Type.DIFFERS_RETAIN_APPROVALS);
        assertThat(comparison.getReason()).isNull();
    }

    @Test
    public void when_analysis_line_description_differs() throws Exception {
        // given
        analysisDocLine.setDescription("some description");
        existingAnalysisDocLine.setDescription("some previous description");

        // when
        CodaDocHeadIncInvoiceIta.Comparison comparison = codaDocHeadIncInvoiceIta.compareWith(existing);

        // then
        assertThat(comparison.getType()).isEqualTo(CodaDocHeadIncInvoiceIta.Comparison.Type.DIFFERS_RETAIN_APPROVALS);
        assertThat(comparison.getReason()).isNull();
    }

    @Test
    public void when_analysis_line_doc_value_differs() throws Exception {
        // given
        analysisDocLine.setDocValue(BigDecimal.ONE);
        existingAnalysisDocLine.setDocValue(BigDecimal.TEN);

        // when
        CodaDocHeadIncInvoiceIta.Comparison comparison = codaDocHeadIncInvoiceIta.compareWith(existing);

        // then
        assertThat(comparison.getType()).isEqualTo(CodaDocHeadIncInvoiceIta.Comparison.Type.DIFFERS_RETAIN_APPROVALS);
        assertThat(comparison.getReason()).isNull();
    }

    @Test
    public void when_analysis_line_doc_sum_tax_differs() throws Exception {
        // given
        analysisDocLine.setDocSumTax(BigDecimal.ONE);
        existingAnalysisDocLine.setDocSumTax(BigDecimal.TEN);

        // when
        CodaDocHeadIncInvoiceIta.Comparison comparison = codaDocHeadIncInvoiceIta.compareWith(existing);

        // then
        assertThat(comparison.getType()).isEqualTo(CodaDocHeadIncInvoiceIta.Comparison.Type.DIFFERS_RETAIN_APPROVALS);
        assertThat(comparison.getReason()).isNull();
    }

    @Test
    public void when_analysis_line_due_date_differs() throws Exception {
        // given
        analysisDocLine.setDueDate(LocalDate.now());
        existingAnalysisDocLine.setDueDate(LocalDate.now().plusDays(-1));

        // when
        CodaDocHeadIncInvoiceIta.Comparison comparison = codaDocHeadIncInvoiceIta.compareWith(existing);

        // then
        assertThat(comparison.getType()).isEqualTo(CodaDocHeadIncInvoiceIta.Comparison.Type.DIFFERS_RETAIN_APPROVALS);
        assertThat(comparison.getReason()).isNull();
    }

    @Test
    public void when_analysis_line_value_date_differs() throws Exception {
        // given
        analysisDocLine.setValueDate(LocalDate.now());
        existingAnalysisDocLine.setValueDate(LocalDate.now().plusDays(-1));

        // when
        CodaDocHeadIncInvoiceIta.Comparison comparison = codaDocHeadIncInvoiceIta.compareWith(existing);

        // then
        assertThat(comparison.getType()).isEqualTo(CodaDocHeadIncInvoiceIta.Comparison.Type.DIFFERS_RETAIN_APPROVALS);
        assertThat(comparison.getReason()).isNull();
    }


}