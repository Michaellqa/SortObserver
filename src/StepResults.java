
public class StepResults {

    String el1 = "";
    String el2 = "";
    String el3 = "";
    String el4 = "";
    String el5 = "";
    String el6 = "";
    String el7 = "";
    String el8 = "";
    String el9 = "";
    String el10 = "";
    String el11 = "";
    String el12 = "";
    String el13 = "";
    String el14 = "";
    String el15 = "";
    String name = "";

    StepResults(){}

    StepResults(int[] array){
        try {
            el1  = Integer.toString(array[0]);
            el2  = Integer.toString(array[1]);
            el3  = Integer.toString(array[2]);
            el4  = Integer.toString(array[3]);
            el5  = Integer.toString(array[4]);
            el6  = Integer.toString(array[5]);
            el7  = Integer.toString(array[6]);
            el8  = Integer.toString(array[7]);
            el9  = Integer.toString(array[8]);
            el10 = Integer.toString(array[9]);
            el11 = Integer.toString(array[10]);
            el12 = Integer.toString(array[11]);
            el13 = Integer.toString(array[12]);
            el14 = Integer.toString(array[13]);
            el15 = Integer.toString(array[14]);
        }catch(ArrayIndexOutOfBoundsException e){}
    }

    public String getEl1(){return el1;}
    public String getEl2(){return el2;}
    public String getEl3(){return el3;}
    public String getEl4(){return el4;}
    public String getEl5(){return el5;}
    public String getEl6(){return el6;}
    public String getEl7(){return el7;}
    public String getEl8(){return el8;}
    public String getEl9(){return el9;}
    public String getEl10(){return el10;}
    public String getEl11(){return el11;}
    public String getEl12(){return el12;}
    public String getEl13(){return el13;}
    public String getEl14(){return el14;}
    public String getEl15(){return el15;}
    public String getName(){return name;}

    public StepResults setName(String name){this.name = name; return this;}
}

