
                               Class diagram
                               
                                   Main 
                                /       \
                   CountTransfer          ReadReportFile
                     /        \
     ParseInfoAboutCount       BankCount


Class BankCount used for operation with counts (check, add, withdraw)
Class CountTransfer used for do transactions between counts
Class ParsInfoAboutCount used to process invoice data from and to file
Class ReadReportFile used to output a report file
You can read more information on class comments
Folder archive consist files which transact (successful and not successful)
Folder input consist files to transaction
Folder inputArchive consist files to copy on input folder for check
File CountsInfo consist date about counts
File report consist date about transactions