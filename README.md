# BankTransfer

## Description

This program performs transactions between accounts.
We take information about existing accounts and their balances from
the CountsInfo file. We take information about the necessary transactions
from the input folder. After the transaction, all files from the input folder
are moved to the archive folder.
Before the transaction, we check the correctness of the entered line in the
transaction file, whether such accounts exist, and whether there is enough
money in the account from which we are making the transfer. After this,
we move money between accounts. After processing the transaction file
is completed, move it to the archive folder.
The invoice file is updated after all transactions are completed.
After each transaction, the operation is recorded in a report file.
It is possible to output the entire report file or part of the records by
date to the console.

## How used program

When you start the program, you must enter the value into the console.

1. Check all files in the input folder and make transactions
2. Output report file to console
3. Output a report file to the console for specified dates
4. Complete the program

## Class diagram

                                   Main 
                                /       \
                   CountTransfer          ReadReportFile
                     /        \
     ParseInfoAboutCount       BankCount

## Class description

* Class BankCount used for operation with counts (check, add, withdraw)
* Class CountTransfer used for do transactions between counts
* Class ParsInfoAboutCount used to process invoice data from and to file
* Class ReadReportFile used to output a report file
* You can read more information on class comments
* Folder archive consist files which transact (successful and not successful)
* Folder input consist files to transaction
* File CountsInfo consist date about counts
* File report consist date about transactions