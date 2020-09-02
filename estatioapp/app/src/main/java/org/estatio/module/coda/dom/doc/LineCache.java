package org.estatio.module.coda.dom.doc;

import java.util.SortedSet;

public interface LineCache {
    SortedSet<CodaDocLine> linesFor(CodaDocHeadIncInvoiceIta codaDocHeadIncInvoiceIta);

    public static LineCache DEFAULT = codaDocHead -> {
        return codaDocHead.getLines();
    };
}
