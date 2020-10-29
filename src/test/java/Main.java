package test.java;


public class Main {
    public Main() {
        f();
    }

    public static void main(String[] args) {
        new Main();
    }

    private void out(Object o) { System.out.println(o + ""); }

    private String spaceGenerator(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i<len; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    private void f() {
        String star = "**";
        int i = 0, startspace = 40, middlespace = 0;
        boolean backtrack =false;
        while(true) {

            if (i==0) {
                out(spaceGenerator(startspace) + star + star);

                if (!backtrack) {
                    startspace-=3;
                    middlespace+=4;
                } else {
                    startspace+=4;
                    middlespace-=4;
                }
            } else if (i%3==0) {
                if (!backtrack) {
                    out(spaceGenerator(--startspace) + star + star + spaceGenerator(middlespace) + star + star);
                    startspace-=3;
                    middlespace+=8;
                } else {
                    out(spaceGenerator(startspace) + star + star + spaceGenerator(middlespace) + star + star);
                    startspace+=4;
                    middlespace-=8;
                }
            } else {
                out(spaceGenerator(startspace) + star + "*" + spaceGenerator(middlespace) + star + "*");

                if (!backtrack) {
                    startspace-=3;
                    middlespace+=6;
                } else {
                    startspace+=3;
                    middlespace-=6;
                }
            }

            if (i == 0 && backtrack) {
                break;
            }

            if (backtrack) i--;
            else i++;

            if (i==12) {
                backtrack=true;
            }
        }
    }
}