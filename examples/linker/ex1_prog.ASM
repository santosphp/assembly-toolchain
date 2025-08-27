	STACK 	16
	START 	PROG

SOMA	INTUSE
VAL_C	INTUSE
	INTDEF	INI

INI	LOAD 	VAL_A     	* Carrega o valor 10
	ADD 	VAL_C    	* Adiciona o valor externo
    	CALL 	SOMA        	* Chama a sub-rotina externa
    	STORE 	RES  		* Guarda o resultado
	WRITE	RES
    	STOP             	* Fim da execução

VAL_A	CONST 	10
RES	SPACE 	1		* Reserva 1 palavra de memória para o resultado

	END