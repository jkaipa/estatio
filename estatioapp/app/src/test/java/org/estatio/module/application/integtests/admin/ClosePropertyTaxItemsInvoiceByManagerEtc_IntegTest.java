package org.estatio.module.application.integtests.admin;

import java.math.BigDecimal;
import java.util.Arrays;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.estatio.module.application.app.AdminDashboard;
import org.estatio.module.application.integtests.ApplicationModuleIntegTestAbstract;
import org.estatio.module.charge.fixtures.charges.enums.Charge_enum;
import org.estatio.module.invoice.dom.InvoiceItem;
import org.estatio.module.invoice.dom.InvoiceRunType;
import org.estatio.module.invoice.dom.PaymentMethod;
import org.estatio.module.lease.contributions.Lease_calculate;
import org.estatio.module.lease.dom.InvoicingFrequency;
import org.estatio.module.lease.dom.Lease;
import org.estatio.module.lease.dom.LeaseAgreementRoleTypeEnum;
import org.estatio.module.lease.dom.LeaseItem;
import org.estatio.module.lease.dom.LeaseItemType;
import org.estatio.module.lease.dom.LeaseTermForServiceCharge;
import org.estatio.module.lease.dom.invoicing.InvoiceForLease;
import org.estatio.module.lease.dom.invoicing.InvoiceForLeaseRepository;
import org.estatio.module.lease.fixtures.docfrag.enums.DocFragment_demo_enum;
import org.estatio.module.lease.fixtures.lease.enums.Lease_enum;

public class ClosePropertyTaxItemsInvoiceByManagerEtc_IntegTest extends ApplicationModuleIntegTestAbstract {

    @Before
    public void setup() {
        runFixtureScript(new FixtureScript() {
            @Override
            protected void execute(ExecutionContext executionContext) {
                executionContext.executeChildren(this,
                        DocFragment_demo_enum.InvoicePreliminaryLetterDescription_DemoGbr,
                        DocFragment_demo_enum.InvoiceDescription_DemoGbr,
                        DocFragment_demo_enum.InvoiceItemDescription_DemoGbr
                );
                executionContext.executeChild(this, Lease_enum.OxfTopModel001Gb);
            }
        });
    }

    @Test
    public void happyCase() throws Exception {

        // given
        final Lease topmodelLease = Lease_enum.OxfTopModel001Gb.findUsing(serviceRegistry);
        final LocalDate startOfTheYear = new LocalDate(2020, 1, 1);
        final BigDecimal budgetedValue = new BigDecimal("1234.56");

        final LeaseItem propTaxItemByManager = topmodelLease
                .newItem(LeaseItemType.PROPERTY_TAX, LeaseAgreementRoleTypeEnum.MANAGER,
                        Charge_enum.GbServiceCharge.findUsing(serviceRegistry), InvoicingFrequency.YEARLY_IN_ARREARS,
                        PaymentMethod.DIRECT_DEBIT, new LocalDate(2018, 7, 15));
        final LeaseTermForServiceCharge term = (LeaseTermForServiceCharge) propTaxItemByManager.newTerm(propTaxItemByManager.getStartDate(), null);
        term.setBudgetedValue(budgetedValue);
        Assertions.assertThat(topmodelLease.findItemsOfType(LeaseItemType.PROPERTY_TAX)).hasSize(1);

        // when
        AdminDashboard adminDashboard = new AdminDashboard();
        serviceRegistry.injectServicesInto(adminDashboard);
        adminDashboard.closePropertyTaxItemsInvoicedByManagerAndOpenItemsInvoicedByLandlord(AdminDashboard.Params.DEMO_OXF);

        // then
        Assertions.assertThat(propTaxItemByManager.getEndDate()).isEqualTo(startOfTheYear.minusDays(1));
        Assertions.assertThat(topmodelLease.findItemsOfType(LeaseItemType.PROPERTY_TAX)).hasSize(2);
        final LeaseItem newItem = topmodelLease.findItemsOfType(LeaseItemType.PROPERTY_TAX).stream()
                .filter(li -> li.getInvoicedBy() == LeaseAgreementRoleTypeEnum.LANDLORD).findFirst().orElse(null);
        Assertions.assertThat(newItem.getStartDate()).isEqualTo(startOfTheYear);
        Assertions.assertThat(newItem.getCharge().getReference()).isEqualTo(AdminDashboard.Params.DEMO_OXF.getChargeReference());
        Assertions.assertThat(newItem.getInvoicingFrequency()).isEqualTo(AdminDashboard.Params.DEMO_OXF.getInvoicingFrequency());
        Assertions.assertThat(newItem.getInvoicingFrequency()).isEqualTo(InvoicingFrequency.YEARLY_IN_ADVANCE_PLUS9M);
        Assertions.assertThat(newItem.valueForDate(startOfTheYear)).isEqualTo(propTaxItemByManager.valueForDate(startOfTheYear.minusDays(1)));
        Assertions.assertThat(newItem.valueForDate(startOfTheYear)).isEqualTo(budgetedValue);

        // and when
        final LocalDate invoiceDueDate = new LocalDate(2020, 10, 1);
        mixin(Lease_calculate.class, topmodelLease).exec(InvoiceRunType.NORMAL_RUN, Arrays.asList(LeaseItemType.PROPERTY_TAX),
                invoiceDueDate, invoiceDueDate, new LocalDate(2020,10,2));
        final InvoiceForLease invoiceForLease = invoiceForLeaseRepository.findByLease(topmodelLease).stream()
                .filter(i -> i.getDueDate() == invoiceDueDate).findFirst().orElse(null);
        Assertions.assertThat(invoiceForLease.getItems()).hasSize(1);
        final InvoiceItem first = invoiceForLease.getItems().first();
        Assertions.assertThat(first.getNetAmount()).isEqualTo(budgetedValue);
        Assertions.assertThat(first.getEffectiveInterval().toString()).isEqualTo("2020-10-01/2021-10-01");

    }

    @Inject InvoiceForLeaseRepository invoiceForLeaseRepository;


}
