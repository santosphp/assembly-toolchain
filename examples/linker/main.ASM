START MAIN
STACK 16

SUMPOW INTUSE
SUM    INTUSE

* MAKE_POWER 3 -> POWER 2 returns 2^3
* MAKE_POWER 2 -> POWER 3 returns 3^2
MACRO
  MAKE_POWER &EXP
  MACRO
            POWER &X
            LOAD #1
            STORE RESULT
            LOAD #0
            STORE COUNT
  &PWR_LOOP LOAD COUNT
            SUB #&EXP
            BRPOS &PWR_END
            BRZERO &PWR_END
            LOAD RESULT
            MULT &X
            STORE RESULT
            LOAD COUNT
            ADD #1
            STORE COUNT
            BR &PWR_LOOP
  &PWR_END
  MEND
MEND

MAIN READ N
     MAKE_POWER 2
     CALL SUMPOW
     WRITE SUM
     STOP

* Memory allocation
RESULT SPACE
COUNT  SPACE
       END

