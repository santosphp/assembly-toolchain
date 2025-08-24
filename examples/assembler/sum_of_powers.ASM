START MAIN
STACK 16

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

* main function
MAIN READ N
     MAKE_POWER 2

     CALL SUMPOW
     WRITE SUM
     STOP

* subroutine, this can later be put in another file
SUMPOW     LOAD #0
           STORE SUM
           LOAD #1
           STORE I
LOOP_START LOAD I
           SUB N
           BRPOS LOOP_END 
           POWER I

           LOAD SUM
           ADD RESULT
           STORE SUM
           LOAD I
           ADD #1
           STORE I
           BR LOOP_START
LOOP_END
           RET

* Memory allocation
N      SPACE
SUM    SPACE
I      SPACE
RESULT SPACE
COUNT  SPACE
       END

