package org.estatio.module.coda.dom.doc;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.estatio.module.coda.dom.doc.CodaDocHeadRepository.STAT_PAY_PAID;

public class CodaDocHeadIncInvoiceItaRepository_deriveStatPayPaidDateIfRequired_Test {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(JUnitRuleMockery2.Mode.INTERFACES_AND_CLASSES);

    @Mock
    ClockService mockClockService;
    @Mock
    CodaDocHeadIncInvoiceIta mockPrevious;

    CodaDocHeadIncInvoiceIta codaDocHeadIncInvoiceIta;

    CodaDocHeadRepository codaDocHeadRepository;

    @Before
    public void setUp() throws Exception {
        codaDocHeadIncInvoiceIta = new CodaDocHeadIncInvoiceIta();

        codaDocHeadRepository = new CodaDocHeadRepository();
        codaDocHeadRepository.clockService = mockClockService;
    }

    @Test
    public void when_not_paid() throws Exception {
        // given
        codaDocHeadIncInvoiceIta.setStatPay("");

        // expect
        context.checking(new Expectations() {{
            never(mockClockService);
            never(mockPrevious);
        }});

        // when
        codaDocHeadRepository.deriveStatPayPaidDateIfRequired(codaDocHeadIncInvoiceIta, mockPrevious);

        // then
        assertThat(codaDocHeadIncInvoiceIta.getStatPayPaidDate()).isNull();
    }

    @Test
    public void when_paid_no_previous() throws Exception {
        // given
        codaDocHeadIncInvoiceIta.setStatPay(STAT_PAY_PAID);

        // expect
        final LocalDate clockTime = LocalDate.now();
        context.checking(new Expectations() {{
            oneOf(mockClockService).now();
            will(returnValue(clockTime));
        }});

        // when
        codaDocHeadRepository.deriveStatPayPaidDateIfRequired(codaDocHeadIncInvoiceIta, null);

        // then
        assertThat(codaDocHeadIncInvoiceIta.getStatPayPaidDate()).isEqualTo(clockTime);
    }

    @Test
    public void when_paid_but_previous_not_paid() throws Exception {
        // given
        codaDocHeadIncInvoiceIta.setStatPay(STAT_PAY_PAID);

        // expect
        final LocalDate clockTime = LocalDate.now();
        context.checking(new Expectations() {{
            allowing(mockPrevious).getStatPay();
            will(returnValue("not_paid"));

            never(mockPrevious).getStatPayPaidDate();

            oneOf(mockClockService).now();
            will(returnValue(clockTime));
        }});

        // when
        codaDocHeadRepository.deriveStatPayPaidDateIfRequired(codaDocHeadIncInvoiceIta, mockPrevious);

        // then
        assertThat(codaDocHeadIncInvoiceIta.getStatPayPaidDate()).isEqualTo(clockTime);
    }

    @Test
    public void when_paid_but_previous_was_paid() throws Exception {
        // given
        codaDocHeadIncInvoiceIta.setStatPay(STAT_PAY_PAID);

        // expect
        final LocalDate previousTime = LocalDate.now();
        context.checking(new Expectations() {{
            allowing(mockPrevious).getStatPay();
            will(returnValue(STAT_PAY_PAID));

            allowing(mockPrevious).getStatPayPaidDate();
            will(returnValue(previousTime));

            never(mockClockService);
        }});

        // when
        codaDocHeadRepository.deriveStatPayPaidDateIfRequired(codaDocHeadIncInvoiceIta, mockPrevious);

        // then
        assertThat(codaDocHeadIncInvoiceIta.getStatPayPaidDate()).isEqualTo(previousTime);
    }
}