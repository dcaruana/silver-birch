package org.caruana.silverbirch;

public class SilverBirchException extends RuntimeException {

    private static final long serialVersionUID = 8910785688986682420L;

    public SilverBirchException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public SilverBirchException(String arg0) {
        super(arg0);
    }

    public SilverBirchException(Throwable arg0) {
        super(arg0);
    }


    public static class SilverBirchConnectionException extends SilverBirchException {

        private static final long serialVersionUID = 3060896288206335336L;

        public SilverBirchConnectionException(String arg0, Throwable arg1) {
            super(arg0, arg1);
        }

        public SilverBirchConnectionException(String arg0) {
            super(arg0);
        }

        public SilverBirchConnectionException(Throwable arg0) {
            super(arg0);
        }
        
    }
}
