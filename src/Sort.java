import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Sort {

    private static String fileA = "a";
    private static int comps = 0, r = 0, w = 0; // remove

    private static int actualArraySize = 100;
    private static int seriaSize;
    public static String unsortedFile = "unsorted";
    public static final String sortedFile = "sorted";

    public  static final int demoArraySize = 15;
    private static String unsortedDemoFile = "unsortedDemo";

    // ######### Array creation ##########
    public static void updateUnsortedArray(boolean forDemo, int size)
    {
        int arraySize, maxValue;
        String fileName;
        Random random = new Random();

        if (forDemo == true) {
            arraySize = demoArraySize;
            fileName = unsortedDemoFile;
            maxValue = 100;
        } else {
            arraySize = actualArraySize = size;
            fileName = unsortedFile;
            maxValue = 1000;
        }

        try (DataOutputStream fileWriter = new DataOutputStream(new FileOutputStream(fileName))) {
            for (int i = 0 ; i < arraySize ; i++){
                int n = random.nextInt(maxValue);
                fileWriter.writeInt(n);
            }
        }catch(IOException e){ e.printStackTrace();}
    }

    // ######### Inspector ##########
    public static int[] getSubArray(int fromNum, int toNum, String filename)
    {
        if (toNum >= actualArraySize) toNum = actualArraySize - 1;
        int[] array = new int[toNum - fromNum + 1];
        int counter = 0;
        try (DataInputStream reader  = new DataInputStream(new FileInputStream(filename)))
        {
            int i = 0;
            while (i < fromNum) {
                reader.readInt();
                i++;
            }
            while (i <= toNum) {
                array[counter++] = reader.readInt();
                i++;
            }
        } catch(IOException e){}
        return array;
    }

    // ######### Demo ##########
    public static ArrayList<StepResults> getSingleStepList()
    {
        updateUnsortedArray(true, 15);
        copyFile(unsortedDemoFile, fileA);
        ArrayList<StepResults> stepList = new ArrayList<>();
        stepList.add(new StepResults(getArrayFromFile(fileA)));

        int seekPosition = demoArraySize;

        while ( seekPosition > 0) {

            seekPosition -= 3;
            int[] sortedArray = getSortedSeria(seekPosition, 3);
            merge(sortedArray, seekPosition);
            stepList.add(new StepResults(sortedArray));
            stepList.add(new StepResults(getArrayFromFile(fileA)));
        }

        return stepList;
    }

    // ########## Statistics ##########
    public static SortingResults getStatList(int arraySize, int memoryValue)
    {
        updateUnsortedArray(false, arraySize);
        return getSortingStats(memoryValue);
    }

    public static ArrayList<SortingResults> getFullRangeStats(int size)
    {
        updateUnsortedArray(false, size);
        ArrayList<SortingResults> list = new ArrayList<>();

        for (int memory = 1 ; memory <= 10 ; memory++) {
            list.add(getSortingStats(memory));
            copyFile(fileA, ("H" + memory));
        }
        return list;
    }

    private static SortingResults getSortingStats(int memoryValue)
    {
        copyFile(unsortedFile, fileA);

        seriaSize = actualArraySize * memoryValue / 100;
        if (seriaSize == 0) {
            seriaSize = 1;
        }

        String title = actualArraySize + " / " + memoryValue + "%";
        comps = r = w = 0;

        int time = (int)System.currentTimeMillis();
        executeSorting();
        time = (int)(System.currentTimeMillis() - time);

        copyFile(fileA, sortedFile);

        return new SortingResults(title, comps, r, w, time);
    }

    private static void executeSorting()
    {
        int seekPosition = actualArraySize;

        while ( seekPosition >= 0) {

            seekPosition -= seriaSize;
            int[] sortedArray = getSortedSeria(seekPosition, seriaSize);
            merge(sortedArray, seekPosition);
        }

        int tail = seekPosition + seriaSize;
        if (tail > 0) {
            int[] sortedArray = getSortedSeria(0, tail);
            merge(sortedArray, 0);
        }
    }

    private static int[] getSortedSeria(int startIndex, int size)
    {
        int[] array = new int[size];
        try (RandomAccessFile file = new RandomAccessFile(fileA, "r")) {

            file.seek(startIndex * 4); // 4 bytes per int
            for (int i = 0 ; i < size ; i++) {
                int value = file.readInt();
                array[i] = value;
                r++;
            }
        } catch (IOException e){}

        return executeInnerSort(array);
    }

    private static int[] executeInnerSort(int[] array)
    {
        for (int i = 1 ; i < array.length ; i++) {
            int temp = array[i];

            int j = i - 1;
            for ( ; j >= 0 && array[j] > temp ; j--) {
                array[j + 1] = array[j];
            }
            array[j+1] = temp;
        }
        return array;
    }

    private static void merge(int[] sortedArray, int index)
    {
        /*
         имеем отсортированный массив и индекс элемента с которого начинается чтение
         сохраняем значение считанного элемента

         сравниваем его с первым элементом массива
         пишем меньшее значение в файл на место (считанный элемент минус размер массива)
         */

        int fileValue = 999979;
        try ( RandomAccessFile fileR = new RandomAccessFile(fileA, "r");
              RandomAccessFile fileW = new RandomAccessFile(fileA, "rw")
        ){
            try {
                fileR.seek((index + sortedArray.length) * 4);
                fileValue = fileR.readInt();
                r++;
            } catch (IOException e){}

            fileW.seek((index) * 4);

            for (int i = 0 ; i < sortedArray.length ; ) {

                try {
                    if (fileValue < sortedArray[i]) {
                        fileW.writeInt(fileValue);
                        fileValue = fileR.readInt(); // закончились элементы в хвосте файла -> дописываем сортированный массив
                        r++;
                    } else {
                        fileW.writeInt(sortedArray[i]);
                        i++;
                    }
                } catch (IOException e){
                    fileValue = 999979;
                }
                w++;
                comps++;
            }

        } catch (IOException e){}
    }

    // ######### Auxiliary functions ##########

    private static int[] getArrayFromFile(String filename)
    {
        int[] array = new int[demoArraySize];
        int i = 0;
        try (DataInputStream reader = new DataInputStream(new FileInputStream(filename)))
        {
            int value = reader.readInt();
            while (true)
            {
                array[i++] = value;
                value = reader.readInt();
            }
        } catch(IOException e){}

        int[] res = new int[i];
        for (int j = 0 ; j < i ; j++)
            res[j] = array[j];

        return res;
    }

    private static void copyFile(String from, String to)
    {
        try (
                DataInputStream reader = new DataInputStream(new FileInputStream(from));
                DataOutputStream writer = new DataOutputStream(new FileOutputStream(to))
        ) {
            while(true){
                writer.writeInt(reader.readInt());
            }
        }catch(IOException e){}
    }

}
