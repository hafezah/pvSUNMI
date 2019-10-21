package com.sunmi.pocketvendor.network;

public class Global {

    // POCKET VENDOR version 0.4.7.6
    public static final String VER = "0.4.7.6";

    // SERVER URL
    //public static final String URL = "https://www.threegmedia.com/merchantpos";   // LIVE URL
    public static final String URL = "https://www.threegmedia.com/merchantpostest"; // TEST URL

    // POST
    public static final String PHP              = "integration.php";                // Login
    public static final String REFRESH          = "refresh.php";                    // Check Balance
    public static final String METERCHECKER     = "metercheck.php";                 // Meter Validation
    public static final String TRANSACTION      = "transaction.php";                // Execute Transaction
    public static final String ACCOUNT          = "accountreport.php";              // Get Balance
    public static final String SETTLEMENTREPORT = "settlementreport.php";           // Get Transaction History
    public static final String DATEREPORT       = "getdate.php";                    // Get Transaction with Date
    public static final String TREG             = "registerterminal.php";           // terminal detection/registration

    public static final String FOLDER           = "/testfolder";                     // Folder Name
    public static final int LOGOUTCOUNT         = 180000;                           // 1000 * 60 * 3 = 180000 (3 minutes)

}
