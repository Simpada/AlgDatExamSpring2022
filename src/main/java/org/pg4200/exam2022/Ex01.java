package org.pg4200.exam2022;

public class Ex01 {

    public String regexPartA() {

        /**
         * Making an assumption here that the test folder will not be a source folder,
         * so will have at least one parent folder
         */

        return "(/[a-zA-z0-9]+)+/[Tt][Ee][Ss][Tt](/[a-zA-z0-9]+)*/[a-zA-z0-9]+[Tt][Ee][Ss][Tt]\\.java";
    }

    public String regexPartB() {

        /**
         * The added complexity allows the subject and the mention of exam to happen in any order,
         * but still requires both, as well as for any random letters to be capitalized
         */

        return "@[a-zA-Z0-9]+:.*" +
                "((([Pp][Gg][a-zA-Z]*[0-9]{3,4})|" +
                "([Pp][Rr][Oo][Gg][Rr][Aa][Mm][Mm][Ii][Nn][Gg])|([Pp][Rr][Oo][Gg][Rr][Aa][Mm][Mm][Ee][Rr][Ii][Nn][Gg]))" +
                ".*(([Ee][Xx][Aa][Mm])|([Ee][Kk][Ss][Aa][Mm][Ee][Nn]))|" +
                "(([Ee][Xx][Aa][Mm])|([Ee][Kk][Ss][Aa][Mm][Ee][Nn])).*" +
                "(([Pp][Gg][a-zA-Z]*[0-9]{3,4})|" +
                "([Pp][Rr][Oo][Gg][Rr][Aa][Mm][Mm][Ii][Nn][Gg])|([Pp][Rr][Oo][Gg][Rr][Aa][Mm][Mm][Ee][Rr][Ii][Nn][Gg])))" +
                ".*\\?";
    }
}
