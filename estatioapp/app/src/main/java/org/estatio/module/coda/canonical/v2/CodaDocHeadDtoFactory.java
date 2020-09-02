package org.estatio.module.coda.canonical.v2;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;

import org.estatio.canonical.coda.v2.CodaDocHeadDto;
import org.estatio.canonical.coda.v2.CodaDocHeadType;
import org.estatio.canonical.common.v2.CodaDocKey;
import org.estatio.module.base.platform.applib.DtoFactoryAbstract;
import org.estatio.module.coda.dom.doc.CodaDocHeadIncInvoiceIta;

@DomainService(
        nature = NatureOfService.DOMAIN,
        objectType = "coda.canonical.v2.CodaDocHeadDtoFactory"
)
public class CodaDocHeadDtoFactory extends DtoFactoryAbstract<CodaDocHeadIncInvoiceIta, CodaDocHeadDto> {

    public CodaDocHeadDtoFactory() {
        super(CodaDocHeadIncInvoiceIta.class, CodaDocHeadDto.class);
    }

    protected CodaDocHeadDto newDto(final CodaDocHeadIncInvoiceIta codaDocHeadIncInvoiceIta) {
        final CodaDocHeadDto dto = new CodaDocHeadDto();
        dto.setMajorVersion("2");
        dto.setMinorVersion("0");

        copyOver(codaDocHeadIncInvoiceIta, dto);

        return dto;
    }

    CodaDocHeadType newType(final CodaDocHeadIncInvoiceIta codaDocHeadIncInvoiceIta) {
        final CodaDocHeadType dto = new CodaDocHeadType();
        copyOver(codaDocHeadIncInvoiceIta, dto);
        return dto;
    }

    private void copyOver(final CodaDocHeadIncInvoiceIta codaDocHeadIncInvoiceIta, final CodaDocHeadType dto) {
        dto.setSelf(mappingHelper.oidDtoFor(codaDocHeadIncInvoiceIta));

        final CodaDocKey docKey = new CodaDocKey();
        docKey.setCmpCode(codaDocHeadIncInvoiceIta.getCmpCode());
        docKey.setDocCode(codaDocHeadIncInvoiceIta.getDocCode());
        docKey.setDocNum(codaDocHeadIncInvoiceIta.getDocNum());
        dto.setCodaDocKey(docKey);

        dto.setCodaPeriod(codaDocHeadIncInvoiceIta.getCodaPeriod());

        dto.setIncomingInvoice(mappingHelper.oidDtoFor(codaDocHeadIncInvoiceIta.getIncomingInvoice()));
    }


}
