package org.estatio.module.coda.dom.codaelement;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
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
        table = "CodaElementBank"
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
                        + "FROM org.estatio.module.coda.dom.codaelement.CodaElementBank "
                        + "WHERE codaElement == :codaElement "),
        @Query(
                name = "findUnique", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.estatio.module.coda.dom.codaelement.CodaElementBank "
                        + "WHERE codaElement == :codaElement "
                        + " && tag == :tag "),
        @Query(
                name = "findByIban", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.estatio.module.coda.dom.codaelement.CodaElementBank "
                        + "WHERE iban == :iban ")
})
@Unique(name = "CodaElementBank_codaElement_tag_UNQ", members = { "codaElement", "tag"})
@DomainObject(
        objectType = "codaelement.CodaElementBank",
        editing = Editing.DISABLED
)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
)
public class CodaElementBank implements Comparable<CodaElementBank> {

    public CodaElementBank() {}

    @Column(allowsNull = "false", name = "elementId")
    @Property(hidden = Where.PARENTED_TABLES)
    @Getter @Setter
    private CodaElement codaElement;

    @Getter @Setter
    @Property()
    private short tag;

    @Property()
    @Getter @Setter
    private boolean defaultBank;

    @Column(allowsNull = "false", length = 36)
    @Property()
    @Getter @Setter
    private String bankName;

    @Column(allowsNull = "false", length = 240)
    @Property()
    @Getter @Setter
    private String longName;

    @Column(allowsNull = "false", length = 36)
    @Property()
    @Getter @Setter
    private String accountNumber; //NOTE: used in ITA for mandate reference!!

    @Column(allowsNull = "false", length = 36)
    @Property()
    @Getter @Setter
    private String iban;

    @Column(allowsNull = "true", length = 36)
    @Property()
    @Getter @Setter
    private String swift;

    @Column(allowsNull = "true", length = 20)
    @Property()
    @Getter @Setter
    private String accountRef; //NOTE: always "."

    @Column(allowsNull = "true", length = 35)
    @Property()
    @Getter @Setter
    private String address1; //NOTE: always "."

    @Persistent(
            mappedBy = "elementBank", dependentElement = "true"
    )
    @Getter @Setter
    private SortedSet<CodaBankMandate> addresses = new TreeSet<>();


    //region > compareTo, toString
    @Override
    public int compareTo(final CodaElementBank other) {
        return ComparisonChain.start()
                .compare(getCodaElement(), other.getCodaElement())
                .compare(getTag(), other.getTag())
                .result();
    }

    @Override
    public String toString() {
        return "CodaElementBank{" +
                "element=" + getCodaElement() +
                ", tag='" + getTag() + '\'' +
                ", iban='" + getIban() + '\'' +
                '}';
    }

    //endregion

}
