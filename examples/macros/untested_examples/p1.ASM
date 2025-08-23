  START TESTE
*
  MACRO
    SCALE &RP
* Macro aninhada MULTSC: Realiza A * B e então divide por &RP
    MACRO MULTSC &A,&B,&C
      LOAD &A                * Carrega o valor de &A no acumulador
      MULT &B                * Multiplica o acumulador por &B
      DIVIDE &RP             * Divide o acumulador por &RP
      STORE &C               * Armazena o resultado em &C
    MEND
* Macro aninhada DIVSC: Realiza A / B e então multiplica por 8
    MACRO DIVSC &A,&B,&C
      LOAD &A                * Carrega o valor de &A no acumulador
      DIVIDE &B              * Divide o acumulador por &B
      MULT @8                * Multiplica o acumulador por 8
      STORE &C               * Armazena o resultado em &C
    MEND
  MEND
*
* Definição da macro DISCR
  MACRO
&LAB DISCR &A,&B,&C,&D
&LAB MULTSC &A,&C,TEMP1     * Chama MULTSC: TEMP1 = (&A * &C) / &RP
    MULTSC TEMP1,@4,TEMP1   * Chama MULTSC: TEMP1 = (TEMP1 * @4) / &RP
    MULTSC &A,&B,TEMP2      * Chama MULTSC: TEMP2 = (&A * &B) / &RP
    SUB TEMP1               * Subtrai TEMP1 do acumulador (que contém TEMP2)
    STORE &D                * Armazena o resultado final em &D
  MEND
*
* Corpo principal do programa
  READ A                  * Lê um valor para A
  READ B                  * Lê um valor para B
  READ C                  * Lê um valor para C
  SCALE 3                 * Invoca a macro SCALE, definindo MULTSC e DIVSC com &RP=3
  DISCR A,B,C,D           * Invoca a macro DISCR para realizar os cálculos definidos
  WRITE D                 * Escreve o valor de D
  STOP                    * Encerra a execução do programa
*
* Definição das áreas de memória (símbolos)
  A SPACE                 * Reserva uma palavra para A
  B SPACE                 * Reserva uma palavra para B
  C SPACE                 * Reserva uma palavra para C
  D SPACE                 * Reserva uma palavra para D
  TEMP1 SPACE             * Reserva uma palavra para TEMP1
  TEMP2 SPACE             * Reserva uma palavra para TEMP2
*
  END                     * Indica o fim do programa fonte
