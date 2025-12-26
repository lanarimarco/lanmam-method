      *===============================================================
      * Program: CUST001 - Customer Inquiry
      * Purpose: Display customer information by customer number
      * Author:  Test Program
      * Date:    2025-12-15
      *===============================================================

     FCUSTMAST  IF   E           K DISK
     FCUSTDSP   CF   E             WORKSTN

      *---------------------------------------------------------------
      * Data Structures
      *---------------------------------------------------------------
     D CUSTDS          DS
     D  CUSTNO                 1      5  0
     D  CUSTNAME               6     35
     D  ADDR1                 36     65
     D  CITY                  66     85
     D  STATE                 86     87
     D  ZIP                   88     92  0
     D  PHONE                 93    104
     D  BALANCE              105    114  2

      *---------------------------------------------------------------
      * Standalone Variables
      *---------------------------------------------------------------
     D wCustNo         S              5  0
     D wMessage        S             50
     D wExit           S              1

      *---------------------------------------------------------------
      * Main Processing
      *---------------------------------------------------------------
     C                   ExFmt     PROMPT

     C                   DoW       *IN03 = *Off

      * Clear error indicator
     C                   Eval      *IN90 = *Off
     C                   Eval      wMessage = *Blanks

      * Validate customer number
     C                   If        PCUSTNO = 0
     C                   Eval      *IN90 = *On
     C                   Eval      wMessage = 'Customer number required'
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
     C                   Eval      wMessage = 'Customer not found'
     C                   EndIf

     C                   ExFmt     PROMPT

     C                   EndDo

     C                   Eval      *INLR = *On
