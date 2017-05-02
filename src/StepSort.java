
import java.util.Random;

public class StepSort {

    private static int SIZE = 10;
    private static final int MAX_VALUE = 100;
    private static int[] array;
    private static int[][] res;

    private static void swap(int i, int j) {

        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private static void setArray(int size) {

        array = new int[size];
        Random rand = new Random();
        for (int i=0 ; i < size ; i++){
            array[i] = Math.abs(rand.nextInt() % MAX_VALUE);
        }
    }
    private static void setArray(int size, int max) {

        array = new int[size];
        Random rand = new Random();
        for (int i=0 ; i < size ; i++){
            array[i] = Math.abs(rand.nextInt() % max);
        }
    }

    private static void append(int i, int size){

        res[i] = new int[size];
        for (int n=0 ; n < size ; n++){
            res[i][n] = array[n];
        }
    }

    private static String[][] convert(int[][] ints){

        int size = ints[0].length;
        String[][] res = new String[ints.length + 1][size];
        for (int i = 0 ; i < size ; i++){
            res[0][i] = String.valueOf(i);
        }
        for (int i=1 ; i <= ints.length ; i++) {
            if (ints[i-1] == null)
                break;
            for (int j = 0; j < size; j++) {
                res[i][j] = String.valueOf(ints[i-1][j]);
            }
        }
        return res;
    }

    // >> Обмен <<
    public static String[][] doBubbleSort() {

        res = new int[SIZE][];
        int higherIndex;
        int k = 0;
        boolean stop = false;

        setArray(SIZE);

        for (int m = 10 ; m > 1 && !stop ; m--) {

            append(k,SIZE);
            k++;

            stop = true;
            for (int i = 0 ; i < SIZE-1 ; i++) {
                higherIndex = i + 1;
                if (array[i] > array[higherIndex]) {
                    stop = false;
                    swap(i, higherIndex);
                }
            }
        }
        append(k,SIZE);

        return convert(res);
    }
    // >> Выбор <<
    public static String[][] doSelectionSort(){

        res = new int[SIZE][];
        int index_min, j;
        setArray(SIZE);

        for ( j=0; j < SIZE-1; j++)
        {
            append(j,SIZE);
            index_min = j;
            for (int i = j+1 ; i < SIZE ; i++){
                if (array[i] < array[index_min]){
                    index_min = i;
                }
             }
            swap(index_min, j);
        }
        append(j,SIZE);

        return convert(res);
    }
    // >> Вставка <<
    public static String[][] doInsertionSort(){

        array = new int[SIZE + 1];
        Random rand = new Random();
        for (int i=1 ; i < SIZE + 1 ; i++){
            array[i] = Math.abs(rand.nextInt() % MAX_VALUE);
        }

        int j, i;
        res = new int[SIZE+1][];

        for (i = 2 ; i < SIZE+1 ; i++) {
            append(i-2, SIZE+1);
            array[0] = array[i];

            for (j = i ; array[j - 1] > array[0] ; j--) {

                array[j] = array[j - 1];
            }
            array[j] = array[0];
        }
        append(i-2, SIZE+1);
        return convert(res);
    }


    // >> Шелл <<
    public static String[][] doShellSort() {

        int Size = 20;
        int inner, outer;
        int temp;
        setArray(Size);

        int h = 0;
        int t = 1;
        for (int i = array.length ; i/2 > 1 ; i /= 2) {
            h = h * 2 + 1;
            t++;
        }
        res = new int[t][];

        int i = 0;
        for ( ; h > 0 ; h = (h - 1) / 2) {

            append(i,Size);
            i++;

            for (outer = h; outer < array.length; outer++) {
                temp = array[outer];
                inner = outer;

                while (inner > h - 1 && array[inner - h] >= temp) {
                    array[inner] = array[inner - h];
                    inner -= h;
                }
                array[inner] = temp;
            }
        }
        append(i, Size);
        return convert(res);
    }

    // >> Линейная <<
    public static String[][] doLinedSort(){

        res = new int[3][];
        int SIZE = 10;
        int[] auxArray = new int[SIZE];

        res = new int[3][];
        setArray(SIZE, SIZE);

        for (int i = 0 ; i < SIZE ; i++) {

            auxArray[array[i]]++;
        }
        append(0, SIZE);
        int k, i = 0;

        for (int j = 0; j < SIZE ; j++) {
            for (k = 0; k < auxArray[j]; k++) {

                array[i] = j;
                i++;
            }
        }

        res[1] = new int[SIZE];
        for (int n=0 ; n < SIZE ; n++){
            res[1][n] = auxArray[n];
        }
        append(2, SIZE);
        return convert(res);
    }
}
