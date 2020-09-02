package org.estatio.module.coda.dom.doc;

import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.queryresultscache.QueryResultsCache;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;

import org.estatio.module.capex.dom.invoice.IncomingInvoice;
import org.estatio.module.capex.dom.invoice.approval.IncomingInvoiceApprovalState;
import org.estatio.module.coda.contributions.IncomingInvoice_codaDocHead;
import org.estatio.module.invoice.dom.PaymentMethod;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = CodaDocHeadIncInvoiceIta.class,
        objectType = "coda.CodaDocHeadRepository"
)
public class CodaDocHeadRepository {

    static final String STAT_PAY_PAID = "paid";
    static final String STAT_PAY_AVAILABLE = "available";

    @Programmatic
    public java.util.List<CodaDocHeadIncInvoiceIta> listAll() {
        return repositoryService.allInstances(CodaDocHeadIncInvoiceIta.class);
    }

    @Programmatic
    public CodaDocHeadIncInvoiceIta findByCmpCodeAndDocCodeAndDocNum(
            final String cmpCode,
            final String docCode,
            final String docNum
    ) {
        return repositoryService.uniqueMatch(
                new org.apache.isis.applib.query.QueryDefault<>(
                        CodaDocHeadIncInvoiceIta.class,
                        "findByCmpCodeAndDocCodeAndDocNum",
                        "cmpCode", cmpCode,
                        "docCode", docCode,
                        "docNum", docNum));
    }

    @Programmatic
    public CodaDocHeadIncInvoiceIta findByCandidate(
            final CodaDocHeadIncInvoiceIta codaDocHeadIncInvoiceIta
    ) {
        final String cmpCode = codaDocHeadIncInvoiceIta.getCmpCode();
        final String docCode = codaDocHeadIncInvoiceIta.getDocCode();
        final String docNum = codaDocHeadIncInvoiceIta.getDocNum();
        return findByCmpCodeAndDocCodeAndDocNum(cmpCode, docCode, docNum);
    }

    @Programmatic
    public CodaDocHeadIncInvoiceIta persistAsReplacementIfRequired(final CodaDocHeadIncInvoiceIta codaDocHeadIncInvoiceIta) {
        // sanity check
        if(repositoryService.isPersistent(codaDocHeadIncInvoiceIta)) {
            throw new IllegalStateException(
                    String.format("CodaDocHead '%s' is already persistent", titleService.titleOf(
                            codaDocHeadIncInvoiceIta)));
        }

        final CodaDocHeadIncInvoiceIta existingCodaDocHeadIncInvoiceIta = findByCandidate(codaDocHeadIncInvoiceIta);

        deriveStatPayPaidDateIfRequired(codaDocHeadIncInvoiceIta, existingCodaDocHeadIncInvoiceIta);

        if (existingCodaDocHeadIncInvoiceIta != null) {
            delete(existingCodaDocHeadIncInvoiceIta);
        }
        return repositoryService.persistAndFlush(codaDocHeadIncInvoiceIta);
    }

    void deriveStatPayPaidDateIfRequired(final CodaDocHeadIncInvoiceIta codaDocHeadIncInvoiceIta, final CodaDocHeadIncInvoiceIta existingCodaDocHeadIncInvoiceIta) {
        if (!STAT_PAY_PAID.equals(codaDocHeadIncInvoiceIta.getStatPay())) {
            return;
        }
        if (isPaid(existingCodaDocHeadIncInvoiceIta)) {
            codaDocHeadIncInvoiceIta.setStatPayPaidDate(existingCodaDocHeadIncInvoiceIta.getStatPayPaidDate());
            return;
        }
        // else
        codaDocHeadIncInvoiceIta.setStatPayPaidDate(clockService.now());
    }

    private static boolean isPaid(final CodaDocHeadIncInvoiceIta existingCodaDocHeadIncInvoiceIta) {
        if (existingCodaDocHeadIncInvoiceIta == null) {
            return false;
        }
        if (!STAT_PAY_PAID.equals(existingCodaDocHeadIncInvoiceIta.getStatPay())) {
            return false;
        }
        return existingCodaDocHeadIncInvoiceIta.getStatPayPaidDate() != null;
    }


