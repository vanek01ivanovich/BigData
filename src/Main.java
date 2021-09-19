import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static final String inputString1 = "Bus Car Train ";
    public static final String inputString2 = "Train Plane Car ";
    public static final String inputString3 = "Bus Bus Plane";
    public static List<String> inputValues = new ArrayList<>();
    public static List<ArrayList<String>> splitValues = new ArrayList<>();
    public static List<ArrayList<HashMap<String, Integer>>> mappedValues = new ArrayList<>();
    public static List<ArrayList<HashMap<String, Integer>>> shuffledValues = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        inputValues.add(inputString1);
        inputValues.add(inputString2);
        inputValues.add(inputString3);
        splitting();
        System.out.println(splitValues);
        mapping();
        System.out.println(mappedValues);
        shuffle();
        System.out.println(shuffledValues);
        makeFilesForReducing();
    }

    public static void makeFilesForReducing() {
        shuffledValues.forEach(value -> {
            if (!value.isEmpty()){
                String keyName = value.get(0).keySet().stream().findFirst().orElse("");
                if (!keyName.equalsIgnoreCase("")){
                    File file = new File(keyName + ".txt");
                    try {
                        FileWriter fileWriter = new FileWriter(file);
                        IntStream.range(0,value.size()).forEach(i -> {
                            try {
                                fileWriter.write(keyName + " , " + value.get(0).values().stream().findFirst().orElse(0) + "\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void mapping() {
        splitValues.forEach(splitValue -> {
            mappedValues.add(new ArrayList<>(splitValue.stream()
                    .map(value -> new HashMap<String, Integer>() {{
                        put(value, 1);
                    }})
                    .collect(Collectors.toList())));
        });
    }

    public static void shuffle() {
        List<HashMap<String, Integer>> allValues = new ArrayList<>();
        mappedValues.forEach(allValues::addAll);
        allValues.sort(Comparator.comparing(map -> map.keySet().stream().findFirst().orElse("")));
        allValues.forEach(value -> {
            if (shuffledValues.isEmpty()) {
                ArrayList<HashMap<String, Integer>> temp = new ArrayList<>();
                temp.add(value);
                shuffledValues.add(temp);
            } else {
                AtomicInteger wasAdded = new AtomicInteger();
                shuffledValues.forEach(shuffleValue -> {
                    if (shuffleValue.contains(value)) {
                        wasAdded.getAndIncrement();
                        shuffleValue.add(value);
                    }
                });
                ArrayList<HashMap<String, Integer>> tempList = new ArrayList<>();
                if (wasAdded.get() == 0) {
                    tempList.add(value);
                }
                wasAdded.set(0);
                if (!tempList.isEmpty()) {
                    shuffledValues.add(tempList);
                }
            }
        });
    }

    public static void splitting() {
        inputValues.forEach(value -> {
            splitValues.add(new ArrayList<>(Arrays.asList(value.split(" "))));
        });
    }
}