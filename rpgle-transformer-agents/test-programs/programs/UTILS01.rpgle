      * Fixed Format RPGLE

     D SquareNumber    PR             15P 2
     D   InputNumber                 15P 2

     D InputNum        S             15P 2
     D ResultNum       S             15P 2

     C                   EVAL      InputNum = 5
     C                   CALL      'SquareNumber'
     C                   PARM                  InputNum
     C                   PARM                  ResultNum
     C
     C                   DSPLY     'Result:'
     C                   DSPLY     ResultNum
     C
     C                   EVAL      *INLR = *ON