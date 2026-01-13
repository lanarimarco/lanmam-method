      *===============================================================
      * Program: CUST002 - Customer Maintenance
      * Purpose: Display and edit customer information by customer number
      * Author:  Test Program
      * Date:    2025-12-15
      *===============================================================

     FCUSTMAST  UF A E           K DISK
     FCUSTDSP   CF   E             WORKSTN

      *---------------------------------------------------------------
      * Main Processing
      *---------------------------------------------------------------
     C                   ExFmt     PROMPT

     C                   DoW       *IN03 = *Off

      * Clear error and success indicators
     C                   Eval      *IN90 = *Off
     C                   Eval      *IN91 = *Off
     C                   Eval      PMSG = *Blanks

      * Validate customer number
     C                   If        PCUSTNO = 0
     C                   Eval      *IN90 = *On
     C                   Eval      PMSG = 'Customer number required'
     C                   ExFmt     PROMPT
     C                   Iter
     C                   EndIf

      * Read customer master for update
     C     PCUSTNO       Chain     CUSTMAST

     C                   If        %Found(CUSTMAST)
      * Customer found - display details for editing
     C                   Eval      DCUSTNO = CUSTNO
     C                   Eval      DCUSTNAME = CUSTNAME
     C                   Eval      DADDR1 = ADDR1
     C                   Eval      DCITY = CITY
     C                   Eval      DSTATE = STATE
     C                   Eval      DZIP = ZIP
     C                   Eval      DPHONE = PHONE
     C                   Eval      DBALANCE = BALANCE

     C                   ExFmt     EDITDTL

      * Check if user pressed F3 to cancel or F12 to return
     C                   If        *IN03 = *On or *IN12 = *On
     C                   Leave
     C                   EndIf

      * Validate edited data
     C                   Eval      *IN90 = *Off
     C                   Eval      PMSG = *Blanks

     C                   If        DCUSTNAME = *Blanks
     C                   Eval      *IN90 = *On
     C                   Eval      PMSG = 'Customer name required'
     C                   ExFmt     EDITDTL
     C                   Iter
     C                   EndIf

     C                   If        DCITY = *Blanks
     C                   Eval      *IN90 = *On
     C                   Eval      PMSG = 'City required'
     C                   ExFmt     EDITDTL
     C                   Iter
     C                   EndIf

     C                   If        DSTATE = *Blanks
     C                   Eval      *IN90 = *On
     C                   Eval      PMSG = 'State required'
     C                   ExFmt     EDITDTL
     C                   Iter
     C                   EndIf

      * Update customer master with edited values
     C                   Eval      CUSTNAME = DCUSTNAME
     C                   Eval      ADDR1 = DADDR1
     C                   Eval      CITY = DCITY
     C                   Eval      STATE = DSTATE
     C                   Eval      ZIP = DZIP
     C                   Eval      PHONE = DPHONE
     C                   Eval      BALANCE = DBALANCE

     C                   Update    CUSTREC

      * Display success message
     C                   Eval      *IN91 = *On
     C                   Eval      PMSG = 'Customer updated successfully'

     C                   Else
      * Customer not found
     C                   Eval      *IN90 = *On
     C                   Eval      PMSG = 'Customer not found'
     C                   EndIf

     C                   ExFmt     PROMPT

     C                   EndDo

     C                   Eval      *INLR = *On
