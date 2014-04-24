package com.sigmaukraine.trn.report;



public class ExceptionManager {

    private static ExceptionManager instance = new ExceptionManager();

    private static ThreadLocal<Boolean> throwRunTimeException = new ThreadLocal<Boolean>(){
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    /**
     * An instance of the ExceptionManager all the operations will be managed
     * through
     * @return ExceptionManager instance
     */
    public static ExceptionManager getInstance() {
        return instance;
    }

    /**
     * A constructor of the class. Since we do not need to create multiple
     * instances it's private.
     */
    private ExceptionManager() {}

    public void process(String description, Exception exception) {
        if (throwRunTimeException.get()) {
            throw new InterruptScenarioException(description,exception);
        }
    }

    public void process(Exception exception) {
        if (throwRunTimeException.get()) {
            throw new InterruptScenarioException(exception);
        }
    }
    
    public void process(String description) {
        if (throwRunTimeException.get()) {
            throw new InterruptScenarioException(description);
        }
    }
    
    public void error(String title) {
        Report.getReport().error(title, new Throwable(title));
        process(title);
    }
    public void error(String title, Exception e) {
        Report.getReport().error(title, e);
        process(title, e);
    }
    public void error(String title, Exception e, SourceProvider page) {
        Report.getReport().error(title, e, page);
        process(title, e);
    }
    
    public static void setThrowExceptionOnWebError(boolean value) {
        throwRunTimeException.set(value);
    }
    
    public static boolean isThrowExceptionOnWebError() {
        return throwRunTimeException.get();
    }
}
