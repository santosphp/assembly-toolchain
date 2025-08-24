START MAIN
STACK 0

INTDEF SUMPOW
INTDEF SUM

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
       END

