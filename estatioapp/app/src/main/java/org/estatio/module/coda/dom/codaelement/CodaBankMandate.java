package org.estatio.module.coda.dom.codaelement;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.Unique;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;

import com.google.common.collect.ComparisonChain;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.Where;

import lombok.Getter;
import lombok.Setter;

@PersistenceCapable(
        identityType = IdentityType.DATASTORE,
        schema = "dbo",
        table = "CodaBankMandate"
)
@DatastoreIdentity(
        strategy = IdGeneratorStrategy.IDENTITY,
        column = "id")
@Version(
        strategy = VersionStrategy.VERSION_NUMBER,
        column = "version")
@Queries({
        @Query(
                name = "findByElementBank", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.estatio.module.coda.dom.codaelement.CodaBankMandate "
                        + "WHERE elementBank == :elementBank "),
        @Query(
                name = "findUnique", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.estatio.module.coda.dom.codaelement.CodaBankMandate "
                        + "WHERE elementBank == :elementBank "
                        + " && mandateReference == :mandateReference "),
})
@Unique(name = "CodaBankMandate_elementBank_mandateReference_UNQ", members = { "elementBank", "mandateReference"})
@DomainObject(
        objectType = "codaelement.CodaBankMandate",
        editing = Editing.DISABLED
)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
)
public class CodaBankMandate implements Comparable<CodaBankMandate> {

    public CodaBankMandate() {}

    @Column(allowsNull = "false", name = "elementId")
    @Property(hidden = Where.PARENTED_TABLES)
    @Getter @Setter
    private CodaElementBank elementBank;

    @Getter @Setter
    @Column(allowsNull = "false", length = 35)
    @Property()
    private String mandateReference;

    @Getter @Setter
    @Column(allowsNull = "false", length = 20)
    @Property()
    private TransactionType transactionType;

    @Getter @Setter
    @Column(allowsNull = "false", length = 20)
    @Property()
    private Scheme b2BIdentificationScheme;

    @Getter @Setter
    @Column(allowsNull = "false", length = 15)
    @Property()
    private Status status;

    @Getter @Setter
    @Column(allowsNull = "false", length = 20)
    @Property()
    private SepaType sepaType;

    @Getter @Setter
    @Column(allowsNull = "false", length = 12)
    @Property()
    private String reasonAmendment;

    @Getter @Setter
    @Column(allowsNull = "false")
    @Property()
    private LocalDate dateOfSigning;

    //region > compareTo, toString
    @Override
    public int compareTo(final CodaBankMandate other) {
        return ComparisonChain.start()
                .compare(getElementBank(), other.getElementBank())
                .compare(getMandateReference(), other.getMandateReference())
                .result();
    }

    @Override
    public String toString() {
        return "CodaElementBank{" +
                "bank=" + getElementBank() +
                ", ref='" + getMandateReference() + '\'' +
                '}';
    }

    //endregion

    public enum TransactionType {
        // <xsd:simpleType name="typeCtSEPATransactionType">
        //    <xsd:annotation>
        //      <xsd:documentation>The allowed values for the SEPA
        //                transaction type.</xsd:documentation>
        //    </xsd:annotation>
        //    <xsd:restriction base="com:typeBaseEnum">
        //      <xsd:enumeration value="none">
        //        <xsd:annotation>
        //          <xsd:documentation>none</xsd:documentation>
        //        </xsd:annotation>
        //      </xsd:enumeration>
        //      <xsd:enumeration value="sepa_tran_rcur">
        //        <xsd:annotation>
        //          <xsd:documentation>sepa_tran_rcur</xsd:documentation>
        //        </xsd:annotation>
        //      </xsd:enumeration>
        //      <xsd:enumeration value="sepa_tran_ooff">
        //        <xsd:annotation>
        //          <xsd:documentation>sepa_tran_ooff</xsd:documentation>
        //        </xsd:annotation>
        //      </xsd:enumeration>
        //      <xsd:enumeration value="sepa_tran_first">
        //        <xsd:annotation>
        //          <xsd:documentation>sepa_tran_first</xsd:documentation>
        //        </xsd:annotation>
        //      </xsd:enumeration>
        //      <xsd:enumeration value="sepa_tran_last">
        //        <xsd:annotation>
        //          <xsd:documentation>sepa_tran_last</xsd:documentation>
        //        </xsd:annotation>
        //      </xsd:enumeration>
        //      <xsd:enumeration value="sepa_tran_reversal">
        //        <xsd:annotation>
        //          <xsd:documentation>sepa_tran_reversal</xsd:documentation>
        //        </xsd:annotation>
        //      </xsd:enumeration>
        //    </xsd:restriction>
        //  </xsd:simpleType>
        SEPA_TRAN_FIRST,
        SEPA_TRAN_RCUR
    }

