package org.pg4200.exam2022;

import org.pg4200.ex11.BitReader;
import org.pg4200.ex11.BitWriter;

import java.util.HashMap;
import java.util.Map;

public class Ex05 {

    public Ex05() {
        InitName();
        InitCode();
        InitMonths();
    }

    HashMap<Integer, String> CourseName = new HashMap<>();
    HashMap<Integer, String> CourseCode = new HashMap<>();
    HashMap<Integer, String> Months = new HashMap<>();

    private void InitName() {
        CourseName.put(1, "Programming");
        CourseName.put(2, "ArtificialIntelligence");
        CourseName.put(3, "FrontendProgramming");
        CourseName.put(4, "Cybersecurity");
        CourseName.put(5, "DataScience");
    }
    private void InitCode() {
        CourseCode.put(1, "PG4200");
        CourseCode.put(2, "AI1701");
        CourseCode.put(3, "FP1453");
        CourseCode.put(4, "SC1007");
        CourseCode.put(5, "DS0112");
    }
    private void InitMonths() {
        Months.put(1, "JAN");
        Months.put(2, "FEB");
        Months.put(3, "MAR");
        Months.put(4, "APR");
        Months.put(5, "MAY");
        Months.put(6, "JUN");
        Months.put(7, "JUL");
        Months.put(8, "AUG");
        Months.put(9, "SEP");
        Months.put(10, "OCT");
        Months.put(11, "NOV");
        Months.put(12, "DEC");
    }

    public int getCourseVal(String code) {

        for (Map.Entry<Integer, String> c: CourseCode.entrySet()) {
            if (c.getValue().equalsIgnoreCase(code)) {
                return c.getKey();
            }
        }
        return -1;
    }

    public String getCourseString(int index) {
        return CourseName.get(index) + "-" + CourseCode.get(index);
    }

    public String getCourseCode(int index) {
        return CourseCode.get(index);
    }

    public int getMonthVal(String month) {

        for (Map.Entry<Integer, String> c: Months.entrySet()) {
            if (c.getValue().equalsIgnoreCase(month)) {
                return c.getKey();
            }
        }
        return -1;
    }

    public String getMonthString(int index) {
        return Months.get(index);
    }

    /**
     * 3 bits: 5 options, for course + courseCode
     * 20 bits: 800k options from 100k-900k.
     * 3 bits: 6 options, for grade
     * 12 bits: 4 digits = 10 000 options ~ 4000, should be high enough
     * 4 bits : Date-Month, 12 alternatives
     * 5 bits : Date-day, 31 alternatives
     * File name takes no space, as it is derived from CourseCode + uniqueId
     *
     * Total: 47 bits
     */

    /** IMPORTANT TO READ
     *
     * Little mistake in the exam here I think. In the description, you mention an id then a grade/assessment,
     * and you also show it in the example format: <program>-<course-code>:<assessment> / <date-of-completion>. File: <filename>;
     * Here however, the unique-id is missing from the format
     * However, in all your examples, there are no assessment/grade, but you have the unique-id
     * Therefore I just added the assessment/grade to the example format, in the order listed earlier,
     * so I could naturally include all that you asked for, though it deviates from the suggested format and examples
     * Here are som examples of how I formatted for the tests I wrote
     * "Programming-PG4200:456987:A / 2022-JUN-06. File: feedback-PG4200-456987.pdf;\n" +
     * "ArtificialIntelligence-AI1701:987456:B / 2021-AUG-13. File: feedback-AI1701-987456.pdf;\n"
     * "FrontendProgramming-FP1453:963258:C / 2022-OCT-30. File: feedback-FP1453-963258.pdf;\n"
     * "Cybersecurity-SC1007:741654:D / 2022-JAN-05. File: feedback-SC1007-741654.pdf;\n"
     * "DataScience-DS0112:159753:F / 2020-MAR-08. File: feedback-DS0112-159753.pdf;\n"
     */

    public byte[] compress(String feedback) {
        BitWriter writer = new BitWriter();

        for (int i = 0; i < feedback.length(); i++) {

            // Course + Code
            while (feedback.charAt(i) != '-') {
                i++;
            }
            i++;

            String courseCode = "";
            while (feedback.charAt(i) != ':') {
                courseCode += feedback.charAt(i);
                i++;
            }

            int courseValue = getCourseVal(courseCode);
            writer.write(courseValue, 3);
            i++;

            // Unique Id
            String id = "";
            for (int j = 0; j < 6; j++) {
                id += feedback.charAt(i + j);
            }
            writer.write(Integer.parseInt(id), 20);
            i += 7;

            // Grade
            writer.write(feedback.charAt(i) - 'A', 3);
            i+=4;

            // Year
            String year = "";
            for (int j = 0; j < 4; j++) {
                year += feedback.charAt(i+j);
            }
            writer.write(Integer.parseInt(year), 12);
            i += 5;

            // Month
            String month = "";
            for (int j = 0; j < 3; j++) {
                month += feedback.charAt(i+j);
            }
            int monthBit = getMonthVal(month);
            writer.write(monthBit, 4);
            i+= 4;

            // Day
            String day = "";

            for (int j = 0; j < 2; j++) {
                day += feedback.charAt(i+j);
            }
            writer.write(Integer.parseInt(day), 5);
            i += 3;

            // Filename (can skip it)
            while (feedback.charAt(i) != ';') {
                i++;
            }
            i++;
        }

        return writer.extract();
    }

    public String decompress(byte[] input) {

        BitReader reader = new BitReader(input);
        String result = "";

        int entries = (input.length * 8) / 47;

        for (int i = 0; i < entries; i++) {

            // Course
            int courseIndex = reader.readInt(3);
            result += getCourseString(courseIndex);
            result += ":";

            // Code
            int code = reader.readInt(20);
            result += code;
            result += ":";

            // Grade
            result += (char) (reader.readInt(3) + 'A');
            result += " / ";

            // Year
            result += reader.readInt(12);
            result += "-";

            // Month
            result += getMonthString(reader.readInt(4));
            result += "-";

            // Day
            result += String.format("%1$" + 2 + "s", reader.readInt(5)).replace(' ', '0');

            // Filename
            result += ". File: feedback-" + getCourseCode(courseIndex) + "-" + code + ".pdf;";

            result += "\n";
        }

        return result;
    }
}