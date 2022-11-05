package org.pg4200.exam2022;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ex05Test {

    public String initInputs() {
        return "Programming-PG4200:456987:A / 2022-JUN-06. File: feedback-PG4200-456987.pdf;\n" +
                "ArtificialIntelligence-AI1701:987456:B / 2021-AUG-13. File: feedback-AI1701-987456.pdf;\n" +
                "FrontendProgramming-FP1453:963258:C / 2022-OCT-30. File: feedback-FP1453-963258.pdf;\n" +
                "Cybersecurity-SC1007:741654:D / 2022-JAN-05. File: feedback-SC1007-741654.pdf;\n" +
                "DataScience-DS0112:159753:F / 2020-MAR-08. File: feedback-DS0112-159753.pdf;\n";
    }


    @Test
    public void testMedium(){
        String input = initInputs();
        Ex05 ar = new Ex05();

        byte[] c1 = ar.compress(input);

        String decom = ar.decompress(c1);

        System.out.println(decom);

        assertEquals(input, decom);
        assertEquals(input.length(), decom.length());
    }

    @Test
    public void testSmall(){
        String input = "Cybersecurity-SC1007:741654:D / 2022-JAN-05. File: feedback-SC1007-741654.pdf;\n";
        Ex05 ar = new Ex05();

        byte[] c1 = ar.compress(input);

        String dec = ar.decompress(c1);

        assertEquals(input.length(), dec.length());
    }

}
