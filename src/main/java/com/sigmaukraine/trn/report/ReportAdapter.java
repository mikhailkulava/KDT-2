package com.sigmaukraine.trn.report;

public interface ReportAdapter {
    void write(ReportWriter writer,Object item);
}
