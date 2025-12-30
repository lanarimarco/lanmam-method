      *===============================================================
      * Program: CUST001 - Customer Inquiry
      * Purpose: Display customer information by customer number
      * Author:  Test Program
      * Date:    2025-12-15
      *===============================================================

     FCUSTMAST  IF   E           K DISK
     FCUSTDSP   CF   E             WORKSTN

      *---------------------------------------------------------------
      * Main Processing
      *---------------------------------------------------------------
     C                   ExFmt     PROMPT

     C                   DoW       *IN03 = *Off

      * Clear error indicator
     C                   Eval      *IN90 = *Off
     C                   Eval      PMSG = *Blanks

      * Validate customer number
     C                   If        PCUSTNO = 0
     C                   Eval      *IN90 = *On
     C                   Eval      PMSG = 'Customer number required'
     C                   ExFmt     PROMPT
     C                   Iter
     C                   EndIf

      * Read customer master
     C     PCUSTNO       Chain     CUSTMAST

     C                   If        %Found(CUSTMAST)
      * Customer found - display details
     C                   Eval      DCUSTNO = CUSTNO
     C                   Eval      DCUSTNAME = CUSTNAME
     C                   Eval      DADDR1 = ADDR1
     C                   Eval      DCITY = CITY
     C                   Eval      DSTATE = STATE
     C                   Eval      DZIP = ZIP
     C                   Eval      DPHONE = PHONE
     C                   Eval      DBALANCE = BALANCE

     C                   ExFmt     DETAIL

     C                   Else
      * Customer not found
     C                   Eval      *IN90 = *On
     C                   Eval      PMSG = 'Customer not found'
     C                   EndIf

     C                   ExFmt     PROMPT

     C                   EndDo

     C                   Eval      *INLR = *On
