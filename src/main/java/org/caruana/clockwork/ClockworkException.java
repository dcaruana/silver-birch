package org.caruana.clockwork;

public class ClockworkException extends RuntimeException {

    private static final long serialVersionUID = 8910785688986682420L;

    public ClockworkException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public ClockworkException(String arg0) {
        super(arg0);
    }

    public ClockworkException(Throwable arg0) {
        super(arg0);
    }


    public static class ClockworkConnectionException extends ClockworkException {

        private static final long serialVersionUID = 3060896288206335336L;

        public ClockworkConnectionException(String arg0, Throwable arg1) {
            super(arg0, arg1);
        }

        public ClockworkConnectionException(String arg0) {
            super(arg0);
        }

        public ClockworkConnectionException(Throwable arg0) {
            super(arg0);
        }
        
    }
}
