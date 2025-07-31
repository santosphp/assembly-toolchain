| mnemonic | opcode | words | operands | action                   | D/In/Im         |
|----------|--------|-------|----------|--------------------------|-----------------|
| ADD      | 02     | 2     | 1        | ACC  <- ACC + opd1       | D/In/Im         |
| BR       | 00     | 2     | 1        | PC   <- opd1             | D/In            |
| BRNEG    | 05     | 2     | 1        | PC   <- opd1, se ACC < 0 | D/In            |
| BRPOS    | 01     | 2     | 1        | PC   <- opd1, se ACC > 0 | D/In            |
| BRZERO   | 04     | 2     | 1        | PC   <- opd1, se ACC = 0 | D/In            |
| CALL     | 15     | 2     | 1        | [SP] <- PC; PC <- opd1   | D/In            |
| COPY     | 13     | 3     | 2        | opd1 <- opd2             | D/In <- D/In/Im |
| DIVIDE   | 10     | 2     | 1        | ACC  <- ACC / opd1       | D/In/Im         |
| LOAD     | 03     | 2     | 1        | ACC  <- opd1             | D/In/Im         |
| MULT     | 14     | 2     | 1        | ACC  <- ACC * opd1       | D/In/Im         |
| PUSH     | 17     | 2     | 1        | [SP] <- Rs               | -               |
| POP      | 18     | 2     | 1        | Rd   <- [SP]             | -               |
| READ     | 12     | 2     | 1        | opd1 <- _input stream_   | D/In            |
| RET      | 16     | 1     | 0        | PC   <- [SP]             | -               |
| STOP     | 11     | 1     | 0        | Término de execução      | -               |
| STORE    | 07     | 2     | 1        | opd1 <- ACC              | D/In            |
| SUB      | 06     | 2     | 1        | ACC  <- ACC - opd1       | D/In/Im         |
| WRITE    | 08     | 2     | 1        | _Output stream_ <- opd1  | D/In/Im         |

| addressing mode | value to add to the instruction        |
|-----------------|----------------------------------------|
| Direct          | + 0                                    |
| Indirect        | + 32 if opd1, +64 if opd2, +98 if both |
| Immediate       | + 128    operand is the immediate      |

