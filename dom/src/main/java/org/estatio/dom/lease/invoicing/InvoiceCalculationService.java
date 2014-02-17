/*
 *
 *  Copyright 2012-2014 Eurocommercial Properties NV
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.estatio.dom.lease.invoicing;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.enterprise.context.RequestScoped;

import com.google.common.collect.Lists;

import org.apache.commons.lang3.ObjectUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.Programmatic;

import org.estatio.dom.EstatioInteractionCache;
import org.estatio.dom.asset.Properties;
import org.estatio.dom.charge.Charge;
import org.estatio.dom.invoice.InvoicingInterval;
import org.estatio.dom.lease.InvoicingFrequency;
import org.estatio.dom.lease.Lease;
import org.estatio.dom.lease.LeaseItem;
import org.estatio.dom.lease.LeaseItemStatus;
import org.estatio.dom.lease.LeaseStatus;
import org.estatio.dom.lease.LeaseTerm;
import org.estatio.dom.lease.LeaseTermValueType;
import org.estatio.dom.lease.Leases;
import org.estatio.dom.lease.Leases.InvoiceRunType;
import org.estatio.dom.valuetypes.AbstractInterval.IntervalEnding;
import org.estatio.dom.valuetypes.LocalDateInterval;
import org.estatio.services.settings.EstatioSettingsService;

@RequestScoped
@Hidden
public class InvoiceCalculationService {

    /**
     * class to store the result a calculation
     * 
     */
    public static class CalculationResult {
        private static final BigDecimal ZERO = new BigDecimal("0.00");
        private BigDecimal value;
        private BigDecimal valueOnDueDate;
        private BigDecimal mockValue;;

        private InvoicingInterval invoicingInterval;
        private LocalDateInterval effectiveInterval;

        public CalculationResult() {
            this(null);
        }

        public CalculationResult(final InvoicingInterval interval) {
            this(interval, interval.asLocalDateInterval(), ZERO, ZERO, ZERO);
        }

        public CalculationResult(
                final InvoicingInterval interval,
                final LocalDateInterval effectiveInterval,
                final BigDecimal value,
                final BigDecimal valueOnDueDate,
                final BigDecimal mockValue) {
            this.invoicingInterval = interval;
            this.effectiveInterval = effectiveInterval;
            this.value = value;
            this.valueOnDueDate = valueOnDueDate;
            this.mockValue = mockValue;
        }

        public BigDecimal value() {
            return value;
        }

        public BigDecimal valueOnDueDate() {
            return valueOnDueDate;
        }

        public BigDecimal mockValue() {
            return mockValue;
        }

        public InvoicingInterval invoicingInterval() {
            return invoicingInterval;
        }

        public LocalDateInterval effectiveInterval() {
            return effectiveInterval;
        }

        @Override
        public String toString() {
            return invoicingInterval.toString().concat(" : ").concat(value.toString());
        }
    }

    /**
     * Utility class for collection of calculation results
     */
    public static class CalculationResultsUtil {
        public static BigDecimal sum(final List<CalculationResult> list) {
            BigDecimal sum = BigDecimal.ZERO;
            if (list == null) {
                return sum;
            }
            for (CalculationResult result : list) {
                sum = sum.add(result.value());
            }
            return sum;
        }
    }

    private LocalDate systemEpochDate() {
        return estatioSettingsService.fetchEpochDate();
    }

    private String interactionId;

    private void startInteraction(final String parameters) {
        if (interactionId == null) {
            interactionId = LocalDateTime.now().toString(); //.concat(" - ").concat(parameters);
            EstatioInteractionCache.startInteraction();
        }
    }

    private void endInteraction() {
        EstatioInteractionCache.endInteraction(interactionId != null);
        interactionId = null;
    }

    @Programmatic
    public void calculateAndInvoice(InvoiceCalculationParameters parameters) {
        try {
            startInteraction(parameters.toString());
            for (Lease lease : parameters.leases() == null ? leases.findLeasesByProperty(parameters.property()) : parameters.leases()) {
                lease.verifyUntil(parameters.dueDateRange().endDateExcluding());
                if (lease.getStatus() != LeaseStatus.SUSPENDED) {
                    SortedSet<LeaseItem> leaseItems =
                            parameters.leaseItem() == null ?
                                    lease.getItems() :
                                    new TreeSet<LeaseItem>(Arrays.asList(parameters.leaseItem()));
                    for (LeaseItem leaseItem : leaseItems) {
                        if (!leaseItem.getStatus().equals(LeaseItemStatus.SUSPENDED)) {
                            if (parameters.leaseItemTypes() == null || parameters.leaseItemTypes().contains(leaseItem.getType())) {
                                SortedSet<LeaseTerm> leaseTerms =
                                        parameters.leaseTerm() == null ?
                                                leaseItem.getTerms() :
                                                new TreeSet<LeaseTerm>(Arrays.asList(parameters.leaseTerm()));
                                for (LeaseTerm leaseTerm : leaseTerms) {
                                    calculateAndInvoice(
                                            leaseTerm,
                                            parameters.dueDateRange().startDate(),
                                            parameters.dueDateRange().endDateExcluding(),
                                            parameters.invoiceDueDate(),
                                            leaseItem.getInvoicingFrequency(),
                                            parameters.invoiceRunType());
                                }
                            }
                        }
                    }
                }
            }
        } finally {
            endInteraction();
        }
    }

    /**
     * Calculates term and creates invoice
     * 
     * @param leaseTerm
     * @param startDueDate
     * @param nextDueDate
     * @param invoiceDueDate
     * @param invoicingFrequency
     * @param runType
     */
    @Programmatic
    public void calculateAndInvoice(
            final LeaseTerm leaseTerm,
            final LocalDate startDueDate,
            final LocalDate nextDueDate,
            final LocalDate invoiceDueDate,
            final InvoicingFrequency invoicingFrequency,
            final InvoiceRunType runType) {
        final List<CalculationResult> results;
        LocalDate termStartDate = leaseTerm.getStartDate();
        LocalDate start = startDueDate;
        // Use the start date of the term when start due date is before or
        // retro run
        if (runType.equals(InvoiceRunType.RETRO_RUN)) {
            start = termStartDate;
        }
        results = calculateDueDateRange(leaseTerm, start, nextDueDate, invoicingFrequency);
        createInvoiceItems(leaseTerm, invoiceDueDate, results, invoicingFrequency);
    }

    /**
     * Calculates a term with a given invoicing frequency
     */
    @Programmatic
    public List<CalculationResult> calculateDueDateRange(
            final LeaseTerm leaseTerm,
            final LocalDate startDueDate,
            final LocalDate nextDueDate,
            final InvoicingFrequency invoicingFrequency) {
        final List<CalculationResult> results = Lists.newArrayList();
        final LocalDateInterval termInterval = leaseTerm.getEffectiveInterval();
        final List<InvoicingInterval> intervals = invoicingFrequency.intervalsInDueDateRange(
                new LocalDateInterval(startDueDate, nextDueDate, IntervalEnding.EXCLUDING_END_DATE), termInterval);

        for (final InvoicingInterval invoicingInterval : intervals) {
            final LocalDateInterval effectiveInterval = invoicingInterval.asLocalDateInterval().overlap(termInterval);
            if (effectiveInterval == null) {
                results.add(new CalculationResult(invoicingInterval));
            } else {
                final BigDecimal overlapDays = new BigDecimal(effectiveInterval.days());
                final BigDecimal frequencyDays = new BigDecimal(invoicingInterval.days());
                final BigDecimal rangeFactor =
                        leaseTerm.valueType().equals(LeaseTermValueType.FIXED) ?
                                BigDecimal.ONE :
                                overlapDays.divide(frequencyDays, MathContext.DECIMAL64);
                final BigDecimal annualFactor = invoicingFrequency.annualMultiplier();
                final LocalDate epochDate = ObjectUtils.firstNonNull(leaseTerm.getLeaseItem().getEpochDate(), systemEpochDate());
                BigDecimal mockValue = BigDecimal.ZERO;
                if (epochDate != null && invoicingInterval.dueDate().isBefore(epochDate)) {
                    mockValue = leaseTerm.valueForDate(epochDate);
                }
                results.add(new CalculationResult(
                        invoicingInterval,
                        effectiveInterval,
                        calculateValue(rangeFactor, annualFactor, leaseTerm.valueForDate(nextDueDate.minusDays(1))),
                        calculateValue(rangeFactor, annualFactor, leaseTerm.valueForDate(invoicingInterval.dueDate())),
                        calculateValue(rangeFactor, annualFactor, mockValue)));
            }
        }
        return results;
    }

    /**
     * Multiplies a value with the range and annual factors
     * 
     * @param rangeFactor
     * @param annualFactor
     * @param value
     * @return
     */
    private BigDecimal calculateValue(
            final BigDecimal rangeFactor,
            final BigDecimal annualFactor,
            final BigDecimal value) {
        if (value != null && annualFactor != null && rangeFactor != null) {
            return value.multiply(annualFactor)
                    .multiply(rangeFactor)
                    .setScale(2, RoundingMode.HALF_UP);
        }
        return new BigDecimal("0.00");
    }

    /**
     * Calculates an invoice item with the difference between the already
     * invoiced and calculated value.
     * 
     * @param leaseTerm
     * @param dueDate
     * @param calculationResult
     * @param invoicingFrequency
     */
    void createInvoiceItems(
            final LeaseTerm leaseTerm,
            final LocalDate dueDate,
            final List<CalculationResult> results,
            final InvoicingFrequency invoicingFrequency) {

        for (CalculationResult result : results) {
            BigDecimal invoicedValue = invoiceItemsForLease.invoicedValue(leaseTerm, result.invoicingInterval().asLocalDateInterval());
            BigDecimal newValue = result.value().subtract(invoicedValue).subtract(result.mockValue());
            if (newValue.compareTo(BigDecimal.ZERO) != 0) {
                boolean adjustment = invoicedValue.add(result.mockValue()).compareTo(BigDecimal.ZERO) != 0;
                InvoiceItemForLease invoiceItem =
                        invoiceItemsForLease.createUnapprovedInvoiceItem(
                                leaseTerm,
                                result.invoicingInterval().asLocalDateInterval(),
                                dueDate,
                                interactionId);
                invoiceItem.setNetAmount(newValue);
                invoiceItem.setQuantity(BigDecimal.ONE);
                LeaseItem leaseItem = leaseTerm.getLeaseItem();
                Charge charge = leaseItem.getCharge();
                invoiceItem.setCharge(charge);
                invoiceItem.setDescription(charge.getDescription());
                invoiceItem.setDueDate(dueDate);
                invoiceItem.setStartDate(result.invoicingInterval().startDate());
                invoiceItem.setEndDate(result.invoicingInterval().endDate());

                LocalDateInterval intervalToUse =
                        adjustment
                                ? result.invoicingInterval().asLocalDateInterval()
                                : result.effectiveInterval();
                invoiceItem.setEffectiveStartDate(intervalToUse.startDate());
                invoiceItem.setEffectiveEndDate(intervalToUse.endDate());

                invoiceItem.setTax(charge.getTax());
                invoiceItem.verify();
                invoiceItem.setAdjustment(adjustment);
            }
        }
    }

    // //////////////////////////////////////

    private EstatioSettingsService estatioSettingsService;

    public void injectEstatioSettings(final EstatioSettingsService estatioSettings) {
        this.estatioSettingsService = estatioSettings;
    }

    private InvoiceItemsForLease invoiceItemsForLease;

    public void injectInvoiceItemsForLease(final InvoiceItemsForLease invoiceItemsForLease) {
        this.invoiceItemsForLease = invoiceItemsForLease;
    }

    private Properties properties;

    public final void injectProperties(final Properties properties) {
        this.properties = properties;
    }

    private Leases leases;

    public final void injectLeases(final Leases leases) {
        this.leases = leases;
    }

}
