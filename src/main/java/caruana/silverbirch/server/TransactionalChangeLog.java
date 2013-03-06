package caruana.silverbirch.server;


import java.util.Collection;

import caruana.silverbirch.Change;
import caruana.silverbirch.ChangeLog;
import caruana.silverbirch.server.log.ChangeLogImpl;

public class TransactionalChangeLog implements ChangeLog
{
    private ChangeLogImpl log;
    private TransactionImpl transaction;
    
    public TransactionalChangeLog(ChangeLogImpl log, TransactionImpl transaction)
    {
        this.log = log;
        this.transaction = transaction;
    }

    @Override
    public Collection<Change> getChanges(Object transactionId)
    {
        return log.getChangeLog(transaction.getConnection(), transactionId);
    }

}
