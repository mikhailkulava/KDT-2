package com.sigmaukraine.trn.report;

public class WebReportAdapter implements ReportAdapter {

    public void write(ReportWriter writer, Object item) {
        if(writer instanceof WebReportWriterWraper && item instanceof WebReportItem) {
            ((WebReportItem)item).message((WebReportWriterWraper) writer);
        }
    }

}
