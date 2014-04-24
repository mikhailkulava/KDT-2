package com.sigmaukraine.trn.report;

import org.apache.log4j.Level;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Report {
    private static Report instance;
    private Queue<ReportWriter> reportWriters;
    private Queue<ReportAdapter> reportAdapters;
    private Report() {
        reportWriters = new ConcurrentLinkedQueue<ReportWriter>();
        reportAdapters = new ConcurrentLinkedQueue<ReportAdapter>();
    }
    static {
        getReport().addWriter(new WebReportWriterWraper());
        getReport().addAdapter(new WebReportAdapter());
    }
    public static Report getReport() {
        if(instance==null) instance = new Report();
        return instance;
    }
    public Iterator<ReportWriter> writerIterator() {
        return reportWriters.iterator();
    }
    public boolean addWriter(ReportWriter e) {
        return reportWriters.add(e);
    }
    public boolean removeWriter(ReportWriter o) {
        return reportWriters.remove(o);
    }
    public void removeAllWriters() {
        reportWriters.clear();
    }
    public Iterator<ReportAdapter> adapterIterator() {
        return reportAdapters.iterator();
    }
    public boolean addAdapter(ReportAdapter e) {
        return reportAdapters.add(e);
    }
    public boolean removeAdapter(ReportAdapter o) {
        return reportAdapters.remove(o);
    }
    public void removeAllAdapters() {
        reportAdapters.clear();
    }
    public void message(Object item) {
        for(ReportAdapter adapter:reportAdapters) {
            for(ReportWriter report:reportWriters) {
                adapter.write(report, item);
            }
        }
    }

    public void info(String title) {
        info(title, title, null);
    }
    public void info(String title, String message) {
        info(title, message, null);
    }
    public void info(String title, String message, SourceProvider page) {
        message(title, Level.INFO, message, null, page);
    }
    public void warn(String title) {
        warn(title, title, null);
    }
    public void warn(String title, String message) {
        warn(title, message, null);
    }
    public void warn(String title, String message, SourceProvider page) {
        message(title, Level.WARN, message, null, page);
    }
    public void error(String title) {
        error(title, title, null);
    }    
    public void error(String title, String message) {
        error(title, message, null);
    }
    public void error(String title, Throwable t) {
        error(title, t, null);
    }
    public void error(String title, Throwable t, SourceProvider page) {
        message(title,Level.ERROR, "", t, page);
    }
    public void error(String title, String message, SourceProvider page) {
        message(title, Level.ERROR, message, null, page);
    }
    private void message(String title, Level level, String message, Throwable throwable, SourceProvider page) {
        message(WebReportItem.message(title, level, message, throwable, page));
    }
    public void openLog(final String logName) {
        openLog(logName, "");
    }
    public void openLog(String logName, String description) {
        message(WebReportItem.openLog(logName, description));
    }
    public void closeLog() {
        message(WebReportItem.closeLog());
    }
    public void openSection(String sectionName) {
        openSection(sectionName, null, null);
    }
    public void openSection(String sectionName, String message, SourceProvider page) {
        message(WebReportItem.openSection(sectionName, message, page));
    }
    public void closeSection() {
        message(WebReportItem.closeSection());
    }
}
