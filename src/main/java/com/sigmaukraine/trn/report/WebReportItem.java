package com.sigmaukraine.trn.report;

import org.apache.log4j.Level;

import com.netcracker.automation.util.Utils;

public abstract class WebReportItem {
    public static Message message(String title, Level level, String message, Throwable throwable, SourceProvider page) {
        return new Message(title, level, message, throwable, page);
    }
    public static OpenLog openLog(String logName, String description) {
        return new OpenLog(logName, description);
    }
    public static OpenSection openSection(String title, String message, SourceProvider page) {
        return new OpenSection(title, message, page);
    }
    public static CloseSection closeSection() {
        return new CloseSection();
    }
    public static CloseLog closeLog() {
        return new CloseLog();
    }
    
    public abstract void message(WebReportWriterWraper webReportWriter);
    
    
    public static class Message extends WebReportItem {
        private String title;
        private Level level;
        private String message;
        private SourceProvider page;
        private Throwable throwable;
        public Message(String title, Level level, String message, Throwable throwable, SourceProvider page) {
            this.title = title;
            this.level = level;
            this.message = message;
            this.page = page;
            this.throwable = throwable;
        }
        public String getTitle() {
            return title;
        }
        public Level getLevel() {
            return level;
        }
        public String getMessage() {
            return message;
        }
        public SourceProvider getPage() {
            return page;
        }
        public Throwable getThrowable() {
            return this.throwable;
        }
        @Override
        public void message(WebReportWriterWraper webReportWriter) {
            String message = this.message;
            if (this.throwable != null) {
                message += "<pre>"+Utils.getStackTrace(this.throwable)+"</pre>";
            }
            webReportWriter.getWebReportWriter().message(getTitle(), getLevel(), message, getPage());
        }
    }
    public static class OpenLog extends WebReportItem {
        private String logName;
        private String description;
        public OpenLog(String logName, String description) {
            this.logName = logName;
            this.description = description;
        }
        public String getLogName() {
            return logName;
        }
        public String getDescription() {
            return description;
        }
        @Override
        public void message(WebReportWriterWraper webReportWriter) {
            webReportWriter.getWebReportWriter().openLog(getLogName(),getDescription());
        }
    }
    public static class OpenSection extends WebReportItem {
        private String title;
        private String message;
        private SourceProvider page;
        public OpenSection(String title, String message, SourceProvider page) {
            this.title = title;
            this.message = message;
            this.page = page;
        }
        public String getTitle() {
            return title;
        }
        public String getMessage() {
            return message;
        }
        public SourceProvider getPage() {
            return page;
        }
        @Override
        public void message(WebReportWriterWraper webReportWriter) {
            webReportWriter.getWebReportWriter().openSection(getTitle(), getMessage(), getPage());
        }
    }
    public static class CloseSection extends WebReportItem {
        @Override
        public void message(WebReportWriterWraper webReportWriter) {
            webReportWriter.getWebReportWriter().closeSection();
        }
    }
    public static class CloseLog extends WebReportItem {
        @Override
        public void message(WebReportWriterWraper webReportWriter) {
            //do nothing because Report is always closed
        }
    }
}
