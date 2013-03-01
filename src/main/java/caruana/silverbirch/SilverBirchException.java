package caruana.silverbirch;

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
    
    public static class SilverBirchTransactionException extends SilverBirchException {

        private static final long serialVersionUID = -9206124810621974524L;

        public SilverBirchTransactionException(String arg0, Throwable arg1) {
            super(arg0, arg1);
        }

        public SilverBirchTransactionException(String arg0) {
            super(arg0);
        }

        public SilverBirchTransactionException(Throwable arg0) {
            super(arg0);
        }
        
    }

    public static class SilverBirchFunctionException extends SilverBirchException {

        private static final long serialVersionUID = 3535780005017942494L;

        public SilverBirchFunctionException(String arg0, Throwable arg1) {
            super(arg0, arg1);
        }

        public SilverBirchFunctionException(String arg0) {
            super(arg0);
        }

        public SilverBirchFunctionException(Throwable arg0) {
            super(arg0);
        }
        
    }

    public static class SilverBirchInternalException extends SilverBirchException {

        private static final long serialVersionUID = 8001753233101943441L;

        public SilverBirchInternalException(String arg0, Throwable arg1) {
            super(arg0, arg1);
        }

        public SilverBirchInternalException(String arg0) {
            super(arg0);
        }

        public SilverBirchInternalException(Throwable arg0) {
            super(arg0);
        }
        
    }

    public static class SilverBirchValidatorException extends SilverBirchException {

        private static final long serialVersionUID = 8001753233101943441L;

        public SilverBirchValidatorException(String arg0, Throwable arg1) {
            super(arg0, arg1);
        }

        public SilverBirchValidatorException(String arg0) {
            super(arg0);
        }

        public SilverBirchValidatorException(Throwable arg0) {
            super(arg0);
        }
        
    }

    public static class SilverBirchBlobException extends SilverBirchException {

        private static final long serialVersionUID = 3794940704807177235L;

        public SilverBirchBlobException(String arg0, Throwable arg1) {
            super(arg0, arg1);
        }

        public SilverBirchBlobException(String arg0) {
            super(arg0);
        }

        public SilverBirchBlobException(Throwable arg0) {
            super(arg0);
        }
        
    }

    public static class SilverBirchItemException extends SilverBirchException {

        private static final long serialVersionUID = -703080989550171692L;

        public SilverBirchItemException(String arg0, Throwable arg1) {
            super(arg0, arg1);
        }

        public SilverBirchItemException(String arg0) {
            super(arg0);
        }

        public SilverBirchItemException(Throwable arg0) {
            super(arg0);
        }
        
    }

}
