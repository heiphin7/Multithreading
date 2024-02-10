import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Semaphore;

/*
* Поиск строки в текстовом файле с использованием многопоточности.
* Я думаю что данный метод будет эффективнее чем просто поиск через contains()
* так как в данном случае, 2 потока будут паралельно искать цель, сокращяя время работы
* программы вдвое.
*
*/

public class Main{
    public static void main(String[] args) throws IOException {
        // Поток входных данные для чтения пути к файлу и строки, которую нужно найти
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String fileName = bufferedReader.readLine();
        String target = bufferedReader.readLine();
        // После считывания данных закрываем поток
        bufferedReader.close();

        // Используем команду readAllLines и записываем все данные в список lines
        List<String> lines = Files.readAllLines(Paths.get(fileName));

        // Делим список lines для двух потоко поровну
        /* По названию понятно, что
        *  firstHalf - первая половина
        *  secondHalf - вторая половину
        */
        String firstHalf = String.valueOf(lines.subList(0, lines.size() / 2));
        String secondHalf = String.valueOf(lines.subList(lines.size()/ 2, lines.size()));


        // Создаем новые потоки из созданных классов
        Thread firstThread = new Thread(new FirstThread(firstHalf, target));
        Thread secondThread = new Thread(new SecondThread(secondHalf, target));

        // Запускаем потоки паралельно
        firstThread.start();
        secondThread.start();
    }

    static class FirstThread implements Runnable{
        String text;
        String target;
        public boolean isHave;
        // Конструктор класса
        public FirstThread(String text, String target){
            this.text = text;
            this.target = target;
        }

        @Override
        public void run() {
            // Тут мы используем паралельный поиск подстроки через contains
            isHave = text.contains(target);
            // Исходя из результатов выводим результат
            if(isHave){
                System.out.println("Цель найдена в первой половине файла");
            }else{
                System.out.println("Цель не была найдена в первой половину файла");
            }
        }
    }

    static class SecondThread implements Runnable{
        String text;
        String target;
        public  boolean isHave;
        // Конструктор класса
        public SecondThread(String text, String target){
            this.text = text;
            this.target = target;

        }

        @Override
        public void run() {
            // Тут мы используем паралельный поиск подстроки через contains
            isHave = text.contains(target);
            // Исходя из результатов выводим результат
            if(isHave){
                System.out.println("Цель была найдена во второй половине файла");
            }else{
                System.out.println("Цель не найдена во второй половине");
            }
        }

    }
}