    @Programmatic
    public List<CodaDocHeadIncInvoiceIta> findUnpaidAndInvalid() {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        CodaDocHeadIncInvoiceIta.class,
                        "findByHandlingAndStatPayNotEqualToAndNotValid",
                        "statPay", STAT_PAY_PAID,
                        "handling", Handling.INCLUDED
                ));
    }

    @Programmatic
    public List<CodaDocHeadIncInvoiceIta> findAvailable() {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        CodaDocHeadIncInvoiceIta.class,
                        "findByStatPay",
                        "statPay", STAT_PAY_AVAILABLE
                ));
    }

    @Programmatic
    public List<CodaDocHeadIncInvoiceIta> findByCmpCodeAndIncomingInvoiceApprovalStateIsNotFinal(final String cmpCode) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        CodaDocHeadIncInvoiceIta.class,
                        "findByCmpCodeAndIncomingInvoiceApprovalStateIsNotFinal",
                        "cmpCode", cmpCode
                ));
    }

    @Programmatic
    public List<CodaDocHeadIncInvoiceIta> findByCodaPeriodQuarterAndHandlingAndValidity(
            final String codaPeriodQuarter,
            final Handling handling,
            final Validity validity) {
        switch (validity) {
        case VALID:
            return repositoryService.allMatches(
                    new org.apache.isis.applib.query.QueryDefault<>(
                            CodaDocHeadIncInvoiceIta.class,
                            "findByCodaPeriodQuarterAndHandlingAndValid",
                            "codaPeriodQuarter", codaPeriodQuarter,
                            "handling", handling));
        case NOT_VALID:
            return repositoryService.allMatches(
                    new org.apache.isis.applib.query.QueryDefault<>(
                            CodaDocHeadIncInvoiceIta.class,
                            "findByCodaPeriodQuarterAndHandlingAndNotValid",
                            "codaPeriodQuarter", codaPeriodQuarter,
                            "handling", handling));
        case BOTH:
        default:
            return repositoryService.allMatches(
                    new org.apache.isis.applib.query.QueryDefault<>(
                            CodaDocHeadIncInvoiceIta.class,
                            "findByCodaPeriodQuarterAndHandling",
                            "codaPeriodQuarter", codaPeriodQuarter,
                            "handling", handling));
        }
    }

    @Programmatic
    public CodaDocHeadIncInvoiceIta findByIncomingInvoice(final IncomingInvoice incomingInvoice) {
        return repositoryService.uniqueMatch(
                new QueryDefault<>(
                        CodaDocHeadIncInvoiceIta.class,
                        "findByIncomingInvoice",
                        "incomingInvoice", incomingInvoice
                )
        );
    }

    public List<CodaDocHeadIncInvoiceIta> findByIncomingInvoiceAtPathPrefixAndApprovalState(
            final String atPathPrefix, final IncomingInvoiceApprovalState approvalState) {

        final List<CodaDocHeadIncInvoiceIta> codaDocHeadIncInvoiceItas = repositoryService.allMatches(
                new QueryDefault<>(
                        CodaDocHeadIncInvoiceIta.class,
                        "findByIncomingInvoiceAtPathPrefixAndApprovalState",
                        "atPathPrefix", atPathPrefix,
                        "approvalState", approvalState));

        prepopulateQueryResultsCacheForMixin(codaDocHeadIncInvoiceItas);

        return codaDocHeadIncInvoiceItas;
    }

    public List<CodaDocHeadIncInvoiceIta> findByIncomingInvoiceAtPathPrefixAndApprovalStateAndPaymentMethod(
            final String atPathPrefix,
            final IncomingInvoiceApprovalState approvalState,
            final PaymentMethod paymentMethod) {

        final List<CodaDocHeadIncInvoiceIta> codaDocHeadIncInvoiceItas = repositoryService.allMatches(
                new QueryDefault<>(
                        CodaDocHeadIncInvoiceIta.class,
                        "findByIncomingInvoiceAtPathPrefixAndApprovalStateAndPaymentMethod",
                        "atPathPrefix", atPathPrefix,
                        "approvalState", approvalState,
                        "paymentMethod", paymentMethod));

        prepopulateQueryResultsCacheForMixin(codaDocHeadIncInvoiceItas);

        return codaDocHeadIncInvoiceItas;
    }

    /**
     * populate the cache, so that IncomingInvoice_codaDocHead
     * (which will be called soon) doesn't need to run a query.
     *
     * @param codaDocHeadIncInvoiceItas
     */
    private void prepopulateQueryResultsCacheForMixin(final List<CodaDocHeadIncInvoiceIta> codaDocHeadIncInvoiceItas) {
        for (final CodaDocHeadIncInvoiceIta codaDocHeadIncInvoiceIta : codaDocHeadIncInvoiceItas) {
            queryResultsCache.put(keyFor(codaDocHeadIncInvoiceIta.getIncomingInvoice()), codaDocHeadIncInvoiceIta);
        }
    }

    private QueryResultsCache.Key keyFor(final IncomingInvoice incomingInvoice) {
        return new QueryResultsCache.Key(IncomingInvoice_codaDocHead.class, "prop", incomingInvoice);
    }


    @Inject
    QueryResultsCache queryResultsCache;

    @Programmatic
    public boolean deleteIfNoInvoiceAttached(final CodaDocHeadIncInvoiceIta codaDocHeadIncInvoiceIta) {
        // sanity check, is already validated at the REST endpoint
        if (codaDocHeadIncInvoiceIta.getIncomingInvoice() == null) {
            delete(codaDocHeadIncInvoiceIta);
            return true;
        }

        return false;
    }

    private void delete(final CodaDocHeadIncInvoiceIta codaDocHeadIncInvoiceIta) {
        for (final CodaDocLine line : Lists.newArrayList(codaDocHeadIncInvoiceIta.getLines())) {
            repositoryService.removeAndFlush(line);
        }
        repositoryService.removeAndFlush(codaDocHeadIncInvoiceIta);
    }


    @javax.inject.Inject
    RepositoryService repositoryService;

    @javax.inject.Inject
    TitleService titleService;

    @Inject
    ClockService clockService;

}
