package org.estatio.dom.lease;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.estatio.dom.agreement.AgreementRoleType;
import org.estatio.dom.agreement.AgreementRoleTypes;
import org.estatio.dom.agreement.AgreementType;
import org.estatio.dom.agreement.AgreementTypes;
import org.estatio.dom.invoice.InvoiceItem;
import org.estatio.dom.invoice.Invoices;
import org.estatio.dom.party.Party;
import org.estatio.dom.utils.DateTimeUtils;
import org.joda.time.LocalDate;
import org.joda.time.Period;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Prototype;
import org.apache.isis.applib.filter.Filter;

@Named("Leases")
public class Leases extends AbstractFactoryAndRepository {

    // {{ Id, iconName
    @Override
    public String getId() {
        return "leases";
    }

    public String iconName() {
        return "Lease";
    }

    // }}

    @ActionSemantics(Of.NON_IDEMPOTENT)
    @MemberOrder(sequence = "1")
    public Lease newLease(final @Named("Reference") String reference, final @Named("Name") String name, final @Named("Start Date") LocalDate startDate, final @Optional @Named("Duration") @DescribedAs("Duration in a text format. Example 6y5m2d") String duration,
            final @Optional @Named("End Date") @DescribedAs("Can be omitted when duration is filled in") LocalDate endDate, final @Optional @Named("Landlord") Party landlord, final @Optional @Named("Tentant") Party tenant) {
        LocalDate calculatedEndDate = endDate;
        if (duration != null) {
            Period p = DateTimeUtils.stringToPeriod(duration);
            if (p != null) {
                calculatedEndDate = startDate.plus(p).minusDays(1);
            }
        }
        Lease lease = newTransientInstance(Lease.class);
        lease.setAgreementType(agreementTypes.find(LeaseConstants.AT_LEASE));
        lease.setReference(reference);
        lease.setName(name);
        lease.setStartDate(startDate);
        lease.setEndDate(calculatedEndDate);
        persistIfNotAlready(lease);
        final AgreementRoleType artTenant = agreementRoleTypes.find(LeaseConstants.ART_TENANT);
        lease.addRole(tenant, artTenant, null, null);
        final AgreementRoleType artLandlord = agreementRoleTypes.find(LeaseConstants.ART_LANDLORD);
        lease.addRole(landlord, artLandlord, null, null);
        return lease;
    }
    
    @ActionSemantics(Of.SAFE)
    @MemberOrder(sequence = "2")
    public List<Lease> findLeasesByReference(final @Named("Reference") String reference) {
        throw new NotImplementedException();
    }

    @Hidden
    @ActionSemantics(Of.SAFE)
    @MemberOrder(sequence = "2")
    public Lease findByReference(final @Named("Reference") String reference) {
        return firstMatch(Lease.class, new Filter<Lease>() {
            @Override
            public boolean accept(final Lease lease) {
                return reference.equals(lease.getReference());
            }
        });
    }

    /**
     * Returns the {@link InvoiceItem}s that are newly
     * {@link Lease#calculate(LocalDate, LocalDate) calculate}d for all of the
     * {@link Lease}s matched by the provided <tt>leaseReference</tt> and the
     * other parameters.
     */
    public List<InvoiceItem> calculate(final @Named("Lease reference") String leaseReference, final @Named("Period Start Date") LocalDate startDate, final @Named("Due date") LocalDate dueDate) {
        List<Lease> leases = findLeasesByReference(leaseReference);
        for (Lease lease : leases) {
            lease.calculate(startDate, dueDate);
        }
        // As a convenience, we now go find them and display them.
        // We've done it this way so that the user can always just go to the
        // menu and make this query.
        return invoicesService.findItems(leaseReference, startDate, dueDate);
    }

    @Prototype
    @ActionSemantics(Of.SAFE)
    public List<Lease> allLeases() {
        return allInstances(Lease.class);
    }

    // {{ injected: Invoices
    private Invoices invoicesService;
    public void injectInvoicesService(final Invoices invoices) {
        this.invoicesService = invoices;
    }
    // }}

    // {{ injected: AgreementTypes
    private AgreementTypes agreementTypes;
    public void injectAgreementTypes(final AgreementTypes agreementTypes) {
        this.agreementTypes = agreementTypes;
    }
    // }}

    // {{ injected: AgreementRoleTypes
    private AgreementRoleTypes agreementRoleTypes;
    public void setAgreementRoleTypes(final AgreementRoleTypes agreementRoleTypes) {
        this.agreementRoleTypes = agreementRoleTypes;
    }
    // }}


}