    public enum Scheme {
        //   <xsd:simpleType name="typeCtSEPAB2BId">
        //    <xsd:annotation>
        //      <xsd:documentation>The allowed values for the division
        //                of the SEPA direct debit scheme that a mandate can
        //                belong to.</xsd:documentation>
        //    </xsd:annotation>
        //    <xsd:restriction base="com:typeBaseEnum">
        //      <xsd:enumeration value="none">
        //        <xsd:annotation>
        //          <xsd:documentation>none</xsd:documentation>
        //        </xsd:annotation>
        //      </xsd:enumeration>
        //      <xsd:enumeration value="sepa_b2bid_b2b">
        //        <xsd:annotation>
        //          <xsd:documentation>sepa_b2bid_b2b</xsd:documentation>
        //        </xsd:annotation>
        //      </xsd:enumeration>
        //      <xsd:enumeration value="sepa_b2bid_core">
        //        <xsd:annotation>
        //          <xsd:documentation>sepa_b2bid_core</xsd:documentation>
        //        </xsd:annotation>
        //      </xsd:enumeration>
        //      <xsd:enumeration value="sepa_b2bid_cor1">
        //        <xsd:annotation>
        //          <xsd:documentation>sepa_b2bid_cor1</xsd:documentation>
        //        </xsd:annotation>
        //      </xsd:enumeration>
        //    </xsd:restriction>
        //  </xsd:simpleType>
        SEPA_B2BID_CORE,
        SEPA_B2BID_B2B
    }

    public enum Status {
        //   <xsd:simpleType name="typeCtSEPAMandateStatus">
        //    <xsd:annotation>
        //      <xsd:documentation>The allowed values for the status of
        //                a direct debit mandate.</xsd:documentation>
        //    </xsd:annotation>
        //    <xsd:restriction base="com:typeBaseEnum">
        //      <xsd:enumeration value="none">
        //        <xsd:annotation>
        //          <xsd:documentation>none</xsd:documentation>
        //        </xsd:annotation>
        //      </xsd:enumeration>
        //      <xsd:enumeration value="sepa_mandate_status_open">
        //        <xsd:annotation>
        //          <xsd:documentation>sepa_mandate_status_open</xsd:documentation>
        //        </xsd:annotation>
        //      </xsd:enumeration>
        //      <xsd:enumeration value="sepa_mandate_status_complete">
        //        <xsd:annotation>
        //          <xsd:documentation>sepa_mandate_status_complete</xsd:documentation>
        //        </xsd:annotation>
        //      </xsd:enumeration>
        //      <xsd:enumeration value="sepa_mandate_status_amended">
        //        <xsd:annotation>
        //          <xsd:documentation>sepa_mandate_status_amended</xsd:documentation>
        //        </xsd:annotation>
        //      </xsd:enumeration>
        //      <xsd:enumeration value="sepa_mandate_status_cancelled">
        //        <xsd:annotation>
        //          <xsd:documentation>sepa_mandate_status_cancelled</xsd:documentation>
        //        </xsd:annotation>
        //      </xsd:enumeration>
        //    </xsd:restriction>
        //  </xsd:simpleType>
        NONE,
        OPEN,
        COMPLETE,
        AMENDED,
        CANCELLED
    }

    public enum SepaType {
        //   <xsd:simpleType name="typeSepaTypeOfMandate">
        //    <xsd:restriction base="com:typeBaseEnum">
        //      <xsd:enumeration value="sepa_type_electronic"/>
        //      <xsd:enumeration value="sepa_type_paper"/>
        //    </xsd:restriction>
        //  </xsd:simpleType>
        SEPA_TYPE_ELECTRONIC
    }

}
