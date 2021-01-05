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
import javax.jdo.annotations.Uniques;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;

import com.google.common.collect.ComparisonChain;

import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.Where;

import org.isisaddons.module.security.dom.tenancy.HasAtPath;

import lombok.Getter;
import lombok.Setter;

@PersistenceCapable(
        identityType = IdentityType.DATASTORE,
        schema = "dbo",
        table = "CodaElement"
)
@DatastoreIdentity(
        strategy = IdGeneratorStrategy.IDENTITY,
        column = "id")
@Version(
        strategy = VersionStrategy.VERSION_NUMBER,
        column = "version")
@Queries({
        @Query(
                name = "findByUuid", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.estatio.module.coda.dom.codaelement.CodaElement "
                        + "WHERE uuid == :uuid "),

})
@Uniques({
        @Unique(name = "CodaElement_uuid_UNQ", members = { "uuid" }),
        @Unique(name = "CodaElement_cmpCode_level_elementCode_UNQ", members = { "cmpCode", "elementLevel", "elementCode" }),
})
@DomainObject(
        objectType = "codaelement.CodaElement",
        editing = Editing.DISABLED
)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
)
public class CodaElement implements Comparable<CodaElement>, HasAtPath {

    @Property(hidden = Where.EVERYWHERE)
    @Column(allowsNull = "false")
    @Getter @Setter
    private String uuid;

    @Column(allowsNull = "false", length = 24)
    @Getter @Setter
    private String cmpCode;

    @Column(allowsNull = "false")
    @Getter @Setter
    private short elementLevel;

    @Column(allowsNull = "false", length = 72)
    @Getter @Setter
    private String elementCode;

    @Column(allowsNull = "true", length = 12)
    @Getter @Setter
    private String templateCode;

    // TODO: there is name, shortName and longName; do we need all here? For ITA longName seems to be mapped to tenant name
    @Column(allowsNull = "false", length = 80)
    @Getter @Setter
    private String name;

    @Column(allowsNull = "false", length = 20)
    @Getter @Setter
    private String shortName;

    @Column(allowsNull = "false", length = 240)
    @Getter @Setter
    private String longName;

    @Column(allowsNull = "false")
    @Getter @Setter
    private String atPath;

    @Persistent(
            mappedBy = "codaElement", dependentElement = "true"
    )
    @Getter @Setter
    private SortedSet<CodaElementAddress> addresses = new TreeSet<>();


    @Override
    public int compareTo(final CodaElement other) {
        return ComparisonChain.start()
                .compare(getCmpCode(), other.getCmpCode())
                .compare(getElementLevel(), other.getElementLevel())
                .compare(getElementCode(), other.getElementCode())
                .result();
    }

    @Override
    public String toString() {
        return "CodaElement{" +
                "companyCode='" + getCmpCode() + '\'' +
                ", level='" + getElementLevel() + '\'' +
                ", code='" + getElementCode() + '\'' +
                '}';
    }

}

