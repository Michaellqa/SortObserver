
public class SortingResults {

    private String title;
    private int comparisons = 0;
    private int readings = 0;
    private int writings = 0;
    private int time = 0;


    SortingResults(String title, int comps, int r, int w, int time)
    {
        this.title = title;
        comparisons = comps;
        readings = r;
        writings = w;
        this.time = time;
    }

    public String getTitle(){
        return title;
    }
    public int getComparisons(){
        return comparisons;
    }
    public int getWritings() {
        return writings;
    }
    public int getReadings() {
        return readings;
    }
    public int getTime() {
        return time;
    }

}
