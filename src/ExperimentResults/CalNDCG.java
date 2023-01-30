package ExperimentResults;

import Main.WSDMExperiment;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalNDCG {
    /**
     * 输出5行，每行两个元素（nDCG@10. nDCG@20）
     * @param resultFile result file
     * @param answerFile ground truth file
     */
    public static void process(String resultFile, String answerFile){
        double[] ndcg10 = new double[5];
        double[] ndcg20 = new double[5];
        File resultfile = new File(resultFile);
        File answerfile = new File(answerFile);
        try {
            FileReader fr = new FileReader(resultfile);
            BufferedReader reader = new BufferedReader(fr);
            List<String> contents = new ArrayList<>();
            String content;
            while((content = reader.readLine()) != null){
                contents.add(content);
            }

            FileReader fra = new FileReader(answerfile);
            BufferedReader readera = new BufferedReader(fra);
            String str;
            int i = 0;
            while((str = readera.readLine()) != null){
                if((i+2) % 2 == 1){
                    Map<Integer, Integer> anserMap = new HashMap<>();
                    List<Integer> answerList = new ArrayList<>();
                    String[] ss = str.split("\t");
                    for(String s : ss){
                       // System.out.println(s);
                        String[] pair = s.split(":");
                        anserMap.put(Integer.parseInt(pair[0]), Integer.parseInt(pair[1]));
                        answerList.add(Integer.parseInt(pair[0]));
                    }
                    anserMap = WSDMExperiment.sortByValue(anserMap);
                    List<Integer> examples = new ArrayList<>();
                    examples.addAll(answerList.subList(0, 5));

                    int lineNum = (i/2)*5;

                    for(int j = 0; j < 5; j ++){
                        String content_j = contents.get(j + lineNum);
                        List<Integer> results = new ArrayList<>();
                        String[] answers = content_j.split("\t");
                        for(String a : answers){
                            results.add(Integer.parseInt(a));
                        }
                        // 10 queries for each group, that's why we have to divide it by 10
                        ndcg10[j] += WSDMExperiment.calculateNDCG(examples.subList(0, j + 1), results, anserMap, 10)/10;
                        ndcg20[j] += WSDMExperiment.calculateNDCG(examples.subList(0, j + 1), results, anserMap, 20)/10;
                    }


                }
                i ++;
            }




        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < 5; i ++){
            System.out.println(ndcg10[i] + ";" + ndcg20[i]);
        }
    }

    public static void main(String[] args){
        process("lzy11b.txt","单步单条布尔.txt");
        System.out.println("___________________________");
        process("lzy21o.txt","多步单条排序.txt");
        System.out.println("___________________________");
        process("lzy21b.txt","多步单条布尔.txt");
        System.out.println("___________________________");
        process("lzy22b.txt","多步多条布尔.txt");
        System.out.println("yago");
        System.out.println("___________________________");
        process("yagoQueries/yago11b.txt","yagoQueries/单步单条布尔.txt");
        System.out.println("___________________________");
        process("yagoQueries/yago21o.txt","yagoQueries/多步单条排序.txt");
        System.out.println("___________________________");
        process("yagoQueries/yago21b.txt","yagoQueries/多步单条布尔.txt");
        System.out.println("___________________________");
        process("yagoQueries/yago22b.txt","yagoQueries/多步多条布尔.txt");
    }
}
