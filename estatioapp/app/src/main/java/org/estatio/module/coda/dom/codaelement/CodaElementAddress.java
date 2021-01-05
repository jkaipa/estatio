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
        table = "CodaElementAddress"
)
@DatastoreIdentity(
        strategy = IdGeneratorStrategy.IDENTITY,
        column = "id")
@Version(
        strategy = VersionStrategy.VERSION_NUMBER,
        column = "version")
@Queries({
        @Query(
                name = "findByCodaElement", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.estatio.module.coda.dom.codaelement.CodaElementAddress "
                        + "WHERE codaElement == :codaElement "),
        @Query(
                name = "findByCodaElementAndTag", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.estatio.module.coda.dom.codaelement.CodaElementAddress "
                        + "WHERE codaElement == :codaElement "
                        + "   && tag      == :tag ")
})
@Unique(name = "CodaElementAddress_supplier_tag_UNQ", members = { "codaElement", "tag" })
@DomainObject(
        objectType = "codaelement.CodaElementAddress",
        editing = Editing.DISABLED
)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
)
public class CodaElementAddress implements Comparable<CodaElementAddress> {

    public CodaElementAddress() {}

    @Column(allowsNull = "false", name = "elementId")
    @Property(hidden = Where.PARENTED_TABLES)
    @Getter @Setter
    private CodaElement codaElement;

    @Getter @Setter
    @Property()
    private short tag;

    @Column(allowsNull = "false", length = 80)
    @Property()
    @Getter @Setter
    private String name;

    @Property()
    @Getter @Setter
    private boolean defaultAddress;

    @Column(allowsNull = "true", length = 35)
    @Property()
    @Getter @Setter
    private String address1;

    @Column(allowsNull = "true", length = 35)
    @Property()
    @Getter @Setter
    private String address2;

    @Column(allowsNull = "true", length = 35)
    @Property()
    @Getter @Setter
    private String address3;

    @Column(allowsNull = "true", length = 35)
    @Property()
    @Getter @Setter
    private String address4;

    @Column(allowsNull = "true", length = 35)
    @Property()
    @Getter @Setter
    private String address5;

    @Column(allowsNull = "true", length = 35)
    @Property()
    @Getter @Setter
    private String address6;

    @Column(allowsNull = "true", length = 12)
    @Property()
    @Getter @Setter
    private String postCode;

    @Column(allowsNull = "true", length = 20)
    @Property()
    @Getter @Setter
    private String tel;

    @Column(allowsNull = "true", length = 20)
    @Property()
    @Getter @Setter
    private String fax;

    @Column(allowsNull = "true", length = 255)
    @Property()
    @Getter @Setter
    private String country;

    @Column(allowsNull = "true", length = 20)
    @Property()
    @Getter @Setter
    private String language;

    @Column(allowsNull = "true", length = 12)
    @Property()
    @Getter @Setter
    private String category;

    @Column(allowsNull = "true", length = 128)
    @Property()
    @Getter @Setter
    private String eMail;


    //region > compareTo, toString
    @Override
    public int compareTo(final CodaElementAddress other) {
        return ComparisonChain.start()
                .compare(getCodaElement(), other.getCodaElement())
                .compare(getTag(), other.getTag())
                .result();
    }

    @Override
    public String toString() {
        return "ElementAddress{" +
                "element=" + getCodaElement() +
                ", tag=" + getTag() +
                '}';
    }

    //endregion

}
